package com.example.gamifiedroadsafetyawareness.model

import android.content.Context
import android.content.SharedPreferences
import com.example.gamifiedroadsafetyawareness.model.db.AppDatabase
import com.example.gamifiedroadsafetyawareness.model.db.DifficultyPerformanceRow
import com.example.gamifiedroadsafetyawareness.model.db.GlobalAnswerStatsRow
import com.example.gamifiedroadsafetyawareness.model.db.IncorrectAnswerAnalyticsRow
import com.example.gamifiedroadsafetyawareness.model.db.ModulePerformanceRow
import com.example.gamifiedroadsafetyawareness.model.db.QuizAggregateRow
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptAnswerEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuestionErrorRateRow
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.model.db.WrongOptionRow
import com.example.gamifiedroadsafetyawareness.model.db.XpHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Facade over the Room-backed gamification engine. Normalizes usernames the same way
 * AuthManager does (trim + lowercase) so user_progress rows join cleanly against accounts.
 */
class XpManager(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val engine = GamificationEngine(db)

    private val legacyPrefs: SharedPreferences =
        context.getSharedPreferences("xp_data", Context.MODE_PRIVATE)

    init {
        GamificationConstants.init(context)
    }

    private fun key(username: String) = username.trim().lowercase()

    /** One-time, lazy migration of the old flat SharedPreferences XP value into Room. */
    private suspend fun ensureMigrated(userId: String) {
        if (db.userProgressDao().get(userId) != null) return
        val legacyXp = legacyPrefs.getInt("${userId}_xp", 0)
        val level = GamificationConstants.getLevelForXp(legacyXp)
        db.userProgressDao().upsert(
            UserProgressEntity(
                userId = userId,
                totalXp = legacyXp,
                currentXp = GamificationConstants.getCurrentLevelXp(legacyXp, level),
                currentLevel = level
            )
        )
    }

    suspend fun getProgress(username: String): UserProgressEntity {
        val userId = key(username)
        ensureMigrated(userId)
        return db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
    }

    fun observeProgress(username: String): Flow<UserProgressEntity?> =
        db.userProgressDao().observe(key(username))

    suspend fun getLeaderboardData(usernames: List<String>): List<UserProgressEntity> {
        val userIds = usernames.map { key(it) }
        userIds.forEach { ensureMigrated(it) }
        return db.userProgressDao().getAll(userIds)
    }

    suspend fun getUnlockedAchievementIds(username: String): Set<String> =
        db.achievementUnlockDao().getUnlockedIds(key(username)).toSet()

    suspend fun getWeeklyXp(usernames: List<String>): Map<String, Int> {
        val userIds = usernames.map { key(it) }
        val weekStart = LocalDate.now().minusDays(7)
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val rows = db.xpHistoryDao().getWeeklyXpForUsers(weekStart, userIds)
        return rows.associate { it.userId to it.weeklyXp }
    }

    /** Hours remaining today (local time) before the daily streak lapses if no activity happens. */
    fun hoursUntilStreakExpires(): Long {
        val now = java.time.LocalDateTime.now()
        val endOfDay = now.toLocalDate().plusDays(1).atStartOfDay()
        return ChronoUnit.HOURS.between(now, endOfDay).coerceAtLeast(0)
    }

    /** Returns null if this module was already completed (no XP re-awarded). */
    suspend fun awardModuleCompletion(
        username: String,
        moduleId: String,
        moduleType: ModuleType,
        moduleTitle: String,
        isPerfect: Boolean = false
    ): XpAwardResult? {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.awardModuleCompletion(userId, moduleId, moduleType, moduleTitle, isPerfect)
    }

    suspend fun awardQuizCompletion(
        username: String,
        correctAnswers: Int,
        totalQuestions: Int,
        comboXpEarned: Int,
        bestComboStreak: Int,
        timeChallengeCompleted: Boolean,
        quizId: String,
        quizTitle: String
    ): XpAwardResult {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.awardQuizCompletion(
            userId, correctAnswers, totalQuestions, comboXpEarned, bestComboStreak,
            timeChallengeCompleted, quizId, quizTitle
        )
    }

    suspend fun awardSimulationDecision(
        username: String,
        tier: SimulationTier,
        aiRecommendationFollowed: Boolean,
        scenarioTitle: String
    ): XpAwardResult {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.awardSimulationDecision(userId, tier, aiRecommendationFollowed, scenarioTitle)
    }

    // ── New methods for the redesigned system ──────────────────────────────────

    /** Get XP transaction history for a user. */
    suspend fun getXpHistory(username: String, limit: Int = 50): List<XpHistoryEntity> {
        val userId = key(username)
        return db.xpHistoryDao().getRecentForUser(userId, limit)
    }

    /** Get all XP history across all users (admin). */
    suspend fun getAllXpHistory(limit: Int = 100): List<XpHistoryEntity> {
        return db.xpHistoryDao().getRecentAll(limit)
    }

    /** Admin manual XP adjustment with audit trail. */
    suspend fun adjustXp(
        username: String,
        delta: Int,
        adminNote: String,
        adminUsername: String
    ): XpAwardResult {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.awardManualAdjustment(userId, delta, adminNote, key(adminUsername))
    }

    /** Get unlock status for all modules. */
    suspend fun getModuleUnlockStatus(username: String): Map<String, Pair<Boolean, String>> {
        val userId = key(username)
        val progress = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        val completedModules = progress.completedModuleIds.splitCsv()

        return GamificationConstants.MODULE_UNLOCK_REQUIREMENTS.map { (moduleId, requirement) ->
            val (unlocked, reason) = when (requirement) {
                is GamificationConstants.UnlockRequirement.None -> true to "Available"
                is GamificationConstants.UnlockRequirement.RequiresLevel -> {
                    val met = progress.currentLevel >= requirement.level
                    met to if (met) "Available" else "Requires Level ${requirement.level}"
                }
                is GamificationConstants.UnlockRequirement.RequiresModule -> {
                    val met = requirement.moduleId in completedModules
                    met to if (met) "Available" else "Complete ${getModuleTitle(requirement.moduleId)}"
                }
                is GamificationConstants.UnlockRequirement.RequiresAllModules -> {
                    val allRequired = GamificationConstants.MODULE_UNLOCK_REQUIREMENTS.keys
                        .filter { it != moduleId && it != "mod_final_challenge" }
                    val met = allRequired.all { it in completedModules }
                    met to if (met) "Available" else "Complete all previous modules"
                }
            }
            moduleId to (unlocked to reason)
        }.toMap()
    }

    private fun getModuleTitle(moduleId: String): String =
        MockData.learningModules.find { it.id == moduleId }?.title ?: moduleId

    /** Get all users' progress (admin). */
    suspend fun getAllUsersProgress(): List<UserProgressEntity> =
        db.userProgressDao().getAllSorted()

    /** Get top users by XP (admin). */
    suspend fun getTopUsersByXp(limit: Int = 20): List<UserProgressEntity> =
        db.userProgressDao().getTopByXp(limit)

    /** Achievement unlock counts across all users, keyed by achievement id (admin reports). */
    suspend fun getAchievementUnlockCounts(): Map<String, Int> =
        db.achievementUnlockDao().getUnlockCounts().associate { it.achievementId to it.count }

    /**
     * Achievement unlock counts restricted to a given set of usernames (e.g. learner accounts
     * only, excluding admins who may have accrued XP via a granted permission).
     */
    suspend fun getAchievementUnlockCounts(restrictToUsernames: Set<String>): Map<String, Int> =
        db.achievementUnlockDao().getAllUnlocks()
            .filter { it.userId in restrictToUsernames }
            .groupingBy { it.achievementId }
            .eachCount()

    /** Awards XP for one correct answer in an AI tutor practice question. See GamificationEngine.awardTutorPracticeAnswer. */
    suspend fun awardTutorPracticeAnswer(username: String, questionLabel: String): XpAwardResult {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.awardTutorPracticeAnswer(userId, questionLabel)
    }

    /**
     * Appends a topic name to the user's missed-topics history (read by the AI tutor to
     * identify weak topics worth revisiting). Not deduped — repeated misses of the same topic
     * are a meaningful signal, not noise.
     */
    suspend fun recordMissedTopic(username: String, topic: String) {
        val userId = key(username)
        ensureMigrated(userId)
        val progress = db.userProgressDao().get(userId) ?: UserProgressEntity(userId = userId)
        val existing = progress.missedTopics.split(",").filter { it.isNotBlank() }
        val updated = (existing + topic).joinToString(",")
        db.userProgressDao().upsert(progress.copy(missedTopics = updated, updatedAt = System.currentTimeMillis()))
    }

    // ── Quiz attempt records (Module Summary / Review / Learning History / admin analytics) ────

    suspend fun recordQuizAttempt(
        username: String,
        quizId: String,
        moduleId: String,
        quizTitle: String,
        difficulty: ModuleType,
        answers: List<QuestionAnswerRecord>,
        timeSpentSeconds: Int,
        startedAt: Long,
        bestComboStreak: Int,
        timeChallengeCompleted: Boolean,
        awardResult: XpAwardResult
    ): QuizAttemptEntity {
        val userId = key(username)
        ensureMigrated(userId)
        return engine.recordQuizAttempt(
            userId, quizId, moduleId, quizTitle, difficulty, answers, timeSpentSeconds, startedAt,
            bestComboStreak, timeChallengeCompleted, awardResult,
            GamificationConstants.QUIZ_PASS_THRESHOLD_PERCENT
        )
    }

    /** Own attempts only — Learning History. */
    suspend fun getAttemptsForUser(username: String): List<QuizAttemptEntity> =
        db.quizAttemptDao().getAttemptsForUser(key(username))

    suspend fun getAttempt(attemptId: Long): QuizAttemptEntity? =
        db.quizAttemptDao().getAttempt(attemptId)

    suspend fun getAnswersForAttempt(attemptId: Long): List<QuizAttemptAnswerEntity> =
        db.quizAttemptDao().getAnswersForAttempt(attemptId)

    suspend fun getHighestScorePercent(username: String, quizId: String): Int =
        db.quizAttemptDao().highestScorePercent(key(username), quizId) ?: 0

    /** Admin-only: all users' attempts. Only call from permission-gated admin screens. */
    suspend fun getAllQuizAttempts(limit: Int = 200): List<QuizAttemptEntity> =
        db.quizAttemptDao().getRecentAttemptsAll(limit)

    /** Admin-only: a specific user's attempts, looked up by an admin rather than the user themself. */
    suspend fun getAttemptsForUserAdmin(username: String): List<QuizAttemptEntity> =
        db.quizAttemptDao().getAttemptsForUserAdmin(key(username))

    suspend fun getPerQuizAggregates(): List<QuizAggregateRow> =
        db.quizAttemptDao().getPerQuizAggregates()

    suspend fun getQuestionErrorRates(quizId: String): List<QuestionErrorRateRow> =
        db.quizAttemptDao().getQuestionErrorRates(quizId)

    suspend fun getMostFrequentWrongOption(quizId: String, questionId: Int): WrongOptionRow? =
        db.quizAttemptDao().getMostFrequentWrongOption(quizId, questionId)

    // ── Admin answer review analytics ─────────────────────────────────────────────────────────

    /** User's attempts filtered by module — admin drill-down. */
    suspend fun getAttemptsForUserByModule(username: String, moduleId: String): List<QuizAttemptEntity> =
        db.quizAttemptDao().getAttemptsForUserByModule(key(username), moduleId)

    /** Incorrect answers only for an attempt — admin/user filtered view. */
    suspend fun getIncorrectAnswersForAttempt(attemptId: Long): List<QuizAttemptAnswerEntity> =
        db.quizAttemptDao().getIncorrectAnswersForAttempt(attemptId)

    /** Performance breakdown by difficulty for a specific user. */
    suspend fun getUserPerformanceByDifficulty(username: String): List<DifficultyPerformanceRow> =
        db.quizAttemptDao().getUserPerformanceByDifficulty(key(username))

    /** Performance breakdown by module for a specific user. */
    suspend fun getUserPerformanceByModule(username: String): List<ModulePerformanceRow> =
        db.quizAttemptDao().getUserPerformanceByModule(key(username))

    /** Global answer stats across all users — admin dashboard. */
    suspend fun getGlobalAnswerStats(): GlobalAnswerStatsRow =
        db.quizAttemptDao().getGlobalAnswerStats()

    /** Questions with highest error rates across all users — admin analysis. */
    suspend fun getIncorrectAnswerAnalytics(): List<IncorrectAnswerAnalyticsRow> =
        db.quizAttemptDao().getIncorrectAnswerAnalytics()
}
