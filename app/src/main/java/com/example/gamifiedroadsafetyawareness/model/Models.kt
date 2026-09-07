package com.example.gamifiedroadsafetyawareness.model

/**
 * Core data models and pre-populated mock data for the AI-Integrated Gamified Road Safety Awareness,
 * Traffic Rule Education, and Driver Decision-Making System.
 */

data class UserProfile(
    val name: String,
    val level: Int,
    val currentXp: Int,
    val nextLevelXp: Int,
    val dailyStreakDays: Int,
    val overallSafetyScore: Int,
    val rankInLgu: Int,
    val lguLeagueName: String,
    val totalStudyHours: Float,
    val quizzesPassed: Int,
    val totalQuizzes: Int
)

data class LearningModule(
    val id: String,
    val title: String,
    val description: String,
    val progressPercentage: Float, // 0f to 1f
    val status: String, // "In Progress", "Up Next", "Completed", "Locked"
    val isRecommended: Boolean = false,
    val moduleType: ModuleType = ModuleType.EASY,
    val xpReward: Int = 100,
    val levelRequirement: Int = 1,
    val isLocked: Boolean = false,
    val lockReason: String = "",
    val xpEarned: Int = 0,
    val hasQuiz: Boolean = false
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int = 0, // By default, 0 since the answer key maps to A
    // Bilingual support: natural Filipino translations shown as secondary text under the
    // English original (never replacing it). Same order/meaning as `question`/`options`.
    val questionFil: String = "",
    val optionsFil: List<String> = emptyList()
)

data class Quiz(
    val id: String,
    val title: String,
    val moduleType: ModuleType = ModuleType.EASY,
    val questions: List<QuizQuestion>
)

data class DecisionOption(
    val id: String,
    val label: String,
    val description: String,
    val isCorrect: Boolean,
    val riskLevel: String, // "High Risk", "Moderate Risk", "Safe Choice"
    val explanation: String,
    val tier: SimulationTier = SimulationTier.SAFE_DECISION, // only meaningful when isCorrect is true
    val aiRecommended: Boolean = false
)

data class SimulationScenario(
    val id: String,
    val scenarioNumber: Int,
    val title: String,
    val weather: String,
    val roadGripReduction: String,
    val aiMode: String,
    val description: String,
    val hazards: List<String>,
    val prompt: String,
    val options: List<DecisionOption>,
    val timeLimitSeconds: Int,
    val xpReward: Int
)

data class BadgeItem(
    val id: String,
    val title: String,
    val icon: String,
    val description: String,
    val isUnlocked: Boolean,
    val progressText: String? = null
)

data class CompetencyMetric(
    val skillName: String,
    val percentage: Int,
    val statusLabel: String,
    val isFocusArea: Boolean = false
)

data class UserAccount(
    val username: String,
    val passwordHash: String,
    val role: String, // "USER" or "ADMIN"
    val displayName: String,
    val permissions: Set<com.example.gamifiedroadsafetyawareness.auth.Permission>,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val gender: String = "", // "MALE" or "FEMALE"
    val age: Int? = null,
    val contactNumber: String = ""
)

object MockData {
    val currentUser = UserProfile(
        name = "Juan D.",
        level = 8,
        currentXp = 2450,
        nextLevelXp = 3000,
        dailyStreakDays = 7,
        overallSafetyScore = 84,
        rankInLgu = 4,
        lguLeagueName = "Quezon City LGU League",
        totalStudyHours = 14.2f,
        quizzesPassed = 24,
        totalQuizzes = 25
    )

    /**
     * Three difficulty-based quiz modules, each containing 20 questions.
     */
    val learningModules = listOf(
        LearningModule(
            id = "mod_easy_quiz",
            title = "🟢 Easy Quiz",
            description = "20 questions covering road safety basics, traffic lights, signs, seat belts, and fundamental driving rules.",
            progressPercentage = 0.0f,
            status = "Up Next",
            moduleType = ModuleType.EASY,
            xpReward = 100,
            levelRequirement = 1,
            hasQuiz = true
        ),
        LearningModule(
            id = "mod_medium_quiz",
            title = "🟡 Medium Quiz",
            description = "20 scenario-based questions on lane changes, rain driving, overtaking rules, and defensive driving techniques.",
            progressPercentage = 0.0f,
            status = "Up Next",
            moduleType = ModuleType.MEDIUM,
            xpReward = 200,
            levelRequirement = 1,
            hasQuiz = true
        ),
        LearningModule(
            id = "mod_hard_quiz",
            title = "🔴 Hard Quiz",
            description = "20 advanced situational questions on right-of-way, night driving, skid control, and multi-hazard intersections.",
            progressPercentage = 0.0f,
            status = "Up Next",
            moduleType = ModuleType.HARD,
            xpReward = 300,
            levelRequirement = 1,
            hasQuiz = true
        )
    )

