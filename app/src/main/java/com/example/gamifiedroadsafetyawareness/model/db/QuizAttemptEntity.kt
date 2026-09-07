package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One completed quiz attempt — the record `xp_history`/`user_progress` never captured (they only
 * store aggregates). Backs the Module Summary screen, Learning History, and admin analytics.
 */
@Entity(
    tableName = "quiz_attempts",
    foreignKeys = [
        ForeignKey(
            entity = UserProgressEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("user_id"),
        Index("quiz_id"),
        Index(value = ["user_id", "quiz_id"]),
        Index("completed_at")
    ]
)
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: String,

    // Quiz.id (e.g. "quiz_easy"). Kept distinct from moduleId even though they're 1:1 today.
    @ColumnInfo(name = "quiz_id")
    val quizId: String,

    // LearningModule.id (e.g. "mod_easy_quiz") — what completedModuleIds/ModuleXp already key on.
    @ColumnInfo(name = "module_id")
    val moduleId: String,

    @ColumnInfo(name = "quiz_title")
    val quizTitle: String,

    // ModuleType.name ("EASY"/"MEDIUM"/"HARD")
    @ColumnInfo(name = "difficulty")
    val difficulty: String,

    // 1-based; computed as (prior attempts for this user+quiz) + 1 at write time.
    @ColumnInfo(name = "attempt_number")
    val attemptNumber: Int,

    @ColumnInfo(name = "total_questions")
    val totalQuestions: Int,

    @ColumnInfo(name = "correct_count")
    val correctCount: Int,

    @ColumnInfo(name = "incorrect_count")
    val incorrectCount: Int,

    @ColumnInfo(name = "unanswered_count")
    val unansweredCount: Int,

    @ColumnInfo(name = "score_percent")
    val scorePercent: Int,

    @ColumnInfo(name = "passed")
    val passed: Boolean,

    @ColumnInfo(name = "xp_earned")
    val xpEarned: Int,

    @ColumnInfo(name = "best_combo_streak")
    val bestComboStreak: Int,

    @ColumnInfo(name = "leveled_up")
    val leveledUp: Boolean,

    @ColumnInfo(name = "level_after")
    val levelAfter: Int,

    @ColumnInfo(name = "time_spent_seconds")
    val timeSpentSeconds: Int,

    @ColumnInfo(name = "time_challenge_completed")
    val timeChallengeCompleted: Boolean,

    @ColumnInfo(name = "started_at")
    val startedAt: Long,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long = System.currentTimeMillis()
)
