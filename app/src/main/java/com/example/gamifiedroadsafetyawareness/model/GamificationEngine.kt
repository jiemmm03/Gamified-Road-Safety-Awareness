package com.example.gamifiedroadsafetyawareness.model

import androidx.room.withTransaction
import com.example.gamifiedroadsafetyawareness.model.db.AppDatabase
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.model.db.XpHistoryEntity
import com.example.gamifiedroadsafetyawareness.model.db.AchievementUnlockEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptAnswerEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/** Parses one of the CSV id-set columns on user_progress into a set, tolerating blanks. */
fun String.splitCsv(): Set<String> =
    split(",").filter { it.isNotBlank() }.toSet()

/**
 * Central XP-awarding engine. Every reward path (module, quiz, simulation, time challenge)
 * routes through [finalizeAward], which applies the daily-streak state machine, persists an
 * xp_history row, updates user_progress, and evaluates achievements — all in one transaction.
 */
class GamificationEngine(private val db: AppDatabase) {

    /**
     * Awards module-completion XP once per module. Returns null if [moduleId] was already
     * completed by this user, so repeat taps can't farm XP.
     *
     * Uses per-module XP from [GamificationConstants.ModuleXp.getModuleXp] which is
     * admin-configurable, falling back to the module type XP if module ID isn't configured.
     */
    suspend fun awardModuleCompletion(
        userId: String,
        moduleId: String,
        moduleType: ModuleType,
        moduleTitle: String,
        isPerfect: Boolean = false
    ): XpAwardResult? {
        val existing = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        if (moduleId in existing.completedModuleIds.splitCsv()) return null

        val moduleXp = GamificationConstants.ModuleXp.getModuleXp(moduleId)
        val perfectBonus = if (isPerfect) GamificationConstants.MODULE_COMPLETION_PERFECT_BONUS else 0

        return finalizeAward(
            userId = userId,
            activityType = "MODULE_${moduleType.name}",
            activityName = moduleTitle,
            baseXp = moduleXp,
            modulePerfectBonusXp = perfectBonus
        ) { progress ->
            val completed = progress.completedModuleIds.splitCsv().toMutableSet()
            completed.add(moduleId)
            progress.copy(completedModuleIds = completed.joinToString(","))
        }
    }

    /**
     * @param comboXpEarned base+streak-bonus+multiplier XP already accumulated live during the quiz
     *   (computed question-by-question in QuizScreen, since the multiplier compounds per-question).
     */
    suspend fun awardQuizCompletion(
        userId: String,
        correctAnswers: Int,
        totalQuestions: Int,
        comboXpEarned: Int,
        bestComboStreak: Int,
        timeChallengeCompleted: Boolean,
        quizId: String,
        quizTitle: String
    ): XpAwardResult {
        val existingProgress = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        val isFirstAttempt = quizId !in existingProgress.attemptedQuizIds.splitCsv()
        val isPerfect = totalQuestions > 0 && correctAnswers == totalQuestions
        val scorePercent = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0

        val perfectBonus = if (isPerfect) GamificationConstants.QuizXp.PERFECT_SCORE else 0
        val completionBonus = GamificationConstants.QuizXp.COMPLETION
        val firstAttemptBonus = if (isPerfect && isFirstAttempt) GamificationConstants.QuizXp.FIRST_ATTEMPT_PERFECT else 0
        val timeChallengeBonus = if (timeChallengeCompleted) GamificationConstants.QuizXp.TIME_CHALLENGE else 0
        val baseXp = correctAnswers * GamificationConstants.QuizXp.CORRECT_ANSWER
        val streakBonus = (comboXpEarned - baseXp).coerceAtLeast(0)

        return finalizeAward(
            userId = userId,
            activityType = "QUIZ_COMPLETION",
            activityName = quizTitle,
            baseXp = baseXp,
            streakBonusXp = streakBonus,
            perfectBonusXp = perfectBonus,
            completionBonusXp = completionBonus,
            firstAttemptBonusXp = firstAttemptBonus,
            timeChallengeBonusXp = timeChallengeBonus,
            multiplier = 1.0f, // multiplier is already baked into comboXpEarned; don't reapply here
            streakCount = bestComboStreak,
            achievementContext = AchievementContext(bestComboStreak = bestComboStreak, quizScorePercent = scorePercent)
        ) { progress ->
            val attempted = progress.attemptedQuizIds.splitCsv().toMutableSet()
            attempted.add(quizId)
            val highScores = progress.highScoreQuizIds.splitCsv().toMutableSet()
            if (scorePercent >= 90) highScores.add(quizId)

            progress.copy(
                quizzesCompleted = progress.quizzesCompleted + 1,
                perfectQuizCount = progress.perfectQuizCount + if (isPerfect) 1 else 0,
                attemptedQuizIds = attempted.joinToString(","),
                highScoreQuizIds = highScores.joinToString(","),
                bestAnswerStreak = maxOf(progress.bestAnswerStreak, bestComboStreak),
                completedQuestionsCount = progress.completedQuestionsCount + correctAnswers
            )
        }
    }

