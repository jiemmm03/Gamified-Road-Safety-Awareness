package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.gamifiedroadsafetyawareness.ui.components.SessionLanguageSelector
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.PureWhite
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
            "mod_easy_quiz", "quiz_easy" -> QuizData.quiz_easy
            "mod_medium_quiz", "quiz_medium" -> QuizData.quiz_medium
            "mod_hard_quiz", "quiz_hard" -> QuizData.quiz_hard
            else -> QuizData.quiz_easy
        }
    }

    val activeQuestions = remember(quizId) {
        val raw = quiz.questions
        if (raw.size > 20) raw.shuffled().take(20) else raw
    }

    // Language selection state (exclusive to Quiz/Assessment session)
    var sessionLanguage by remember { mutableStateOf<String?>(null) }
    var isSessionStarted by remember { mutableStateOf(false) }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var isAnswered by remember { mutableStateOf(false) }
    var timeLeftSeconds by remember { mutableStateOf(20) }

    var comboStreak by remember { mutableIntStateOf(0) }
    var bestComboStreak by remember { mutableIntStateOf(0) }
    var comboXpEarned by remember { mutableIntStateOf(0) }
    var currentMultiplier by remember { mutableFloatStateOf(1.0f) }
    var rewardKey by remember { mutableIntStateOf(0) }
    var rewardText by remember { mutableStateOf("") }
    var hadAnyTimeout by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    var startingProgress by remember { mutableStateOf<UserProgressEntity?>(null) }
    var awardResult by remember { mutableStateOf<XpAwardResult?>(null) }
    var awardedAttempt by remember { mutableStateOf<QuizAttemptEntity?>(null) }
    var highestScorePercentEver by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val answerLog = remember { mutableStateListOf<QuestionAnswerRecord>() }
    var startedAtMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    val isFilipino = sessionLanguage == "fil" || sessionLanguage == "tl"

    val hasProgress = isSessionStarted && !isFinished && (currentQuestionIndex > 0 || isAnswered)
    var showExitConfirm by remember { mutableStateOf(false) }
    fun requestExit() {
        if (hasProgress) showExitConfirm = true else onNavigateBack()
    }

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
                totalQuestions = activeQuestions.size,
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
                awardResult = result,
                selectedLanguage = sessionLanguage ?: "en"
            )
            highestScorePercentEver = xpManager.getHighestScorePercent(username, quiz.id)
            onQuizFinished(score, activeQuestions.size, result)
        }
    }

    LaunchedEffect(currentQuestionIndex, isFinished, isSessionStarted) {
        if (isFinished || !isSessionStarted) return@LaunchedEffect
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
            val timedOutQuestion = activeQuestions[currentQuestionIndex]
            val timedOutTopic = RoadSafetyTopic.classify(timedOutQuestion.question, timedOutQuestion.options)
            val currentQuestionText = if (isFilipino && timedOutQuestion.questionFil.isNotBlank()) timedOutQuestion.questionFil else timedOutQuestion.question
            val currentOptions = if (isFilipino && timedOutQuestion.optionsFil.isNotEmpty()) timedOutQuestion.optionsFil else timedOutQuestion.options
            val explanationText = if (isFilipino && timedOutTopic.explanationFil.isNotBlank()) timedOutTopic.explanationFil else timedOutTopic.explanation
            val safetyTipText = if (isFilipino && timedOutTopic.safetyTipFil.isNotBlank()) timedOutTopic.safetyTipFil else timedOutTopic.safetyTip

            answerLog.add(
                QuestionAnswerRecord(
                    questionId = timedOutQuestion.id,
                    questionNumber = currentQuestionIndex + 1,
                    questionText = currentQuestionText,
                    selectedOptionIndex = -1,
                    correctOptionIndex = timedOutQuestion.correctAnswerIndex,
                    selectedAnswerText = if (isFilipino) "Walang Sagot" else "Unanswered",
                    correctAnswerText = currentOptions.getOrElse(timedOutQuestion.correctAnswerIndex) { "" },
                    isCorrect = false,
                    pointsEarned = 0,
                    xpEarned = 0,
                    explanation = explanationText,
                    safetyTip = safetyTipText,
                    difficulty = quiz.moduleType.name,
                    topic = timedOutTopic.name,
                    answeredAt = System.currentTimeMillis()
                )
            )
            delay(800L)
            if (currentQuestionIndex < activeQuestions.size - 1) {
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

    if (!isSessionStarted) {
        // ── LANGUAGE SELECTION SCREEN (Appears immediately before starting quiz) ──
        SessionLanguageSelector(
            sessionTitle = quiz.title,
            sessionSubtitle = "20 timed road safety and traffic rule questions with AI evaluation.",
            difficultyLabel = "${quiz.moduleType.label} • +${GamificationConstants.ModuleXp.getModuleXp(quizId)} XP",
            questionCountText = "${activeQuestions.size} Questions",
            sessionTypeLabel = "QUIZ ASSESSMENT",
            selectedLanguage = sessionLanguage,
            onLanguageSelected = { sessionLanguage = it },
            onStartConfirmed = { lang ->
                sessionLanguage = lang
                startedAtMillis = System.currentTimeMillis()
                isSessionStarted = true
            },
            onCancel = onNavigateBack,
            modifier = modifier
        )
    } else if (isFinished) {
        val result = awardResult
        val attempt = awardedAttempt
        if (result == null || attempt == null) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
        val question = activeQuestions[currentQuestionIndex]
        val choiceLabels = listOf("A", "B", "C", "D", "E", "F")
        val currentQuestionText = if (isFilipino && question.questionFil.isNotBlank()) question.questionFil else question.question
        val currentOptionsList = if (isFilipino && question.optionsFil.isNotEmpty()) question.optionsFil else question.options

        val shuffledOptions = remember(currentQuestionIndex, isFilipino) {
            currentOptionsList.mapIndexed { index, text -> index to text }.shuffled()
        }
        val runningTotalXp = (startingProgress?.totalXp ?: 0) + comboXpEarned
        val displayLevel = startingProgress?.currentLevel ?: 1

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // ── Header Bar ───────────────────────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = quiz.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = if (isFilipino) "Tanong ${currentQuestionIndex + 1} ng ${activeQuestions.size}" else "Question ${currentQuestionIndex + 1} of ${activeQuestions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ── Linear Progress Bar ──────────────────────────────────────
                LinearProgressIndicator(
                    progress = { (currentQuestionIndex + 1).toFloat() / activeQuestions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Gamification HUD ─────────────────────────────────────────
                QuizGamificationHud(
                    totalXp = runningTotalXp,
                    comboStreak = comboStreak,
                    bestComboStreak = bestComboStreak,
                    multiplier = currentMultiplier,
                    level = displayLevel,
                    questionIndex = currentQuestionIndex,
                    totalQuestions = activeQuestions.size
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Timer Card ───────────────────────────────────────────────
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.HourglassEmpty,
                                contentDescription = "Timer",
                                tint = timerColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isFilipino) "Natitirang Oras" else "Time Remaining",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${timeLeftSeconds}s",
                            style = MaterialTheme.typography.titleMedium,
                            color = timerColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LinearProgressIndicator(
                        progress = { timerProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = timerColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Question Card ────────────────────────────────────────────
                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = currentQuestionText,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Answer Choices ───────────────────────────────────────────
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    shuffledOptions.forEachIndexed { displayIndex, (originalIndex, optionText) ->
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
                            text = optionText,
                            state = state,
                            enabled = !isAnswered,
                            onClick = {
                                selectedOptionIndex = originalIndex
                                isAnswered = true
                                val topic = RoadSafetyTopic.classify(question.question, question.options)
                                val explanationText = if (isFilipino && topic.explanationFil.isNotBlank()) topic.explanationFil else topic.explanation
                                val safetyTipText = if (isFilipino && topic.safetyTipFil.isNotBlank()) topic.safetyTipFil else topic.safetyTip
                                val selectedAnswerText = currentOptionsList.getOrElse(originalIndex) { "" }
                                val correctAnswerText = currentOptionsList.getOrElse(question.correctAnswerIndex) { "" }

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
                                        milestoneBonus > 0 -> if (isFilipino) "🔥 $comboStreak Sunod-sunod na Tama! +$questionXp XP" else "🔥 $comboStreak Answer Streak! +$questionXp XP"
                                        currentMultiplier > 1f -> if (isFilipino) "✅ Tama! +$questionXp XP (${currentMultiplier}×)" else "✅ Correct! +$questionXp XP (${currentMultiplier}×)"
                                        else -> if (isFilipino) "✅ Tama! +$questionXp XP" else "✅ Correct! +$questionXp XP"
                                    }
                                    rewardKey++
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                } else {
                                    comboStreak = 0
                                    currentMultiplier = 1.0f
                                    rewardText = if (isFilipino) "❌ Naputol ang streak!" else "❌ Streak broken!"
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
                                        questionText = currentQuestionText,
                                        selectedOptionIndex = originalIndex,
                                        correctOptionIndex = question.correctAnswerIndex,
                                        selectedAnswerText = selectedAnswerText,
                                        correctAnswerText = correctAnswerText,
                                        isCorrect = isCorrect,
                                        pointsEarned = if (isCorrect) GamificationConstants.QuizXp.CORRECT_ANSWER else 0,
                                        xpEarned = questionXp,
                                        explanation = explanationText,
                                        safetyTip = safetyTipText,
                                        difficulty = quiz.moduleType.name,
                                        topic = topic.name,
                                        answeredAt = System.currentTimeMillis()
                                    )
                                )
                            }
                        )
                    }
                }

                // ── Clear Answer Feedback Box ────────────────────────────────
                if (isAnswered) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val isUserCorrect = selectedOptionIndex == question.correctAnswerIndex
                    val topic = RoadSafetyTopic.classify(question.question, question.options)
                    val explanationText = if (isFilipino && topic.explanationFil.isNotBlank()) topic.explanationFil else topic.explanation
                    val safetyTipText = if (isFilipino && topic.safetyTipFil.isNotBlank()) topic.safetyTipFil else topic.safetyTip
                    val correctText = currentOptionsList.getOrElse(question.correctAnswerIndex) { "" }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isUserCorrect) EmeraldGreen.copy(alpha = 0.1f) else TrafficRed.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, if (isUserCorrect) EmeraldGreen.copy(alpha = 0.4f) else TrafficRed.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isUserCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                                        contentDescription = null,
                                        tint = if (isUserCorrect) EmeraldGreen else TrafficRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (isUserCorrect) {
                                            if (isFilipino) "Tamang Sagot!" else "Correct Answer!"
                                        } else if (selectedOptionIndex == -1) {
                                            if (isFilipino) "Ubos na ang Oras!" else "Time's Up!"
                                        } else {
                                            if (isFilipino) "Maling Sagot" else "Incorrect"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUserCorrect) EmeraldGreen else TrafficRed
                                    )
                                }

                                if (isUserCorrect) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = BadgeGold.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "+${GamificationConstants.QuizXp.CORRECT_ANSWER} XP",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = BadgeGold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            if (!isUserCorrect) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isFilipino) "Tamang Sagot: $correctText" else "Correct Answer: $correctText",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isFilipino) "Paliwanag: $explanationText" else "Why: $explanationText",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )

                            if (safetyTipText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = if (isFilipino) "💡 Ligtas na Payo: " else "💡 Safe Tip: ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberYellow
                                    )
                                    Text(
                                        text = safetyTipText,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Next / Finish / Quit Action Buttons ───────────────────────
                if (isAnswered) {
                    val nextButtonLabel = if (currentQuestionIndex < activeQuestions.size - 1) {
                        if (isFilipino) "Susunod na Tanong" else "Next Question"
                    } else {
                        if (isFilipino) "Tapusin ang Pagsusulit" else "Finish Quiz"
                    }
                    Button(
                        onClick = {
                            if (currentQuestionIndex < activeQuestions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                isFinished = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = nextButtonLabel,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            maxLines = 1,
                            softWrap = false
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                AppOutlinedButton(
                    text = if (isFilipino) "Umalis sa Pagsusulit" else "Quit Quiz",
                    onClick = { requestExit() },
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            FloatingRewardPopup(
                rewardKey = rewardKey,
                text = rewardText,
                color = EmeraldGreen,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp)
            )
        }
    }

    if (showExitConfirm) {
        ConfirmActionDialog(
            title = if (isFilipino) "Umalis sa Pagsusulit?" else "Leave Quiz?",
            message = if (isFilipino) "Sigurado ka bang nais mong bumalik? Maaaring mawala ang iyong kasalukuyang progreso." else "Are you sure you want to go back? Your current progress may not be saved.",
            confirmLabel = if (isFilipino) "Umalis" else "Leave",
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
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
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
            if (textFil.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = textFil,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
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
                    modifier = Modifier.size(22.dp)
                )
            }
            QuizOptionState.INCORRECT -> {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = "Your answer, incorrect",
                    tint = TrafficRed,
                    modifier = Modifier.size(22.dp)
                )
            }
            else -> Unit
        }
    }
}
