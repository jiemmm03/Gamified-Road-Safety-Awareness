package com.example.gamifiedroadsafetyawareness.model

import com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager

/**
 * One rendered chat turn from RoadSafe AI. Plain data — [ui.screens.AiTutorScreen] decides how
 * to render it.
 *
 * Fields:
 * - [text]         — the main AI response body (always present)
 * - [answerLabel]  — "Answer:" prefix line (only for quiz/scenario responses)
 * - [explanation]  — "Explanation:" section body (for quiz/scenario answers and topic lookups)
 * - [safetyTip]    — "Safety Tip:" section body (for quiz/scenario answers and topic lookups)
 * - [answerOptions] — when non-empty, this message is a question awaiting a choice; the UI
 *                     renders them as [AnswerOptionsColumn] buttons wired to
 *                     [AiTutorEngine.submitAnswer] rather than free-text input.
 * - [quickReplies] — plain suggestion chips that re-submit their own text via
 *                    [AiTutorEngine.handleFreeText].
 * - [isStructured] — true when the message carries Answer/Explanation/Tip sections to be
 *                    rendered as formatted sections rather than a single flat text block.
 * - [topicTag]     — short display label (e.g., "Right-of-Way") shown as a chip on the bubble.
 */
data class TutorMessage(
    val text: String,
    val answerLabel: String = "",
    val explanation: String = "",
    val safetyTip: String = "",
    val topicTag: String = "",
    val isStructured: Boolean = false,
    val quickReplies: List<String> = emptyList(),
    val answerOptions: List<String> = emptyList()
)

/**
 * Rule-based local dialogue engine for "RoadSafe AI" — no LLM, no network calls.
 *
 * Priorities applied in every response (per Master Prompt):
 *   1. Accuracy   — every explanation is grounded in R.A. 4136 / DPWH / LTO curriculum.
 *   2. Relevance  — off-topic queries are redirected with a respectful gate message.
 *   3. Safety     — every non-trivial response surfaces at least one safety tip.
 *   4. Clarity    — responses use the structured Answer / Explanation / Safety Tip format.
 *   5. Conciseness — conversational messages are short; structured messages are thorough.
 *
 * Language detection: if the user writes in Filipino/Tagalog (≥2 Filipino keywords present),
 * the engine returns the FIL variant of explanations and tips. Language is auto-detected
 * per message — no explicit mode switch is required.
 *
 * Firestore sync: every interaction is logged to the [FirebaseSyncManager.syncAiInteraction]
 * so the Admin Web panel can display real AI activity feed.
 */
