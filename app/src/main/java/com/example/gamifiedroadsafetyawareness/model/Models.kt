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
    val status: String, // "In Progress", "Up Next", "Completed"
    val aiDifficulty: String, // "Easy", "Medium", "Hard ⚠️"
    val isRecommended: Boolean = false
)

data class DecisionOption(
    val id: String,
    val label: String,
    val description: String,
    val isCorrect: Boolean,
    val riskLevel: String, // "High Risk", "Moderate Risk", "Safe Choice"
    val explanation: String
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

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val level: Int,
    val xp: Int,
    val streak: Int,
    val complianceRate: String,
    val leagueTag: String,
    val isCurrentUser: Boolean = false
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

    val learningModules = listOf(
        LearningModule(
            id = "mod_2",
            title = "Module 2: Traffic Signs & Pavement Markings",
            description = "Master international regulatory, warning, and guide signs under LTO guidelines.",
            progressPercentage = 0.75f,
            status = "In Progress",
            aiDifficulty = "Medium"
        ),
        LearningModule(
            id = "mod_3",
            title = "Module 3: Speed Limits & Overtaking Laws",
            description = "Defensive positioning, highway speed regulations, and blind spot management.",
            progressPercentage = 0.0f,
            status = "Up Next",
            aiDifficulty = "Hard ⚠️"
        ),
        LearningModule(
            id = "mod_4",
            title = "Module 4: Intersection & Right-of-Way Priority",
            description = "Targeted lesson recommended by AI based on recent quiz telemetry.",
            progressPercentage = 0.20f,
            status = "AI Recommended",
            aiDifficulty = "Adaptive",
            isRecommended = true
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
                explanation = "Under Philippine Traffic Laws (RA 4136) and International Road Safety standards, emergency vehicles with active sirens have absolute right-of-way. Stopping before the crosswalk protects vulnerable pedestrians."
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    val leaderboardEntries = listOf(
        LeaderboardEntry(1, "Maria Santos", 15, 14200, 14, "99% Compliance", "Quezon City LGU"),
        LeaderboardEntry(2, "Carlos Reyes", 14, 13850, 12, "97% Compliance", "Smart Driving School"),
        LeaderboardEntry(3, "Elena Gomez", 12, 11400, 9, "95% Compliance", "QC Youth Safety Group"),
        LeaderboardEntry(4, "Juan D. (You)", 8, 2450, 7, "92% Compliance", "Quezon City LGU", isCurrentUser = true),
        LeaderboardEntry(5, "Pedro Penduko", 7, 2100, 5, "88% Compliance", "Smart Driving School"),
        LeaderboardEntry(6, "Ana Mercado", 6, 1850, 4, "90% Compliance", "Makati Traffic League")
    )

    val badges = listOf(
        BadgeItem("b1", "Hazard Hunter", "🛡️", "Successfully avoided 50 sudden pedestrian hazards in simulations.", true, "Unlocked (50/50)"),
        BadgeItem("b2", "Right-of-Way Pro", "🛑", "Achieved 100% accuracy on intersection priority quizzes.", true, "Unlocked (100%)"),
        BadgeItem("b3", "Night Owl Driver", "🌙", "Completed 10 nighttime low-visibility driving lessons.", true, "Unlocked (10/10)"),
        BadgeItem("b4", "Emergency Responder", "🚑", "Properly yield right-of-way to emergency vehicles in 5 scenarios.", false, "In Progress (4/5 Scenarios)"),
        BadgeItem("b5", "Law Master", "⚖️", "Score 95%+ on the official LTO Mock Theoretical Exam.", false, "Locked (Req: Lvl 10)"),
        BadgeItem("b6", "Zero Collision", "✨", "Complete 20 consecutive simulations with 0 infractions.", false, "Locked (Req: 15/20)")
    )

    val competencyMetrics = listOf(
        CompetencyMetric("Traffic Signs & Rules", 95, "Mastered"),
        CompetencyMetric("Right-of-Way Compliance", 88, "Proficient"),
        CompetencyMetric("Speed & Distance Control", 82, "Good"),
        CompetencyMetric("Hazard Perception & Reflex", 72, "⚠️ AI Focus Area", isFocusArea = true),
        CompetencyMetric("Eco & Defensive Driving", 85, "Proficient")
    )

    val aiCurriculumLogs = listOf(
        "Dynamic Difficulty Scaling: Increased simulation hazard density from 2 to 4 simultaneous vehicles based on your high score in Module 1.",
        "Targeted Remediation Quizzes: Generated a custom 10-question quiz on 'Pedestrian Priority' after Scenario #10 infraction.",
        "Instructor/School Synchronization: Flagged 'Night Driving' as a practice topic for your real-world driving school instructor.",
        "Reflex Diagnostic: Noted 0.6s hesitation before braking on slippery road; inserted 5-min mini-lesson on Wet Weather Braking."
    )
}
