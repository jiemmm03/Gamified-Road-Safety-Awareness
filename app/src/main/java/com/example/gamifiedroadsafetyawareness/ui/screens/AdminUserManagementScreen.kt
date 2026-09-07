package com.example.gamifiedroadsafetyawareness.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Woman
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.gamifiedroadsafetyawareness.audit.ActionType
import com.example.gamifiedroadsafetyawareness.audit.AuditLog
import com.example.gamifiedroadsafetyawareness.audit.AuditManager
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.model.AchievementDefinitions
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.UserAccount
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.model.db.XpHistoryEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedProgressRing
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppOutlinedButton
import com.example.gamifiedroadsafetyawareness.ui.components.AuditLogItem
import com.example.gamifiedroadsafetyawareness.ui.components.BadgeTile
import com.example.gamifiedroadsafetyawareness.ui.components.ConfirmActionDialog
import com.example.gamifiedroadsafetyawareness.ui.components.SearchFilterBar
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private enum class AdminUserTab { ACCOUNTS, RECORDS }

@Composable
fun AdminUserManagementScreen(
    authManager: AuthManager,
    xpManager: XpManager? = null,
    currentAdminUsername: String,
    onManageXp: (username: String) -> Unit,
    onViewFullHistory: (UserAccount) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    canExport: Boolean = true
) {
    val context = LocalContext.current
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val allAccounts = remember(refreshTrigger) { authManager.getAllAccounts() }

    var selectedTab by remember { mutableStateOf(AdminUserTab.ACCOUNTS) }

    var searchQuery by remember { mutableStateOf("") }
    var roleFilter by remember { mutableStateOf<String?>(null) }
    var statusFilter by remember { mutableStateOf<Boolean?>(null) }
    var selectedAccount by remember { mutableStateOf<UserAccount?>(null) }
    var accountPendingDeactivate by remember { mutableStateOf<UserAccount?>(null) }

    val filteredAccounts = remember(allAccounts, searchQuery, roleFilter, statusFilter) {
        allAccounts.filter { account ->
            val matchesQuery = searchQuery.isBlank() ||
                account.username.contains(searchQuery, ignoreCase = true) ||
                account.displayName.contains(searchQuery, ignoreCase = true)
            val matchesRole = roleFilter == null || account.role == roleFilter
            val matchesStatus = statusFilter == null || account.isActive == statusFilter
            matchesQuery && matchesRole && matchesStatus
        }
    }

    // Keep the detail view's selected account in sync after activate/deactivate/permission changes.
    LaunchedEffect(allAccounts) {
        selectedAccount?.let { current ->
            selectedAccount = allAccounts.find { it.username == current.username }
        }
    }

    if (selectedAccount != null) {
        AdminUserDetailView(
            account = selectedAccount!!,
            currentAdminUsername = currentAdminUsername,
            authManager = authManager,
            xpManager = xpManager,
            onBack = { selectedAccount = null },
            onRequestDeactivate = { accountPendingDeactivate = selectedAccount },
            onActivate = {
                authManager.setAccountActive(selectedAccount!!.username, true)
                refreshTrigger++
            },
            onManageXp = { onManageXp(selectedAccount!!.username) },
            onViewFullHistory = { onViewFullHistory(selectedAccount!!) },
            onPermissionsSaved = { refreshTrigger++ },
            modifier = modifier
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (selectedTab == AdminUserTab.ACCOUNTS) "User Management" else "Registration Records",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedTab == AdminUserTab.ACCOUNTS) {
                            Text(
                                text = "${filteredAccounts.size} of ${allAccounts.size} accounts",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == AdminUserTab.ACCOUNTS,
                    onClick = { selectedTab = AdminUserTab.ACCOUNTS },
                    label = { Text("Accounts") }
                )
                FilterChip(
                    selected = selectedTab == AdminUserTab.RECORDS,
                    onClick = { selectedTab = AdminUserTab.RECORDS },
                    label = { Text("Registration Records") }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == AdminUserTab.ACCOUNTS) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SearchFilterBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            placeholder = "Search by name or username..."
                        )
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = roleFilter == null,
                                onClick = { roleFilter = null },
                                label = { Text("All Roles") }
                            )
                            FilterChip(
                                selected = roleFilter == "USER",
                                onClick = { roleFilter = "USER" },
                                label = { Text("Users") }
                            )
                            FilterChip(
                                selected = roleFilter == "ADMIN",
                                onClick = { roleFilter = "ADMIN" },
                                label = { Text("Admins") }
                            )
                        }
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = statusFilter == null,
                                onClick = { statusFilter = null },
                                label = { Text("All Status") }
                            )
                            FilterChip(
                                selected = statusFilter == true,
                                onClick = { statusFilter = true },
                                label = { Text("Active") }
                            )
                            FilterChip(
                                selected = statusFilter == false,
                                onClick = { statusFilter = false },
                                label = { Text("Inactive") }
                            )
                        }
                    }

                    items(filteredAccounts) { account ->
                        AdminUserListCard(
                            account = account,
                            onClick = { selectedAccount = account }
                        )
                    }

                    if (filteredAccounts.isEmpty()) {
                        item {
                            Text(
                                text = "No users match your search/filters.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            } else {
                RegistrationRecordsList(
                    allAccounts = allAccounts,
                    canExport = canExport,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (accountPendingDeactivate != null) {
        val target = accountPendingDeactivate!!
        ConfirmActionDialog(
            title = "Deactivate @${target.username}?",
            message = "${target.displayName} will be unable to log in until an admin reactivates this account.",
            confirmLabel = "Deactivate",
            destructive = true,
            onConfirm = {
                val ok = authManager.setAccountActive(target.username, false)
                if (!ok) {
                    Toast.makeText(context, "Unable to deactivate this account.", Toast.LENGTH_SHORT).show()
                }
                refreshTrigger++
                accountPendingDeactivate = null
            },
            onDismiss = { accountPendingDeactivate = null }
        )
    }
}

@Composable
private fun AdminUserListCard(account: UserAccount, onClick: () -> Unit) {
    val isAdmin = account.role == "ADMIN"
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isAdmin) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAdmin) Icons.Rounded.AdminPanelSettings else Icons.Rounded.Person,
                        contentDescription = "User",
                        tint = if (isAdmin) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = account.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        if (!account.isActive) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TrafficRed.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "INACTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TrafficRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = "@${account.username} · ${account.role}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AdminUserDetailView(
    account: UserAccount,
    currentAdminUsername: String,
    authManager: AuthManager,
    xpManager: XpManager?,
    onBack: () -> Unit,
    onRequestDeactivate: () -> Unit,
    onActivate: () -> Unit,
    onManageXp: () -> Unit,
    onViewFullHistory: () -> Unit,
    onPermissionsSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val auditManager = remember { AuditManager(context) }

    var progress by remember { mutableStateOf<UserProgressEntity?>(null) }
    var xpHistory by remember { mutableStateOf<List<XpHistoryEntity>>(emptyList()) }
    var unlockedAchievementIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var recentActivity by remember { mutableStateOf<List<com.example.gamifiedroadsafetyawareness.audit.AuditLog>>(emptyList()) }
    var showPermissionsDialog by remember { mutableStateOf(false) }
    var selectedLog by remember { mutableStateOf<com.example.gamifiedroadsafetyawareness.audit.AuditLog?>(null) }

    LaunchedEffect(account.username, xpManager) {
        val manager = xpManager ?: return@LaunchedEffect
        progress = manager.getProgress(account.username)
        xpHistory = manager.getXpHistory(account.username, 10)
        unlockedAchievementIds = manager.getUnlockedAchievementIds(account.username)
    }

    LaunchedEffect(account.username) {
        recentActivity = auditManager.searchAndFilterLogs(username = account.username).take(10)
    }

    val isSelf = account.username.trim().lowercase() == currentAdminUsername.trim().lowercase()
    val isDefaultAdmin = account.username == AuthManager.DEFAULT_ADMIN_USER

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = account.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${account.username} · ${account.role}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PROGRESS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val p = progress
                    if (p == null) {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AnimatedProgressRing(
                                progress = GamificationConstants.getLevelProgress(p.totalXp, p.currentLevel),
                                size = 64.dp,
                                ringColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "Lv ${p.currentLevel}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "${p.totalXp} total XP",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${p.currentStreak} day streak · ${p.quizzesCompleted} quizzes completed",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AppButton(text = "Adjust XP / View Full History", onClick = onManageXp)
                    }
                }
            }
        }

        item {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PROFILE DETAILS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (account.gender) {
                                "MALE" -> Icons.Rounded.Man
                                "FEMALE" -> Icons.Rounded.Woman
                                else -> Icons.Rounded.Person
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Gender",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when (account.gender) {
                                    "MALE" -> "Male"
                                    "FEMALE" -> "Female"
                                    else -> "Not specified"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Contact Number",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = account.contactNumber.ifBlank { "Not provided" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Achievements",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            val badges = AchievementDefinitions.toBadgeItems(unlockedAchievementIds)
            androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(badges) { badge -> BadgeTile(badge = badge) }
            }
        }

        if (xpHistory.isNotEmpty()) {
            item {
                Text(
                    text = "Recent XP Activity",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(xpHistory) { entry -> XpHistoryRow(entry = entry) }
        }

        if (recentActivity.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Account Activity",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(recentActivity) { log ->
                AuditLogItem(log = log, onClick = { selectedLog = log })
            }
        }

        item {
            Text(
                text = "Actions",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            AppOutlinedButton(
                text = "View Full Activity History",
                onClick = onViewFullHistory
            )
        }
        item {
            AppButton(
                text = "Manage Permissions",
                onClick = { showPermissionsDialog = true }
            )
        }
        item {
            if (account.isActive) {
                AppButton(
                    text = if (isSelf) "Deactivate (Unavailable — this is your account)" else if (isDefaultAdmin) "Deactivate (Unavailable — default admin)" else "Deactivate Account",
                    onClick = onRequestDeactivate,
                    enabled = !isSelf && !isDefaultAdmin,
                    icon = Icons.Rounded.Block,
                    containerColor = TrafficRed
                )
            } else {
                AppButton(
                    text = "Activate Account",
                    onClick = onActivate,
                    icon = Icons.Rounded.CheckCircle,
                    containerColor = EmeraldGreen
                )
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    if (showPermissionsDialog) {
        ManagePermissionsDialog(
            user = account,
            onDismiss = { showPermissionsDialog = false },
            onSave = { updatedPermissions ->
                authManager.updateUserPermissions(account.username, updatedPermissions)
                showPermissionsDialog = false
                onPermissionsSaved()
            }
        )
    }

    selectedLog?.let { log ->
        com.example.gamifiedroadsafetyawareness.ui.components.AuditDetailPanel(
            log = log,
            onDismiss = { selectedLog = null }
        )
    }
}

private val registrationDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

private fun formatRegistrationDate(epochMillis: Long): String =
    if (epochMillis <= 0L) "Unknown"
    else Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate().format(registrationDateFormatter)

@Composable
private fun RegistrationRecordsList(
    allAccounts: List<UserAccount>,
    canExport: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auditManager = remember { AuditManager(context) }

    var registrationSources by remember { mutableStateOf<Map<String, AuditLog>>(emptyMap()) }
    LaunchedEffect(Unit) {
        val creationLogs = withContext(Dispatchers.IO) {
            auditManager.searchAndFilterLogs(actionType = ActionType.USER_CREATED)
        }
        registrationSources = creationLogs.associateBy { it.username }
    }

    var searchQuery by remember { mutableStateOf("") }
    var roleFilter by remember { mutableStateOf<String?>(null) }
    var genderFilter by remember { mutableStateOf<String?>(null) }
    var sortAscending by remember { mutableStateOf(false) }
    var selectedRecord by remember { mutableStateOf<UserAccount?>(null) }

    val today = remember { LocalDate.now() }
    val thisWeekCount = remember(allAccounts, today) {
        allAccounts.count {
            it.createdAt > 0L &&
                ChronoUnit.DAYS.between(Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate(), today) <= 7
        }
    }
    val thisMonthCount = remember(allAccounts, today) {
        allAccounts.count {
            it.createdAt > 0L &&
                ChronoUnit.DAYS.between(Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate(), today) <= 30
        }
    }

    val filteredRecords = remember(allAccounts, searchQuery, roleFilter, genderFilter, sortAscending) {
        val filtered = allAccounts.filter { account ->
            val matchesQuery = searchQuery.isBlank() ||
                account.username.contains(searchQuery, ignoreCase = true) ||
                account.displayName.contains(searchQuery, ignoreCase = true)
            val matchesRole = roleFilter == null || account.role == roleFilter
            val matchesGender = genderFilter == null || account.gender == genderFilter
            matchesQuery && matchesRole && matchesGender
        }
        val sorted = filtered.sortedBy { it.createdAt }
        if (sortAscending) sorted else sorted.reversed()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecordStatChip("Total", allAccounts.size.toString(), Modifier.weight(1f))
                RecordStatChip("This Week", thisWeekCount.toString(), Modifier.weight(1f))
                RecordStatChip("This Month", thisMonthCount.toString(), Modifier.weight(1f))
            }
        }

        item {
            SearchFilterBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by name or username..."
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = roleFilter == null, onClick = { roleFilter = null }, label = { Text("All Roles") })
                FilterChip(selected = roleFilter == "USER", onClick = { roleFilter = "USER" }, label = { Text("Users") })
                FilterChip(selected = roleFilter == "ADMIN", onClick = { roleFilter = "ADMIN" }, label = { Text("Admins") })
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = genderFilter == null, onClick = { genderFilter = null }, label = { Text("All Genders") })
                FilterChip(selected = genderFilter == "MALE", onClick = { genderFilter = "MALE" }, label = { Text("Male") })
                FilterChip(selected = genderFilter == "FEMALE", onClick = { genderFilter = "FEMALE" }, label = { Text("Female") })
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredRecords.size} records",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { sortAscending = !sortAscending }) {
                        Icon(
                            imageVector = if (sortAscending) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (sortAscending) "Oldest First" else "Newest First", style = MaterialTheme.typography.labelSmall)
                    }
                    if (canExport) {
                        IconButton(onClick = {
                            scope.launch {
                                val csvFile = withContext(Dispatchers.IO) {
                                    exportRegistrationRecordsToCsv(context, filteredRecords, registrationSources)
                                }
                                if (csvFile != null) {
                                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", csvFile)
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/csv"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Export Registration Records"))
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.Download, contentDescription = "Export CSV")
                        }
                    }
                }
            }
        }

        items(filteredRecords) { account ->
            RegistrationRecordCard(
                account = account,
                registrationSource = registrationSources[account.username]?.remarks,
                onClick = { selectedRecord = account }
            )
        }

        if (filteredRecords.isEmpty()) {
            item {
                Text(
                    text = "No registration records match your search/filters.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    selectedRecord?.let { account ->
        RegistrationRecordDetailDialog(
            account = account,
            registrationSource = registrationSources[account.username]?.remarks,
            onDismiss = { selectedRecord = null }
        )
    }
}

@Composable
private fun RecordStatChip(label: String, value: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier, elevation = 1) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun RegistrationRecordCard(account: UserAccount, registrationSource: String?, onClick: () -> Unit) {
    val sourceLabel = when {
        registrationSource == null -> "Unknown"
        registrationSource.startsWith("Self-registered") -> "Self-Registered"
        registrationSource.startsWith("Account created by admin") -> "Admin-Created"
        else -> registrationSource
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${account.username} · ${account.role}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (account.gender.isNotBlank()) {
                    Icon(
                        imageVector = if (account.gender == "MALE") Icons.Rounded.Man else Icons.Rounded.Woman,
                        contentDescription = account.gender,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registered ${formatRegistrationDate(account.createdAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = sourceLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RegistrationRecordDetailDialog(account: UserAccount, registrationSource: String?, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Registration Record",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                RecordDetailRow("Full Name", account.displayName)
                RecordDetailRow("Username", "@${account.username}")
                RecordDetailRow("Role", account.role)
                RecordDetailRow(
                    "Gender",
                    when (account.gender) {
                        "MALE" -> "Male"
                        "FEMALE" -> "Female"
                        else -> "Not specified"
                    }
                )
                RecordDetailRow("Contact Number", account.contactNumber.ifBlank { "Not provided" })
                RecordDetailRow("Registered On", formatRegistrationDate(account.createdAt))
                RecordDetailRow("Registration Source", registrationSource ?: "Unknown")
                RecordDetailRow("Status", if (account.isActive) "Active" else "Inactive")
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Close") }
                }
            }
        }
    }
}

@Composable
private fun RecordDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
    }
}

private fun exportRegistrationRecordsToCsv(
    context: Context,
    accounts: List<UserAccount>,
    registrationSources: Map<String, AuditLog>
): File? {
    if (accounts.isEmpty()) return null
    val csvFile = File(context.cacheDir, "registration_records_export.csv")
    return try {
        FileOutputStream(csvFile).use { fos ->
            OutputStreamWriter(fos).use { writer ->
                writer.append("Username,Full Name,Role,Gender,Contact Number,Registered On,Status,Registration Source\n")
                accounts.forEach { account ->
                    val source = registrationSources[account.username]?.remarks ?: "Unknown"
                    writer.append(
                        "${account.username},\"${account.displayName.replace("\"", "\"\"")}\"," +
                            "${account.role},${account.gender.ifBlank { "N/A" }},${account.contactNumber.ifBlank { "N/A" }}," +
                            "${formatRegistrationDate(account.createdAt)},${if (account.isActive) "Active" else "Inactive"}," +
                            "\"${source.replace("\"", "\"\"")}\"\n"
                    )
                }
            }
        }
        csvFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
