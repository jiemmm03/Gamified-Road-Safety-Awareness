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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.QuizData
import com.example.gamifiedroadsafetyawareness.model.RoadSafetyTopic
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptAnswerEntity
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.NeutralGray
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val allQuizzes = listOf(QuizData.quiz_easy, QuizData.quiz_medium, QuizData.quiz_hard)

private enum class AnswerFilter(val label: String) {
    ALL("All Answers"),
    CORRECT("✅ Correct"),
    INCORRECT("❌ Incorrect"),
    UNANSWERED("⚪ Unanswered")
}

/**
 * Per-question answer review for one completed attempt. Shows a summary header with all attempt
 * stats, filterable question cards with status badges, expandable explanations, per-question XP,
 * and related safety tips. Only reachable from a completed attempt's Module Summary — that's how
 * "don't reveal answers before completion" is enforced structurally.
 */
@Composable
fun ModuleReviewScreen(
    attemptId: Long,
    xpManager: XpManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var attempt by remember(attemptId) { mutableStateOf<QuizAttemptEntity?>(null) }
    var answers by remember(attemptId) { mutableStateOf<List<QuizAttemptAnswerEntity>?>(null) }
    var filter by remember { mutableStateOf(AnswerFilter.ALL) }

    LaunchedEffect(attemptId) {
        attempt = xpManager.getAttempt(attemptId)
        answers = xpManager.getAnswersForAttempt(attemptId)
    }

    val currentAttempt = attempt
    val currentAnswers = answers

    val filtered = remember(currentAnswers, filter) {
        currentAnswers?.filter { answer ->
            when (filter) {
                AnswerFilter.ALL -> true
                AnswerFilter.CORRECT -> answer.isCorrect
                AnswerFilter.INCORRECT -> !answer.isCorrect && answer.selectedOptionIndex != -1
                AnswerFilter.UNANSWERED -> answer.selectedOptionIndex == -1
            }
        } ?: emptyList()
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
                        text = "Review Answers",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    if (currentAttempt != null) {
                        Text(
                            text = currentAttempt.quizTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ── Summary Card ────────────────────────────────────────────────────────
        if (currentAttempt != null) {
            item {
                ReviewSummaryCard(attempt = currentAttempt)
            }
        }

        // ── Filter Chips ────────────────────────────────────────────────────────
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AnswerFilter.entries.toList()) { option ->
                    val count = when (option) {
                        AnswerFilter.ALL -> currentAnswers?.size ?: 0
                        AnswerFilter.CORRECT -> currentAnswers?.count { it.isCorrect } ?: 0
                        AnswerFilter.INCORRECT -> currentAnswers?.count { !it.isCorrect && it.selectedOptionIndex != -1 } ?: 0
                        AnswerFilter.UNANSWERED -> currentAnswers?.count { it.selectedOptionIndex == -1 } ?: 0
                    }
                    AssistChip(
                        onClick = { filter = option },
                        label = { Text("${option.label} ($count)") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (filter == option) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            labelColor = if (filter == option) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        // ── Loading / Empty ─────────────────────────────────────────────────────
        if (currentAnswers == null) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (filtered.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No answers match this filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── Question Cards ──────────────────────────────────────────────────────
        items(filtered, key = { it.id }) { answer ->
            ReviewQuestionCard(answer = answer, quizId = currentAttempt?.quizId.orEmpty())
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Summary Card
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
private fun ReviewSummaryCard(attempt: QuizAttemptEntity) {
    val dateStr = remember(attempt.completedAt) {
        SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault()).format(Date(attempt.completedAt))
    }
    val resultColor = if (attempt.passed) EmeraldGreen else TrafficRed
    val resultLabel = if (attempt.passed) "PASSED" else "FAILED"

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = resultLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = resultColor,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                DifficultyBadge(difficulty = attempt.difficulty)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Stats grid (2 columns)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SummaryRow("Attempt #", "${attempt.attemptNumber}")
                SummaryRow("Date", dateStr)
                SummaryRow("Score", "${attempt.scorePercent}%")
                SummaryRow("Correct", "${attempt.correctCount} / ${attempt.totalQuestions}")
                SummaryRow("Incorrect", "${attempt.incorrectCount}")
                SummaryRow("Unanswered", "${attempt.unansweredCount}")
                SummaryRow("Training XP Earned", "+${attempt.xpEarned}")
                SummaryRow("Time Spent", formatTime(attempt.timeSpentSeconds))
                if (attempt.bestComboStreak > 0) {
                    SummaryRow("Best Combo", "${attempt.bestComboStreak}x streak")
                }
                if (attempt.leveledUp) {
                    SummaryRow("Promoted", "→ Officer Training Level ${attempt.levelAfter} 🎉")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Question Card
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
private fun ReviewQuestionCard(answer: QuizAttemptAnswerEntity, quizId: String) {
    var expanded by remember { mutableStateOf(false) }

    // Resolve display text — use snapshotted fields if populated; fall back to QuizData for
    // pre-migration records where the new columns have empty defaults.
    val quiz = remember(quizId) { allQuizzes.find { it.id == quizId } }
    val liveQuestion = remember(quizId, answer.questionId) {
        quiz?.questions?.find { it.id == answer.questionId }
    }
    val choiceLabels = listOf("A", "B", "C", "D", "E", "F")

    val userAnswerText = if (answer.selectedOptionIndex == -1) {
        "Unanswered (time ran out)"
    } else if (answer.selectedAnswerText.isNotEmpty()) {
        "${choiceLabels.getOrElse(answer.selectedOptionIndex) { "?" }}. ${answer.selectedAnswerText}"
    } else {
        val text = liveQuestion?.options?.getOrNull(answer.selectedOptionIndex) ?: ""
        "${choiceLabels.getOrElse(answer.selectedOptionIndex) { "?" }}. $text"
    }

    val correctAnswerText = if (answer.correctAnswerText.isNotEmpty()) {
        "${choiceLabels.getOrElse(answer.correctOptionIndex) { "?" }}. ${answer.correctAnswerText}"
    } else {
        val text = liveQuestion?.options?.getOrNull(answer.correctOptionIndex) ?: ""
        "${choiceLabels.getOrElse(answer.correctOptionIndex) { "?" }}. $text"
    }

    val topic = runCatching { RoadSafetyTopic.valueOf(answer.topic) }
        .getOrDefault(RoadSafetyTopic.ROAD_COURTESY)
    val explanation = answer.explanation.ifEmpty { topic.explanation }
    val safetyTip = answer.safetyTip.ifEmpty { topic.safetyTip }
    val difficulty = answer.difficulty.ifEmpty { "—" }
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
    val statusLabel = when {
        answer.isCorrect -> "✅ Correct"
        answer.selectedOptionIndex == -1 -> "⚪ Unanswered"
        else -> "❌ Incorrect"
    }

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ── Question header with status badge ───────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = statusLabel,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (questionNum > 0) "Question $questionNum" else "Question",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DifficultyBadge(difficulty = difficulty)
                    if (answer.xpEarned > 0 || answer.pointsEarned > 0) {
                        XpBadge(xp = answer.xpEarned)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Question text ───────────────────────────────────────────────────
            Text(
                text = answer.questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Answers ─────────────────────────────────────────────────────────
            Text(
                text = "Your Answer: $userAnswerText",
                style = MaterialTheme.typography.bodyMedium,
                color = if (answer.isCorrect) EmeraldGreen else TrafficRed
            )
            if (!answer.isCorrect) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Correct Answer: $correctAnswerText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EmeraldGreen
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = statusLabel,
                style = MaterialTheme.typography.labelSmall,
                color = statusColor,
                fontWeight = FontWeight.Bold
            )

            // ── Expandable explanation ──────────────────────────────────────────
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "💡 Safety Tip: $safetyTip",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────────────────────────
// Shared Badges
// ─────────────────────────────────────────────────────────────────────────────────────────────────

@Composable
fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty.uppercase()) {
        "EASY" -> EmeraldGreen
        "MEDIUM" -> AmberYellow
        "HARD" -> TrafficRed
        else -> NeutralGray
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = difficulty.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun XpBadge(xp: Int) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "+$xp XP",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return if (min > 0) "${min}m ${sec}s" else "${sec}s"
}
