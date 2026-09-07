package com.example.gamifiedroadsafetyawareness.model

/**
 * One rendered chat turn from RoadSafe AI. Plain data — [ui.screens.AiTutorScreen] decides how
 * to render it. [answerOptions], when non-empty, means this message is a question awaiting a
 * choice — the UI should render them as buttons wired to [AiTutorEngine.submitAnswer] rather
 * than free-text input. [quickReplies] are plain suggestion chips that just re-submit their own
 * text via [AiTutorEngine.handleFreeText].
 */
data class TutorMessage(
    val text: String,
    val quickReplies: List<String> = emptyList(),
    val answerOptions: List<String> = emptyList()
)

/**
 * Rule-based local dialogue engine for "RoadSafe AI" — no LLM, no network calls. Every response
 * is either a template filled with the user's real Room-backed progress (via [XpManager]), or a
 * fixed knowledge-base lookup ([RoadSafetyTopic]). Free-form input is matched against a small,
 * fixed set of known intents/topics by keyword; anything unrecognized gets [FALLBACK_TEXT]
 * verbatim rather than a guessed answer — this engine does not have true natural-language
 * understanding, and never pretends to.
 */
class AiTutorEngine(private val xpManager: XpManager, private val username: String) {

    companion object {
        const val FALLBACK_TEXT = "I don't have enough verified information to give you a " +
            "reliable answer. Please check the official traffic authority or the application's " +
            "verified learning materials."

        val QUICK_REPLIES = listOf(
            "Quiz me", "Practice a scenario", "How am I doing?", "Explain a topic", "Hint"
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

    suspend fun greet(): TutorMessage {
        val progress = xpManager.getProgress(username)
        return TutorMessage(
            text = "Hi! I'm RoadSafe AI, your road safety tutor. I can quiz you, walk you " +
                "through driving-decision scenarios, explain a topic, or check how you're doing. " +
                "You're currently Level ${progress.currentLevel} with ${progress.totalXp} XP. " +
                "What would you like to do?",
            quickReplies = QUICK_REPLIES
        )
    }

    /** Handles typed input or a tapped quick-reply chip (both arrive as plain text). */
    suspend fun handleFreeText(raw: String): TutorMessage {
        val text = raw.trim().lowercase()
        if (text.isBlank()) return TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)

        return when {
            text.contains("hi") && text.length <= 6 -> greet()
            text.contains("hint") -> giveHint()
            text.contains("quiz") || text.contains("test me") -> startQuiz(difficultyFrom(text))
            text.contains("scenario") || text.contains("practice") || text.contains("decision") -> startScenario()
            text.contains("how am i doing") || text.contains("progress") -> progressSummary()
            text.contains("explain") && topicLookup(text) == null -> listTopics()
            else -> topicLookup(text) ?: TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)
        }
    }

