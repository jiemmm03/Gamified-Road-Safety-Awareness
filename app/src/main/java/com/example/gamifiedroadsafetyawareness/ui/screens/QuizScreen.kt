package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.QuestionAnswerRecord
import com.example.gamifiedroadsafetyawareness.model.QuizData
import com.example.gamifiedroadsafetyawareness.model.RoadSafetyTopic
import com.example.gamifiedroadsafetyawareness.model.XpAwardResult
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.QuizAttemptEntity
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppOutlinedButton
import com.example.gamifiedroadsafetyawareness.ui.components.ConfirmActionDialog
import com.example.gamifiedroadsafetyawareness.ui.components.FloatingRewardPopup
import com.example.gamifiedroadsafetyawareness.ui.components.QuizGamificationHud
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    quizId: String,
    username: String = "",
    xpManager: XpManager? = null,
    onNavigateBack: () -> Unit,
    onQuizFinished: (score: Int, total: Int, result: XpAwardResult) -> Unit = { _, _, _ -> },
    onReviewAnswers: (attemptId: Long) -> Unit = {},
    onViewLearningHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val quiz = remember(quizId) {
        when (quizId) {
            "mod_easy_quiz" -> QuizData.quiz_easy
            "mod_medium_quiz" -> QuizData.quiz_medium
            "mod_hard_quiz" -> QuizData.quiz_hard
            else -> QuizData.quiz_easy
        }
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }
    var timeLeftSeconds by remember { mutableStateOf(20) }

    // Per-quiz combo streak — resets on a wrong answer, drives streak bonuses + multiplier.
    var comboStreak by remember { mutableIntStateOf(0) }
    var bestComboStreak by remember { mutableIntStateOf(0) }
    var comboXpEarned by remember { mutableIntStateOf(0) }
    var currentMultiplier by remember { mutableFloatStateOf(1.0f) }
    var rewardKey by remember { mutableIntStateOf(0) }
    var rewardText by remember { mutableStateOf("") }
    // Answering every question before its timer expires earns the time-challenge bonus.
    var hadAnyTimeout by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    var startingProgress by remember { mutableStateOf<UserProgressEntity?>(null) }
    var awardResult by remember { mutableStateOf<XpAwardResult?>(null) }
    var awardedAttempt by remember { mutableStateOf<QuizAttemptEntity?>(null) }
    var highestScorePercentEver by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // Per-question answer log — previously discarded once the user moved to the next question.
    // Backs Module Summary's breakdown and the Module Review screen.
    val answerLog = remember { mutableStateListOf<QuestionAnswerRecord>() }
    val startedAtMillis = remember { System.currentTimeMillis() }

    // Has the user actually engaged with this attempt? Gates the leave-confirmation so a fresh,
    // untouched question 1 can be left silently but real progress can't be discarded by accident.
    val hasProgress = !isFinished && (currentQuestionIndex > 0 || isAnswered)
    var showExitConfirm by remember { mutableStateOf(false) }
    fun requestExit() {
        if (hasProgress) showExitConfirm = true else onNavigateBack()
    }

    // Same function backs the system back button, the header arrow, and the Quit/Continue
    // buttons, so every exit path behaves identically.
    BackHandler { requestExit() }

    LaunchedEffect(username, xpManager) {
        if (username.isNotBlank()) {
            startingProgress = xpManager?.getProgress(username)
        }
    }

    LaunchedEffect(isFinished) {
        if (isFinished && xpManager != null && username.isNotBlank()) {
            val result = xpManager.awardQuizCompletion(
                username = username,
                correctAnswers = score,
                totalQuestions = quiz.questions.size,
                comboXpEarned = comboXpEarned,
                bestComboStreak = bestComboStreak,
                timeChallengeCompleted = !hadAnyTimeout,
                quizId = quizId,
                quizTitle = quiz.title
            )
            awardResult = result
            awardedAttempt = xpManager.recordQuizAttempt(
                username = username,
                quizId = quiz.id,
                moduleId = quizId,
                quizTitle = quiz.title,
                difficulty = quiz.moduleType,
                answers = answerLog.toList(),
                timeSpentSeconds = ((System.currentTimeMillis() - startedAtMillis) / 1000).toInt(),
                startedAt = startedAtMillis,
                bestComboStreak = bestComboStreak,
                timeChallengeCompleted = !hadAnyTimeout,
                awardResult = result
            )
            highestScorePercentEver = xpManager.getHighestScorePercent(username, quiz.id)
            onQuizFinished(score, quiz.questions.size, result)
        }
    }

    LaunchedEffect(currentQuestionIndex, isFinished) {
        if (isFinished) return@LaunchedEffect
        timeLeftSeconds = 20
        selectedOptionIndex = -1
        isAnswered = false
        while (timeLeftSeconds > 0 && !isAnswered) {
            delay(1000L)
            if (!isAnswered) {
                timeLeftSeconds--
            }
        }
        if (!isAnswered && !isFinished) {
            isAnswered = true
            hadAnyTimeout = true
            comboStreak = 0
            currentMultiplier = 1.0f
            val timedOutQuestion = quiz.questions[currentQuestionIndex]
            val timedOutTopic = RoadSafetyTopic.classify(timedOutQuestion.question, timedOutQuestion.options)
            answerLog.add(
                QuestionAnswerRecord(
                    questionId = timedOutQuestion.id,
                    questionNumber = currentQuestionIndex + 1,
                    questionText = timedOutQuestion.question,
                    selectedOptionIndex = -1,
                    correctOptionIndex = timedOutQuestion.correctAnswerIndex,
                    selectedAnswerText = "Unanswered",
                    correctAnswerText = timedOutQuestion.options.getOrElse(timedOutQuestion.correctAnswerIndex) { "" },
                    isCorrect = false,
                    pointsEarned = 0,
                    xpEarned = 0,
                    explanation = timedOutTopic.explanation,
                    safetyTip = timedOutTopic.safetyTip,
                    difficulty = quiz.moduleType.name,
                    topic = timedOutTopic.name,
                    answeredAt = System.currentTimeMillis()
                )
            )
            delay(800L)
            if (currentQuestionIndex < quiz.questions.size - 1) {
                currentQuestionIndex++
            } else {
                isFinished = true
            }
        }
    }

    val timerProgress by animateFloatAsState(
        targetValue = timeLeftSeconds / 20f,
        animationSpec = tween(durationMillis = 300),
        label = "timer"
    )

    val timerColor by animateColorAsState(
        targetValue = when {
            timeLeftSeconds > 10 -> EmeraldGreen
            timeLeftSeconds > 5 -> AmberYellow
            else -> TrafficRed
        },
        animationSpec = tween(durationMillis = 300),
        label = "timerColor"
    )

    if (isFinished) {
        val result = awardResult
        val attempt = awardedAttempt
        if (result == null || attempt == null) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ModuleSummaryScreen(
                attempt = attempt,
                awardResult = result,
                highestScorePercentEver = highestScorePercentEver,
                onReviewAnswers = { onReviewAnswers(attempt.id) },
                onViewLearningHistory = onViewLearningHistory,
                onDone = { requestExit() },
                modifier = modifier
            )
        }
    } else {
        val question = quiz.questions[currentQuestionIndex]
        val choiceLabels = listOf("A", "B", "C", "D", "E", "F")
        val shuffledOptions = remember(currentQuestionIndex) {
            question.options.mapIndexed { index, text -> index to text }.shuffled()
        }
        val runningTotalXp = (startingProgress?.totalXp ?: 0) + comboXpEarned
        val displayLevel = startingProgress?.currentLevel ?: 1

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { requestExit() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = quiz.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Question ${currentQuestionIndex + 1} of ${quiz.questions.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / quiz.questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                QuizGamificationHud(
                    totalXp = runningTotalXp,
                    comboStreak = comboStreak,
                    bestComboStreak = bestComboStreak,
                    multiplier = currentMultiplier,
                    level = displayLevel,
                    questionIndex = currentQuestionIndex,
                    totalQuestions = quiz.questions.size
                )

                Spacer(modifier = Modifier.height(12.dp))

                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.HourglassEmpty,
                                contentDescription = "Timer",
                                tint = timerColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Time Remaining",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${timeLeftSeconds}s",
                            style = MaterialTheme.typography.titleLarge,
                            color = timerColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LinearProgressIndicator(
                        progress = { timerProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = timerColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (question.questionFil.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question.questionFil,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    shuffledOptions.forEachIndexed { displayIndex, (originalIndex, option) ->
                        val isSelected = selectedOptionIndex == originalIndex
                        val isCorrect = originalIndex == question.correctAnswerIndex
                        val state = when {
                            isAnswered && isCorrect -> QuizOptionState.CORRECT
                            isAnswered && isSelected && !isCorrect -> QuizOptionState.INCORRECT
                            !isAnswered && isSelected -> QuizOptionState.SELECTED
                            else -> QuizOptionState.NEUTRAL
                        }

                        QuizOptionRow(
                            label = choiceLabels[displayIndex],
                            text = option,
                            textFil = question.optionsFil.getOrNull(originalIndex).orEmpty(),
                            state = state,
                            enabled = !isAnswered,
                            onClick = {
                                selectedOptionIndex = originalIndex
                                isAnswered = true
                                val topic = RoadSafetyTopic.classify(question.question, question.options)
                                // Compute XP before logging so it can be captured in the record.
                                var questionXp = 0
                                if (isCorrect) {
                                    score++
                                    comboStreak++
                                    bestComboStreak = maxOf(bestComboStreak, comboStreak)
                                    val milestoneBonus = GamificationConstants.STREAK_BONUS_TABLE[comboStreak] ?: 0
                                    currentMultiplier = GamificationConstants.multiplierForStreak(comboStreak)
                                    questionXp = ((GamificationConstants.QuizXp.CORRECT_ANSWER + milestoneBonus) * currentMultiplier).toInt()
                                    comboXpEarned += questionXp
                                    rewardText = when {
                                        milestoneBonus > 0 -> "🔥 $comboStreak Answer Streak! +$questionXp XP"
                                        currentMultiplier > 1f -> "✅ Correct! +$questionXp XP (${currentMultiplier}×)"
                                        else -> "✅ Correct! +$questionXp XP"
                                    }
                                    rewardKey++
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                } else {
                                    comboStreak = 0
                                    currentMultiplier = 1.0f
                                    rewardText = "❌ Streak broken!"
                                    rewardKey++
                                    if (username.isNotBlank() && xpManager != null) {
                                        coroutineScope.launch {
                                            xpManager.recordMissedTopic(username, topic.name)
                                        }
                                    }
                                }
                                answerLog.add(
                                    QuestionAnswerRecord(
                                        questionId = question.id,
                                        questionNumber = currentQuestionIndex + 1,
                                        questionText = question.question,
                                        selectedOptionIndex = originalIndex,
                                        correctOptionIndex = question.correctAnswerIndex,
                                        selectedAnswerText = question.options.getOrElse(originalIndex) { "" },
                                        correctAnswerText = question.options.getOrElse(question.correctAnswerIndex) { "" },
                                        isCorrect = isCorrect,
                                        pointsEarned = if (isCorrect) GamificationConstants.QuizXp.CORRECT_ANSWER else 0,
                                        xpEarned = questionXp,
                                        explanation = topic.explanation,
                                        safetyTip = topic.safetyTip,
                                        difficulty = quiz.moduleType.name,
                                        topic = topic.name,
                                        answeredAt = System.currentTimeMillis()
                                    )
                                )
                            }
                        )
                    }
                }

                if (isAnswered) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val correctDisplayIndex = shuffledOptions.indexOfFirst { it.first == question.correctAnswerIndex }
                    val isUserCorrect = selectedOptionIndex == question.correctAnswerIndex
                    val feedbackText = when {
                        isUserCorrect -> "✅ Correct! +${GamificationConstants.QuizXp.CORRECT_ANSWER} XP"
                        selectedOptionIndex == -1 -> "⏰ Time's up! The correct answer was ${choiceLabels[correctDisplayIndex]}."
                        else -> "❌ Incorrect — Keep learning and try again!"
                    }
                    Text(
                        text = feedbackText,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isUserCorrect) EmeraldGreen else TrafficRed
                    )
                    if (isUserCorrect && comboStreak > 1) {
                        Text(
                            text = "🔥 $comboStreak answer streak! ${if (currentMultiplier > 1f) "${currentMultiplier}× multiplier active" else ""}",
                            style = MaterialTheme.typography.labelMedium,
                            color = AmberYellow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isAnswered) {
                    AppButton(
                        text = if (currentQuestionIndex < quiz.questions.size - 1) "Next Question →" else "Finish Quiz",
                        onClick = {
                            if (currentQuestionIndex < quiz.questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                isFinished = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                AppOutlinedButton(
                    text = "Quit Quiz",
                    onClick = { requestExit() },
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            FloatingRewardPopup(
                rewardKey = rewardKey,
                text = rewardText,
                color = EmeraldGreen,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            )
        }
    }

    if (showExitConfirm) {
        ConfirmActionDialog(
            title = "Leave Quiz?",
            message = "Are you sure you want to go back? Your current progress may not be saved.",
            confirmLabel = "Leave",
            destructive = true,
            onConfirm = {
                showExitConfirm = false
                onNavigateBack()
            },
            onDismiss = { showExitConfirm = false }
        )
    }
}

private enum class QuizOptionState { NEUTRAL, SELECTED, CORRECT, INCORRECT }

/**
 * A single quiz choice: flat (no shadow) bordered row with a compact letter badge and, once
 * answered, a trailing check/cross icon — kept deliberately quiet so 4-6 stacked options read
 * as one clean list rather than a stack of cards.
 */
@Composable
private fun QuizOptionRow(
    label: String,
    text: String,
    textFil: String = "",
    state: QuizOptionState,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = when (state) {
        QuizOptionState.SELECTED -> MaterialTheme.colorScheme.primary
        QuizOptionState.CORRECT -> EmeraldGreen
        QuizOptionState.INCORRECT -> TrafficRed
        QuizOptionState.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val bgColor = when (state) {
        QuizOptionState.NEUTRAL -> MaterialTheme.colorScheme.surface
        else -> accentColor.copy(alpha = 0.08f)
    }
    val borderColor = when (state) {
        QuizOptionState.NEUTRAL -> MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        else -> accentColor
    }
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bgColor, shape)
            .border(1.2.dp, borderColor, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(accentColor.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (textFil.isNotBlank()) {
                Text(
                    text = textFil,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        when (state) {
            QuizOptionState.CORRECT -> {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Correct answer",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            QuizOptionState.INCORRECT -> {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = "Your answer, incorrect",
                    tint = TrafficRed,
                    modifier = Modifier.size(20.dp)
                )
            }
            else -> Unit
        }
    }
}
