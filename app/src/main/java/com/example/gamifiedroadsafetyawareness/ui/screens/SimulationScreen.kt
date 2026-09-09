package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.*
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppOutlinedButton
import com.example.gamifiedroadsafetyawareness.ui.components.ConfirmActionDialog
import com.example.gamifiedroadsafetyawareness.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SimulationScreen(
    username: String = "",
    xpManager: XpManager? = null,
    onBackClick: () -> Unit,
    onSubmitDecision: ((DecisionOption) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scenarios = remember { ChatScenarios.all }
    val totalScenarios = scenarios.size

    var currentScenarioIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<DecisionOption?>(null) }
    var isDecisionSubmitted by remember { mutableStateOf(false) }
    var showExitConfirm by remember { mutableStateOf(false) }
    var isSimulationFinished by remember { mutableStateOf(false) }

    // Gamification state across all 20 scenarios
    var correctCount by remember { mutableIntStateOf(0) }
    var totalXpEarned by remember { mutableIntStateOf(0) }
    val incorrectScenarios = remember { mutableStateListOf<SimulationScenario>() }
    val simulationAnswerRecords = remember { mutableStateListOf<QuestionAnswerRecord>() }
    val simulationStartedAt = remember { System.currentTimeMillis() }
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val currentScenario = scenarios.getOrElse(currentScenarioIndex) { scenarios.first() }

    fun requestExit() {
        if (!isSimulationFinished && (currentScenarioIndex > 0 || isDecisionSubmitted || selectedOption != null)) {
            showExitConfirm = true
        } else {
            onBackClick()
        }
    }

    BackHandler { requestExit() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isSimulationFinished) {
            // SIMULATION COMPLETE SCREEN
            SimulationCompleteView(
                score = correctCount,
                total = totalScenarios,
                totalXp = totalXpEarned,
                incorrectScenarios = incorrectScenarios,
                onRetake = {
                    currentScenarioIndex = 0
                    selectedOption = null
                    isDecisionSubmitted = false
                    isSimulationFinished = false
                    correctCount = 0
                    totalXpEarned = 0
                    incorrectScenarios.clear()
                    simulationAnswerRecords.clear()
                },
                onReturnHome = onBackClick
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header Bar: Navigation, Progress, Score, XP & Difficulty
                SimulationHeader(
                    currentIndex = currentScenarioIndex + 1,
                    total = totalScenarios,
                    score = correctCount,
                    totalXp = totalXpEarned,
                    difficulty = currentScenario.difficulty,
                    onExit = { requestExit() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = { (currentScenarioIndex + 1).toFloat() / totalScenarios.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = when (currentScenario.difficulty) {
                        SimulationDifficulty.EASY -> EmeraldGreen
                        SimulationDifficulty.MEDIUM -> AmberYellow
                        SimulationDifficulty.HARD -> TrafficRed
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Scenario Title & Environmental Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Scenario ${String.format("%02d", currentScenario.scenarioNumber)} of $totalScenarios",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentScenario.title.substringAfter("— ").ifBlank { currentScenario.title },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    DifficultyPill(difficulty = currentScenario.difficulty)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. LARGE VISUAL SIMULATION IMAGE (Realistic Educational Driver POV Scene)
                VisualSimulationScene(
                    scenarioNumber = currentScenario.scenarioNumber,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. SITUATION CARD (Short description of what the driver sees)
                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Visibility,
                                contentDescription = "Situation",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SITUATION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentScenario.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. DECISION QUESTION
                Text(
                    text = currentScenario.prompt.ifBlank { "What would you do?" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 5. 4 CHOICES (A, B, C, D)
                currentScenario.options.forEachIndexed { index, option ->
                    val optionLetter = when (index) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        else -> "D"
                    }
                    val isSelected = selectedOption?.id == option.id

                    DecisionChoiceCard(
                        letter = optionLetter,
                        option = option,
                        isSelected = isSelected,
                        isSubmitted = isDecisionSubmitted,
                        onClick = {
                            if (!isDecisionSubmitted) {
                                selectedOption = option
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. ACTION BUTTON / AI FEEDBACK CARD
                if (!isDecisionSubmitted) {
                    AppButton(
                        text = "Submit Decision",
                        onClick = {
                            val option = selectedOption ?: return@AppButton
                            isDecisionSubmitted = true
                            val correctIndex = currentScenario.options.indexOfFirst { it.isCorrect }
                            val selectedIndex = currentScenario.options.indexOfFirst { it.id == option.id }
                            val correctOpt = currentScenario.options.getOrNull(correctIndex)

                            simulationAnswerRecords.add(
                                QuestionAnswerRecord(
                                    questionId = currentScenario.scenarioNumber,
                                    questionNumber = currentScenario.scenarioNumber,
                                    questionText = "${currentScenario.title}: ${currentScenario.description}",
                                    selectedOptionIndex = selectedIndex,
                                    correctOptionIndex = correctIndex,
                                    selectedAnswerText = option.description,
                                    correctAnswerText = correctOpt?.description ?: "",
                                    isCorrect = option.isCorrect,
                                    pointsEarned = if (option.isCorrect) 100 else 0,
                                    xpEarned = if (option.isCorrect) currentScenario.xpReward else 0,
                                    explanation = "${option.explanation}${if (currentScenario.safetyPrinciple.isNotBlank()) " | Safety Principle: ${currentScenario.safetyPrinciple}" else ""}",
                                    safetyTip = currentScenario.recommendedAction,
                                    difficulty = currentScenario.difficulty.label,
                                    topic = currentScenario.topicTag
                                )
                            )

                            if (option.isCorrect) {
                                correctCount++
                                totalXpEarned += currentScenario.xpReward
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            } else {
                                incorrectScenarios.add(currentScenario)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                            onSubmitDecision?.invoke(option)
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // 7. AI FEEDBACK CARD (Post-Submission)
                    val chosen = selectedOption
                    if (chosen != null) {
                        AiFeedbackSection(
                            isCorrect = chosen.isCorrect,
                            scenario = currentScenario,
                            chosenOption = chosen,
                            onNext = {
                                if (currentScenarioIndex < totalScenarios - 1) {
                                    currentScenarioIndex++
                                    selectedOption = null
                                    isDecisionSubmitted = false
                                } else {
                                    // Finished all 20 scenarios!
                                    isSimulationFinished = true
                                    // Persist attempt record for Admin & Super Admin Monitoring
                                    if (username.isNotBlank() && xpManager != null) {
                                        coroutineScope.launch {
                                            val awardResult = xpManager.awardQuizCompletion(
                                                username = username,
                                                correctAnswers = correctCount,
                                                totalQuestions = totalScenarios,
                                                comboXpEarned = totalXpEarned,
                                                bestComboStreak = correctCount,
                                                timeChallengeCompleted = true,
                                                quizId = "simulation_20_scenarios",
                                                quizTitle = "20-Scenario Visual Driving Simulation"
                                            )
                                            xpManager.recordQuizAttempt(
                                                username = username,
                                                quizId = "simulation_20_scenarios",
                                                moduleId = "mod_simulation_20",
                                                quizTitle = "20-Scenario Visual Driving Simulation",
                                                difficulty = ModuleType.HARD,
                                                answers = simulationAnswerRecords.toList(),
                                                timeSpentSeconds = ((System.currentTimeMillis() - simulationStartedAt) / 1000).toInt(),
                                                startedAt = simulationStartedAt,
                                                bestComboStreak = correctCount,
                                                timeChallengeCompleted = true,
                                                awardResult = awardResult
                                            )
                                        }
                                    }
                                }
                            },
                            isLastScenario = currentScenarioIndex == totalScenarios - 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showExitConfirm) {
        ConfirmActionDialog(
            title = "Leave Simulation?",
            message = "You are on Scenario ${currentScenarioIndex + 1} of $totalScenarios. Exiting now will discard your current progress in this session.",
            confirmLabel = "Leave",
            destructive = true,
            onConfirm = {
                showExitConfirm = false
                onBackClick()
            },
            onDismiss = { showExitConfirm = false }
        )
    }
}

@Composable
private fun SimulationHeader(
    currentIndex: Int,
    total: Int,
    score: Int,
    totalXp: Int,
    difficulty: SimulationDifficulty,
    onExit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onExit,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score Tag
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Score",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$score / ${currentIndex - 1}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // XP Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BadgeGold.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EmojiEvents,
                        contentDescription = "XP",
                        tint = BadgeGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+$totalXp XP",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BadgeGold
                    )
                }
            }
        }
    }
}

@Composable
private fun DifficultyPill(difficulty: SimulationDifficulty) {
    val (color, label) = when (difficulty) {
        SimulationDifficulty.EASY -> EmeraldGreen to "Easy"
        SimulationDifficulty.MEDIUM -> AmberYellow to "Medium"
        SimulationDifficulty.HARD -> TrafficRed to "Hard"
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun DecisionChoiceCard(
    letter: String,
    option: DecisionOption,
    isSelected: Boolean,
    isSubmitted: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSubmitted && option.isCorrect -> EmeraldGreen
        isSubmitted && isSelected && !option.isCorrect -> TrafficRed
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    }

    val backgroundColor = when {
        isSubmitted && option.isCorrect -> EmeraldGreen.copy(alpha = 0.12f)
        isSubmitted && isSelected && !option.isCorrect -> TrafficRed.copy(alpha = 0.12f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surface
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        border = BorderStroke(if (isSelected || isSubmitted) 2.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Letter Indicator (A, B, C, D)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isSubmitted && option.isCorrect -> EmeraldGreen
                            isSubmitted && isSelected && !option.isCorrect -> TrafficRed
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected || (isSubmitted && option.isCorrect)) PureWhite else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun AiFeedbackSection(
    isCorrect: Boolean,
    scenario: SimulationScenario,
    chosenOption: DecisionOption,
    onNext: () -> Unit,
    isLastScenario: Boolean
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (isCorrect) EmeraldGreen.copy(alpha = 0.08f) else TrafficRed.copy(alpha = 0.08f),
        borderColor = if (isCorrect) EmeraldGreen.copy(alpha = 0.6f) else TrafficRed.copy(alpha = 0.6f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header status
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Warning,
                    contentDescription = "Result",
                    tint = if (isCorrect) EmeraldGreen else TrafficRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCorrect) "✓ Correct Decision" else "⚠ Review Your Decision",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isCorrect) EmeraldGreen else TrafficRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Why?
            Text(
                text = "Why?",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = chosenOption.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hazard Identified
            if (scenario.hazardIdentified.isNotBlank()) {
                Text(
                    text = "Hazard Identified:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = scenario.hazardIdentified,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Safety Principle
            if (scenario.safetyPrinciple.isNotBlank()) {
                Text(
                    text = "Safety Principle:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = scenario.safetyPrinciple,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Recommended Action
            if (scenario.recommendedAction.isNotBlank()) {
                Text(
                    text = "Recommended Action:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = scenario.recommendedAction,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Next button
            AppButton(
                text = if (isLastScenario) "View Simulation Results 🎉" else "Next Scenario →",
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SimulationCompleteView(
    score: Int,
    total: Int,
    totalXp: Int,
    incorrectScenarios: List<SimulationScenario>,
    onRetake: () -> Unit,
    onReturnHome: () -> Unit
) {
    val accuracyPercent = if (total > 0) ((score.toFloat() / total.toFloat()) * 100).toInt() else 0

    val (performanceLevel, performanceColor, performanceIcon) = when {
        accuracyPercent >= 90 -> Triple("Excellent", EmeraldGreen, "🏆")
        accuracyPercent >= 75 -> Triple("Good", BadgeGold, "🌟")
        accuracyPercent >= 50 -> Triple("Needs Improvement", AmberYellow, "⚠️")
        else -> Triple("Needs More Practice", TrafficRed, "🔄")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Badge Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(performanceColor.copy(alpha = 0.15f))
                .border(2.dp, performanceColor.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = performanceIcon, fontSize = 38.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "SIMULATION COMPLETE",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "AI Road Safety & Driver Decision Assessment",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Summary Score Card
        AppCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$score / $total",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = performanceColor
                )
                Text(
                    text = "$accuracyPercent% Accuracy · Level: $performanceLevel",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = performanceColor
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem(label = "Correct", value = "$score", color = EmeraldGreen)
                    MetricItem(label = "Incorrect", value = "${total - score}", color = TrafficRed)
                    MetricItem(label = "XP Earned", value = "+$totalXp", color = BadgeGold)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // AI Recommendations based on failed scenarios
        AppCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "AI Coach",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI COACH RECOMMENDATIONS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (incorrectScenarios.isEmpty()) {
                    Text(
                        text = "Outstanding performance! You demonstrated mastery of Philippine road safety laws, hazard anticipation, and defensive decision-making in all 20 scenarios.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    val topics = incorrectScenarios.map { it.topicTag }.filter { it.isNotBlank() }.distinct()
                    Text(
                        text = "Based on your decision patterns, you may need additional review in the following areas:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    topics.forEach { topic ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(text = "• ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(text = topic, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Retake Simulation",
            onClick = onRetake,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppOutlinedButton(
            text = "Return to Dashboard",
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * VisualSimulationScene draws realistic, educational driver-perspective road scene illustrations
 * for each of the 20 Philippine road scenarios using custom Jetpack Compose Canvas graphics.
 */
@Composable
fun VisualSimulationScene(
    scenarioNumber: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        when (scenarioNumber) {
            1 -> drawPedestrianCrossingScene(width, height)
            2 -> drawChangingTrafficLightScene(width, height)
            3 -> drawMotorcycleBlindSpotScene(width, height)
            4 -> drawSuddenBrakingScene(width, height)
            5 -> drawHeavyRainScene(width, height)
            6 -> drawRoadObstructionScene(width, height)
            7 -> drawEmergencyVehicleScene(width, height)
            8 -> drawUnsafeOvertakingScene(width, height)
            9 -> drawSchoolZoneScene(width, height)
            10 -> drawMotorcycleTrafficScene(width, height)
            11 -> drawIntersectionConflictScene(width, height)
            12 -> drawNightDrivingScene(width, height)
            13 -> drawDistractedDrivingScene(width, height)
            14 -> drawFatiguedDrivingScene(width, height)
            15 -> drawTrafficSignScene(width, height)
            16 -> drawSlipperyRoadScene(width, height)
            17 -> drawAggressiveDriverScene(width, height)
            18 -> drawSuddenPedestrianHazardScene(width, height)
            19 -> drawVehicleTireProblemScene(width, height)
            else -> drawComplexMultiHazardScene(width, height)
        }
    }
}

// -------------------------------------------------------------
// SCENE DRAWING FUNCTIONS (Realistic Educational Driver POV)
// -------------------------------------------------------------

private fun DrawScope.drawCommonRoadBase(
    width: Float,
    height: Float,
    skyColor: Color = Color(0xFF87CEEB),
    groundColor: Color = Color(0xFF4A7043),
    roadColor: Color = Color(0xFF2C3138)
) {
    val horizonY = height * 0.42f

    // Sky
    drawRect(color = skyColor, size = Size(width, horizonY))

    // Ground / Grass
    drawRect(
        color = groundColor,
        topLeft = Offset(0f, horizonY),
        size = Size(width, height - horizonY)
    )

    // Road (Vanishing Perspective)
    val roadPath = Path().apply {
        moveTo(width * 0.42f, horizonY)
        lineTo(width * 0.58f, horizonY)
        lineTo(width * 0.95f, height)
        lineTo(width * 0.05f, height)
        close()
    }
    drawPath(roadPath, color = roadColor)

    // Center broken line
    val strokeWidth = 3.5f
    val steps = 5
    for (i in 0 until steps) {
        val t1 = (i.toFloat() + 0.2f) / steps.toFloat()
        val t2 = (i.toFloat() + 0.7f) / steps.toFloat()
        val y1 = horizonY + (height - horizonY) * t1
        val y2 = horizonY + (height - horizonY) * t2
        val x1 = width * 0.5f
        val x2 = width * 0.5f
        drawLine(
            color = Color(0xFFF1C40F),
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = strokeWidth * (1f + t1 * 2f)
        )
    }
}

// 01 — Pedestrian Crossing
private fun DrawScope.drawPedestrianCrossingScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)
    val horizonY = height * 0.42f

    // Zebra crosswalk stripes
    val stripeY = height * 0.68f
    val stripeHeight = height * 0.12f
    for (i in 0..6) {
        val startX = width * (0.20f + i * 0.09f)
        drawRect(
            color = PureWhite,
            topLeft = Offset(startX, stripeY),
            size = Size(width * 0.055f, stripeHeight)
        )
    }

    // Pedestrian silhouette on right curb
    val pedX = width * 0.78f
    val pedY = height * 0.62f
    drawCircle(color = Color(0xFF1E272E), radius = 9f, center = Offset(pedX, pedY - 26f))
    drawRoundRect(
        color = Color(0xFF1E272E),
        topLeft = Offset(pedX - 7f, pedY - 16f),
        size = Size(14f, 32f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )

    // Warning Crosswalk Sign on pole
    drawRect(color = Color(0xFF718093), topLeft = Offset(width * 0.88f, horizonY + 10f), size = Size(4f, 60f))
    val signDiamond = Path().apply {
        moveTo(width * 0.885f, horizonY)
        lineTo(width * 0.93f, horizonY + 18f)
        lineTo(width * 0.885f, horizonY + 36f)
        lineTo(width * 0.84f, horizonY + 18f)
        close()
    }
    drawPath(signDiamond, color = Color(0xFFF1C40F))
}

// 02 — Changing Traffic Light
private fun DrawScope.drawChangingTrafficLightScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)
    val horizonY = height * 0.42f

    // Gantry pole and frame
    drawRect(color = Color(0xFF2D3436), topLeft = Offset(width * 0.65f, horizonY - 45f), size = Size(6f, 75f))
    drawRect(color = Color(0xFF2D3436), topLeft = Offset(width * 0.40f, horizonY - 50f), size = Size(width * 0.28f, 6f))

    // Traffic light housing
    val boxX = width * 0.46f
    val boxY = horizonY - 70f
    drawRoundRect(
        color = Color(0xFF1E272E),
        topLeft = Offset(boxX, boxY),
        size = Size(36f, 72f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    // Red (off), Yellow (GLOWING AMBER), Green (off)
    drawCircle(color = Color(0xFF4A1010), radius = 7.5f, center = Offset(boxX + 18f, boxY + 16f))
    // Glowing Amber
    drawCircle(color = Color(0xFFFFB300), radius = 10f, center = Offset(boxX + 18f, boxY + 36f))
    drawCircle(color = Color(0xFFFFE082), radius = 6f, center = Offset(boxX + 18f, boxY + 36f))
    drawCircle(color = Color(0xFF0B3B18), radius = 7.5f, center = Offset(boxX + 18f, boxY + 56f))

    // White intersection stop bar line
    drawRect(color = PureWhite, topLeft = Offset(width * 0.22f, height * 0.74f), size = Size(width * 0.56f, 10f))
}

// 03 — Motorcycle Blind Spot
private fun DrawScope.drawMotorcycleBlindSpotScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Side Mirror Frame on Left
    val mirrorX = width * 0.08f
    val mirrorY = height * 0.35f
    drawRoundRect(
        color = Color(0xFF1E272E),
        topLeft = Offset(mirrorX - 8f, mirrorY - 8f),
        size = Size(width * 0.32f + 16f, height * 0.48f + 16f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
    )
    // Mirror glass
    drawRoundRect(
        color = Color(0xFF74B9FF),
        topLeft = Offset(mirrorX, mirrorY),
        size = Size(width * 0.32f, height * 0.48f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )

    // Motorcycle reflected in mirror
    val mcX = mirrorX + width * 0.16f
    val mcY = mirrorY + height * 0.26f
    // Rider & bike silhouette
    drawCircle(color = Color(0xFF2C3A47), radius = 14f, center = Offset(mcX, mcY - 24f))
    drawRoundRect(color = Color(0xFFD63031), topLeft = Offset(mcX - 16f, mcY - 10f), size = Size(32f, 30f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    // Bright round headlight
    drawCircle(color = Color(0xFFFFF9C4), radius = 10f, center = Offset(mcX, mcY + 12f))

    // Turn indicator blinking on car dashboard (Bottom left)
    drawRoundRect(
        color = Color(0xFF00B894),
        topLeft = Offset(width * 0.45f, height * 0.82f),
        size = Size(30f, 18f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
}

// 04 — Sudden Braking
private fun DrawScope.drawSuddenBrakingScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Leading vehicle ahead
    val carWidth = width * 0.38f
    val carHeight = height * 0.30f
    val carX = (width - carWidth) / 2f
    val carY = height * 0.45f

    // Car Body
    drawRoundRect(
        color = Color(0xFF2C3E50),
        topLeft = Offset(carX, carY),
        size = Size(carWidth, carHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )
    // Rear windshield
    drawRoundRect(
        color = Color(0xFF1E272E),
        topLeft = Offset(carX + carWidth * 0.12f, carY + carHeight * 0.12f),
        size = Size(carWidth * 0.76f, carHeight * 0.38f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )

    // Glowing Red Brake Lights
    val lightWidth = carWidth * 0.20f
    val lightHeight = carHeight * 0.18f
    val lightY = carY + carHeight * 0.58f
    // Left brake light + halo
    drawRoundRect(color = Color(0xFFFF1744), topLeft = Offset(carX + 8f, lightY), size = Size(lightWidth, lightHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    drawCircle(color = Color(0x66FF1744), radius = 22f, center = Offset(carX + 8f + lightWidth / 2f, lightY + lightHeight / 2f))
    // Right brake light + halo
    drawRoundRect(color = Color(0xFFFF1744), topLeft = Offset(carX + carWidth - lightWidth - 8f, lightY), size = Size(lightWidth, lightHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    drawCircle(color = Color(0x66FF1744), radius = 22f, center = Offset(carX + carWidth - lightWidth / 2f - 8f, lightY + lightHeight / 2f))
}

// 05 — Heavy Rain
private fun DrawScope.drawHeavyRainScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height, skyColor = Color(0xFF4A5568), groundColor = Color(0xFF2D3748), roadColor = Color(0xFF1A202C))

    // Water puddle sheen
    drawOval(color = Color(0x44CBD5E0), topLeft = Offset(width * 0.25f, height * 0.65f), size = Size(width * 0.50f, 25f))

    // Rain streaks across windshield
    val rainColor = Color(0x77E2E8F0)
    for (i in 0..40) {
        val rx = (i * 29) % width.toInt()
        val ry = (i * 37) % height.toInt()
        drawLine(
            color = rainColor,
            start = Offset(rx.toFloat(), ry.toFloat()),
            end = Offset(rx.toFloat() - 12f, ry.toFloat() + 24f),
            strokeWidth = 2f
        )
    }

    // Windshield wiper sweep arc
    drawArc(
        color = Color(0x33FFFFFF),
        startAngle = 180f,
        sweepAngle = 120f,
        useCenter = true,
        topLeft = Offset(width * 0.05f, height * 0.25f),
        size = Size(width * 0.90f, height * 0.90f)
    )
}

// 06 — Road Obstruction
private fun DrawScope.drawRoadObstructionScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Stalled delivery truck in right lane
    val truckX = width * 0.54f
    val truckY = height * 0.44f
    val truckW = width * 0.30f
    val truckH = height * 0.32f
    drawRoundRect(color = Color(0xFFE67E22), topLeft = Offset(truckX, truckY), size = Size(truckW, truckH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))

    // Orange Traffic Cones
    val coneX = width * 0.50f
    val coneY = height * 0.72f
    val conePath = Path().apply {
        moveTo(coneX, coneY)
        lineTo(coneX + 16f, coneY + 36f)
        lineTo(coneX - 16f, coneY + 36f)
        close()
    }
    drawPath(conePath, color = Color(0xFFFF5722))

    // Oncoming car in left lane
    val onX = width * 0.24f
    val onY = height * 0.46f
    drawRoundRect(color = Color(0xFFBDC3C7), topLeft = Offset(onX, onY), size = Size(width * 0.18f, height * 0.16f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
}

// 07 — Emergency Vehicle
private fun DrawScope.drawEmergencyVehicleScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Rearview Mirror at Top Center
    val mirW = width * 0.58f
    val mirH = height * 0.38f
    val mirX = (width - mirW) / 2f
    val mirY = 16f

    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(mirX - 6f, mirY - 6f), size = Size(mirW + 12f, mirH + 12f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    drawRoundRect(color = Color(0xFF74B9FF), topLeft = Offset(mirX, mirY), size = Size(mirW, mirH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))

    // Ambulance inside mirror
    val ambX = mirX + mirW * 0.30f
    val ambY = mirY + mirH * 0.28f
    val ambW = mirW * 0.40f
    val ambH = mirH * 0.55f
    drawRoundRect(color = PureWhite, topLeft = Offset(ambX, ambY), size = Size(ambW, ambH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))

    // Red & Blue Flashing Emergency Strobes
    drawCircle(color = Color(0xFFFF1744), radius = 12f, center = Offset(ambX + ambW * 0.25f, ambY - 6f))
    drawCircle(color = Color(0xFF2979FF), radius = 12f, center = Offset(ambX + ambW * 0.75f, ambY - 6f))
}

// 08 — Unsafe Overtaking
private fun DrawScope.drawUnsafeOvertakingScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height, groundColor = Color(0xFF2E7D32))
    val horizonY = height * 0.42f

    // Solid Double Yellow Lines
    drawLine(color = Color(0xFFF1C40F), start = Offset(width * 0.49f, horizonY), end = Offset(width * 0.48f, height), strokeWidth = 5f)
    drawLine(color = Color(0xFFF1C40F), start = Offset(width * 0.51f, horizonY), end = Offset(width * 0.52f, height), strokeWidth = 5f)

    // Slow agricultural tricycle ahead on right
    val triX = width * 0.58f
    val triY = height * 0.54f
    drawRoundRect(color = Color(0xFF27AE60), topLeft = Offset(triX, triY), size = Size(width * 0.24f, height * 0.22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))
    drawCircle(color = Color(0xFF1E272E), radius = 14f, center = Offset(triX + width * 0.20f, triY + height * 0.22f))

    // Winding Curve Warning Sign
    drawRect(color = Color(0xFF718093), topLeft = Offset(width * 0.88f, horizonY), size = Size(4f, 50f))
    val diamond = Path().apply {
        moveTo(width * 0.885f, horizonY - 30f)
        lineTo(width * 0.93f, horizonY - 12f)
        lineTo(width * 0.885f, horizonY + 6f)
        lineTo(width * 0.84f, horizonY - 12f)
        close()
    }
    drawPath(diamond, color = Color(0xFFF1C40F))
}

// 09 — School Zone
private fun DrawScope.drawSchoolZoneScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)
    val horizonY = height * 0.42f

    // Yellow School Zone Warning Sign
    val signX = width * 0.82f
    val signY = horizonY - 20f
    drawRect(color = Color(0xFF718093), topLeft = Offset(signX + 16f, signY), size = Size(4f, 65f))
    val pentagon = Path().apply {
        moveTo(signX + 18f, signY - 35f)
        lineTo(signX + 38f, signY - 15f)
        lineTo(signX + 38f, signY + 15f)
        lineTo(signX - 2f, signY + 15f)
        lineTo(signX - 2f, signY - 15f)
        close()
    }
    drawPath(pentagon, color = Color(0xFFFFD600))

    // Speed Limit 20 km/h circle
    drawCircle(color = Color(0xFFD50000), radius = 18f, center = Offset(width * 0.16f, horizonY + 10f))
    drawCircle(color = PureWhite, radius = 14f, center = Offset(width * 0.16f, horizonY + 10f))

    // Children walking on right curb
    val pedX = width * 0.74f
    val pedY = height * 0.65f
    drawCircle(color = Color(0xFF1E272E), radius = 7f, center = Offset(pedX, pedY - 18f))
    drawRoundRect(color = Color(0xFF2980B9), topLeft = Offset(pedX - 5f, pedY - 11f), size = Size(10f, 22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f))
}

// 10 — Motorcycle Traffic
private fun DrawScope.drawMotorcycleTrafficScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Left Filtering Motorcycle
    val mcLeftX = width * 0.22f
    val mcLeftY = height * 0.58f
    drawCircle(color = Color(0xFF2C3E50), radius = 11f, center = Offset(mcLeftX, mcLeftY - 18f))
    drawRoundRect(color = Color(0xFFE74C3C), topLeft = Offset(mcLeftX - 10f, mcLeftY - 7f), size = Size(20f, 24f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))

    // Right Filtering Motorcycle
    val mcRightX = width * 0.76f
    val mcRightY = height * 0.56f
    drawCircle(color = Color(0xFF2C3E50), radius = 11f, center = Offset(mcRightX, mcRightY - 18f))
    drawRoundRect(color = Color(0xFF3498DB), topLeft = Offset(mcRightX - 10f, mcRightY - 7f), size = Size(20f, 24f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
}

// 11 — Intersection Conflict
private fun DrawScope.drawIntersectionConflictScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)
    val horizonY = height * 0.42f

    // Cross Street intersection
    drawRect(color = Color(0xFF34495E), topLeft = Offset(0f, height * 0.50f), size = Size(width, height * 0.20f))

    // Conflicting Car entering aggressively from left
    val conflictCarX = width * 0.25f
    val conflictCarY = height * 0.54f
    drawRoundRect(color = Color(0xFF3498DB), topLeft = Offset(conflictCarX, conflictCarY), size = Size(width * 0.32f, height * 0.14f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))
}

// 12 — Night Driving
private fun DrawScope.drawNightDrivingScene(width: Float, height: Float) {
    // Pitch dark environment
    drawRect(color = Color(0xFF0F141C), size = Size(width, height))

    // Headlight cone beam
    val beamPath = Path().apply {
        moveTo(width * 0.46f, height * 0.46f)
        lineTo(width * 0.54f, height * 0.46f)
        lineTo(width * 0.96f, height)
        lineTo(width * 0.04f, height)
        close()
    }
    drawPath(beamPath, color = Color(0x33FFF59D))

    // Right White Fog Line illuminated
    drawLine(color = PureWhite, start = Offset(width * 0.54f, height * 0.46f), end = Offset(width * 0.90f, height), strokeWidth = 4f)

    // Unlit pedestrian on right shoulder
    val pedX = width * 0.85f
    val pedY = height * 0.68f
    drawCircle(color = Color(0xAA1E272E), radius = 8f, center = Offset(pedX, pedY - 20f))
    drawRoundRect(color = Color(0xAA1E272E), topLeft = Offset(pedX - 6f, pedY - 12f), size = Size(12f, 28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
}

// 13 — Distracted Driving
private fun DrawScope.drawDistractedDrivingScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Phone holder mounted on dashboard
    val phoneW = width * 0.28f
    val phoneH = height * 0.46f
    val phoneX = width * 0.62f
    val phoneY = height * 0.48f

    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(phoneX, phoneY), size = Size(phoneW, phoneH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    drawRoundRect(color = Color(0xFF2C3E50), topLeft = Offset(phoneX + 4f, phoneY + 4f), size = Size(phoneW - 8f, phoneH - 8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))

    // Notification alert banner on screen
    drawRoundRect(color = Color(0xFF00C853), topLeft = Offset(phoneX + 8f, phoneY + 16f), size = Size(phoneW - 16f, 28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
}

// 14 — Fatigued Driving
private fun DrawScope.drawFatiguedDrivingScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height, skyColor = Color(0xFF1A202C))

    // Eyelid / Drowsy vignette blur top and bottom
    drawRect(color = Color(0xCC000000), topLeft = Offset(0f, 0f), size = Size(width, height * 0.22f))
    drawRect(color = Color(0xCC000000), topLeft = Offset(0f, height * 0.78f), size = Size(width, height * 0.22f))

    // Rest Stop Sign
    val signX = width * 0.75f
    val signY = height * 0.35f
    drawRoundRect(color = Color(0xFF1976D2), topLeft = Offset(signX, signY), size = Size(width * 0.20f, height * 0.16f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
}

// 15 — Traffic Sign Recognition
private fun DrawScope.drawTrafficSignScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)
    val horizonY = height * 0.42f

    // Red Traffic Light
    val boxX = width * 0.48f
    val boxY = horizonY - 60f
    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(boxX, boxY), size = Size(30f, 60f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    drawCircle(color = Color(0xFFFF1744), radius = 8f, center = Offset(boxX + 15f, boxY + 12f))

    // Regulatory Signboard "NO RIGHT TURN ON RED"
    val signX = width * 0.62f
    val signY = horizonY - 45f
    drawRoundRect(color = PureWhite, topLeft = Offset(signX, signY), size = Size(width * 0.28f, height * 0.24f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
    drawRoundRect(color = Color(0xFFD50000), topLeft = Offset(signX + 3f, signY + 3f), size = Size(width * 0.28f - 6f, height * 0.24f - 6f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f), style = Stroke(width = 3f))
}

// 16 — Slippery Road
private fun DrawScope.drawSlipperyRoadScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height, groundColor = Color(0xFF37474F), roadColor = Color(0xFF263238))

    // Wet reflective sheen and tire skid curve
    drawOval(color = Color(0x66B0BEC5), topLeft = Offset(width * 0.30f, height * 0.60f), size = Size(width * 0.45f, 20f))

    // Slippery road sign (Yellow diamond)
    val signX = width * 0.78f
    val signY = height * 0.38f
    val diamond = Path().apply {
        moveTo(signX + 20f, signY)
        lineTo(signX + 40f, signY + 20f)
        lineTo(signX + 20f, signY + 40f)
        lineTo(signX, signY + 20f)
        close()
    }
    drawPath(diamond, color = Color(0xFFFFD600))
}

// 17 — Aggressive Driver
private fun DrawScope.drawAggressiveDriverScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Rearview Mirror with Tailgating SUV
    val mirW = width * 0.70f
    val mirH = height * 0.46f
    val mirX = (width - mirW) / 2f
    val mirY = 14f

    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(mirX - 6f, mirY - 6f), size = Size(mirW + 12f, mirH + 12f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f))
    drawRoundRect(color = Color(0xFF2C3E50), topLeft = Offset(mirX, mirY), size = Size(mirW, mirH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f))

    // Massive black SUV grill filling entire mirror
    drawRoundRect(color = Color(0xFF111111), topLeft = Offset(mirX + 10f, mirY + 12f), size = Size(mirW - 20f, mirH - 20f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))

    // Blinding High Beams
    drawCircle(color = PureWhite, radius = 22f, center = Offset(mirX + mirW * 0.22f, mirY + mirH * 0.55f))
    drawCircle(color = Color(0x88FFF59D), radius = 32f, center = Offset(mirX + mirW * 0.22f, mirY + mirH * 0.55f))
    drawCircle(color = PureWhite, radius = 22f, center = Offset(mirX + mirW * 0.78f, mirY + mirH * 0.55f))
    drawCircle(color = Color(0x88FFF59D), radius = 32f, center = Offset(mirX + mirW * 0.78f, mirY + mirH * 0.55f))
}

// 18 — Sudden Pedestrian Hazard
private fun DrawScope.drawSuddenPedestrianHazardScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Stopped Passenger Jeepney on right
    val jX = width * 0.56f
    val jY = height * 0.42f
    val jW = width * 0.38f
    val jH = height * 0.35f
    drawRoundRect(color = Color(0xFF0097A7), topLeft = Offset(jX, jY), size = Size(jW, jH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))
    // Jeepney roof / traditional chrome visor
    drawRect(color = Color(0xFFFFD700), topLeft = Offset(jX, jY), size = Size(jW, 14f))

    // Pedestrian stepping out from front of jeepney
    val pedX = width * 0.50f
    val pedY = height * 0.65f
    drawCircle(color = Color(0xFFD50000), radius = 10f, center = Offset(pedX, pedY - 24f))
    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(pedX - 8f, pedY - 14f), size = Size(16f, 34f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
}

// 19 — Vehicle/Tire Problem
private fun DrawScope.drawVehicleTireProblemScene(width: Float, height: Float) {
    drawCommonRoadBase(width, height)

    // Digital Instrument Cluster with Flashing Flat Tire Warning
    val dashW = width * 0.54f
    val dashH = height * 0.34f
    val dashX = (width - dashW) / 2f
    val dashY = height * 0.62f

    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(dashX, dashY), size = Size(dashW, dashH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    drawRoundRect(color = Color(0xFF0F172A), topLeft = Offset(dashX + 4f, dashY + 4f), size = Size(dashW - 8f, dashH - 8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f))

    // Warning Symbol
    drawCircle(color = Color(0xFFFF6D00), radius = 16f, center = Offset(dashX + dashW / 2f, dashY + dashH * 0.40f))
}

// 20 — Complex Multi-Hazard Road Scenario
private fun DrawScope.drawComplexMultiHazardScene(width: Float, height: Float) {
    drawHeavyRainScene(width, height)

    // Stopped Jeepney on right
    val jX = width * 0.60f
    val jY = height * 0.48f
    drawRoundRect(color = Color(0xFFE91E63), topLeft = Offset(jX, jY), size = Size(width * 0.32f, height * 0.28f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))

    // Motorcycle on left
    val mcX = width * 0.18f
    val mcY = height * 0.60f
    drawCircle(color = Color(0xFF2C3E50), radius = 10f, center = Offset(mcX, mcY - 16f))
    drawRoundRect(color = Color(0xFFFFEB3B), topLeft = Offset(mcX - 8f, mcY - 6f), size = Size(16f, 22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))

    // Pedestrian with umbrella in center
    val pedX = width * 0.44f
    val pedY = height * 0.68f
    // Umbrella dome
    drawArc(color = Color(0xFF9C27B0), startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = Offset(pedX - 16f, pedY - 32f), size = Size(32f, 20f))
    drawRoundRect(color = Color(0xFF1E272E), topLeft = Offset(pedX - 5f, pedY - 14f), size = Size(10f, 24f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f))
}
