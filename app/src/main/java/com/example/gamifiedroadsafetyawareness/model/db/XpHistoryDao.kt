package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class WeeklyXpRow(
    @androidx.room.ColumnInfo(name = "user_id") val userId: String,
    @androidx.room.ColumnInfo(name = "weeklyXp") val weeklyXp: Int
)

@Dao
interface XpHistoryDao {
    @Insert
    suspend fun insert(entity: XpHistoryEntity): Long

    @Query("SELECT * FROM xp_history WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit")
    suspend fun getRecentForUser(userId: String, limit: Int): List<XpHistoryEntity>

    @Query(
        """
        SELECT user_id, SUM(total_awarded) as weeklyXp
        FROM xp_history
        WHERE created_at >= :weekStartMillis AND user_id IN (:userIds)
        GROUP BY user_id
        ORDER BY weeklyXp DESC
        """
    )
    suspend fun getWeeklyXpForUsers(weekStartMillis: Long, userIds: List<String>): List<WeeklyXpRow>

    @Query("SELECT * FROM xp_history ORDER BY created_at DESC LIMIT :limit")
    suspend fun getRecentAll(limit: Int): List<XpHistoryEntity>
}
