package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.SearchFilterBar
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** All-users searchable/filterable quiz and simulation attempt log — admin/super-admin monitoring. */
@Composable
fun AdminQuizAttemptsScreen(
    xpManager: XpManager,
    onReviewAttempt: (Long) -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attempts by remember { mutableStateOf<List<QuizAttemptEntity>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<Boolean?>(null) } // null = all, true = passed, false = failed
    var typeFilter by remember { mutableStateOf<String?>(null) } // null = all, "SIMULATION", "QUIZ"
    var difficultyFilter by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        attempts = xpManager.getAllQuizAttempts()
    }

    val filtered = remember(attempts, searchQuery, statusFilter, typeFilter, difficultyFilter) {
        attempts.filter { attempt ->
            val matchesQuery = searchQuery.isBlank() ||
                attempt.userId.contains(searchQuery, ignoreCase = true) ||
                attempt.quizTitle.contains(searchQuery, ignoreCase = true)
            val matchesStatus = statusFilter == null || attempt.passed == statusFilter
            val matchesType = when (typeFilter) {
                "SIMULATION" -> attempt.quizId.contains("simulation", ignoreCase = true)
                "QUIZ" -> !attempt.quizId.contains("simulation", ignoreCase = true)
                else -> true
            }
            val matchesDifficulty = difficultyFilter == null || attempt.difficulty == difficultyFilter
            matchesQuery && matchesStatus && matchesType && matchesDifficulty
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Monitoring & Attempts",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Admin & Super Admin Activity Log",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            SearchFilterBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by user or simulation scenario..."
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(selected = typeFilter == null && statusFilter == null, onClick = { typeFilter = null; statusFilter = null }, label = { Text("All Attempts") })
                }
                item {
                    FilterChip(selected = typeFilter == "SIMULATION", onClick = { typeFilter = if (typeFilter == "SIMULATION") null else "SIMULATION" }, label = { Text("Simulations 🚗") })
                }
                item {
                    FilterChip(selected = typeFilter == "QUIZ", onClick = { typeFilter = if (typeFilter == "QUIZ") null else "QUIZ" }, label = { Text("Quizzes 📝") })
                }
                item {
                    FilterChip(selected = statusFilter == true, onClick = { statusFilter = if (statusFilter == true) null else true }, label = { Text("Passed") })
                }
                item {
                    FilterChip(selected = statusFilter == false, onClick = { statusFilter = if (statusFilter == false) null else false }, label = { Text("Failed") })
                }
                item {
                    FilterChip(selected = difficultyFilter == "EASY", onClick = { difficultyFilter = if (difficultyFilter == "EASY") null else "EASY" }, label = { Text("Easy") })
                }
                item {
                    FilterChip(selected = difficultyFilter == "MEDIUM", onClick = { difficultyFilter = if (difficultyFilter == "MEDIUM") null else "MEDIUM" }, label = { Text("Medium") })
                }
                item {
                    FilterChip(selected = difficultyFilter == "HARD", onClick = { difficultyFilter = if (difficultyFilter == "HARD") null else "HARD" }, label = { Text("Hard") })
                }
            }
        }

        item {
            Text(
                text = "${filtered.size} of ${attempts.size} recorded attempts (Tap to review answers)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(filtered, key = { it.id }) { attempt ->
            AdminAttemptRow(
                attempt = attempt,
                onClick = { onReviewAttempt(attempt.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun AdminAttemptRow(
    attempt: QuizAttemptEntity,
    onClick: () -> Unit
) {
    val dateStr = remember(attempt.completedAt) {
        SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(attempt.completedAt))
    }
    val isSim = attempt.quizId.contains("simulation", ignoreCase = true)

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        containerColor = MaterialTheme.colorScheme.surface,
        elevation = 2
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isSim) "🚗 " else "📝 ",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "@${attempt.userId} · ${attempt.quizTitle}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$dateStr · ${attempt.difficulty} · Attempt #${attempt.attemptNumber} · ${attempt.correctCount}/${attempt.totalQuestions} Correct",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
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
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Review",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
