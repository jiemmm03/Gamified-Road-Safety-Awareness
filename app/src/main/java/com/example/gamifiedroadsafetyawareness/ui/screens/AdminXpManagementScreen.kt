package com.example.gamifiedroadsafetyawareness.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.model.AchievementDefinitions
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.model.db.XpHistoryEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedProgressRing
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminXpManagementScreen(
    adminUsername: String,
    xpManager: XpManager? = null,
    authManager: AuthManager? = null,
    preselectedUsername: String? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // getAllUsersProgress() returns every account's progress row regardless of role. This screen
    // presents "Total Users"/"Avg Level" and the leaderboard as learner-only metrics, so filter
    // out any admin account that has also accrued XP (e.g. via a granted quiz/simulation permission).
    val learnerUsernames = remember(authManager) {
        authManager?.getAllAccounts()?.filter { it.role == "USER" }?.map { it.username }?.toSet()
    }
    var allUsers by remember { mutableStateOf<List<UserProgressEntity>>(emptyList()) }
    var selectedUser by remember { mutableStateOf<UserProgressEntity?>(null) }
    var selectedUserHistory by remember { mutableStateOf<List<XpHistoryEntity>>(emptyList()) }
    var selectedUserAttempts by remember { mutableStateOf<List<QuizAttemptEntity>>(emptyList()) }
    var showAdjustDialog by remember { mutableStateOf(false) }
    var showModuleConfig by remember { mutableStateOf(false) }
    var showLevelConfig by remember { mutableStateOf(false) }
    var showQuizXpConfig by remember { mutableStateOf(false) }
    var showAchievementXpConfig by remember { mutableStateOf(false) }
    var showStreakConfig by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(xpManager, refreshTrigger, learnerUsernames) {
        val manager = xpManager ?: return@LaunchedEffect
        val progress = manager.getAllUsersProgress()
        allUsers = learnerUsernames?.let { names -> progress.filter { it.userId in names } } ?: progress
    }

    LaunchedEffect(allUsers, preselectedUsername) {
        if (selectedUser == null && preselectedUsername != null) {
            selectedUser = allUsers.find { it.userId == preselectedUsername }
        }
    }

    LaunchedEffect(selectedUser, xpManager) {
        val manager = xpManager ?: return@LaunchedEffect
        val user = selectedUser ?: return@LaunchedEffect
        selectedUserHistory = manager.getXpHistory(user.userId, 50)
        selectedUserAttempts = manager.getAttemptsForUserAdmin(user.userId)
    }

    if (selectedUser != null) {
        // Detail view for selected user
        UserXpDetailView(
            user = selectedUser!!,
            history = selectedUserHistory,
            attempts = selectedUserAttempts,
            onBack = { selectedUser = null },
            onAdjustXp = { showAdjustDialog = true },
            modifier = modifier
        )
    } else {
        // Main admin list
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                            text = "Training XP & Level Management",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage user progress and system configuration",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        icon = Icons.Rounded.Settings,
                        title = "Module XP",
                        tint = MaterialTheme.colorScheme.primary,
                        onClick = { showModuleConfig = true },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        icon = Icons.Rounded.TrendingUp,
                        title = "Level Thresholds",
                        tint = MaterialTheme.colorScheme.secondary,
                        onClick = { showLevelConfig = true },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        icon = Icons.Rounded.Quiz,
                        title = "Quiz XP",
                        tint = MaterialTheme.colorScheme.tertiary,
                        onClick = { showQuizXpConfig = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        icon = Icons.Rounded.EmojiEvents,
                        title = "Achievement XP",
                        tint = AmberYellow,
                        onClick = { showAchievementXpConfig = true },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        icon = Icons.Rounded.LocalFireDepartment,
                        title = "Streak Rewards",
                        tint = MaterialTheme.colorScheme.error,
                        onClick = { showStreakConfig = true },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Summary stats
            item {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SYSTEM OVERVIEW",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${allUsers.size}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Total Users",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val totalXp = allUsers.sumOf { it.totalXp }
                                Text(
                                    text = "$totalXp",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Total Training XP Awarded",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val avgLevel = if (allUsers.isNotEmpty()) allUsers.map { it.currentLevel }.average().toInt() else 0
                                Text(
                                    text = "$avgLevel",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Avg Level",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // User list — already ranked by total XP descending (getAllUsersProgress ->
            // getAllSorted), so this doubles as the real leaderboard.
            item {
                Text(
                    text = "Leaderboard — Ranked by XP",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            itemsIndexed(allUsers) { index, user ->
                AdminUserCard(
                    user = user,
                    rank = index + 1,
                    onClick = { selectedUser = user },
                    onAdjustXp = {
                        selectedUser = user
                        showAdjustDialog = true
                    }
                )
            }

            if (allUsers.isEmpty()) {
                item {
                    Text(
                        text = "No user progress data found.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Adjust XP Dialog
    if (showAdjustDialog && selectedUser != null) {
        AdjustXpDialog(
            userId = selectedUser!!.userId,
            currentXp = selectedUser!!.totalXp,
            onDismiss = { showAdjustDialog = false },
            onConfirm = { delta, note ->
                scope.launch {
                    xpManager?.adjustXp(
                        username = selectedUser!!.userId,
                        delta = delta,
                        adminNote = note,
                        adminUsername = adminUsername
                    )
                    refreshTrigger++
                    selectedUser = xpManager?.getProgress(selectedUser!!.userId)
                    Toast.makeText(context, "XP adjusted: ${if (delta > 0) "+" else ""}$delta", Toast.LENGTH_SHORT).show()
                }
                showAdjustDialog = false
            }
        )
    }

    // Module XP Configuration Dialog
    if (showModuleConfig) {
        ModuleXpConfigDialog(
            onDismiss = { showModuleConfig = false },
            onSave = {
                Toast.makeText(context, "Module XP configuration saved.", Toast.LENGTH_SHORT).show()
                showModuleConfig = false
            }
        )
    }

    // Level Threshold Configuration Dialog
    if (showLevelConfig) {
        LevelThresholdConfigDialog(
            onDismiss = { showLevelConfig = false },
            onSave = {
                Toast.makeText(context, "Level thresholds saved.", Toast.LENGTH_SHORT).show()
                showLevelConfig = false
            }
        )
    }

    if (showQuizXpConfig) {
        QuizXpConfigDialog(
            onDismiss = { showQuizXpConfig = false },
            onSave = {
                Toast.makeText(context, "Quiz XP configuration saved.", Toast.LENGTH_SHORT).show()
                showQuizXpConfig = false
            }
        )
    }

    if (showAchievementXpConfig) {
        AchievementXpConfigDialog(
            onDismiss = { showAchievementXpConfig = false },
            onSave = {
                Toast.makeText(context, "Achievement bonus XP saved.", Toast.LENGTH_SHORT).show()
                showAchievementXpConfig = false
            }
        )
    }

    if (showStreakConfig) {
        StreakConfigDialog(
            onDismiss = { showStreakConfig = false },
            onSave = {
                Toast.makeText(context, "Streak rewards saved.", Toast.LENGTH_SHORT).show()
                showStreakConfig = false
            }
        )
    }
}

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier.clickable(onClick = onClick),
        elevation = 4
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = "Configure",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AdminUserCard(
    user: UserProgressEntity,
    rank: Int,
    onClick: () -> Unit,
    onAdjustXp: () -> Unit
) {
    val levelName = GamificationConstants.getLevelName(user.currentLevel)
    val progress = GamificationConstants.getLevelProgress(user.totalXp, user.currentLevel)
    val rankColor = when (rank) {
        1 -> AmberYellow
        2, 3 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleMedium,
                color = rankColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp)
            )
            AnimatedProgressRing(
                progress = progress,
                size = 44.dp,
                strokeWidth = 4.dp,
                ringColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "${user.currentLevel}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.userId,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$levelName · ${user.totalXp} XP · ${user.currentStreak}d streak",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onAdjustXp) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Adjust XP",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun UserXpDetailView(
    user: UserProgressEntity,
    history: List<XpHistoryEntity>,
    attempts: List<QuizAttemptEntity> = emptyList(),
    onBack: () -> Unit,
    onAdjustXp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelName = GamificationConstants.getLevelName(user.currentLevel)
    val completedModules = user.completedModuleIds.split(",").filter { it.isNotBlank() }.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                Text(
                    text = user.userId,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            AppCard(modifier = Modifier.fillMaxWidth(), elevation = 8) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedProgressRing(
                        progress = GamificationConstants.getLevelProgress(user.totalXp, user.currentLevel),
                        size = 80.dp,
                        strokeWidth = 6.dp,
                        ringColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "${user.currentLevel}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Level ${user.currentLevel} — $levelName",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${user.totalXp} Total XP · ${user.currentStreak}d streak · $completedModules modules",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AppButton(text = "Adjust XP", onClick = onAdjustXp)
                }
            }
        }

        item {
            Text(
                text = "Quiz Attempts",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        items(attempts, key = { "attempt_${it.id}" }) { attempt ->
            AppCard(modifier = Modifier.fillMaxWidth(), elevation = 2) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = attempt.quizTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${attempt.difficulty} · Attempt #${attempt.attemptNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${attempt.scorePercent}% · ${if (attempt.passed) "Passed" else "Failed"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (attempt.passed) EmeraldGreen else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (attempts.isEmpty()) {
            item {
                Text(
                    text = "No quiz attempts for this user.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        item {
            Text(
                text = "XP History",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        items(history) { entry ->
            XpHistoryRow(entry = entry)
        }

        if (history.isEmpty()) {
            item {
                Text(
                    text = "No XP history for this user.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun AdjustXpDialog(
    userId: String,
    currentXp: Int,
    onDismiss: () -> Unit,
    onConfirm: (delta: Int, note: String) -> Unit
) {
    var xpDelta by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        AppCard {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Adjust XP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "User: $userId · Current XP: $currentXp",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = xpDelta,
                    onValueChange = { xpDelta = it },
                    label = { Text("XP Amount (+/-)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. 100 or -50") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Admin Note (Required)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Reason for adjustment") },
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val delta = xpDelta.toIntOrNull()
                            if (delta != null && note.isNotBlank()) {
                                onConfirm(delta, note)
                            }
                        },
                        enabled = xpDelta.toIntOrNull() != null && note.isNotBlank()
                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModuleXpConfigDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val moduleXpValues = remember { mutableStateMapOf<String, String>() }
    val allModuleXp = GamificationConstants.ModuleXp.getAllModuleXp()
    // Real, current module titles — was previously a stale hardcoded map referencing
    // module ids that no longer exist in the 3-module system.
    val moduleNames = remember { MockData.learningModules.associate { it.id to it.title } }

    LaunchedEffect(Unit) {
        allModuleXp.forEach { (id, xp) ->
            moduleXpValues[id] = xp.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Module XP Rewards",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Configure the XP reward earned for each module. Use Module Management to enable or disable modules.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    moduleXpValues.forEach { (moduleId, xpStr) ->
                        val name = moduleNames[moduleId] ?: moduleId
                        OutlinedTextField(
                            value = xpStr,
                            onValueChange = { moduleXpValues[moduleId] = it },
                            label = { Text(name, maxLines = 1) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("XP") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        moduleXpValues.forEach { (moduleId, xpStr) ->
                            xpStr.toIntOrNull()?.let { xp ->
                                GamificationConstants.ModuleXp.setModuleXp(moduleId, xp)
                            }
                        }
                        onSave()
                    }) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelThresholdConfigDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val thresholds = remember { mutableStateMapOf<Int, String>() }

    LaunchedEffect(Unit) {
        GamificationConstants.getLevelThresholds().forEach { (level, xp) ->
            thresholds[level] = xp.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Level Thresholds",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "XP required to reach each level",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    thresholds.toSortedMap().forEach { (level, xpStr) ->
                        val levelName = GamificationConstants.getLevelName(level)
                        OutlinedTextField(
                            value = xpStr,
                            onValueChange = { thresholds[level] = it },
                            label = { Text("Level $level — $levelName") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("XP") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        thresholds.forEach { (level, xpStr) ->
                            xpStr.toIntOrNull()?.let { xp ->
                                GamificationConstants.setLevelThreshold(level, xp)
                            }
                        }
                        onSave()
                    }) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizXpConfigDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var correctAnswer by remember { mutableStateOf(GamificationConstants.QuizXp.CORRECT_ANSWER.toString()) }
    var perfectScore by remember { mutableStateOf(GamificationConstants.QuizXp.PERFECT_SCORE.toString()) }
    var completion by remember { mutableStateOf(GamificationConstants.QuizXp.COMPLETION.toString()) }
    var firstAttemptPerfect by remember { mutableStateOf(GamificationConstants.QuizXp.FIRST_ATTEMPT_PERFECT.toString()) }
    var timeChallenge by remember { mutableStateOf(GamificationConstants.QuizXp.TIME_CHALLENGE.toString()) }
    var modulePerfectBonus by remember { mutableStateOf(GamificationConstants.MODULE_COMPLETION_PERFECT_BONUS.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Quiz & Module XP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "XP earned from quiz answers and completions",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    XpConfigField("Correct Answer", correctAnswer) { correctAnswer = it }
                    XpConfigField("Perfect Score", perfectScore) { perfectScore = it }
                    XpConfigField("Quiz Completion", completion) { completion = it }
                    XpConfigField("First-Attempt Perfect", firstAttemptPerfect) { firstAttemptPerfect = it }
                    XpConfigField("Time Challenge", timeChallenge) { timeChallenge = it }
                    XpConfigField("Module Perfect-Completion Bonus", modulePerfectBonus) { modulePerfectBonus = it }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        correctAnswer.toIntOrNull()?.let { GamificationConstants.QuizXp.setCorrectAnswer(it) }
                        perfectScore.toIntOrNull()?.let { GamificationConstants.QuizXp.setPerfectScore(it) }
                        completion.toIntOrNull()?.let { GamificationConstants.QuizXp.setCompletion(it) }
                        firstAttemptPerfect.toIntOrNull()?.let { GamificationConstants.QuizXp.setFirstAttemptPerfect(it) }
                        timeChallenge.toIntOrNull()?.let { GamificationConstants.QuizXp.setTimeChallenge(it) }
                        modulePerfectBonus.toIntOrNull()?.let { GamificationConstants.setConfigInt("module_perfect_bonus", it) }
                        onSave()
                    }) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementXpConfigDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val bonusValues = remember {
        mutableStateMapOf<String, String>().apply {
            AchievementDefinitions.ALL.forEach { def ->
                this[def.id] = GamificationConstants.AchievementXp.getBonusXp(def.id, def.bonusXp).toString()
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Achievement Bonus XP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Unlock conditions are fixed; only the bonus XP awarded on unlock is configurable.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AchievementDefinitions.ALL.forEach { def ->
                        XpConfigField("${def.icon} ${def.title}", bonusValues[def.id] ?: "0") {
                            bonusValues[def.id] = it
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        bonusValues.forEach { (id, xpStr) ->
                            xpStr.toIntOrNull()?.let { GamificationConstants.AchievementXp.setBonusXp(id, it) }
                        }
                        onSave()
                    }) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun StreakConfigDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val bonusValues = remember {
        mutableStateMapOf<Int, String>().apply {
            GamificationConstants.STREAK_BONUS_TABLE.forEach { (milestone, xp) -> this[milestone] = xp.toString() }
        }
    }
    val multiplierValues = remember {
        mutableStateMapOf<Int, String>().apply {
            GamificationConstants.STREAK_MULTIPLIER_TABLE.forEach { (threshold, mult) -> this[threshold] = mult.toString() }
        }
    }
    val dailyValues = remember {
        mutableStateMapOf<Int, String>().apply {
            GamificationConstants.DAILY_STREAK_REWARD_TABLE.forEach { (day, xp) -> this[day] = xp.toString() }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Streak Rewards",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Correct-answer streak bonuses, multipliers, and daily streak rewards",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Correct-Answer Streak Bonus XP",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    bonusValues.toSortedMap().forEach { (milestone, xpStr) ->
                        XpConfigField("$milestone in a row", xpStr) { bonusValues[milestone] = it }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Streak Multiplier",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    multiplierValues.toSortedMap().forEach { (threshold, multStr) ->
                        OutlinedTextField(
                            value = multStr,
                            onValueChange = { multiplierValues[threshold] = it },
                            label = { Text("$threshold in a row", maxLines = 1) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            suffix = { Text("x") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Daily Learning Streak Bonus XP",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    dailyValues.toSortedMap().forEach { (day, xpStr) ->
                        XpConfigField("Day $day", xpStr) { dailyValues[day] = it }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        bonusValues.forEach { (milestone, xpStr) ->
                            xpStr.toIntOrNull()?.let { GamificationConstants.setStreakBonus(milestone, it) }
                        }
                        multiplierValues.forEach { (threshold, multStr) ->
                            multStr.toFloatOrNull()?.let { GamificationConstants.setStreakMultiplier(threshold, it) }
                        }
                        dailyValues.forEach { (day, xpStr) ->
                            xpStr.toIntOrNull()?.let { GamificationConstants.setDailyStreakReward(day, it) }
                        }
                        onSave()
                    }) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun XpConfigField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, maxLines = 1) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        suffix = { Text("XP") }
    )
}
