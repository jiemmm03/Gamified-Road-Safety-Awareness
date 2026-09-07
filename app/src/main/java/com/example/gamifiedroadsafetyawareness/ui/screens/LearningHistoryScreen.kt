package com.example.gamifiedroadsafetyawareness.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private enum class HistoryFilter(val label: String) {
    ALL("All"), COMPLETED("Completed"), FAILED("Failed"),
    EASY("Easy"), MEDIUM("Medium"), HARD("Hard")
}

private enum class DateRangeFilter(val label: String, val days: Int?) {
    ALL_TIME("All Time", null),
    LAST_7_DAYS("Last 7 Days", 7),
    LAST_30_DAYS("Last 30 Days", 30)
}

/** Filterable list of the user's own past quiz attempts. Tap a row to reopen its full Module Summary. */
@Composable
fun LearningHistoryScreen(
    username: String,
    xpManager: XpManager,
    onOpenAttempt: (attemptId: Long) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attempts by remember { mutableStateOf<List<QuizAttemptEntity>?>(null) }
    var filter by remember { mutableStateOf(HistoryFilter.ALL) }
    var dateFilter by remember { mutableStateOf(DateRangeFilter.ALL_TIME) }

    LaunchedEffect(username) {
        if (username.isNotBlank()) {
            attempts = xpManager.getAttemptsForUser(username)
        }
    }

    val filtered = remember(attempts, filter, dateFilter) {
        val cutoff = dateFilter.days?.let { System.currentTimeMillis() - TimeUnit.DAYS.toMillis(it.toLong()) }
        (attempts ?: emptyList()).filter { attempt ->
            val matchesFilter = when (filter) {
                HistoryFilter.ALL -> true
                HistoryFilter.COMPLETED -> attempt.passed
                HistoryFilter.FAILED -> !attempt.passed
                HistoryFilter.EASY -> attempt.difficulty == "EASY"
                HistoryFilter.MEDIUM -> attempt.difficulty == "MEDIUM"
                HistoryFilter.HARD -> attempt.difficulty == "HARD"
            }
            val matchesDate = cutoff == null || attempt.completedAt >= cutoff
            matchesFilter && matchesDate
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "My Learning History",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(HistoryFilter.entries.toList()) { option ->
                FilterChipRow(text = option.label, selected = filter == option, onClick = { filter = option })
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DateRangeFilter.entries.toList()) { option ->
                FilterChipRow(text = option.label, selected = dateFilter == option, onClick = { dateFilter = option })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (attempts == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "No quiz attempts match these filters yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { attempt ->
                    AttemptRow(attempt = attempt, onClick = { onOpenAttempt(attempt.id) })
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun FilterChipRow(text: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun AttemptRow(attempt: QuizAttemptEntity, onClick: () -> Unit) {
    val dateStr = remember(attempt.completedAt) {
        SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault()).format(Date(attempt.completedAt))
    }

    AppCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), elevation = 2) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attempt.quizTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$dateStr · Attempt #${attempt.attemptNumber} · +${attempt.xpEarned} XP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${attempt.scorePercent}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (attempt.passed) EmeraldGreen else TrafficRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (attempt.passed) "Passed" else "Failed",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (attempt.passed) EmeraldGreen else TrafficRed
                )
            }
        }
    }
}
