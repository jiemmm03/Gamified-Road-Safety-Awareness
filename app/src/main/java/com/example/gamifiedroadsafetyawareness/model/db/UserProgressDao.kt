package com.example.gamifiedroadsafetyawareness.model.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE user_id = :userId")
    suspend fun get(userId: String): UserProgressEntity?

    @Query("SELECT * FROM user_progress WHERE user_id = :userId")
    fun observe(userId: String): Flow<UserProgressEntity?>

    @Upsert
    suspend fun upsert(entity: UserProgressEntity)

    @Query("SELECT * FROM user_progress WHERE user_id IN (:userIds)")
    suspend fun getAll(userIds: List<String>): List<UserProgressEntity>

    @Query("SELECT * FROM user_progress ORDER BY total_xp DESC LIMIT :limit")
    suspend fun getTopByXp(limit: Int): List<UserProgressEntity>

    @Query("SELECT * FROM user_progress ORDER BY total_xp DESC")
    suspend fun getAllSorted(): List<UserProgressEntity>
}
