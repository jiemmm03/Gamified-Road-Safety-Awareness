package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class QuizAggregateRow(
    @ColumnInfo(name = "quiz_id") val quizId: String,
    @ColumnInfo(name = "attemptCount") val attemptCount: Int,
    @ColumnInfo(name = "avgScore") val avgScore: Double,
    @ColumnInfo(name = "passedCount") val passedCount: Int,
    @ColumnInfo(name = "avgTimeSeconds") val avgTimeSeconds: Double,
    @ColumnInfo(name = "avgXp") val avgXp: Double
)

data class QuestionErrorRateRow(
    @ColumnInfo(name = "question_id") val questionId: Int,
    @ColumnInfo(name = "errorRate") val errorRate: Double,
    @ColumnInfo(name = "totalAnswers") val totalAnswers: Int
)

data class WrongOptionRow(
    @ColumnInfo(name = "selected_option_index") val selectedOptionIndex: Int,
    @ColumnInfo(name = "pickCount") val pickCount: Int
)

data class DifficultyPerformanceRow(
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "avgScore") val avgScore: Double,
    @ColumnInfo(name = "attemptCount") val attemptCount: Int
)

data class ModulePerformanceRow(
    @ColumnInfo(name = "quiz_id") val quizId: String,
    @ColumnInfo(name = "quiz_title") val quizTitle: String,
    @ColumnInfo(name = "avgScore") val avgScore: Double,
    @ColumnInfo(name = "attemptCount") val attemptCount: Int,
    @ColumnInfo(name = "bestScore") val bestScore: Int
)

data class GlobalAnswerStatsRow(
    @ColumnInfo(name = "totalAttempts") val totalAttempts: Int,
    @ColumnInfo(name = "totalAnswers") val totalAnswers: Int,
    @ColumnInfo(name = "correctAnswers") val correctAnswers: Int,
    @ColumnInfo(name = "incorrectAnswers") val incorrectAnswers: Int
)

data class IncorrectAnswerAnalyticsRow(
    @ColumnInfo(name = "quiz_id") val quizId: String,
    @ColumnInfo(name = "question_id") val questionId: Int,
    @ColumnInfo(name = "question_text") val questionText: String,
    @ColumnInfo(name = "errorCount") val errorCount: Int,
    @ColumnInfo(name = "totalAnswers") val totalAnswers: Int,
    @ColumnInfo(name = "difficulty") val difficulty: String
)

@Dao
interface QuizAttemptDao {

    @Insert
    suspend fun insertAttempt(attempt: QuizAttemptEntity): Long

    @Insert
    suspend fun insertAnswers(answers: List<QuizAttemptAnswerEntity>)

    @Query("SELECT COUNT(*) FROM quiz_attempts WHERE user_id = :userId AND quiz_id = :quizId")
    suspend fun countAttempts(userId: String, quizId: String): Int

    @Query("SELECT MAX(score_percent) FROM quiz_attempts WHERE user_id = :userId AND quiz_id = :quizId")
    suspend fun highestScorePercent(userId: String, quizId: String): Int?

    @Query("SELECT * FROM quiz_attempts WHERE id = :attemptId")
    suspend fun getAttempt(attemptId: Long): QuizAttemptEntity?

    @Query("SELECT * FROM quiz_attempt_answers WHERE attempt_id = :attemptId ORDER BY question_number, id")
    suspend fun getAnswersForAttempt(attemptId: Long): List<QuizAttemptAnswerEntity>

    @Query("SELECT * FROM quiz_attempt_answers WHERE attempt_id = :attemptId AND is_correct = 0 ORDER BY question_number, id")
    suspend fun getIncorrectAnswersForAttempt(attemptId: Long): List<QuizAttemptAnswerEntity>

    // ── User-scoped: own data only, always called with key(username) ──────────────────────────
    @Query("SELECT * FROM quiz_attempts WHERE user_id = :userId ORDER BY completed_at DESC")
    suspend fun getAttemptsForUser(userId: String): List<QuizAttemptEntity>

