package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "current_xp")
    val currentXp: Int = 0,

    @ColumnInfo(name = "total_xp")
    val totalXp: Int = 0,

    @ColumnInfo(name = "current_level")
    val currentLevel: Int = 1,

    @ColumnInfo(name = "current_streak")
    val currentStreak: Int = 0,

    @ColumnInfo(name = "longest_streak")
    val longestStreak: Int = 0,

    @ColumnInfo(name = "daily_streak")
    val dailyStreak: Int = 0,

    @ColumnInfo(name = "last_activity_date")
    val lastActivityDate: String? = null,

    @ColumnInfo(name = "streak_freezes")
    val streakFreezes: Int = 0,

    @ColumnInfo(name = "multiplier")
    val multiplier: Float = 1.0f,

    @ColumnInfo(name = "perfect_quiz_count")
    val perfectQuizCount: Int = 0,

    @ColumnInfo(name = "quizzes_completed")
    val quizzesCompleted: Int = 0,

    // CSV of quiz ids ever attempted — used to derive "first attempt" for the first-attempt-perfect bonus.
    @ColumnInfo(name = "attempted_quiz_ids")
    val attemptedQuizIds: String = "",

    // CSV of quiz ids passed at >=90% at least once — used by the "Traffic Rule Expert" achievement.
    @ColumnInfo(name = "high_score_quiz_ids")
    val highScoreQuizIds: String = "",

    // CSV of learning module ids already completed — prevents re-awarding module XP on re-tap.
    @ColumnInfo(name = "completed_module_ids")
    val completedModuleIds: String = "",

    // CSV of individual question IDs already answered correctly — prevents XP re-exploitation.
    @ColumnInfo(name = "answered_question_ids")
    val answeredQuestionIds: String = "",

    // Best answer streak across all quizzes (all-time).
    @ColumnInfo(name = "best_answer_streak")
    val bestAnswerStreak: Int = 0,

    // Total number of correctly answered questions.
    @ColumnInfo(name = "completed_questions_count")
    val completedQuestionsCount: Int = 0,

    // Total simulation scenarios completed.
    @ColumnInfo(name = "scenarios_completed")
    val scenariosCompleted: Int = 0,

    // CSV of RoadSafetyTopic names the user has answered incorrectly — read by the AI tutor to
    // identify weak topics worth revisiting. Append-only; not deduped (frequency implies weight).
    @ColumnInfo(name = "missed_topics")
    val missedTopics: String = "",

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
