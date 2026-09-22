package com.example.gamifiedroadsafetyawareness.firebase

import android.util.Log
import com.example.gamifiedroadsafetyawareness.auth.UserRole
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import com.example.gamifiedroadsafetyawareness.model.AppConfig
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants

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

    private val _appConfigFlow = MutableStateFlow(AppConfig())
    val appConfigFlow: StateFlow<AppConfig> = _appConfigFlow.asStateFlow()

    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    init {
        ensureAuth()
        listenToAppConfig()
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
     * Note: Administrative accounts are explicitly excluded from user_progress syncing
     * to prevent them from recording to driver rankings or appearing on leaderboards.
     */
    fun syncUserProgress(
        progress: UserProgressEntity,
        displayName: String = "",
        unlockedAchievementIds: Collection<String> = emptyList()
    ) {
        if (progress.userId.equals("admin", ignoreCase = true)) {
            Log.d(tag, "Skipping cloud sync for admin account to prevent recording to leaderboard/ranks")
            return
        }
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
                "moduleId" to attempt.moduleId,
                "quizTitle" to attempt.quizTitle,
                "difficulty" to attempt.difficulty,
                "score" to attempt.correctCount,
                "totalQuestions" to attempt.totalQuestions,
                "percentage" to attempt.scorePercent,
                "passed" to attempt.passed,
                "timeSpentSeconds" to attempt.timeSpentSeconds,
                "selectedLanguage" to attempt.selectedLanguage,
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

            // Also mirror structured event to activities and activity_logs
            // Resolve display name and role from the users collection for accurate logging
            firestore.collection(COLLECTION_USERS).document(attempt.userId).get()
                .addOnSuccessListener { userDoc ->
                    val resolvedName = userDoc?.getString("displayName") ?: attempt.userId
                    val resolvedRole = userDoc?.getString("role") ?: "user"
                    recordActivity(
                        userId = attempt.userId,
                        username = attempt.userId,
                        displayName = resolvedName,
                        role = resolvedRole,
                        activityType = "Quiz",
                        action = "Completed Quiz: ${attempt.quizTitle} (${attempt.correctCount}/${attempt.totalQuestions}, ${attempt.scorePercent.toInt()}%)",
                        description = "Difficulty: ${attempt.difficulty}, Passed: ${attempt.passed}",
                        assessmentId = attempt.quizId,
                        xpEarned = attempt.xpEarned,
                        status = if (attempt.passed) "Passed" else "Failed"
                    )
                }
                .addOnFailureListener {
                    // Fallback: use userId if user doc lookup fails
                    recordActivity(
                        userId = attempt.userId,
                        username = attempt.userId,
                        displayName = attempt.userId,
                        role = "user",
                        activityType = "Quiz",
                        action = "Completed Quiz: ${attempt.quizTitle} (${attempt.correctCount}/${attempt.totalQuestions}, ${attempt.scorePercent.toInt()}%)",
                        description = "Difficulty: ${attempt.difficulty}, Passed: ${attempt.passed}",
                        assessmentId = attempt.quizId,
                        xpEarned = attempt.xpEarned,
                        status = if (attempt.passed) "Passed" else "Failed"
                    )
                }
        } catch (e: Exception) {
            Log.e(tag, "Firebase attempt sync exception: ${e.message}")
        }
    }

    /**
     * Record a single AI tutor interaction to Cloud Firestore (`ai_interactions` collection).
     * Called by [com.example.gamifiedroadsafetyawareness.model.AiTutorEngine] after every turn
     * so the Admin Web panel's AI Activity Feed is populated with real user queries.
     *
     * @param userId   The logged-in user's username/uid.
     * @param prompt   The user's message or query (truncated to 500 chars for Firestore efficiency).
     * @param response The AI's response text (truncated to 500 chars).
     * @param topic    The road-safety topic classification (e.g., "Right-of-Way").
     * @param language Detected language: "EN" or "FIL".
     */
    fun syncAiInteraction(
        userId: String,
        prompt: String,
        response: String,
        topic: String,
        language: String = "EN"
    ) {
        try {
            val docId = "ai_${System.currentTimeMillis()}_${userId.take(8)}"
            val data = hashMapOf(
                "userId" to userId,
                "prompt" to prompt.take(500),
                "response" to response.take(500),
                "topic" to topic,
                "language" to language,
                "timestamp" to com.google.firebase.Timestamp.now(),
                "source" to "mobile_app"
            )
            firestore.collection(COLLECTION_AI_INTERACTIONS).document(docId)
                .set(data)
                .addOnFailureListener { e ->
                    Log.w(tag, "AI interaction sync failed (non-critical): ${e.message}")
                }
        } catch (e: Exception) {
            Log.w(tag, "AI interaction sync exception (non-critical): ${e.message}")
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
                "adminId" to log.username,
                "username" to log.username,
                "fullName" to log.fullName,
                "role" to log.role,
                "action" to log.actionType.name,
                "actionType" to log.actionType.name,
                "module" to log.module.name,
                "description" to log.description,
                "targetUser" to (log.newValue ?: log.previousValue ?: ""),
                "details" to log.description,
                "riskLevel" to log.riskLevel.name,
                "result" to log.result.name,
                "deviceInfo" to log.deviceInfo,
                "ipAddress" to log.ipAddress,
                "timestamp" to log.timestampUtc,
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
     * Record a comprehensive, structured activity log to Cloud Firestore.
     * Primary collection: `activities`
     * Mirrored collection: `activity_logs` (backward compatibility)
     */
    fun recordActivity(
        userId: String,
        username: String,
        displayName: String = "",
        role: String = "Learner",
        activityType: String = "Session",
        action: String,
        description: String = "",
        moduleId: String? = null,
        assessmentId: String? = null,
        xpEarned: Int? = null,
        details: String = "",
        status: String = "Completed",
        sessionId: String = "session_${System.currentTimeMillis()}",
        deviceId: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
        metadata: Map<String, Any> = emptyMap()
    ) {
        try {
            val now = System.currentTimeMillis()
            val resolvedDisplay = if (displayName.isNotBlank()) displayName else username
            val activityId = "act_${username}_${now}_${(100..999).random()}"
            val finalDesc = if (description.isNotBlank()) description else details

            val data = hashMapOf<String, Any>(
                "activityId" to activityId,
                "userId" to userId,
                "username" to username,
                "displayName" to resolvedDisplay,
                "role" to role,
                "activityType" to activityType,
                "action" to action,
                "description" to finalDesc,
                "details" to finalDesc,
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "timestampMillis" to now,
                "createdAtMillis" to now,
                "sessionId" to sessionId,
                "deviceId" to deviceId,
                "status" to status
            )
            if (moduleId != null) data["moduleId"] = moduleId
            if (assessmentId != null) data["assessmentId"] = assessmentId
            if (xpEarned != null) data["xpEarned"] = xpEarned
            if (metadata.isNotEmpty()) data["metadata"] = metadata

            // 1. Primary dedicated collection: activities
            firestore.collection(COLLECTION_ACTIVITIES).document(activityId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Activity record saved to $COLLECTION_ACTIVITIES: $activityId ($action)")
                }
                .addOnFailureListener { e ->
                    Log.w(tag, "Failed writing to $COLLECTION_ACTIVITIES: ${e.message}")
                }

            // 2. Mirrored collection: activity_logs
            firestore.collection(COLLECTION_ACTIVITY_LOGS).document(activityId)
                .set(data, SetOptions.merge())
        } catch (e: Exception) {
            Log.e(tag, "recordActivity exception: ${e.message}")
        }
    }

    /**
     * Backward-compatible delegation to [recordActivity]
     */
    fun recordActivityLog(
        userId: String,
        username: String,
        role: String,
        action: String,
        activityType: String = "Session",
        details: String = "",
        status: String = "Active",
        sessionId: String = "session_${System.currentTimeMillis()}",
        device: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
    ) {
        recordActivity(
            userId = userId,
            username = username,
            displayName = username,
            role = role,
            activityType = activityType,
            action = action,
            description = details,
            status = status,
            sessionId = sessionId,
            deviceId = device
        )
    }

    /**
     * Record a user login event and update the user's active presence in Cloud Firestore for monitoring.
     * Guaranteed to be called ONLY on successful explicit authentication.
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
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "timestampMillis" to now,
                "timestampUtc" to now
            )
            firestore.collection(COLLECTION_USER_LOGINS).document(eventId)
                .set(loginEvent, SetOptions.merge())

            // 2. Record to activities collection
            recordActivity(
                userId = username,
                username = username,
                displayName = displayName,
                role = role,
                activityType = "Login",
                action = if (isSuccess) "Logged in" else "Failed login attempt",
                description = if (isSuccess) "Signed in successfully on $deviceInfo" else "Reason: ${failureReason ?: "Invalid credentials"}",
                status = if (isSuccess) "Active" else "Failed",
                deviceId = deviceInfo
            )

            // 3. Update real-time presence and account status in users collection
            if (isSuccess) {
                val userRef = firestore.collection(COLLECTION_USERS).document(username)
                val userState = hashMapOf(
                    "username" to username,
                    "displayName" to displayName,
                    "role" to role,
                    "isOnline" to true,
                    "lastLoginAt" to now,
                    "lastSeenAt" to now,
                    "lastActiveTimestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
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
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "timestampMillis" to now,
                "timestampUtc" to now
            )
            firestore.collection(COLLECTION_USER_LOGINS).document(eventId)
                .set(logoutEvent, SetOptions.merge())

            // Record to activities collection
            recordActivity(
                userId = username,
                username = username,
                displayName = displayName,
                role = role,
                activityType = "Logout",
                action = "Logged out",
                description = "User ended session on $deviceInfo",
                status = "Ended",
                deviceId = deviceInfo
            )

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
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "createdAtMillis" to System.currentTimeMillis(),
                "isOnline" to false,
                "isActive" to true
            )
            userRef.set(data, SetOptions.merge())

            // Also record to activities collection
            recordActivity(
                userId = username,
                username = username,
                displayName = displayName,
                role = role,
                activityType = "Registration",
                action = "Registered an account",
                description = "New account registered ($role) — $displayName",
                status = "Created"
            )
        } catch (e: Exception) {
            Log.e(tag, "syncRegisteredUser exception: ${e.message}")
        }
    }

    /**
     * Sync a display name change to Cloud Firestore users collection.
     * Called when a user updates their display name from the Profile screen.
     */
    fun syncDisplayNameChange(username: String, newDisplayName: String) {
        try {
            val userRef = firestore.collection(COLLECTION_USERS).document(username)
            val data = hashMapOf(
                "displayName" to newDisplayName,
                "lastUpdatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            userRef.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Display name updated in cloud for $username: $newDisplayName")
                }
                .addOnFailureListener { e ->
                    Log.w(tag, "Failed to sync display name change: ${e.message}")
                }

            // Also update in user_progress for leaderboard accuracy
            val progressRef = firestore.collection(COLLECTION_USER_PROGRESS).document(username)
            progressRef.set(hashMapOf("displayName" to newDisplayName), SetOptions.merge())

            // Record activity
            recordActivity(
                userId = username,
                username = username,
                displayName = newDisplayName,
                role = "Learner",
                activityType = "Profile",
                action = "Updated profile display name",
                description = "Changed display name to: $newDisplayName",
                status = "Updated"
            )
        } catch (e: Exception) {
            Log.e(tag, "syncDisplayNameChange exception: ${e.message}")
        }
    }

    /**
     * Sync an account activation/deactivation status change to Cloud Firestore.
     * Called when an admin activates or deactivates a user account.
     */
    fun syncAccountStatusChange(username: String, isActive: Boolean, adminUsername: String) {
        try {
            val userRef = firestore.collection(COLLECTION_USERS).document(username)
            val data = hashMapOf(
                "isActive" to isActive,
                "lastStatusChangeAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "statusChangedBy" to adminUsername
            )
            userRef.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(tag, "Account status updated for $username: isActive=$isActive")
                }
                .addOnFailureListener { e ->
                    Log.w(tag, "Failed to sync account status change: ${e.message}")
                }

            // Record activity
            recordActivity(
                userId = adminUsername,
                username = adminUsername,
                displayName = adminUsername,
                role = "Admin",
                activityType = "Admin Action",
                action = "${if (isActive) "Activated" else "Deactivated"} account: @$username",
                description = "User status changed by admin @$adminUsername",
                status = if (isActive) "Activated" else "Deactivated"
            )
        } catch (e: Exception) {
            Log.e(tag, "syncAccountStatusChange exception: ${e.message}")
        }
    }

    /**
     * Record module progress (start / completion) activity.
     */
    fun recordModuleActivity(
        userId: String,
        username: String,
        displayName: String,
        role: String,
        moduleId: String,
        moduleTitle: String,
        isCompleted: Boolean,
        xpEarned: Int? = null
    ) {
        val action = if (isCompleted) "Completed Module: $moduleTitle" else "Started Module: $moduleTitle"
        recordActivity(
            userId = userId,
            username = username,
            displayName = displayName,
            role = role,
            activityType = "Module",
            action = action,
            description = if (isCompleted) "Successfully finished all sections in $moduleTitle" else "Begun learning $moduleTitle",
            moduleId = moduleId,
            xpEarned = xpEarned,
            status = if (isCompleted) "Completed" else "In Progress"
        )
    }

    /**
     * Record gamification achievement, level up, or XP award.
     */
    fun recordGamificationActivity(
        userId: String,
        username: String,
        displayName: String,
        role: String,
        activityType: String,
        action: String,
        xpEarned: Int? = null,
        description: String = ""
    ) {
        recordActivity(
            userId = userId,
            username = username,
            displayName = displayName,
            role = role,
            activityType = activityType,
            action = action,
            description = description,
            xpEarned = xpEarned,
            status = "Awarded"
        )
    }

    /**
     * Record system settings change (e.g. language change).
     */
    fun recordSettingsActivity(
        userId: String,
        username: String,
        displayName: String,
        role: String,
        action: String,
        description: String
    ) {
        recordActivity(
            userId = userId,
            username = username,
            displayName = displayName,
            role = role,
            activityType = "Settings",
            action = action,
            description = description,
            status = "Updated"
        )
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
     * Administrative accounts are excluded from learner leaderboard rankings.
     */
    suspend fun getCloudLeaderboard(limit: Long = 25): List<CloudLeaderboardEntry> {
        return try {
            val snapshot = firestore.collection(COLLECTION_USER_PROGRESS)
                .orderBy("totalXp", Query.Direction.DESCENDING)
                .limit(limit + 10)
                .get()
                .await()

            snapshot.documents
                .mapNotNull { doc ->
                    val userId = doc.safeString("userId", "Anonymous")
                    val role = doc.safeString("role", "").lowercase()
                    if (userId.equals("admin", ignoreCase = true) || role == "admin" || role == "officer") {
                        null
                    } else {
                        doc
                    }
                }
                .take(limit.toInt())
                .mapIndexedNotNull { index, doc ->
                    try {
                        val userId = doc.safeString("userId", "Anonymous")
                        CloudLeaderboardEntry(
                            rank = index + 1,
                            userId = userId,
                            displayName = doc.safeString("displayName", userId),
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
     * Real-time stream to observe role updates for a specific user.
     * Listens to users/{username} in Firestore to detect when an administrator updates their role.
     */
    fun observeUserRole(username: String): Flow<UserRole> = callbackFlow {
        val trimmed = username.trim().lowercase()
        if (trimmed.isBlank()) {
            trySend(UserRole.USER)
            close()
            return@callbackFlow
        }

        val listener = firestore.collection(COLLECTION_USERS).document(trimmed)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(tag, "observeUserRole listener error for $trimmed: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val rawRole = snapshot.safeString("role", snapshot.safeString("accountRole", "user"))
                    val role = UserRole.fromRoleString(rawRole)
                    trySend(role)
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Role fetch result hierarchy for strict and safe role evaluation.
     */
    sealed class RoleFetchResult {
        data class Success(val role: UserRole) : RoleFetchResult()
        object MissingProfile : RoleFetchResult()
        object MissingRole : RoleFetchResult()
        data class InvalidRole(val raw: String) : RoleFetchResult()
        data class NetworkFailure(val message: String) : RoleFetchResult()
    }

    /**
     * Detailed role retrieval from Firestore users/{username} with granular error classifications.
     */
    suspend fun fetchUserRoleDetailed(username: String): RoleFetchResult {
        val trimmed = username.trim().lowercase()
        if (trimmed.isBlank()) return RoleFetchResult.MissingProfile
        return try {
            val doc = firestore.collection(COLLECTION_USERS).document(trimmed).get().await()
            if (!doc.exists()) {
                RoleFetchResult.MissingProfile
            } else {
                val rawRole = doc.getString("role") ?: doc.getString("accountRole")
                if (rawRole.isNullOrBlank()) {
                    RoleFetchResult.MissingRole
                } else {
                    val parsed = UserRole.parseRoleOrNull(rawRole)
                    if (parsed != null) {
                        RoleFetchResult.Success(parsed)
                    } else {
                        Log.w(tag, "Invalid account role '$rawRole' detected for user '$trimmed'")
                        RoleFetchResult.InvalidRole(rawRole)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "fetchUserRoleDetailed failed for $trimmed: ${e.message}")
            RoleFetchResult.NetworkFailure(e.message ?: "Network error connecting to Firebase")
        }
    }

    /**
     * Data model for cloud-registered user profiles.
     */
    data class CloudUserData(
        val username: String,
        val password: String,
        val role: UserRole,
        val displayName: String,
        val contact: String,
        val gender: String,
        val isActive: Boolean
    )

    /**
     * Retrieve user profile and credentials from Firestore for cross-platform login sync.
     */
    fun fetchCloudUserDataBlocking(username: String, timeoutSeconds: Long = 4): CloudUserData? {
        val trimmed = username.trim().lowercase()
        if (trimmed.isBlank()) return null
        return try {
            val task = firestore.collection(COLLECTION_USERS).document(trimmed).get()
            val doc = com.google.android.gms.tasks.Tasks.await(task, timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS)
            if (doc != null && doc.exists()) {
                val cloudPass = doc.getString("password") ?: ""
                val cloudRoleStr = doc.getString("role") ?: doc.getString("accountRole") ?: "user"
                // Read displayName consistently — fall back to fullName/name for legacy data
                val cloudName = doc.getString("displayName")
                    ?: doc.getString("fullName")
                    ?: doc.getString("name")
                    ?: trimmed
                val cloudContact = doc.getString("contactNumber")
                    ?: doc.getString("contact")
                    ?: doc.getString("phone")
                    ?: ""
                val cloudGender = doc.getString("gender") ?: ""
                val cloudActive = doc.getBoolean("isActive") ?: true
                val parsedRole = UserRole.fromRoleString(cloudRoleStr)
                CloudUserData(
                    username = trimmed,
                    password = cloudPass,
                    role = parsedRole,
                    displayName = cloudName,
                    contact = cloudContact,
                    gender = cloudGender,
                    isActive = cloudActive
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(tag, "fetchCloudUserDataBlocking failed for $trimmed: ${e.message}")
            null
        }
    }

    /**
     * Fetch user's authoritative role directly from Firestore with safe default.
     */
    suspend fun fetchUserRole(username: String): UserRole {
        return when (val res = fetchUserRoleDetailed(username)) {
            is RoleFetchResult.Success -> res.role
            else -> UserRole.USER
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

    /**
     * Real-time sync listener for central App Configuration from system_settings/app_config.
     */
    fun listenToAppConfig() {
        try {
            firestore.collection("system_settings").document("app_config")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(tag, "App config listen error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val config = parseAppConfig(snapshot)
                        _appConfigFlow.value = config
                        GamificationConstants.applyAppConfig(config)
                        Log.d(tag, "Successfully loaded system app config from Firestore: $config")
                    }
                }
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach app config listener: ${e.message}")
        }
    }

    private fun parseAppConfig(doc: com.google.firebase.firestore.DocumentSnapshot): AppConfig {
        val disabledList = try {
            @Suppress("UNCHECKED_CAST")
            (doc.get("disabledModuleIds") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        return AppConfig(
            quizPassingScore = doc.safeInt("quizPassingScore", 70),
            assessmentPassingScore = doc.safeInt("assessmentPassingScore", 75),
            simulationPassingScore = doc.safeInt("simulationPassingScore", 75),
            baseQuizXp = doc.safeInt("baseQuizXp", 100),
            dailyStreakMultiplier = (doc.get("dailyStreakMultiplier") as? Number)?.toFloat() ?: 1.25f,
            quizAttemptLimit = doc.safeInt("quizAttemptLimit", 0),
            quizTimerSeconds = doc.safeInt("quizTimerSeconds", 20),
            randomizeQuestions = doc.safeBoolean("randomizeQuestions", false),
            randomizeChoices = doc.safeBoolean("randomizeChoices", false),
            showCorrectAnswers = doc.safeBoolean("showCorrectAnswers", true),
            allowQuizRetake = doc.safeBoolean("allowQuizRetake", true),
            minScoreForXp = doc.safeInt("minScoreForXp", 50),

            xpPerCompletedModule = doc.safeInt("xpPerCompletedModule", 50),
            xpPerPassedQuiz = doc.safeInt("xpPerPassedQuiz", 100),
            xpPerPassedAssessment = doc.safeInt("xpPerPassedAssessment", 150),
            xpPerCorrectAnswer = doc.safeInt("xpPerCorrectAnswer", 10),
            xpStreakBonusBase = doc.safeInt("xpStreakBonusBase", 20),
            maxXpPerQuiz = doc.safeInt("maxXpPerQuiz", 300),
            enableDailyStreak = doc.safeBoolean("enableDailyStreak", true),
            streakResetHours = doc.safeInt("streakResetHours", 24),
            streakMultiplierMax = (doc.get("streakMultiplierMax") as? Number)?.toFloat() ?: 2.0f,
            enableUserLevels = doc.safeBoolean("enableUserLevels", true),
            xpPerLevel = doc.safeInt("xpPerLevel", 500),
            maxLevel = doc.safeInt("maxLevel", 50),
            autoLevelCalc = doc.safeBoolean("autoLevelCalc", true),
            enableLeaderboard = doc.safeBoolean("enableLeaderboard", true),
            leaderboardUpdateFreq = doc.safeString("leaderboardUpdateFreq", "Real-time"),
            leaderboardRankingType = doc.safeString("leaderboardRankingType", "totalXp"),

            langEnglishEnabled = doc.safeBoolean("langEnglishEnabled", true),
            langFilipinoEnabled = doc.safeBoolean("langFilipinoEnabled", true),
            defaultQuizLanguage = doc.safeString("defaultQuizLanguage", "en"),

            maintenanceMode = doc.safeBoolean("maintenanceMode", false),
            maintenanceMessage = doc.safeString("maintenanceMessage", "RoadSafe AI is undergoing scheduled system maintenance. Please try again shortly."),
            minAppVersion = doc.safeString("minAppVersion", "1.0.0"),
            forceUpdate = doc.safeBoolean("forceUpdate", false),
            announcementEnabled = doc.safeBoolean("announcementEnabled", false),
            announcementTitle = doc.safeString("announcementTitle", "Municipal Road Safety Notice"),
            announcementMessage = doc.safeString("announcementMessage", ""),
            announcementStartDate = doc.safeString("announcementStartDate", ""),
            announcementEndDate = doc.safeString("announcementEndDate", ""),
            enableAnimations = doc.safeBoolean("enableAnimations", true),

            requireModuleBeforeQuiz = doc.safeBoolean("requireModuleBeforeQuiz", false),
            strictLinearProgression = doc.safeBoolean("strictLinearProgression", false),
            contentVersion = doc.safeString("contentVersion", "v1.2.0-300Q"),
            disabledModuleIds = disabledList,

            rememberLoginSession = doc.safeBoolean("rememberLoginSession", true),
            sessionTimeoutDays = doc.safeInt("sessionTimeoutDays", 0),
            allowMultipleDevices = doc.safeBoolean("allowMultipleDevices", true),

            enableNotifications = doc.safeBoolean("enableNotifications", true),
            notifyQuizReminder = doc.safeBoolean("notifyQuizReminder", true),
            notifyStreakReminder = doc.safeBoolean("notifyStreakReminder", true),
            notifyNewModule = doc.safeBoolean("notifyNewModule", true),
            notifyAchievement = doc.safeBoolean("notifyAchievement", true),
            notifyLeaderboard = doc.safeBoolean("notifyLeaderboard", true),

            autoSyncProgress = doc.safeBoolean("autoSyncProgress", true),
            syncFrequency = doc.safeString("syncFrequency", "realtime"),

            adminSessionTimeoutMinutes = doc.safeInt("adminSessionTimeoutMinutes", 60),
            requireReauthSensitive = doc.safeBoolean("requireReauthSensitive", true),
            adminActivityLogging = doc.safeBoolean("adminActivityLogging", true),

            enableSoftDelete = doc.safeBoolean("enableSoftDelete", false),
            dataRetentionDays = doc.safeInt("dataRetentionDays", 0),

            lastUpdatedTimestamp = doc.safeLong("lastUpdatedTimestamp", System.currentTimeMillis()),
            lastUpdatedBy = doc.safeString("lastUpdatedBy", "system")
        )
    }

    companion object {
        const val COLLECTION_USER_PROGRESS = "user_progress"
        const val COLLECTION_QUIZ_ATTEMPTS = "quiz_attempts"
        const val COLLECTION_AUDIT_LOGS = "audit_logs"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_USER_LOGINS = "user_logins"
        const val COLLECTION_AI_INTERACTIONS = "ai_interactions"
        const val COLLECTION_ACTIVITY_LOGS = "activity_logs"
        const val COLLECTION_ACTIVITIES = "activities"

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
