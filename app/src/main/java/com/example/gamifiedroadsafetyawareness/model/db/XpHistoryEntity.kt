package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "xp_history",
    foreignKeys = [
        ForeignKey(
            entity = UserProgressEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id"), Index("created_at")]
)
data class XpHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "activity_type")
    val activityType: String,

    @ColumnInfo(name = "activity_name")
    val activityName: String,

    @ColumnInfo(name = "xp_earned")
    val xpEarned: Int,

    @ColumnInfo(name = "bonus_xp")
    val bonusXp: Int,

    @ColumnInfo(name = "multiplier")
    val multiplier: Float,

    @ColumnInfo(name = "streak_count")
    val streakCount: Int,

    @ColumnInfo(name = "total_awarded")
    val totalAwarded: Int,

    @ColumnInfo(name = "previous_xp")
    val previousXp: Int = 0,

    @ColumnInfo(name = "new_xp")
    val newXp: Int = 0,

    @ColumnInfo(name = "previous_level")
    val previousLevel: Int = 1,

    @ColumnInfo(name = "new_level")
    val newLevel: Int = 1,

    @ColumnInfo(name = "admin_note")
    val adminNote: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
