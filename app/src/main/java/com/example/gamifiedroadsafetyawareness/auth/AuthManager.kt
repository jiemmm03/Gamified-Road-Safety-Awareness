package com.example.gamifiedroadsafetyawareness.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.gamifiedroadsafetyawareness.model.UserAccount
import com.example.gamifiedroadsafetyawareness.audit.AuditManager
import com.example.gamifiedroadsafetyawareness.audit.ActionType
import com.example.gamifiedroadsafetyawareness.audit.Module
import com.example.gamifiedroadsafetyawareness.audit.AuditResult
import com.example.gamifiedroadsafetyawareness.audit.RiskLevel
import com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Local authentication manager using SharedPreferences.
 * Pre-seeds default admin and user accounts on first launch.
 */
class AuthManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val userPrefs: SharedPreferences =
        context.getSharedPreferences("user_accounts", Context.MODE_PRIVATE)

    private val auditManager = AuditManager(context)
    private val syncManager = FirebaseSyncManager.getInstance()

    companion object {
        private const val KEY_SEEDED = "accounts_seeded"
        private const val KEY_LOGGED_IN_USER = "logged_in_user"
        private const val KEY_LOGGED_IN_ROLE = "logged_in_role"
        private const val KEY_REMEMBER_ME = "remember_me"

        // Default credentials
        const val DEFAULT_ADMIN_USER = "admin"
        const val DEFAULT_ADMIN_PASS = "admin123"
        const val DEFAULT_USER_USER = "user"
        const val DEFAULT_USER_PASS = "user123"

        const val MIN_PASSWORD_LENGTH = 8

        // ── S-06: Rate limiting constants ──────────────────────────────────
        private const val MAX_FAILED_ATTEMPTS = 5
        private const val BASE_LOCKOUT_DURATION_MS = 30_000L // 30 seconds
        private const val MAX_LOCKOUT_DURATION_MS = 300_000L // 5 minutes

        // ── S-04: PBKDF2 constants ─────────────────────────────────────────
        private const val PBKDF2_ITERATIONS = 120_000
        private const val PBKDF2_KEY_LENGTH = 256
        private const val SALT_LENGTH_BYTES = 16
    }

    init {
        seedDefaultAccounts()
    }

    private fun seedDefaultAccounts() {
        if (!prefs.getBoolean(KEY_SEEDED, false)) {
            registerAccount(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_PASS, UserRole.ADMIN, "Administrator")
            registerAccount(DEFAULT_USER_USER, DEFAULT_USER_PASS, UserRole.USER, "Juan D.")
            // S-03: Flag seeded accounts for forced password change on first login
            userPrefs.edit()
                .putBoolean("${DEFAULT_ADMIN_USER}_must_change_password", true)
                .putBoolean("${DEFAULT_USER_USER}_must_change_password", true)
                .apply()
            prefs.edit().putBoolean(KEY_SEEDED, true).apply()
        }
    }

    // ── S-03: Check if user must change their default password ─────────────
    fun mustChangePassword(username: String): Boolean {
        val trimmedUser = username.trim().lowercase()
        return userPrefs.getBoolean("${trimmedUser}_must_change_password", false)
    }

    fun clearMustChangePassword(username: String) {
        val trimmedUser = username.trim().lowercase()
        userPrefs.edit().remove("${trimmedUser}_must_change_password").apply()
    }

    // ── S-04: PBKDF2 + per-user salt password hashing ──────────────────────
    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH_BYTES)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun hashWithPbkdf2(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        val saltHex = salt.joinToString("") { "%02x".format(it) }
        val hashHex = hash.joinToString("") { "%02x".format(it) }
        return "pbkdf2:$saltHex:$hashHex"
    }

    /**
     * Hash a password. Uses PBKDF2 with a random salt for new passwords.
     * Retained for backward compatibility — call [hashAndStorePassword] for new registrations
     * and [verifyPassword] for login checks.
     */
    fun hashPassword(password: String): String {
        // Legacy SHA-256 for backward compat (only used during migration check)
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashAndStorePassword(username: String, password: String) {
        val salt = generateSalt()
        val hash = hashWithPbkdf2(password, salt)
        userPrefs.edit().putString("${username}_hash", hash).apply()
    }

    /**
     * Verify a password against the stored hash. Supports both legacy SHA-256
     * (auto-upgraded to PBKDF2 on successful match) and PBKDF2 hashes.
     */
    private fun verifyPassword(username: String, password: String): Boolean {
        val storedHash = userPrefs.getString("${username}_hash", null) ?: return false

        return if (storedHash.startsWith("pbkdf2:")) {
            // PBKDF2 hash format: "pbkdf2:<saltHex>:<hashHex>"
            val parts = storedHash.split(":")
            if (parts.size != 3) return false
            val salt = parts[1].chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            val expectedHash = hashWithPbkdf2(password, salt)
            storedHash == expectedHash
        } else {
            // Legacy SHA-256 — verify and auto-upgrade to PBKDF2
            val legacyHash = hashPassword(password)
            if (storedHash == legacyHash) {
                // Auto-upgrade: re-hash with PBKDF2 + salt
                hashAndStorePassword(username, password)
                true
            } else {
                false
            }
        }
    }

    // ── S-06: Login rate limiting ──────────────────────────────────────────
    private fun getFailedAttemptCount(username: String): Int =
        prefs.getInt("failed_attempts_$username", 0)

    private fun getLastFailedTime(username: String): Long =
        prefs.getLong("failed_time_$username", 0L)

    private fun recordFailedAttempt(username: String) {
        val count = getFailedAttemptCount(username) + 1
        prefs.edit()
            .putInt("failed_attempts_$username", count)
            .putLong("failed_time_$username", System.currentTimeMillis())
            .apply()
    }

    private fun clearFailedAttempts(username: String) {
        prefs.edit()
            .remove("failed_attempts_$username")
            .remove("failed_time_$username")
            .apply()
    }

    private fun getLockoutRemainingMs(username: String): Long {
        val failCount = getFailedAttemptCount(username)
        if (failCount < MAX_FAILED_ATTEMPTS) return 0L
        val lockoutDuration = (BASE_LOCKOUT_DURATION_MS * (1 shl (failCount - MAX_FAILED_ATTEMPTS).coerceAtMost(3)))
            .coerceAtMost(MAX_LOCKOUT_DURATION_MS)
        val elapsed = System.currentTimeMillis() - getLastFailedTime(username)
        return (lockoutDuration - elapsed).coerceAtLeast(0L)
    }

    fun getLockoutRemainingSeconds(username: String): Int =
        (getLockoutRemainingMs(username.trim().lowercase()) / 1000).toInt()

    fun login(username: String, password: String): LoginResult {
        val trimmedUser = username.trim().lowercase()
        val storedHash = userPrefs.getString("${trimmedUser}_hash", null)
            ?: return LoginResult.InvalidCredentials

        // S-06: Check rate limiting before attempting password verification
        val lockoutRemaining = getLockoutRemainingMs(trimmedUser)
        if (lockoutRemaining > 0) {
            val seconds = (lockoutRemaining / 1000).toInt()
            return LoginResult.AccountLocked(seconds)
        }

        if (!verifyPassword(trimmedUser, password)) {
            recordFailedAttempt(trimmedUser)
            val attemptsLeft = MAX_FAILED_ATTEMPTS - getFailedAttemptCount(trimmedUser)
            auditManager.logAction(
                userId = "UNKNOWN",
                fullName = "Unknown",
                username = trimmedUser,
                role = "UNKNOWN",
                actionType = ActionType.FAILED_LOGIN,
                module = Module.AUTHENTICATION,
                description = "Failed login attempt for user: $trimmedUser" +
                    if (attemptsLeft <= 0) " (ACCOUNT LOCKED)" else " ($attemptsLeft attempts remaining)",
                result = AuditResult.FAILED,
                riskLevel = if (attemptsLeft <= 0) RiskLevel.HIGH else RiskLevel.MEDIUM
            )
            syncManager.recordUserLogin(
                username = trimmedUser,
                displayName = "Unknown",
                role = "UNKNOWN",
                isSuccess = false,
                failureReason = "Invalid password credentials"
            )
            return LoginResult.InvalidCredentials
        }

        // Successful password match — clear failed attempts
        clearFailedAttempts(trimmedUser)

        val role = userPrefs.getString("${trimmedUser}_role", "USER") ?: "USER"
        val displayName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser
        val userRole = if (role == "ADMIN") UserRole.ADMIN else UserRole.USER
        val permissions = getUserPermissions(trimmedUser, userRole)

        if (!userPrefs.getBoolean("${trimmedUser}_active", true)) {
            auditManager.logAction(
                userId = trimmedUser,
                fullName = displayName,
                username = trimmedUser,
                role = role,
                actionType = ActionType.FAILED_LOGIN,
                module = Module.AUTHENTICATION,
                description = "Login blocked for deactivated account: $trimmedUser",
                result = AuditResult.FAILED,
                riskLevel = RiskLevel.MEDIUM
            )
            syncManager.recordUserLogin(
                username = trimmedUser,
                displayName = displayName,
                role = role,
                isSuccess = false,
                failureReason = "Account deactivated"
            )
            return LoginResult.AccountDeactivated
        }

        // Save session
        prefs.edit()
            .putString(KEY_LOGGED_IN_USER, trimmedUser)
            .putString(KEY_LOGGED_IN_ROLE, role)
            .apply()

        auditManager.logAction(
            userId = trimmedUser,
            fullName = displayName,
            username = trimmedUser,
            role = role,
            actionType = ActionType.LOGIN,
            module = Module.AUTHENTICATION,
            description = "User $trimmedUser logged in successfully.",
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.LOW
        )

        // Save and monitor user login on Cloud Database (Firebase Firestore)
        syncManager.recordUserLogin(
            username = trimmedUser,
            displayName = displayName,
            role = role,
            isSuccess = true
        )

        return LoginResult.Success(userRole, displayName, permissions)
    }

    fun logout() {
        val loggedInUser = prefs.getString(KEY_LOGGED_IN_USER, null)
        val role = prefs.getString(KEY_LOGGED_IN_ROLE, "UNKNOWN")
        val displayName = loggedInUser?.let { userPrefs.getString("${it}_display", it) } ?: "Unknown"

        if (loggedInUser != null) {
            auditManager.logAction(
                userId = loggedInUser,
                fullName = displayName,
                username = loggedInUser,
                role = role ?: "UNKNOWN",
                actionType = ActionType.LOGOUT,
                module = Module.AUTHENTICATION,
                description = "User $loggedInUser logged out.",
                result = AuditResult.SUCCESS,
                riskLevel = RiskLevel.LOW
            )
            // Update cloud database user status to offline
            syncManager.recordUserLogout(
                username = loggedInUser,
                displayName = displayName,
                role = role ?: "UNKNOWN"
            )
        }

        prefs.edit()
            .remove(KEY_LOGGED_IN_USER)
            .remove(KEY_LOGGED_IN_ROLE)
            .putBoolean(KEY_REMEMBER_ME, false)
            .apply()
    }

    fun isRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, true)

    fun setRememberMe(value: Boolean) {
        prefs.edit().putBoolean(KEY_REMEMBER_ME, value).apply()
    }

    fun hasActiveSession(): Boolean {
        val user = prefs.getString(KEY_LOGGED_IN_USER, null) ?: return false
        val hash = userPrefs.getString("${user}_hash", null) ?: return false
        return userPrefs.getBoolean("${user}_active", true)
    }

    fun getSavedSession(): LoginResult? {
        val user = prefs.getString(KEY_LOGGED_IN_USER, null) ?: return null
        val role = prefs.getString(KEY_LOGGED_IN_ROLE, null) ?: return null
        val hash = userPrefs.getString("${user}_hash", null)
        if (hash == null) {
            // Account was deleted externally or credentials purged
            logout()
            return null
        }
        if (!userPrefs.getBoolean("${user}_active", true)) {
            // Account deactivated
            logout()
            return LoginResult.AccountDeactivated
        }
        val displayName = userPrefs.getString("${user}_display", user) ?: user
        val userRole = if (role == "ADMIN") UserRole.ADMIN else UserRole.USER
        val permissions = getUserPermissions(user, userRole)
        return LoginResult.Success(userRole, displayName, permissions)
    }

    fun getUserPermissions(username: String, fallbackRole: UserRole): Set<Permission> {
        val permStrings = userPrefs.getStringSet("${username}_permissions", null)
        return if (permStrings != null) {
            permStrings.mapNotNull { 
                try { Permission.valueOf(it) } catch (e: Exception) { null } 
            }.toSet()
        } else {
            // Fallback for existing users who haven't been seeded yet
            val defaultPerms = RolePermissions.getPermissions(fallbackRole)
            updateUserPermissions(username, defaultPerms)
            defaultPerms
        }
    }

    fun updateUserPermissions(username: String, permissions: Set<Permission>) {
        val trimmedUser = username.trim().lowercase()
        
        // Log action (assume current logged in user is admin, but since we don't have it easily here, we mock the actor or pass it)
        val actor = getLoggedInUsername() ?: "SYSTEM"
        val actorRole = prefs.getString(KEY_LOGGED_IN_ROLE, "ADMIN") ?: "ADMIN"
        val targetDisplayName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser
        val oldPermStrings = userPrefs.getStringSet("${trimmedUser}_permissions", null)
        val oldPerms = oldPermStrings?.joinToString(", ") ?: "NONE"
        val newPerms = permissions.map { it.name }.joinToString(", ")

        userPrefs.edit()
            .putStringSet("${trimmedUser}_permissions", permissions.map { it.name }.toSet())
            .apply()

        auditManager.logAction(
            userId = actor,
            fullName = actor,
            username = actor,
            role = actorRole,
            actionType = ActionType.PERMISSIONS_MODIFIED,
            module = Module.USER_MANAGEMENT,
            description = "Modified permissions for $targetDisplayName (@$trimmedUser)",
            previousValue = oldPerms,
            newValue = newPerms,
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.MEDIUM
        )
    }

    /**
     * Activates or deactivates a user account. Blocked for the default admin and for the
     * currently logged-in admin's own account (self-lockout guard).
     */
    fun setAccountActive(username: String, active: Boolean): Boolean {
        val trimmedUser = username.trim().lowercase()
        if (trimmedUser == DEFAULT_ADMIN_USER) return false
        if (trimmedUser == getLoggedInUsername()) return false

        val targetDisplayName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser
        userPrefs.edit().putBoolean("${trimmedUser}_active", active).apply()

        val actor = getLoggedInUsername() ?: "SYSTEM"
        val actorRole = prefs.getString(KEY_LOGGED_IN_ROLE, "ADMIN") ?: "ADMIN"

        auditManager.logAction(
            userId = actor,
            fullName = actor,
            username = actor,
            role = actorRole,
            actionType = if (active) ActionType.USER_ACTIVATED else ActionType.USER_DEACTIVATED,
            module = Module.USER_MANAGEMENT,
            description = "${if (active) "Activated" else "Deactivated"} account: $targetDisplayName (@$trimmedUser)",
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.MEDIUM
        )

        return true
    }

    /** Changes a user's password after verifying their current one and validating the new one. */
    fun changePassword(username: String, currentPassword: String, newPassword: String): PasswordChangeResult {
        val trimmedUser = username.trim().lowercase()
        if (userPrefs.getString("${trimmedUser}_hash", null) == null) {
            return PasswordChangeResult.WrongCurrentPassword
        }

        if (!verifyPassword(trimmedUser, currentPassword)) {
            return PasswordChangeResult.WrongCurrentPassword
        }
        if (newPassword.length < MIN_PASSWORD_LENGTH) {
            return PasswordChangeResult.PasswordTooShort
        }
        if (!newPassword.any { it.isLetter() } || !newPassword.any { it.isDigit() }) {
            return PasswordChangeResult.PasswordTooWeak
        }
        if (verifyPassword(trimmedUser, newPassword)) {
            return PasswordChangeResult.SamePassword
        }

        // Use PBKDF2 for the new password
        hashAndStorePassword(trimmedUser, newPassword)
        // S-03: Clear forced password change flag after successful change
        clearMustChangePassword(trimmedUser)

        val role = userPrefs.getString("${trimmedUser}_role", "USER") ?: "USER"
        val displayName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser
        auditManager.logAction(
            userId = trimmedUser,
            fullName = displayName,
            username = trimmedUser,
            role = role,
            actionType = ActionType.PASSWORD_CHANGE,
            module = Module.AUTHENTICATION,
            description = "Password changed for $displayName (@$trimmedUser)",
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.MEDIUM
        )

        return PasswordChangeResult.Success
    }

    /** Updates a user's display name. */
    fun updateDisplayName(username: String, newDisplayName: String): Boolean {
        val trimmedUser = username.trim().lowercase()
        val trimmedName = newDisplayName.trim()
        if (trimmedName.isEmpty()) return false
        if (userPrefs.getString("${trimmedUser}_hash", null) == null) return false

        val oldName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser
        userPrefs.edit().putString("${trimmedUser}_display", trimmedName).apply()

        val role = userPrefs.getString("${trimmedUser}_role", "USER") ?: "USER"
        auditManager.logAction(
            userId = trimmedUser,
            fullName = trimmedName,
            username = trimmedUser,
            role = role,
            actionType = ActionType.USER_UPDATED,
            module = Module.USER_MANAGEMENT,
            description = "Display name changed for @$trimmedUser",
            previousValue = oldName,
            newValue = trimmedName,
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.LOW
        )

        return true
    }

    fun registerAccount(
        username: String,
        password: String,
        role: UserRole,
        displayName: String,
        gender: String = "",
        age: Int? = null,
        contactNumber: String = ""
    ): Boolean {
        val trimmedUser = username.trim().lowercase()
        if (trimmedUser.isEmpty() || password.isEmpty()) return false

        // Check if already exists
        val existingUsers = getAllAccounts().map { it.username }
        if (existingUsers.contains(trimmedUser) && userPrefs.getString("${trimmedUser}_hash", null) != null) {
            // Update existing
        }

        // Use PBKDF2 with random salt for new registrations
        val salt = generateSalt()
        val hash = hashWithPbkdf2(password, salt)
        val defaultPerms = RolePermissions.getPermissions(role)

        val editor = userPrefs.edit()
            .putString("${trimmedUser}_hash", hash)
            .putString("${trimmedUser}_role", role.name)
            .putString("${trimmedUser}_display", displayName)
            .putLong("${trimmedUser}_created", System.currentTimeMillis())
            .putStringSet("${trimmedUser}_permissions", defaultPerms.map { it.name }.toSet())
            .putString("${trimmedUser}_gender", gender)
            .putString("${trimmedUser}_contact", contactNumber.trim())
        if (age != null) editor.putInt("${trimmedUser}_age", age) else editor.remove("${trimmedUser}_age")
        editor.apply()

        // Add to user list
        val userList = userPrefs.getStringSet("all_users", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
        userList.add(trimmedUser)
        userPrefs.edit().putStringSet("all_users", userList).apply()

        // Record the sign-up under the NEW user's own identity (full name), not the actor's,
        // so self-registrations show up correctly in the audit trail instead of as "SYSTEM".
        val actor = getLoggedInUsername()
        val registrationSource = if (actor != null && actor != trimmedUser) {
            "Account created by admin: $actor"
        } else {
            "Self-registered via sign-up"
        }

        auditManager.logAction(
            userId = trimmedUser,
            fullName = displayName,
            username = trimmedUser,
            role = role.name,
            actionType = ActionType.USER_CREATED,
            module = Module.USER_MANAGEMENT,
            description = "New account registered: $displayName (@$trimmedUser, ${role.name})",
            newValue = "Role: ${role.name}",
            remarks = registrationSource,
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.MEDIUM
        )

        // Save newly registered user to Cloud Database (Firebase Firestore)
        syncManager.syncRegisteredUser(
            username = trimmedUser,
            displayName = displayName,
            role = role.name,
            gender = gender,
            age = age,
            contactNumber = contactNumber.trim()
        )

        return true
    }

    fun registerAndLogin(
        username: String,
        password: String,
        role: UserRole,
        displayName: String,
        gender: String = "",
        age: Int? = null,
        contactNumber: String = ""
    ): LoginResult {
        val created = registerAccount(username, password, role, displayName, gender, age, contactNumber)
        if (!created) return LoginResult.InvalidCredentials
        return login(username, password)
    }

    fun deleteAccount(username: String): Boolean {
        val trimmedUser = username.trim().lowercase()
        if (trimmedUser == DEFAULT_ADMIN_USER) return false // Can't delete default admin

        val targetDisplayName = userPrefs.getString("${trimmedUser}_display", trimmedUser) ?: trimmedUser

        // S-09: Remove ALL user fields, including personal data
        userPrefs.edit()
            .remove("${trimmedUser}_hash")
            .remove("${trimmedUser}_role")
            .remove("${trimmedUser}_display")
            .remove("${trimmedUser}_created")
            .remove("${trimmedUser}_permissions")
            .remove("${trimmedUser}_active")          // was missing
            .remove("${trimmedUser}_gender")          // was missing — personal data
            .remove("${trimmedUser}_contact")         // was missing — personal data
            .remove("${trimmedUser}_age")             // was missing — personal data
            .remove("${trimmedUser}_must_change_password") // S-03 flag cleanup
            .apply()

        // Clear rate-limiting state for the deleted account
        prefs.edit()
            .remove("failed_attempts_$trimmedUser")
            .remove("failed_time_$trimmedUser")
            .apply()

        val userList = userPrefs.getStringSet("all_users", mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
        userList.remove(trimmedUser)
        userPrefs.edit().putStringSet("all_users", userList).apply()

        val actor = getLoggedInUsername() ?: "SYSTEM"
        val actorRole = prefs.getString(KEY_LOGGED_IN_ROLE, "ADMIN") ?: "ADMIN"

        auditManager.logAction(
            userId = actor,
            fullName = actor,
            username = actor,
            role = actorRole,
            actionType = ActionType.USER_DELETED,
            module = Module.USER_MANAGEMENT,
            description = "Deleted account: $targetDisplayName (@$trimmedUser)",
            result = AuditResult.SUCCESS,
            riskLevel = RiskLevel.HIGH
        )

        return true
    }

    fun getAllAccounts(): List<UserAccount> {
        val userList = userPrefs.getStringSet("all_users", emptySet()) ?: emptySet()
        return userList.map { username ->
            val roleStr = userPrefs.getString("${username}_role", "USER") ?: "USER"
            val userRole = if (roleStr == "ADMIN") UserRole.ADMIN else UserRole.USER
            UserAccount(
                username = username,
                passwordHash = userPrefs.getString("${username}_hash", "") ?: "",
                role = roleStr,
                displayName = userPrefs.getString("${username}_display", username) ?: username,
                permissions = getUserPermissions(username, userRole),
                createdAt = userPrefs.getLong("${username}_created", 0L),
                isActive = userPrefs.getBoolean("${username}_active", true),
                gender = userPrefs.getString("${username}_gender", "") ?: "",
                age = if (userPrefs.contains("${username}_age")) userPrefs.getInt("${username}_age", 0) else null,
                contactNumber = userPrefs.getString("${username}_contact", "") ?: ""
            )
        }.sortedBy { it.username }
    }

    fun getLoggedInUsername(): String? = prefs.getString(KEY_LOGGED_IN_USER, null)
}

sealed class LoginResult {
    data class Success(val role: UserRole, val displayName: String, val permissions: Set<Permission>) : LoginResult()
    object InvalidCredentials : LoginResult()
    object AccountDeactivated : LoginResult()
    /** S-06: Account is temporarily locked due to too many failed attempts. */
    data class AccountLocked(val remainingSeconds: Int) : LoginResult()
}

sealed class PasswordChangeResult {
    object Success : PasswordChangeResult()
    object WrongCurrentPassword : PasswordChangeResult()
    object PasswordTooShort : PasswordChangeResult()
    object PasswordTooWeak : PasswordChangeResult()
    object SamePassword : PasswordChangeResult()
}
