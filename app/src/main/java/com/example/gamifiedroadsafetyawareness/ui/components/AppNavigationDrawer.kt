package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.gamifiedroadsafetyawareness.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.auth.UserRole
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.ui.Screen
import com.example.gamifiedroadsafetyawareness.ui.theme.*

/**
 * Modern, functional, intuitive navigation drawer redesigned specifically for the
 * “AI-Integrated Gamified Road Safety Awareness, Traffic Rule Education, and Driver Decision-Making System.”
 *
 * Implements a professional police/security command aesthetic with:
 * - Security-inspired header with badge emblem, app name, and administrator identity
 * - Strong visual hierarchy with MAIN, MANAGEMENT (expandable), and ACCOUNT sections
 * - Clearly highlighted active page and selective rounded containers
 * - Full accessibility-friendly 48dp+ touch targets, clear contrast, and smooth animations
 */
@Composable
fun AppNavigationDrawer(
    currentScreen: Screen,
    currentUserRole: UserRole,
    displayName: String,
    username: String,
    userProgress: UserProgressEntity?,
    onNavigateTo: (Screen) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignOutConfirm by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    // Auto-expand Management section if current screen is one of the management sub-screens
    val isManagementScreenActive = currentScreen == Screen.AdminManagement ||
        currentScreen == Screen.AdminUserManagement ||
        currentScreen == Screen.AdminModuleManagement ||
        currentScreen == Screen.AdminAnswerReview

    var isManagementExpanded by remember(isManagementScreenActive) {
        mutableStateOf(isManagementScreenActive)
    }

    ModalDrawerSheet(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ────────────────────────────────────────────────────────────────
            // ① HEADER: Police/Security-Inspired App Logo & Admin Identity
            // ────────────────────────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                color = NavyPrimary,
                border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.35f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NavyPrimary, PoliceBlue)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Top row: App Logo, App Name, and Close (X) button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Police/Security Badge Emblem Logo
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "RoadSafe AI Official Logo",
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )

                                Column {
                                    Text(
                                        text = if (currentUserRole == UserRole.ADMIN) "Road Safety Admin" else "Road Safety Hub",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            letterSpacing = 0.3.sp
                                        ),
                                        color = PureWhite
                                    )
                                    Text(
                                        text = if (currentUserRole == UserRole.ADMIN) "System Command Center" else "Traffic Education & Awareness",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PureWhite.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Prominent Close Button
                            IconButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(PureWhite.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close Menu",
                                    tint = PureWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        HorizontalDivider(
                            color = PureWhite.copy(alpha = 0.15f),
                            thickness = 1.dp
                        )

                        // Administrator Identity Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(PureWhite.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (currentUserRole == UserRole.ADMIN) {
                                            Icons.Rounded.AdminPanelSettings
                                        } else {
                                            Icons.Rounded.Person
                                        },
                                        contentDescription = "Profile",
                                        tint = BadgeGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                // Online/Active Status indicator
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen)
                                        .background(PureWhite.copy(alpha = 0.2f))
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = displayName.ifBlank { "Administrator" },
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = PureWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (username.isNotBlank()) "@$username" else "@admin",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PureWhite.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Role Pill
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (currentUserRole == UserRole.ADMIN) BadgeGold.copy(alpha = 0.2f) else PoliceBlue.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, if (currentUserRole == UserRole.ADMIN) BadgeGold.copy(alpha = 0.5f) else PureWhite.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (currentUserRole == UserRole.ADMIN) "ADMIN" else "LEARNER",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.6.sp
                                    ),
                                    color = if (currentUserRole == UserRole.ADMIN) BadgeGold else PureWhite,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ────────────────────────────────────────────────────────────────
            // ADMIN NAVIGATION STRUCTURE
            // ────────────────────────────────────────────────────────────────
            if (currentUserRole == UserRole.ADMIN) {

                // ── SECTION: COMMAND ─────────────────────────────────────────
                SidebarSectionHeader(title = "COMMAND")

                SidebarMenuItem(
                    label = "Dashboard",
                    subtitle = "System overview & telemetry",
                    icon = Icons.Rounded.Dashboard,
                    isActive = currentScreen == Screen.AdminDashboard,
                    onClick = { onNavigateTo(Screen.AdminDashboard) }
                )

                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                // ── SECTION: ADMINISTRATIVE CONTROL CENTER ──────────────────
                SidebarSectionHeader(title = "ADMINISTRATIVE CONTROL CENTER")

                SidebarMenuItem(
                    label = "Manage Gamification",
                    subtitle = "XP, levels, streaks & badges",
                    icon = Icons.Rounded.EmojiEvents,
                    isActive = currentScreen == Screen.AdminXpManagement,
                    onClick = { onNavigateTo(Screen.AdminXpManagement) }
                )

                SidebarMenuItem(
                    label = "Reports & Analytics",
                    subtitle = "Trends, completion & performance",
                    icon = Icons.Rounded.Analytics,
                    isActive = currentScreen == Screen.AdminReports,
                    onClick = { onNavigateTo(Screen.AdminReports) }
                )

                SidebarMenuItem(
                    label = "Audit Trail",
                    subtitle = "Track actions & security events",
                    icon = Icons.Rounded.Security,
                    isActive = currentScreen == Screen.AuditDashboard,
                    onClick = { onNavigateTo(Screen.AuditDashboard) }
                )

                SidebarMenuItem(
                    label = "Cloud Live Monitor",
                    subtitle = "Firebase logins & live presence",
                    icon = Icons.Rounded.CloudSync,
                    isActive = currentScreen == Screen.CloudMonitoring,
                    onClick = { onNavigateTo(Screen.CloudMonitoring) }
                )

                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                // ── SECTION: MANAGEMENT (Expandable) ─────────────────────────
                SidebarSectionHeader(title = "MANAGEMENT")

                // Expandable Parent Menu: Management
                ExpandableParentMenuItem(
                    label = "Management",
                    subtitle = "Content & user administration",
                    icon = Icons.Rounded.ManageAccounts,
                    isExpanded = isManagementExpanded,
                    hasActiveChild = isManagementScreenActive,
                    onToggleExpand = { isManagementExpanded = !isManagementExpanded }
                )

                // Expandable Children: User Management, Module Management, Answer Review
                AnimatedVisibility(
                    visible = isManagementExpanded,
                    enter = expandVertically(animationSpec = tween(220)) + fadeIn(animationSpec = tween(220)),
                    exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(180))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SidebarSubMenuItem(
                            label = "User Management",
                            subtitle = "Accounts, roles & permissions",
                            icon = Icons.Rounded.People,
                            isActive = currentScreen == Screen.AdminUserManagement,
                            onClick = { onNavigateTo(Screen.AdminUserManagement) }
                        )

                        SidebarSubMenuItem(
                            label = "Module Management",
                            subtitle = "Training lessons & visibility",
                            icon = Icons.AutoMirrored.Rounded.MenuBook,
                            isActive = currentScreen == Screen.AdminModuleManagement,
                            onClick = { onNavigateTo(Screen.AdminModuleManagement) }
                        )

                        SidebarSubMenuItem(
                            label = "Answer Review",
                            subtitle = "Submitted quiz attempts",
                            icon = Icons.Rounded.RateReview,
                            isActive = currentScreen == Screen.AdminAnswerReview,
                            onClick = { onNavigateTo(Screen.AdminAnswerReview) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                // ── SECTION: ACCOUNT ────────────────────────────────────────
                SidebarSectionHeader(title = "ACCOUNT & SETTINGS")

                SidebarMenuItem(
                    label = "Account & Settings",
                    subtitle = "Admin preferences & security",
                    icon = Icons.Rounded.Settings,
                    isActive = currentScreen == Screen.Settings || currentScreen == Screen.AccountSecurity,
                    onClick = { onNavigateTo(Screen.Settings) }
                )

                SidebarMenuItem(
                    label = "Help & Support",
                    subtitle = "Documentation & admin guide",
                    icon = Icons.AutoMirrored.Rounded.HelpOutline,
                    isActive = false,
                    onClick = { showHelpDialog = true }
                )

            } else {
                // ────────────────────────────────────────────────────────────
                // LEARNER NAVIGATION STRUCTURE (For regular USER role)
                // ────────────────────────────────────────────────────────────
                SidebarSectionHeader(title = "MAIN MISSIONS")

                SidebarMenuItem(
                    label = "Dashboard",
                    subtitle = "Daily brief & road overview",
                    icon = Icons.Rounded.Home,
                    isActive = currentScreen == Screen.Dashboard,
                    onClick = { onNavigateTo(Screen.Dashboard) }
                )

                SidebarMenuItem(
                    label = "Training Modules",
                    subtitle = "Traffic rules, signs & tests",
                    icon = Icons.AutoMirrored.Rounded.MenuBook,
                    isActive = currentScreen == Screen.Assessment,
                    onClick = { onNavigateTo(Screen.Assessment) }
                )

                SidebarMenuItem(
                    label = "Hazard Simulation",
                    subtitle = "Interactive driving decisions",
                    icon = Icons.Rounded.Psychology,
                    isActive = currentScreen == Screen.Simulation,
                    onClick = { onNavigateTo(Screen.Simulation) }
                )

                SidebarMenuItem(
                    label = "RoadSafe AI Tutor",
                    subtitle = "Personalized safety coach",
                    icon = Icons.Rounded.AutoAwesome,
                    isActive = currentScreen == Screen.AiTutor,
                    onClick = { onNavigateTo(Screen.AiTutor) }
                )

                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                SidebarSectionHeader(title = "PROGRESS & RECORDS")

                SidebarMenuItem(
                    label = "My Progress & Ranks",
                    subtitle = "Badges, level & streaks",
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                    isActive = currentScreen == Screen.Gamification,
                    onClick = { onNavigateTo(Screen.Gamification) }
                )

                SidebarMenuItem(
                    label = "Learning History",
                    subtitle = "Past attempts & answer logs",
                    icon = Icons.Rounded.History,
                    isActive = currentScreen == Screen.LearningHistory,
                    onClick = { onNavigateTo(Screen.LearningHistory) }
                )

                SidebarMenuItem(
                    label = "Performance Analytics",
                    subtitle = "Safety scores & error trends",
                    icon = Icons.Rounded.Analytics,
                    isActive = currentScreen == Screen.Analytics,
                    onClick = { onNavigateTo(Screen.Analytics) }
                )

                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                SidebarSectionHeader(title = "ACCOUNT")

                SidebarMenuItem(
                    label = "Profile & Credentials",
                    subtitle = "Personal stats & badges",
                    icon = Icons.Rounded.Person,
                    isActive = currentScreen == Screen.Profile,
                    onClick = { onNavigateTo(Screen.Profile) }
                )

                SidebarMenuItem(
                    label = "Settings",
                    subtitle = "Language & app preferences",
                    icon = Icons.Rounded.Settings,
                    isActive = currentScreen == Screen.Settings,
                    onClick = { onNavigateTo(Screen.Settings) }
                )

                SidebarMenuItem(
                    label = "Help & Support",
                    subtitle = "System guide & hotlines",
                    icon = Icons.AutoMirrored.Rounded.HelpOutline,
                    isActive = false,
                    onClick = { showHelpDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

            // ────────────────────────────────────────────────────────────────
            // ③ LOGOUT ACTION (Visually Separated)
            // ────────────────────────────────────────────────────────────────
            Surface(
                onClick = { showSignOutConfirm = true },
                shape = RoundedCornerShape(12.dp),
                color = TrafficRed.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, TrafficRed.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TrafficRed.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Logout,
                                contentDescription = "Logout",
                                tint = TrafficRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = TrafficRed
                            )
                            Text(
                                text = "End active session",
                                style = MaterialTheme.typography.labelSmall,
                                color = TrafficRed.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = TrafficRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Version Branding Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Road Safety Awareness System • v1.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Logout Confirmation Dialog
    // ────────────────────────────────────────────────────────────────────────
    if (showSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirm = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = null,
                    tint = TrafficRed,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = "Confirm Logout",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to end your administrative session, $displayName?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutConfirm = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TrafficRed)
                ) {
                    Text("Logout", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSignOutConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ────────────────────────────────────────────────────────────────────────
    // Help & Support Information Modal
    // ────────────────────────────────────────────────────────────────────────
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
                    contentDescription = null,
                    tint = BadgeGold,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = "Admin Help & Support",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "System Navigation Guide:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "• Dashboard: Real-time overview of users, active streaks, and quiz engagement metrics.\n" +
                            "• Management: Expand to access User Accounts, Training Module controls, and Answer Review.\n" +
                            "• Reports & Analytics: View cohort trends, completion rates, and topic weakness breakdowns.\n" +
                            "• Audit Trail: Immutable security logs recording administrative actions, logins, and permission changes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Text(
                        text = "Administrator Support:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "• Technical Support: support@roadsafety.gov.ph\n" +
                            "• Security Dispatch: (02) 8527-3085\n" +
                            "• System Build: v1.0 (Production Release)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showHelpDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Helper UI Components for Drawer Menu
// ────────────────────────────────────────────────────────────────────────────

@Composable
private fun SidebarSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontSize = 11.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun SidebarMenuItem(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isActive) {
        PoliceBlue.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val contentColor = if (isActive) {
        PoliceBlue
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = if (isActive) {
            BorderStroke(1.dp, PoliceBlue.copy(alpha = 0.4f))
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp),
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
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isActive) PoliceBlue.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BadgeGold)
                )
            }
        }
    }
}

@Composable
private fun ExpandableParentMenuItem(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isExpanded: Boolean,
    hasActiveChild: Boolean,
    onToggleExpand: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "chevronRotation"
    )

    val backgroundColor = if (hasActiveChild) {
        PoliceBlue.copy(alpha = 0.08f)
    } else {
        Color.Transparent
    }

    val contentColor = if (hasActiveChild) {
        PoliceBlue
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onToggleExpand,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = if (hasActiveChild) {
            BorderStroke(1.dp, PoliceBlue.copy(alpha = 0.3f))
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp),
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
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (hasActiveChild) PoliceBlue.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (hasActiveChild) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = if (hasActiveChild) PoliceBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
private fun SidebarSubMenuItem(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isActive) {
        PoliceBlue.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val contentColor = if (isActive) {
        PoliceBlue
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        border = if (isActive) {
            BorderStroke(1.dp, PoliceBlue.copy(alpha = 0.4f))
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Sub-item visual connector dot/icon
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isActive) PoliceBlue.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        ),
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(BadgeGold)
                )
            }
        }
    }
}