    suspend fun awardSimulationDecision(
        userId: String,
        tier: SimulationTier,
        aiRecommendationFollowed: Boolean,
        scenarioTitle: String
    ): XpAwardResult {
        val tierXp = when (tier) {
            SimulationTier.SAFE_DECISION -> GamificationConstants.SimulationXp.SAFE_DECISION
            SimulationTier.EXCELLENT_DECISION -> GamificationConstants.SimulationXp.EXCELLENT_DECISION
            SimulationTier.PERFECT_SIMULATION -> GamificationConstants.SimulationXp.PERFECT_SIMULATION
        }
        val aiBonus = if (aiRecommendationFollowed) GamificationConstants.SimulationXp.AI_RECOMMENDATION_FOLLOWED else 0

        return finalizeAward(
            userId = userId,
            activityType = "SIMULATION_DECISION",
            activityName = scenarioTitle,
            baseXp = tierXp,
            aiRecommendationBonusXp = aiBonus
        ) { progress ->
            progress.copy(scenariosCompleted = progress.scenariosCompleted + 1)
        }
    }

    /**
     * Awards XP for one correct answer given during an ad-hoc AI tutor practice question, as
     * opposed to a full QuizScreen quiz completion. Wrong answers earn no XP and aren't
     * recorded here — the caller records the missed topic separately. Reuses [finalizeAward]
     * so streaks/level-ups/achievements still evaluate normally, without touching
     * quizzesCompleted/attemptedQuizIds (this isn't a real quiz attempt).
     */
    suspend fun awardTutorPracticeAnswer(userId: String, questionLabel: String): XpAwardResult =
        finalizeAward(
            userId = userId,
            activityType = "AI_TUTOR_PRACTICE",
            activityName = questionLabel,
            baseXp = GamificationConstants.QuizXp.CORRECT_ANSWER
        )

