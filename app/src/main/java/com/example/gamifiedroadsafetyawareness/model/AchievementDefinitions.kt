package com.example.gamifiedroadsafetyawareness.model

import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity

/**
 * Single definition of a gamification achievement / badge. [condition] is evaluated
 * post-transaction by [GamificationEngine.evaluateAchievements] to decide if the badge
 * should be newly granted.
 */
data class AchievementDefinition(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val bonusXp: Int,
    val condition: (progress: UserProgressEntity, ctx: AchievementContext) -> Boolean
)

object AchievementDefinitions {

    val ALL = listOf(
        // ── Spec-required achievements ────────────────────────────────────────
        AchievementDefinition(
            id = "first_step",
            title = "First Step",
            description = "Complete your first learning module",
            icon = "🏅",
            bonusXp = 50
        ) { progress, _ ->
            progress.completedModuleIds.splitCsv().size >= 1
        },
        AchievementDefinition(
            id = "hot_streak",
            title = "Hot Streak",
            description = "Achieve 10 consecutive correct answers",
            icon = "🔥",
            bonusXp = 100
        ) { progress, ctx ->
            maxOf(progress.bestAnswerStreak, ctx.bestComboStreak) >= 10
        },
        AchievementDefinition(
            id = "knowledge_seeker",
            title = "Knowledge Seeker",
            description = "Complete 5 learning modules",
            icon = "📚",
            bonusXp = 200
        ) { progress, _ ->
            progress.completedModuleIds.splitCsv().size >= 5
        },
        AchievementDefinition(
            id = "perfect_driver",
            title = "Perfect Driver",
            description = "Achieve a perfect quiz score",
            icon = "🎯",
            bonusXp = 150
        ) { progress, _ ->
            progress.perfectQuizCount >= 1
        },
        AchievementDefinition(
            id = "road_safety_champion",
            title = "Road Safety Champion",
            description = "Reach Level 10",
            icon = "🏆",
            bonusXp = 500
        ) { progress, _ ->
            progress.currentLevel >= 10
        },

        // ── Extended achievements ─────────────────────────────────────────────
        AchievementDefinition(
            id = "streak_master",
            title = "Streak Master",
            description = "Maintain a 7-day learning streak",
            icon = "🔥",
            bonusXp = 100
        ) { progress, _ ->
            progress.longestStreak >= 7
        },
        AchievementDefinition(
            id = "quiz_veteran",
            title = "Quiz Veteran",
            description = "Complete 10 quizzes",
            icon = "📝",
            bonusXp = 100
        ) { progress, _ ->
            progress.quizzesCompleted >= 10
        },
        AchievementDefinition(
            id = "traffic_rule_expert",
            title = "Traffic Rule Expert",
            description = "Score 90%+ on 3 different quizzes",
            icon = "⚖️",
            bonusXp = 150
        ) { progress, _ ->
            progress.highScoreQuizIds.splitCsv().size >= 3
        },
        AchievementDefinition(
            id = "dedication_award",
            title = "Dedication Award",
            description = "Reach 5,000 total XP",
            icon = "⭐",
            bonusXp = 200
        ) { progress, _ ->
            progress.totalXp >= 5_000
        },
        AchievementDefinition(
            id = "xp_legend",
            title = "XP Legend",
            description = "Accumulate 10,000 total XP",
            icon = "💎",
            bonusXp = 500
        ) { progress, _ ->
            progress.totalXp >= 10_000
        },
        AchievementDefinition(
            id = "module_master",
            title = "Module Master",
            description = "Complete all 8 learning modules",
            icon = "🎓",
            bonusXp = 300
        ) { progress, _ ->
            progress.completedModuleIds.splitCsv().size >= 8
        },
        AchievementDefinition(
            id = "scenario_specialist",
            title = "Scenario Specialist",
            description = "Complete 5 simulation scenarios",
            icon = "🚗",
            bonusXp = 150
        ) { progress, _ ->
            progress.scenariosCompleted >= 5
        },
        AchievementDefinition(
            id = "question_master",
            title = "Question Master",
            description = "Answer 100 questions correctly",
            icon = "💯",
            bonusXp = 200
        ) { progress, _ ->
            progress.completedQuestionsCount >= 100
        },
        AchievementDefinition(
            id = "marathon_streak",
            title = "Marathon Streak",
            description = "Maintain a 30-day learning streak",
            icon = "🏅",
            bonusXp = 500
        ) { progress, _ ->
            progress.longestStreak >= 30
        }
    )

    /**
     * Converts already-unlocked achievement IDs into [BadgeItem]s for the badge gallery.
     * Locked achievements display as locked.
     */
    fun toBadgeItems(unlockedIds: Set<String>): List<BadgeItem> =
        ALL.map { definition ->
            BadgeItem(
                id = definition.id,
                title = definition.title,
                icon = definition.icon,
                description = definition.description,
                isUnlocked = definition.id in unlockedIds,
                progressText = if (definition.id in unlockedIds) "Unlocked · +${definition.bonusXp} XP" else "Locked"
            )
        }
}