    @Query("SELECT * FROM quiz_attempts WHERE user_id = :userId AND module_id = :moduleId ORDER BY completed_at DESC")
    suspend fun getAttemptsForUserByModule(userId: String, moduleId: String): List<QuizAttemptEntity>

    // ── Admin-scoped: only reachable from permission-gated admin screens ───────────────────────
    @Query("SELECT * FROM quiz_attempts ORDER BY completed_at DESC LIMIT :limit")
    suspend fun getRecentAttemptsAll(limit: Int): List<QuizAttemptEntity>

    @Query("SELECT * FROM quiz_attempts WHERE user_id = :userId ORDER BY completed_at DESC")
    suspend fun getAttemptsForUserAdmin(userId: String): List<QuizAttemptEntity>

    @Query(
        """
        SELECT quiz_id, COUNT(*) as attemptCount, AVG(score_percent) as avgScore,
               SUM(CASE WHEN passed THEN 1 ELSE 0 END) as passedCount,
               AVG(time_spent_seconds) as avgTimeSeconds, AVG(xp_earned) as avgXp
        FROM quiz_attempts GROUP BY quiz_id
        """
    )
    suspend fun getPerQuizAggregates(): List<QuizAggregateRow>

    @Query(
        """
        SELECT question_id,
               SUM(CASE WHEN is_correct THEN 0 ELSE 1 END) * 1.0 / COUNT(*) as errorRate,
               COUNT(*) as totalAnswers
        FROM quiz_attempt_answers WHERE quiz_id = :quizId GROUP BY question_id
        ORDER BY errorRate DESC
        """
    )
    suspend fun getQuestionErrorRates(quizId: String): List<QuestionErrorRateRow>

    @Query(
        """
        SELECT selected_option_index, COUNT(*) as pickCount
        FROM quiz_attempt_answers
        WHERE quiz_id = :quizId AND question_id = :questionId AND is_correct = 0
        GROUP BY selected_option_index ORDER BY pickCount DESC LIMIT 1
        """
    )
    suspend fun getMostFrequentWrongOption(quizId: String, questionId: Int): WrongOptionRow?

    // ── Admin answer review analytics ─────────────────────────────────────────────────────────

    @Query(
        """
        SELECT difficulty, AVG(score_percent) as avgScore, COUNT(*) as attemptCount
        FROM quiz_attempts WHERE user_id = :userId
        GROUP BY difficulty
        """
    )
    suspend fun getUserPerformanceByDifficulty(userId: String): List<DifficultyPerformanceRow>

    @Query(
        """
        SELECT quiz_id, quiz_title, AVG(score_percent) as avgScore, COUNT(*) as attemptCount,
               MAX(score_percent) as bestScore
        FROM quiz_attempts WHERE user_id = :userId
        GROUP BY quiz_id
        """
    )
    suspend fun getUserPerformanceByModule(userId: String): List<ModulePerformanceRow>

    @Query(
        """
        SELECT
            (SELECT COUNT(*) FROM quiz_attempts) as totalAttempts,
            (SELECT COUNT(*) FROM quiz_attempt_answers) as totalAnswers,
            (SELECT COUNT(*) FROM quiz_attempt_answers WHERE is_correct = 1) as correctAnswers,
            (SELECT COUNT(*) FROM quiz_attempt_answers WHERE is_correct = 0) as incorrectAnswers
        """
    )
    suspend fun getGlobalAnswerStats(): GlobalAnswerStatsRow

    @Query(
        """
        SELECT a.quiz_id, a.question_id, a.question_text,
               SUM(CASE WHEN a.is_correct = 0 THEN 1 ELSE 0 END) as errorCount,
               COUNT(*) as totalAnswers,
               COALESCE(a.difficulty, '') as difficulty
        FROM quiz_attempt_answers a
        GROUP BY a.quiz_id, a.question_id
        HAVING errorCount > 0
        ORDER BY errorCount DESC
        """
    )
    suspend fun getIncorrectAnswerAnalytics(): List<IncorrectAnswerAnalyticsRow>
}