    /**
     * Records one completed quiz attempt for Module Summary/Review/History and admin analytics.
     * A separate transaction from [finalizeAward]/[awardQuizCompletion] — it runs after XP has
     * already been awarded and only consumes the [XpAwardResult] that call returned, so the
     * existing reward/streak math is untouched. `attemptNumber` is computed here (not trusted
     * from the caller) so it stays correct regardless of client-side timing.
     */
    suspend fun recordQuizAttempt(
        userId: String,
        quizId: String,
        moduleId: String,
        quizTitle: String,
        difficulty: ModuleType,
        answers: List<QuestionAnswerRecord>,
        timeSpentSeconds: Int,
        startedAt: Long,
        bestComboStreak: Int,
        timeChallengeCompleted: Boolean,
        awardResult: XpAwardResult,
        passThresholdPercent: Int
    ): QuizAttemptEntity = db.withTransaction {
        val attemptNumber = db.quizAttemptDao().countAttempts(userId, quizId) + 1
        val correctCount = answers.count { it.isCorrect }
        val unansweredCount = answers.count { it.selectedOptionIndex == -1 }
        val incorrectCount = answers.size - correctCount - unansweredCount
        val scorePercent = if (answers.isNotEmpty()) (correctCount * 100) / answers.size else 0

        val attempt = QuizAttemptEntity(
            userId = userId,
            quizId = quizId,
            moduleId = moduleId,
            quizTitle = quizTitle,
            difficulty = difficulty.name,
            attemptNumber = attemptNumber,
            totalQuestions = answers.size,
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            unansweredCount = unansweredCount,
            scorePercent = scorePercent,
            passed = scorePercent >= passThresholdPercent,
            xpEarned = awardResult.totalAwarded,
            bestComboStreak = bestComboStreak,
            leveledUp = awardResult.leveledUp,
            levelAfter = awardResult.newLevel,
            timeSpentSeconds = timeSpentSeconds,
            timeChallengeCompleted = timeChallengeCompleted,
            startedAt = startedAt
        )
        val attemptId = db.quizAttemptDao().insertAttempt(attempt)
        db.quizAttemptDao().insertAnswers(
            answers.map { answer ->
                QuizAttemptAnswerEntity(
                    attemptId = attemptId,
                    quizId = quizId,
                    questionId = answer.questionId,
                    questionNumber = answer.questionNumber,
                    questionText = answer.questionText,
                    selectedOptionIndex = answer.selectedOptionIndex,
                    correctOptionIndex = answer.correctOptionIndex,
                    selectedAnswerText = answer.selectedAnswerText,
                    correctAnswerText = answer.correctAnswerText,
                    isCorrect = answer.isCorrect,
                    pointsEarned = answer.pointsEarned,
                    xpEarned = answer.xpEarned,
                    explanation = answer.explanation,
                    safetyTip = answer.safetyTip,
                    difficulty = answer.difficulty,
                    topic = answer.topic,
                    answeredAt = answer.answeredAt
                )
            }
        )
        val savedAttempt = attempt.copy(id = attemptId)
        try {
            FirebaseSyncManager.getInstance().syncQuizAttempt(savedAttempt)
        } catch (_: Exception) {}
        savedAttempt
    }

    /**
     * Admin-only manual XP adjustment. Creates a full audit trail entry.
     */
    suspend fun awardManualAdjustment(
        userId: String,
        xpDelta: Int,
        adminNote: String,
        adminUserId: String
    ): XpAwardResult = db.withTransaction {
        val existing = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        val previousLevel = existing.currentLevel
        val previousXp = existing.totalXp

        val newTotalXp = (existing.totalXp + xpDelta).coerceAtLeast(0)
        val newLevel = GamificationConstants.getLevelForXp(newTotalXp)

        val updated = existing.copy(
            totalXp = newTotalXp,
            currentXp = GamificationConstants.getCurrentLevelXp(newTotalXp, newLevel),
            currentLevel = newLevel,
            updatedAt = System.currentTimeMillis()
        )
        db.userProgressDao().upsert(updated)
        try {
            FirebaseSyncManager.getInstance().syncUserProgress(
                progress = updated,
                displayName = userId
            )
        } catch (_: Exception) {}

        db.xpHistoryDao().insert(
            XpHistoryEntity(
                userId = userId,
                activityType = "ADMIN_ADJUSTMENT",
                activityName = "Manual XP adjustment by $adminUserId",
                xpEarned = if (xpDelta > 0) xpDelta else 0,
                bonusXp = 0,
                multiplier = 1.0f,
                streakCount = 0,
                totalAwarded = xpDelta,
                previousXp = previousXp,
                newXp = newTotalXp,
                previousLevel = previousLevel,
                newLevel = newLevel,
                adminNote = adminNote
            )
        )

        XpAwardResult(
            baseXp = xpDelta,
            totalAwarded = xpDelta,
            totalXp = newTotalXp,
            previousLevel = previousLevel,
            newLevel = newLevel
        )
    }

