package com.example.gamifiedroadsafetyawareness.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.gamifiedroadsafetyawareness.audit.*
import com.example.gamifiedroadsafetyawareness.ui.components.AuditDetailPanel
import com.example.gamifiedroadsafetyawareness.ui.components.AuditLogItem
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.InfoBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

private const val PAGE_SIZE = 25

/**
 * Simplified category filter that maps to the underlying [Module] enum.
 * "Quizzes" maps to CONTENT_DATA + quiz action types; "System" combines SYSTEM + ADMINISTRATIVE.
 */
private enum class CategoryFilter(val label: String) {
    ALL("All"),
    USERS("Users"),
    MODULES("Modules"),
    QUIZZES("Quizzes"),
    ACCOUNT("Account"),
    SYSTEM("System")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditDashboardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    filterUsername: String? = null,
    filterDisplayName: String? = null,
    canExport: Boolean = true
) {
    val context = LocalContext.current
    val auditManager = remember { AuditManager(context) }
    val scope = rememberCoroutineScope()

    var allLogs by remember { mutableStateOf<List<AuditLog>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedLog by remember { mutableStateOf<AuditLog?>(null) }

    // Filters
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(CategoryFilter.ALL) }
    var sortAscending by remember { mutableStateOf(false) }
    var visibleCount by remember { mutableIntStateOf(PAGE_SIZE) }

    fun loadLogs() {
        scope.launch {
            isLoading = true
            val moduleFilter = when (selectedCategory) {
                CategoryFilter.ALL -> null
                CategoryFilter.USERS -> Module.USER_MANAGEMENT
                CategoryFilter.MODULES -> Module.CONTENT_DATA
                CategoryFilter.QUIZZES -> Module.CONTENT_DATA
                CategoryFilter.ACCOUNT -> Module.AUTHENTICATION
                CategoryFilter.SYSTEM -> Module.SYSTEM
            }
            val filtered = withContext(Dispatchers.IO) {
                auditManager.searchAndFilterLogs(
                    query = searchQuery,
                    module = moduleFilter,
                    username = filterUsername
                )
            }
            // Additional client-side filter for Quizzes (quiz-specific action types within CONTENT_DATA)
            allLogs = if (selectedCategory == CategoryFilter.QUIZZES) {
                filtered.filter { it.actionType == ActionType.QUIZ_COMPLETED }
            } else if (selectedCategory == CategoryFilter.SYSTEM) {
                // Also include ADMINISTRATIVE module
                val adminLogs = withContext(Dispatchers.IO) {
                    auditManager.searchAndFilterLogs(
                        query = searchQuery,
                        module = Module.ADMINISTRATIVE,
                        username = filterUsername
                    )
                }
                (filtered + adminLogs).sortedByDescending { it.timestampUtc }
            } else {
                filtered
            }
            visibleCount = PAGE_SIZE
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadLogs()
    }

    val displayedLogs = remember(allLogs, sortAscending) {
        if (sortAscending) allLogs.reversed() else allLogs
    }
    val pagedLogs = remember(displayedLogs, visibleCount) {
        displayedLogs.take(visibleCount)
    }

    // Summary stats
    val todayStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val todaysLogs = allLogs.filter { it.timestampUtc >= todayStart }
    val userActions = allLogs.count { it.module == Module.USER_MANAGEMENT }
    val moduleActions = allLogs.count { it.module == Module.CONTENT_DATA }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Top App Bar ─────────────────────────────────────────────────────
        TopAppBar(
            title = {
                Text(
                    text = if (filterDisplayName != null) "History: $filterDisplayName" else "Audit Trail",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                if (canExport) {
                    IconButton(onClick = {
                        scope.launch {
                            val csvFile = withContext(Dispatchers.IO) { auditManager.exportToCsv() }
                            if (csvFile != null) {
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", csvFile)
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/csv"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Export Audit Logs"))
                            }
                        }
                    }) {
                        Icon(Icons.Rounded.Download, contentDescription = "Export CSV")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.dashboardHorizontalPadding),
            contentPadding = PaddingValues(vertical = Dimens.spacingMedium)
        ) {
            // ── Subtitle ────────────────────────────────────────────────────
            if (filterDisplayName == null) {
                item {
                    Text(
                        text = "Monitor important administrative activities and system changes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                }
            }

            // ── Summary Stats Card ──────────────────────────────────────────
            item {
                DashboardCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.spacingMedium),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AuditStatItem(
                            label = "Today",
                            value = todaysLogs.size.toString(),
                            icon = Icons.Rounded.Today,
                            tint = InfoBlue
                        )
                        AuditStatDivider()
                        AuditStatItem(
                            label = "User",
                            value = userActions.toString(),
                            icon = Icons.Rounded.Person,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        AuditStatDivider()
                        AuditStatItem(
                            label = "Module",
                            value = moduleActions.toString(),
                            icon = Icons.AutoMirrored.Rounded.MenuBook,
                            tint = EmeraldGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            }

            // ── Search Bar ──────────────────────────────────────────────────
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        loadLogs()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by name or activity...") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                loadLogs()
                            }) {
                                Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(Dimens.cardCornerRadius),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            }

            // ── Category Filter Chips ───────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
                ) {
                    CategoryFilter.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                loadLogs()
                            },
                            label = {
                                Text(
                                    text = category.label,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            }

            // ── Sort Toggle ─────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activity Timeline",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { sortAscending = !sortAscending }) {
                        Icon(
                            imageVector = if (sortAscending) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sortAscending) "Oldest" else "Newest",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            }

            // ── Activity List ───────────────────────────────────────────────
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (allLogs.isEmpty()) {
                // Empty state
                item {
                    DashboardCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.cardPadding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                            Text(
                                text = "No Activity Yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                            Text(
                                text = "There are no recorded activities to display.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(pagedLogs) { log ->
                    AuditLogItem(
                        log = log,
                        onClick = { selectedLog = log }
                    )
                }
                if (visibleCount < displayedLogs.size) {
                    item {
                        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                        TextButton(
                            onClick = { visibleCount += PAGE_SIZE },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Load More (${displayedLogs.size - visibleCount} remaining)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            // ── Bottom spacer ───────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            }
        }
    }

    // ── Detail Bottom Sheet ─────────────────────────────────────────────────
    selectedLog?.let { log ->
        AuditDetailPanel(
            log = log,
            onDismiss = { selectedLog = null }
        )
    }
}

// ─── Summary Stat Item ──────────────────────────────────────────────────────

@Composable
private fun AuditStatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AuditStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(48.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}
