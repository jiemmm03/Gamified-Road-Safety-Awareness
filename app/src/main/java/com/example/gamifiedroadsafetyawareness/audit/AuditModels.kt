package com.example.gamifiedroadsafetyawareness.audit

import org.json.JSONObject
import java.util.UUID

enum class ActionType {
    LOGIN,
    LOGOUT,
    FAILED_LOGIN,
    PASSWORD_CHANGE,
    PASSWORD_RESET,
    TWO_FACTOR_AUTH,
    ACCOUNT_LOCK,
    ACCOUNT_UNLOCK,
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    USER_ACTIVATED,
    USER_DEACTIVATED,
    ROLE_CHANGED,
    PERMISSIONS_MODIFIED,
    RECORD_CREATED,
    RECORD_EDITED,
    RECORD_DELETED,
    BULK_OPERATION,
    IMPORT_DATA,
    EXPORT_DATA,
    APPROVAL_ACTION,
    REJECTION_ACTION,
    QUIZ_COMPLETED,
    SIMULATION_DECISION,
    SYSTEM_SETTINGS_MODIFIED,
    DATABASE_BACKUP,
    RESTORE_OPERATION,
    SECURITY_CONFIG_CHANGED,
    ACCESS_CONTROL_UPDATED,
    OTHER
}

enum class Module {
    AUTHENTICATION,
    USER_MANAGEMENT,
    CONTENT_DATA,
    ADMINISTRATIVE,
    SYSTEM
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}

enum class AuditResult {
    SUCCESS,
    FAILED
}

data class AuditLog(
    val auditId: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "",
    val actionType: ActionType,
    val module: Module,
    val description: String = "",
    val previousValue: String? = null,
    val newValue: String? = null,
    val timestampUtc: Long = System.currentTimeMillis(),
    val deviceInfo: String = "",
    val ipAddress: String = "",
    val sessionId: String = "",
    val loginStatus: String = "",
    val result: AuditResult = AuditResult.SUCCESS,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val remarks: String? = null,
    val linkedRecordId: String? = null
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("auditId", auditId)
        json.put("userId", userId)
        json.put("fullName", fullName)
        json.put("username", username)
        json.put("email", email)
        json.put("role", role)
        json.put("actionType", actionType.name)
        json.put("module", module.name)
        json.put("description", description)
        json.put("previousValue", previousValue ?: JSONObject.NULL)
        json.put("newValue", newValue ?: JSONObject.NULL)
        json.put("timestampUtc", timestampUtc)
        json.put("deviceInfo", deviceInfo)
        json.put("ipAddress", ipAddress)
        json.put("sessionId", sessionId)
        json.put("loginStatus", loginStatus)
        json.put("result", result.name)
        json.put("riskLevel", riskLevel.name)
        json.put("remarks", remarks ?: JSONObject.NULL)
        json.put("linkedRecordId", linkedRecordId ?: JSONObject.NULL)
        return json
    }

    companion object {
        fun fromJson(json: JSONObject): AuditLog {
            return AuditLog(
                auditId = json.getString("auditId"),
                userId = json.getString("userId"),
                fullName = json.getString("fullName"),
                username = json.getString("username"),
                email = json.optString("email", ""),
                role = json.getString("role"),
                actionType = try { ActionType.valueOf(json.getString("actionType")) } catch (e: Exception) { ActionType.OTHER },
                module = try { Module.valueOf(json.getString("module")) } catch (e: Exception) { Module.SYSTEM },
                description = json.getString("description"),
                previousValue = if (json.isNull("previousValue")) null else json.getString("previousValue"),
                newValue = if (json.isNull("newValue")) null else json.getString("newValue"),
                timestampUtc = json.getLong("timestampUtc"),
                deviceInfo = json.getString("deviceInfo"),
                ipAddress = json.getString("ipAddress"),
                sessionId = json.getString("sessionId"),
                loginStatus = json.getString("loginStatus"),
                result = try { AuditResult.valueOf(json.getString("result")) } catch (e: Exception) { AuditResult.FAILED },
                riskLevel = try { RiskLevel.valueOf(json.getString("riskLevel")) } catch (e: Exception) { RiskLevel.LOW },
                remarks = if (json.isNull("remarks")) null else json.getString("remarks"),
                linkedRecordId = if (json.isNull("linkedRecordId")) null else json.getString("linkedRecordId")
            )
        }
    }
}