    private suspend fun finalizeAward(
        userId: String,
        activityType: String,
        activityName: String,
        baseXp: Int,
        streakBonusXp: Int = 0,
        perfectBonusXp: Int = 0,
        completionBonusXp: Int = 0,
        firstAttemptBonusXp: Int = 0,
        timeChallengeBonusXp: Int = 0,
        aiRecommendationBonusXp: Int = 0,
        modulePerfectBonusXp: Int = 0,
        multiplier: Float = 1.0f,
        streakCount: Int = 0,
        achievementContext: AchievementContext = AchievementContext(),
        progressUpdater: (UserProgressEntity) -> UserProgressEntity = { it }
    ): XpAwardResult = db.withTransaction {
        val existing = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        val daily = applyDailyStreak(existing)
        val withActivityFields = progressUpdater(existing)

        val comboTotal = ((baseXp + streakBonusXp) * multiplier).toInt()
        val flatBonuses = perfectBonusXp + completionBonusXp + firstAttemptBonusXp +
            timeChallengeBonusXp + aiRecommendationBonusXp + modulePerfectBonusXp
        val totalBeforeAchievements = comboTotal + flatBonuses + daily.bonusXp

        val previousLevel = existing.currentLevel
        val previousXp = existing.totalXp
        val totalXpBeforeAchievements = existing.totalXp + totalBeforeAchievements
        val levelBeforeAchievements = GamificationConstants.getLevelForXp(totalXpBeforeAchievements)

        var updated = withActivityFields.copy(
            totalXp = totalXpBeforeAchievements,
            currentXp = GamificationConstants.getCurrentLevelXp(totalXpBeforeAchievements, levelBeforeAchievements),
            currentLevel = levelBeforeAchievements,
            currentStreak = daily.newCurrentStreak,
            longestStreak = daily.newLongestStreak,
            dailyStreak = daily.newCurrentStreak,
            lastActivityDate = daily.newLastActivityDate,
            streakFreezes = daily.newFreezes,
            multiplier = multiplier,
            updatedAt = System.currentTimeMillis()
        )

        val (unlocked, achievementBonus) = evaluateAchievements(userId, updated, achievementContext)
        var finalTotalXp = totalXpBeforeAchievements
        var finalLevel = levelBeforeAchievements
        if (achievementBonus > 0) {
            finalTotalXp += achievementBonus
            finalLevel = GamificationConstants.getLevelForXp(finalTotalXp)
            updated = updated.copy(
                totalXp = finalTotalXp,
                currentXp = GamificationConstants.getCurrentLevelXp(finalTotalXp, finalLevel),
                currentLevel = finalLevel
            )
        }

        db.userProgressDao().upsert(updated)
        try {
            FirebaseSyncManager.getInstance().syncUserProgress(
                progress = updated,
                displayName = userId,
                unlockedAchievementIds = unlocked.map { it.id }
            )
        } catch (_: Exception) {}

        val totalAwarded = totalBeforeAchievements
        db.xpHistoryDao().insert(
            XpHistoryEntity(
                userId = userId,
                activityType = activityType,
                activityName = activityName,
                xpEarned = baseXp,
                bonusXp = totalAwarded - baseXp,
                multiplier = multiplier,
                streakCount = streakCount,
                totalAwarded = totalAwarded,
                previousXp = previousXp,
                newXp = finalTotalXp,
                previousLevel = previousLevel,
                newLevel = finalLevel
            )
        )
        if (achievementBonus > 0) {
            db.xpHistoryDao().insert(
                XpHistoryEntity(
                    userId = userId,
                    activityType = "ACHIEVEMENT_UNLOCK",
                    activityName = unlocked.joinToString(", ") { it.title },
                    xpEarned = 0,
                    bonusXp = achievementBonus,
                    multiplier = 1.0f,
                    streakCount = 0,
                    totalAwarded = achievementBonus,
                    previousXp = totalXpBeforeAchievements,
                    newXp = finalTotalXp,
                    previousLevel = levelBeforeAchievements,
                    newLevel = finalLevel
                )
            )
        }

        XpAwardResult(
            baseXp = baseXp,
            streakBonusXp = comboTotal - baseXp,
            perfectBonusXp = perfectBonusXp,
            completionBonusXp = completionBonusXp,
            firstAttemptBonusXp = firstAttemptBonusXp,
            timeChallengeBonusXp = timeChallengeBonusXp,
            aiRecommendationBonusXp = aiRecommendationBonusXp,
            dailyStreakBonusXp = daily.bonusXp,
            streakProtected = daily.streakProtected,
            multiplierApplied = multiplier,
            achievementsUnlocked = unlocked,
            achievementBonusXp = achievementBonus,
            totalAwarded = totalAwarded + achievementBonus,
            totalXp = finalTotalXp,
            previousLevel = previousLevel,
            newLevel = finalLevel,
            streakFreezeEarned = daily.freezeEarned,
            currentDailyStreak = daily.newCurrentStreak,
            modulePerfectBonusXp = modulePerfectBonusXp
        )
    }

