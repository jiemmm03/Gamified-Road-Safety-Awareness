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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.QuizData
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuestionErrorRateRow
import com.example.gamifiedroadsafetyawareness.model.db.QuizAggregateRow
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

private data class QuestionAnalyticsRow(
    val questionId: Int,
    val questionText: String,
    val errorRatePercent: Int,
    val totalAnswers: Int,
    val mostFrequentWrongAnswer: String?
)

/** Per-module analytics: aggregate stats plus which questions students get wrong most often. */
@Composable
fun AdminModuleAnalyticsScreen(
    quizId: String,
    xpManager: XpManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quiz = remember(quizId) {
        listOf(QuizData.quiz_easy, QuizData.quiz_medium, QuizData.quiz_hard).find { it.id == quizId }
    }
    var aggregate by remember(quizId) { mutableStateOf<QuizAggregateRow?>(null) }
    var questionRows by remember(quizId) { mutableStateOf<List<QuestionAnalyticsRow>>(emptyList()) }

    LaunchedEffect(quizId) {
        aggregate = xpManager.getPerQuizAggregates().find { it.quizId == quizId }
        val errorRates: List<QuestionErrorRateRow> = xpManager.getQuestionErrorRates(quizId)
        questionRows = errorRates.map { row ->
            val question = quiz?.questions?.find { it.id == row.questionId }
            val wrongOption = xpManager.getMostFrequentWrongOption(quizId, row.questionId)
            val wrongText = wrongOption?.let { w ->
                question?.options?.getOrNull(w.selectedOptionIndex)?.let { text -> "$text (${w.pickCount}x)" }
            }
            QuestionAnalyticsRow(
                questionId = row.questionId,
                questionText = question?.question ?: "Question ${row.questionId}",
                errorRatePercent = (row.errorRate * 100).toInt(),
                totalAnswers = row.totalAnswers,
                mostFrequentWrongAnswer = wrongText
            )
        }
    }

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
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = quiz?.title ?: quizId,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Module analytics",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        aggregate?.let { agg ->
            item {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        StatLine("Total attempts", "${agg.attemptCount}")
                        StatLine("Passed", "${agg.passedCount}")
                        StatLine("Failed", "${agg.attemptCount - agg.passedCount}")
                        StatLine("Average score", "${agg.avgScore.toInt()}%")
                        StatLine("Average time", "${agg.avgTimeSeconds.toInt()}s")
                        StatLine("Average XP earned", "${agg.avgXp.toInt()}")
                    }
                }
            }
        }

        item {
            Text(
                text = "Questions With the Highest Error Rate",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        if (questionRows.isEmpty()) {
            item {
                Text(
                    text = "No answers recorded yet for this module.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(questionRows) { row ->
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = row.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    StatLine(
                        "Error rate",
                        "${row.errorRatePercent}% (${row.totalAnswers} answers)",
                        valueColor = if (row.errorRatePercent >= 50) TrafficRed else AmberYellow
                    )
                    row.mostFrequentWrongAnswer?.let {
                        StatLine("Most common wrong answer", it)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun StatLine(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
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
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}
