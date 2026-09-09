package com.example.gamifiedroadsafetyawareness.auth

/**
 * Defines granular permissions for the RBAC system.
 * Each screen and action in the app maps to one or more permissions.
 */
enum class Permission {
    // User-facing screens
    VIEW_DASHBOARD,
    LAUNCH_SIMULATION,
    VIEW_ASSESSMENT,
    VIEW_GAMIFICATION,
    VIEW_ANALYTICS,
    VIEW_PROFILE,

    // Admin-only screens
    VIEW_ADMIN_PANEL,
    MANAGE_USERS,
    MANAGE_CONTENT,
    MANAGE_SETTINGS,
    VIEW_SYSTEM_OVERVIEW,
    EXPORT_DATA,
    VIEW_AUDIT_LOGS
}

/**
 * Single source of truth for role-to-permission mapping.
 * To add a new role, simply add a new entry to this map.
 */
object RolePermissions {

    private val permissionMap: Map<UserRole, Set<Permission>> = mapOf(
        UserRole.USER to setOf(
            Permission.VIEW_DASHBOARD,
            Permission.LAUNCH_SIMULATION,
            Permission.VIEW_ASSESSMENT,
            Permission.VIEW_GAMIFICATION,
            Permission.VIEW_ANALYTICS,
            Permission.VIEW_PROFILE
        ),
        UserRole.ADMIN to setOf(
            Permission.VIEW_DASHBOARD,
            Permission.VIEW_PROFILE,
            Permission.VIEW_ADMIN_PANEL,
            Permission.MANAGE_USERS,
            Permission.MANAGE_CONTENT,
            Permission.MANAGE_SETTINGS,
            Permission.VIEW_SYSTEM_OVERVIEW,
            Permission.EXPORT_DATA,
            Permission.VIEW_GAMIFICATION,
            Permission.VIEW_AUDIT_LOGS
        ),
        UserRole.SUPER_ADMIN to setOf(
            Permission.VIEW_DASHBOARD,
            Permission.LAUNCH_SIMULATION,
            Permission.VIEW_ASSESSMENT,
            Permission.VIEW_GAMIFICATION,
            Permission.VIEW_ANALYTICS,
            Permission.VIEW_PROFILE,
            Permission.VIEW_ADMIN_PANEL,
            Permission.MANAGE_USERS,
            Permission.MANAGE_CONTENT,
            Permission.MANAGE_SETTINGS,
            Permission.VIEW_SYSTEM_OVERVIEW,
            Permission.EXPORT_DATA,
            Permission.VIEW_AUDIT_LOGS
        )
    )

    /**
     * Check if a given role has the specified permission.
     */
    fun hasPermission(role: UserRole, permission: Permission): Boolean {
        return permissionMap[role]?.contains(permission) == true
    }

    /**
     * Get all permissions for a given role.
     */
    fun getPermissions(role: UserRole): Set<Permission> {
        return permissionMap[role] ?: emptySet()
    }
}
