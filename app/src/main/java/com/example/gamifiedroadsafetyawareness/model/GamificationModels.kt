package com.example.gamifiedroadsafetyawareness.model

/** Tier of a driver-decision-simulator outcome, drives which [GamificationConstants.SimulationXp] value applies. */
enum class SimulationTier {
    SAFE_DECISION,
    EXCELLENT_DECISION,
    PERFECT_SIMULATION
}

/** Difficulty tier of a learning module, used for its display label. Completion XP is resolved per-module by id via [GamificationConstants.ModuleXp.getModuleXp]. */
enum class ModuleType {
    EASY,
    MEDIUM,
    HARD;

    val label: String
        get() = when (this) {
            EASY -> "Easy Module"
            MEDIUM -> "Medium Module"
            HARD -> "Hard Module"
        }
}

/**
 * Full breakdown of one XP-awarding event, structured so the UI (end-of-quiz summary,
 * floating reward popups, level-up modal) can render every line item the spec asks for
 * without re-deriving any of the math.
 */
data class XpAwardResult(
    val baseXp: Int,
    val streakBonusXp: Int = 0,
    val perfectBonusXp: Int = 0,
    val completionBonusXp: Int = 0,
    val firstAttemptBonusXp: Int = 0,
    val timeChallengeBonusXp: Int = 0,
    val aiRecommendationBonusXp: Int = 0,
    val dailyStreakBonusXp: Int = 0,
    val streakProtected: Boolean = false,
    val multiplierApplied: Float = 1.0f,
    val achievementsUnlocked: List<AchievementDefinition> = emptyList(),
    val achievementBonusXp: Int = 0,
    val totalAwarded: Int,
    val totalXp: Int,
    val previousLevel: Int,
    val newLevel: Int,
    val streakFreezeEarned: Boolean = false,
    val currentDailyStreak: Int = 0,
    val modulePerfectBonusXp: Int = 0
) {
    val leveledUp: Boolean get() = newLevel > previousLevel
    val levelsGained: Int get() = newLevel - previousLevel
    val newLevelName: String get() = GamificationConstants.getLevelName(newLevel)
}

/** Session-only context an activity's persisted [com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity] alone can't answer. */
data class AchievementContext(
    val bestComboStreak: Int = 0,
    val quizScorePercent: Int = 0
)

/**
 * One question's outcome, captured live during a quiz attempt so it survives past the moment the
 * user moves to the next question (previously discarded entirely). Feeds
 * [com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptAnswerEntity] rows and the
 * Module Review screen.
 */
data class QuestionAnswerRecord(
    val questionId: Int,
    val questionNumber: Int = 0,         // 1-based display order
    val questionText: String,
    val selectedOptionIndex: Int,         // -1 = unanswered/timeout
    val correctOptionIndex: Int,
    val selectedAnswerText: String = "",  // snapshot of option text
    val correctAnswerText: String = "",   // snapshot of correct option text
    val isCorrect: Boolean,
    val pointsEarned: Int = 0,           // points for this question
    val xpEarned: Int = 0,               // XP earned for this question
    val explanation: String = "",         // topic explanation
    val safetyTip: String = "",           // related road-safety tip
    val difficulty: String = "",          // EASY/MEDIUM/HARD
    val topic: String,                    // RoadSafetyTopic.name
    val answeredAt: Long = 0L            // timestamp when answered
)
