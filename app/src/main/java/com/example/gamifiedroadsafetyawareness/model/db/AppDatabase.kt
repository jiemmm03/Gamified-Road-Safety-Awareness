package com.example.gamifiedroadsafetyawareness.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UserProgressEntity::class,
        XpHistoryEntity::class,
        AchievementUnlockEntity::class,
        QuizAttemptEntity::class,
        QuizAttemptAnswerEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProgressDao(): UserProgressDao
    abstract fun xpHistoryDao(): XpHistoryDao
    abstract fun achievementUnlockDao(): AchievementUnlockDao
    abstract fun quizAttemptDao(): QuizAttemptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * v1 -> v2: adds user_progress.completed_module_ids for module-completion XP.
         * Additive column with a default, so existing XP/streak rows are preserved.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE user_progress ADD COLUMN completed_module_ids TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        /**
         * v2 -> v3: adds new columns for the XP & Level-Up system redesign.
         * - user_progress: answered_question_ids, best_answer_streak, completed_questions_count, scenarios_completed
         * - xp_history: previous_xp, new_xp, previous_level, new_level, admin_note
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // user_progress new columns
                db.execSQL("ALTER TABLE user_progress ADD COLUMN answered_question_ids TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE user_progress ADD COLUMN best_answer_streak INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE user_progress ADD COLUMN completed_questions_count INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE user_progress ADD COLUMN scenarios_completed INTEGER NOT NULL DEFAULT 0")
                // xp_history new columns
                db.execSQL("ALTER TABLE xp_history ADD COLUMN previous_xp INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE xp_history ADD COLUMN new_xp INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE xp_history ADD COLUMN previous_level INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE xp_history ADD COLUMN new_level INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE xp_history ADD COLUMN admin_note TEXT DEFAULT NULL")
            }
        }

        /**
         * v3 -> v4: adds user_progress.missed_topics for the AI tutor's weak-topic tracking.
         * Additive column with a default, so existing rows are preserved.
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_progress ADD COLUMN missed_topics TEXT NOT NULL DEFAULT ''")
            }
        }

        /**
         * v4 -> v5: adds quiz_attempts + quiz_attempt_answers, this app's first CREATE TABLE
         * migration (prior migrations were all additive ALTER TABLE ADD COLUMN).
         */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quiz_attempts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        user_id TEXT NOT NULL, quiz_id TEXT NOT NULL, module_id TEXT NOT NULL,
                        quiz_title TEXT NOT NULL, difficulty TEXT NOT NULL, attempt_number INTEGER NOT NULL,
                        total_questions INTEGER NOT NULL, correct_count INTEGER NOT NULL,
                        incorrect_count INTEGER NOT NULL, unanswered_count INTEGER NOT NULL,
                        score_percent INTEGER NOT NULL, passed INTEGER NOT NULL, xp_earned INTEGER NOT NULL,
                        best_combo_streak INTEGER NOT NULL, leveled_up INTEGER NOT NULL, level_after INTEGER NOT NULL,
                        time_spent_seconds INTEGER NOT NULL, time_challenge_completed INTEGER NOT NULL,
                        started_at INTEGER NOT NULL, completed_at INTEGER NOT NULL,
                        FOREIGN KEY(user_id) REFERENCES user_progress(user_id) ON DELETE CASCADE
                    )
                    """
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempts_user_id ON quiz_attempts(user_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempts_quiz_id ON quiz_attempts(quiz_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempts_user_id_quiz_id ON quiz_attempts(user_id, quiz_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempts_completed_at ON quiz_attempts(completed_at)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS quiz_attempt_answers (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        attempt_id INTEGER NOT NULL, quiz_id TEXT NOT NULL, question_id INTEGER NOT NULL,
                        question_text TEXT NOT NULL, selected_option_index INTEGER NOT NULL,
                        correct_option_index INTEGER NOT NULL, is_correct INTEGER NOT NULL, topic TEXT NOT NULL,
                        FOREIGN KEY(attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE
                    )
                    """
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempt_answers_attempt_id ON quiz_attempt_answers(attempt_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_quiz_attempt_answers_quiz_id_question_id ON quiz_attempt_answers(quiz_id, question_id)")
            }
        }

        /**
         * v5 -> v6: enriches quiz_attempt_answers with snapshotted display text, per-question
         * XP/points, explanation, safety tip, difficulty, and answer timestamp — the fields
         * the Answer Review spec requires to render each question card without re-resolving
         * from QuizData. All columns have safe defaults so existing rows survive.
         */
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN question_number INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN selected_answer_text TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN correct_answer_text TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN points_earned INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN xp_earned INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN explanation TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN safety_tip TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN difficulty TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE quiz_attempt_answers ADD COLUMN answered_at INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gamification.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