    /** Handles a tap on one of the current message's [TutorMessage.answerOptions]. */
    suspend fun submitAnswer(index: Int): TutorMessage = when (pendingType) {
        PendingType.QUIZ -> handleQuizAnswer(index)
        PendingType.SCENARIO -> handleScenarioAnswer(index)
        PendingType.NONE -> TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)
    }

    // ── Quiz flow (spec §5 / §7 / §13) ──────────────────────────────────────────

    private fun quizFor(difficulty: ModuleType): Quiz = when (difficulty) {
        ModuleType.EASY -> QuizData.quiz_easy
        ModuleType.MEDIUM -> QuizData.quiz_medium
        ModuleType.HARD -> QuizData.quiz_hard
    }

    private fun difficultyFrom(text: String): ModuleType? = when {
        text.contains("hard") -> ModuleType.HARD
        text.contains("medium") -> ModuleType.MEDIUM
        text.contains("easy") -> ModuleType.EASY
        else -> null
    }

    /** Rolling in-session accuracy suggests the next difficulty — never tied to currentLevel (spec §12). */
    private fun adaptiveDifficulty(): ModuleType {
        if (sessionTotal < 3) return lastDifficultyUsed
        val accuracy = sessionCorrect.toFloat() / sessionTotal
        return when {
            accuracy >= 0.8f -> when (lastDifficultyUsed) {
                ModuleType.EASY -> ModuleType.MEDIUM
                ModuleType.MEDIUM -> ModuleType.HARD
                ModuleType.HARD -> ModuleType.HARD
            }
            accuracy <= 0.4f -> when (lastDifficultyUsed) {
                ModuleType.HARD -> ModuleType.MEDIUM
                ModuleType.MEDIUM -> ModuleType.EASY
                ModuleType.EASY -> ModuleType.EASY
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

        val difficultyLabel = when (difficulty) {
            ModuleType.EASY -> "🟢 Easy"
            ModuleType.MEDIUM -> "🟡 Medium"
            ModuleType.HARD -> "🔴 Hard"
        }

        return TutorMessage(
            text = "$difficultyLabel question:\n\n${question.question}",
            quickReplies = listOf("Hint"),
            answerOptions = question.options.mapIndexed { i, option -> "${'A' + i}. $option" }
        )
    }

    private suspend fun handleQuizAnswer(index: Int): TutorMessage {
        val question = pendingQuestion ?: return TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)
        val topic = pendingQuestionTopic ?: RoadSafetyTopic.ROAD_COURTESY
        val isCorrect = index == question.correctAnswerIndex

        sessionTotal++
        if (isCorrect) sessionCorrect++

        val text = if (isCorrect) {
            val award = xpManager.awardTutorPracticeAnswer(username, question.question.take(60))
            buildString {
                append("Result: ✅ Correct!\n\nNice work — that's +${award.totalAwarded} XP.")
                if (award.leveledUp) append(" You leveled up to Level ${award.newLevel} (${award.newLevelName})! 🎉")
            }
        } else {
            xpManager.recordMissedTopic(username, topic.name)
            val correctAnswer = question.options.getOrNull(question.correctAnswerIndex) ?: ""
            "Result: ❌ Incorrect\nCorrect Answer: $correctAnswer\nWhy: ${topic.explanation}\nSafety Tip: ${topic.safetyTip}"
        }

        pendingQuestion = null
        pendingQuestionTopic = null
        pendingType = PendingType.NONE
        hintStage = 0

        return TutorMessage(text, QUICK_REPLIES)
    }

    private fun giveHint(): TutorMessage {
        val topic = pendingQuestionTopic
        if (pendingType != PendingType.QUIZ || topic == null) {
            return TutorMessage("There's no active question right now — want me to quiz you?", QUICK_REPLIES)
        }
        hintStage++
        val text = when (hintStage) {
            1 -> "Hint 1: Think about this in terms of ${topic.displayName}."
            2 -> "Hint 2: ${topic.safetyTip}"
            else -> "That's both hints I have for this one — take your best guess from the options above."
        }
        return TutorMessage(text, quickReplies = if (hintStage < 2) listOf("Hint") else emptyList())
    }

    // ── Scenario flow (spec §4 / §11) ───────────────────────────────────────────

    private fun startScenario(): TutorMessage {
        val allScenarios = listOf(MockData.activeScenario) + ChatScenarios.all
        val scenario = allScenarios.filter { it.id != pendingScenario?.id }.ifEmpty { allScenarios }.random()

        pendingScenario = scenario
        pendingType = PendingType.SCENARIO

        val hazardsText = scenario.hazards.joinToString("\n")
        return TutorMessage(
            text = "Scenario: ${scenario.description}\n\n$hazardsText\n\n${scenario.prompt}",
            answerOptions = scenario.options.map { "${it.label}: ${it.description}" }
        )
    }

    private suspend fun handleScenarioAnswer(index: Int): TutorMessage {
        val scenario = pendingScenario ?: return TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)
        val option = scenario.options.getOrNull(index) ?: return TutorMessage(FALLBACK_TEXT, QUICK_REPLIES)

        val rating = when {
            option.isCorrect -> "🟢 Safe"
            option.riskLevel.contains("Extreme", ignoreCase = true) -> "🔴 Unsafe"
            else -> "🟡 Needs Improvement"
        }

        var xpAwarded = 0
        if (option.isCorrect) {
            val award = xpManager.awardSimulationDecision(username, option.tier, option.aiRecommended, scenario.title)
            xpAwarded = award.totalAwarded
        }

        val recommended = scenario.options.firstOrNull { it.aiRecommended } ?: scenario.options.first { it.isCorrect }
        val principleTopic = RoadSafetyTopic.classify(scenario.title, listOf(scenario.prompt) + scenario.hazards)
        val xpLine = if (xpAwarded > 0) "+$xpAwarded XP" else "No XP this time — only the safest choice earns XP. Want to try another scenario?"

        pendingScenario = null
        pendingType = PendingType.NONE

        val text = """
            Scenario: ${scenario.title}
            Your Decision: ${option.description}
            Safety Rating: $rating
            Reason: ${option.explanation}
            Recommended Action: ${recommended.description}
            Safety Principle: ${principleTopic.safetyTip}
            XP: $xpLine
        """.trimIndent()

        return TutorMessage(text, QUICK_REPLIES)
    }

    // ── Personalization summary (spec §3) ───────────────────────────────────────

    private suspend fun progressSummary(): TutorMessage {
        val progress = xpManager.getProgress(username)
        val history = xpManager.getXpHistory(username, 5)
        val unlockedIds = xpManager.getUnlockedAchievementIds(username)

        val missed = progress.missedTopics.split(",").filter { it.isNotBlank() }
        val weakTopicLine = missed.groupingBy { it }.eachCount().entries
            .maxByOrNull { it.value }
            ?.let { (name, count) -> RoadSafetyTopic.entries.find { it.name == name }?.to(count) }
            ?.let { (topic, count) -> "You've missed ${topic.displayName} questions $count time(s) recently — want to practice that topic?" }
            ?: "No recent weak spots in your quiz history — nice work."

        data class Candidate(val title: String, val remaining: Int, val target: Int)
        val candidates = listOfNotNull(
            if ("quiz_veteran" !in unlockedIds) Candidate("Quiz Veteran (complete 10 quizzes)", 10 - progress.quizzesCompleted, 10) else null,
            if ("scenario_specialist" !in unlockedIds) Candidate("Scenario Specialist (5 simulations)", 5 - progress.scenariosCompleted, 5) else null,
            if ("question_master" !in unlockedIds) Candidate("Question Master (100 correct answers)", 100 - progress.completedQuestionsCount, 100) else null,
            if ("dedication_award" !in unlockedIds) Candidate("Dedication Award (5,000 total XP)", 5000 - progress.totalXp, 5000) else null,
            if ("streak_master" !in unlockedIds) Candidate("Streak Master (7-day streak)", 7 - progress.longestStreak, 7) else null
        ).filter { it.remaining > 0 }
        val nearest = candidates.minByOrNull { it.remaining.toFloat() / it.target }
        val achievementLine = nearest?.let { "You're getting close to \"${it.title}\" — ${it.remaining} to go." } ?: ""

        val recentLine = if (history.isNotEmpty()) {
            "Recent activity: " + history.take(3).joinToString("; ") { "${it.activityName} (+${it.totalAwarded} XP)" }
        } else ""

        val text = buildString {
            append("You're Level ${progress.currentLevel} (${GamificationConstants.getLevelName(progress.currentLevel)}) ")
            append("with ${progress.totalXp} total XP and a ${progress.currentStreak}-day streak.\n\n")
            append(weakTopicLine)
            if (achievementLine.isNotEmpty()) append("\n\n$achievementLine")
            if (recentLine.isNotEmpty()) append("\n\n$recentLine")
        }

        return TutorMessage(text, QUICK_REPLIES)
    }

    // ── Topic Q&A (spec §8 / §14) ───────────────────────────────────────────────

    private fun topicLookup(text: String): TutorMessage? {
        val topic = RoadSafetyTopic.entries.firstOrNull { candidate ->
            text.contains(candidate.displayName.lowercase()) || candidate.keywords.any { text.contains(it) }
        } ?: return null

        return TutorMessage(
            text = "${topic.displayName}\n\n${topic.explanation}\n\nSafety Tip: ${topic.safetyTip}",
            quickReplies = QUICK_REPLIES
        )
    }

    private fun listTopics(): TutorMessage {
        val names = RoadSafetyTopic.entries.map { it.displayName }
        return TutorMessage(
            text = "Sure — which topic would you like explained?\n\n" + names.joinToString("\n") { "• $it" },
            quickReplies = names
        )
    }
}
