package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "achievement_unlocks",
    indices = [Index(value = ["user_id", "achievement_id"], unique = true)]
)
data class AchievementUnlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "achievement_id")
    val achievementId: String,

    @ColumnInfo(name = "unlocked_at")
    val unlockedAt: Long = System.currentTimeMillis()
)
