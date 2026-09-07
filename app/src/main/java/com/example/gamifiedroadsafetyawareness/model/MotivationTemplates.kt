package com.example.gamifiedroadsafetyawareness.model

import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity

/**
 * Rule-based "AI encouragement" text generation — same templated-string convention as
 * MockData.aiCurriculumLogs, not a real model call.
 */
object MotivationTemplates {
    fun generate(progress: UserProgressEntity?): String {
        if (progress == null) {
            return "Complete your first lesson to start earning XP and building your learning streak!"
        }

        val xpToNextLevel = GamificationConstants.getXpForNextLevel(progress.currentLevel) -
            GamificationConstants.getCurrentLevelXp(progress.totalXp, progress.currentLevel)

        return when {
            progress.currentStreak >= 7 ->
                "Keep your ${progress.currentStreak}-day learning streak alive! Consistency is the fastest way to master road safety."
            xpToNextLevel in 1..150 ->
                "Only $xpToNextLevel XP until Level ${progress.currentLevel + 1}. One more quiz could get you there!"
            progress.longestStreak > progress.currentStreak && progress.longestStreak >= 3 ->
                "Your best streak was ${progress.longestStreak} days — beat your record by learning today."
            progress.streakFreezes > 0 ->
                "You have ${progress.streakFreezes} streak freeze${if (progress.streakFreezes == 1) "" else "s"} banked, protecting your progress if you ever miss a day."
            else ->
                "Every lesson sharpens your road safety instincts. Keep going!"
        }
    }
}