    private suspend fun evaluateAchievements(
        userId: String,
        progress: UserProgressEntity,
        context: AchievementContext
    ): Pair<List<AchievementDefinition>, Int> {
        val unlockedIds = db.achievementUnlockDao().getUnlockedIds(userId).toSet()
        val newlyUnlocked = mutableListOf<AchievementDefinition>()
        var bonusTotal = 0
        for (definition in AchievementDefinitions.ALL) {
            if (definition.id in unlockedIds) continue
            if (definition.condition(progress, context)) {
                newlyUnlocked.add(definition)
                bonusTotal += GamificationConstants.AchievementXp.getBonusXp(definition.id, definition.bonusXp)
                db.achievementUnlockDao().insert(AchievementUnlockEntity(userId = userId, achievementId = definition.id))
            }
        }
        return newlyUnlocked to bonusTotal
    }

    private data class DailyStreakOutcome(
        val newCurrentStreak: Int,
        val newLongestStreak: Int,
        val newFreezes: Int,
        val newLastActivityDate: String,
        val bonusXp: Int,
        val streakProtected: Boolean,
        val freezeEarned: Boolean
    )

    private fun applyDailyStreak(progress: UserProgressEntity): DailyStreakOutcome {
        val today = LocalDate.now()
        val todayStr = today.toString()
        val lastDateStr = progress.lastActivityDate

        if (lastDateStr == todayStr) {
            return DailyStreakOutcome(
                newCurrentStreak = progress.currentStreak,
                newLongestStreak = progress.longestStreak,
                newFreezes = progress.streakFreezes,
                newLastActivityDate = todayStr,
                bonusXp = 0,
                streakProtected = false,
                freezeEarned = false
            )
        }

        if (lastDateStr == null) {
            val bonus = GamificationConstants.DAILY_STREAK_REWARD_TABLE[1] ?: 0
            return DailyStreakOutcome(1, maxOf(progress.longestStreak, 1), progress.streakFreezes, todayStr, bonus, false, false)
        }

        val lastDate = LocalDate.parse(lastDateStr)
        val daysBetween = ChronoUnit.DAYS.between(lastDate, today)

        return when (daysBetween) {
            1L -> {
                val newStreak = progress.currentStreak + 1
                val newLongest = maxOf(progress.longestStreak, newStreak)
                val freezeEarned = newStreak % GamificationConstants.FREEZE_EARN_INTERVAL_DAYS == 0
                val newFreezes = progress.streakFreezes + if (freezeEarned) 1 else 0
                val bonus = GamificationConstants.DAILY_STREAK_REWARD_TABLE[newStreak] ?: 0
                DailyStreakOutcome(newStreak, newLongest, newFreezes, todayStr, bonus, false, freezeEarned)
            }
            2L -> {
                if (progress.streakFreezes > 0) {
                    DailyStreakOutcome(progress.currentStreak, progress.longestStreak, progress.streakFreezes - 1, todayStr, 0, true, false)
                } else {
                    DailyStreakOutcome(1, maxOf(progress.longestStreak, 1), progress.streakFreezes, todayStr, 0, false, false)
                }
            }
            else -> {
                DailyStreakOutcome(1, maxOf(progress.longestStreak, 1), progress.streakFreezes, todayStr, 0, false, false)
            }
        }
    }
}
