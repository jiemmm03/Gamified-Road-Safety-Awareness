package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One answered (or timed-out) question within a [QuizAttemptEntity]. All display-facing text
 * (question wording, selected/correct answer text, explanation, safety tip) is snapshotted at
 * answer time so historical review stays accurate even if quiz content is edited later.
 * The (quiz_id, question_id) index backs the admin per-question error-rate and
 * most-frequent-wrong-answer aggregate queries across all users' attempts.
 */
@Entity(
    tableName = "quiz_attempt_answers",
    foreignKeys = [
        ForeignKey(
            entity = QuizAttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attempt_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("attempt_id"),
        Index(value = ["quiz_id", "question_id"])
    ]
)
data class QuizAttemptAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "attempt_id")
    val attemptId: Long,

    // Denormalized from the parent attempt — avoids a join for admin per-question queries.
    @ColumnInfo(name = "quiz_id")
    val quizId: String,

    @ColumnInfo(name = "question_id")
    val questionId: Int,

    // 1-based display order within the attempt.
    @ColumnInfo(name = "question_number")
    val questionNumber: Int = 0,

    // Snapshotted at answer time so historical review stays accurate if question text changes later.
    @ColumnInfo(name = "question_text")
    val questionText: String,

    // -1 = unanswered/timeout
    @ColumnInfo(name = "selected_option_index")
    val selectedOptionIndex: Int,

    @ColumnInfo(name = "correct_option_index")
    val correctOptionIndex: Int,

    // Snapshotted answer text — self-contained, no re-resolving needed.
    @ColumnInfo(name = "selected_answer_text")
    val selectedAnswerText: String = "",

    @ColumnInfo(name = "correct_answer_text")
    val correctAnswerText: String = "",

    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean,

    // Points/XP earned for this specific question.
    @ColumnInfo(name = "points_earned")
    val pointsEarned: Int = 0,

    @ColumnInfo(name = "xp_earned")
    val xpEarned: Int = 0,

    // Topic-based explanation and safety tip, snapshotted from RoadSafetyTopic at answer time.
    @ColumnInfo(name = "explanation")
    val explanation: String = "",

    @ColumnInfo(name = "safety_tip")
    val safetyTip: String = "",

    // Quiz difficulty (EASY/MEDIUM/HARD) — denormalized for per-difficulty analytics.
    @ColumnInfo(name = "difficulty")
    val difficulty: String = "",

    // RoadSafetyTopic.name
    @ColumnInfo(name = "topic")
    val topic: String,

    // Timestamp when this individual answer was submitted.
    @ColumnInfo(name = "answered_at")
    val answeredAt: Long = 0L
)
