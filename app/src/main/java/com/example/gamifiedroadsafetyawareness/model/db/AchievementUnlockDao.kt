package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class AchievementUnlockCountRow(
    @ColumnInfo(name = "achievement_id") val achievementId: String,
    @ColumnInfo(name = "cnt") val count: Int
)

data class AchievementUnlockOwnerRow(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "achievement_id") val achievementId: String
)

@Dao
interface AchievementUnlockDao {
    @Query("SELECT achievement_id FROM achievement_unlocks WHERE user_id = :userId")
    suspend fun getUnlockedIds(userId: String): List<String>

    @Insert
    suspend fun insert(entity: AchievementUnlockEntity)

    @Query("SELECT achievement_id, COUNT(*) as cnt FROM achievement_unlocks GROUP BY achievement_id")
    suspend fun getUnlockCounts(): List<AchievementUnlockCountRow>

    @Query("SELECT user_id, achievement_id FROM achievement_unlocks")
    suspend fun getAllUnlocks(): List<AchievementUnlockOwnerRow>
}
