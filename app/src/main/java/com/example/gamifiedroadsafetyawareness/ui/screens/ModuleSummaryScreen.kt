package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.XpAwardResult
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedProgressRing
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedXpCounter
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppOutlinedButton
import com.example.gamifiedroadsafetyawareness.ui.components.CelebrationBurst
import com.example.gamifiedroadsafetyawareness.ui.components.LevelUpModal
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

/** Excellent/Very Good/Good/Needs Improvement/Failed — independent of the pass/fail line. */
private fun performanceRating(scorePercent: Int): Pair<String, Color> = when {
    scorePercent >= 90 -> "Excellent" to BadgeGold
    scorePercent >= 75 -> "Very Good" to EmeraldGreen
    scorePercent >= 60 -> "Good" to AmberYellow
    scorePercent >= 40 -> "Needs Improvement" to AmberYellow
    else -> "Failed" to TrafficRed
}

private fun formatDuration(seconds: Int): String {
    if (seconds < 60) return "${seconds}s"
    val minutes = seconds / 60
    val remainder = seconds % 60
    return if (remainder == 0) "${minutes}m" else "${minutes}m ${remainder}s"
}

private fun difficultyLabel(difficulty: String): String = when (difficulty) {
    "EASY" -> "Easy"
    "MEDIUM" -> "Medium"
    "HARD" -> "Hard"
    else -> difficulty
}

/**
 * Module Summary — shown both right after finishing a quiz ([awardResult] present, so the
 * celebratory reward breakdown/level-up modal render) and when reopening a past attempt from
 * Learning History ([awardResult] null — just the factual breakdown, no confetti).
 */
@Composable
fun ModuleSummaryScreen(
    attempt: QuizAttemptEntity,
    awardResult: XpAwardResult?,
    highestScorePercentEver: Int,
    onReviewAnswers: () -> Unit,
    onViewLearningHistory: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val celebrate = remember(attempt.id) { awardResult != null && attempt.scorePercent >= 75 }
    var showLevelUpModal by remember(attempt.id) { mutableStateOf(awardResult?.leveledUp == true) }
    val (ratingLabel, ratingColor) = performanceRating(attempt.scorePercent)
    val accuracyPercent = if (attempt.correctCount + attempt.incorrectCount > 0) {
        (attempt.correctCount * 100) / (attempt.correctCount + attempt.incorrectCount)
    } else 0

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onDone, modifier = Modifier.align(Alignment.Start)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            AnimatedProgressRing(
                progress = attempt.scorePercent / 100f,
                size = 120.dp,
                strokeWidth = 8.dp,
                ringColor = if (attempt.scorePercent >= 50) EmeraldGreen else TrafficRed
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedXpCounter(
                        targetValue = attempt.correctCount,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "/ ${attempt.totalQuestions}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (awardResult != null) "Module Complete! 🎉" else attempt.quizTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Score: ${attempt.scorePercent}% — $ratingLabel",
                style = MaterialTheme.typography.titleMedium,
                color = ratingColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusPill(
                    text = if (attempt.passed) "✅ Passed" else "❌ Failed",
                    color = if (attempt.passed) EmeraldGreen else TrafficRed
                )
                StatusPill(text = difficultyLabel(attempt.difficulty), color = MaterialTheme.colorScheme.primary)
                StatusPill(text = "Attempt #${attempt.attemptNumber}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (awardResult?.leveledUp == true) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "🎉 Promoted! Officer Training Level ${awardResult.newLevel} — ${awardResult.newLevelName}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (awardResult?.streakProtected == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🧊 Streak protected!",
                    style = MaterialTheme.typography.labelLarge,
                    color = AmberYellow,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "PERFORMANCE",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    StatRow("Correct", "${attempt.correctCount}", EmeraldGreen)
                    StatRow("Incorrect", "${attempt.incorrectCount}", TrafficRed)
                    StatRow("Unanswered", "${attempt.unansweredCount}", AmberYellow)
                    StatRow("Accuracy", "$accuracyPercent%", MaterialTheme.colorScheme.onSurface)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    StatRow("Time spent", formatDuration(attempt.timeSpentSeconds), MaterialTheme.colorScheme.onSurface)
                    StatRow("Highest score", "$highestScorePercentEver%", MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "TRAINING REWARDS",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (awardResult != null) {
                        RewardLine("Base XP", awardResult.baseXp)
                        if (awardResult.streakBonusXp > 0) RewardLine("Streak bonus", awardResult.streakBonusXp)
                        if (awardResult.perfectBonusXp > 0) RewardLine("Perfect bonus", awardResult.perfectBonusXp)
                        if (awardResult.completionBonusXp > 0) RewardLine("Quiz completion", awardResult.completionBonusXp)
                        if (awardResult.firstAttemptBonusXp > 0) RewardLine("First-attempt perfect", awardResult.firstAttemptBonusXp)
                        if (awardResult.dailyStreakBonusXp > 0) RewardLine("Daily streak bonus", awardResult.dailyStreakBonusXp)
                        if (awardResult.timeChallengeBonusXp > 0) RewardLine("Time challenge", awardResult.timeChallengeBonusXp)
                        if (awardResult.modulePerfectBonusXp > 0) RewardLine("Perfect module", awardResult.modulePerfectBonusXp)
                        if (awardResult.achievementBonusXp > 0) {
                            RewardLine("Achievements unlocked", awardResult.achievementBonusXp)
                            awardResult.achievementsUnlocked.forEach { achievement ->
                                Text(
                                    text = "  ${achievement.icon} ${achievement.title}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Training XP Earned",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+${attempt.xpEarned} XP",
                            style = MaterialTheme.typography.titleMedium,
                            color = BadgeGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (attempt.leveledUp) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Officer Training Level → ${attempt.levelAfter}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(text = "Review Answers", onClick = onReviewAnswers)
            Spacer(modifier = Modifier.height(12.dp))
            AppOutlinedButton(text = "View Learning History", onClick = onViewLearningHistory)
            Spacer(modifier = Modifier.height(12.dp))
            AppButton(text = "Continue Learning", onClick = onDone)
            Spacer(modifier = Modifier.height(24.dp))
        }

        CelebrationBurst(trigger = celebrate, modifier = Modifier.fillMaxSize())
    }

    if (showLevelUpModal && awardResult != null) {
        LevelUpModal(
            newLevel = awardResult.newLevel,
            levelName = awardResult.newLevelName,
            onDismiss = { showLevelUpModal = false }
        )
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RewardLine(label: String, xp: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "+$xp",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}
