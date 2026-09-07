package com.example.gamifiedroadsafetyawareness.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Every numeric reward value from the gamification spec, named instead of scattered as
 * magic numbers, plus the level curve. Admin-configurable values are stored in
 * SharedPreferences and fall back to the defaults defined here.
 */
object GamificationConstants {

    // ── Admin-configurable keys (SharedPreferences) ────────────────────────────
    private const val PREFS_NAME = "xp_config"
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getConfigInt(key: String, default: Int): Int =
        prefs?.getInt(key, default) ?: default

    fun setConfigInt(key: String, value: Int) {
        prefs?.edit()?.putInt(key, value)?.apply()
    }

    // ── Per-Module XP Rewards (admin-configurable) ─────────────────────────────
    object ModuleXp {
        private val DEFAULTS = mapOf(
            "mod_easy_quiz" to 100,
            "mod_medium_quiz" to 200,
            "mod_hard_quiz" to 300
        )

        fun getModuleXp(moduleId: String): Int =
            getConfigInt("module_xp_$moduleId", DEFAULTS[moduleId] ?: 100)

        fun setModuleXp(moduleId: String, xp: Int) =
            setConfigInt("module_xp_$moduleId", xp)

        fun getAllModuleXp(): Map<String, Int> =
            DEFAULTS.keys.associateWith { getModuleXp(it) }
    }

    // ── Module/Quiz enable-disable (admin-configurable "publish" toggle) ───────
    // The live content model is 1 module = 1 quiz (e.g. mod_easy_quiz -> quiz_easy), so a
    // single flag per module id covers both "Module Management" and "Quiz Management".
    object ContentSettings {
        private val DEFAULT_ENABLED = mapOf(
            "mod_easy_quiz" to true,
            "mod_medium_quiz" to true,
            "mod_hard_quiz" to true
        )

        fun isModuleEnabled(moduleId: String): Boolean =
            prefs?.getBoolean("module_enabled_$moduleId", DEFAULT_ENABLED[moduleId] ?: true) ?: true

        fun setModuleEnabled(moduleId: String, enabled: Boolean) {
            prefs?.edit()?.putBoolean("module_enabled_$moduleId", enabled)?.apply()
        }
    }

    // ── Quiz XP ────────────────────────────────────────────────────────────────
    object QuizXp {
        val CORRECT_ANSWER get() = getConfigInt("quiz_correct_answer", 10)
        val PERFECT_SCORE get() = getConfigInt("quiz_perfect_score", 50)
        val COMPLETION get() = getConfigInt("quiz_completion", 25)
        val FIRST_ATTEMPT_PERFECT get() = getConfigInt("quiz_first_attempt_perfect", 30)
        val TIME_CHALLENGE get() = getConfigInt("quiz_time_challenge", 25)

        fun setCorrectAnswer(xp: Int) = setConfigInt("quiz_correct_answer", xp)
        fun setPerfectScore(xp: Int) = setConfigInt("quiz_perfect_score", xp)
        fun setCompletion(xp: Int) = setConfigInt("quiz_completion", xp)
        fun setFirstAttemptPerfect(xp: Int) = setConfigInt("quiz_first_attempt_perfect", xp)
        fun setTimeChallenge(xp: Int) = setConfigInt("quiz_time_challenge", xp)
    }

    // ── Simulation XP ──────────────────────────────────────────────────────────
    object SimulationXp {
        const val SAFE_DECISION = 15
        const val EXCELLENT_DECISION = 25
        const val PERFECT_SIMULATION = 60
        const val AI_RECOMMENDATION_FOLLOWED = 10
    }

    // ── Module Completion Bonus ────────────────────────────────────────────────
    val MODULE_COMPLETION_PERFECT_BONUS get() = getConfigInt("module_perfect_bonus", 50)

    // ── Quiz Pass Threshold (admin-configurable; default 60%) ──────────────────
    val QUIZ_PASS_THRESHOLD_PERCENT get() = getConfigInt("quiz_pass_threshold_percent", 60)
    fun setQuizPassThreshold(percent: Int) = setConfigInt("quiz_pass_threshold_percent", percent)