class AiTutorEngine(
    private val xpManager: XpManager,
    private val username: String,
    private val syncManager: FirebaseSyncManager = FirebaseSyncManager()
) {

    companion object {
        /** Shown only when the user asks something completely outside road-safety scope. */
        const val OFF_TOPIC_EN =
            "I'm RoadSafe AI — a specialized road safety and driver education assistant. " +
            "I can help you understand Philippine traffic rules, road signs, driving " +
            "scenarios, quiz questions, and road safety topics. " +
            "What road safety question can I answer for you?"

        const val OFF_TOPIC_FIL =
            "Ako ay RoadSafe AI — isang espesyalista sa kaligtasan sa daan at edukasyon " +
            "ng driver. Maaari akong tumulong sa iyo sa mga patakaran sa trapiko sa Pilipinas, " +
            "mga senyas sa daan, senaryo ng pagmamaneho, at mga paksa sa kaligtasan. " +
            "Anong tanong sa kaligtasan sa daan ang maaari kong sagutin para sa iyo?"

        /** Shown when the engine cannot classify a road-safety query to any known topic. */
        const val FALLBACK_EN =
            "I don't have verified information for that specific question. " +
            "Please consult the official LTO learning materials, DPWH road sign manual, " +
            "or the modules in this app for accurate guidance."

        const val FALLBACK_FIL =
            "Wala akong nabe-verify na impormasyon para sa tanong na iyon. " +
            "Mangyaring kumonsulta sa opisyal na materyales ng LTO, manual ng senyas sa daan " +
            "ng DPWH, o mga module sa app na ito para sa tumpak na gabay."

        val QUICK_REPLIES_EN = listOf(
            "Quiz me", "Practice a scenario", "How am I doing?", "Explain a topic", "Hint"
        )
        val QUICK_REPLIES_FIL = listOf(
            "I-quiz mo ako", "Subukan ang senaryo", "Kumusta ang aking progreso?",
            "Ipaliwanag ang paksa", "Pahiwatig"
        )
    }

    private enum class PendingType { NONE, QUIZ, SCENARIO }

    private var pendingType = PendingType.NONE
    private var pendingQuestion: QuizQuestion? = null
    private var pendingQuestionTopic: RoadSafetyTopic? = null
    private var pendingScenario: SimulationScenario? = null
    private var hintStage = 0

    private var lastDifficultyUsed = ModuleType.EASY
    private var sessionCorrect = 0
    private var sessionTotal = 0

    /** Language detected for the current turn. Re-evaluated per call to handleFreeText. */
    private var isFilipinoMode = false

    // ── Public interface ──────────────────────────────────────────────────────────

    suspend fun greet(): TutorMessage {
        val progress = xpManager.getProgress(username)
        val isFil = isFilipinoMode
        val greetText = if (isFil) {
            "Kumusta! Ako si RoadSafe AI, ang iyong guro sa kaligtasan sa daan. " +
            "Maaari akong mag-quiz sa iyo, gabayan ka sa mga senaryo ng pagmamaneho, " +
            "ipaliwanag ang mga paksa, o suriin ang iyong progreso. " +
            "Kasalukuyan kang Level ${progress.currentLevel} na may ${progress.totalXp} XP. " +
            "Ano ang gusto mong gawin?"
        } else {
            "Hi! I'm RoadSafe AI, your road safety tutor. I can quiz you, walk you " +
            "through driving-decision scenarios, explain a traffic topic, or check " +
            "how you're doing. You're currently Level ${progress.currentLevel} with " +
            "${progress.totalXp} XP. What would you like to do?"
        }
        logInteraction(
            prompt = "[GREET]",
            response = greetText,
            topic = "General",
            language = if (isFil) "FIL" else "EN"
        )
        return TutorMessage(
            text = greetText,
            quickReplies = if (isFil) QUICK_REPLIES_FIL else QUICK_REPLIES_EN
        )
    }

    /** Handles typed input or a tapped quick-reply chip (both arrive as plain text). */
    suspend fun handleFreeText(raw: String): TutorMessage {
        val text = raw.trim()
        if (text.isBlank()) return fallback()

        isFilipinoMode = RoadSafetyTopic.isFilipino(text)
        val lower = text.lowercase()

        val result = when {
            isGreeting(lower) -> greet()
            isHintIntent(lower) -> giveHint()
            isQuizIntent(lower) -> startQuiz(difficultyFrom(lower))
            isScenarioIntent(lower) -> startScenario()
            isProgressIntent(lower) -> progressSummary()
            isTopicListIntent(lower) -> listTopics()
            else -> {
                // Check on-topic guard before doing topic lookup
                if (RoadSafetyTopic.isOnTopic(text)) {
                    topicLookup(lower) ?: fallback()
                } else {
                    offTopic()
                }
            }
        }

        // Log every free-text interaction to Firestore
        val topicName = pendingQuestionTopic?.displayName ?: pendingScenario?.title ?: result.topicTag.ifBlank { "General" }
        logInteraction(
            prompt = text,
            response = result.text.take(500),
            topic = topicName,
            language = if (isFilipinoMode) "FIL" else "EN"
        )

        return result
    }

    /** Handles a tap on one of the current message's [TutorMessage.answerOptions]. */
    suspend fun submitAnswer(index: Int): TutorMessage = when (pendingType) {
        PendingType.QUIZ -> handleQuizAnswer(index)
        PendingType.SCENARIO -> handleScenarioAnswer(index)
        PendingType.NONE -> TutorMessage(
            text = if (isFilipinoMode) "Walang aktibong tanong sa kasalukuyan."
                   else "There's no active question right now.",
            quickReplies = quickReplies()
        )
    }

    // ── Intent detectors ──────────────────────────────────────────────────────────

    private fun isGreeting(t: String) =
        (t == "hi" || t == "hello" || t == "kumusta" || t == "helo") ||
        (t.startsWith("hi ") && t.length <= 10) ||
        (t.startsWith("hello") && t.length <= 10)

    private fun isHintIntent(t: String) =
        t.contains("hint") || t.contains("pahiwatig") || t.contains("clue")

    private fun isQuizIntent(t: String) =
        t.contains("quiz") || t.contains("test me") || t.contains("question") ||
        t.contains("i-quiz") || t.contains("tanong") || t.contains("exam")

    private fun isScenarioIntent(t: String) =
        t.contains("scenario") || t.contains("practice") || t.contains("decision") ||
        t.contains("simulate") || t.contains("senaryo") || t.contains("subukan") ||
        t.contains("simulate") || t.contains("simulation")

    private fun isProgressIntent(t: String) =
        t.contains("progress") || t.contains("how am i doing") || t.contains("my xp") ||
        t.contains("my level") || t.contains("my score") || t.contains("progreso") ||
        t.contains("kumusta ang") || t.contains("gaano")

    private fun isTopicListIntent(t: String) =
        (t.contains("explain") || t.contains("ipaliwanag") || t.contains("topics") ||
         t.contains("paksa") || t.contains("list")) && topicLookup(t) == null

    // ── Off-topic gate ─────────────────────────────────────────────────────────────

    private fun offTopic(): TutorMessage {
        val msg = if (isFilipinoMode) OFF_TOPIC_FIL else OFF_TOPIC_EN
        logInteraction("[OFF-TOPIC]", msg, "Off-Topic", if (isFilipinoMode) "FIL" else "EN")
        return TutorMessage(text = msg, quickReplies = quickReplies())
    }

    private fun fallback(): TutorMessage {
        val msg = if (isFilipinoMode) FALLBACK_FIL else FALLBACK_EN
        return TutorMessage(text = msg, quickReplies = quickReplies())
    }

    // ── Quiz flow ─────────────────────────────────────────────────────────────────

    private fun quizFor(difficulty: ModuleType): Quiz = when (difficulty) {
        ModuleType.EASY   -> QuizData.quiz_easy
        ModuleType.MEDIUM -> QuizData.quiz_medium
        ModuleType.HARD   -> QuizData.quiz_hard
    }

    private fun difficultyFrom(text: String): ModuleType? = when {
        text.contains("hard") || text.contains("mahirap")  -> ModuleType.HARD
        text.contains("medium") || text.contains("katamtaman") -> ModuleType.MEDIUM
        text.contains("easy") || text.contains("madali")   -> ModuleType.EASY
        else -> null
    }

    private fun adaptiveDifficulty(): ModuleType {
        if (sessionTotal < 3) return lastDifficultyUsed
        val accuracy = sessionCorrect.toFloat() / sessionTotal
        return when {
            accuracy >= 0.8f -> when (lastDifficultyUsed) {
                ModuleType.EASY   -> ModuleType.MEDIUM
                ModuleType.MEDIUM -> ModuleType.HARD
                ModuleType.HARD   -> ModuleType.HARD
            }
            accuracy <= 0.4f -> when (lastDifficultyUsed) {
                ModuleType.HARD   -> ModuleType.MEDIUM
                ModuleType.MEDIUM -> ModuleType.EASY
                ModuleType.EASY   -> ModuleType.EASY
            }
            else -> lastDifficultyUsed
        }
    }

    private fun startQuiz(requestedDifficulty: ModuleType?): TutorMessage {
        val difficulty = requestedDifficulty ?: adaptiveDifficulty()
        val quiz = quizFor(difficulty)
        val candidates = quiz.questions.filter { it.id != pendingQuestion?.id }
        val question = (candidates.ifEmpty { quiz.questions }).random()

        pendingQuestion = question
        pendingQuestionTopic = RoadSafetyTopic.classify(question.question, question.options)
        pendingType = PendingType.QUIZ
        hintStage = 0
        lastDifficultyUsed = difficulty

        val diffLabel = when (difficulty) {
            ModuleType.EASY   -> if (isFilipinoMode) "🟢 Madali" else "🟢 Easy"
            ModuleType.MEDIUM -> if (isFilipinoMode) "🟡 Katamtaman" else "🟡 Medium"
            ModuleType.HARD   -> if (isFilipinoMode) "🔴 Mahirap" else "🔴 Hard"
        }
        val topicLabel = pendingQuestionTopic?.displayName ?: ""

        val prompt = if (isFilipinoMode)
            "$diffLabel na tanong:\n\n${question.question}"
        else
            "$diffLabel question:\n\n${question.question}"

        return TutorMessage(
            text = prompt,
            topicTag = topicLabel,
            quickReplies = listOf(if (isFilipinoMode) "Pahiwatig" else "Hint"),
            answerOptions = question.options.mapIndexed { i, option -> "${'A' + i}. $option" }
        )
    }

    private suspend fun handleQuizAnswer(index: Int): TutorMessage {
        val question = pendingQuestion ?: return fallback()
        val topic = pendingQuestionTopic ?: RoadSafetyTopic.ROAD_COURTESY
        val isCorrect = index == question.correctAnswerIndex
        val isFil = isFilipinoMode

        sessionTotal++
        if (isCorrect) sessionCorrect++

        val answerLabel: String
        val explanationText: String
        val tipText: String
        val summaryText: String

        if (isCorrect) {
            val award = xpManager.awardTutorPracticeAnswer(username, question.question.take(60))
            answerLabel = if (isFil) "✅ Tama!" else "✅ Correct!"
            summaryText = if (isFil)
                "Magaling — +${award.totalAwarded} XP ang iyong natanggap.${if (award.leveledUp) " Nag-level up ka sa Level ${award.newLevel} (${award.newLevelName})! 🎉" else ""}"
            else
                "Nice work — you earned +${award.totalAwarded} XP.${if (award.leveledUp) " You leveled up to Level ${award.newLevel} (${award.newLevelName})! 🎉" else ""}"
            explanationText = if (isFil) topic.explanationFil else topic.explanation
            tipText = if (isFil) topic.safetyTipFil else topic.safetyTip
        } else {
            xpManager.recordMissedTopic(username, topic.name)
            val correctAnswer = question.options.getOrNull(question.correctAnswerIndex) ?: ""
            answerLabel = if (isFil) "❌ Mali" else "❌ Incorrect"
            summaryText = if (isFil)
                "Tamang sagot: $correctAnswer"
            else
                "The correct answer was: $correctAnswer"
            explanationText = if (isFil) topic.explanationFil else topic.explanation
            tipText = if (isFil) topic.safetyTipFil else topic.safetyTip
        }

        logInteraction(
            prompt = "[QUIZ-ANSWER] ${question.question.take(80)}",
            response = "$answerLabel $summaryText",
            topic = topic.displayName,
            language = if (isFil) "FIL" else "EN"
        )

        pendingQuestion = null
        pendingQuestionTopic = null
        pendingType = PendingType.NONE
        hintStage = 0

        return TutorMessage(
            text = summaryText,
            answerLabel = answerLabel,
            explanation = explanationText,
            safetyTip = tipText,
            topicTag = topic.displayName,
            isStructured = true,
            quickReplies = quickReplies()
        )
    }

    private fun giveHint(): TutorMessage {
        val topic = pendingQuestionTopic
        if (pendingType != PendingType.QUIZ || topic == null) {
            return TutorMessage(
                text = if (isFilipinoMode)
                    "Walang aktibong tanong sa kasalukuyan. Gusto mo bang mag-quiz?"
                else
                    "There's no active question right now — want me to quiz you?",
                quickReplies = quickReplies()
            )
        }
        hintStage++
        val text = when (hintStage) {
            1 -> if (isFilipinoMode)
                    "Pahiwatig 1: Isipin ito kaugnay ng ${topic.displayName}."
                 else
                    "Hint 1: Think about this in terms of ${topic.displayName}."
            2 -> if (isFilipinoMode)
                    "Pahiwatig 2: ${topic.safetyTipFil}"
                 else
                    "Hint 2: ${topic.safetyTip}"
            else -> if (isFilipinoMode)
                    "Iyan na ang lahat ng pahiwatig — pumili na ng pinakamainam na sagot."
                 else
                    "That's both hints I have — take your best guess from the options above."
        }
        return TutorMessage(
            text = text,
            quickReplies = if (hintStage < 2) listOf(if (isFilipinoMode) "Pahiwatig" else "Hint") else emptyList()
        )
    }

    // ── Scenario flow ─────────────────────────────────────────────────────────────

    private fun startScenario(): TutorMessage {
        val allScenarios = listOf(MockData.activeScenario) + ChatScenarios.all
        val scenario = allScenarios.filter { it.id != pendingScenario?.id }.ifEmpty { allScenarios }.random()

        pendingScenario = scenario
        pendingType = PendingType.SCENARIO

        val hazardsText = scenario.hazards.joinToString("\n")
        val prefix = if (isFilipinoMode) "Senaryo" else "Scenario"
        return TutorMessage(
            text = "$prefix: ${scenario.description}\n\n$hazardsText\n\n${scenario.prompt}",
            topicTag = RoadSafetyTopic.classify(scenario.title, scenario.hazards).displayName,
            answerOptions = scenario.options.map { "${it.label}: ${it.description}" }
        )
    }

    private suspend fun handleScenarioAnswer(index: Int): TutorMessage {
        val scenario = pendingScenario ?: return fallback()
        val option = scenario.options.getOrNull(index) ?: return fallback()
        val isFil = isFilipinoMode

        val ratingLabel = when {
            option.isCorrect -> if (isFil) "🟢 Ligtas" else "🟢 Safe"
            option.riskLevel.contains("Extreme", ignoreCase = true) -> if (isFil) "🔴 Hindi Ligtas" else "🔴 Unsafe"
            else -> if (isFil) "🟡 Kailangan ng Pagpapabuti" else "🟡 Needs Improvement"
        }

        var xpAwarded = 0
        if (option.isCorrect) {
            val award = xpManager.awardSimulationDecision(username, option.tier, option.aiRecommended, scenario.title)
            xpAwarded = award.totalAwarded
        }

        val recommended = scenario.options.firstOrNull { it.aiRecommended } ?: scenario.options.first { it.isCorrect }
        val principleTopic = RoadSafetyTopic.classify(scenario.title, listOf(scenario.prompt) + scenario.hazards)

        val xpLine = if (xpAwarded > 0) "+$xpAwarded XP"
                     else if (isFil) "Walang XP ngayon — ang pinaka-ligtas na pagpili lamang ang nagbibigay ng XP."
                     else "No XP this time — only the safest choice earns XP."

        val summaryText = buildString {
            append(if (isFil) "Iyong Desisyon: " else "Your Decision: ")
            append("${option.description}\n")
            append(if (isFil) "XP: $xpLine" else "XP: $xpLine")
        }

        val explanationText = buildString {
            append(if (isFil) "Inirerekomendang aksyon: ${recommended.description}\n\n" else "Recommended action: ${recommended.description}\n\n")
            append(option.explanation)
        }

        val tipText = if (isFil) principleTopic.safetyTipFil else principleTopic.safetyTip

        logInteraction(
            prompt = "[SCENARIO-ANSWER] ${scenario.title}",
            response = "$ratingLabel | $summaryText",
            topic = principleTopic.displayName,
            language = if (isFil) "FIL" else "EN"
        )

        pendingScenario = null
        pendingType = PendingType.NONE

        return TutorMessage(
            text = summaryText,
            answerLabel = ratingLabel,
            explanation = explanationText,
            safetyTip = tipText,
            topicTag = principleTopic.displayName,
            isStructured = true,
            quickReplies = quickReplies()
        )
    }

    // ── Progress summary ───────────────────────────────────────────────────────────

    private suspend fun progressSummary(): TutorMessage {
        val progress = xpManager.getProgress(username)
        val history = xpManager.getXpHistory(username, 5)
        val unlockedIds = xpManager.getUnlockedAchievementIds(username)
        val isFil = isFilipinoMode

        val missed = progress.missedTopics.split(",").filter { it.isNotBlank() }
        val weakTopicLine = missed.groupingBy { it }.eachCount().entries
            .maxByOrNull { it.value }
            ?.let { (name, count) -> RoadSafetyTopic.entries.find { it.name == name }?.to(count) }
            ?.let { (topic, count) ->
                if (isFil) "Napansin ko na madalas kang nagkakamali sa ${topic.displayName} ($count beses) — gusto mo bang magsanay?"
                else "You've missed ${topic.displayName} questions $count time(s) recently — want to practice that topic?"
            }
            ?: if (isFil) "Walang kahinaan na nakita sa iyong kasaysayan — magaling!" else "No recent weak spots — nice work!"

        data class Candidate(val title: String, val remaining: Int, val target: Int)
        val candidates = listOfNotNull(
            if ("quiz_veteran" !in unlockedIds) Candidate("Quiz Veteran (10 quizzes)", 10 - progress.quizzesCompleted, 10) else null,
            if ("scenario_specialist" !in unlockedIds) Candidate("Scenario Specialist (5 simulations)", 5 - progress.scenariosCompleted, 5) else null,
            if ("question_master" !in unlockedIds) Candidate("Question Master (100 correct)", 100 - progress.completedQuestionsCount, 100) else null,
            if ("dedication_award" !in unlockedIds) Candidate("Dedication Award (5,000 XP)", 5000 - progress.totalXp, 5000) else null,
            if ("streak_master" !in unlockedIds) Candidate("Streak Master (7-day streak)", 7 - progress.longestStreak, 7) else null
        ).filter { it.remaining > 0 }
        val nearest = candidates.minByOrNull { it.remaining.toFloat() / it.target }
        val achievementLine = nearest?.let {
            if (isFil) "Malapit ka na sa \"${it.title}\" — ${it.remaining} pa ang kailangan."
            else "You're getting close to \"${it.title}\" — ${it.remaining} to go."
        } ?: ""

        val recentLine = if (history.isNotEmpty()) {
            val prefix = if (isFil) "Kamakailan: " else "Recent activity: "
            prefix + history.take(3).joinToString("; ") { "${it.activityName} (+${it.totalAwarded} XP)" }
        } else ""

        val levelName = GamificationConstants.getLevelName(progress.currentLevel)
        val text = buildString {
            if (isFil) {
                append("Ikaw ay Level ${progress.currentLevel} ($levelName) ")
                append("na may ${progress.totalXp} kabuuang XP at ${progress.currentStreak}-araw na streak.\n\n")
            } else {
                append("You're Level ${progress.currentLevel} ($levelName) ")
                append("with ${progress.totalXp} total XP and a ${progress.currentStreak}-day streak.\n\n")
            }
            append(weakTopicLine)
            if (achievementLine.isNotEmpty()) append("\n\n$achievementLine")
            if (recentLine.isNotEmpty()) append("\n\n$recentLine")
        }

        logInteraction("[PROGRESS-QUERY]", text.take(300), "Progress", if (isFil) "FIL" else "EN")

        return TutorMessage(text = text, quickReplies = quickReplies())
    }

    // ── Topic Q&A ─────────────────────────────────────────────────────────────────

    private fun topicLookup(text: String): TutorMessage? {
        val topic = RoadSafetyTopic.entries.firstOrNull { candidate ->
            text.contains(candidate.displayName.lowercase()) ||
            candidate.keywords.any { text.contains(it) }
        } ?: return null

        val isFil = isFilipinoMode
        val summaryText = if (isFil)
            "Narito ang impormasyon tungkol sa ${topic.displayName}:"
        else
            "Here's what you need to know about ${topic.displayName}:"

        logInteraction(
            prompt = text,
            response = (if (isFil) topic.explanationFil else topic.explanation).take(300),
            topic = topic.displayName,
            language = if (isFil) "FIL" else "EN"
        )

        return TutorMessage(
            text = summaryText,
            answerLabel = topic.displayName,
            explanation = if (isFil) topic.explanationFil else topic.explanation,
            safetyTip = if (isFil) topic.safetyTipFil else topic.safetyTip,
            topicTag = topic.displayName,
            isStructured = true,
            quickReplies = quickReplies()
        )
    }

    private fun listTopics(): TutorMessage {
        val isFil = isFilipinoMode
        val names = RoadSafetyTopic.entries.map { it.displayName }
        val prompt = if (isFil) "Sige — alin sa mga paksang ito ang gusto mong ipaliwanag?"
                     else "Sure — which topic would you like explained?"
        return TutorMessage(
            text = "$prompt\n\n" + names.joinToString("\n") { "• $it" },
            quickReplies = names.take(8)
        )
    }

    // ── Helpers ───────────────────────────────────────────────────────────────────

    private fun quickReplies() = if (isFilipinoMode) QUICK_REPLIES_FIL else QUICK_REPLIES_EN

    /**
     * Fire-and-forget Firestore sync for every AI interaction.
     * The Admin Web panel reads from `ai_interactions` to display the real-time feed.
     */
    private fun logInteraction(
        prompt: String,
        response: String,
        topic: String,
        language: String
    ) {
        try {
            syncManager.syncAiInteraction(
                userId = username,
                prompt = prompt,
                response = response,
                topic = topic,
                language = language
            )
        } catch (_: Exception) {
            // Logging is non-critical — never crash the UI thread
        }
    }
}
