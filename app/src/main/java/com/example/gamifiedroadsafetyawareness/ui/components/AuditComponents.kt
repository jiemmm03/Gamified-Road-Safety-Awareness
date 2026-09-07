package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.audit.ActionType
import com.example.gamifiedroadsafetyawareness.audit.AuditLog
import com.example.gamifiedroadsafetyawareness.audit.AuditResult
import com.example.gamifiedroadsafetyawareness.audit.RiskLevel
import com.example.gamifiedroadsafetyawareness.ui.theme.AdminPurple
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.InfoBlue
import com.example.gamifiedroadsafetyawareness.ui.theme.NeutralGray
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.*

// ─── Activity Card ──────────────────────────────────────────────────────────

/**
 * A single audit activity card showing WHO did WHAT and WHEN.
 * Human-readable action names, friendly timestamps, and category emoji.
 */
@Composable
fun AuditLogItem(
    log: AuditLog,
    onClick: () -> Unit
) {
    val iconAndColor = getActionIconAndColor(log.actionType)
    val humanAction = getHumanReadableAction(log.actionType)
    val categoryEmoji = getCategoryEmoji(log.actionType)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                onClickLabel = humanAction,
                role = Role.Button,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationLow),
        shape = RoundedCornerShape(Dimens.cardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingMedium),
            verticalAlignment = Alignment.Top
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconAndColor.second.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconAndColor.first,
                    contentDescription = null,
                    tint = iconAndColor.second,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.spacingMedium))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                // Admin name
                Text(
                    text = log.fullName.ifBlank { log.username },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.spacingTiny))

                // Action with emoji
                Text(
                    text = "$categoryEmoji $humanAction",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Description (if meaningful and different from action)
                if (log.description.isNotBlank() && log.description != log.actionType.name) {
                    Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                    Text(
                        text = log.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                // Timestamp + result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatFriendlyTimestamp(log.timestampUtc),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Result chip
                    val resultColor = when (log.result) {
                        AuditResult.SUCCESS -> EmeraldGreen
                        AuditResult.FAILED -> TrafficRed
                    }
                    val resultLabel = when (log.result) {
                        AuditResult.SUCCESS -> getSuccessLabel(log.actionType)
                        AuditResult.FAILED -> "Failed"
                    }
                    Badge(
                        containerColor = resultColor.copy(alpha = 0.12f),
                        contentColor = resultColor
                    ) {
                        Text(
                            text = resultLabel,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = AppTypeScale.badgeLabel
                        )
                    }
                }
            }
        }
    }
}

// ─── Activity Detail Panel ──────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditDetailPanel(
    log: AuditLog,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.dashboardHorizontalPadding, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            val iconAndColor = getActionIconAndColor(log.actionType)
            val humanAction = getHumanReadableAction(log.actionType)

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconAndColor.second.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconAndColor.first,
                        contentDescription = null,
                        tint = iconAndColor.second,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                Column {
                    Text(
                        text = humanAction,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formatFriendlyTimestamp(log.timestampUtc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.spacingMedium))

            // Activity details
            DetailRow("Activity", humanAction)
            DetailRow("Performed By", "${log.fullName} (@${log.username})")
            DetailRow("Role", log.role)
            DetailRow("Date & Time", formatDateFull(log.timestampUtc))

            val resultColor = when (log.result) {
                AuditResult.SUCCESS -> EmeraldGreen
                AuditResult.FAILED -> TrafficRed
            }
            val resultLabel = when (log.result) {
                AuditResult.SUCCESS -> getSuccessLabel(log.actionType)
                AuditResult.FAILED -> "Failed"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Badge(
                    containerColor = resultColor.copy(alpha = 0.12f),
                    contentColor = resultColor
                ) {
                    Text(
                        text = resultLabel,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = AppTypeScale.badgeLabel
                    )
                }
            }

            if (log.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                DetailRow("Description", log.description)
            }

            // Changes (Before/After)
            if (log.previousValue != null || log.newValue != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.spacingMedium))
                Text(
                    text = "Changes",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Before",
                            style = MaterialTheme.typography.labelSmall,
                            color = TrafficRed
                        )
                        Text(
                            log.previousValue ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "After",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldGreen
                        )
                        Text(
                            log.newValue ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // System info (demoted, still accessible)
            HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.spacingMedium))
            Text(
                text = "Technical Details",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            Text(
                text = "Device: ${log.deviceInfo}  •  ID: ${log.auditId.take(8)}…",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Detail Row ─────────────────────────────────────────────────────────────

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(2f)
        )
    }
}

// ─── Human-Readable Action Names ────────────────────────────────────────────