    // ── Streak Bonuses (spec default: 3→15, 5→25, 10→50, 20→100), admin-configurable ──
    private val DEFAULT_STREAK_BONUS_TABLE = mapOf(
        3 to 15,
        5 to 25,
        10 to 50,
        20 to 100
    )

    /** Consecutive-correct-answer milestone -> bonus XP awarded on that exact question. */
    val STREAK_BONUS_TABLE: Map<Int, Int>
        get() = DEFAULT_STREAK_BONUS_TABLE.keys.associateWith { milestone ->
            getConfigInt("streak_bonus_$milestone", DEFAULT_STREAK_BONUS_TABLE.getValue(milestone))
        }

    fun setStreakBonus(milestone: Int, xp: Int) = setConfigInt("streak_bonus_$milestone", xp)

    private val DEFAULT_STREAK_MULTIPLIER_TABLE = listOf(
        5 to 1.2f,
        10 to 1.5f,
        15 to 1.8f,
        20 to 2.0f
    )

    /** Consecutive-correct-answer threshold -> XP multiplier, sorted ascending by threshold. */
    val STREAK_MULTIPLIER_TABLE: List<Pair<Int, Float>>
        get() = DEFAULT_STREAK_MULTIPLIER_TABLE.map { (threshold, defaultMult) ->
            val stored = prefs?.getFloat("streak_multiplier_$threshold", defaultMult) ?: defaultMult
            threshold to stored
        }

    fun setStreakMultiplier(threshold: Int, multiplier: Float) {
        prefs?.edit()?.putFloat("streak_multiplier_$threshold", multiplier)?.apply()
    }

    private val DEFAULT_DAILY_STREAK_REWARD_TABLE = mapOf(
        1 to 20,
        3 to 40,
        7 to 75,
        14 to 150,
        30 to 500,
        60 to 1200,
        100 to 3000
    )

    /** Daily learning streak (day count) -> bonus XP awarded when that day count is reached. */
    val DAILY_STREAK_REWARD_TABLE: Map<Int, Int>
        get() = DEFAULT_DAILY_STREAK_REWARD_TABLE.keys.associateWith { day ->
            getConfigInt("daily_streak_reward_$day", DEFAULT_DAILY_STREAK_REWARD_TABLE.getValue(day))
        }

    fun setDailyStreakReward(day: Int, xp: Int) = setConfigInt("daily_streak_reward_$day", xp)

    const val FREEZE_EARN_INTERVAL_DAYS = 7

    /** Highest multiplier threshold reached by [streak], or 1.0x if below the first threshold. */
    fun multiplierForStreak(streak: Int): Float =
        STREAK_MULTIPLIER_TABLE.lastOrNull { streak >= it.first }?.second ?: 1.0f

    // ── Level Curve (spec table: L1=0 through L10=16000) ───────────────────────
    private val DEFAULT_LEVEL_THRESHOLDS = listOf(
        1 to 0,
        2 to 500,
        3 to 1200,
        4 to 2000,
        5 to 3000,
        6 to 4500,
        7 to 6500,
        8 to 9000,
        9 to 12000,
        10 to 16000
    )

    private const val MAX_TABLE_LEVEL = 120

    /**
     * Index = level, value = cumulative total XP required to reach that level. Index 0 unused.
     * Recomputed on every access (not cached) so admin threshold edits via [setLevelThreshold]
     * take effect immediately instead of only after a process restart.
     */
    val LEVEL_XP_TABLE: List<Int>
        get() {
            val anchors = DEFAULT_LEVEL_THRESHOLDS.map { (level, defaultXp) ->
                level to getConfigInt("level_threshold_$level", defaultXp)
            }
            return (0..MAX_TABLE_LEVEL).map { level -> if (level < 1) 0 else xpForLevel(level, anchors) }
        }

