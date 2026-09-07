package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.model.QuizData
import com.example.gamifiedroadsafetyawareness.model.RoadSafetyTopic
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.DifficultyPerformanceRow
import com.example.gamifiedroadsafetyawareness.model.db.GlobalAnswerStatsRow
import com.example.gamifiedroadsafetyawareness.model.db.IncorrectAnswerAnalyticsRow
import com.example.gamifiedroadsafetyawareness.model.db.ModulePerformanceRow
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptAnswerEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.SearchFilterBar
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.InfoBlue
import com.example.gamifiedroadsafetyawareness.ui.theme.NeutralGray
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val allQuizzes = listOf(QuizData.quiz_easy, QuizData.quiz_medium, QuizData.quiz_hard)

private enum class AdminSection(val label: String) {
    DASHBOARD("Dashboard"),
    USER_DRILL("User Search"),
    INCORRECT("Incorrect Analysis"),
    PERFORMANCE("Performance")
}

/**
 * Comprehensive admin answer review screen with:
 *  1. Dashboard summary cards (global stats)
 *  2. User → Module → Attempt drill-down with full answer review
 *  3. Incorrect answer analysis (error rates, most common wrong answers)
 *  4. User performance analytics (by difficulty, by module)
 */
@Composable
fun AdminAnswerReviewScreen(
    authManager: AuthManager,
    xpManager: XpManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var section by remember { mutableStateOf(AdminSection.DASHBOARD) }

    // Dashboard data
    var globalStats by remember { mutableStateOf<GlobalAnswerStatsRow?>(null) }

    // User drill-down data
    var searchQuery by remember { mutableStateOf("") }
    val allUsers = remember { authManager.getAllAccounts().filter { it.role == "USER" } }
    val filteredUsers = remember(allUsers, searchQuery) {
        if (searchQuery.isBlank()) allUsers
        else allUsers.filter { it.username.contains(searchQuery, ignoreCase = true) }
    }
    var selectedUser by remember { mutableStateOf<String?>(null) }
    var userAttempts by remember { mutableStateOf<List<QuizAttemptEntity>>(emptyList()) }
    var selectedAttempt by remember { mutableStateOf<QuizAttemptEntity?>(null) }
    var attemptAnswers by remember { mutableStateOf<List<QuizAttemptAnswerEntity>>(emptyList()) }

    // Incorrect analysis data
    var incorrectAnalytics by remember { mutableStateOf<List<IncorrectAnswerAnalyticsRow>>(emptyList()) }

    // Performance data
    var perfUser by remember { mutableStateOf("") }
    var diffPerf by remember { mutableStateOf<List<DifficultyPerformanceRow>>(emptyList()) }
    var modPerf by remember { mutableStateOf<List<ModulePerformanceRow>>(emptyList()) }

    // Load dashboard on start
    LaunchedEffect(Unit) {
        globalStats = xpManager.getGlobalAnswerStats()
        incorrectAnalytics = xpManager.getIncorrectAnswerAnalytics()
    }

    // User drill-down: load attempts when user is selected
    LaunchedEffect(selectedUser) {
        val user = selectedUser ?: return@LaunchedEffect
        userAttempts = xpManager.getAttemptsForUserAdmin(user)
        selectedAttempt = null
        attemptAnswers = emptyList()
    }

    // Attempt drill-down: load answers when attempt is selected
    LaunchedEffect(selectedAttempt) {
        val att = selectedAttempt ?: return@LaunchedEffect
        attemptAnswers = xpManager.getAnswersForAttempt(att.id)
    }

    // Performance: load when user is typed
    LaunchedEffect(perfUser) {
        if (perfUser.isNotBlank()) {
            diffPerf = xpManager.getUserPerformanceByDifficulty(perfUser)
            modPerf = xpManager.getUserPerformanceByModule(perfUser)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Header ──────────────────────────────────────────────────────────────
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
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Answer Review",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Admin Dashboard",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── Section Tabs ────────────────────────────────────────────────────────
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(AdminSection.entries.toList()) { s ->
                    AssistChip(
                        onClick = { section = s },
                        label = { Text(s.label, style = MaterialTheme.typography.labelMedium) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (section == s) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            labelColor = if (section == s) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        // ════════════════════════════════════════════════════════════════════════
        // SECTION: Dashboard
        // ════════════════════════════════════════════════════════════════════════
        when (section) {
            AdminSection.DASHBOARD -> {
                item {
                    Text(
                        text = "OVERVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val stats = globalStats
                if (stats == null) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DashMetric(
                                label = "Total Attempts",
                                value = "${stats.totalAttempts}",
                                icon = Icons.Rounded.Quiz,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            DashMetric(
                                label = "Total Answers",
                                value = "${stats.totalAnswers}",
                                icon = Icons.Rounded.Analytics,
                                color = InfoBlue,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DashMetric(
                                label = "Correct",
                                value = "${stats.correctAnswers}",
                                icon = Icons.Rounded.CheckCircle,
                                color = EmeraldGreen,
                                modifier = Modifier.weight(1f)
                            )
                            DashMetric(
                                label = "Incorrect",
                                value = "${stats.incorrectAnswers}",
                                icon = Icons.Rounded.Cancel,
                                color = TrafficRed,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        val accuracy = if (stats.totalAnswers > 0) {
                            (stats.correctAnswers * 100) / stats.totalAnswers
                        } else 0
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Average Accuracy",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$accuracy%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = when {
                                        accuracy >= 70 -> EmeraldGreen
                                        accuracy >= 40 -> AmberYellow
                                        else -> TrafficRed
                                    },
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // ════════════════════════════════════════════════════════════════════
            // SECTION: User Search → Drill-down
            // ════════════════════════════════════════════════════════════════════
            AdminSection.USER_DRILL -> {
                // Step 1: Search bar
                item {
                    SearchFilterBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        placeholder = "Search by username..."
                    )
                }

                // User list
                items(filteredUsers, key = { it.username }) { user ->
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedUser = user.username
                                selectedAttempt = null
                            },
                        containerColor = if (selectedUser == user.username)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface,
                        elevation = if (selectedUser == user.username) 4 else 2
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "@${user.username}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = user.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Step 2: Attempts list for selected user
                if (selectedUser != null) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Attempts by @${selectedUser}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (userAttempts.isEmpty()) {
                        item {
                            Text(
                                text = "No quiz attempts found for this user.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    items(userAttempts, key = { it.id }) { att ->
                        val dateStr = remember(att.completedAt) {
                            SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                                .format(Date(att.completedAt))
                        }
                        val isSelected = selectedAttempt?.id == att.id
                        AppCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAttempt = att },
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface,
                            elevation = if (isSelected) 4 else 2
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = att.quizTitle,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "$dateStr · ${att.difficulty} · Attempt #${att.attemptNumber}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${att.scorePercent}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (att.passed) EmeraldGreen else TrafficRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (att.passed) "Passed" else "Failed",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (att.passed) EmeraldGreen else TrafficRed
                                    )
                                }
                            }
                        }
                    }

                    // Step 3: Full answer review for selected attempt
                    if (selectedAttempt != null) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            AdminAttemptSummaryHeader(attempt = selectedAttempt!!)
                        }

                        if (attemptAnswers.isEmpty()) {
                            item {
                                Text(
                                    text = "No detailed answers recorded for this attempt. (This may be an older attempt from before the detailed logging system was introduced.)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            items(attemptAnswers, key = { it.id }) { answer ->
                                AdminAnswerRow(answer = answer, quizId = selectedAttempt!!.quizId)
                            }
                        }
                    }
                }
            }

            // ════════════════════════════════════════════════════════════════════
            // SECTION: Incorrect Answer Analysis
            // ════════════════════════════════════════════════════════════════════
            AdminSection.INCORRECT -> {
                item {
                    Text(
                        text = "FREQUENTLY MISSED QUESTIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = TrafficRed,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (incorrectAnalytics.isEmpty()) {
                    item {
                        Text(
                            text = "No answer data recorded yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }

                items(incorrectAnalytics.take(20)) { row ->
                    val errorRate = if (row.totalAnswers > 0) (row.errorCount * 100) / row.totalAnswers else 0
                    val quiz = allQuizzes.find { it.id == row.quizId }
                    val liveQuestion = quiz?.questions?.find { it.id == row.questionId }
                    val questionText = if (row.questionText.isNotBlank()) row.questionText
                    else liveQuestion?.question ?: "Question ${row.questionId}"

                    val topic = liveQuestion?.let {
                        RoadSafetyTopic.classify(it.question, it.options)
                    }

                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.ErrorOutline,
                                        contentDescription = null,
                                        tint = TrafficRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${errorRate}% error rate",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (errorRate >= 50) TrafficRed else AmberYellow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (row.difficulty.isNotBlank()) {
                                    DifficultyBadge(difficulty = row.difficulty)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = questionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "❌ ${row.errorCount} wrong",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TrafficRed
                                )
                                Text(
                                    text = "📝 ${row.totalAnswers} total",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (topic != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Topic: ${topic.displayName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            quiz?.title?.let { title ->
                                Text(
                                    text = "Module: $title",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // ════════════════════════════════════════════════════════════════════
            // SECTION: Performance Analytics
            // ════════════════════════════════════════════════════════════════════
            AdminSection.PERFORMANCE -> {
                item {
                    SearchFilterBar(
                        query = perfUser,
                        onQueryChange = { perfUser = it },
                        placeholder = "Enter username to analyze..."
                    )
                }

                if (perfUser.isNotBlank() && (diffPerf.isNotEmpty() || modPerf.isNotEmpty())) {
                    // By difficulty
                    item {
                        Text(
                            text = "PERFORMANCE BY DIFFICULTY",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(diffPerf) { row ->
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DifficultyBadge(difficulty = row.difficulty)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${row.attemptCount} attempts",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "${row.avgScore.toInt()}% avg",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = when {
                                        row.avgScore >= 70 -> EmeraldGreen
                                        row.avgScore >= 40 -> AmberYellow
                                        else -> TrafficRed
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // By module
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "PERFORMANCE BY MODULE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(modPerf) { row ->
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = row.quizTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${row.attemptCount} attempts · Best: ${row.bestScore}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${row.avgScore.toInt()}% avg",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = when {
                                            row.avgScore >= 70 -> EmeraldGreen
                                            row.avgScore >= 40 -> AmberYellow
                                            else -> TrafficRed
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Strengths / Weaknesses
                    if (modPerf.size >= 2) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            val strongest = modPerf.maxByOrNull { it.avgScore }
                            val weakest = modPerf.minByOrNull { it.avgScore }
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "STRENGTHS & WEAKNESSES",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (strongest != null) {
                                        Text(
                                            text = "💪 Strongest: ${strongest.quizTitle} (${strongest.avgScore.toInt()}% avg)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    if (weakest != null) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "📉 Weakest: ${weakest.quizTitle} (${weakest.avgScore.toInt()}% avg)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TrafficRed,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (perfUser.isNotBlank()) {
                    item {
                        Text(
                            text = "No data found for \"$perfUser\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Dashboard metric card
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
private fun DashMetric(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier, elevation = 2) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Admin attempt summary header (inside user drill-down)
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminAttemptSummaryHeader(attempt: QuizAttemptEntity) {
    val dateStr = remember(attempt.completedAt) {
        SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault()).format(Date(attempt.completedAt))
    }
    val resultColor = if (attempt.passed) EmeraldGreen else TrafficRed

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "@${attempt.userId}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = attempt.quizTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (attempt.passed) "PASSED" else "FAILED",
                        style = MaterialTheme.typography.labelMedium,
                        color = resultColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "${attempt.scorePercent}%",
                        style = MaterialTheme.typography.headlineSmall,
                        color = resultColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Attempt #${attempt.attemptNumber}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dateStr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("✅ ${attempt.correctCount}  ❌ ${attempt.incorrectCount}  ⚪ ${attempt.unansweredCount}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                Text("+${attempt.xpEarned} XP", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Admin answer row (expandable, inside user drill-down)
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
private fun AdminAnswerRow(answer: QuizAttemptAnswerEntity, quizId: String) {
    var expanded by remember { mutableStateOf(false) }

    val quiz = remember(quizId) { allQuizzes.find { it.id == quizId } }
    val liveQuestion = remember(quizId, answer.questionId) {
        quiz?.questions?.find { it.id == answer.questionId }
    }
    val choiceLabels = listOf("A", "B", "C", "D", "E", "F")

    val userText = if (answer.selectedOptionIndex == -1) {
        "Unanswered"
    } else if (answer.selectedAnswerText.isNotEmpty()) {
        "${choiceLabels.getOrElse(answer.selectedOptionIndex) { "?" }}. ${answer.selectedAnswerText}"
    } else {
        "${choiceLabels.getOrElse(answer.selectedOptionIndex) { "?" }}. ${liveQuestion?.options?.getOrNull(answer.selectedOptionIndex) ?: ""}"
    }
    val correctText = if (answer.correctAnswerText.isNotEmpty()) {
        "${choiceLabels.getOrElse(answer.correctOptionIndex) { "?" }}. ${answer.correctAnswerText}"
    } else {
        "${choiceLabels.getOrElse(answer.correctOptionIndex) { "?" }}. ${liveQuestion?.options?.getOrNull(answer.correctOptionIndex) ?: ""}"
    }

    val topic = runCatching { RoadSafetyTopic.valueOf(answer.topic) }
        .getOrDefault(RoadSafetyTopic.ROAD_COURTESY)
    val explanation = answer.explanation.ifEmpty { topic.explanation }
    val safetyTip = answer.safetyTip.ifEmpty { topic.safetyTip }
    val questionNum = if (answer.questionNumber > 0) answer.questionNumber else 0

    val statusIcon = when {
        answer.isCorrect -> Icons.Rounded.CheckCircle
        answer.selectedOptionIndex == -1 -> Icons.Rounded.RadioButtonUnchecked
        else -> Icons.Rounded.Cancel
    }
    val statusColor = when {
        answer.isCorrect -> EmeraldGreen
        answer.selectedOptionIndex == -1 -> NeutralGray
        else -> TrafficRed
    }

    AppCard(modifier = Modifier.fillMaxWidth(), elevation = 2) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (questionNum > 0) "Q$questionNum" else "Q",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = answer.questionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = if (expanded) Int.MAX_VALUE else 2
                )
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your Answer: $userText",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (answer.isCorrect) EmeraldGreen else TrafficRed
                    )
                    if (!answer.isCorrect) {
                        Text(
                            text = "Correct Answer: $correctText",
                            style = MaterialTheme.typography.bodySmall,
                            color = EmeraldGreen
                        )
                    }

                    if (answer.xpEarned > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+${answer.xpEarned} XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = topic.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💡 $safetyTip",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
