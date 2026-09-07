package com.example.gamifiedroadsafetyawareness.model

/**
 * Additional driving-decision scenarios for the AI tutor's "practice a scenario" flow, in the
 * exact same shape as [MockData.activeScenario] so they're reusable by the real Simulation
 * screen too, not chat-only throwaway content. Five representative categories from the tutor's
 * spec (14 were listed; building all of them with the same quality bar is a separate content
 * effort) — pedestrian crossing, a yellow light, a sudden stop ahead, an emergency vehicle, and
 * a distracted-driving moment.
 */
object ChatScenarios {

    private val pedestrianCrossing = SimulationScenario(
        id = "chat_scen_1",
        scenarioNumber = 13,
        title = "Pedestrian Stepping Onto a Mid-Block Crosswalk",
        weather = "Clear",
        roadGripReduction = "0% Grip",
        aiMode = "Pedestrian Intent Prediction Active",
        description = "You're driving at 30 km/h on a two-lane street near a school. A pedestrian " +
            "is standing at a marked mid-block crosswalk, looking at their phone, one foot already " +
            "off the curb.",
        hazards = listOf(
            "⚠️ Hazard 1: Distracted pedestrian at a marked crosswalk",
            "⚠️ Hazard 2: No traffic signal at this crossing — driver judgment required"
        ),
        prompt = "What is the safest immediate action?",
        options = listOf(
            DecisionOption(
                id = "ped_a",
                label = "Option A",
                description = "Speed up slightly to pass the crosswalk before the pedestrian commits to crossing.",
                isCorrect = false,
                riskLevel = "Extreme Risk",
                explanation = "The pedestrian is already stepping off the curb — accelerating toward " +
                    "a marked crosswalk with a pedestrian present is exactly the scenario pedestrian " +
                    "right-of-way rules exist to prevent."
            ),
            DecisionOption(
                id = "ped_b",
                label = "Option B",
                description = "Honk to alert the pedestrian, then continue through at reduced speed.",
                isCorrect = false,
                riskLevel = "Moderate Risk",
                explanation = "Honking doesn't transfer right-of-way to you. A distracted pedestrian may " +
                    "not react predictably to a horn, and you're still committing to cross their path."
            ),
            DecisionOption(
                id = "ped_c",
                label = "Option C (Recommended)",
                description = "Slow to a stop well before the crosswalk and wave the pedestrian across.",
                isCorrect = true,
                riskLevel = "Safe Choice",
                explanation = "Pedestrians at a marked crosswalk have priority. Stopping early gives them " +
                    "room and time, and removes any ambiguity about your intent.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    private val yellowLight = SimulationScenario(
        id = "chat_scen_2",
        scenarioNumber = 14,
        title = "Yellow Light at a Busy Intersection",
        weather = "Clear",
        roadGripReduction = "0% Grip",
        aiMode = "Intersection Timing Analysis Active",
        description = "You're approaching an intersection at 50 km/h. The light turns yellow while " +
            "you're still about 40 meters away — close enough that stopping suddenly could risk a " +
            "rear-end, but far enough that you could still brake normally.",
        hazards = listOf(
            "⚠️ Hazard 1: Yellow light with borderline stopping distance",
            "⚠️ Hazard 2: Vehicle following moderately close behind"
        ),
        prompt = "What is the safest and most legally compliant action?",
        options = listOf(
            DecisionOption(
                id = "yellow_a",
                label = "Option A",
                description = "Accelerate to clear the intersection before the light turns red.",
                isCorrect = false,
                riskLevel = "Extreme Risk",
                explanation = "Speeding up on yellow — \"beating the light\" — is a common cause of " +
                    "intersection collisions with cross-traffic that starts moving on green."
            ),
            DecisionOption(
                id = "yellow_b",
                label = "Option B",
                description = "Brake hard immediately, regardless of the vehicle behind you.",
                isCorrect = false,
                riskLevel = "Moderate Risk",
                explanation = "Braking hard without checking your mirror risks a rear-end collision if " +
                    "the driver behind isn't expecting a sudden stop."
            ),
            DecisionOption(
                id = "yellow_c",
                label = "Option C (Recommended)",
                description = "Check your mirror, then brake smoothly to stop before the line.",
                isCorrect = true,
                riskLevel = "Safe Choice",
                explanation = "At this distance you can stop safely and normally. A yellow light means " +
                    "prepare to stop, not a countdown to accelerate through.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    private val suddenStopAhead = SimulationScenario(
        id = "chat_scen_3",
        scenarioNumber = 15,
        title = "Vehicle Ahead Brakes Suddenly",
        weather = "Light Rain",
        roadGripReduction = "-15% Grip",
        aiMode = "Following-Distance Monitor Active",
        description = "You're driving at 60 km/h in light rain. The vehicle two car-lengths ahead " +
            "brakes suddenly for a road obstacle you can't yet see.",
        hazards = listOf(
            "⚠️ Hazard 1: Sudden braking from the vehicle ahead",
            "⚠️ Hazard 2: Wet road surface increasing stopping distance"
        ),
        prompt = "What is the safest immediate response?",
        options = listOf(
            DecisionOption(
                id = "stop_a",
                label = "Option A",
                description = "Swerve into the next lane without checking it first.",
                isCorrect = false,
                riskLevel = "Extreme Risk",
                explanation = "Changing lanes without checking creates a new collision risk with " +
                    "traffic you haven't accounted for — often worse than the original hazard."
            ),
            DecisionOption(
                id = "stop_b",
                label = "Option B",
                description = "Brake as hard as possible in your current lane.",
                isCorrect = false,
                riskLevel = "Moderate Risk",
                explanation = "Panic braking on a wet road can lock the wheels and reduce steering " +
                    "control. It may be necessary, but only as a controlled, not maximal, response — " +
                    "and it wouldn't have been needed at all with adequate following distance."
            ),
            DecisionOption(
                id = "stop_c",
                label = "Option C (Recommended)",
                description = "Brake firmly but smoothly, staying in your lane — this is exactly what following distance is for.",
                isCorrect = true,
                riskLevel = "Safe Choice",
                explanation = "With a proper following distance for wet conditions, a firm, controlled " +
                    "brake in your own lane is enough to stop safely without overreacting.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    private val emergencyVehicle = SimulationScenario(
        id = "chat_scen_4",
        scenarioNumber = 16,
        title = "Ambulance Approaching From Behind",
        weather = "Clear",
        roadGripReduction = "0% Grip",
        aiMode = "Emergency Vehicle Detection Active",
        description = "You're in the middle lane of a three-lane road. An ambulance with active " +
            "siren and lights is approaching quickly from behind.",
        hazards = listOf(
            "⚠️ Hazard 1: Emergency vehicle needing a clear path",
            "⚠️ Hazard 2: Traffic in adjacent lanes also reacting"
        ),
        prompt = "What is the safest and most legally compliant response?",
        options = listOf(
            DecisionOption(
                id = "emg_a",
                label = "Option A",
                description = "Stop immediately in your current lane.",
                isCorrect = false,
                riskLevel = "Moderate Risk",
                explanation = "Stopping in place blocks the ambulance's path instead of clearing it, " +
                    "and risks being rear-ended by traffic behind you."
            ),
            DecisionOption(
                id = "emg_b",
                label = "Option B",
                description = "Speed up to get ahead of the ambulance before pulling over.",
                isCorrect = false,
                riskLevel = "Extreme Risk",
                explanation = "Accelerating in front of an emergency vehicle delays it further and " +
                    "increases collision risk for everyone reacting to it at once."
            ),
            DecisionOption(
                id = "emg_c",
                label = "Option C (Recommended)",
                description = "Signal, smoothly move to the side of the road, and slow to let it pass.",
                isCorrect = true,
                riskLevel = "Safe Choice",
                explanation = "Emergency vehicles with active sirens have priority. Yielding to the " +
                    "side in a controlled way clears their path quickly and safely.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    private val distractedMoment = SimulationScenario(
        id = "chat_scen_5",
        scenarioNumber = 17,
        title = "Phone Notification While Driving",
        weather = "Clear",
        roadGripReduction = "0% Grip",
        aiMode = "Driver Attention Monitor Active",
        description = "You're driving on a moderately busy road when your phone buzzes with a " +
            "notification in the cupholder.",
        hazards = listOf(
            "⚠️ Hazard 1: Potential loss of visual and mental attention from the road",
            "⚠️ Hazard 2: Moderate traffic requiring continuous awareness"
        ),
        prompt = "What is the safest response?",
        options = listOf(
            DecisionOption(
                id = "dist_a",
                label = "Option A",
                description = "Glance down and quickly check what the notification says.",
                isCorrect = false,
                riskLevel = "Extreme Risk",
                explanation = "Even a 2-3 second glance away from the road at driving speed covers a " +
                    "significant distance \"blind.\" Most distraction-related collisions happen in " +
                    "exactly this window."
            ),
            DecisionOption(
                id = "dist_b",
                label = "Option B",
                description = "Try to check it using voice commands without looking at the screen.",
                isCorrect = false,
                riskLevel = "Moderate Risk",
                explanation = "Voice commands reduce visual distraction but still divide your attention " +
                    "and reaction time — better than looking, but not the safest option available."
            ),
            DecisionOption(
                id = "dist_c",
                label = "Option C (Recommended)",
                description = "Leave the phone untouched and check it only after safely stopping or reaching your destination.",
                isCorrect = true,
                riskLevel = "Safe Choice",
                explanation = "No notification is worth dividing attention from active driving. Waiting " +
                    "until stopped removes the risk entirely.",
                tier = SimulationTier.PERFECT_SIMULATION,
                aiRecommended = true
            )
        ),
        timeLimitSeconds = 15,
        xpReward = 150
    )

    val all: List<SimulationScenario> = listOf(
        pedestrianCrossing, yellowLight, suddenStopAhead, emergencyVehicle, distractedMoment
    )
}
