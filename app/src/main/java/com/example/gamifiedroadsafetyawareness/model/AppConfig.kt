package com.example.gamifiedroadsafetyawareness.model

/**
 * Complete, strongly typed configuration model for RoadSafe AI.
 * Synchronized live between Admin Web Command Center and Android Mobile App via Firestore document `system_settings/app_config`.
 */
data class AppConfig(
    // ── 1. Examination & Assessment Settings ──
    val quizPassingScore: Int = 70, // 50 - 100%
    val assessmentPassingScore: Int = 75, // 50 - 100%
    val simulationPassingScore: Int = 75, // 50 - 100%
    val baseQuizXp: Int = 100,
    val dailyStreakMultiplier: Float = 1.25f,
    val quizAttemptLimit: Int = 0, // 0 = Unlimited, 1, 2, 3, etc.
    val quizTimerSeconds: Int = 20, // 0 = No timer, 15, 20, 30, etc.
    val randomizeQuestions: Boolean = false,
    val randomizeChoices: Boolean = false,
    val showCorrectAnswers: Boolean = true,
    val allowQuizRetake: Boolean = true,
    val minScoreForXp: Int = 50, // Minimum % required to award XP

    // ── 2. Gamification & XP Economy Settings ──
    val xpPerCompletedModule: Int = 50,
    val xpPerPassedQuiz: Int = 100,
    val xpPerPassedAssessment: Int = 150,
    val xpPerCorrectAnswer: Int = 10,
    val xpStreakBonusBase: Int = 20,
    val maxXpPerQuiz: Int = 300,
    val enableDailyStreak: Boolean = true,
    val streakResetHours: Int = 24,
    val streakMultiplierMax: Float = 2.0f,
    val enableUserLevels: Boolean = true,
    val xpPerLevel: Int = 500,
    val maxLevel: Int = 50,
    val autoLevelCalc: Boolean = true,
    val enableLeaderboard: Boolean = true,
    val leaderboardUpdateFreq: String = "Real-time", // "Real-time", "Hourly", "Daily"
    val leaderboardRankingType: String = "totalXp", // "totalXp", "modulesCompleted", "assessmentScore", "combined"

    // ── 3. Language Settings (Quiz & Assessment only) ──
    val langEnglishEnabled: Boolean = true,
    val langFilipinoEnabled: Boolean = true,
    val defaultQuizLanguage: String = "en", // "en" | "fil"

    // ── 4. Mobile App Behavior Settings ──
    val maintenanceMode: Boolean = false,
    val maintenanceMessage: String = "RoadSafe AI is undergoing scheduled system maintenance. Please try again shortly.",
    val minAppVersion: String = "1.0.0",
    val forceUpdate: Boolean = false,
    val announcementEnabled: Boolean = false,
    val announcementTitle: String = "Municipal Road Safety Notice",
    val announcementMessage: String = "",
    val announcementStartDate: String = "",
    val announcementEndDate: String = "",
    val enableAnimations: Boolean = true,

    // ── 5. Modules & Content Progression ──
    val requireModuleBeforeQuiz: Boolean = false,
    val strictLinearProgression: Boolean = false,
    val contentVersion: String = "v1.2.0-300Q",
    val disabledModuleIds: List<String> = emptyList(),

    // ── 6. User Session Settings ──
    val rememberLoginSession: Boolean = true,
    val sessionTimeoutDays: Int = 0, // 0 = Never, 7, 30
    val allowMultipleDevices: Boolean = true,

    // ── 7. Notification Settings ──
    val enableNotifications: Boolean = true,
    val notifyQuizReminder: Boolean = true,
    val notifyStreakReminder: Boolean = true,
    val notifyNewModule: Boolean = true,
    val notifyAchievement: Boolean = true,
    val notifyLeaderboard: Boolean = true,

    // ── 8. Progress & Synchronization ──
    val autoSyncProgress: Boolean = true,
    val syncFrequency: String = "realtime", // "realtime", "5min", "15min", "manual"

    // ── 9. Security & Access Control ──
    val adminSessionTimeoutMinutes: Int = 60,
    val requireReauthSensitive: Boolean = true,
    val adminActivityLogging: Boolean = true,

    // ── 10. Data Management ──
    val enableSoftDelete: Boolean = false,
    val dataRetentionDays: Int = 0, // 0 = Permanent

    // ── Metadata ──
    val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
    val lastUpdatedBy: String = "system"
) {
    fun isModuleDisabled(moduleId: String): Boolean = disabledModuleIds.contains(moduleId)
}