internal fun getHumanReadableAction(action: ActionType): String {
    return when (action) {
        ActionType.LOGIN -> "Logged In"
        ActionType.LOGOUT -> "Logged Out"
        ActionType.FAILED_LOGIN -> "Failed Login Attempt"
        ActionType.PASSWORD_CHANGE -> "Changed Password"
        ActionType.PASSWORD_RESET -> "Reset Password"
        ActionType.TWO_FACTOR_AUTH -> "Updated Two-Factor Auth"
        ActionType.ACCOUNT_LOCK -> "Locked Account"
        ActionType.ACCOUNT_UNLOCK -> "Unlocked Account"
        ActionType.USER_CREATED -> "Created User Account"
        ActionType.USER_UPDATED -> "Updated User Account"
        ActionType.USER_DELETED -> "Deleted User Account"
        ActionType.USER_ACTIVATED -> "Activated User Account"
        ActionType.USER_DEACTIVATED -> "Deactivated User Account"
        ActionType.ROLE_CHANGED -> "Changed User Role"
        ActionType.PERMISSIONS_MODIFIED -> "Modified Permissions"
        ActionType.RECORD_CREATED -> "Created Record"
        ActionType.RECORD_EDITED -> "Updated Record"
        ActionType.RECORD_DELETED -> "Deleted Record"
        ActionType.BULK_OPERATION -> "Performed Bulk Operation"
        ActionType.IMPORT_DATA -> "Imported Data"
        ActionType.EXPORT_DATA -> "Exported Data"
        ActionType.APPROVAL_ACTION -> "Approved Action"
        ActionType.REJECTION_ACTION -> "Rejected Action"
        ActionType.QUIZ_COMPLETED -> "Completed Quiz"
        ActionType.SIMULATION_DECISION -> "Completed Simulation"
        ActionType.SYSTEM_SETTINGS_MODIFIED -> "Updated System Settings"
        ActionType.DATABASE_BACKUP -> "Created Database Backup"
        ActionType.RESTORE_OPERATION -> "Restored Data"
        ActionType.SECURITY_CONFIG_CHANGED -> "Updated Security Config"
        ActionType.ACCESS_CONTROL_UPDATED -> "Updated Access Control"
        ActionType.OTHER -> "Other Activity"
    }
}

// ─── Category Emoji ─────────────────────────────────────────────────────────

private fun getCategoryEmoji(action: ActionType): String {
    return when (action) {
        ActionType.LOGIN, ActionType.LOGOUT, ActionType.FAILED_LOGIN,
        ActionType.PASSWORD_CHANGE, ActionType.PASSWORD_RESET,
        ActionType.TWO_FACTOR_AUTH, ActionType.ACCOUNT_LOCK,
        ActionType.ACCOUNT_UNLOCK -> "🔐"

        ActionType.USER_CREATED, ActionType.USER_UPDATED,
        ActionType.USER_DELETED, ActionType.USER_ACTIVATED,
        ActionType.USER_DEACTIVATED, ActionType.ROLE_CHANGED,
        ActionType.PERMISSIONS_MODIFIED -> "👤"

        ActionType.RECORD_CREATED, ActionType.RECORD_EDITED,
        ActionType.RECORD_DELETED -> "📚"

        ActionType.QUIZ_COMPLETED -> "📝"
        ActionType.SIMULATION_DECISION -> "🚗"

        ActionType.SYSTEM_SETTINGS_MODIFIED, ActionType.DATABASE_BACKUP,
        ActionType.RESTORE_OPERATION, ActionType.SECURITY_CONFIG_CHANGED,
        ActionType.ACCESS_CONTROL_UPDATED -> "⚙️"

        ActionType.EXPORT_DATA, ActionType.IMPORT_DATA -> "📊"
        ActionType.BULK_OPERATION -> "📦"
        ActionType.APPROVAL_ACTION -> "✅"
        ActionType.REJECTION_ACTION -> "❌"

        ActionType.OTHER -> "📋"
    }
}

// ─── Success Label (context-sensitive) ──────────────────────────────────────

private fun getSuccessLabel(action: ActionType): String {
    return when (action) {
        ActionType.USER_CREATED, ActionType.RECORD_CREATED -> "Created"
        ActionType.USER_DELETED, ActionType.RECORD_DELETED -> "Deleted"
        ActionType.USER_ACTIVATED, ActionType.ACCOUNT_UNLOCK -> "Activated"
        ActionType.USER_DEACTIVATED, ActionType.ACCOUNT_LOCK -> "Deactivated"
        ActionType.USER_UPDATED, ActionType.RECORD_EDITED,
        ActionType.ROLE_CHANGED, ActionType.PERMISSIONS_MODIFIED,
        ActionType.SYSTEM_SETTINGS_MODIFIED, ActionType.SECURITY_CONFIG_CHANGED,
        ActionType.ACCESS_CONTROL_UPDATED, ActionType.PASSWORD_CHANGE,
        ActionType.PASSWORD_RESET -> "Updated"
        ActionType.QUIZ_COMPLETED, ActionType.SIMULATION_DECISION -> "Completed"
        ActionType.APPROVAL_ACTION -> "Approved"
        ActionType.REJECTION_ACTION -> "Rejected"
        ActionType.EXPORT_DATA -> "Exported"
        ActionType.IMPORT_DATA -> "Imported"
        else -> "Successful"
    }
}

