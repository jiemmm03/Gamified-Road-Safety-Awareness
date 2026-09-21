package com.example.gamifiedroadsafetyawareness.auth

enum class UserRole(val backendValue: String, val displayLabel: String) {
    USER("user", "Driver / Learner (USER)"),
    ADMIN("admin", "Traffic Officer (ADMIN)"),
    SUPER_ADMIN("admin", "Traffic Officer (ADMIN)");

    companion object {
        fun fromRoleString(raw: String?): UserRole {
            if (raw.isNullOrBlank()) return USER
            val normalized = raw.trim().lowercase()
            return when {
                normalized == "admin" || normalized == "traffic_officer" || normalized == "officer" || normalized == "super_admin" -> ADMIN
                normalized == "user" || normalized == "driver" || normalized == "learner" -> USER
                raw.equals("ADMIN", ignoreCase = true) -> ADMIN
                raw.equals("USER", ignoreCase = true) -> USER
                else -> USER
            }
        }

        fun getDisplayRoleName(role: UserRole): String = when (role) {
            ADMIN, SUPER_ADMIN -> "Traffic Officer (ADMIN)"
            USER -> "Driver / Learner (USER)"
        }

        fun getFriendlyTitle(role: UserRole): String = when (role) {
            ADMIN, SUPER_ADMIN -> "Traffic Officer"
            USER -> "Driver / Learner"
        }
    }
}