    private fun xpForLevel(level: Int, anchors: List<Pair<Int, Int>>): Int {
        anchors.firstOrNull { it.first == level }?.let { return it.second }

        val lastAnchor = anchors.last()
        if (level > lastAnchor.first) {
            val prevAnchor = anchors[anchors.size - 2]
            val increment = (lastAnchor.second - prevAnchor.second).toFloat() / (lastAnchor.first - prevAnchor.first)
            return (lastAnchor.second + increment * (level - lastAnchor.first)).toInt()
        }

        val upperIndex = anchors.indexOfFirst { it.first > level }
        val (la, xa) = anchors[upperIndex - 1]
        val (lb, xb) = anchors[upperIndex]
        val t = (level - la).toDouble() / (lb - la)
        return (xa + (xb - xa) * Math.pow(t, 1.5)).toInt()
    }

    fun getLevelForXp(totalXp: Int): Int {
        val table = LEVEL_XP_TABLE
        var level = 1
        for (lvl in 1..table.lastIndex) {
            if (totalXp >= table[lvl]) level = lvl else break
        }
        return level
    }

    fun getCurrentLevelXp(totalXp: Int, level: Int): Int =
        totalXp - LEVEL_XP_TABLE.getOrElse(level) { 0 }

    fun getXpForNextLevel(level: Int): Int {
        val current = LEVEL_XP_TABLE.getOrElse(level) { 0 }
        val next = LEVEL_XP_TABLE.getOrElse(level + 1) { current + 1000 }
        return next - current
    }

    fun getLevelProgress(totalXp: Int, level: Int): Float {
        val span = getXpForNextLevel(level)
        if (span <= 0) return 0f
        return (getCurrentLevelXp(totalXp, level).toFloat() / span).coerceIn(0f, 1f)
    }

    // ── Level Names ────────────────────────────────────────────────────────────
    // Officer Training Level names — a training-academy progression, deliberately not literal
    // police ranks (Sergeant/Lieutenant/etc.), to stay professional without feeling militaristic.
    val LEVEL_NAMES: Map<Int, String> = mapOf(
        1 to "Recruit Driver",
        2 to "Road Safety Trainee",
        3 to "Certified Road Learner",
        4 to "Defensive Driver",
        5 to "Patrol-Ready Driver",
        6 to "Skilled Road Officer",
        7 to "Road Safety Specialist",
        8 to "Senior Safety Officer",
        9 to "Master Road Officer",
        10 to "Road Safety Chief"
    )

    fun getLevelName(level: Int): String =
        LEVEL_NAMES[level.coerceIn(1, 10)] ?: "Level $level"

    // ── Achievement Bonus XP (admin-configurable; unlock CONDITIONS stay fixed — they're
    // Kotlin lambdas in AchievementDefinitions, not data, so only the reward amount is editable) ──
    object AchievementXp {
        fun getBonusXp(achievementId: String, defaultXp: Int): Int =
            getConfigInt("achievement_xp_$achievementId", defaultXp)

        fun setBonusXp(achievementId: String, xp: Int) =
            setConfigInt("achievement_xp_$achievementId", xp)
    }

    // ── Module Unlock Requirements ─────────────────────────────────────────────
    sealed class UnlockRequirement {
        object None : UnlockRequirement()
        data class RequiresLevel(val level: Int) : UnlockRequirement()
        data class RequiresModule(val moduleId: String) : UnlockRequirement()
        object RequiresAllModules : UnlockRequirement()
    }

    val MODULE_UNLOCK_REQUIREMENTS: Map<String, UnlockRequirement> = mapOf(
        "mod_easy_quiz" to UnlockRequirement.None,
        "mod_medium_quiz" to UnlockRequirement.None,
        "mod_hard_quiz" to UnlockRequirement.None
    )

    // ── Admin Level Threshold Configuration ────────────────────────────────────
    fun setLevelThreshold(level: Int, xp: Int) {
        setConfigInt("level_threshold_$level", xp)
    }

    fun getLevelThresholds(): List<Pair<Int, Int>> =
        DEFAULT_LEVEL_THRESHOLDS.map { (level, defaultXp) ->
            level to getConfigInt("level_threshold_$level", defaultXp)
        }
}
