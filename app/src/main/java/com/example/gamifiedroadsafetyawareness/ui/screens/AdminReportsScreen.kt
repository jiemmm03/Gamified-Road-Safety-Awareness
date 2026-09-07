package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.model.AchievementDefinitions
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.QuizData
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.splitCsv
import com.example.gamifiedroadsafetyawareness.model.db.QuizAggregateRow
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.ui.components.TrendLineChart
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.InfoBlue
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Composable
fun AdminReportsScreen(
    authManager: AuthManager,
    xpManager: XpManager? = null,
    onOpenModuleAnalytics: (quizId: String) -> Unit = {},
    onOpenQuizAttempts: () -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var allProgress by remember { mutableStateOf<List<UserProgressEntity>>(emptyList()) }
    var achievementCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var quizAggregates by remember { mutableStateOf<List<QuizAggregateRow>>(emptyList()) }
    val allAccounts = remember { authManager.getAllAccounts() }
    val learnerUsernames = remember(allAccounts) {
        allAccounts.filter { it.role == "USER" }.map { it.username }.toSet()
    }
    val totalLearners = learnerUsernames.size

    LaunchedEffect(xpManager, learnerUsernames) {
        val manager = xpManager ?: return@LaunchedEffect
        allProgress = manager.getAllUsersProgress().filter { it.userId in learnerUsernames }
        achievementCounts = manager.getAchievementUnlockCounts(learnerUsernames)
        quizAggregates = manager.getPerQuizAggregates()
    }

    val allQuizzes = remember { listOf(QuizData.quiz_easy, QuizData.quiz_medium, QuizData.quiz_hard) }
    fun quizTitleFor(quizId: String): String = allQuizzes.find { it.id == quizId }?.title ?: quizId

    val mostDifficultQuiz = remember(quizAggregates) { quizAggregates.minByOrNull { it.avgScore } }
    val leastDifficultQuiz = remember(quizAggregates) { quizAggregates.maxByOrNull { it.avgScore } }
    val mostAttemptedQuiz = remember(quizAggregates) { quizAggregates.maxByOrNull { it.attemptCount } }

    val today = remember { LocalDate.now() }

    val activeUsers7d = remember(allProgress) {
        allProgress.count { p ->
            p.lastActivityDate
                ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?.let { ChronoUnit.DAYS.between(it, today) <= 7 }
                ?: false
        }
    }
    val averageLevel = remember(allProgress) {
        if (allProgress.isNotEmpty()) allProgress.map { it.currentLevel }.average() else 0.0
    }

    // Quiz pass rate — real data from QuizAggregateRow
    val totalAttempts = remember(quizAggregates) { quizAggregates.sumOf { it.attemptCount } }
    val totalPassed = remember(quizAggregates) { quizAggregates.sumOf { it.passedCount } }
    val quizPassRate = remember(totalAttempts, totalPassed) {
        if (totalAttempts > 0) (totalPassed * 100.0 / totalAttempts) else 0.0
    }

    // Registration trend — weekly new learner-account counts over the last 8 weeks.
    val weeklyRegistrations = remember(allAccounts, today) {
        val buckets = IntArray(8)
        allAccounts.filter { it.role == "USER" }.forEach { account ->
            val date = Instant.ofEpochMilli(account.createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
            val weeksAgo = ChronoUnit.WEEKS.between(date, today).toInt()
            if (weeksAgo in 0..7) buckets[7 - weeksAgo]++
        }
        buckets.toList()
    }

    // Module completion rates — real data from each user's completed_module_ids CSV.
    val moduleCompletionRates = remember(allProgress) {
        MockData.learningModules.map { module ->
            val completedCount = allProgress.count { module.id in it.completedModuleIds.splitCsv() }
            val rate = if (totalLearners > 0) (completedCount * 100 / totalLearners) else 0
            Triple(module.title, completedCount, rate)
        }
    }

    // XP distribution histogram.
    val xpBuckets = remember(allProgress) {
        val ranges = listOf(0..100, 101..500, 501..1000, 1001..2500, 2501..Int.MAX_VALUE)
        val labels = listOf("0–100", "101–500", "501–1K", "1K–2.5K", "2.5K+")
        labels.zip(ranges).map { (label, range) -> label to allProgress.count { it.totalXp in range } }
    }

    // Top unlocked achievements.
    val topAchievements = remember(achievementCounts) {
        achievementCounts.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { (id, count) -> (AchievementDefinitions.ALL.find { def -> def.id == id }?.title ?: id) to count }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimens.dashboardHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                Column {
                    Text(
                        text = "Reports & Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "System-wide engagement and progress metrics",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── ① Summary Stats (2×2 grid) ─────────────────────────────────────
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    ReportStatCard(
                        label = "Total Learners",
                        value = totalLearners.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatCard(
                        label = "Active (7 days)",
                        value = activeUsers7d.toString(),
                        color = EmeraldGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
                ) {
                    ReportStatCard(
                        label = "Average Level",
                        value = String.format("%.1f", averageLevel),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatCard(
                        label = "Quiz Pass Rate",
                        value = if (totalAttempts > 0) "${quizPassRate.toInt()}%" else "—",
                        color = if (quizPassRate >= 70) EmeraldGreen else AmberYellow,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // ── ② Registration Trend ────────────────────────────────────────────
        item {
            ReportSectionCard(title = "Registration Trend (8 Weeks)") {
                if (weeklyRegistrations.all { it == 0 }) {
                    EmptyDataText("No registrations in the last 8 weeks.")
                } else {
                    TrendLineChart(
                        values = weeklyRegistrations.map { it.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                }
            }
        }

        // ── ③ Module Completion Rates ───────────────────────────────────────
        item {
            ReportSectionCard(title = "Module Completion Rates") {
                if (allProgress.isEmpty()) {
                    EmptyDataText("No learner progress recorded yet.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                        moduleCompletionRates.forEach { (title, count, rate) ->
                            ReportBarRow(
                                label = title,
                                valueLabel = "$count of $totalLearners ($rate%)",
                                fraction = rate / 100f,
                                color = InfoBlue
                            )
                        }
                    }
                }
            }
        }

        // ── ④ Quiz Performance ──────────────────────────────────────────────
        item {
            ReportSectionCard(title = "Quiz Performance") {
                if (quizAggregates.isEmpty()) {
                    EmptyDataText("No quiz attempts recorded yet.")
                } else {
                    // Average score per quiz
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                        quizAggregates.forEach { row ->
                            ReportBarRow(
                                label = quizTitleFor(row.quizId),
                                valueLabel = "${row.avgScore.toInt()}% avg · ${row.attemptCount} attempts",
                                fraction = (row.avgScore / 100).toFloat(),
                                color = InfoBlue,
                                onClick = { onOpenModuleAnalytics(row.quizId) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.spacingSmall))

                    // Difficulty & engagement rankings
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)) {
                        Text(
                            text = "DIFFICULTY & ENGAGEMENT",
                            style = AppTypeScale.eyebrowLabel,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                        mostDifficultQuiz?.let {
                            RankingRow("Most Difficult", quizTitleFor(it.quizId), "${it.avgScore.toInt()}% avg", TrafficRed)
                        }
                        leastDifficultQuiz?.let {
                            RankingRow("Easiest", quizTitleFor(it.quizId), "${it.avgScore.toInt()}% avg", EmeraldGreen)
                        }
                        mostAttemptedQuiz?.let {
                            RankingRow("Most Attempted", quizTitleFor(it.quizId), "${it.attemptCount} attempts", MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.spacingSmall))

                    // Summary line
                    val avgTime = quizAggregates.map { it.avgTimeSeconds }.average().takeIf { !it.isNaN() } ?: 0.0
                    Text(
                        text = "Total: $totalAttempts attempts · $totalPassed passed · ${totalAttempts - totalPassed} failed · ${avgTime.toInt()}s avg time",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                    // View all attempts link
                    TextButton(onClick = onOpenQuizAttempts) {
                        Text(
                            text = "View All Quiz Attempts →",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // ── ⑤ XP Distribution ───────────────────────────────────────────────
        item {
            ReportSectionCard(title = "XP Distribution") {
                if (allProgress.isEmpty()) {
                    EmptyDataText("No XP data available yet.")
                } else {
                    val maxCount = xpBuckets.maxOfOrNull { it.second } ?: 0
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                        xpBuckets.forEach { (label, count) ->
                            ReportBarRow(
                                label = "$label XP",
                                valueLabel = "$count users",
                                fraction = if (maxCount > 0) count / maxCount.toFloat() else 0f,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // ── ⑥ Top Achievements ──────────────────────────────────────────────
        item {
            ReportSectionCard(title = "Top Achievements Unlocked") {
                if (topAchievements.isEmpty()) {
                    EmptyDataText("No achievements unlocked yet.")
                } else {
                    val maxCount = topAchievements.maxOf { it.second }
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                        topAchievements.forEach { (title, count) ->
                            ReportBarRow(
                                label = title,
                                valueLabel = "$count unlocks",
                                fraction = count / maxCount.toFloat(),
                                color = AmberYellow
                            )
                        }
                    }
                }
            }
        }

        // ── Bottom spacer ───────────────────────────────────────────────────
        item { Spacer(modifier = Modifier.height(Dimens.spacingMedium)) }
    }
}

// ─── Stat Card ──────────────────────────────────────────────────────────────

@Composable
private fun ReportStatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    DashboardCard(modifier = modifier) {
        Column(modifier = Modifier.padding(Dimens.spacingMedium)) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = color
            )
            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Section Card ───────────────────────────────────────────────────────────

@Composable
private fun ReportSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    DashboardCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(Dimens.cardPadding)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            content()
        }
    }
}

// ─── Ranking Row ────────────────────────────────────────────────────────────

@Composable
private fun RankingRow(rank: String, title: String, detail: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = rank,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = detail,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

// ─── Bar Row ────────────────────────────────────────────────────────────────

@Composable
private fun ReportBarRow(
    label: String,
    valueLabel: String,
    fraction: Float,
    color: Color,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = if (onClick != null) Modifier.fillMaxWidth().clickable(onClick = onClick)
        else Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { fraction.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}

// ─── Empty Data State ───────────────────────────────────────────────────────

@Composable
private fun EmptyDataText(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingMedium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
