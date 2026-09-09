package com.example.gamifiedroadsafetyawareness.ui.screens

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.firebase.*
import com.example.gamifiedroadsafetyawareness.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudMonitoringScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val syncManager = remember { FirebaseSyncManager.getInstance() }
    val scope = rememberCoroutineScope()

    var cloudUsers by remember { mutableStateOf<List<CloudUserStatus>>(emptyList()) }
    var cloudLogins by remember { mutableStateOf<List<CloudLoginEntry>>(emptyList()) }
    var cloudQuizzes by remember { mutableStateOf<List<CloudQuizAttemptEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedUserForDetails by remember { mutableStateOf<CloudUserStatus?>(null) }
    var userProgressDetail by remember { mutableStateOf<CloudUserProgressDetail?>(null) }
    var isLoadingUserDetail by remember { mutableStateOf(false) }

    // Live real-time listener from Firestore with auto-recovery
    LaunchedEffect(Unit) {
        // 1. Observe Users stream
        launch {
            while (isActive) {
                try {
                    syncManager.observeCloudUsers()
                        .catch { 
                            isLoading = false
                            delay(3000)
                        }
                        .collect { users ->
                            cloudUsers = users
                            isLoading = false
                        }
                } catch (e: Exception) {
                    isLoading = false
                    delay(3000)
                }
            }
        }
        // 2. Observe Login Activity stream
        launch {
            while (isActive) {
                try {
                    syncManager.observeRecentCloudLogins(limit = 100)
                        .catch { delay(3000) }
                        .collect { logins ->
                            cloudLogins = logins
                        }
                } catch (e: Exception) {
                    delay(3000)
                }
            }
        }
        // 3. Observe Quiz Attempts stream
        launch {
            while (isActive) {
                try {
                    syncManager.observeCloudQuizAttempts(limit = 50)
                        .catch { delay(3000) }
                        .collect { attempts ->
                            cloudQuizzes = attempts
                        }
                } catch (e: Exception) {
                    delay(3000)
                }
            }
        }
    }

    // Manual Refresh fallback
    fun refreshData() {
        scope.launch {
            isLoading = true
            try {
                val users = withContext(Dispatchers.IO) { syncManager.getCloudUsers() }
                val logins = withContext(Dispatchers.IO) { syncManager.getRecentCloudLogins(limit = 100) }
                val quizzes = withContext(Dispatchers.IO) { syncManager.getCloudQuizAttempts(limit = 50) }
                cloudUsers = users
                cloudLogins = logins
                cloudQuizzes = quizzes
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    // Pulsing live streaming dot animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val onlineCount = remember(cloudUsers) { cloudUsers.count { it.isOnline } }
    val failedAttempts = remember(cloudLogins) { cloudLogins.count { it.eventType == "FAILED_LOGIN" } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Device Live Monitor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                            )
                        }
                        Text(
                            text = "Connected to Firebase Cloud Firestore",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { refreshData() },
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Dimens.dashboardHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing),
            contentPadding = PaddingValues(vertical = Dimens.spacingMedium)
        ) {
            // ── Project Telemetry Card ──────────────────────────────────────
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.35f)),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        NavyPrimary.copy(alpha = 0.07f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .scale(pulseScale)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen)
                                )
                                Text(
                                    text = "REAL-TIME DEVICE TELEMETRY",
                                    style = AppTypeScale.eyebrowLabel,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = EmeraldGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Sensors,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = EmeraldGreen
                                    )
                                    Text(
                                        text = "LIVE STREAM",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        color = EmeraldGreen
                                    )
                                }
                            }
                        }

                        // Metric Counter Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CloudMetricBox(
                                label = "ONLINE NOW",
                                value = onlineCount.toString(),
                                valueColor = EmeraldGreen,
                                icon = Icons.Rounded.Wifi,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "PHONES",
                                value = cloudUsers.size.toString(),
                                valueColor = MaterialTheme.colorScheme.primary,
                                icon = Icons.Rounded.PhoneAndroid,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "QUIZZES",
                                value = cloudQuizzes.size.toString(),
                                valueColor = InfoBlue,
                                icon = Icons.Rounded.Quiz,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "ALERTS",
                                value = failedAttempts.toString(),
                                valueColor = if (failedAttempts > 0) TrafficRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                icon = Icons.Rounded.Security,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // ── Segmented Tab Selector (3 Tabs) ─────────────────────────────
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        TabButton(
                            title = "Devices (${cloudUsers.size})",
                            isSelected = selectedTab == 0,
                            icon = Icons.Rounded.PhoneAndroid,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = 0 }
                        )
                        TabButton(
                            title = "Quizzes (${cloudQuizzes.size})",
                            isSelected = selectedTab == 1,
                            icon = Icons.Rounded.Quiz,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = 1 }
                        )
                        TabButton(
                            title = "Logins (${cloudLogins.size})",
                            isSelected = selectedTab == 2,
                            icon = Icons.Rounded.History,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = 2 }
                        )
                    }
                }
            }

            // ── Content: Tab 0 (Active Devices & Presence) ────────────────────
            if (selectedTab == 0) {
                if (cloudUsers.isEmpty() && !isLoading) {
                    item {
                        EmptyCloudView(
                            icon = Icons.Rounded.PersonSearch,
                            title = "No Active Devices Found",
                            description = "Once another mobile phone logs in or registers, their phone model, online status, and live telemetry will appear here instantly.",
                            onTestPing = {
                                scope.launch {
                                    syncManager.recordUserLogin(
                                        username = "admin",
                                        displayName = "Administrator",
                                        role = "ADMIN",
                                        isSuccess = true
                                    )
                                    refreshData()
                                }
                            }
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "💡 Tap any user to view detailed phone specs & learning stats",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                        )
                    }
                    items(cloudUsers, key = { it.username }) { user ->
                        CloudUserCard(
                            user = user,
                            onClick = {
                                selectedUserForDetails = user
                                isLoadingUserDetail = true
                                scope.launch {
                                    val progress = withContext(Dispatchers.IO) {
                                        syncManager.getCloudUserProgress(user.username)
                                    }
                                    userProgressDetail = progress
                                    isLoadingUserDetail = false
                                }
                            }
                        )
                    }
                }
            }

            // ── Content: Tab 1 (Quiz Submissions Stream) ─────────────────────
            if (selectedTab == 1) {
                if (cloudQuizzes.isEmpty() && !isLoading) {
                    item {
                        EmptyCloudView(
                            icon = Icons.Rounded.RateReview,
                            title = "No Quiz Attempts Yet",
                            description = "When users finish tests or quizzes on their mobile phones, scores and answers will stream here in real time.",
                            onTestPing = {
                                scope.launch {
                                    syncManager.syncQuizAttempt(
                                        com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity(
                                            userId = "test_user",
                                            quizId = "traffic_signs_mastery",
                                            moduleId = "mod_signs",
                                            quizTitle = "Traffic Signs Mastery",
                                            difficulty = "EASY",
                                            attemptNumber = 1,
                                            totalQuestions = 5,
                                            correctCount = 5,
                                            incorrectCount = 0,
                                            unansweredCount = 0,
                                            scorePercent = 100,
                                            passed = true,
                                            xpEarned = 50,
                                            bestComboStreak = 5,
                                            leveledUp = false,
                                            levelAfter = 1,
                                            timeSpentSeconds = 42,
                                            timeChallengeCompleted = true,
                                            startedAt = System.currentTimeMillis() - 42000L,
                                            completedAt = System.currentTimeMillis()
                                        )
                                    )
                                    refreshData()
                                }
                            }
                        )
                    }
                } else {
                    items(cloudQuizzes, key = { "${it.userId}_${it.quizId}_${it.timestamp}" }) { quiz ->
                        CloudQuizAttemptCard(quiz = quiz)
                    }
                }
            }

            // ── Content: Tab 2 (Login & Security Stream) ─────────────────────
            if (selectedTab == 2) {
                if (cloudLogins.isEmpty() && !isLoading) {
                    item {
                        EmptyCloudView(
                            icon = Icons.Rounded.HistoryToggleOff,
                            title = "No Login Records Yet",
                            description = "Every login, logout, and password attempt made on other phones is captured and streamed to Firestore.",
                            onTestPing = {
                                scope.launch {
                                    syncManager.recordUserLogin(
                                        username = "test_user",
                                        displayName = "Officer",
                                        role = "USER",
                                        isSuccess = true
                                    )
                                    refreshData()
                                }
                            }
                        )
                    }
                } else {
                    items(cloudLogins, key = { "${it.username}_${it.timestamp}" }) { login ->
                        CloudLoginCard(login = login)
                    }
                }
            }
        }
    }

    // ── Device Telemetry Modal Bottom Sheet ─────────────────────────────────
    selectedUserForDetails?.let { user ->
        ModalBottomSheet(
            onDismissRequest = {
                selectedUserForDetails = null
                userProgressDetail = null
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (user.role == "ADMIN") Icons.Rounded.Shield else Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Column {
                            Text(
                                text = user.displayName.ifBlank { user.username },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "@${user.username} · ${user.role}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (user.isOnline) EmeraldGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(
                            1.dp,
                            if (user.isOnline) EmeraldGreen.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = if (user.isOnline) "🟢 ONLINE" else "⚪ OFFLINE",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (user.isOnline) EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))

                // Hardware telemetry
                Text(
                    text = "HARDWARE & SESSION TELEMETRY",
                    style = AppTypeScale.eyebrowLabel,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DetailInfoRow(label = "📱 Mobile Device", value = user.deviceInfo.ifBlank { "Unknown Android Phone" })
                        DetailInfoRow(label = "🔢 Total Sessions", value = "${user.loginCount} logins recorded")
                        if (user.lastLoginAt > 0) {
                            val df = SimpleDateFormat("MMM dd, yyyy · hh:mm:ss a", Locale.getDefault())
                            DetailInfoRow(label = "🕒 Last Seen", value = df.format(Date(user.lastLoginAt)))
                        }
                    }
                }

                // Cloud Progress telemetry
                Text(
                    text = "TRAINING & LEARNER METRICS",
                    style = AppTypeScale.eyebrowLabel,
                    color = MaterialTheme.colorScheme.primary
                )

                if (isLoadingUserDetail) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else {
                    userProgressDetail?.let { progress ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CloudMetricBox(
                                label = "LEVEL",
                                value = "Lv.${progress.currentLevel}",
                                valueColor = BadgeGold,
                                icon = Icons.Rounded.Stars,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "TOTAL XP",
                                value = "${progress.totalXp} XP",
                                valueColor = EmeraldGreen,
                                icon = Icons.Rounded.Bolt,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "STREAK",
                                value = "${progress.currentStreak}d",
                                valueColor = AmberYellow,
                                icon = Icons.Rounded.LocalFireDepartment,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CloudMetricBox(
                                label = "QUIZZES",
                                value = "${progress.quizzesCompleted}",
                                valueColor = InfoBlue,
                                icon = Icons.Rounded.CheckCircle,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } ?: run {
                        Text(
                            text = "No training progress synced yet for this device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun CloudMetricBox(
    label: String,
    value: String,
    valueColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = valueColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                ),
                color = valueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
        label = "tab_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "tab_color"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.5.sp
                ),
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CloudUserCard(
    user: CloudUserStatus,
    onClick: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar with live indicator
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = if (user.role == "ADMIN") BadgeGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (user.role == "ADMIN") Icons.Rounded.Shield else Icons.Rounded.Person,
                            contentDescription = null,
                            tint = if (user.role == "ADMIN") BadgeGold else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                // Online/Offline status dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (user.isOnline) EmeraldGreen else Color.Gray)
                        .padding(2.dp)
                )
            }

            // User info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = user.displayName.ifBlank { user.username },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (user.role == "ADMIN") BadgeGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = user.role,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (user.role == "ADMIN") BadgeGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (user.deviceInfo.isNotBlank()) {
                    Text(
                        text = "📱 ${user.deviceInfo}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (user.lastLoginAt > 0) {
                    Text(
                        text = "Last active: ${timeFormat.format(Date(user.lastLoginAt))}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Presence Chip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (user.isOnline) EmeraldGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(
                    1.dp,
                    if (user.isOnline) EmeraldGreen.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = if (user.isOnline) "ONLINE" else "OFFLINE",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    color = if (user.isOnline) EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CloudQuizAttemptCard(quiz: CloudQuizAttemptEntry) {
    val timeFormat = remember { SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault()) }
    val statusColor = if (quiz.passed) EmeraldGreen else TrafficRed

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.35f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = statusColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (quiz.passed) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = quiz.quizId.replace("_", " ").replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = if (quiz.passed) "PASSED (${quiz.percentage}%)" else "FAILED (${quiz.percentage}%)",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = statusColor
                        )
                    }
                }

                Text(
                    text = "User: @${quiz.userId} · Score: ${quiz.score}/${quiz.totalQuestions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (quiz.timeSpentSeconds > 0) {
                    Text(
                        text = "⏱ Time Spent: ${quiz.timeSpentSeconds}s",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = timeFormat.format(Date(quiz.timestamp)),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
private fun CloudLoginCard(login: CloudLoginEntry) {
    val timeFormat = remember { SimpleDateFormat("MMM dd, yyyy · hh:mm:ss a", Locale.getDefault()) }
    val isSuccess = login.status == "SUCCESS"
    val isLogout = login.eventType == "LOGOUT"

    val statusColor = when {
        isLogout -> InfoBlue
        isSuccess -> EmeraldGreen
        else -> TrafficRed
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.35f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = statusColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when {
                            isLogout -> Icons.AutoMirrored.Rounded.Logout
                            isSuccess -> Icons.Rounded.CheckCircle
                            else -> Icons.Rounded.Error
                        },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = login.displayName.ifBlank { login.username },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = login.eventType,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = statusColor
                        )
                    }
                }

                Text(
                    text = "@${login.username} · ${login.role}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!login.failureReason.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Reason: ${login.failureReason}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = TrafficRed
                    )
                }

                if (login.deviceInfo.isNotBlank()) {
                    Text(
                        text = "📱 ${login.deviceInfo}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = timeFormat.format(Date(login.timestamp)),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
private fun EmptyCloudView(
    icon: ImageVector,
    title: String,
    description: String,
    onTestPing: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onTestPing,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Rounded.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Send Test Ping to Firestore", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
