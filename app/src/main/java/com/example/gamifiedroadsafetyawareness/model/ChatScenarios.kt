package com.example.gamifiedroadsafetyawareness.model

/**
 * Complete 20-Item Visual Driving Simulation Scenarios for the
 * AI-Integrated Gamified Road Safety Awareness, Traffic Rule Education, and Driver Decision-Making System.
 *
 * Exactly 20 Scenarios:
 * - Easy: 01 to 05 (5 items)
 * - Medium: 06 to 15 (10 items)
 * - Hard: 16 to 20 (5 items)
 */
object ChatScenarios {

    val scenario01 = SimulationScenario(
        id = "sim_01",
        scenarioNumber = 1,
        title = "01 — Pedestrian Crossing",
        difficulty = SimulationDifficulty.EASY,
        weather = "Clear Daylight",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Pedestrian Intent & Crosswalk Monitor Active",
        description = "You are driving at 35 km/h on a standard Philippine city street. Ahead, a marked zebra pedestrian crossing is visible with pedestrians standing on the curb preparing to cross.",
        hazards = listOf(
            "⚠️ Hazard 1: Vulnerable pedestrians stepping onto zebra crossing",
            "⚠️ Hazard 2: Cross-traffic flow requiring clear vehicle signaling"
        ),
        prompt = "What is the safest and legally required action?",
        options = listOf(
            DecisionOption(
                "s01_a", "Option A",
                "Speed up slightly to clear the crossing before the pedestrians step onto the road.",
                false, "Extreme Collision Risk",
                "Speeding up through an occupied crosswalk directly endangers pedestrians and violates Philippine traffic law (R.A. 4136)."
            ),
            DecisionOption(
                "s01_b", "Option B",
                "Honk your horn repeatedly and continue driving through at current speed.",
                false, "Hazardous & Illegal",
                "Honking does not transfer right-of-way to the driver. Pedestrians on designated zebra crossings have legal priority."
            ),
            DecisionOption(
                "s01_c", "Option C (Recommended)",
                "Decelerate smoothly, bring the vehicle to a full stop before the stop line, and yield right-of-way.",
                true, "Safe Choice",
                "Decelerating early and yielding right-of-way gives pedestrians a safe passage and eliminates impact risk.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s01_d", "Option D",
                "Swerve into the opposing lane to drive around the crossing without stopping.",
                false, "Severe Head-On Collision Risk",
                "Swerving into opposing lanes creates catastrophic head-on crash risks with oncoming traffic."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 100,
        hazardIdentified = "Vulnerable pedestrians at marked crosswalk with potential sudden movement.",
        safetyPrinciple = "R.A. 4136 (Land Transportation and Traffic Code) — Absolute duty to yield right-of-way to pedestrians at marked crosswalks.",
        recommendedAction = "Release accelerator, apply gentle threshold braking, stop before the white limit line, and wait until pedestrians fully clear your lane.",
        topicTag = "Pedestrian Safety & Crosswalks"
    )

    val scenario02 = SimulationScenario(
        id = "sim_02",
        scenarioNumber = 2,
        title = "02 — Changing Traffic Light",
        difficulty = SimulationDifficulty.EASY,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Intersection Timing & Dilemma Zone AI Active",
        description = "You are approaching an urban signalized intersection at 40 km/h, approximately 35 meters away. The traffic signal suddenly changes from green to solid amber/yellow.",
        hazards = listOf(
            "⚠️ Hazard 1: Impending red light phase",
            "⚠️ Hazard 2: Cross-traffic anticipating early green release"
        ),
        prompt = "What is the safest and most legally compliant action?",
        options = listOf(
            DecisionOption(
                "s02_a", "Option A",
                "Accelerate quickly to beat the light before the red signal turns on.",
                false, "High Intersection Crash Risk",
                "Speeding up on yellow frequently leads to right-angle broadside (T-bone) crashes as cross-traffic starts moving."
            ),
            DecisionOption(
                "s02_b", "Option B (Recommended)",
                "Check rearview mirror and apply controlled braking to stop safely before the stop line.",
                true, "Safe Choice",
                "Yellow means prepare to stop when it can be done safely. At 35m and 40 km/h, controlled stopping is completely safe.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s02_c", "Option C",
                "Slam on emergency brakes instantly without checking following traffic.",
                false, "Rear-End Collision Risk",
                "Sudden panic braking without glancing at mirrors can trigger rear-end collisions from following vehicles."
            ),
            DecisionOption(
                "s02_d", "Option D",
                "Sound your horn and coast through the intersection without slowing down.",
                false, "Traffic Signal Violation",
                "Ignoring yellow duty-to-stop violates basic intersection safety laws and endangers cross-traffic."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 100,
        hazardIdentified = "Signal phase change from green to yellow in the dilemma zone.",
        safetyPrinciple = "Yellow Signal Rule — Duty to stop safely before the stop line unless already too close to stop without danger.",
        recommendedAction = "Check your rearview mirror, apply progressive brake pressure, and stop cleanly behind the intersection limit line.",
        topicTag = "Traffic Signals & Intersections"
    )

    val scenario03 = SimulationScenario(
        id = "sim_03",
        scenarioNumber = 3,
        title = "03 — Motorcycle Blind Spot",
        difficulty = SimulationDifficulty.EASY,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Lateral Blind Zone Detection Active",
        description = "You plan to change lanes to the left. Your rearview and side mirrors look clear, but a quick shoulder check reveals a motorcycle riding in your rear-left blind spot.",
        hazards = listOf(
            "⚠️ Hazard 1: Motorcycle concealed in vehicle rear-quarter blind zone",
            "⚠️ Hazard 2: Rapid closure rate of two-wheeled vehicles"
        ),
        prompt = "What should you do before initiating your lane change?",
        options = listOf(
            DecisionOption(
                "s03_a", "Option A",
                "Continue your lane change quickly since you already activated your turn signal.",
                false, "Side-Swipe Collision",
                "Turn signals indicate intention, not right-of-way. Merging into an occupied blind spot causes side-swipes."
            ),
            DecisionOption(
                "s03_b", "Option B (Recommended)",
                "Hold your current lane, maintain speed, allow the motorcycle to pass, and re-check blind spot before merging.",
                true, "Safe Choice",
                "Staying in your lane until the blind spot is 100% verified clear prevents fatal motorcycle impacts.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s03_c", "Option C",
                "Brake abruptly in your lane to force the rider to overtake you faster.",
                false, "Traffic Flow Disruption",
                "Sudden braking on open roads creates confusion and hazard for following traffic."
            ),
            DecisionOption(
                "s03_d", "Option D",
                "Honk and slowly drift into the left lane expecting the rider to yield to you.",
                false, "Aggressive & Reckless Maneuver",
                "Crowding vulnerable two-wheelers is reckless driving and poses life-threatening risk."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 100,
        hazardIdentified = "Motorcycle traveling in the vehicle's lateral blind zone.",
        safetyPrinciple = "Mirror-Signal-Shoulder Check (MSM / SMOG) — Always perform physical head checks to eliminate blind spots before lateral maneuvers.",
        recommendedAction = "Stay in your current lane, cancel or keep indicator steady, wait for the rider to clear ahead, re-check shoulder, then merge smoothly.",
        topicTag = "Blind-Spot & Lane Changing"
    )

    val scenario04 = SimulationScenario(
        id = "sim_04",
        scenarioNumber = 4,
        title = "04 — Sudden Braking",
        difficulty = SimulationDifficulty.EASY,
        weather = "Overcast",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Forward Collision Buffer AI Active",
        description = "You are cruising at 50 km/h on a national highway maintaining a 3-second gap. The car directly ahead suddenly slams on its brakes with bright brake lights glowing.",
        hazards = listOf(
            "⚠️ Hazard 1: Rapid deceleration of leading vehicle",
            "⚠️ Hazard 2: Following traffic closing speed"
        ),
        prompt = "What is your immediate, safest reaction?",
        options = listOf(
            DecisionOption(
                "s04_a", "Option A (Recommended)",
                "Apply firm, controlled braking in your own lane while monitoring your rearview mirror.",
                true, "Safe Choice",
                "A 3-second buffer allows controlled straight-line stopping without loss of stability or rear-end crashes.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s04_b", "Option B",
                "Immediately swerve onto the road shoulder without checking for pedestrians or obstacles.",
                false, "Off-Road Shoulder Hazard",
                "Blind swerving onto shoulders risks hitting pedestrians, deep ditches, or stationary parked tricycles."
            ),
            DecisionOption(
                "s04_c", "Option C",
                "Swerve into the oncoming traffic lane to bypass braking.",
                false, "Head-On Collision Hazard",
                "Crossing into opposing traffic is the highest-fatality collision mode in road accidents."
            ),
            DecisionOption(
                "s04_d", "Option D",
                "Lightly tap brakes and flash high beams hoping the front car accelerates.",
                false, "Imminent Rear-End Crash",
                "Failing to brake decisively when the lead vehicle stops guarantees an avoidable rear-end collision."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 100,
        hazardIdentified = "Emergency braking by leading vehicle requiring immediate controlled deceleration.",
        safetyPrinciple = "3-Second Following Distance Rule & Controlled Threshold Braking.",
        recommendedAction = "Depress brake pedal firmly in a straight line, keep steering centered, and flash hazards if coming to a dead stop to warn following cars.",
        topicTag = "Following Distance & Emergency Braking"
    )

    val scenario05 = SimulationScenario(
        id = "sim_05",
        scenarioNumber = 5,
        title = "05 — Heavy Rain",
        difficulty = SimulationDifficulty.EASY,
        weather = "Tropical Downpour",
        roadGripReduction = "-40% Grip Loss",
        aiMode = "Hydroplane & Friction Sensor Active",
        description = "A sudden tropical downpour hits the road. Visibility is significantly reduced, water is sheeting across the asphalt, and windshield wipers are on high speed.",
        hazards = listOf(
            "⚠️ Hazard 1: Hydroplaning risk over standing water puddles",
            "⚠️ Hazard 2: Reduced sight distance and 2x longer braking distance"
        ),
        prompt = "What set of driving adjustments must you make?",
        options = listOf(
            DecisionOption(
                "s05_a", "Option A",
                "Turn on hazard emergency flashers and drive at normal highway speed.",
                false, "Misleading Signal Hazard",
                "Using hazard flashers while moving disables turn indicators and confuses surrounding drivers into thinking you are stalled."
            ),
            DecisionOption(
                "s05_b", "Option B (Recommended)",
                "Reduce speed by 20-30%, double following distance, turn on low-beam headlights, and avoid abrupt steering.",
                true, "Safe Choice",
                "Lower speed and increased spacing prevents hydroplaning and gives tires time to displace water through tread grooves.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s05_c", "Option C",
                "Turn on high beams and tailgate the car ahead to follow its tire tracks closely.",
                false, "Blinding Glare & Tailgating",
                "High beams reflect off rain droplets causing white glare back into the driver's eyes, while tailgating removes stopping buffer."
            ),
            DecisionOption(
                "s05_d", "Option D",
                "Brake sharply whenever your tires enter standing water puddles.",
                false, "Hydroplane Skid Trigger",
                "Braking hard directly in puddles locks tires and initiates immediate hydroplaning skids."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 100,
        hazardIdentified = "Reduced tire traction and impaired visibility caused by heavy rain.",
        safetyPrinciple = "Adverse Weather Speed Adjustment & Low-Beam Illumination.",
        recommendedAction = "Slow down smoothly, switch on low beams (not hazards), extend following distance to at least 5-6 seconds, and brake gently in advance.",
        topicTag = "Inclement Weather & Wet Roads"
    )

    val scenario06 = SimulationScenario(
        id = "sim_06",
        scenarioNumber = 6,
        title = "06 — Road Obstruction",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Lane Obstruction & Right-of-Way AI Active",
        description = "A stalled cargo delivery truck and fallen debris partially block your travel lane ahead. Oncoming traffic is approaching in the opposite lane.",
        hazards = listOf(
            "⚠️ Hazard 1: Blocked travel lane requiring detour",
            "⚠️ Hazard 2: Oncoming traffic having unobstructed right-of-way"
        ),
        prompt = "How should you safely navigate past this obstruction?",
        options = listOf(
            DecisionOption(
                "s06_a", "Option A",
                "Speed up and squeeze past the obstacle before the oncoming vehicle arrives.",
                false, "Pinch Point Collision",
                "Rushing into a narrowed bottleneck against oncoming traffic leads to head-on side collisions."
            ),
            DecisionOption(
                "s06_b", "Option B (Recommended)",
                "Slow down, stop behind the obstruction in your lane, yield to oncoming traffic, and pass only when clear.",
                true, "Safe Choice",
                "The driver whose lane is obstructed must always yield right-of-way to traffic in the unobstructed oncoming lane.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s06_c", "Option C",
                "Honk continuously and force oncoming vehicles to stop for you.",
                false, "Aggressive Road Conflict",
                "Demanding false right-of-way causes road rage and head-on gridlock."
            ),
            DecisionOption(
                "s06_d", "Option D",
                "Drive up onto the pedestrian sidewalk to bypass the stalled truck.",
                false, "Illegal Sidewalk Intrusion",
                "Mounting sidewalks endangers pedestrians and is strictly prohibited under traffic regulations."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Partial lane blockage with oncoming traffic having unobstructed priority.",
        safetyPrinciple = "Obstruction Priority Rule — Traffic facing an obstruction must yield right-of-way to oncoming vehicles.",
        recommendedAction = "Decelerate early, stop with ample room behind the stalled truck, check opposing lane, signal left when clear, and pass with caution.",
        topicTag = "Road Obstructions & Maneuvering"
    )

    val scenario07 = SimulationScenario(
        id = "sim_07",
        scenarioNumber = 7,
        title = "07 — Emergency Vehicle",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Acoustic Siren & Emergency Strobe Monitor Active",
        description = "An ambulance with flashing red and blue strobe lights and loud emergency sirens is rapidly approaching from behind in your lane.",
        hazards = listOf(
            "⚠️ Hazard 1: High-speed emergency vehicle requiring unimpeded passage",
            "⚠️ Hazard 2: Neighboring vehicles abruptly maneuvering to yield"
        ),
        prompt = "What is your legal obligation and safest maneuver?",
        options = listOf(
            DecisionOption(
                "s07_a", "Option A",
                "Stop dead in the center of your travel lane immediately.",
                false, "Path Blockage Hazard",
                "Stopping dead in the lane blocks the ambulance's route rather than opening up an unobstructed path."
            ),
            DecisionOption(
                "s07_b", "Option B",
                "Accelerate to outrun the ambulance until you find a convenient turn-off.",
                false, "Dangerous Delay & Speeding",
                "Racing ahead of emergency vehicles delays critical medical assistance and endangers traffic."
            ),
            DecisionOption(
                "s07_c", "Option C (Recommended)",
                "Signal right, smoothly pull over as close as possible to the right curb/shoulder, and come to a stop.",
                true, "Safe Choice",
                "R.A. 4136 Section 49 mandates drivers to immediately pull to the right curb and stop upon the approach of emergency vehicles.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s07_d", "Option D",
                "Tailgate closely behind the ambulance to bypass heavy traffic.",
                false, "Illegal Emergency Convoy",
                "Following within 150 meters of an active emergency vehicle is illegal and causes severe rear-end crashes."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Active emergency response vehicle requiring clear right-side corridor.",
        safetyPrinciple = "R.A. 4136 Sec. 49 (Duty on Approach of Authorized Emergency Vehicles) — Yield right curb immediately.",
        recommendedAction = "Check your right mirror, signal right, move smoothly to the curb or outer shoulder, stop completely, and remain stationary until the vehicle clears.",
        topicTag = "Emergency Vehicle Awareness"
    )

    val scenario08 = SimulationScenario(
        id = "sim_08",
        scenarioNumber = 8,
        title = "08 — Unsafe Overtaking",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Sightline & Road Geometry Assessment Active",
        description = "You are following a slow-moving agricultural tricycle on an uphill winding provincial road with a solid double yellow center line and an upcoming blind curve.",
        hazards = listOf(
            "⚠️ Hazard 1: Blind curve with zero oncoming sight distance",
            "⚠️ Hazard 2: Solid double yellow no-overtaking pavement markings"
        ),
        prompt = "What is the only safe and lawful decision?",
        options = listOf(
            DecisionOption(
                "s08_a", "Option A",
                "Cross the double yellow line quickly to overtake while the tricycle is slow.",
                false, "High Risk Head-On Crash",
                "Crossing solid double yellow lines on blind curves is one of the most fatal traffic violations."
            ),
            DecisionOption(
                "s08_b", "Option B (Recommended)",
                "Maintain a safe following distance, stay in your lane, and wait for a clear broken-line passing zone.",
                true, "Safe Choice",
                "Patience and obedience to double yellow lines guarantees zero risk of oncoming head-on impacts.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s08_c", "Option C",
                "Tailgate inches behind the tricycle and honk until the driver pulls into the ditch.",
                false, "Tailgating & Intimidation",
                "Intimidating slow road users creates panic and causes rollovers or sudden crashes."
            ),
            DecisionOption(
                "s08_d", "Option D",
                "Overtake on the unpaved right dirt shoulder around the tricycle.",
                false, "Illegal Shoulder Passing",
                "Overtaking on the right shoulder is illegal and risks ditch rollovers and hitting hidden pedestrians."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Blind curve with zero sightline and solid double yellow no-passing lines.",
        safetyPrinciple = "Pavement Markings Compliance — Solid double yellow lines strictly prohibit overtaking from either direction.",
        recommendedAction = "Drop back to maintain clear vision, stay firmly within your lane, and wait until you reach a straight section with broken white/yellow markings and clear visibility.",
        topicTag = "Safe Overtaking & Road Markings"
    )

    val scenario09 = SimulationScenario(
        id = "sim_09",
        scenarioNumber = 9,
        title = "09 — School Zone",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear Morning",
        roadGripReduction = "0% Grip Loss",
        aiMode = "School Zone Pedestrian Proximity Active",
        description = "You are approaching a public elementary school zone during morning drop-off hours. Yellow school zone warning signs are posted, and children are walking along the roadside.",
        hazards = listOf(
            "⚠️ Hazard 1: Unpredictable child pedestrian movements near roadways",
            "⚠️ Hazard 2: Congested drop-off tricycles and passenger vans"
        ),
        prompt = "How should you adjust your driving behavior in this school zone?",
        options = listOf(
            DecisionOption(
                "s09_a", "Option A",
                "Maintain 40 km/h and honk continuously to make children step back.",
                false, "Child Panic Hazard",
                "Loud horns startle young children, causing unpredictable running reactions into the road."
            ),
            DecisionOption(
                "s09_b", "Option B (Recommended)",
                "Reduce speed to 20 km/h or lower, scan sidewalks and between parked vehicles, and cover the brake pedal.",
                true, "Safe Choice",
                "R.A. 4136 mandates a maximum speed limit of 20 km/h in school zones to ensure instantaneous stopping capability.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s09_c", "Option C",
                "Overtake parked drop-off tricycles quickly to clear the school zone faster.",
                false, "Severe Blind Spot Impact",
                "Overtaking near school gates blinds you to children crossing directly in front of parked transport."
            ),
            DecisionOption(
                "s09_d", "Option D",
                "Focus solely on the bumper of the car ahead and disregard the sidewalk.",
                false, "Tunnel Vision Hazard",
                "Tunnel vision prevents early perception of children stepping off curbs."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "High concentration of vulnerable child pedestrians in designated school area.",
        safetyPrinciple = "School Zone Speed Limits (R.A. 4136 Max 20 km/h) & Defensive Scanning.",
        recommendedAction = "Decelerate to 20 km/h or below, keep foot hovering over the brake pedal (cover braking), and scan under and between parked vehicles for small feet.",
        topicTag = "School Zones & Child Safety"
    )

    val scenario10 = SimulationScenario(
        id = "sim_10",
        scenarioNumber = 10,
        title = "10 — Motorcycle Traffic",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Multi-Rider Spatial Cushioning AI Active",
        description = "You are driving in dense urban traffic surrounded by multiple motorcycles lane-filtering and riding closely on both your left and right sides.",
        hazards = listOf(
            "⚠️ Hazard 1: Motorcycles filtering through tight lateral blind zones",
            "⚠️ Hazard 2: Sudden speed changes in congested traffic"
        ),
        prompt = "What is the best defensive driving strategy?",
        options = listOf(
            DecisionOption(
                "s10_a", "Option A",
                "Weave left and right within your lane to block motorcycles from filtering past you.",
                false, "Aggressive Collision Hazard",
                "Intentional weaving causes catastrophic side-swipe collisions with riders."
            ),
            DecisionOption(
                "s10_b", "Option B (Recommended)",
                "Maintain a predictable central lane position, avoid sudden swerves, and signal well in advance.",
                true, "Safe Choice",
                "Predictable lane positioning and early signaling give two-wheeled motorists time to react safely.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s10_c", "Option C",
                "Open your vehicle door slightly to block riders passing on the right.",
                false, "Illegal & Lethal Act",
                "Dooring riders in moving traffic is criminal recklessness resulting in severe injury or death."
            ),
            DecisionOption(
                "s10_d", "Option D",
                "Accelerate aggressively whenever a gap opens to stay ahead of all bikes.",
                false, "Erratic Acceleration Risk",
                "Aggressive bursts in heavy traffic lead to multi-vehicle chain collisions."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "High-density two-wheeler traffic in close lateral proximity.",
        safetyPrinciple = "Lane Discipline & Predictability — Maintain steady positioning and clear signaling around vulnerable road users.",
        recommendedAction = "Keep your vehicle centered in the lane, scan mirrors frequently, avoid sudden steering inputs, and signal at least 30 meters before any turn.",
        topicTag = "Motorcycle Awareness & Coexistence"
    )

    val scenario11 = SimulationScenario(
        id = "sim_11",
        scenarioNumber = 11,
        title = "11 — Intersection Conflict",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Unsignalized Intersection Conflict AI Active",
        description = "You are approaching an uncontrolled 4-way intersection without traffic lights. A vehicle approaching rapidly from your left fails to slow down.",
        hazards = listOf(
            "⚠️ Hazard 1: Cross-traffic vehicle failing to yield at unsignalized junction",
            "⚠️ Hazard 2: Risk of severe broadside T-bone impact"
        ),
        prompt = "Even if you technically have right-of-way from the right, what should you do?",
        options = listOf(
            DecisionOption(
                "s11_a", "Option A",
                "Accelerate into the intersection to assert your legal right-of-way.",
                false, "Broadside T-Bone Crash",
                "Insisting on technical right-of-way against a vehicle that is not stopping guarantees a severe collision."
            ),
            DecisionOption(
                "s11_b", "Option B (Recommended)",
                "Slow down, prepare to stop, and yield to avoid the collision despite having technical priority.",
                true, "Safe Choice",
                "Defensive driving principle: 'Right-of-way is something to be given, not taken.' Avoiding a crash takes priority.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s11_c", "Option C",
                "Close your eyes, honk your horn, and maintain current cruising speed.",
                false, "Extreme Recklessness",
                "Horns do not physically stop another vehicle from entering your path."
            ),
            DecisionOption(
                "s11_d", "Option D",
                "Swerve hard into the sidewalk corner to dodge the other vehicle.",
                false, "Infrastructure & Pedestrian Impact",
                "Swerving onto corners endangers pedestrians and causes rollover crashes."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Cross-traffic vehicle disregarding right-of-way at uncontrolled intersection.",
        safetyPrinciple = "Defensive Priority Rule — Never insist on right-of-way if it compromises safety; prioritize crash avoidance.",
        recommendedAction = "Release throttle, apply brakes, allow the non-yielding vehicle to cross clear, re-scan all 4 approaches, and proceed only when 100% safe.",
        topicTag = "Intersection Safety & Right-of-Way"
    )

    val scenario12 = SimulationScenario(
        id = "sim_12",
        scenarioNumber = 12,
        title = "12 — Night Driving",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Dark Rural Night",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Night Visibility & Glare Mitigation Active",
        description = "Driving on an unlit rural national highway at night. An oncoming truck's headlights are approaching, while an unlit pedestrian is walking on your right shoulder.",
        hazards = listOf(
            "⚠️ Hazard 1: Glare from oncoming high-beam headlights blinding night vision",
            "⚠️ Hazard 2: Unlit pedestrian walking along dark shoulder"
        ),
        prompt = "What is the safest nighttime driving practice?",
        options = listOf(
            DecisionOption(
                "s12_a", "Option A",
                "Stare directly into the oncoming headlights to verify the vehicle's position.",
                false, "Flash Blindness Hazard",
                "Looking directly into bright beams causes retinal bleaching and temporary flash blindness for 3 to 7 seconds."
            ),
            DecisionOption(
                "s12_b", "Option B",
                "Turn on your own high beams to retaliate against the oncoming truck.",
                false, "Mutual Blindness Disaster",
                "Blinding oncoming drivers increases the chance of them drifting directly into your lane head-on."
            ),
            DecisionOption(
                "s12_c", "Option C (Recommended)",
                "Switch to low beams, reduce speed, and focus your gaze toward the white fog line on the right shoulder.",
                true, "Safe Choice",
                "Focusing on the right white line prevents glare blindness while keeping the pedestrian on the shoulder visible.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s12_d", "Option D",
                "Turn off your headlights momentarily to let your eyes adjust to the darkness.",
                false, "Blind Driving Hazard",
                "Extinguishing headlights while driving at night creates total blindness and fatal crash conditions."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Headlight glare combined with unlit shoulder pedestrian in low ambient light.",
        safetyPrinciple = "Night Driving & Glare Mitigation Protocol — Low beam courtesy and peripheral edge tracking.",
        recommendedAction = "Dim high beams to low beams within 150m of oncoming traffic, ease off throttle to match headlight illumination range, and track the right edge line.",
        topicTag = "Night Driving & Glare Management"
    )

    val scenario13 = SimulationScenario(
        id = "sim_13",
        scenarioNumber = 13,
        title = "13 — Distracted Driving",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Cognitive Distraction & Phone Law Monitor Active",
        description = "While driving at 50 km/h in moderate traffic, your smartphone rings with an urgent incoming message notification mounted on the dashboard holder.",
        hazards = listOf(
            "⚠️ Hazard 1: Cognitive, visual, and manual driver distraction",
            "⚠️ Hazard 2: Rapid traffic deceleration while eyes are off the road"
        ),
        prompt = "Under Philippine Law (R.A. 10913 - Anti-Distracted Driving Act), what must you do?",
        options = listOf(
            DecisionOption(
                "s13_a", "Option A",
                "Quickly glance down and text a 1-word reply with one hand on the wheel.",
                false, "R.A. 10913 Violation & Crash Risk",
                "At 50 km/h, looking at a screen for just 3 seconds means traveling 42 meters completely blind."
            ),
            DecisionOption(
                "s13_b", "Option B (Recommended)",
                "Ignore the phone completely while vehicle is in motion, or safely pull over into a legal parking area before checking.",
                true, "Safe Choice",
                "Zero mobile interaction while driving eliminates distraction and ensures full legal compliance under R.A. 10913.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s13_c", "Option C",
                "Hold the phone up directly in front of your eyes so you can see both screen and road.",
                false, "Illegal Field-of-View Obstruction",
                "R.A. 10913 strictly prohibits positioning mobile screens in the driver's line of sight."
            ),
            DecisionOption(
                "s13_d", "Option D",
                "Have a rear passenger reach over your shoulder to hold the phone while you reply.",
                false, "Physical Cockpit Interference",
                "Passenger reaching across the driver impairs vehicle steering and control."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Mobile phone distraction compromising situational awareness and reaction time.",
        safetyPrinciple = "R.A. 10913 (Anti-Distracted Driving Act) — Total prohibition of mobile device usage while operating a motor vehicle.",
        recommendedAction = "Keep both hands on the wheel and eyes on the road. If the call is an emergency, pull completely off the roadway into a designated parking spot.",
        topicTag = "Distracted Driving & R.A. 10913"
    )

    val scenario14 = SimulationScenario(
        id = "sim_14",
        scenarioNumber = 14,
        title = "14 — Fatigued Driving",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Late Night",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Drowsiness & Alertness Tracking Active",
        description = "You have been driving continuously for over 4 hours late at night. You find yourself yawning repeatedly, eyes stinging, and your car drifting onto the rumble strip.",
        hazards = listOf(
            "⚠️ Hazard 1: Microsleep episodes and involuntary loss of consciousness",
            "⚠️ Hazard 2: High-speed roadway departure or rear-end impact"
        ),
        prompt = "What is the only medically and practically effective solution for driver fatigue?",
        options = listOf(
            DecisionOption(
                "s14_a", "Option A",
                "Roll down all windows and blast loud radio music.",
                false, "Ineffective Temporary Gimmick",
                "Fresh air and music cannot overcome biological brain sleep pressure; microsleeps will still occur."
            ),
            DecisionOption(
                "s14_b", "Option B",
                "Drink an energy drink and speed up to reach your destination before falling asleep.",
                false, "High-Speed Fatigue Hazard",
                "Speeding while fatigued drastically increases crash severity when a microsleep happens."
            ),
            DecisionOption(
                "s14_c", "Option C (Recommended)",
                "Signal, exit at the nearest gas station / rest stop, park safely, and take a 20-30 minute power nap.",
                true, "Safe Choice",
                "Sleep is the only biological cure for fatigue. A short power nap restores cognitive alertness and reaction speed.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s14_d", "Option D",
                "Slap your face periodically and continue driving in the leftmost express lane.",
                false, "Fatal Road Departure Risk",
                "Fighting severe sleepiness leads to sudden lane drifting at fatal expressway speeds."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Driver drowsiness and microsleep onset causing loss of vehicle tracking.",
        safetyPrinciple = "Fatigue Management Protocol — Recognize early sleep warning signs and take immediate restorative rest.",
        recommendedAction = "Take the next exit or pull into a well-lit service station, lock doors, recline seat, and rest for 20-30 minutes before resuming travel.",
        topicTag = "Driver Fatigue & Alertness"
    )

    val scenario15 = SimulationScenario(
        id = "sim_15",
        scenarioNumber = 15,
        title = "15 — Traffic Sign Recognition",
        difficulty = SimulationDifficulty.MEDIUM,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Regulatory Sign & Signal Recognition Active",
        description = "You are in the rightmost lane intending to make a right turn on a red signal. Beside the traffic light, a regulatory signboard states: 'NO RIGHT TURN ON RED SIGNAL'.",
        hazards = listOf(
            "⚠️ Hazard 1: Conflicting pedestrian crossing phase during red signal",
            "⚠️ Hazard 2: Protected cross-traffic turning stream"
        ),
        prompt = "What action is legally required by this regulatory sign?",
        options = listOf(
            DecisionOption(
                "s15_a", "Option A",
                "Turn right anyway if no cross-traffic or traffic enforcers are visible.",
                false, "Red Light Violation",
                "Disregarding a posted regulatory prohibition sign constitutes an illegal turn on red violation."
            ),
            DecisionOption(
                "s15_b", "Option B (Recommended)",
                "Come to a complete stop before the stop line and remain stopped until the green light/arrow turns on.",
                true, "Safe Choice",
                "Regulatory signs are mandatory traffic laws. Remaining stopped protects pedestrians and cross-traffic.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s15_c", "Option C",
                "Honk twice and perform a slow rolling right turn without stopping.",
                false, "Pedestrian Collision Risk",
                "Rolling turns on red ignore crossing pedestrians who have the walk signal."
            ),
            DecisionOption(
                "s15_d", "Option D",
                "Turn on hazard emergency lights to justify executing the right turn.",
                false, "Illegal Maneuver",
                "Hazard lights do not grant legal exemption from regulatory traffic signs."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 120,
        hazardIdentified = "Regulatory prohibition sign restricting right turns during the red light phase.",
        safetyPrinciple = "DPWH / LTO Regulatory Traffic Sign Compliance — Signs with red borders or black text are mandatory commands.",
        recommendedAction = "Stop completely before the white stop line, keep turn signal active, and wait patiently for the green phase before initiating your turn.",
        topicTag = "Traffic Signs & Regulatory Markings"
    )

    val scenario16 = SimulationScenario(
        id = "sim_16",
        scenarioNumber = 16,
        title = "16 — Slippery Road",
        difficulty = SimulationDifficulty.HARD,
        weather = "Post-Rain Oil Slick",
        roadGripReduction = "-50% Grip Loss",
        aiMode = "Traction Dynamics & Oversteer Sensor Active",
        description = "You enter an asphalt curve shortly after a light shower that brought surface oil and grease up. The rear tires begin to break traction and slide outward (oversteer).",
        hazards = listOf(
            "⚠️ Hazard 1: Loss of lateral tire grip causing vehicle fishtailing",
            "⚠️ Hazard 2: Potential complete spin-out into opposing oncoming lane"
        ),
        prompt = "How do you regain steering control and prevent a catastrophic spin-out?",
        options = listOf(
            DecisionOption(
                "s16_a", "Option A",
                "Slam the brake pedal to the floor and yank the steering wheel hard in the opposite direction.",
                false, "Violent 360 Spin-Out",
                "Slamming brakes while skidding locks all 4 wheels, eliminating all steering capability and accelerating the spin."
            ),
            DecisionOption(
                "s16_b", "Option B (Recommended)",
                "Ease smoothly off the accelerator, steer gently in the direction you want to go (counter-steer), and avoid harsh braking.",
                true, "Safe Choice",
                "Releasing the throttle transfers weight forward smoothly, while gentle steering into the skid allows tires to regain rolling traction.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s16_c", "Option C",
                "Floor the accelerator pedal to power through the curve.",
                false, "Total Loss of Traction",
                "Applying heavy throttle during a skid overwhelms remaining grip and guarantees an immediate spin-out."
            ),
            DecisionOption(
                "s16_d", "Option D",
                "Yank the emergency handbrake immediately.",
                false, "Instant Lockup & Rollover",
                "Pulling the handbrake locks the rear wheels instantly, causing an uncontrollable spin."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 150,
        hazardIdentified = "Oversteer skid on low-friction oily roadway curve.",
        safetyPrinciple = "Skid Recovery Protocol — Counter-steer smoothly in the intended direction of travel without sudden braking or acceleration.",
        recommendedAction = "Do not touch the brake pedal; ease off the gas pedal, steer smoothly into the direction of the skid, and re-center the wheel as grip returns.",
        topicTag = "Skid Control & Vehicle Dynamics"
    )

    val scenario17 = SimulationScenario(
        id = "sim_17",
        scenarioNumber = 17,
        title = "17 — Aggressive Driver",
        difficulty = SimulationDifficulty.HARD,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "De-escalation & Spatial Cushioning AI Active",
        description = "An aggressive SUV driver is tailgating inches from your rear bumper, flashing high beams, and honking aggressively to force you to exceed the speed limit.",
        hazards = listOf(
            "⚠️ Hazard 1: Severe tailgating with zero rear stopping buffer",
            "⚠️ Hazard 2: Escalating aggressive road rage behavior"
        ),
        prompt = "What is the safest defensive method to de-escalate this road conflict?",
        options = listOf(
            DecisionOption(
                "s17_a", "Option A",
                "Brake check the aggressive vehicle abruptly to teach the driver a lesson.",
                false, "High-Speed Collision & Road Rage",
                "Brake checking is illegal, causes high-speed rear-end pileups, and triggers violent physical altercations."
            ),
            DecisionOption(
                "s17_b", "Option B (Recommended)",
                "Maintain emotional control, signal right, safely change to the slower lane when clear, and let the vehicle pass.",
                true, "Safe Choice",
                "De-escalating by giving way removes the hazard from your rear buffer and prevents dangerous road rage incidents.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s17_c", "Option C",
                "Match the driver's speed, roll down your window, and exchange angry gestures.",
                false, "Violent Road Conflict",
                "Engaging in road rage escalates tension and causes distracted driving collisions."
            ),
            DecisionOption(
                "s17_d", "Option D",
                "Block the lane deliberately and slow down to enforce the speed limit yourself.",
                false, "Provocation Hazard",
                "Vigilante lane blocking provokes reckless aggressive overtaking maneuvers."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 150,
        hazardIdentified = "Aggressive tailgater and potential road rage escalation.",
        safetyPrinciple = "Road Rage De-escalation & Defensive Yielding — Do not engage; allow aggressive motorists to pass safely.",
        recommendedAction = "Keep calm, avoid eye contact, signal right, transition safely to the rightmost lane, and let the aggressive driver proceed.",
        topicTag = "Defensive Driving & Road Rage De-escalation"
    )

    val scenario18 = SimulationScenario(
        id = "sim_18",
        scenarioNumber = 18,
        title = "18 — Sudden Pedestrian Hazard",
        difficulty = SimulationDifficulty.HARD,
        weather = "Clear",
        roadGripReduction = "0% Grip Loss",
        aiMode = "Blind Zone Evasive Threshold AI Active",
        description = "You are passing a stopped passenger jeepney in the curb lane. Suddenly, a pedestrian steps out directly from in front of the jeepney into your lane.",
        hazards = listOf(
            "⚠️ Hazard 1: Concealed pedestrian emerging from jeepney blind zone",
            "⚠️ Hazard 2: Extremely compressed perception-reaction distance"
        ),
        prompt = "What is your critical split-second evasive action?",
        options = listOf(
            DecisionOption(
                "s18_a", "Option A (Recommended)",
                "Apply maximum threshold braking in your lane while gripping the wheel firmly and scanning escape paths.",
                true, "Safe Choice",
                "Immediate straight-line threshold braking maximizes stopping distance reduction without losing vehicle control.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s18_b", "Option B",
                "Swerve blindly into oncoming traffic to avoid braking.",
                false, "Catastrophic Head-On Crash",
                "Blind swerving into opposing traffic turns a single-lane hazard into a fatal multi-car head-on collision."
            ),
            DecisionOption(
                "s18_c", "Option C",
                "Honk horn and maintain speed, expecting the pedestrian to jump back.",
                false, "Fatal Pedestrian Impact",
                "Pedestrians often freeze in fear when startled by horns at close range."
            ),
            DecisionOption(
                "s18_d", "Option D",
                "Accelerate to squeeze past before the pedestrian takes another step.",
                false, "Direct Impact Collision",
                "Accelerating toward crossing pedestrians guarantees severe collision impact."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 150,
        hazardIdentified = "Pedestrian concealed by public utility vehicle stepping directly into travel path.",
        safetyPrinciple = "Public Transport Blind Zone Protocol — Always anticipate pedestrians stepping out from behind stopped jeepneys/buses.",
        recommendedAction = "Apply immediate emergency braking in a straight line with ABS engaged, maintain firm two-handed grip, and sound horn only to alert.",
        topicTag = "Pedestrian Hazard Anticipation"
    )

    val scenario19 = SimulationScenario(
        id = "sim_19",
        scenarioNumber = 19,
        title = "19 — Vehicle/Tire Problem",
        difficulty = SimulationDifficulty.HARD,
        weather = "Clear",
        roadGripReduction = "-30% Grip Loss",
        aiMode = "Blowout Stability & Emergency Procedure Active",
        description = "While driving at 80 km/h on an expressway, you hear a loud 'BANG', the steering wheel pulls violently to the left, and your front-left tire blows out.",
        hazards = listOf(
            "⚠️ Hazard 1: Sudden loss of directional steering stability",
            "⚠️ Hazard 2: High rollover risk upon harsh brake application"
        ),
        prompt = "What is the proper emergency procedure to maintain vehicle stability?",
        options = listOf(
            DecisionOption(
                "s19_a", "Option A",
                "Slam the brake pedal as hard as possible and jerk the wheel toward the right shoulder.",
                false, "Violent Rollover Hazard",
                "Hard braking on a blown tire causes the wheel rim to dig into the asphalt, flipping the vehicle."
            ),
            DecisionOption(
                "s19_b", "Option B (Recommended)",
                "Grip the steering wheel firmly with both hands, ease off accelerator smoothly, avoid hard braking, and guide vehicle to shoulder as speed drops.",
                true, "Safe Choice",
                "Steering stability and natural engine deceleration prevent tire blowout rollovers and allow controlled shoulder stopping.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s19_c", "Option C",
                "Shift immediately into Reverse or Park to stop the vehicle instantly.",
                false, "Transmission Destruction & Wheel Lock",
                "Shifting into Park/Reverse at 80 km/h destroys the transmission and locks the drive wheels."
            ),
            DecisionOption(
                "s19_d", "Option D",
                "Accelerate heavily to keep the blown tire spinning evenly on the rim.",
                false, "Total Loss of Control",
                "Accelerating on a shredded tire tears away suspension components and causes immediate loss of control."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 150,
        hazardIdentified = "High-speed front tire blowout inducing severe directional pull.",
        safetyPrinciple = "Tire Blowout Emergency Protocol — Maintain steering counter-force, do not brake abruptly, let vehicle decelerate naturally.",
        recommendedAction = "Grip steering wheel firmly at 9 and 3 o'clock, smoothly release the throttle, apply light braking only after speed drops below 40 km/h, and coast to the emergency shoulder.",
        topicTag = "Emergency Maneuvers & Vehicle Failures"
    )

    val scenario20 = SimulationScenario(
        id = "sim_20",
        scenarioNumber = 20,
        title = "20 — Complex Road-Safety Scenario",
        difficulty = SimulationDifficulty.HARD,
        weather = "Heavy Rain · Dusk · Low Visibility",
        roadGripReduction = "-45% Grip Loss",
        aiMode = "Compound Multi-Hazard Prioritization Engine Active",
        description = "Approaching a busy unsignalized Philippine intersection in heavy rain at dusk. A passenger jeepney is unloading on the right curb, two motorcycles are lane-splitting on your left, and an umbrella-carrying pedestrian is crossing ahead.",
        hazards = listOf(
            "⚠️ Hazard 1: Compound low road grip and low dusk visibility",
            "⚠️ Hazard 2: Crossing pedestrian with umbrella obstructing vision",
            "⚠️ Hazard 3: Filtering motorcycles on left and unloading jeepney on right"
        ),
        prompt = "How do you prioritize risks and execute the safest sequence of actions?",
        options = listOf(
            DecisionOption(
                "s20_a", "Option A",
                "Speed up through the intersection to get out of the dangerous multi-hazard area as quickly as possible.",
                false, "Catastrophic Multi-Vehicle Crash",
                "Rushing through complex compounding hazards triggers uncontrollable chain-reaction collisions."
            ),
            DecisionOption(
                "s20_b", "Option B (Recommended)",
                "Decelerate smoothly to low speed, increase space buffers, yield priority to the crossing pedestrian first, and monitor both mirrors for swerving motorcycles.",
                true, "Safe Choice",
                "Prioritizing the most vulnerable road user (pedestrian) while dropping speed gives you time to manage all compounding risks safely.",
                SimulationTier.PERFECT_SIMULATION, true
            ),
            DecisionOption(
                "s20_c", "Option C",
                "Honk continuously, turn on high beams, and force everyone else to freeze for you.",
                false, "Sensory Overload & Panic",
                "Blinding high beams and constant honking induces panic and unpredictability in surrounding motorists."
            ),
            DecisionOption(
                "s20_d", "Option D",
                "Swerve left around the jeepney without checking left side mirror for motorcycles.",
                false, "Motorcycle Side Impact",
                "Blind swerving around public vehicles causes direct rider collisions with filtering motorcycles."
            )
        ),
        timeLimitSeconds = 20,
        xpReward = 200,
        hazardIdentified = "Compound multi-hazard environment combining rain, low visibility, crossing pedestrians, unloading jeepneys, and filtering motorcycles.",
        safetyPrinciple = "Hierarchical Risk Prioritization & Defensive Space Cushioning in Complex Philippine Traffic Environments.",
        recommendedAction = "1. Reduce speed to crawling pace. 2. Yield first to the vulnerable pedestrian. 3. Maintain lane position without swerving into motorcycle paths. 4. Clear the intersection smoothly.",
        topicTag = "Compound Risk Prioritization & Defensive Mastery"
    )

    val all: List<SimulationScenario> = listOf(
        scenario01, scenario02, scenario03, scenario04, scenario05,
        scenario06, scenario07, scenario08, scenario09, scenario10,
        scenario11, scenario12, scenario13, scenario14, scenario15,
        scenario16, scenario17, scenario18, scenario19, scenario20
    )
}
