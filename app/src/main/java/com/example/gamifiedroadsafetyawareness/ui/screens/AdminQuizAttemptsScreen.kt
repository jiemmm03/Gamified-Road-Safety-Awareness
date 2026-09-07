package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FilterChip
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
import com.example.gamifiedroadsafetyawareness.ui.components.SearchFilterBar
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** All-users searchable/filterable quiz attempt log — admin-only. */
@Composable
fun AdminQuizAttemptsScreen(
    xpManager: XpManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attempts by remember { mutableStateOf<List<QuizAttemptEntity>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<Boolean?>(null) } // null = all, true = passed, false = failed
    var difficultyFilter by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        attempts = xpManager.getAllQuizAttempts()
    }

    val filtered = remember(attempts, searchQuery, statusFilter, difficultyFilter) {
        attempts.filter { attempt ->
            val matchesQuery = searchQuery.isBlank() ||
                attempt.userId.contains(searchQuery, ignoreCase = true) ||
                attempt.quizTitle.contains(searchQuery, ignoreCase = true)
            val matchesStatus = statusFilter == null || attempt.passed == statusFilter
            val matchesDifficulty = difficultyFilter == null || attempt.difficulty == difficultyFilter
            matchesQuery && matchesStatus && matchesDifficulty
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
                Text(
                    text = "All Quiz Attempts",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            SearchFilterBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by username or module..."
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(selected = statusFilter == null, onClick = { statusFilter = null }, label = { Text("All") })
                }
                item {
                    FilterChip(selected = statusFilter == true, onClick = { statusFilter = true }, label = { Text("Passed") })
                }
                item {
                    FilterChip(selected = statusFilter == false, onClick = { statusFilter = false }, label = { Text("Failed") })
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
                text = "${filtered.size} of ${attempts.size} attempts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(filtered, key = { it.id }) { attempt ->
            AdminAttemptRow(attempt)
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun AdminAttemptRow(attempt: QuizAttemptEntity) {
    val dateStr = remember(attempt.completedAt) {
        SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(attempt.completedAt))
    }
    AppCard(modifier = Modifier.fillMaxWidth(), elevation = 2) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "@${attempt.userId} · ${attempt.quizTitle}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$dateStr · ${attempt.difficulty} · Attempt #${attempt.attemptNumber}",
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
