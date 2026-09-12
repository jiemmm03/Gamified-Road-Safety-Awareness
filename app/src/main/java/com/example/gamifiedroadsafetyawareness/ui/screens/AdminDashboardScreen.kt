package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.CloudSync
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import androidx.compose.ui.window.Dialog
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.auth.Permission
import com.example.gamifiedroadsafetyawareness.model.UserAccount
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

@Composable
fun AdminDashboardScreen(
    adminName: String = "Admin",
    authManager: AuthManager,
    xpManager: XpManager? = null,
    auditManager: com.example.gamifiedroadsafetyawareness.audit.AuditManager? = null,
    onLogout: () -> Unit,
    onNavigateToAudit: () -> Unit = {},
    onManageXp: () -> Unit = {},
    onViewReports: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCloudMonitor: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allAccounts = remember { authManager.getAllAccounts() }
    val totalUsers = allAccounts.count { it.role == "USER" }
    val totalAdmins = allAccounts.count { it.role == "ADMIN" }
    val learnerUsernames = remember(allAccounts) {
        allAccounts.filter { it.role == "USER" }.map { it.username }.toSet()
    }

    // getAllUsersProgress() returns every account's progress row regardless of role — filter to
    // learner accounts so this doesn't double-count an admin who has also accrued XP.
    var activeStreaks by remember { mutableIntStateOf(0) }
    LaunchedEffect(xpManager, learnerUsernames) {
        activeStreaks = xpManager?.getAllUsersProgress()
            ?.count { it.userId in learnerUsernames && it.currentStreak > 0 } ?: 0
    }

    val recentLogs = remember(auditManager) {
        auditManager?.getAllLogs()?.take(4) ?: emptyList()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimens.dashboardHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
    ) {
        // ── Header: Official Police Command Operations Center ───────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.4f)),
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    NavyPrimary.copy(alpha = 0.05f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            com.example.gamifiedroadsafetyawareness.ui.components.AppEyebrowLabel(
                                text = "OPERATIONS COMMAND • ROAD SAFETY REGISTRY",
                                color = BadgeGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = adminName,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NavyPrimary.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, NavyPrimary.copy(alpha = 0.25f))
                            ) {
                                Text(
                                    text = "SYSTEM ADMINISTRATOR • DISPATCH CLEARANCE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        fontSize = 10.sp
                                    ),
                                    color = NavyPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        com.example.gamifiedroadsafetyawareness.ui.components.PoliceBadgeInsignia(
                            size = 52.dp,
                            badgeColor = BadgeGold,
                            baseColor = NavyPrimary
                        )
                    }
                }
            }
        }

        // ── Network Status Metrics Card ─────────────────────────────────────
        item {
            DashboardCard {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Text(
                        text = "NETWORK STATUS",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MetricItem(
                            label = "Learners",
                            value = totalUsers.toString(),
                            valueColor = MaterialTheme.colorScheme.onSurface
                        )
                        MetricDivider()
                        MetricItem(
                            label = "Admins",
                            value = totalAdmins.toString(),
                            valueColor = MaterialTheme.colorScheme.secondary
                        )
                        MetricDivider()
                        MetricItem(
                            label = "Active Streaks",
                            value = activeStreaks.toString(),
                            valueColor = BadgeGold
                        )
                    }
                }
            }
        }

        // ── System Status & Security Telemetry ──────────────────────────────
        item {
            DashboardCard {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SYSTEM STATUS & HEALTH",
                            style = AppTypeScale.eyebrowLabel,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreen.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen)
                                )
                                Text(
                                    text = "OPERATIONAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = EmeraldGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SystemStatusPill(
                            label = "Database",
                            status = "Room v6 Active",
                            icon = Icons.Rounded.Storage,
                            modifier = Modifier.weight(1f)
                        )
                        SystemStatusPill(
                            label = "AI Engine",
                            status = "Simulation Ready",
                            icon = Icons.Rounded.SmartToy,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SystemStatusPill(
                            label = "Access Control",
                            status = "RBAC Enforced",
                            icon = Icons.Rounded.Lock,
                            modifier = Modifier.weight(1f)
                        )
                        SystemStatusPill(
                            label = "Security Stream",
                            status = "Audit Synced",
                            icon = Icons.Rounded.Shield,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                        onClick = onNavigateToCloudMonitor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CloudSync,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Firebase Cloud Monitor",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Live logins, active presence & cloud telemetry",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── Recent Activity & Audit Feed ────────────────────────────────────
        item {
            DashboardCard {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Text(
                        text = "RECENT ADMINISTRATIVE ACTIVITY",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Real-time security and administrative event stream",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    if (recentLogs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No recent administrative events recorded.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            recentLogs.forEach { log ->
                                RecentActivityItem(log = log)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                    Text(
                        text = "⚡ Full administrative records available in the Audit Trail sidebar",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // ── Bottom safe-area spacer ─────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        }
    }
}

// ─── Data Model ─────────────────────────────────────────────────────────────

data class AdminHubCardSpec(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val onClick: () -> Unit
)

// ─── Responsive Grid ────────────────────────────────────────────────────────


/**
 * Responsive grid of Admin Management cards: 2-per-row on width >= 480dp (tablets/foldables),
 * a single column on phones. Generalizes the ad-hoc BoxWithConstraints breakpoint this section
 * started with so it doesn't need re-deriving as more hub cards are added.
 */
@Composable
fun AdminHubCardGrid(cards: List<AdminHubCardSpec>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth >= 480.dp) {
            val rows = cards.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)) {
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
                    ) {
                        row.forEach { spec ->
                            AdminManagementCard(
                                title = spec.title,
                                description = spec.description,
                                icon = spec.icon,
                                iconTint = spec.iconTint,
                                onClick = spec.onClick,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)) {
                cards.forEach { spec ->
                    AdminManagementCard(
                        title = spec.title,
                        description = spec.description,
                        icon = spec.icon,
                        iconTint = spec.iconTint,
                        onClick = spec.onClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ─── Hub Card ───────────────────────────────────────────────────────────────

/**
 * Equal-weight navigation tile for the Admin Management section. The icon is marked
 * decorative (contentDescription = null) since the title/description text already conveys
 * the action to screen readers; the card itself carries the accessible button semantics.
 */
@Composable
internal fun AdminManagementCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier.clickable(
            onClickLabel = title,
            role = Role.Button,
            onClick = onClick
        )
    ) {
        Column(modifier = Modifier.padding(Dimens.cardPadding)) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(Dimens.iconContainerSize)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.iconToTitleSpacing))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.titleToDescriptionSpacing))

            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Metrics ────────────────────────────────────────────────────────────────

@Composable
private fun MetricItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = AppTypeScale.statValue,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(Dimens.spacingTiny))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MetricDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}

@Composable
private fun SystemStatusPill(
    label: String,
    status: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RecentActivityItem(log: com.example.gamifiedroadsafetyawareness.audit.AuditLog) {
    val riskColor = when (log.riskLevel) {
        com.example.gamifiedroadsafetyawareness.audit.RiskLevel.HIGH -> TrafficRed
        com.example.gamifiedroadsafetyawareness.audit.RiskLevel.MEDIUM -> AmberYellow
        else -> EmeraldGreen
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(riskColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Shield,
                        contentDescription = null,
                        tint = riskColor,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Column {
                    Text(
                        text = log.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "@${log.username} • ${log.module.name}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = riskColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, riskColor.copy(alpha = 0.3f))
            ) {
                Text(
                    text = log.riskLevel.name,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    color = riskColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// ─── Permissions Dialog (unchanged) ─────────────────────────────────────────

@Composable
fun ManagePermissionsDialog(
    user: UserAccount,
    onDismiss: () -> Unit,
    onSave: (Set<Permission>) -> Unit
) {
    var editedPermissions by remember { mutableStateOf(user.permissions) }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingMedium),
            elevation = 8
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.cardPadding)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Manage Permissions",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "For @${user.username}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                
                Column(
                    modifier = Modifier.heightIn(max = 240.dp)
                ) {
                    Permission.values().forEach { permission ->
                        val hasPerm = editedPermissions.contains(permission)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimens.spacingSmall)
                                .clickable {
                                    editedPermissions = if (hasPerm) {
                                        editedPermissions - permission
                                    } else {
                                        editedPermissions + permission
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = permission.name.replace("_", " "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = hasPerm,
                                onCheckedChange = { isChecked ->
                                    editedPermissions = if (isChecked) {
                                        editedPermissions + permission
                                    } else {
                                        editedPermissions - permission
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = EmeraldGreen,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = TrafficRed.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.spacingLarge))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                    Button(
                        onClick = { onSave(editedPermissions) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Save Changes",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