    val activeScenario = SimulationScenario(
        id = "scen_12",
        scenarioNumber = 12,
        title = "Wet Road Intersection & Right-of-Way",
        weather = "Rain (Low Visibility)",
        roadGripReduction = "-30% Grip",
        aiMode = "Dynamic Hazard Spawning Active",
        description = "Your vehicle (Blue Car) is approaching a 4-way intersection at 40 km/h. An emergency vehicle (Ambulance) with active siren is approaching from the left. A pedestrian is stepping onto the crosswalk on your right. The traffic light is blinking yellow.",
        hazards = listOf(
            "⚠️ Hazard 1: Ambulance with Siren (Left approach)",
            "⚠️ Hazard 2: Pedestrian stepping onto crosswalk (Right blind spot)",
            "⚠️ Hazard 3: Wet asphalt (Braking distance increased by 50%)"
        ),
        prompt = "What is the safest and most legally compliant immediate action under Philippine Traffic Laws (RA 4136)?",
        options = listOf(
            DecisionOption(
                id = "opt_a",
                label = "Option A",
                description = "Accelerate quickly to clear the intersection before the ambulance arrives, avoiding a sudden stop on wet pavement.",
                isCorrect = false,
                riskLevel = "Extreme Risk (85% Collision Probability)",
                explanation = "Attempting to 'beat' an emergency vehicle on wet pavement creates an extreme probability of a T-bone collision or severe hydroplaning."
            ),
            DecisionOption(
                id = "opt_b",
                label = "Option B",
                description = "Honk your horn to warn the pedestrian, yield to the ambulance, and edge forward into the crosswalk to prepare for turning.",
                isCorrect = false,
                riskLevel = "Moderate Risk (Traffic Code Violation)",
                explanation = "Honking at pedestrians on crosswalks violates pedestrian priority rules. Edging into the crosswalk obstructs pedestrian flow and creates an infraction."
            ),
            DecisionOption(
                id = "opt_c",
                label = "Option C (Recommended)",
                description = "Come to a smooth, complete stop before the crosswalk line, yielding right-of-way to both the ambulance and the pedestrian.",
                isCorrect = true,
                riskLevel = "Safe Choice (0% Collision Probability)",
                explanation = "Under Philippine Traffic Laws (RA 4136) and International Road Safety standards, emergency vehicles with active sirens have absolute right-of-way. Stopping before the crosswalk protects vulnerable pedestrians.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    val badges = listOf(
        BadgeItem("b1", "NEURAL HUNTER", "🎯", "Successfully identified and avoided 50 sudden pedestrian anomalies in simulations.", true, "UNLOCKED (50/50)"),
        BadgeItem("b2", "PRIORITY NODE PRO", "🛑", "Achieved 100% accuracy on intersection algorithmic priority quizzes.", true, "UNLOCKED (100%)"),
        BadgeItem("b3", "NIGHT VISION DRIVER", "🌙", "Completed 10 nighttime low-visibility sensor calibration lessons.", true, "UNLOCKED (10/10)"),
        BadgeItem("b4", "EMERGENCY OVERRIDE", "🚑", "Properly yield right-of-way to emergency vehicles in 5 scenarios.", false, "IN PROGRESS (4/5)"),
        BadgeItem("b5", "LAW MASTER (AI)", "⚖️", "Score 95%+ on the official System Architecture Mock Exam.", false, "LOCKED (REQ: LVL 10)"),
        BadgeItem("b6", "ZERO COLLISION RUN", "✨", "Complete 20 consecutive simulation loops with 0 infractions.", false, "LOCKED (REQ: 15/20)")
    )

    val competencyMetrics = listOf(
        CompetencyMetric("Signs & Regulations", 95, "Mastered"),
        CompetencyMetric("Algorithmic Right-of-Way", 88, "Proficient"),
        CompetencyMetric("Speed & Velocity Control", 82, "Good"),
        CompetencyMetric("Hazard Perception Latency", 72, "⚠️ DIAGNOSTIC FOCUS", isFocusArea = true),
        CompetencyMetric("Eco & Cyber-Defensive Driving", 85, "Proficient")
    )

    val aiCurriculumLogs = listOf(
        "Dynamic Difficulty Scaling: Increased simulation hazard density from 2 to 4 simultaneous vehicles based on neural efficiency in Module 1.",
        "Targeted Remediation Quizzes: Generated a custom 10-question matrix on 'Pedestrian Priority' after Scenario #10 infraction.",
        "Instructor/School Synchronization: Flagged 'Night Driving' as a priority focus area for your physical driving instructor.",
        "Reflex Diagnostic: Noted 0.6s latency before braking on slippery road; inserted 5-min cyber-lesson on Wet Weather Braking."
    )
}
