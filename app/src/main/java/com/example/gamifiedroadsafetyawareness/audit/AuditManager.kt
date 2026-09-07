package com.example.gamifiedroadsafetyawareness.audit

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Scanner

/**
 * Manages appending to and reading from a local JSON lines file for audit logs.
 * Ensure it acts as append-only for immutable logs.
 */
class AuditManager(private val context: Context) {
    private val logFileName = "audit_logs.jsonl"
    private val logFile: File = File(context.filesDir, logFileName)

    fun logAction(
        userId: String,
        fullName: String,
        username: String,
        role: String,
        actionType: ActionType,
        module: Module,
        description: String,
        previousValue: String? = null,
        newValue: String? = null,
        deviceInfo: String = android.os.Build.MODEL,
        ipAddress: String = "127.0.0.1", // Mocked for local app
        sessionId: String = "",
        loginStatus: String = "ACTIVE",
        result: AuditResult = AuditResult.SUCCESS,
        riskLevel: RiskLevel = RiskLevel.LOW,
        remarks: String? = null,
        linkedRecordId: String? = null
    ) {
        val logEntry = AuditLog(
            userId = userId,
            fullName = fullName,
            username = username,
            role = role,
            actionType = actionType,
            module = module,
            description = description,
            previousValue = previousValue,
            newValue = newValue,
            deviceInfo = deviceInfo,
            ipAddress = ipAddress,
            sessionId = sessionId,
            loginStatus = loginStatus,
            result = result,
            riskLevel = riskLevel,
            remarks = remarks,
            linkedRecordId = linkedRecordId
        )

        appendLog(logEntry)
    }

    @Synchronized
    private fun appendLog(log: AuditLog) {
        try {
            val jsonString = log.toJson().toString()
            FileOutputStream(logFile, true).use { fos ->
                OutputStreamWriter(fos).use { writer ->
                    writer.append(jsonString)
                    writer.append("\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Push to Firebase Cloud Firestore for real-time security & login monitoring
        try {
            com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager.getInstance().syncAuditLog(log)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getAllLogs(): List<AuditLog> {
        val logs = mutableListOf<AuditLog>()
        if (!logFile.exists()) return logs

        try {
            Scanner(logFile).use { scanner ->
                while (scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    if (line.isNotBlank()) {
                        try {
                            val json = JSONObject(line)
                            logs.add(AuditLog.fromJson(json))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Return sorted by newest first
        return logs.sortedByDescending { it.timestampUtc }
    }

    fun searchAndFilterLogs(
        query: String = "",
        actionType: ActionType? = null,
        riskLevel: RiskLevel? = null,
        module: Module? = null,
        result: AuditResult? = null,
        username: String? = null
    ): List<AuditLog> {
        val allLogs = getAllLogs()
        return allLogs.filter { log ->
            val matchesQuery = query.isBlank() ||
                log.username.contains(query, ignoreCase = true) ||
                log.fullName.contains(query, ignoreCase = true) ||
                log.description.contains(query, ignoreCase = true)

            val matchesAction = actionType == null || log.actionType == actionType
            val matchesRisk = riskLevel == null || log.riskLevel == riskLevel
            val matchesModule = module == null || log.module == module
            val matchesResult = result == null || log.result == result
            val matchesUsername = username == null || log.username.equals(username, ignoreCase = true)

            matchesQuery && matchesAction && matchesRisk && matchesModule && matchesResult && matchesUsername
        }
    }

    fun exportToCsv(): File? {
        val allLogs = getAllLogs()
        if (allLogs.isEmpty()) return null

        val csvFile = File(context.cacheDir, "audit_logs_export.csv")
        try {
            FileOutputStream(csvFile).use { fos ->
                OutputStreamWriter(fos).use { writer ->
                    // Header
                    writer.append("Audit ID,Timestamp,User ID,Username,Full Name,Role,Action,Module,Description,Risk,Result,IP Address,Device\n")
                    // Rows
                    allLogs.forEach { log ->
                        writer.append("${log.auditId},${log.timestampUtc},${log.userId},${log.username},${log.fullName},${log.role},${log.actionType},${log.module},\"${log.description.replace("\"", "\"\"")}\",${log.riskLevel},${log.result},${log.ipAddress},${log.deviceInfo}\n")
                    }
                }
            }
            return csvFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
