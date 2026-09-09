package com.example.gamifiedroadsafetyawareness.firebase

import android.util.Log
import com.example.gamifiedroadsafetyawareness.audit.AuditLog
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Cloud Synchronization Manager connecting the local Jetpack Room SQLite database
 * with Google Cloud Firestore and Firebase Authentication.
 *
 * Implements an Offline-First strategy: all operations run asynchronously in background
 * and gracefully fall back to local Room database if the device is offline or cellular
 * connectivity is unavailable.
 */
class FirebaseSyncManager {

    private val tag = "FirebaseSyncManager"

    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    init {
        ensureAuth()
    }

    private fun ensureAuth() {
        try {
            if (auth.currentUser == null) {
                auth.signInAnonymously()
                    .addOnSuccessListener {
                        Log.d(tag, "Firebase anonymous session established: ${it.user?.uid}")
                    }
                    .addOnFailureListener {
                        Log.w(tag, "Firebase anonymous auth note: ${it.message}")
                    }
            }
        } catch (e: Exception) {
            Log.w(tag, "Firebase auth init exception: ${e.message}")
        }
    }

    /**
     * Synchronize a learner or officer's training progress to Cloud Firestore.
     */
    fun syncUserProgress(
        progress: UserProgressEntity,
        displayName: String = "",
        unlockedAchievementIds: Collection<String> = emptyList()
    ) {
        try {
            val userDoc = firestore.collection(COLLECTION_USER_PROGRESS).document(progress.userId)
            val data = hashMapOf(
                "userId" to progress.userId,
                "displayName" to displayName.ifBlank { progress.userId },
                "currentXp" to progress.currentXp,
                "totalXp" to progress.totalXp,
                "currentLevel" to progress.currentLevel,
                "currentStreak" to progress.currentStreak,
                "longestStreak" to progress.longestStreak,
                "quizzesCompleted" to progress.quizzesCompleted,
                "perfectQuizCount" to progress.perfectQuizCount,
                "lastActivityDate" to (progress.lastActivityDate ?: ""),
                "unlockedBadges" to unlockedAchievementIds.toList(),
                "lastSyncedTimestamp" to System.currentTimeMillis()
            )

            userDoc.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Successfully synced progress for user: ${progress.userId}")
                }
                .addOnFailureListener { error ->
                    Log.w(tag, "Failed to sync progress to cloud (offline cache will persist): ${error.message}")
                }
        } catch (e: Exception) {
            Log.e(tag, "Firebase sync exception: ${e.message}")
        }
    }

    /**
     * Push completed quiz attempt records to Cloud Firestore for fleet and administrative tracking.
     */
    fun syncQuizAttempt(attempt: QuizAttemptEntity) {
        try {
            val docId = "${attempt.userId}_${attempt.quizId}_${attempt.completedAt}"
            val attemptDoc = firestore.collection(COLLECTION_QUIZ_ATTEMPTS).document(docId)

            val data = hashMapOf(
                "userId" to attempt.userId,
                "quizId" to attempt.quizId,
                "score" to attempt.correctCount,
                "totalQuestions" to attempt.totalQuestions,
                "percentage" to attempt.scorePercent,
                "passed" to attempt.passed,
                "timeSpentSeconds" to attempt.timeSpentSeconds,
                "timestamp" to attempt.completedAt,
                "syncedAt" to System.currentTimeMillis()
            )

            attemptDoc.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Quiz attempt logged to Firestore: $docId")
                }
                .addOnFailureListener { error ->
                    Log.w(tag, "Failed to upload quiz attempt to Firestore: ${error.message}")
                }
        } catch (e: Exception) {
            Log.e(tag, "Firebase attempt sync exception: ${e.message}")
        }
    }

    /**
     * Push critical security and administrative audit entries to Cloud Firestore.
     */
    fun syncAuditLog(log: AuditLog) {
        try {
            val docId = "audit_${log.timestampUtc}_${log.username}_${log.auditId.take(8)}"
            val auditDoc = firestore.collection(COLLECTION_AUDIT_LOGS).document(docId)

            val data = hashMapOf(
                "auditId" to log.auditId,
                "actionType" to log.actionType.name,
                "module" to log.module.name,
                "username" to log.username,
                "fullName" to log.fullName,
                "role" to log.role,
                "description" to log.description,
                "riskLevel" to log.riskLevel.name,
                "result" to log.result.name,
                "deviceInfo" to log.deviceInfo,
                "ipAddress" to log.ipAddress,
                "timestampUtc" to log.timestampUtc,
                "syncedAt" to System.currentTimeMillis()
            )

            auditDoc.set(data, SetOptions.merge())
                .addOnFailureListener { e ->
                    Log.w(tag, "Audit log failed to sync to cloud: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e(tag, "Audit sync exception: ${e.message}")
        }
    }

    /**
     * Record a user login event and update the user's active presence in Cloud Firestore for monitoring.
     */
    fun recordUserLogin(
        username: String,
        displayName: String,
        role: String,
        isSuccess: Boolean = true,
        failureReason: String? = null,
        deviceInfo: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
    ) {
        try {
            val now = System.currentTimeMillis()
            val eventType = if (isSuccess) "LOGIN" else "FAILED_LOGIN"
            val eventId = "${username}_${eventType.lowercase()}_$now"

            // 1. Append immutable event to user_logins collection for timeline monitoring
            val loginEvent = hashMapOf(
                "username" to username,
                "displayName" to displayName,
                "role" to role,
                "eventType" to eventType,
                "status" to if (isSuccess) "SUCCESS" else "FAILED",
                "failureReason" to (failureReason ?: ""),
                "deviceInfo" to deviceInfo,
                "androidVersion" to android.os.Build.VERSION.RELEASE,
                "timestamp" to now,
                "timestampUtc" to now
            )
            firestore.collection(COLLECTION_USER_LOGINS).document(eventId)
                .set(loginEvent, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Logged login event to Firestore: $eventId")
                }
                .addOnFailureListener { e ->
                    Log.w(tag, "Failed to log login event to Firestore: ${e.message}")
                }

            // 2. Update real-time presence and account status in users collection
            if (isSuccess) {
                val userRef = firestore.collection(COLLECTION_USERS).document(username)
                val userState = hashMapOf(
                    "username" to username,
                    "displayName" to displayName,
                    "role" to role,
                    "isOnline" to true,
                    "lastLoginAt" to now,
                    "lastSeenAt" to now,
                    "deviceInfo" to deviceInfo,
                    "loginCount" to com.google.firebase.firestore.FieldValue.increment(1)
                )
                userRef.set(userState, SetOptions.merge())
            }
        } catch (e: Exception) {
            Log.e(tag, "recordUserLogin exception: ${e.message}")
        }
    }

    /**
     * Record a user logout event and update status to offline in Cloud Firestore.
     */
    fun recordUserLogout(
        username: String,
        displayName: String,
        role: String,
        deviceInfo: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
    ) {
        try {
            val now = System.currentTimeMillis()
            val eventId = "${username}_logout_$now"

            val logoutEvent = hashMapOf(
                "username" to username,
                "displayName" to displayName,
                "role" to role,
                "eventType" to "LOGOUT",
                "status" to "SUCCESS",
                "deviceInfo" to deviceInfo,
                "androidVersion" to android.os.Build.VERSION.RELEASE,
                "timestamp" to now,
                "timestampUtc" to now
            )
            firestore.collection(COLLECTION_USER_LOGINS).document(eventId)
                .set(logoutEvent, SetOptions.merge())

            // Update user presence to offline
            val userRef = firestore.collection(COLLECTION_USERS).document(username)
            val userState = hashMapOf(
                "isOnline" to false,
                "lastLogoutAt" to now,
                "lastSeenAt" to now
            )
            userRef.set(userState, SetOptions.merge())
        } catch (e: Exception) {
            Log.e(tag, "recordUserLogout exception: ${e.message}")
        }
    }

    /**
     * Sync newly registered user account details to Cloud Firestore.
     */
    fun syncRegisteredUser(
        username: String,
        displayName: String,
        role: String,
        gender: String = "",
        age: Int? = null,
        contactNumber: String = ""
    ) {
        try {
            val userRef = firestore.collection(COLLECTION_USERS).document(username)
            val data = hashMapOf(
                "username" to username,
                "displayName" to displayName,
                "role" to role,
                "gender" to gender,
                "age" to (age ?: 0),
                "contactNumber" to contactNumber,
                "createdAt" to System.currentTimeMillis(),
                "isOnline" to false,
                "isActive" to true
            )
            userRef.set(data, SetOptions.merge())
        } catch (e: Exception) {
            Log.e(tag, "syncRegisteredUser exception: ${e.message}")
        }
    }

    /**
     * Fetch recent user login events from Cloud Firestore for administrative monitoring.
     */
    suspend fun getRecentCloudLogins(limit: Long = 50): List<CloudLoginEntry> {
        return try {
            val snapshot = firestore.collection(COLLECTION_USER_LOGINS)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    CloudLoginEntry(
                        username = doc.safeString("username"),
                        displayName = doc.safeString("displayName"),
                        role = doc.safeString("role", "USER"),
                        eventType = doc.safeString("eventType", "LOGIN"),
                        status = doc.safeString("status", "SUCCESS"),
                        failureReason = doc.getString("failureReason"),
                        deviceInfo = doc.safeString("deviceInfo"),
                        timestamp = doc.safeLong("timestamp", 0L)
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Could not fetch cloud login entries: ${e.message}")
            emptyList()
        }
    }

    /**
     * Fetch real-time cloud leaderboard ranks across all registered devices.
     */
    suspend fun getCloudLeaderboard(limit: Long = 25): List<CloudLeaderboardEntry> {
        return try {
            val snapshot = firestore.collection(COLLECTION_USER_PROGRESS)
                .orderBy("totalXp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents.mapIndexedNotNull { index, doc ->
                try {
                    CloudLeaderboardEntry(
                        rank = index + 1,
                        userId = doc.safeString("userId", "Anonymous"),
                        displayName = doc.safeString("displayName", doc.safeString("userId", "User")),
                        totalXp = doc.safeInt("totalXp", 0),
                        currentLevel = doc.safeInt("currentLevel", 1),
                        streak = doc.safeInt("currentStreak", 0)
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Could not fetch cloud leaderboard: ${e.message}")
            emptyList()
        }
    }

    /**
     * Fetch all users tracked in Cloud Firestore along with their online presence and stats.
     */
    suspend fun getCloudUsers(): List<CloudUserStatus> {
        return try {
            val snapshot = firestore.collection(COLLECTION_USERS)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    CloudUserStatus(
                        username = doc.safeString("username", doc.id),
                        displayName = doc.safeString("displayName", doc.id),
                        role = doc.safeString("role", "USER"),
                        isOnline = doc.safeBoolean("isOnline", false),
                        lastLoginAt = doc.safeLong("lastLoginAt", 0L),
                        lastLogoutAt = doc.safeLong("lastLogoutAt", 0L),
                        deviceInfo = doc.safeString("deviceInfo", ""),
                        loginCount = doc.safeInt("loginCount", 0)
                    )
                } catch (e: Exception) {
                    null
                }
            }.sortedByDescending { it.lastLoginAt }
        } catch (e: Exception) {
            Log.w(tag, "Could not fetch cloud users: ${e.message}")
            emptyList()
        }
    }


    /**
     * Real-time stream of all users tracked in Cloud Firestore along with their online presence and stats.
     */
    fun observeCloudUsers(): Flow<List<CloudUserStatus>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_USERS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "observeCloudUsers snapshot error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        try {
                            CloudUserStatus(
                                username = doc.safeString("username", doc.id),
                                displayName = doc.safeString("displayName", doc.id),
                                role = doc.safeString("role", "USER"),
                                isOnline = doc.safeBoolean("isOnline", false),
                                lastLoginAt = doc.safeLong("lastLoginAt", 0L),
                                lastLogoutAt = doc.safeLong("lastLogoutAt", 0L),
                                deviceInfo = doc.safeString("deviceInfo", ""),
                                loginCount = doc.safeInt("loginCount", 0)
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }.sortedByDescending { it.lastLoginAt }
                    trySend(users)
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Real-time stream of user login, logout, and security events from all connected mobile phones.
     */
    fun observeRecentCloudLogins(limit: Long = 100): Flow<List<CloudLoginEntry>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_USER_LOGINS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "observeRecentCloudLogins error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val logins = snapshot.documents.mapNotNull { doc ->
                        try {
                            CloudLoginEntry(
                                username = doc.safeString("username"),
                                displayName = doc.safeString("displayName"),
                                role = doc.safeString("role", "USER"),
                                eventType = doc.safeString("eventType", "LOGIN"),
                                status = doc.safeString("status", "SUCCESS"),
                                failureReason = doc.getString("failureReason"),
                                deviceInfo = doc.safeString("deviceInfo"),
                                timestamp = doc.safeLong("timestamp", 0L)
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(logins)
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Real-time stream of quiz attempts and exam results submitted from all phones.
     */
    fun observeCloudQuizAttempts(limit: Long = 50): Flow<List<CloudQuizAttemptEntry>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_QUIZ_ATTEMPTS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "observeCloudQuizAttempts error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val attempts = snapshot.documents.mapNotNull { doc ->
                        try {
                            CloudQuizAttemptEntry(
                                userId = doc.safeString("userId"),
                                quizId = doc.safeString("quizId"),
                                score = doc.safeInt("score", 0),
                                totalQuestions = doc.safeInt("totalQuestions", 0),
                                percentage = doc.safeInt("percentage", 0),
                                passed = doc.safeBoolean("passed", false),
                                timeSpentSeconds = doc.safeInt("timeSpentSeconds", 0),
                                timestamp = doc.safeLong("timestamp", 0L)
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(attempts)
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Fetch recent quiz attempts from Cloud Firestore.
     */
    suspend fun getCloudQuizAttempts(limit: Long = 50): List<CloudQuizAttemptEntry> {
        return try {
            val snapshot = firestore.collection(COLLECTION_QUIZ_ATTEMPTS)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    CloudQuizAttemptEntry(
                        userId = doc.safeString("userId"),
                        quizId = doc.safeString("quizId"),
                        score = doc.safeInt("score", 0),
                        totalQuestions = doc.safeInt("totalQuestions", 0),
                        percentage = doc.safeInt("percentage", 0),
                        passed = doc.safeBoolean("passed", false),
                        timeSpentSeconds = doc.safeInt("timeSpentSeconds", 0),
                        timestamp = doc.safeLong("timestamp", 0L)
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Could not fetch cloud quiz attempts: ${e.message}")
            emptyList()
        }
    }

    /**
     * Fetch user progress details for a specific user.
     */
    suspend fun getCloudUserProgress(userId: String): CloudUserProgressDetail? {
        return try {
            val doc = firestore.collection(COLLECTION_USER_PROGRESS).document(userId).get().await()
            if (doc.exists()) {
                @Suppress("UNCHECKED_CAST")
                val badges = (doc.get("unlockedBadges") as? List<String>) ?: emptyList()
                CloudUserProgressDetail(
                    userId = doc.safeString("userId", userId),
                    displayName = doc.safeString("displayName", userId),
                    currentXp = doc.safeInt("currentXp", 0),
                    totalXp = doc.safeInt("totalXp", 0),
                    currentLevel = doc.safeInt("currentLevel", 1),
                    currentStreak = doc.safeInt("currentStreak", 0),
                    quizzesCompleted = doc.safeInt("quizzesCompleted", 0),
                    perfectQuizCount = doc.safeInt("perfectQuizCount", 0),
                    unlockedBadges = badges,
                    lastActivityDate = doc.safeString("lastActivityDate", ""),
                    lastSyncedTimestamp = doc.safeLong("lastSyncedTimestamp", 0L)
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(tag, "Could not fetch user progress for $userId: ${e.message}")
            null
        }
    }

    /**
     * Real-time sync listener for module & quiz availability settings published by the web admin.
     */
    fun listenToModuleSettings() {
        try {
            firestore.collection("system_settings").document("modules")
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
                    val data = snapshot.data ?: return@addSnapshotListener
                    data.forEach { (key, value) ->
                        if (value is Boolean) {
                            com.example.gamifiedroadsafetyawareness.model.GamificationConstants.ContentSettings.setModuleEnabled(key, value)
                        }
                    }
                    Log.d(tag, "Synced module availability settings from cloud: $data")
                }
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach module settings listener: ${e.message}")
        }
    }

    /**
     * Updates module availability setting in Firestore to synchronize across all clients.
     */
    fun syncModuleSetting(moduleId: String, enabled: Boolean) {
        try {
            val payload = mapOf(
                moduleId to enabled,
                "updatedAt" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("system_settings").document("modules")
                .set(payload, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Module $moduleId availability set to $enabled in cloud.")
                }
                .addOnFailureListener { e ->
                    Log.w(tag, "Failed to sync module setting to cloud: ${e.message}")
                }
        } catch (e: Exception) {
            Log.w(tag, "Error syncing module setting: ${e.message}")
        }
    }

    companion object {
        const val COLLECTION_USER_PROGRESS = "user_progress"
        const val COLLECTION_QUIZ_ATTEMPTS = "quiz_attempts"
        const val COLLECTION_AUDIT_LOGS = "audit_logs"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_USER_LOGINS = "user_logins"

        @Volatile
        private var INSTANCE: FirebaseSyncManager? = null

        fun getInstance(): FirebaseSyncManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FirebaseSyncManager().also { INSTANCE = it }
            }
    }
}

/**
 * Data model for cloud-synchronized driver leaderboard ranking
 */
data class CloudLeaderboardEntry(
    val rank: Int,
    val userId: String,
    val displayName: String,
    val totalXp: Int,
    val currentLevel: Int,
    val streak: Int
)

/**
 * Data model for cloud-synchronized user login monitoring
 */
data class CloudLoginEntry(
    val username: String,
    val displayName: String,
    val role: String,
    val eventType: String,
    val status: String,
    val failureReason: String?,
    val deviceInfo: String,
    val timestamp: Long
)

/**
 * Data model for cloud-synchronized user presence and account monitoring
 */
data class CloudUserStatus(
    val username: String,
    val displayName: String,
    val role: String,
    val isOnline: Boolean,
    val lastLoginAt: Long,
    val lastLogoutAt: Long,
    val deviceInfo: String,
    val loginCount: Int
)

/**
 * Data model for cloud-synchronized quiz attempt results
 */
data class CloudQuizAttemptEntry(
    val userId: String,
    val quizId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val passed: Boolean,
    val timeSpentSeconds: Int,
    val timestamp: Long
)

/**
 * Data model for comprehensive cloud user progress details
 */
data class CloudUserProgressDetail(
    val userId: String,
    val displayName: String,
    val currentXp: Int,
    val totalXp: Int,
    val currentLevel: Int,
    val currentStreak: Int,
    val quizzesCompleted: Int,
    val perfectQuizCount: Int,
    val unlockedBadges: List<String>,
    val lastActivityDate: String,
    val lastSyncedTimestamp: Long
)

// ── DocumentSnapshot Type-Safe Helper Extensions ──────────────────────────────
private fun com.google.firebase.firestore.DocumentSnapshot.safeLong(field: String, default: Long = 0L): Long {
    val raw = this.get(field) ?: return default
    return when (raw) {
        is Number -> raw.toLong()
        is com.google.firebase.Timestamp -> raw.toDate().time
        is java.util.Date -> raw.time
        is String -> raw.toLongOrNull() ?: default
        else -> default
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.safeInt(field: String, default: Int = 0): Int {
    val raw = this.get(field) ?: return default
    return when (raw) {
        is Number -> raw.toInt()
        is String -> raw.toIntOrNull() ?: default
        else -> default
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.safeBoolean(field: String, default: Boolean = false): Boolean {
    val raw = this.get(field) ?: return default
    return when (raw) {
        is Boolean -> raw
        is String -> raw.toBooleanStrictOrNull() ?: default
        is Number -> raw.toInt() != 0
        else -> default
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.safeString(field: String, default: String = ""): String {
    val raw = this.get(field) ?: return default
    return raw.toString()
}