// ─── Icon & Color Mapping ───────────────────────────────────────────────────

private fun getActionIconAndColor(action: ActionType): Pair<ImageVector, Color> {
    return when (action) {
        ActionType.LOGIN -> Pair(Icons.AutoMirrored.Rounded.Login, InfoBlue)
        ActionType.LOGOUT -> Pair(Icons.AutoMirrored.Rounded.Logout, NeutralGray)
        ActionType.FAILED_LOGIN -> Pair(Icons.Rounded.Warning, TrafficRed)
        ActionType.PASSWORD_CHANGE, ActionType.PASSWORD_RESET -> Pair(Icons.Rounded.Lock, AmberYellow)
        ActionType.TWO_FACTOR_AUTH -> Pair(Icons.Rounded.Security, InfoBlue)
        ActionType.ACCOUNT_LOCK -> Pair(Icons.Rounded.Lock, TrafficRed)
        ActionType.ACCOUNT_UNLOCK -> Pair(Icons.Rounded.LockOpen, EmeraldGreen)
        ActionType.USER_CREATED -> Pair(Icons.Rounded.PersonAdd, EmeraldGreen)
        ActionType.USER_UPDATED -> Pair(Icons.Rounded.Edit, InfoBlue)
        ActionType.USER_DELETED -> Pair(Icons.Rounded.PersonRemove, TrafficRed)
        ActionType.USER_ACTIVATED -> Pair(Icons.Rounded.CheckCircle, EmeraldGreen)
        ActionType.USER_DEACTIVATED -> Pair(Icons.Rounded.Block, TrafficRed)
        ActionType.ROLE_CHANGED, ActionType.PERMISSIONS_MODIFIED -> Pair(Icons.Rounded.AdminPanelSettings, AdminPurple)
        ActionType.RECORD_CREATED -> Pair(Icons.Rounded.AddCircle, EmeraldGreen)
        ActionType.RECORD_EDITED -> Pair(Icons.Rounded.Edit, InfoBlue)
        ActionType.RECORD_DELETED -> Pair(Icons.Rounded.Delete, TrafficRed)
        ActionType.BULK_OPERATION -> Pair(Icons.Rounded.DynamicFeed, AdminPurple)
        ActionType.IMPORT_DATA -> Pair(Icons.Rounded.Upload, InfoBlue)
        ActionType.EXPORT_DATA -> Pair(Icons.Rounded.Download, InfoBlue)
        ActionType.APPROVAL_ACTION -> Pair(Icons.Rounded.ThumbUp, EmeraldGreen)
        ActionType.REJECTION_ACTION -> Pair(Icons.Rounded.ThumbDown, TrafficRed)
        ActionType.QUIZ_COMPLETED -> Pair(Icons.Rounded.Quiz, InfoBlue)
        ActionType.SIMULATION_DECISION -> Pair(Icons.Rounded.Psychology, AdminPurple)
        ActionType.SYSTEM_SETTINGS_MODIFIED -> Pair(Icons.Rounded.Settings, AmberYellow)
        ActionType.DATABASE_BACKUP -> Pair(Icons.Rounded.Backup, InfoBlue)
        ActionType.RESTORE_OPERATION -> Pair(Icons.Rounded.Restore, AmberYellow)
        ActionType.SECURITY_CONFIG_CHANGED -> Pair(Icons.Rounded.Security, TrafficRed)
        ActionType.ACCESS_CONTROL_UPDATED -> Pair(Icons.Rounded.Shield, AdminPurple)
        ActionType.OTHER -> Pair(Icons.Rounded.Info, NeutralGray)
    }
}

// ─── Friendly Timestamp Formatting ──────────────────────────────────────────

/**
 * Formats a UTC timestamp into a human-friendly string:
 * - Same day → "Today • 8:24 PM"
 * - Previous day → "Yesterday • 3:20 PM"
 * - Older → "Aug 27, 2026 • 10:15 AM"
 */
internal fun formatFriendlyTimestamp(timestamp: Long): String {
    val now = Calendar.getInstance()
    val then = Calendar.getInstance().apply { timeInMillis = timestamp }

    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(timestamp))

    val isToday = now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday = yesterday.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)

    return when {
        isToday -> "Today • $timeStr"
        isYesterday -> "Yesterday • $timeStr"
        else -> {
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            "${dateFormat.format(Date(timestamp))} • $timeStr"
        }
    }
}

/**
 * Full date/time for detail panel: "August 27, 2026 • 8:24 PM"
 */
private fun formatDateFull(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM d, yyyy • h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
