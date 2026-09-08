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
    }

    init {
        seedDefaultAccounts()
    }

    private fun seedDefaultAccounts() {
        if (!prefs.getBoolean(KEY_SEEDED, false)) {
            registerAccount(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_PASS, UserRole.ADMIN, "Administrator")
            registerAccount(DEFAULT_USER_USER, DEFAULT_USER_PASS, UserRole.USER, "Juan D.")
            prefs.edit().putBoolean(KEY_SEEDED, true).apply()
        }
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun login(username: String, password: String): LoginResult {
        val trimmedUser = username.trim().lowercase()
        val storedHash = userPrefs.getString("${trimmedUser}_hash", null)
            ?: return LoginResult.InvalidCredentials

        val inputHash = hashPassword(password)
        if (storedHash != inputHash) {
            auditManager.logAction(
                userId = "UNKNOWN",
                fullName = "Unknown",
                username = trimmedUser,
                role = "UNKNOWN",
                actionType = ActionType.FAILED_LOGIN,
                module = Module.AUTHENTICATION,
                description = "Failed login attempt for user: $trimmedUser",
                result = AuditResult.FAILED,
                riskLevel = RiskLevel.MEDIUM
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
        val storedHash = userPrefs.getString("${trimmedUser}_hash", null)
            ?: return PasswordChangeResult.WrongCurrentPassword

        if (hashPassword(currentPassword) != storedHash) {
            return PasswordChangeResult.WrongCurrentPassword
        }
        if (newPassword.length < MIN_PASSWORD_LENGTH) {
            return PasswordChangeResult.PasswordTooShort
        }
        if (!newPassword.any { it.isLetter() } || !newPassword.any { it.isDigit() }) {
            return PasswordChangeResult.PasswordTooWeak
        }
        if (hashPassword(newPassword) == storedHash) {
            return PasswordChangeResult.SamePassword
        }

        userPrefs.edit().putString("${trimmedUser}_hash", hashPassword(newPassword)).apply()

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

        val hash = hashPassword(password)
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

        userPrefs.edit()
            .remove("${trimmedUser}_hash")
            .remove("${trimmedUser}_role")
            .remove("${trimmedUser}_display")
            .remove("${trimmedUser}_created")
            .remove("${trimmedUser}_permissions")
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
}

sealed class PasswordChangeResult {
    object Success : PasswordChangeResult()
    object WrongCurrentPassword : PasswordChangeResult()
    object PasswordTooShort : PasswordChangeResult()
    object PasswordTooWeak : PasswordChangeResult()
    object SamePassword : PasswordChangeResult()
}
