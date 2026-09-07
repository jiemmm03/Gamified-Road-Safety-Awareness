package com.example.gamifiedroadsafetyawareness.model

/**
 * A deterministic categorization of the app's real quiz questions into road-safety topics, plus
 * one curated explanation/tip per topic. Questions have no structured topic field, so this
 * classifies them by matching known keywords in the question's own text — a genuine
 * categorization of existing content, not invented data. Used by the AI tutor for "why was this
 * wrong" explanations, topic Q&A, and weak-topic tracking (see XpManager.recordMissedTopic).
 */
enum class RoadSafetyTopic(
    val displayName: String,
    val keywords: List<String>,
    val explanation: String,
    val safetyTip: String
) {
    RIGHT_OF_WAY(
        displayName = "Right-of-Way",
        keywords = listOf("right of way", "right-of-way", "yield", "priority", "who goes first", "intersection"),
        explanation = "Right-of-way rules decide who moves first when two road users' paths cross — " +
            "at intersections, when merging, or when a pedestrian is crossing. Having the right-of-way " +
            "doesn't mean forcing your way through; it means you may proceed only once it's actually safe.",
        safetyTip = "When in doubt about who has the right-of-way, slow down and yield. Being " +
            "\"technically correct\" isn't worth a collision."
    ),
    TRAFFIC_SIGNS(
        displayName = "Traffic Signs & Signals",
        keywords = listOf("sign", "signal", "traffic light", "red light", "yellow light", "green light", "stop sign", "marking"),
        explanation = "Traffic signs and signals are the shared language of the road — warning signs " +
            "alert you to hazards, regulatory signs state rules you must follow, and signals control " +
            "who moves when. Misreading one is one of the most common causes of avoidable collisions.",
        safetyTip = "A yellow light means prepare to stop, not speed up to beat it."
    ),
    SPEED_MANAGEMENT(
        displayName = "Speed Management",
        keywords = listOf("speed", "speeding", "velocity", "km/h", "kph", "limit"),
        explanation = "Speed limits are the maximum for ideal conditions, not a target. Rain, poor " +
            "visibility, heavy traffic, or an unfamiliar road all call for driving below the posted limit.",
        safetyTip = "Higher speed means longer stopping distance and less time to react — adjust for " +
            "conditions, not just the sign."
    ),
    FOLLOWING_DISTANCE(
        displayName = "Following Distance",
        keywords = listOf("following distance", "tailgat", "stopping distance", "sudden stop", "brake", "rear-end"),
        explanation = "Following distance is your safety margin if the vehicle ahead brakes suddenly. " +
            "It needs to grow with your speed and shrink your reaction+braking time in an emergency.",
        safetyTip = "Use the 3-second rule in good conditions — pick a fixed point the car ahead " +
            "passes, and count 3 seconds before you reach it. Double that in rain."
    ),
    PEDESTRIAN_SAFETY(
        displayName = "Pedestrian Safety",
        keywords = listOf("pedestrian", "crosswalk", "crossing", "sidewalk", "school zone"),
        explanation = "Pedestrians have no protection in a collision, so they're given priority at " +
            "crosswalks and school zones. Drivers are expected to anticipate people stepping out, " +
            "especially near schools, transit stops, and blind corners.",
        safetyTip = "Always slow down near crosswalks and school zones, even if you don't see anyone " +
            "yet — visibility can be limited by parked vehicles or blind spots."
    ),
    OVERTAKING(
        displayName = "Overtaking",
        keywords = listOf("overtak", "overtake", "passing", "pass another", "lane change"),
        explanation = "Overtaking is one of the riskiest maneuvers because it briefly puts you in the " +
            "path of oncoming traffic or a blind spot. It's only safe with clear visibility, enough " +
            "distance, and where it's legally permitted (solid lines, curves, and intersections usually prohibit it).",
        safetyTip = "If you're not certain you have enough space and visibility to complete an overtake " +
            "safely, don't start it."
    ),
    MOTORCYCLE_SAFETY(
        displayName = "Motorcycle Safety",
        keywords = listOf("motorcycle", "motorbike", "rider", "helmet"),
        explanation = "Motorcycles are smaller, faster to maneuver, and easier to miss in a mirror " +
            "check than cars. Both riders and drivers sharing the road need extra awareness of blind " +
            "spots and lane position.",
        safetyTip = "Drivers: always do a shoulder check before changing lanes — motorcycles can be " +
            "hidden in a mirror's blind spot."
    ),
    IMPAIRED_OR_DISTRACTED(
        displayName = "Impaired & Distracted Driving",
        keywords = listOf("drunk", "alcohol", "impair", "distract", "phone", "texting", "fatigue", "tired", "sleepy"),
        explanation = "Alcohol, fatigue, and distractions like phone use all slow reaction time and " +
            "impair judgment — often without the driver realizing how much. These are consistently " +
            "among the leading causes of serious collisions.",
        safetyTip = "If you're too tired or distracted to give the road your full attention, pull over " +
            "safely before continuing."
    ),
    EMERGENCY_PROCEDURES(
        displayName = "Emergency Procedures",
        keywords = listOf("emergency", "ambulance", "siren", "fire truck", "hazard", "breakdown"),
        explanation = "Emergency vehicles with active sirens have priority — the safe response is to " +
            "yield and clear a path, not to compete for the intersection.",
        safetyTip = "When you hear a siren, check your mirrors, signal, and pull to the side smoothly " +
            "rather than braking hard in place."
    ),
    ROAD_COURTESY(
        displayName = "Road Courtesy & Driver Responsibility",
        keywords = listOf("courtesy", "responsib", "aggressive", "horn", "obstacle", "weather", "wet", "slippery"),
        explanation = "Beyond the specific rules, safe driving is a shared responsibility — staying " +
            "calm under pressure, adjusting to road/weather conditions, and not escalating with " +
            "aggressive drivers all reduce risk for everyone.",
        safetyTip = "You can't control other drivers' behavior, only your own — defensive driving means " +
            "planning for their mistakes, not just avoiding your own."
    );

    companion object {
        /** Deterministic keyword match against a question's own text; falls back to ROAD_COURTESY. */
        fun classify(questionText: String, options: List<String> = emptyList()): RoadSafetyTopic {
            val haystack = (listOf(questionText) + options).joinToString(" ").lowercase()
            return entries.firstOrNull { topic -> topic.keywords.any { haystack.contains(it) } }
                ?: ROAD_COURTESY
        }
    }
}
