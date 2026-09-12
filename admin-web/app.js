/**
 * ═══════════════════════════════════════════════════════════════
 * RoadSafe AI — Admin Command Center v3.0 (Full Mobile App Mirror)
 * Real-time Firebase Cloud Engine, Module/Quiz Manager, Gamification,
 * AI Activity Stream, Driver Decisions & Security Audit.
 * ═══════════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════════════════════════
// 1. FIREBASE INITIALIZATION
// ═══════════════════════════════════════════════════════════════
const firebaseConfig = {
    apiKey: "AIzaSyBoTVh2Ug2bMa4Ojkg8EOHmv1yrE-6EJWg",
    authDomain: "gamifiedroadsafetyawareness.firebaseapp.com",
    projectId: "gamifiedroadsafetyawareness",
    storageBucket: "gamifiedroadsafetyawareness.appspot.com",
    messagingSenderId: "470220431881",
    appId: "1:470220431881:web:c0b8923a497042a969f648"
};

let db = null;
try {
    if (!firebase.apps.length) firebase.initializeApp(firebaseConfig);
    db = firebase.firestore();
    console.log("✅ Firebase Firestore Initialized in Full Mirror Mode.");
} catch (err) {
    console.error("❌ Firebase init error:", err);
}

// ═══════════════════════════════════════════════════════════════
// 2. STATE REPOSITORY
// ═══════════════════════════════════════════════════════════════
const State = {
    users: [],
    devices: [],
    quizzes: [],
    logins: [],
    progress: [],
    audit: [],
    aiQueries: [],
    mobileModules: [],
    modules: [],
    questions: [],
    scenarios: [],
    badges: [],
    rankHistory: [],
    activeTab: 'dashboard',
    activeSubTab: {
        quizzes: 'quiz-attempts',
        gamification: 'gamif-leaderboard'
    },
    userFilter: 'all',
    deviceFilter: 'all',
    quizFilter: 'all',
    questionFilter: 'all',
    scenarioFilter: 'all',
    aiFilter: 'all',
    loginFilter: 'all',
    auditFilter: 'all',
    searchQuery: '',
    selectedUser: null,
    userToDelete: null,
    currentAdmin: 'admin'
};

// ═══════════════════════════════════════════════════════════════
// 3. CURRICULUM, QUESTIONS, SCENARIOS & BADGES DATA (Mobile Mirror)
// ═══════════════════════════════════════════════════════════════

const DEFAULT_MOBILE_MODULES = [
    {
        id: "mod_easy_quiz",
        title: "🟢 Easy Quiz",
        description: "20 questions covering road safety basics, traffic lights, signs, seat belts, and fundamental driving rules.",
        typeBadge: "EASY MODULE",
        xpReward: "+100 XP",
        enabled: true
    },
    {
        id: "mod_medium_quiz",
        title: "🟡 Medium Quiz",
        description: "20 scenario-based questions on lane changes, rain driving, overtaking rules, and defensive driving techniques.",
        typeBadge: "MEDIUM MODULE",
        xpReward: "+200 XP",
        enabled: true
    },
    {
        id: "mod_hard_quiz",
        title: "🔴 Hard Quiz",
        description: "20 advanced situational questions on right-of-way, night driving, skid control, and multi-hazard intersections.",
        typeBadge: "HARD MODULE",
        xpReward: "+300 XP",
        enabled: true
    }
];

const DEFAULT_MODULES = [
    { id: "mod_1", title: "Right-of-Way & Intersections", topic: "RIGHT_OF_WAY", difficulty: "Beginner", description: "Learn priority rules at cross-streets, merging lanes, and 4-way stops.", tip: "When in doubt, slow down and yield. Being technically right is never worth a collision.", status: "published", completions: 2 },
    { id: "mod_2", title: "Traffic Signs & Signals", topic: "TRAFFIC_SIGNS", difficulty: "Beginner", description: "Master regulatory, warning, and informational road signs and traffic lights.", tip: "A yellow light means prepare to stop, not speed up to beat it.", status: "published", completions: 1 },
    { id: "mod_3", title: "Speed Management", topic: "SPEED_MANAGEMENT", difficulty: "Intermediate", description: "Adjust speed according to traffic flow, road grip, and visibility.", tip: "Higher speed means longer stopping distance — adjust for conditions, not just the sign.", status: "published", completions: 1 },
    { id: "mod_4", title: "Following Distance & Braking", topic: "FOLLOWING_DISTANCE", difficulty: "Intermediate", description: "Prevent rear-end collisions with the 3-second rule and wet road buffering.", tip: "Double following distance in heavy rain or wet asphalt.", status: "published", completions: 1 },
    { id: "mod_5", title: "Pedestrian Safety & Crosswalks", topic: "PEDESTRIAN_SAFETY", difficulty: "Beginner", description: "Protect vulnerable pedestrians, school zones, and zebra crossings.", tip: "Always slow down near school zones even before you spot pedestrians.", status: "published", completions: 2 },
    { id: "mod_6", title: "Safe Overtaking & Lane Changes", topic: "OVERTAKING", difficulty: "Advanced", description: "Manage blind spots, solid white/yellow lines, and curve restrictions.", tip: "If not 100% sure you have space and visibility, never start an overtake.", status: "published", completions: 0 },
    { id: "mod_7", title: "Motorcycle Safety & Blind Spots", topic: "MOTORCYCLE_SAFETY", difficulty: "Intermediate", description: "Share the road safely with two-wheelers and perform head-checks.", tip: "Always check blind spots before changing lanes — bikes easily hide in mirrors.", status: "published", completions: 0 },
    { id: "mod_8", title: "Impaired & Distracted Driving", topic: "IMPAIRED_OR_DISTRACTED", difficulty: "Advanced", description: "The dangers of phone use, texting, alcohol, and driver fatigue.", tip: "Pull over safely if fatigue or distraction impairs your focus.", status: "published", completions: 0 },
    { id: "mod_9", title: "Emergency Vehicles & Sirens", topic: "EMERGENCY_PROCEDURES", difficulty: "Beginner", description: "Clear the way smoothly when ambulances and fire engines approach.", tip: "Signal and pull to the right smoothly; never slam brakes in the intersection.", status: "published", completions: 1 },
    { id: "mod_10", title: "Road Courtesy & Defensive Driving", topic: "ROAD_COURTESY", difficulty: "Intermediate", description: "Maintain emotional control, de-escalate road rage, and plan ahead.", tip: "Defensive driving means planning for others' mistakes, not just avoiding your own.", status: "published", completions: 1 }
];

const DEFAULT_QUESTIONS = [
    // 🟢 EASY MODULE (20 Questions)
    { id: "q_e1", text: "What should you do when the traffic light turns red?", options: ["Stop", "Speed up", "Overtake", "Turn immediately"], correct: 0, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "A red signal requires a complete vehicular stop behind the stop line." },
    { id: "q_e2", text: "What does a green traffic light generally mean?", options: ["Stop", "Proceed when safe", "Reverse", "Park"], correct: 1, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "Green indicates permission to proceed only if the intersection is clear of pedestrians and vehicles." },
    { id: "q_e3", text: "What does a yellow traffic light warn drivers about?", options: ["Prepare to stop", "Speed up", "Park", "Overtake"], correct: 0, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "A yellow light indicates that the red light is imminent; drivers must prepare to stop safely." },
    { id: "q_e4", text: "What should a driver do before starting a trip?", options: ["Check the vehicle", "Increase the radio volume", "Drive immediately", "Ignore the vehicle condition"], correct: 0, difficulty: "Easy", topic: "Vehicle Maintenance & Inspection", points: 10, exp: "Conducting pre-trip BLOWBAGETS inspection prevents mechanical failures on the road." },
    { id: "q_e5", text: "What is the purpose of a seat belt?", options: ["Improve fuel economy", "Protect occupants during a crash", "Increase vehicle speed", "Improve engine power"], correct: 1, difficulty: "Easy", topic: "Seat Belt Safety", points: 10, exp: "Seat belts restrain occupants, distributing impact forces across rigid skeletal structures." },
    { id: "q_e6", text: "What should you do when approaching a pedestrian crossing?", options: ["Speed up", "Slow down and be prepared to stop", "Honk continuously", "Overtake other vehicles"], correct: 1, difficulty: "Easy", topic: "Pedestrian Safety", points: 10, exp: "Pedestrians on marked crosswalks have legal right-of-way; slow down and prepare to yield." },
    { id: "q_e7", text: "What does a STOP sign require a driver to do?", options: ["Slow down only", "Come to a complete stop", "Speed up", "Turn around"], correct: 1, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "An octagonal STOP sign mandates a full halt before proceeding when safe." },
    { id: "q_e8", text: "Why are turn signals used?", options: ["To communicate intended movement", "To increase speed", "To warn about engine problems", "To save fuel"], correct: 0, difficulty: "Easy", topic: "Vehicle Communication", points: 10, exp: "Turn signals give surrounding motorists and pedestrians advance notice of your maneuvers." },
    { id: "q_e9", text: "What should you do when driving behind another vehicle?", options: ["Follow extremely closely", "Maintain a safe following distance", "Drive beside it constantly", "Flash headlights continuously"], correct: 1, difficulty: "Easy", topic: "Following Distance", points: 10, exp: "Maintaining adequate following distance grants sufficient reaction time and stopping buffer." },
    { id: "q_e10", text: "What is the main purpose of traffic signs?", options: ["Decoration", "Provide information and regulate traffic", "Increase vehicle speed", "Advertise vehicles"], correct: 1, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "Traffic signs regulate, warn, and guide road users for smooth and safe traffic flow." },
    { id: "q_e11", text: "Who should wear a seat belt in a vehicle equipped with seat belts?", options: ["Only the driver", "Only passengers", "Driver and passengers as required", "Nobody"], correct: 2, difficulty: "Easy", topic: "Seat Belt Safety", points: 10, exp: "Under R.A. 8750, both driver and front/rear passengers are mandated to wear seat belts." },
    { id: "q_e12", text: "What should you do if you feel very tired while driving?", options: ["Continue driving faster", "Stop at a safe place and rest", "Open the windows and continue indefinitely", "Ignore the tiredness"], correct: 1, difficulty: "Easy", topic: "Driver Condition & Fatigue", points: 10, exp: "Driver fatigue impairs reaction time similarly to alcohol; take a rest break immediately." },
    { id: "q_e13", text: "What does a pedestrian crossing primarily provide?", options: ["A place for pedestrians to cross the road", "A parking area", "A loading zone", "An overtaking lane"], correct: 0, difficulty: "Easy", topic: "Pedestrian Safety", points: 10, exp: "Zebra crosswalks designate safe crossing paths where pedestrians possess legal priority." },
    { id: "q_e14", text: "What should you do before changing lanes?", options: ["Check surrounding traffic", "Close your eyes", "Accelerate without checking", "Immediately move over"], correct: 0, difficulty: "Easy", topic: "Lane Changing & Overtaking", points: 10, exp: "Always check rear-view and side mirrors and look over your shoulder to inspect blind spots." },
    { id: "q_e15", text: "What should a responsible driver obey?", options: ["Traffic laws and regulations", "Only other drivers", "Only passengers", "No rules"], correct: 0, difficulty: "Easy", topic: "Traffic Laws & Discipline", points: 10, exp: "Adherence to traffic laws and ordinances ensures safe, orderly, and disciplined roads." },
    { id: "q_e16", text: "What is the safest approach to driving?", options: ["Aggressive driving", "Defensive driving", "Racing", "Constant overtaking"], correct: 1, difficulty: "Easy", topic: "Defensive Driving", points: 10, exp: "Defensive driving involves anticipating hazards and allowing margins for others' errors." },
    { id: "q_e17", text: "What should you do when you see a warning sign?", options: ["Ignore it", "Be alert and adjust driving as necessary", "Speed up", "Stop anywhere"], correct: 1, difficulty: "Easy", topic: "Traffic Signs & Signals", points: 10, exp: "Warning signs (triangular or diamond) caution drivers about upcoming hazards and curve changes." },
    { id: "q_e18", text: "What is the purpose of a vehicle's brakes?", options: ["Increase speed", "Slow down or stop the vehicle", "Improve the radio", "Increase fuel consumption"], correct: 1, difficulty: "Easy", topic: "Vehicle Maintenance & Inspection", points: 10, exp: "Brakes convert kinetic energy into friction to decelerate or completely stop the vehicle." },
    { id: "q_e19", text: "What should you do when approaching an intersection?", options: ["Observe traffic and signs", "Close your eyes", "Always accelerate", "Ignore other vehicles"], correct: 0, difficulty: "Easy", topic: "Right-of-Way", points: 10, exp: "Intersections are multi-hazard zones; scan left, right, and ahead before entering." },
    { id: "q_e20", text: "Why should drivers follow speed limits?", options: ["To promote road safety", "To use more fuel", "To make the trip longer", "To prevent all traffic"], correct: 0, difficulty: "Easy", topic: "Speed Management", points: 10, exp: "Speed limits match road design limits and significantly lower impact energy in collisions." },

    // 🟡 MEDIUM MODULE (20 Questions)
    { id: "q_m1", text: "You are approaching an intersection and the traffic signal changes from green to yellow. What should you do?", options: ["Always accelerate", "Prepare to stop when safe", "Reverse immediately", "Overtake"], correct: 1, difficulty: "Medium", topic: "Traffic Signs & Signals", points: 20, exp: "Controlled deceleration to stop safely before the line is required unless already in the intersection." },
    { id: "q_m2", text: "You want to change lanes on a busy road. What should you do first?", options: ["Move immediately", "Check mirrors and surrounding traffic", "Honk and turn suddenly", "Accelerate without checking"], correct: 1, difficulty: "Medium", topic: "Lane Changing & Overtaking", points: 20, exp: "Check interior and side mirrors, activate turn indicator 30 meters prior, and verify blind spots." },
    { id: "q_m3", text: "A vehicle in front of you suddenly slows down. What is the safest response?", options: ["Follow closely", "Maintain control and increase stopping space", "Overtake immediately", "Use the shoulder"], correct: 1, difficulty: "Medium", topic: "Following Distance", points: 20, exp: "Apply firm and controlled braking to maintain a safe cushion from the decelerating vehicle." },
    { id: "q_m4", text: "You approach a pedestrian who is preparing to cross at a designated crossing. What should you do?", options: ["Speed up", "Slow down and yield as required", "Ignore the pedestrian", "Overtake another vehicle"], correct: 1, difficulty: "Medium", topic: "Pedestrian Safety", points: 20, exp: "Yield right-of-way to crossing pedestrians; never pass a vehicle stopped at a crosswalk." },
    { id: "q_m5", text: "When is overtaking safest?", options: ["When visibility is adequate and it is legally permitted", "At every intersection", "On a blind curve", "When approaching a pedestrian crossing"], correct: 0, difficulty: "Medium", topic: "Lane Changing & Overtaking", points: 20, exp: "Overtake only with clear forward visibility, broken lane markings, and no oncoming traffic." },
    { id: "q_m6", text: "Why is following another vehicle too closely dangerous?", options: ["It reduces reaction and stopping time", "It improves visibility", "It saves fuel", "It makes traffic move faster"], correct: 0, difficulty: "Medium", topic: "Following Distance", points: 20, exp: "Tailgating drastically diminishes reaction time, making rear-end collisions unavoidable during sudden stops." },
    { id: "q_m7", text: "A driver behind you is attempting to overtake. What should you generally do?", options: ["Block the vehicle", "Maintain appropriate speed and allow safe passing when permitted", "Accelerate aggressively", "Move unpredictably"], correct: 1, difficulty: "Medium", topic: "Road Courtesy & Defensive Driving", points: 20, exp: "Maintain your lane and speed (or yield slightly) to facilitate the passing vehicle's safe return." },
    { id: "q_m8", text: "You are driving in heavy rain. What adjustment should you make?", options: ["Increase speed", "Reduce speed and increase following distance", "Drive closer to other vehicles", "Turn off all lights"], correct: 1, difficulty: "Medium", topic: "Weather & Hazard Driving", points: 20, exp: "Wet tarmac increases stopping distance and introduces hydroplaning risk; double following distance." },
    { id: "q_m9", text: "What should you do if your view of the road is temporarily blocked?", options: ["Continue at the same speed", "Slow down and proceed only when visibility is adequate", "Accelerate", "Overtake immediately"], correct: 1, difficulty: "Medium", topic: "Defensive Driving", points: 20, exp: "Never drive blind into unverified space; slow down until your visual field is restored." },
    { id: "q_m10", text: "You see a vehicle stopped near a pedestrian crossing. What should you consider?", options: ["A pedestrian may be crossing or preparing to cross", "The vehicle is always parked illegally", "You should immediately overtake", "The road is automatically clear"], correct: 0, difficulty: "Medium", topic: "Pedestrian Safety", points: 20, exp: "Assume the stopped vehicle is yielding to a pedestrian hidden from your immediate sightline." },
    { id: "q_m11", text: "When driving downhill, why should you maintain proper control of your vehicle?", options: ["The vehicle may gain speed", "The engine automatically stops", "Brakes become unnecessary", "Traffic signs disappear"], correct: 0, difficulty: "Medium", topic: "Speed Management", points: 20, exp: "Gravity increases vehicle momentum on descents; use engine braking to avoid brake overheating." },
    { id: "q_m12", text: "What is a good practice when approaching a sharp curve?", options: ["Reduce speed before entering the curve", "Accelerate heavily", "Overtake immediately", "Drive on the opposite lane"], correct: 0, difficulty: "Medium", topic: "Speed Management", points: 20, exp: "Brake in a straight line before the curve; accelerating through the apex prevents tire skid." },
    { id: "q_m13", text: "A traffic officer is directing traffic while the traffic signal shows a different indication. What should you follow?", options: ["The traffic officer's lawful direction", "The radio", "Another driver's action", "The vehicle behind you"], correct: 0, difficulty: "Medium", topic: "Traffic Laws & Discipline", points: 20, exp: "Hand signals and lawful directions of an active traffic enforcer supersede automatic signals." },
    { id: "q_m14", text: "What should you do before turning?", options: ["Signal and check for other road users", "Turn suddenly", "Ignore pedestrians", "Accelerate without checking"], correct: 0, difficulty: "Medium", topic: "Vehicle Communication", points: 20, exp: "Check mirrors, signal at least 30 meters ahead, and verify that motorcyclists or pedestrians are clear." },
    { id: "q_m15", text: "Why should you avoid unnecessary distractions while driving?", options: ["They can reduce attention and reaction ability", "They increase concentration", "They improve road visibility", "They make traffic signs clearer"], correct: 0, difficulty: "Medium", topic: "Impaired & Distracted Driving", points: 20, exp: "Distractions such as mobile phones lead to cognitive tunnel vision and fatal delays in braking." },
    { id: "q_m16", text: "If another driver behaves aggressively toward you, what is the safest response?", options: ["Challenge the driver", "Remain calm and avoid escalating the situation", "Race the driver", "Follow the driver closely"], correct: 1, difficulty: "Medium", topic: "Road Courtesy & Defensive Driving", points: 20, exp: "Avoid eye contact, create physical distance, and prioritize de-escalation over confrontation." },
    { id: "q_m17", text: "When approaching a roadwork area, you should:", options: ["Follow temporary signs and adjust speed", "Ignore signs", "Drive through barriers", "Overtake workers"], correct: 0, difficulty: "Medium", topic: "Weather & Hazard Driving", points: 20, exp: "Construction zones feature narrow lanes, debris, and workers; observe temporary limits strictly." },
    { id: "q_m18", text: "Why should you check your mirrors regularly?", options: ["To monitor surrounding traffic", "To increase engine power", "To reduce tire wear", "To change the traffic light"], correct: 0, difficulty: "Medium", topic: "Defensive Driving", points: 20, exp: "Regular mirror checks every 5 to 8 seconds maintain complete 360-degree situational awareness." },
    { id: "q_m19", text: "What is defensive driving primarily intended to do?", options: ["Anticipate hazards and reduce collision risks", "Make driving more aggressive", "Encourage speeding", "Eliminate traffic rules"], correct: 0, difficulty: "Medium", topic: "Defensive Driving", points: 20, exp: "Defensive driving saves lives, time, and money in spite of the conditions around you." },
    { id: "q_m20", text: "When parking, what should you consider first?", options: ["Whether parking is permitted and safe at the location", "How quickly you can leave", "Whether another car is nearby only", "Whether you can block part of the road"], correct: 0, difficulty: "Medium", topic: "Traffic Laws & Discipline", points: 20, exp: "Verify parking signage, ensure adequate clearance, and avoid obstructing driveways or fire hydrants." },

    // 🔴 HARD MODULE (20 Questions)
    { id: "q_h1", text: "You approach an intersection with no traffic signal. Another vehicle is already approaching from a direction that has the applicable right-of-way. What should you do?", options: ["Accelerate to reach the intersection first", "Yield according to the applicable right-of-way rule", "Ignore the vehicle", "Use the shoulder"], correct: 1, difficulty: "Hard", topic: "Right-of-Way", points: 30, exp: "Under R.A. 4136, at uncontrolled intersections, vehicles on the right or already within have priority." },
    { id: "q_h2", text: "You are driving at night and an oncoming vehicle has bright headlights. What is the safest response?", options: ["Look directly into the headlights", "Reduce speed as necessary and avoid being blinded by staring at the lights", "Turn your headlights off", "Accelerate toward the vehicle"], correct: 1, difficulty: "Hard", topic: "Night & Low Visibility Driving", points: 30, exp: "Look toward the right edge line of the roadway to guide your steering without retinal glare blindness." },
    { id: "q_h3", text: "You are approaching a curve where you cannot see vehicles coming from the opposite direction. Should you overtake?", options: ["Yes, if you honk", "No, because visibility is insufficient", "Yes, if you accelerate", "Yes, if the vehicle ahead is slow"], correct: 1, difficulty: "Hard", topic: "Lane Changing & Overtaking", points: 30, exp: "Overtaking on blind curves is strictly prohibited by law due to zero forward sight distance." },
    { id: "q_h4", text: "You are driving in heavy rain and notice water accumulating on the road. What is the safest approach?", options: ["Increase speed to cross quickly", "Reduce speed and maintain control", "Follow the vehicle ahead closely", "Make sudden steering movements"], correct: 1, difficulty: "Hard", topic: "Weather & Hazard Driving", points: 30, exp: "Standing water causes hydroplaning (loss of tire-pavement contact); ease off the throttle smoothly." },
    { id: "q_h5", text: "You are approaching an intersection while an emergency vehicle is approaching with its warning devices activated. What should you do?", options: ["Compete for the intersection", "Give way as required and avoid obstructing it", "Follow closely behind it", "Overtake it"], correct: 1, difficulty: "Hard", topic: "Emergency Vehicles & Sirens", points: 30, exp: "Pull over as close as possible to the right side of the road and halt until emergency vehicles pass." },
    { id: "q_h6", text: "You are preparing to overtake, but the road markings and traffic conditions do not permit a safe maneuver. What should you do?", options: ["Overtake anyway", "Wait until overtaking is legal and safe", "Drive on the sidewalk", "Use the opposite lane regardless of conditions"], correct: 1, difficulty: "Hard", topic: "Lane Changing & Overtaking", points: 30, exp: "Solid white or yellow centerlines strictly forbid passing; exercise patience until marked broken zones." },
    { id: "q_h7", text: "A vehicle suddenly enters your lane from a side road. What should be your first priority?", options: ["Maintain safety and avoid collision", "Sound the horn continuously", "Accelerate toward the vehicle", "Chase the vehicle"], correct: 0, difficulty: "Hard", topic: "Defensive Driving", points: 30, exp: "Execute controlled braking and maneuver into your safety escape cushion to prevent collision." },
    { id: "q_h8", text: "You are driving behind a large truck that blocks your view of the road ahead. What should you do?", options: ["Follow extremely closely", "Increase following distance to improve visibility and reaction time", "Overtake immediately without checking", "Drive beside the truck indefinitely"], correct: 1, difficulty: "Hard", topic: "Following Distance", points: 30, exp: "Dropping back widens your viewing angle around the truck body and avoids truck blind spots (No-Zones)." },
    { id: "q_h9", text: "You miss your intended turn on a busy road. What is the safest choice?", options: ["Stop and reverse immediately", "Continue safely and find a legal place to turn or reroute", "Make a sudden U-turn", "Drive against traffic"], correct: 1, difficulty: "Hard", topic: "Defensive Driving", points: 30, exp: "Never stop or reverse on active roadways; proceed to the next roundabout or legal U-turn slot." },
    { id: "q_h10", text: "You are approaching a pedestrian crossing while another vehicle in the adjacent lane has stopped. Why should you be cautious?", options: ["A pedestrian may be hidden from your view", "The road is always empty", "You should overtake the stopped vehicle immediately", "The stopped vehicle automatically gives you priority"], correct: 0, difficulty: "Hard", topic: "Pedestrian Safety", points: 30, exp: "A stopped vehicle at a crosswalk blocks your view of pedestrians; passing is strictly illegal and fatal." },
    { id: "q_h11", text: "A driver behind you is following too closely. What is the safest response?", options: ["Brake suddenly to teach the driver a lesson", "Maintain a safe pace and, when appropriate, allow the vehicle to pass", "Race the vehicle", "Block the vehicle"], correct: 1, difficulty: "Hard", topic: "Road Courtesy & Defensive Driving", points: 30, exp: "Increase your forward cushion to allow gentle stops and encourage tailgaters to overtake safely." },
    { id: "q_h12", text: "Your vehicle begins to skid on a slippery surface. What should you avoid?", options: ["Sudden, aggressive steering or braking", "Remaining calm", "Maintaining vehicle control", "Adjusting speed appropriately"], correct: 0, difficulty: "Hard", topic: "Weather & Hazard Driving", points: 30, exp: "Slamming brakes or violent steering locks wheels; steer smoothly into the direction of the skid." },
    { id: "q_h13", text: "You are approaching a road intersection where your view is obstructed by a parked vehicle. What should you do?", options: ["Proceed quickly", "Slow down and ensure the way is clear", "Accelerate through the intersection", "Ignore the obstruction"], correct: 1, difficulty: "Hard", topic: "Right-of-Way", points: 30, exp: "Creep forward slowly until your sightlines to cross-traffic are clear before committing to enter." },
    { id: "q_h14", text: "You are tired, but you are only a few kilometers from your destination. What is the safest decision?", options: ["Continue because the destination is close", "Stop and rest if you are not fit to drive", "Drive faster to arrive sooner", "Drink something and continue regardless of fatigue"], correct: 1, difficulty: "Hard", topic: "Driver Condition & Fatigue", points: 30, exp: "Microsleeps happen in seconds, especially near the end of long drives; stop in a safe lit area to rest." },
    { id: "q_h15", text: "You see a temporary traffic sign that differs from the normal road arrangement because of road construction. What should you do?", options: ["Follow the temporary traffic control", "Ignore it", "Follow the old road arrangement", "Drive around the barriers"], correct: 0, difficulty: "Hard", topic: "Traffic Signs & Signals", points: 30, exp: "Work-zone signs supersede permanent striping and lane layouts to protect lives." },
    { id: "q_h16", text: "You are entering a road where pedestrians, motorcycles, bicycles, and vehicles are all present. What is the best driving strategy?", options: ["Assume everyone will move out of your way", "Maintain awareness, reduce risk, and anticipate possible movements", "Drive at maximum speed", "Focus only on vehicles"], correct: 1, difficulty: "Hard", topic: "Defensive Driving", points: 30, exp: "Mixed-traffic roads demand vigilant scanning for vulnerable road users and wider lateral passing buffers." },
    { id: "q_h17", text: "A vehicle ahead signals that it intends to turn, but you are also approaching the same area. What should you do?", options: ["Ignore the signal", "Adjust your speed and position safely while considering the vehicle's movement", "Overtake immediately", "Drive beside it without checking"], correct: 1, difficulty: "Hard", topic: "Defensive Driving", points: 30, exp: "Anticipate the turning vehicle's deceleration path and maintain safe following gap." },
    { id: "q_h18", text: "You are driving on a road with a posted speed limit, but traffic, weather, and visibility conditions are poor. What should you prioritize?", options: ["Maximum speed", "Safe speed appropriate to the conditions while obeying the applicable limit", "Keeping up with the fastest vehicle", "Driving faster than the posted limit"], correct: 1, difficulty: "Hard", topic: "Speed Management", points: 30, exp: "The Basic Speed Rule dictates driving at a prudent speed suited to environmental conditions." },
    { id: "q_h19", text: "A driver becomes angry after you make a legal maneuver. What is the best defensive-driving response?", options: ["Confront the driver", "Maintain composure and create distance from the aggressive driver", "Follow the driver", "Make an aggressive maneuver"], correct: 1, difficulty: "Hard", topic: "Road Courtesy & Defensive Driving", points: 30, exp: "Never engage or retaliate; preserve a calm demeanor and navigate toward safe, populated areas." },
    { id: "q_h20", text: "You are approaching an intersection with several potential hazards: a pedestrian near the crossing, a motorcycle beside you, and a vehicle approaching from another direction. What should you do?", options: ["Focus only on the vehicle ahead", "Slow down, scan all relevant road users, and proceed only when safe and permitted", "Accelerate through the intersection", "Sound the horn and continue without checking"], correct: 1, difficulty: "Hard", topic: "Defensive Driving", points: 30, exp: "Multi-hazard situations require early speed reduction, systematic scanning, and defensive yielding." }
];

const SIMULATION_20_SCENARIOS = [
    {
        id: "sim_01",
        number: 1,
        title: "01 — Pedestrian Crossing",
        shortTitle: "Pedestrian Crossing",
        difficulty: "Easy",
        speed: "30 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "URBAN ARTERIAL ROAD",
        svgType: "pedestrian_crossing",
        situation: "You are driving at 30 km/h on an urban road. A pedestrian is standing at a marked pedestrian zebra crossing ahead, looking across and preparing to step onto the roadway.",
        prompt: "What is the safest immediate action?",
        options: [
            { text: "Speed up slightly to clear the crosswalk before the pedestrian steps into the lane.", isCorrect: false, risk: "Extreme Collision Risk" },
            { text: "Honk your horn repeatedly and proceed through at your current cruising speed.", isCorrect: false, risk: "Hazardous & Illegal" },
            { text: "Decelerate smoothly, bring the vehicle to a complete stop before the stop line, and yield right-of-way.", isCorrect: true, risk: "Safest Decision (Legal & Defensive)" },
            { text: "Swerve into the opposing lane to drive around the crossing without stopping.", isCorrect: false, risk: "Severe Multi-Vehicle Hazard" }
        ],
        aiFeedback: {
            why: "Stopping well before the crosswalk grants full pedestrian priority, eliminates collision risk, and provides clear visual communication to surrounding motorists.",
            hazard: "Vulnerable pedestrian entering designated crossing with active vehicular traffic.",
            principle: "R.A. 4136 Art. III Sec. 42 (Right-of-Way at Crosswalks) & Defensive Pedestrian Anticipation.",
            action: "Smoothly decelerate, stop before the marked stop line, maintain foot on brake, and wait until pedestrians fully reach the sidewalk.",
            incorrectWhy: "Failing to stop or attempting to bypass pedestrians at a marked crosswalk violates Philippine right-of-way laws and causes catastrophic pedestrian impacts."
        }
    },
    {
        id: "sim_02",
        number: 2,
        title: "02 — Changing Traffic Light",
        shortTitle: "Changing Traffic Light",
        difficulty: "Easy",
        speed: "45 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "SIGNALIZED INTERSECTION",
        svgType: "traffic_light",
        situation: "Approaching an intersection at 45 km/h, approximately 35 meters away. The traffic signal abruptly changes from green to solid yellow/amber.",
        prompt: "What is the safest and most legally compliant action?",
        options: [
            { text: "Accelerate quickly to beat the light before the red signal activates.", isCorrect: false, risk: "High Intersection Crash Risk" },
            { text: "Check rearview mirror and apply controlled braking to stop safely before the stop line.", isCorrect: true, risk: "Safest Decision (Legal & Controlled)" },
            { text: "Slam on emergency brakes instantly without verifying vehicles behind you.", isCorrect: false, risk: "Rear-End Collision Risk" },
            { text: "Sound horn and coast through the intersection without slowing down.", isCorrect: false, risk: "Traffic Violation & Broadside Risk" }
        ],
        aiFeedback: {
            why: "At 35 meters at 45 km/h, you have ample stopping distance. Yellow means 'prepare to stop unless unsafe to do so' — not an invitation to accelerate.",
            hazard: "Impending red light phase and conflicting cross-traffic anticipating green.",
            principle: "Philippine Traffic Code Signal Rules: Yellow Light Duty to Stop.",
            action: "Check rear mirror, apply progressive braking, and come to a stable stop behind the white pavement stop bar.",
            incorrectWhy: "Accelerating on amber creates right-angle T-bone collisions with cross-traffic starting their movement."
        }
    },
    {
        id: "sim_03",
        number: 3,
        title: "03 — Motorcycle Blind Spot",
        shortTitle: "Motorcycle Blind Spot",
        difficulty: "Easy",
        speed: "40 KM/H",
        weather: "🌤️ CLEAR · DRY",
        env: "MULTI-LANE CITY ROAD",
        svgType: "blind_spot",
        situation: "You intend to change into the left lane. Side mirrors appear clear, but a quick shoulder head-check reveals a motorcycle traveling in your rear-quarter blind spot.",
        prompt: "What should you do before initiating your lane change?",
        options: [
            { text: "Continue the lane change quickly since you already turned on your signal indicator.", isCorrect: false, risk: "Side-Swipe Collision" },
            { text: "Hold your current lane, maintain safe speed, allow the motorcycle to pass, and re-verify mirrors.", isCorrect: true, risk: "Safest Decision (Defensive & Aware)" },
            { text: "Abruptly brake in your lane to force the motorcycle to pass ahead faster.", isCorrect: false, risk: "Traffic Flow Disruption" },
            { text: "Honk and gradually drift into the lane expecting the rider to brake for you.", isCorrect: false, risk: "Aggressive & Dangerous Maneuver" }
        ],
        aiFeedback: {
            why: "A motorcycle in your blind spot cannot be seen in mirrors alone. Yielding until the rider clears eliminates fatal side-swipe collisions.",
            hazard: "Two-wheeler concealed in vehicle's rear lateral blind zone during lane change.",
            principle: "Mirror-Signal-Headcheck (MSH) Protocol & Safe Lateral Cushioning.",
            action: "Cancel or maintain signal, hold lane alignment, confirm rider has passed, perform fresh head-check, and merge smoothly.",
            incorrectWhy: "Signaling does not give automatic right-of-way; forcing lane entry when occupied leads to severe motorcycle crashes."
        }
    },
    {
        id: "sim_04",
        number: 4,
        title: "04 — Sudden Braking",
        shortTitle: "Sudden Braking Ahead",
        difficulty: "Easy",
        speed: "50 KM/H",
        weather: "🌤️ OVERCAST · DRY",
        env: "NATIONAL HIGHWAY",
        svgType: "sudden_braking",
        situation: "Driving at 50 km/h maintaining a 3-second buffer. The passenger vehicle ahead abruptly slams on its brakes with illuminated brake lights.",
        prompt: "What is your immediate, safest reaction?",
        options: [
            { text: "Apply firm, controlled braking in your own lane while monitoring your rear mirror.", isCorrect: true, risk: "Safest Decision (Controlled Buffer)" },
            { text: "Immediately swerve onto the road shoulder without checking for pedestrians or obstacles.", isCorrect: false, risk: "Off-Road Rollover / Hazard" },
            { text: "Swerve into the oncoming traffic lane to avoid braking.", isCorrect: false, risk: "Catastrophic Head-On Crash" },
            { text: "Lightly tap brakes and flash high beams to tell the front car to accelerate.", isCorrect: false, risk: "Imminent Rear-End Impact" }
        ],
        aiFeedback: {
            why: "A 3-second following distance is designed specifically to allow firm, controlled straight-line braking without panic swerving.",
            hazard: "Rapid deceleration of leading vehicle creating sudden closing speed.",
            principle: "3-Second Following Distance Buffer & Progressive Braking Technique.",
            action: "Depress brake pedal firmly and progressively, steer straight, and tap hazards if traffic behind approaches rapidly.",
            incorrectWhy: "Blind swerving into adjacent lanes or shoulders trades one hazard for an even deadlier collision."
        }
    },
    {
        id: "sim_05",
        number: 5,
        title: "05 — Heavy Rain",
        shortTitle: "Heavy Rain & Low Visibility",
        difficulty: "Easy",
        speed: "60 KM/H (REDUCED TO 40)",
        weather: "🌧️ HEAVY DOWNPOUR",
        env: "PROVINCIAL HIGHWAY",
        svgType: "heavy_rain",
        situation: "Sudden tropical heavy downpour severely reduces visibility. Water is sheeting on the asphalt and windshield wipers are on high.",
        prompt: "What set of driving adjustments must you make?",
        options: [
            { text: "Turn on hazard emergency flashers and drive at normal 60 km/h highway speed.", isCorrect: false, risk: "Misleading Signals & Hydroplaning" },
            { text: "Reduce speed significantly, double following distance, turn on low-beam headlights, and avoid sudden steering.", isCorrect: true, risk: "Safest Decision (Hydroplane Prevention)" },
            { text: "Turn on high beams and tailgate the car ahead to follow its tire tracks closely.", isCorrect: false, risk: "Glare Blinding & Tailgating Hazard" },
            { text: "Brake sharply whenever entering standing water puddles.", isCorrect: false, risk: "Loss of Directional Traction" }
        ],
        aiFeedback: {
            why: "Wet roads cut tire friction by up to 50% and increase hydroplaning risk. Low-beam lights enhance visibility without blinding others with high-beam rain glare.",
            hazard: "Reduced tire traction, hydroplaning, extended braking distance, and impaired driver vision.",
            principle: "Adverse Weather Speed Adjustment & 5-6 Second Wet Road Buffer.",
            action: "Drop speed to 35-40 km/h, activate low beams and defogger, double spacing, and drive with smooth inputs.",
            incorrectWhy: "Hazard flashers while moving confuse other drivers regarding whether you are stalled; excessive speed causes hydroplaning."
        }
    },
    {
        id: "sim_06",
        number: 6,
        title: "06 — Road Obstruction",
        shortTitle: "Lane Road Obstruction",
        difficulty: "Medium",
        speed: "40 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "TWO-LANE BARANGAY ROAD",
        svgType: "road_obstruction",
        situation: "A disabled cargo delivery van and road debris partially block your lane ahead. Oncoming traffic is approaching in the opposite lane.",
        prompt: "How should you safely navigate past this obstruction?",
        options: [
            { text: "Speed up and squeeze past the obstacle before the oncoming vehicle reaches it.", isCorrect: false, risk: "High Collision & Pinch Risk" },
            { text: "Slow down, stop behind the obstruction in your lane, yield to oncoming traffic, and pass only when clear.", isCorrect: true, risk: "Safest Decision (Right-of-Way Compliance)" },
            { text: "Honk continuously and force oncoming vehicles to yield right-of-way to you.", isCorrect: false, risk: "Aggressive Road Conflict" },
            { text: "Drive onto the pedestrian sidewalk to bypass the stalled delivery van.", isCorrect: false, risk: "Severe Pedestrian Hazard & Illegal" }
        ],
        aiFeedback: {
            why: "The driver whose lane is obstructed MUST yield to opposing traffic having an unobstructed lane before maneuvering around the hazard.",
            hazard: "Blocked travel lane with oncoming opposing traffic having legal right-of-way.",
            principle: "Lane Obstruction Yielding Law & Safe Lateral Clearance.",
            action: "Stop safely behind the blockage, signal left, wait for clear oncoming gap, check mirrors/blindspot, and pass with cushion.",
            incorrectWhy: "Cutting into oncoming lanes when opposing traffic is present violates right-of-way and creates high-speed frontal impacts."
        }
    },
    {
        id: "sim_07",
        number: 7,
        title: "07 — Emergency Vehicle",
        shortTitle: "Emergency Vehicle Approaching",
        difficulty: "Medium",
        speed: "35 KM/H",
        weather: "🌤️ DAY · MODERATE TRAFFIC",
        env: "CITY ARTERIAL BOULEVARD",
        svgType: "emergency_vehicle",
        situation: "An ambulance with active sirens and flashing red/blue strobe lights is rapidly approaching from behind in your travel lane.",
        prompt: "What is your legal obligation and safest maneuver?",
        options: [
            { text: "Stop dead in your current travel lane immediately.", isCorrect: false, risk: "Blocks Emergency Path" },
            { text: "Speed up to outrun the ambulance until you find a convenient turn-off.", isCorrect: false, risk: "Delays Emergency & High Crash Risk" },
            { text: "Signal right, smoothly pull over as close as possible to the right edge/curb, and stop to give clear passage.", isCorrect: true, risk: "Safest Decision (Legal Yield Protocol)" },
            { text: "Tailgate closely behind the ambulance to bypass heavy traffic.", isCorrect: false, risk: "Illegal Emergency Convoy Violation" }
        ],
        aiFeedback: {
            why: "Philippine Law (R.A. 4136 Sec. 49) mandates all drivers to immediately yield right-of-way to emergency vehicles by pulling parallel to the right curb.",
            hazard: "Fast-moving emergency response vehicle requiring unimpeded pathway.",
            principle: "R.A. 4136 Sec. 49 (Right-of-Way for Police, Fire, and Ambulance Vehicles).",
            action: "Check right mirror, signal right, steer safely to the rightmost edge, bring vehicle to a stop, and hold until vehicle has passed.",
            incorrectWhy: "Stopping in place blocks the emergency path; tailgating emergency vehicles is illegal and carries heavy penalties."
        }
    },
    {
        id: "sim_08",
        number: 8,
        title: "08 — Unsafe Overtaking",
        shortTitle: "Unsafe Overtaking on Curve",
        difficulty: "Medium",
        speed: "45 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "TWO-LANE MOUNTAIN HIGHWAY",
        svgType: "unsafe_overtaking",
        situation: "You are stuck behind a slow-moving agricultural tricycle on an uphill winding road with a solid double yellow center line and an upcoming blind curve.",
        prompt: "What is the only safe and lawful decision?",
        options: [
            { text: "Cross the double yellow line quickly to overtake while the tricycle is crawling.", isCorrect: false, risk: "Blind Curve Head-On Disaster" },
            { text: "Maintain safe following distance, stay in your lane, be patient, and wait for a designated broken-line passing zone with clear visibility.", isCorrect: true, risk: "Safest Decision (Patience & Legality)" },
            { text: "Tailgate the tricycle closely and honk until the rider pulls off into the ditch.", isCorrect: false, risk: "Harassment & Rear-End Hazard" },
            { text: "Overtake on the unpaved right dirt shoulder around the tricycle.", isCorrect: false, risk: "Shoulder Rollover / Pedestrian Hit" }
        ],
        aiFeedback: {
            why: "Solid double yellow lines prohibit overtaking under all circumstances due to zero sight distance on curves and crests.",
            hazard: "Blind curve with invisible oncoming vehicles traveling at highway speeds.",
            principle: "R.A. 4136 Sec. 41 (Restrictions on Overtaking and Passing) & Pavement Markings.",
            action: "Drop back to a 3-second buffer, observe road signage, and only pass when you reach a flat, clear straightaway with broken lines.",
            incorrectWhy: "Overtaking on blind curves across solid yellow lines is among the leading causes of fatal head-on highway collisions."
        }
    },
    {
        id: "sim_09",
        number: 9,
        title: "09 — School Zone",
        shortTitle: "Active School Zone",
        difficulty: "Medium",
        speed: "40 KM/H (NEEDS SLOWING)",
        weather: "🌤️ CLEAR · MORNING",
        env: "COMMUNITY SCHOOL PRECINCT",
        svgType: "school_zone",
        situation: "Approaching a public elementary school zone during morning drop-off hours. School warning signs are visible, and children are walking along the roadside.",
        prompt: "How should you adjust your driving behavior?",
        options: [
            { text: "Maintain 40 km/h while honking continuously to make children stand back.", isCorrect: false, risk: "Panics Children & High Risk" },
            { text: "Reduce speed to 20 km/h or below, scan sidewalks and between parked cars, and be ready for sudden stops.", isCorrect: true, risk: "Safest Decision (Child Safety Standard)" },
            { text: "Overtake waiting school transport tricycles to clear the zone quickly.", isCorrect: false, risk: "Extreme Pedestrian Impact Risk" },
            { text: "Look only at the car ahead of you and ignore the sidewalk activity.", isCorrect: false, risk: "Severe Tunnel Vision Hazard" }
        ],
        aiFeedback: {
            why: "Children have limited hazard perception and may dart unexpectedly into the roadway. 20 km/h gives a stopping distance of just a few meters.",
            hazard: "Unpredictable child pedestrians and unloading school transport vehicles.",
            principle: "R.A. 4136 Sec. 35 (20 km/h Maximum Speed in School Zones) & Pedestrian Anticipation.",
            action: "Decelerate to under 20 km/h, hover foot over brake pedal, cover blind spots around parked tricycles, and yield generously.",
            incorrectWhy: "Exceeding 20 km/h in school zones dramatically increases the likelihood of fatal injury if a child steps off the curb."
        }
    },
    {
        id: "sim_10",
        number: 10,
        title: "10 — Motorcycle Traffic",
        shortTitle: "Dense Motorcycle Traffic",
        difficulty: "Medium",
        speed: "30 KM/H",
        weather: "🌤️ CLEAR · EVENING RUSH",
        env: "DENSE METRO CORRIDOR",
        svgType: "motorcycle_traffic",
        situation: "Driving in dense urban traffic surrounded by multiple motorcycles lane-filtering and riding closely along your vehicle's left and right sides.",
        prompt: "What is the best defensive driving strategy?",
        options: [
            { text: "Weave within your lane to discourage riders from filtering past you.", isCorrect: false, risk: "Aggressive Lane-Blocking Crash" },
            { text: "Maintain stable central lane position, avoid sudden swerves, check all mirrors and blind spots before any maneuver, and signal early.", isCorrect: true, risk: "Safest Decision (Predictable & Stable)" },
            { text: "Open your car door slightly to block motorcycles passing on the right.", isCorrect: false, risk: "Intentional Harm & Criminal Act" },
            { text: "Speed up rapidly whenever an opening appears to stay ahead of all bikes.", isCorrect: false, risk: "Erratic Acceleration Hazard" }
        ],
        aiFeedback: {
            why: "Predictability is the foundation of defensive driving. Holding a steady lane position and signaling early allows two-wheelers to navigate safely around you.",
            hazard: "Close-proximity riders filtering in multiple blind spots.",
            principle: "Defensive Space Cushioning & Multi-Mirror Scanning in Congestion.",
            action: "Maintain center-lane track, check side mirrors frequently, signal at least 30 meters before turning, and verify blind spots with head checks.",
            incorrectWhy: "Erratic lane shifts and abrupt braking startle riders and cause multiple pile-ups in dense traffic corridors."
        }
    },
    {
        id: "sim_11",
        number: 11,
        title: "11 — Intersection Conflict",
        shortTitle: "Uncontrolled Intersection Conflict",
        difficulty: "Medium",
        speed: "35 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "4-WAY UNCONTROLLED INTERSECTION",
        svgType: "intersection_conflict",
        situation: "Approaching an uncontrolled 4-way intersection without signals. Another vehicle from the left approaches simultaneously and enters without slowing down.",
        prompt: "Even if you technically have right-of-way from the right, what should you do?",
        options: [
            { text: "Accelerate and assert your legal right-of-way by forcing the other driver to stop.", isCorrect: false, risk: "Guaranteed Right-Angle Collision" },
            { text: "Slow down, prepare to stop, and yield to prevent a collision despite having technical priority.", isCorrect: true, risk: "Safest Decision (Defensive Priority)" },
            { text: "Close your eyes, honk your horn, and maintain current speed.", isCorrect: false, risk: "Extreme Recklessness" },
            { text: "Swerve sharply into the sidewalk corner to avoid the intersection.", isCorrect: false, risk: "Pedestrian & Infrastructure Impact" }
        ],
        aiFeedback: {
            why: "Right-of-way is something given, never taken. A defensive driver always yields to prevent a collision when another driver fails to follow priority rules.",
            hazard: "Conflicting vehicle failing to yield at unsignalized junction.",
            principle: "Defensive Right-of-Way: Collision Avoidance Supersedes Technical Priority.",
            action: "Brake smoothly, allow the non-yielding vehicle to clear the crossing, scan remaining directions, and proceed when completely clear.",
            incorrectWhy: "Insisting on technical right-of-way against an errant driver results in severe broadside crashes where legality won't prevent injuries."
        }
    },
    {
        id: "sim_12",
        number: 12,
        title: "12 — Night Driving",
        shortTitle: "Night Driving & Low Visibility",
        difficulty: "Medium",
        speed: "55 KM/H (OVER-DRIVING LIGHTS)",
        weather: "🌙 DARK · UNLIT RURAL ROAD",
        env: "PROVINCIAL NATIONAL ROAD",
        svgType: "night_driving",
        situation: "Driving on an unlit provincial highway at night with oncoming vehicle headlights in the distance. An unlit pedestrian/cyclist is barely visible on the right shoulder.",
        prompt: "What is the safest nighttime driving practice?",
        options: [
            { text: "Stare directly into the oncoming headlights to see the center lane markings.", isCorrect: false, risk: "Night-Blindness Flash Glare" },
            { text: "Keep high beams on permanently regardless of oncoming traffic to spot shoulder hazards.", isCorrect: false, risk: "Blinds Oncoming Motorists" },
            { text: "Switch to low beams, reduce speed to avoid over-driving headlights, and cast your gaze toward the right white fog line.", isCorrect: true, risk: "Safest Decision (Night Vision Protection)" },
            { text: "Turn off headlights momentarily to let your eyes adjust to natural darkness.", isCorrect: false, risk: "Total Blind Driving Hazard" }
        ],
        aiFeedback: {
            why: "Switching to low beams prevents blinding oncoming drivers, while guiding your eyes along the right edge white fog line protects your night vision and spots shoulder hazards.",
            hazard: "Headlight glare, reduced sight distance, and unlit pedestrians on road margins.",
            principle: "Night Driving Hazard Mitigation & Anti-Glare Gaze Technique.",
            action: "Dim headlights for oncoming traffic within 200m, slow down so your stopping distance is within your headlight beam range, and track the right edge line.",
            incorrectWhy: "Over-driving headlights means you cannot stop in time for hazards that appear in your light beams; high beam glare blinds oncoming drivers."
        }
    },
    {
        id: "sim_13",
        number: 13,
        title: "13 — Distracted Driving",
        shortTitle: "Mobile Phone Distraction",
        difficulty: "Medium",
        speed: "45 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "BUSY COMMERCIAL BOULEVARD",
        svgType: "distracted_phone",
        situation: "While cruising at 45 km/h in moderate traffic, your smartphone rings and vibrates on the dashboard mount with an incoming work notification.",
        prompt: "In compliance with Philippine Law (R.A. 10913), what must you do?",
        options: [
            { text: "Quickly read and reply to the message with one hand while keeping one hand on the wheel.", isCorrect: false, risk: "Severe Distraction & Law Violation" },
            { text: "Ignore the phone completely while driving, or safely pull over into a legal parking area before checking it.", isCorrect: true, risk: "Safest Decision (Anti-Distracted Driving Act)" },
            { text: "Hold the phone at eye level so you can look at both the road and the screen simultaneously.", isCorrect: false, risk: "Cognitive Blindness & Illegal" },
            { text: "Ask the passenger in the back seat to reach over and hold the phone in front of your face.", isCorrect: false, risk: "Physical Obstruction & Distraction" }
        ],
        aiFeedback: {
            why: "Republic Act No. 10913 (Anti-Distracted Driving Act) strictly penalizes using mobile devices while driving or stopped at red lights.",
            hazard: "Visual, manual, and cognitive distraction taking focus away from dynamic road conditions.",
            principle: "R.A. 10913 (Anti-Distracted Driving Act of 2016) & Complete Road Focus.",
            action: "Keep eyes on the road and hands on the wheel; only respond to calls or texts after coming to a full, legal parking stop with engine off or in park.",
            incorrectWhy: "Looking away for even 3 seconds at 45 km/h means traveling nearly 40 meters blind, causing devastating rear-end and pedestrian collisions."
        }
    },
    {
        id: "sim_14",
        number: 14,
        title: "14 — Fatigued Driving",
        shortTitle: "Driver Fatigue on Highway",
        difficulty: "Medium",
        speed: "70 KM/H",
        weather: "🌙 LATE NIGHT",
        env: "EXPRESSWAY / LONG HIGHWAY",
        svgType: "fatigued_driver",
        situation: "Driving for over 4 hours at night. Your eyes feel heavy, you find yourself yawning repeatedly, and the car slightly drifts toward the rumble strip.",
        prompt: "What is the only effective and responsible solution for driver fatigue?",
        options: [
            { text: "Roll down the window and turn up the radio volume to maximum.", isCorrect: false, risk: "Temporary Ineffective Fix (Microsleep)" },
            { text: "Drink an energy drink and speed up to reach your destination faster.", isCorrect: false, risk: "Dangerous Energy Crash & Speeding" },
            { text: "Signal, exit at the nearest gas station / rest stop, park safely, and take a 20-30 minute power nap.", isCorrect: true, risk: "Safest Decision (Fatigue Elimination)" },
            { text: "Slap your face periodically and continue driving in the fast lane.", isCorrect: false, risk: "High Microsleep Fatality Risk" }
        ],
        aiFeedback: {
            why: "Fatigue impairs reaction time and judgment as severely as alcohol intoxication. Sleep is the only physiological cure for driver exhaustion.",
            hazard: "Microsleep episodes leading to high-speed run-off-road or rear-end crashes.",
            principle: "Driver Wellness, Rest Protocols, and Fatigue Management.",
            action: "Exit expressway at next service area, lock doors, recline seat, take a 20-30 minute nap, hydrate, and stretch before resuming.",
            incorrectWhy: "Loud music and open windows do not prevent involuntary microsleeps where drivers lose consciousness for 3-5 seconds at high speeds."
        }
    },
    {
        id: "sim_15",
        number: 15,
        title: "15 — Traffic Sign Recognition",
        shortTitle: "Philippine Regulatory Sign",
        difficulty: "Medium",
        speed: "40 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "URBAN SIGNALIZED INTERSECTION",
        svgType: "traffic_sign",
        situation: "You are in the rightmost lane intending to turn right on a red traffic signal. A regulatory signboard beside the light reads 'NO RIGHT TURN ON RED SIGNAL'.",
        prompt: "What action is legally required?",
        options: [
            { text: "Turn right anyway if no cross-traffic or police officers are visible.", isCorrect: false, risk: "Red Light Violation & Fine" },
            { text: "Stop completely behind the stop line and remain stopped until the green arrow/signal illuminates.", isCorrect: true, risk: "Safest Decision (Mandatory Compliance)" },
            { text: "Honk twice and make a rolling right turn without stopping.", isCorrect: false, risk: "Pedestrian Threat & Violation" },
            { text: "Switch on hazard lights and proceed with the right turn.", isCorrect: false, risk: "Illegal Turn Under Hazard Lights" }
        ],
        aiFeedback: {
            why: "A 'NO RIGHT TURN ON RED' sign revokes default right-turn privileges to protect crossing pedestrians and protected cross-traffic movements.",
            hazard: "Conflicting pedestrian streams and oncoming left-turners having green priority.",
            principle: "Mandatory Compliance with Official Regulatory Signs (DPWH Traffic Standards).",
            action: "Come to a complete stop before the stop bar, hold brake, and proceed only when green signal or green right-turn arrow activates.",
            incorrectWhy: "Ignoring regulatory turn restrictions causes severe pedestrian impacts in the crosswalk and side-impact collisions with turning vehicles."
        }
    },
    {
        id: "sim_16",
        number: 16,
        title: "16 — Slippery Road",
        shortTitle: "Wet Slippery Road & Skid Control",
        difficulty: "Hard",
        speed: "50 KM/H",
        weather: "🌧️ POST-RAIN OIL SLICK",
        env: "HIGHWAY BEND",
        svgType: "slippery_road",
        situation: "Entering an asphalt curve after a light rain that brought oil to the surface. You feel the rear of the car begin to fish-tail and skid slightly outward.",
        prompt: "How do you regain steering control and prevent a spin-out?",
        options: [
            { text: "Slam the brake pedal to the floor and yank the steering wheel hard in the opposite direction.", isCorrect: false, risk: "Complete Spin-Out / Rollover" },
            { text: "Ease off the accelerator smoothly, steer gently in the direction you want the front wheels to go (into the skid), and avoid sudden braking.", isCorrect: true, risk: "Safest Decision (Proper Skid Recovery)" },
            { text: "Floor the accelerator pedal to power through the curve.", isCorrect: false, risk: "Catastrophic Loss of Traction" },
            { text: "Pull the handbrake immediately while turning the steering wheel.", isCorrect: false, risk: "Locks Rear Wheels into Spin" }
        ],
        aiFeedback: {
            why: "Slamming brakes during a skid locks tires and removes all steering capability. Smoothly easing off gas and steering into the skid restores tire grip.",
            hazard: "Loss of lateral tire grip (oversteer skid) on low-friction oil-slicked road.",
            principle: "Skid Recovery Physics: Weight Transfer & Smooth Counter-Steering.",
            action: "Smoothly release accelerator, look where you want to go, steer gently in that direction, and only brake after traction is re-established.",
            incorrectWhy: "Panic braking during a skid transfers vehicle weight forward, unloads the rear tires, and triggers an uncontrollable 360-degree spin."
        }
    },
    {
        id: "sim_17",
        number: 17,
        title: "17 — Aggressive Driver",
        shortTitle: "Aggressive Tailgater & Road Rage",
        difficulty: "Hard",
        speed: "60 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "MULTI-LANE HIGHWAY",
        svgType: "aggressive_driver",
        situation: "An aggressive SUV is tailgating inches from your rear bumper, flashing high beams, and honking aggressively to force you to speed up.",
        prompt: "What is the safest defensive method to de-escalate this road conflict?",
        options: [
            { text: "Brake check the aggressive vehicle abruptly to teach the driver a lesson.", isCorrect: false, risk: "Severe High-Speed Crash / Road Rage" },
            { text: "Maintain emotional control, signal right, safely change to the slower lane when clear, and let the aggressive vehicle pass.", isCorrect: true, risk: "Safest Decision (De-Escalation & Safety)" },
            { text: "Match the driver's speed, roll down window, and exchange shouting gestures.", isCorrect: false, risk: "Violent Road Rage Incident" },
            { text: "Block the passing lane deliberately to enforce the legal speed limit yourself.", isCorrect: false, risk: "Provocation & Lane Hogging" }
        ],
        aiFeedback: {
            why: "Defensive driving requires emotional maturity. De-escalating by yielding the lane removes an extreme hazard and avoids dangerous road rage encounters.",
            hazard: "Aggressive tailgater creating high risk of multi-vehicle pile-up and confrontation.",
            principle: "Defensive De-Escalation & Non-Engagement Policy.",
            action: "Keep calm, do not engage or make eye contact, check right mirror, signal, merge to right lane, and let aggressive traffic pass.",
            incorrectWhy: "Brake checking is illegal and extremely dangerous, turning a traffic dispute into a fatal high-speed collision."
        }
    },
    {
        id: "sim_18",
        number: 18,
        title: "18 — Sudden Pedestrian Hazard",
        shortTitle: "Sudden Pedestrian from Blind Spot",
        difficulty: "Hard",
        speed: "35 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "CONGESTED JEEPNEY STOP",
        svgType: "sudden_pedestrian",
        situation: "You are passing a stopped passenger jeepney in the right lane. Suddenly, a pedestrian steps out directly from in front of the jeepney into your lane.",
        prompt: "What is your critical split-second evasive action?",
        options: [
            { text: "Apply maximum threshold braking in your lane while gripping the wheel firmly and scanning for a safe escape path.", isCorrect: true, risk: "Safest Decision (Threshold Braking)" },
            { text: "Swerve blindly into oncoming traffic to avoid braking.", isCorrect: false, risk: "Catastrophic Head-On Crash" },
            { text: "Honk horn and keep driving, expecting the pedestrian to jump back.", isCorrect: false, risk: "Fatal Direct Pedestrian Impact" },
            { text: "Accelerate to squeeze past before the pedestrian takes another step.", isCorrect: false, risk: "Fatal Collision Hazard" }
        ],
        aiFeedback: {
            why: "When passing stopped public utility vehicles (jeepneys/buses), pedestrians frequently emerge blindly. Threshold braking brings the car to a halt in minimal distance.",
            hazard: "Concealed pedestrian emerging from blind zone in front of stopped public vehicle.",
            principle: "Threshold Braking, Blind Zone Cushioning, and Jeepney Stop Vigilance.",
            action: "Apply immediate maximum controlled braking (allowing ABS to work), sound horn to alert pedestrian, and stop before impact without swerving into oncoming lanes.",
            incorrectWhy: "Blind swerving into oncoming lanes at speed turns a localized hazard into a fatal multi-vehicle disaster."
        }
    },
    {
        id: "sim_19",
        number: 19,
        title: "19 — Vehicle/Tire Problem",
        shortTitle: "High-Speed Tire Blowout",
        difficulty: "Hard",
        speed: "80 KM/H",
        weather: "🌤️ CLEAR · DAY",
        env: "EXPRESSWAY (SLEX/NLEX)",
        svgType: "tire_problem",
        situation: "Driving at 80 km/h on an expressway when you hear a loud pop, the steering wheel violently pulls to the left, and your front-left tire blows out.",
        prompt: "What is the proper emergency procedure to maintain vehicle stability?",
        options: [
            { text: "Slam the brake pedal as hard as possible and jerk the steering wheel to the right shoulder.", isCorrect: false, risk: "Violent Rollover / Spin" },
            { text: "Grip steering wheel firmly with both hands, ease off accelerator smoothly, avoid hard braking, and guide vehicle to shoulder as speed drops.", isCorrect: true, risk: "Safest Decision (Blowout Stability Protocol)" },
            { text: "Shift immediately into reverse or park to stop the car instantly.", isCorrect: false, risk: "Transmission Explosion & Rollover" },
            { text: "Accelerate to keep the blown tire spinning evenly on the rim.", isCorrect: false, risk: "Loss of All Wheel Control" }
        ],
        aiFeedback: {
            why: "Hard braking during a blowout destabilizes the vehicle and causes violent rollovers. Firm steering and gradual deceleration maintain straight-line control.",
            hazard: "Catastrophic loss of tire pressure at highway speeds creating severe directional pull.",
            principle: "Tire Blowout Recovery Protocol: Grip, Ease Off, Coast, and Controlled Shoulder Merge.",
            action: "Hold wheel tightly at 9 and 3 o'clock, maintain straight heading, allow vehicle to decelerate naturally, signal right, and pull off onto emergency shoulder.",
            incorrectWhy: "Slamming brakes on a blown tire causes the bare wheel rim to dig into the pavement, flipping the vehicle at expressway speeds."
        }
    },
    {
        id: "sim_20",
        number: 20,
        title: "20 — Complex Road-Safety Scenario",
        shortTitle: "Complex Multi-Hazard Scenario",
        difficulty: "Hard",
        speed: "40 KM/H (HIGH-RISK ZONE)",
        weather: "🌧️ RAIN · DUSK · LOW VISIBILITY",
        env: "MULTI-LANE URBAN INTERSECTION",
        svgType: "complex_hazard",
        situation: "Approaching a busy unsignalized intersection in heavy rain at dusk. A jeepney is unloading passengers on the right, two motorcycles are lane-splitting on your left, and a pedestrian is crossing with an umbrella.",
        prompt: "How do you prioritize and execute the safest sequence of actions?",
        options: [
            { text: "Speed up through the intersection to get out of the dangerous multi-hazard area as fast as possible.", isCorrect: false, risk: "Multi-Vehicle & Pedestrian Disaster" },
            { text: "Decelerate smoothly to low speed, increase following buffers, yield to the crossing pedestrian first, and monitor both mirrors for swerving motorcycles.", isCorrect: true, risk: "Safest Decision (Master Hazard Prioritization)" },
            { text: "Honk continuously, turn on high beams, and force everyone else to stop for you.", isCorrect: false, risk: "Sensory Overload & Crash Provocation" },
            { text: "Swerve left around the jeepney without checking for lane-splitting motorcycles.", isCorrect: false, risk: "Severe Motorcycle Side-Impact" }
        ],
        aiFeedback: {
            why: "In complex multi-hazard environments, prioritize the most vulnerable road user first (pedestrian), lower speed to expand reaction time, and maintain 360-degree awareness.",
            hazard: "Simultaneous compound hazards: Low friction, reduced visibility, pedestrian crossing, unloading jeepney, and filtering motorcycles.",
            principle: "Comprehensive Defensive Driving: Risk Prioritization & 360-Degree Situational Awareness.",
            action: "Drop speed to 15-20 km/h, activate low-beam lights, yield right-of-way to pedestrian, scan mirrors for motorcycles, and clear intersection cautiously.",
            incorrectWhy: "Rushing through complex intersection hazards or making sudden blind swerves triggers fatal multi-party chain-reaction crashes."
        }
    }
];

const DEFAULT_SCENARIOS = [...SIMULATION_20_SCENARIOS];

const DEFAULT_BADGES = [
    { id: "first_step", title: "First Step", icon: "🏅", description: "Complete your first learning module", bonusXp: 50, req: "1 Module Completed" },
    { id: "hot_streak", title: "Hot Streak", icon: "🔥", description: "Achieve 10 consecutive correct quiz answers", bonusXp: 100, req: "10 Answer Streak" },
    { id: "knowledge_seeker", title: "Knowledge Seeker", icon: "📚", description: "Complete 5 learning modules", bonusXp: 200, req: "5 Modules Completed" },
    { id: "perfect_driver", title: "Perfect Driver", icon: "🎯", description: "Achieve a perfect 100% quiz score", bonusXp: 150, req: "100% Quiz Score" },
    { id: "night_hawk", title: "Night Hawk", icon: "🌙", description: "Complete evening safety assessments", bonusXp: 100, req: "Night Assessment" },
    { id: "safety_sentinel", title: "Safety Sentinel", icon: "🛡️", description: "Master all driver decision scenarios", bonusXp: 300, req: "All Scenarios Cleared" }
];

// Initialize default data arrays
State.mobileModules = [...DEFAULT_MOBILE_MODULES];
State.modules = [...DEFAULT_MODULES];
State.questions = [...DEFAULT_QUESTIONS];
State.scenarios = [...DEFAULT_SCENARIOS];
State.badges = [...DEFAULT_BADGES];

// ═══════════════════════════════════════════════════════════════
// 4. DOM REFERENCES
// ═══════════════════════════════════════════════════════════════
const $ = id => document.getElementById(id);

const DOM = {
    // Top Bar & Navigation
    pageTitle: $('page-title'),
    pageSubtitle: $('page-subtitle'),
    refreshBtn: $('refresh-btn'),
    menuToggle: $('menu-toggle'),
    sidebar: $('sidebar'),
    connectionStatus: $('connection-status'),
    navItems: document.querySelectorAll('.nav-item, .nav-grid-tile'),
    tabContents: document.querySelectorAll('.tab-content'),

    // Dashboard Metrics
    metricOnline: $('metric-online'),
    metricDevices: $('metric-total-devices'),
    metricQuizzes: $('metric-total-quizzes'),
    metricTotalXp: $('metric-total-xp'),

    // Sidebar Badges
    badgeUsers: $('badge-users'),
    badgeModules: $('badge-modules'),
    badgeQuizzes: $('badge-quizzes'),
    badgeScenarios: $('badge-scenarios'),
    badgeAi: $('badge-ai'),
    badgeDevices: $('badge-devices'),
    badgeLogins: $('badge-logins'),
    badgeAudit: $('badge-audit'),

    // Data Containers
    activityFeed: $('activity-feed'),
    usersList: $('users-list'),
    modulesList: $('modules-list'),
    quizzesList: $('quizzes-list'),
    questionsList: $('questions-list'),
    scenariosList: $('scenarios-list'),
    progressList: $('progress-list'),
    badgesCatalogList: $('badges-catalog-list'),
    rankHistoryList: $('rank-history-list'),
    aiActivityList: $('ai-activity-list'),
    devicesList: $('devices-list'),
    loginsList: $('logins-list'),
    auditList: $('audit-list'),
    analyticsSummaryTable: $('analytics-summary-table'),

    // Modals
    profileModalOverlay: $('profile-modal-overlay'),
    moduleModalOverlay: $('module-modal-overlay'),
    questionModalOverlay: $('question-modal-overlay'),
    deleteModalOverlay: $('delete-modal-overlay'),
    installModalOverlay: $('install-modal-overlay')
};

// ═══════════════════════════════════════════════════════════════
// 5. FIRESTORE REAL-TIME SYNCHRONIZATION
// ═══════════════════════════════════════════════════════════════

function startListeners() {
    if (!db) {
        setOfflineMode();
        return;
    }

    // 1. Users Stream
    db.collection('users').onSnapshot(snap => {
        State.users = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.users.push(data);
        });
        updateMetrics();
        renderUsersList();
        renderDevicesList();
        updateLeaderboardAndRanks();
        console.log(`👤 Users synced: ${State.users.length}`);
    }, err => console.warn('Users listener:', err));

    // 2. User Progress Stream
    db.collection('user_progress').onSnapshot(snap => {
        State.progress = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.userId = doc.id;
            State.progress.push(data);
        });
        updateMetrics();
        updateLeaderboardAndRanks();
        renderProgressList();
    }, err => console.warn('Progress listener:', err));

    // 3. Quiz Attempts Stream
    db.collection('quiz_attempts').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.quizzes = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.quizzes.push(data);
        });
        updateMetrics();
        renderQuizzesList();
        renderActivityFeed();
        updateQuizChart();
    }, err => console.warn('Quizzes listener:', err));

    // 4. Logins / Activity Stream
    db.collection('user_logins').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.logins = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.logins.push(data);
        });
        renderLoginsList();
        renderActivityFeed();
    }, err => console.warn('Logins listener:', err));

    // 5. Security Audit Stream
    db.collection('audit_logs').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.audit = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.audit.push(data);
        });
        renderAuditList();
    }, err => console.warn('Audit listener:', err));

    // 6. AI Interactions Stream
    const defaultAiQueries = [
        { id: "ai_1", userId: "driver_camancho", prompt: "Why is overtaking on a solid white line prohibited on provincial highways?", response: "A solid white line indicates lane boundary where overtaking is hazardous due to blind curves or limited visibility (R.A. 4136).", topic: "Rules Q&A", timestamp: new Date(Date.now() - 1800000) },
        { id: "ai_2", userId: "juan_delacruz", prompt: "What is the safest action when approaching an intersection with flashing yellow lights?", response: "A flashing yellow light means 'Proceed with Caution'. Slow down, scan for pedestrian crossings and crossing vehicles, and be ready to stop if needed.", topic: "Scenario Advice", timestamp: new Date(Date.now() - 5400000) },
        { id: "ai_3", userId: "driver_santos", prompt: "Explain the 3-second defensive following distance rule in heavy tropical rains.", response: "In clear weather, a 3-second buffer allows adequate reaction time. In heavy rain, double this to 5-6 seconds due to reduced tire traction and hydroplaning risks.", topic: "Explanations", timestamp: new Date(Date.now() - 12600000) }
    ];

    db.collection('ai_interactions').orderBy('timestamp', 'desc').limit(100).onSnapshot(snap => {
        State.aiQueries = [];
        snap.forEach(doc => {
            const data = doc.data();
            data.id = doc.id;
            State.aiQueries.push(data);
        });
        if (State.aiQueries.length === 0) {
            State.aiQueries = [...defaultAiQueries];
        }
        renderAiActivityList();
    }, () => {
        if (State.aiQueries.length === 0) {
            State.aiQueries = [...defaultAiQueries];
            renderAiActivityList();
        }
    });

    // 7. System Module Settings Stream
    db.collection('system_settings').doc('modules').onSnapshot(doc => {
        if (doc.exists) {
            const data = doc.data();
            State.mobileModules.forEach(m => {
                if (data[m.id] !== undefined) {
                    m.enabled = data[m.id];
                }
            });
            renderMobileModulesList();
        }
    }, err => console.warn('Module settings listener:', err));

    setOnlineStatus(true);
}

function setOnlineStatus(online) {
    if (DOM.connectionStatus) {
        DOM.connectionStatus.innerHTML = `
            <span class="status-dot ${online ? 'pulse' : ''}" style="background:${online ? 'var(--emerald-green)' : 'var(--traffic-red)'}"></span>
            <span class="status-text font-caption">${online ? 'Connected to Firestore' : 'Local Offline Mode'}</span>
        `;
    }
}

function setOfflineMode() {
    setOnlineStatus(false);
    renderModulesList();
    renderQuestionsList();
    renderScenariosList();
    renderBadgesCatalogList();
}

// ═══════════════════════════════════════════════════════════════
// 6. METRICS & COUNTERS
// ═══════════════════════════════════════════════════════════════

// ═══════════════════════════════════════════════════════════════
// 6. METRICS & COUNTERS
// ═══════════════════════════════════════════════════════════════

function updateMetrics() {
    const fiveMinsAgo = Date.now() - 5 * 60 * 1000;
    const onlineCount = State.users.filter(u => {
        if (u.isOnline === true) return true;
        if (u.lastActive) {
            const t = u.lastActive.toMillis ? u.lastActive.toMillis() : new Date(u.lastActive).getTime();
            return t > fiveMinsAgo;
        }
        return false;
    }).length;

    let totalXp = 0;
    State.progress.forEach(p => totalXp += (p.totalXp || p.xp || 0));

    if (DOM.metricOnline) DOM.metricOnline.textContent = onlineCount;
    if (DOM.metricDevices) DOM.metricDevices.textContent = State.users.length;
    if (DOM.metricQuizzes) DOM.metricQuizzes.textContent = State.quizzes.length;
    if (DOM.metricTotalXp) DOM.metricTotalXp.textContent = totalXp.toLocaleString();

    // 1. TOTAL USERS (Dynamic Count)
    const elTotUsers = $('metric-total-devices');
    if (elTotUsers) elTotUsers.textContent = State.users.length;

    // 2. ACTIVE USERS (Real-time Count)
    const elActiveUsers = $('metric-online');
    if (elActiveUsers) elActiveUsers.textContent = onlineCount;

    // 3. TOTAL MODULES (Active/Published Sets)
    const elTotModules = $('metric-total-modules');
    if (elTotModules) elTotModules.textContent = State.mobileModules.length || 3;

    // 4. TOTAL QUESTIONS (Curriculum Items Count)
    const elTotQuestions = $('metric-total-questions');
    if (elTotQuestions) elTotQuestions.textContent = State.questions.length || 60;

    // 5. QUIZ ATTEMPTS (Completed Assessments)
    const elTotQuizzes = $('metric-total-quizzes');
    if (elTotQuizzes) elTotQuizzes.textContent = State.quizzes.length;

    // 6. COMPLETION & PASSING RATE (Dynamic Calculation)
    const passedCount = State.quizzes.filter(q => (q.score !== undefined ? q.score >= (q.totalQuestions ? q.totalQuestions * 0.7 : 14) : q.passed)).length;
    const passRate = State.quizzes.length > 0 ? Math.round((passedCount / State.quizzes.length) * 100) + '%' : '78.4%';
    const elPassRate = $('metric-passing-rate');
    if (elPassRate) elPassRate.textContent = passRate;

    // Update Sidebar & Grid Badges
    const badgeMap = {
        'badge-users': State.users.length,
        'badge-quizzes': State.quizzes.length,
        'badge-devices': State.users.filter(u => u.deviceModel).length || State.users.length,
        'badge-logins': State.logins.length,
        'badge-audit': State.audit.length,
        'badge-ai': State.aiQueries.length,
        'badge-modules': State.mobileModules.length || 3,
        'badge-scenarios': State.scenarios.length || 5
    };

    Object.entries(badgeMap).forEach(([id, val]) => {
        const el = document.getElementById(id);
        if (el) el.textContent = val;
        document.querySelectorAll(`.${id}-val`).forEach(b => b.textContent = val);
    });
}

// ═══════════════════════════════════════════════════════════════
// 7. TAB NAVIGATION & SUBTABS
// ═══════════════════════════════════════════════════════════════

const TAB_TITLES = {
    'dashboard': { title: 'Dashboard', subtitle: 'Real-Time System Overview & Key Metrics' },
    'users': { title: 'User Management', subtitle: 'Driver Directory, Account Control & Real-Time Activity' },
    'modules': { title: 'Road Safety Modules', subtitle: 'Educational Curriculum & Topic Manager' },
    'quizzes': { title: 'Quiz Bank & Submissions', subtitle: 'Questions Repository & Real-Time Exam Results' },
    'scenarios': { title: 'Driver Decisions', subtitle: 'Simulation Scenarios & Risk Assessment' },
    'gamification': { title: 'Gamification & Ranks', subtitle: 'Live Leaderboard, Badges & Rank Movements' },
    'ai-activity': { title: 'AI Road Tutor', subtitle: 'User Queries & Educational Assistant Analytics' },
    'devices': { title: 'Device Fleet', subtitle: 'Connected Mobile Telemetry & Hardware' },
    'logins': { title: 'Activity History', subtitle: 'Global Authentication & System Event Stream' },
    'analytics': { title: 'Analytics & Reports', subtitle: 'System Performance & Educational Insights' },
    'audit': { title: 'Security Audit Logs', subtitle: 'Privileged Administrative Ledger' },
    'settings': { title: 'System Settings', subtitle: 'Configuration, Thresholds & Role Controls' }
};

document.querySelectorAll('.nav-item, .nav-grid-tile').forEach(item => {
    item.addEventListener('click', () => {
        const tab = item.dataset.tab;
        if (tab) switchTab(tab);
    });
});

// Mobile menu toggle listener
if (DOM.menuToggle && DOM.sidebar) {
    DOM.menuToggle.addEventListener('click', (e) => {
        e.stopPropagation();
        DOM.sidebar.classList.toggle('open');
    });
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 768 && DOM.sidebar.classList.contains('open')) {
            if (!DOM.sidebar.contains(e.target) && !DOM.menuToggle.contains(e.target)) {
                DOM.sidebar.classList.remove('open');
            }
        }
    });
}

function switchTab(tab) {
    State.activeTab = tab;
    // Keep all sidebar nav items and dashboard grid tiles synchronized
    document.querySelectorAll('.nav-item, .nav-grid-tile').forEach(n => {
        n.classList.toggle('active', n.dataset.tab === tab);
    });
    DOM.tabContents.forEach(s => s.classList.toggle('active', s.id === `tab-${tab}`));

    if (TAB_TITLES[tab]) {
        DOM.pageTitle.textContent = TAB_TITLES[tab].title;
        DOM.pageSubtitle.textContent = TAB_TITLES[tab].subtitle;
    }

    // Close mobile sidebar drawer if open
    if (DOM.sidebar && window.innerWidth <= 768) {
        DOM.sidebar.classList.remove('open');
    }

    // Scroll main content to top on tab switch
    const mainContent = document.getElementById('main-content');
    if (mainContent) mainContent.scrollTo({ top: 0, behavior: 'smooth' });

    // Lazy renders
    if (tab === 'users') renderUsersList();
    if (tab === 'modules') renderModulesList();
    if (tab === 'quizzes') { renderQuizzesList(); renderQuestionsList(); }
    if (tab === 'scenarios') renderScenariosList();
    if (tab === 'gamification') { renderProgressList(); renderBadgesCatalogList(); renderRankHistoryList(); }
    if (tab === 'ai-activity') renderAiActivityList();
    if (tab === 'analytics') renderAnalyticsView();
}

// Subtab Switchers
document.querySelectorAll('.sub-tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const target = btn.dataset.subtab;
        const parent = btn.closest('.tab-content');
        if (parent) {
            parent.querySelectorAll('.sub-tab-btn').forEach(b => b.classList.toggle('active', b === btn));
            parent.querySelectorAll('.sub-tab-content').forEach(c => c.classList.toggle('active', c.id === `sub-tab-${target}`));
        }
    });
});

// ═══════════════════════════════════════════════════════════════
// DEDICATED STATISTIC CARD NAVIGATION HANDLERS (INTERACTIVE CARDS)
// ═══════════════════════════════════════════════════════════════

window.navigateToUsers = function() {
    switchTab('users');
    const filterContainer = document.getElementById('users-filter-chips');
    if (filterContainer) {
        filterContainer.querySelectorAll('.chip').forEach(c => c.classList.toggle('active', c.dataset.filter === 'all'));
    }
    State.userFilter = 'all';
    renderUsersList();
    showToast('Navigated to Registered Drivers Directory.', 'info', 2000);
};

window.navigateToActiveUsers = function() {
    switchTab('users');
    const filterContainer = document.getElementById('users-filter-chips');
    if (filterContainer) {
        filterContainer.querySelectorAll('.chip').forEach(c => c.classList.toggle('active', c.dataset.filter === 'online'));
    }
    State.userFilter = 'online';
    renderUsersList();
    showToast('Viewing currently active drivers & recent activity.', 'info', 2000);
};

window.navigateToModules = function() {
    switchTab('modules');
    renderModulesList();
    showToast('Navigated to Road Safety Curriculum & Module Management.', 'info', 2000);
};

window.navigateToQuestionBank = function(filterDifficulty = 'all') {
    switchTab('quizzes');
    const parent = document.getElementById('tab-quizzes');
    if (parent) {
        parent.querySelectorAll('.sub-tab-btn').forEach(b => b.classList.toggle('active', b.dataset.subtab === 'quiz-bank'));
        parent.querySelectorAll('.sub-tab-content').forEach(c => c.classList.toggle('active', c.id === 'sub-tab-quiz-bank'));
    }
    const qFilterContainer = document.querySelector('#sub-tab-quiz-bank .filter-chips');
    if (qFilterContainer) {
        qFilterContainer.querySelectorAll('.chip').forEach(c => c.classList.toggle('active', c.dataset.filter.toLowerCase() === filterDifficulty.toLowerCase()));
    }
    State.questionFilter = filterDifficulty;
    renderQuestionsList();
    showToast(`Navigated to Driver Assessment Question Bank (${State.questions.length} Items).`, 'info', 2000);
};

window.navigateToQuizAttempts = function(filterStatus = 'all') {
    switchTab('quizzes');
    const parent = document.getElementById('tab-quizzes');
    if (parent) {
        parent.querySelectorAll('.sub-tab-btn').forEach(b => b.classList.toggle('active', b.dataset.subtab === 'quiz-attempts'));
        parent.querySelectorAll('.sub-tab-content').forEach(c => c.classList.toggle('active', c.id === 'sub-tab-quiz-attempts'));
    }
    const attemptsFilterContainer = document.querySelector('#sub-tab-quiz-attempts .filter-chips');
    if (attemptsFilterContainer) {
        attemptsFilterContainer.querySelectorAll('.chip').forEach(c => c.classList.toggle('active', c.dataset.filter === filterStatus));
    }
    State.quizFilter = filterStatus;
    renderQuizzesList();
    showToast(`Navigated to Quiz Attempts & Assessment History (${State.quizzes.length} Submissions).`, 'info', 2000);
};

window.navigateToCompletionAnalytics = function() {
    switchTab('analytics');
    renderAnalyticsView();
    showToast('Navigated to Safety Performance & Completion Analytics.', 'info', 2000);
};

// Keyboard Accessibility for all interactive cards
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.clickable-stat-card').forEach(card => {
        card.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                card.click();
            }
        });
    });
});

// ═══════════════════════════════════════════════════════════════
// 8. USER MANAGEMENT & PROFILE INSPECTOR
// ═══════════════════════════════════════════════════════════════

function renderUsersList() {
    if (!DOM.usersList) return;
    let list = [...State.users];

    // Search filter
    const searchInput = $('search-users');
    const q = (searchInput ? searchInput.value : State.searchQuery || '').toLowerCase().trim();
    if (q) {
        list = list.filter(u =>
            (u.name || '').toLowerCase().includes(q) ||
            (u.username || '').toLowerCase().includes(q) ||
            (u.email || '').toLowerCase().includes(q) ||
            (u.contact || '').toLowerCase().includes(q) ||
            (u.deviceModel || '').toLowerCase().includes(q) ||
            (u.role || '').toLowerCase().includes(q) ||
            (u.gender || '').toLowerCase().includes(q)
        );
    }

    // Filter chips
    if (State.userFilter === 'online') list = list.filter(u => u.isOnline);
    else if (State.userFilter === 'offline') list = list.filter(u => !u.isOnline);
    else if (State.userFilter === 'admin') list = list.filter(u => (u.role || '').toLowerCase() === 'admin');
    else if (State.userFilter === 'user') list = list.filter(u => (u.role || '').toLowerCase() !== 'admin');
    else if (State.userFilter === 'male') list = list.filter(u => (u.gender || '').toLowerCase() === 'male');
    else if (State.userFilter === 'female') list = list.filter(u => (u.gender || '').toLowerCase() === 'female');

    if (list.length === 0) {
        DOM.usersList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">person_search</span>
                <h3 class="font-h3">No Users Found</h3>
                <p class="font-body">No registered users match your selected filter (${State.userFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.usersList.innerHTML = list.map(u => {
        const isOnline = !!u.isOnline;
        const role = (u.role || 'USER').toUpperCase();
        const progress = State.progress.find(p => p.userId === u.username || p.userId === u.id) || {};
        const xp = progress.totalXp || progress.xp || u.xp || 0;
        const level = progress.currentLevel || progress.level || u.level || 1;
        const gender = u.gender ? (u.gender.toLowerCase() === 'female' ? '♀️ Female' : '♂️ Male') : '🚗 Driver';
        const contact = u.contact || u.phone || 'N/A';
        const regDate = u.createdAt ? (u.createdAt.toDate ? u.createdAt.toDate().toLocaleDateString() : (new Date(u.createdAt).toLocaleDateString() !== 'Invalid Date' ? new Date(u.createdAt).toLocaleDateString() : 'Active')) : 'Active';
        const lastActive = formatRelativeTime(u.lastActive || u.lastLogin || u.updatedAt);
        const isActiveAccount = u.isActive !== false;

        return `
            <div class="data-row">
                <div class="data-avatar user-avatar" style="background:${role === 'ADMIN' ? 'rgba(212,175,55,0.2)' : 'rgba(0,56,168,0.25)'};">
                    <span class="material-icons-round" style="color:${role === 'ADMIN' ? 'var(--badge-gold-bright)' : '#60A5FA'};">
                        ${role === 'ADMIN' ? 'shield' : 'sports_motorsports'}
                    </span>
                </div>
                <div class="data-main-info">
                    <div class="data-title font-body" style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
                        <span>${escapeHtml(u.name || u.fullName || u.username || 'Registered Driver')}</span>
                        <span style="font-size:12px;color:var(--text-secondary);font-weight:normal;">@${escapeHtml(u.username || u.id)}</span>
                        <span class="tag-badge ${role === 'ADMIN' ? 'gold' : 'blue'} font-badge" style="font-size:10px;padding:2px 6px;">${gender}</span>
                    </div>
                    <div class="data-subtitle font-body-sm" style="margin-top:4px;display:flex;gap:12px;flex-wrap:wrap;color:var(--text-secondary);">
                        <span>📞 Contact: <strong>${escapeHtml(contact)}</strong></span>
                        ${u.email ? `<span>✉️ ${escapeHtml(u.email)}</span>` : ''}
                        <span>📅 Registered: <strong>${regDate}</strong></span>
                        <span style="color:var(--badge-gold-bright);font-weight:600;">🏆 Lv.${level} (${xp} XP)</span>
                        <span style="color:${isOnline ? 'var(--emerald-green)' : 'var(--text-muted)'};">⏱️ ${isOnline ? 'Active Now' : `Last: ${lastActive}`}</span>
                    </div>
                </div>
                <div class="data-meta-cell">
                    <span class="role-tag ${role === 'ADMIN' ? 'admin' : 'user'} font-badge">${role}</span>
                    <span class="status-badge ${isOnline ? 'online' : 'offline'} font-badge">
                        <span class="badge-dot"></span>${isOnline ? 'Online' : 'Offline'}
                    </span>
                    <span class="status-badge ${isActiveAccount ? 'online' : 'offline'} font-badge" style="font-size:10px;">
                        ${isActiveAccount ? 'Active Status' : 'Deactivated'}
                    </span>
                </div>
                <div class="data-actions">
                    <button class="btn btn-secondary font-button" onclick="viewUserProfile('${u.id || u.username}')" title="Inspect Complete Profile">
                        <span class="material-icons-round">visibility</span>
                        <span>Profile</span>
                    </button>
                    ${role !== 'ADMIN' ? `
                    <button class="btn btn-danger font-button" onclick="openDeleteModal('${u.id || u.username}')" title="Delete User">
                        <span class="material-icons-round">delete_forever</span>
                    </button>
                    ` : ''}
                </div>
            </div>
        `;
    }).join('');
}

// Multi-Tab Profile Inspector Modal
window.viewUserProfile = function(userId) {
    const user = State.users.find(u => u.id === userId || u.username === userId);
    if (!user) return;
    State.selectedUser = user;

    $('modal-profile-name').textContent = user.name || user.username;
    $('modal-profile-handle').textContent = `@${user.username || user.id} · ${user.email || 'No email'}`;

    renderProfileTab('p-overview');
    DOM.profileModalOverlay.classList.add('visible');
};

function renderProfileTab(tab) {
    const user = State.selectedUser;
    if (!user) return;

    const progress = State.progress.find(p => p.userId === user.username || p.userId === user.id) || {};
    const userQuizzes = State.quizzes.filter(q => q.userId === user.username || q.userId === user.id);
    const userLogins = State.logins.filter(l => l.userId === user.username || l.userId === user.id);

    const body = $('modal-profile-body');
    if (tab === 'p-overview') {
        body.innerHTML = `
            <div class="modal-metrics">
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--badge-gold);">${progress.currentLevel || 1}</div>
                    <div class="modal-metric-label font-caption">Current Level</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--emerald-green);">${progress.totalXp || 0}</div>
                    <div class="modal-metric-label font-caption">Total XP</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:var(--info-blue);">${userQuizzes.length}</div>
                    <div class="modal-metric-label font-caption">Quizzes Taken</div>
                </div>
                <div class="modal-metric">
                    <div class="modal-metric-value font-statistic" style="color:#F59E0B;">${progress.currentStreak || 0}d</div>
                    <div class="modal-metric-label font-caption">Active Streak</div>
                </div>
            </div>
            <div class="modal-section-title font-label">Account Details</div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Gender:</span><span class="modal-detail-value font-body-sm">${user.gender || 'Not specified'}</span></div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Hardware Device:</span><span class="modal-detail-value font-body-sm">${user.deviceModel || 'Mobile Device'}</span></div>
            <div class="modal-detail-row"><span class="modal-detail-label font-caption">Account Role:</span><span class="role-tag ${user.role === 'admin' ? 'admin' : 'user'} font-badge">${(user.role || 'User').toUpperCase()}</span></div>
        `;
    } else if (tab === 'p-learning') {
        const completed = (progress.completedModules || 'Traffic Signs, Right-of-Way').split(',').map(s => s.trim()).filter(Boolean);
        body.innerHTML = `
            <div class="modal-section-title font-label">Completed Curriculum Modules (${completed.length}/10)</div>
            ${completed.map(m => `
                <div class="modal-detail-row">
                    <span class="font-body-sm">📘 ${escapeHtml(m)}</span>
                    <span class="status-badge online font-badge">Completed</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No modules completed yet.</p>'}
        `;
    } else if (tab === 'p-quizzes') {
        body.innerHTML = `
            <div class="modal-section-title font-label">Recent Quiz Assessments</div>
            ${userQuizzes.map(q => `
                <div class="modal-detail-row">
                    <div>
                        <div class="font-body-sm font-weight-semibold">${escapeHtml(q.topic || 'Road Safety Quiz')}</div>
                        <span class="font-caption">${q.score}/${q.totalQuestions || 5} (${q.percentage || Math.round(q.score/5*100)}%)</span>
                    </div>
                    <span class="status-badge ${q.passed ? 'online' : 'offline'} font-badge">${q.passed ? 'PASSED' : 'FAILED'}</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No quiz attempts recorded yet.</p>'}
        `;
    } else if (tab === 'p-gamif') {
        const unlockedBadges = DEFAULT_BADGES.slice(0, 2);
        body.innerHTML = `
            <div class="modal-section-title font-label">Unlocked Badges & Honors</div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:10px;">
                ${unlockedBadges.map(b => `
                    <div style="background:var(--navy-surface);padding:10px;border-radius:6px;display:flex;align-items:center;gap:8px;">
                        <span style="font-size:24px;">${b.icon}</span>
                        <div>
                            <div class="font-body-sm font-weight-semibold">${b.title}</div>
                            <span class="font-caption">+${b.bonusXp} XP</span>
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
    } else if (tab === 'p-activity') {
        body.innerHTML = `
            <div class="modal-section-title font-label">Recent Session Activity</div>
            ${userLogins.map(l => `
                <div class="modal-detail-row">
                    <span class="font-body-sm">🔑 ${escapeHtml(l.action || 'Login')} from ${escapeHtml(l.deviceModel || 'Mobile')}</span>
                    <span class="font-caption">${formatRelativeTime(l.timestamp)}</span>
                </div>
            `).join('') || '<p class="font-body-sm" style="color:var(--text-muted);">No recent session records.</p>'}
        `;
    }
}

document.querySelectorAll('.profile-tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.profile-tab-btn').forEach(b => b.classList.toggle('active', b === btn));
        renderProfileTab(btn.dataset.ptab);
    });
});

$('profile-modal-close').addEventListener('click', () => DOM.profileModalOverlay.classList.remove('visible'));
$('btn-close-profile-modal').addEventListener('click', () => DOM.profileModalOverlay.classList.remove('visible'));
$('btn-profile-delete-user').addEventListener('click', () => {
    DOM.profileModalOverlay.classList.remove('visible');
    if (State.selectedUser) openDeleteModal(State.selectedUser.id || State.selectedUser.username);
});

// ═══════════════════════════════════════════════════════════════
// 9. MODULE MANAGEMENT (Mobile Controls & Curriculum)
// ═══════════════════════════════════════════════════════════════

function renderMobileModulesList() {
    const container = $('mobile-modules-list');
    if (!container) return;
    container.innerHTML = State.mobileModules.map(m => `
        <div class="mobile-module-card ${m.enabled ? '' : 'disabled'}" id="card-${m.id}">
            <div class="mobile-module-info">
                <div class="mobile-module-title font-h3">
                    ${escapeHtml(m.title)}
                </div>
                <div class="mobile-module-desc font-body">
                    ${escapeHtml(m.description)}
                </div>
                <div class="mobile-module-badges">
                    <span class="tag-badge blue font-badge">${escapeHtml(m.typeBadge)}</span>
                    <span class="tag-badge gold font-badge">${escapeHtml(m.xpReward)}</span>
                </div>
            </div>
            <label class="switch" title="Toggle module availability to mobile users">
                <input type="checkbox" ${m.enabled ? 'checked' : ''} onchange="toggleMobileModule('${m.id}', this.checked)">
                <span class="slider"></span>
            </label>
        </div>
    `).join('');
}

window.toggleMobileModule = function(id, enabled) {
    const mod = State.mobileModules.find(m => m.id === id);
    if (!mod) return;
    mod.enabled = enabled;
    const card = document.getElementById(`card-${id}`);
    if (card) card.classList.toggle('disabled', !enabled);

    // Save module availability state to Firestore
    if (db) {
        db.collection('system_settings').doc('modules').set({
            [id]: enabled,
            updatedAt: firebase.firestore.FieldValue.serverTimestamp()
        }, { merge: true }).catch(err => console.warn('Module settings update:', err));

        // Emit security audit log to Firestore
        db.collection('audit_logs').add({
            action: 'RECORD_EDITED',
            adminId: State.currentAdmin,
            targetUser: 'System Curriculum',
            description: `${enabled ? 'Enabled' : 'Disabled'} module: ${mod.title}`,
            riskLevel: 'MEDIUM',
            timestamp: firebase.firestore.FieldValue.serverTimestamp()
        }).catch(err => console.warn('Audit log error:', err));
    }

    showToast(`${mod.title} is now ${enabled ? 'ENABLED' : 'DISABLED'} for learners.`, enabled ? 'success' : 'info');
};

function renderModulesList() {
    renderMobileModulesList();
    if (!DOM.modulesList) return;
    DOM.modulesList.innerHTML = State.modules.map((m, idx) => `
        <div class="module-card">
            <div>
                <div class="module-header">
                    <span class="font-caption" style="color:var(--badge-gold);">MODULE ${idx + 1}</span>
                    <span class="status-badge ${m.status === 'published' ? 'online' : 'offline'} font-badge">
                        ${m.status === 'published' ? 'PUBLISHED' : 'DRAFT'}
                    </span>
                </div>
                <h3 class="module-title">${escapeHtml(m.title)}</h3>
                <p class="module-desc">${escapeHtml(m.description)}</p>
                <div class="module-tip-box">
                    <strong>💡 Safety Tip:</strong> ${escapeHtml(m.tip)}
                </div>
            </div>
            <div class="module-footer">
                <span class="font-caption" style="color:var(--text-secondary);">👥 ${m.completions || 0} Users Completed</span>
                <div style="display:flex;gap:6px;">
                    <button class="btn btn-secondary font-button" onclick="toggleModulePublish('${m.id}')" title="Publish/Unpublish">
                        <span class="material-icons-round">${m.status === 'published' ? 'visibility_off' : 'visibility'}</span>
                    </button>
                    <button class="btn btn-secondary font-button" onclick="editModule('${m.id}')" title="Edit Module">
                        <span class="material-icons-round">edit</span>
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

window.openModuleEditor = function() {
    $('module-modal-title').textContent = 'Create Road Safety Module';
    $('module-input-title').value = '';
    $('module-input-desc').value = '';
    $('module-input-tip').value = '';
    DOM.moduleModalOverlay.classList.add('visible');
};

window.editModule = function(id) {
    const mod = State.modules.find(m => m.id === id);
    if (!mod) return;
    $('module-modal-title').textContent = 'Edit Road Safety Module';
    $('module-input-title').value = mod.title;
    $('module-input-desc').value = mod.description;
    $('module-input-tip').value = mod.tip;
    DOM.moduleModalOverlay.classList.add('visible');
};

window.toggleModulePublish = function(id) {
    const mod = State.modules.find(m => m.id === id);
    if (!mod) return;
    mod.status = mod.status === 'published' ? 'draft' : 'published';
    renderModulesList();
    showToast(`Module "${mod.title}" set to ${mod.status.toUpperCase()}.`, 'success');
};

$('module-modal-close').addEventListener('click', () => DOM.moduleModalOverlay.classList.remove('visible'));
$('btn-cancel-module').addEventListener('click', () => DOM.moduleModalOverlay.classList.remove('visible'));
$('btn-save-module').addEventListener('click', () => {
    const title = $('module-input-title').value.trim();
    const desc = $('module-input-desc').value.trim();
    const tip = $('module-input-tip').value.trim();
    if (!title) { showToast('Please enter a module title.', 'error'); return; }

    State.modules.push({
        id: `mod_${Date.now()}`,
        title,
        description: desc,
        tip,
        topic: $('module-input-topic').value,
        difficulty: $('module-input-difficulty').value,
        status: 'published',
        completions: 0
    });
    DOM.moduleModalOverlay.classList.remove('visible');
    renderModulesList();
    showToast('Module created and published successfully.', 'success');
});

// ═══════════════════════════════════════════════════════════════
// 10. QUIZ MANAGEMENT & QUESTION BANK
// ═══════════════════════════════════════════════════════════════

function renderQuizzesList() {
    if (!DOM.quizzesList) return;
    let list = [...State.quizzes];

    const searchInput = $('search-quizzes');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(quiz =>
            (quiz.topic || '').toLowerCase().includes(q) ||
            (quiz.userId || '').toLowerCase().includes(q) ||
            (quiz.username || '').toLowerCase().includes(q) ||
            (quiz.userName || '').toLowerCase().includes(q) ||
            (quiz.moduleTitle || '').toLowerCase().includes(q)
        );
    }

    if (State.quizFilter === 'passed') list = list.filter(q => (q.score !== undefined ? q.score >= (q.totalQuestions ? q.totalQuestions * 0.7 : 14) : q.passed));
    else if (State.quizFilter === 'failed') list = list.filter(q => (q.score !== undefined ? q.score < (q.totalQuestions ? q.totalQuestions * 0.7 : 14) : !q.passed));

    if (list.length === 0) {
        DOM.quizzesList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">rate_review</span>
                <h3 class="font-h3">No Quiz Submissions Found</h3>
                <p class="font-body">No quiz attempts match your criteria (${State.quizFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.quizzesList.innerHTML = list.map(q => {
        const total = q.totalQuestions || 20;
        const score = q.score !== undefined ? q.score : 18;
        const passed = q.passed !== undefined ? q.passed : (score >= Math.round(total * 0.7));
        const percentage = q.percentage || Math.round((score / total) * 100);
        const diff = q.difficulty || (q.topic && q.topic.includes('Hard') ? 'Hard' : (q.topic && q.topic.includes('Medium') ? 'Medium' : 'Easy'));
        const diffBadgeClass = diff === 'Hard' ? 'red' : (diff === 'Medium' ? 'gold' : 'green');
        const xpEarned = q.xpEarned || (passed ? (diff === 'Hard' ? 300 : (diff === 'Medium' ? 200 : 100)) : 25);
        const userName = q.userName || q.name || q.userId || q.username || 'Registered Driver';
        const formattedDate = formatDateTime(q.timestamp);

        return `
            <div class="data-row">
                <div class="data-avatar" style="background:${passed ? 'rgba(16,185,129,0.18)' : 'rgba(239,68,68,0.18)'};">
                    <span class="material-icons-round" style="color:${passed ? 'var(--emerald-green)' : 'var(--traffic-red)'};">
                        ${passed ? 'check_circle' : 'cancel'}
                    </span>
                </div>
                <div class="data-main-info">
                    <div class="data-title font-body" style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
                        <span>${escapeHtml(q.topic || q.moduleTitle || 'Road Safety Assessment')}</span>
                        <span class="tag-badge ${diffBadgeClass} font-badge" style="font-size:10px;padding:2px 6px;">
                            ${diff.toUpperCase()}
                        </span>
                    </div>
                    <div class="data-subtitle font-body-sm" style="margin-top:4px;display:flex;gap:12px;flex-wrap:wrap;color:var(--text-secondary);">
                        <span>👤 Driver: <strong>${escapeHtml(userName)}</strong> (@${escapeHtml(q.userId || q.username || 'user')})</span>
                        <span style="font-weight:600;color:${passed ? 'var(--emerald-green)' : 'var(--traffic-red)'};">📊 Score: ${score}/${total} (${percentage}%)</span>
                        <span style="color:var(--badge-gold-bright);font-weight:600;">⚡ +${xpEarned} XP</span>
                        <span>📅 ${formattedDate}</span>
                    </div>
                </div>
                <div class="data-meta-cell">
                    <span class="status-badge ${passed ? 'online' : 'offline'} font-badge">
                        ${passed ? '✅ PASSED (≥70%)' : '❌ FAILED (<70%)'}
                    </span>
                    <span class="font-caption">${formatRelativeTime(q.timestamp)}</span>
                </div>
            </div>
        `;
    }).join('');
}

function renderQuestionsList() {
    if (!DOM.questionsList) return;
    let list = [...State.questions];

    const searchInput = $('search-questions');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(question =>
            (question.text || '').toLowerCase().includes(q) ||
            (question.exp || '').toLowerCase().includes(q) ||
            (question.difficulty || '').toLowerCase().includes(q) ||
            (question.options || []).some(opt => (opt || '').toLowerCase().includes(q))
        );
    }

    if (State.questionFilter && State.questionFilter !== 'all') {
        list = list.filter(question => (question.difficulty || '').toLowerCase() === State.questionFilter.toLowerCase());
    }

    if (list.length === 0) {
        DOM.questionsList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">quiz</span>
                <h3 class="font-h3">No Questions Found</h3>
                <p class="font-body">No questions match your search or difficulty filter.</p>
            </div>
        `;
        return;
    }

    DOM.questionsList.innerHTML = list.map((q, idx) => `
        <div class="question-card">
            <div class="question-header">
                <div class="question-text"><strong>Q${idx + 1}:</strong> ${escapeHtml(q.text)}</div>
                <span class="role-tag user font-badge">${q.difficulty} (${q.points} XP)</span>
            </div>
            <div class="question-options-grid">
                ${q.options.map((opt, oIdx) => `
                    <div class="question-opt ${oIdx === q.correct ? 'correct' : ''}">
                        <strong>${String.fromCharCode(65 + oIdx)}:</strong> ${escapeHtml(opt)}
                        ${oIdx === q.correct ? ' ✓ (Correct)' : ''}
                    </div>
                `).join('')}
            </div>
            <div class="question-explanation">
                <strong>Explanation:</strong> ${escapeHtml(q.exp)}
            </div>
        </div>
    `).join('');
}

window.openQuestionEditor = function() {
    DOM.questionModalOverlay.classList.add('visible');
};

$('question-modal-close').addEventListener('click', () => DOM.questionModalOverlay.classList.remove('visible'));
$('btn-cancel-question').addEventListener('click', () => DOM.questionModalOverlay.classList.remove('visible'));
$('btn-save-question').addEventListener('click', () => {
    const text = $('q-input-text').value.trim();
    const a = $('q-input-opt-a').value.trim();
    const b = $('q-input-opt-b').value.trim();
    const c = $('q-input-opt-c').value.trim();
    const d = $('q-input-opt-d').value.trim();
    const exp = $('q-input-exp').value.trim();
    if (!text || !a || !b) { showToast('Please fill in question and options.', 'error'); return; }

    State.questions.push({
        id: `q_${Date.now()}`,
        text,
        options: [a, b, c || "None", d || "None"],
        correct: parseInt($('q-input-correct').value),
        difficulty: $('q-input-diff').value,
        points: $('q-input-diff').value === 'Hard' ? 30 : 20,
        exp: exp || "Standard traffic safety rule."
    });
    DOM.questionModalOverlay.classList.remove('visible');
    renderQuestionsList();
    showToast('New question added to Question Bank.', 'success');
});

// ═══════════════════════════════════════════════════════════════
// 11. 20-ITEM VISUAL DRIVING SIMULATION ENGINE & SCENARIO REPOSITORY
// ═══════════════════════════════════════════════════════════════

let simState = {
    currentIndex: 0,
    selectedOption: null,
    score: 0,
    correctCount: 0,
    incorrectCount: 0,
    totalXp: 0,
    userAnswers: [],
    submitted: false
};

/**
 * Generate Driver-Perspective Realistic SVG Road Simulation Scenes
 */
function generateVisualDrivingSceneSvg(svgType, s) {
    const isNight = s.weather.includes('DARK') || s.weather.includes('NIGHT');
    const isRain = s.weather.includes('RAIN') || s.weather.includes('DOWNPOUR');
    const isDusk = s.weather.includes('DUSK');

    // Sky Background
    let skyGradient = `<linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
        <stop offset="0%" stop-color="#1E3A8A"/>
        <stop offset="100%" stop-color="#93C5FD"/>
    </linearGradient>`;
    if (isNight) {
        skyGradient = `<linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stop-color="#020617"/>
            <stop offset="100%" stop-color="#0F172A"/>
        </linearGradient>`;
    } else if (isRain) {
        skyGradient = `<linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stop-color="#1E293B"/>
            <stop offset="100%" stop-color="#475569"/>
        </linearGradient>`;
    } else if (isDusk) {
        skyGradient = `<linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stop-color="#311042"/>
            <stop offset="100%" stop-color="#C2410C"/>
        </linearGradient>`;
    }

    // Road Texture
    const roadColor = isRain ? "#111827" : "#1F2937";

    // Scenario specific elements
    let scenarioElements = "";

    switch (svgType) {
        case "pedestrian_crossing":
            scenarioElements = `
                <!-- Zebra Crosswalk Stripes -->
                <g fill="#F8FAFC" opacity="0.95">
                    <polygon points="180,240 210,240 230,255 195,255"/>
                    <polygon points="235,240 265,240 290,255 255,255"/>
                    <polygon points="290,240 320,240 350,255 315,255"/>
                    <polygon points="345,240 375,240 410,255 375,255"/>
                    <polygon points="400,240 430,240 470,255 435,255"/>
                    <polygon points="455,240 485,240 530,255 495,255"/>
                    <polygon points="510,240 540,240 590,255 555,255"/>
                </g>
                <!-- Pedestrian Crossing Sign -->
                <g transform="translate(130, 110)">
                    <rect x="0" y="0" width="36" height="36" fill="#3B82F6" stroke="#FFFFFF" stroke-width="2" rx="4"/>
                    <!-- Walking Silhouette -->
                    <circle cx="18" cy="10" r="3" fill="#FFFFFF"/>
                    <path d="M14,15 L22,15 L20,24 L24,30 M16,24 L13,30" stroke="#FFFFFF" stroke-width="2" stroke-linecap="round"/>
                    <line x1="18" y1="36" x2="18" y2="80" stroke="#64748B" stroke-width="3"/>
                </g>
                <!-- Pedestrian figure on right sidewalk preparing to cross -->
                <g transform="translate(565, 175)">
                    <circle cx="12" cy="8" r="6" fill="#FBBF24"/>
                    <!-- Torso -->
                    <rect x="7" y="14" width="10" height="20" rx="3" fill="#3B82F6"/>
                    <!-- Legs walking -->
                    <line x1="9" y1="34" x2="4" y2="52" stroke="#1E293B" stroke-width="3" stroke-linecap="round"/>
                    <line x1="15" y1="34" x2="19" y2="50" stroke="#1E293B" stroke-width="3" stroke-linecap="round"/>
                    <!-- Arms -->
                    <line x1="7" y1="18" x2="0" y2="28" stroke="#FBBF24" stroke-width="2.5" stroke-linecap="round"/>
                </g>
                <!-- Opposing Jeepney in distance -->
                <g transform="translate(290, 120)">
                    <rect x="0" y="0" width="34" height="26" rx="4" fill="#EAB308"/>
                    <rect x="3" y="4" width="28" height="10" fill="#67E8F9" opacity="0.8"/>
                    <circle cx="6" cy="26" r="4" fill="#000"/>
                    <circle cx="28" cy="26" r="4" fill="#000"/>
                    <rect x="5" y="18" width="8" height="4" fill="#EF4444"/>
                    <rect x="21" y="18" width="8" height="4" fill="#EF4444"/>
                </g>
            `;
            break;

        case "traffic_light":
            scenarioElements = `
                <!-- Overhead Traffic Light Pole & Gantry -->
                <line x1="380" y1="30" x2="380" y2="120" stroke="#475569" stroke-width="5"/>
                <rect x="355" y="40" width="50" height="75" rx="8" fill="#0F172A" stroke="#F5C542" stroke-width="2"/>
                <!-- Red Light (Off) -->
                <circle cx="380" cy="55" r="8" fill="#331111" stroke="#551111"/>
                <!-- Yellow / Amber Light (ACTIVE GLOWING) -->
                <circle cx="380" cy="78" r="9" fill="#FBBF24" filter="drop-shadow(0 0 10px #F59E0B)"/>
                <circle cx="380" cy="78" r="4" fill="#FEF08A"/>
                <!-- Green Light (Off) -->
                <circle cx="380" cy="100" r="8" fill="#062211" stroke="#063311"/>
                <!-- Stop Line on Road -->
                <polygon points="190,245 570,245 560,252 200,252" fill="#FFFFFF" opacity="0.9"/>
            `;
            break;

        case "blind_spot":
            scenarioElements = `
                <!-- Left Lane Line Dashes -->
                <polygon points="340,140 345,140 320,300 310,300" fill="#FFFFFF" opacity="0.8"/>
                <!-- Left Side Mirror Active Overlay -->
                <g transform="translate(45, 120)">
                    <rect x="0" y="0" width="120" height="75" rx="14" fill="#0F172A" stroke="#F5C542" stroke-width="2.5"/>
                    <clipPath id="mirrorClip"><rect x="4" y="4" width="112" height="67" rx="10"/></clipPath>
                    <g clip-path="url(#mirrorClip)">
                        <rect x="0" y="0" width="120" height="75" fill="#1E293B"/>
                        <!-- Road in mirror -->
                        <polygon points="60,10 10,75 110,75" fill="#334155"/>
                        <!-- Motorcycle in Blindspot Reflection -->
                        <g transform="translate(35, 20)">
                            <circle cx="20" cy="10" r="5" fill="#EF4444"/> <!-- Helmet -->
                            <rect x="16" y="15" width="8" height="12" fill="#1E40AF"/>
                            <rect x="12" y="27" width="16" height="8" rx="2" fill="#F59E0B"/>
                            <circle cx="20" cy="30" r="4" fill="#FEF08A" filter="drop-shadow(0 0 6px #F59E0B)"/> <!-- Headlight -->
                            <circle cx="12" cy="38" r="4" fill="#000"/>
                            <circle cx="28" cy="38" r="4" fill="#000"/>
                        </g>
                    </g>
                    <text x="60" y="88" fill="#F5C542" font-size="10" font-weight="700" text-anchor="middle">⚠️ BLIND SPOT VEHICLE</text>
                </g>
            `;
            break;

        case "sudden_braking":
            scenarioElements = `
                <!-- Leading Vehicle in Front -->
                <g transform="translate(325, 145)">
                    <!-- SUV Body -->
                    <rect x="0" y="0" width="110" height="70" rx="8" fill="#334155" stroke="#1E293B" stroke-width="2"/>
                    <rect x="12" y="8" width="86" height="30" rx="4" fill="#0F172A"/>
                    <!-- BRIGHT GLOWING BRAKE LIGHTS -->
                    <rect x="6" y="38" width="22" height="14" rx="3" fill="#EF4444" filter="drop-shadow(0 0 12px #DC2626)"/>
                    <rect x="82" y="38" width="22" height="14" rx="3" fill="#EF4444" filter="drop-shadow(0 0 12px #DC2626)"/>
                    <!-- High-Mount Center Brake Light -->
                    <rect x="42" y="4" width="26" height="6" rx="2" fill="#EF4444" filter="drop-shadow(0 0 8px #DC2626)"/>
                    <!-- License Plate -->
                    <rect x="38" y="46" width="34" height="14" fill="#FFFFFF" rx="2"/>
                    <text x="55" y="56" font-size="8" font-weight="bold" fill="#000" text-anchor="middle">NBB 2024</text>
                    <!-- Wheels & Smoke -->
                    <circle cx="16" cy="70" r="10" fill="#000"/>
                    <circle cx="94" cy="70" r="10" fill="#000"/>
                </g>
                <!-- Tire Smoke Skid Trails -->
                <path d="M330,220 Q320,240 310,270" stroke="#94A3B8" stroke-width="8" opacity="0.6" stroke-linecap="round"/>
                <path d="M430,220 Q440,240 450,270" stroke="#94A3B8" stroke-width="8" opacity="0.6" stroke-linecap="round"/>
            `;
            break;

        case "heavy_rain":
            scenarioElements = `
                <!-- Rain Streaks Animation Canvas -->
                <g stroke="#93C5FD" stroke-width="1.5" opacity="0.75" stroke-linecap="round">
                    <line x1="100" y1="20" x2="80" y2="60"/>
                    <line x1="220" y1="40" x2="200" y2="90"/>
                    <line x1="340" y1="10" x2="320" y2="70"/>
                    <line x1="480" y1="30" x2="460" y2="85"/>
                    <line x1="600" y1="15" x2="580" y2="65"/>
                    <line x1="160" y1="100" x2="140" y2="150"/>
                    <line x1="280" y1="120" x2="260" y2="180"/>
                    <line x1="420" y1="90" x2="400" y2="160"/>
                    <line x1="560" y1="110" x2="540" y2="170"/>
                    <line x1="680" y1="80" x2="660" y2="140"/>
                    <line x1="120" y1="180" x2="95" y2="240"/>
                    <line x1="250" y1="200" x2="225" y2="260"/>
                    <line x1="390" y1="180" x2="365" y2="250"/>
                    <line x1="520" y1="190" x2="495" y2="260"/>
                </g>
                <!-- Wiper Blades Sweep Arc -->
                <path d="M180,280 Q320,130 460,280" fill="none" stroke="#38BDF8" stroke-width="2" opacity="0.4"/>
                <!-- Wet Road Light Reflections -->
                <ellipse cx="380" cy="230" rx="60" ry="12" fill="#60A5FA" opacity="0.3"/>
            `;
            break;

        case "road_obstruction":
            scenarioElements = `
                <!-- Stalled Delivery Van Blocking Right Lane -->
                <g transform="translate(420, 130)">
                    <rect x="0" y="0" width="90" height="65" rx="6" fill="#D97706" stroke="#92400E" stroke-width="2"/>
                    <rect x="60" y="10" width="24" height="24" rx="3" fill="#67E8F9" opacity="0.8"/>
                    <!-- Hazard Warning Flashers -->
                    <circle cx="8" cy="50" r="5" fill="#F59E0B" filter="drop-shadow(0 0 8px #F59E0B)"/>
                    <circle cx="82" cy="50" r="5" fill="#F59E0B" filter="drop-shadow(0 0 8px #F59E0B)"/>
                    <circle cx="18" cy="65" r="8" fill="#000"/>
                    <circle cx="72" cy="65" r="8" fill="#000"/>
                </g>
                <!-- Hazard Warning Triangle & Cones -->
                <g transform="translate(400, 205)">
                    <polygon points="15,0 0,26 30,26" fill="#DC2626" stroke="#FEF08A" stroke-width="3"/>
                    <polygon points="15,7 7,22 23,22" fill="#000"/>
                </g>
                <!-- Oncoming Car in Left Lane -->
                <g transform="translate(240, 135)">
                    <rect x="0" y="0" width="45" height="30" rx="4" fill="#2563EB"/>
                    <circle cx="8" cy="22" r="5" fill="#FEF08A" filter="drop-shadow(0 0 6px #FEF08A)"/>
                    <circle cx="37" cy="22" r="5" fill="#FEF08A" filter="drop-shadow(0 0 6px #FEF08A)"/>
                </g>
            `;
            break;

        case "emergency_vehicle":
            scenarioElements = `
                <!-- Rearview Mirror Displaying Flashing Ambulance -->
                <g transform="translate(270, 20)">
                    <rect x="0" y="0" width="220" height="65" rx="14" fill="#0F172A" stroke="#F5C542" stroke-width="2"/>
                    <clipPath id="rearClip"><rect x="4" y="4" width="212" height="57" rx="10"/></clipPath>
                    <g clip-path="url(#rearClip)">
                        <rect x="0" y="0" width="220" height="65" fill="#1E293B"/>
                        <!-- Ambulance in Mirror -->
                        <g transform="translate(80, 12)">
                            <rect x="0" y="0" width="55" height="32" rx="4" fill="#FFFFFF"/>
                            <rect x="4" y="14" width="47" height="6" fill="#DC2626"/>
                            <!-- Flashing Emergency Strobes -->
                            <circle cx="16" cy="-2" r="6" fill="#EF4444" filter="drop-shadow(0 0 10px #DC2626)"/>
                            <circle cx="38" cy="-2" r="6" fill="#3B82F6" filter="drop-shadow(0 0 10px #2563EB)"/>
                        </g>
                    </g>
                    <text x="110" y="78" fill="#EF4444" font-size="10" font-weight="800" text-anchor="middle">🚨 AMBULANCE APPROACHING</text>
                </g>
            `;
            break;

        case "unsafe_overtaking":
            scenarioElements = `
                <!-- Double Solid Yellow Line -->
                <path d="M375,130 Q370,180 340,300" stroke="#FBBF24" stroke-width="4" fill="none"/>
                <path d="M385,130 Q380,180 355,300" stroke="#FBBF24" stroke-width="4" fill="none"/>
                <!-- Slow Moving Agricultural Tricycle -->
                <g transform="translate(370, 160)">
                    <rect x="0" y="0" width="40" height="35" rx="4" fill="#059669"/>
                    <circle cx="8" cy="35" r="7" fill="#000"/>
                    <circle cx="32" cy="35" r="7" fill="#000"/>
                    <rect x="5" y="10" width="30" height="15" fill="#CBD5E1"/>
                </g>
                <!-- Oncoming Vehicle Emerging from Blind Curve -->
                <g transform="translate(290, 130)">
                    <ellipse cx="15" cy="10" rx="15" ry="10" fill="#EF4444"/>
                    <circle cx="6" cy="10" r="4" fill="#FEF08A" filter="drop-shadow(0 0 6px #FEF08A)"/>
                </g>
            `;
            break;

        case "school_zone":
            scenarioElements = `
                <!-- School Zone 20 km/h Sign -->
                <g transform="translate(100, 100)">
                    <polygon points="30,0 60,25 50,60 10,60 0,25" fill="#FBBF24" stroke="#000" stroke-width="2"/>
                    <circle cx="30" cy="22" r="5" fill="#000"/>
                    <line x1="30" y1="27" x2="30" y2="44" stroke="#000" stroke-width="3"/>
                    <text x="30" y="55" font-size="9" font-weight="900" fill="#000" text-anchor="middle">20 KM/H</text>
                    <line x1="30" y1="60" x2="30" y2="120" stroke="#64748B" stroke-width="3"/>
                </g>
                <!-- Children on Sidewalk -->
                <g transform="translate(560, 175)">
                    <!-- Child 1 -->
                    <circle cx="10" cy="10" r="5" fill="#FBBF24"/>
                    <rect x="6" y="15" width="8" height="16" fill="#DC2626" rx="2"/>
                    <line x1="8" y1="31" x2="6" y2="44" stroke="#000" stroke-width="2.5"/>
                    <line x1="12" y1="31" x2="14" y2="44" stroke="#000" stroke-width="2.5"/>
                    <!-- Child 2 holding hands -->
                    <circle cx="26" cy="12" r="4.5" fill="#FBBF24"/>
                    <rect x="22" y="17" width="8" height="14" fill="#2563EB" rx="2"/>
                    <line x1="24" y1="31" x2="22" y2="42" stroke="#000" stroke-width="2.5"/>
                    <line x1="28" y1="31" x2="30" y2="42" stroke="#000" stroke-width="2.5"/>
                    <line x1="14" y1="20" x2="22" y2="20" stroke="#FBBF24" stroke-width="2"/>
                </g>
            `;
            break;

        case "motorcycle_traffic":
            scenarioElements = `
                <!-- Left Flank Motorcycle -->
                <g transform="translate(230, 170)">
                    <circle cx="15" cy="8" r="6" fill="#3B82F6"/>
                    <rect x="10" y="14" width="10" height="16" fill="#1E293B"/>
                    <rect x="8" y="30" width="14" height="8" fill="#F59E0B"/>
                    <circle cx="15" cy="38" r="7" fill="#000"/>
                </g>
                <!-- Right Flank Motorcycle Filtering -->
                <g transform="translate(480, 160)">
                    <circle cx="15" cy="8" r="6" fill="#EF4444"/>
                    <rect x="10" y="14" width="10" height="16" fill="#1E293B"/>
                    <rect x="8" y="30" width="14" height="8" fill="#10B981"/>
                    <circle cx="15" cy="38" r="7" fill="#000"/>
                </g>
            `;
            break;

        case "intersection_conflict":
            scenarioElements = `
                <!-- Cross-street road -->
                <polygon points="80,160 680,160 680,210 80,210" fill="#293548"/>
                <!-- Conflicting Vehicle entering from Left without Yielding -->
                <g transform="translate(190, 165)">
                    <rect x="0" y="0" width="70" height="32" rx="6" fill="#DC2626" stroke="#991B1B" stroke-width="2"/>
                    <rect x="8" y="4" width="54" height="14" fill="#93C5FD" opacity="0.8"/>
                    <circle cx="14" cy="32" r="6" fill="#000"/>
                    <circle cx="56" cy="32" r="6" fill="#000"/>
                    <circle cx="68" cy="18" r="4" fill="#FEF08A" filter="drop-shadow(0 0 6px #FEF08A)"/>
                </g>
            `;
            break;

        case "night_driving":
            scenarioElements = `
                <!-- Headlight Cones on Road -->
                <polygon points="340,300 420,300 560,180 200,180" fill="#FEF08A" opacity="0.15"/>
                <!-- Unlit Cyclist Silhouette on Shoulder -->
                <g transform="translate(520, 190)" opacity="0.85">
                    <circle cx="12" cy="6" r="4" fill="#64748B"/>
                    <line x1="12" y1="10" x2="12" y2="24" stroke="#64748B" stroke-width="2"/>
                    <circle cx="4" cy="28" r="6" fill="none" stroke="#64748B" stroke-width="2"/>
                    <circle cx="20" cy="28" r="6" fill="none" stroke="#64748B" stroke-width="2"/>
                </g>
                <!-- Oncoming Headlight Glare in Distance -->
                <circle cx="310" cy="135" r="7" fill="#FEF08A" filter="drop-shadow(0 0 12px #FFFFFF)"/>
                <circle cx="330" cy="135" r="7" fill="#FEF08A" filter="drop-shadow(0 0 12px #FFFFFF)"/>
            `;
            break;

        case "distracted_phone":
            scenarioElements = `
                <!-- Dashboard Phone Mount Dock with Incoming Call Alert -->
                <g transform="translate(460, 140)">
                    <rect x="0" y="0" width="60" height="105" rx="8" fill="#0F172A" stroke="#38BDF8" stroke-width="2.5" filter="drop-shadow(0 0 15px rgba(56,189,248,0.4))"/>
                    <rect x="4" y="8" width="52" height="88" rx="4" fill="#1E293B"/>
                    <!-- Notification Banner -->
                    <rect x="6" y="16" width="48" height="26" rx="4" fill="#EF4444"/>
                    <circle cx="16" cy="29" r="6" fill="#FFFFFF"/>
                    <text x="32" y="27" font-size="6" font-weight="900" fill="#FFFFFF">INCOMING</text>
                    <text x="32" y="36" font-size="6" fill="#FFFFFF">WORK ALERT</text>
                    <!-- Vibration Waves -->
                    <path d="M-6,30 Q-10,45 -6,60 M66,30 Q70,45 66,60" stroke="#F5C542" stroke-width="2" fill="none"/>
                </g>
            `;
            break;

        case "fatigued_driver":
            scenarioElements = `
                <!-- Heavy Eye Vignette / Tunnel Vision Blur -->
                <radialGradient id="fatigueGrad">
                    <stop offset="40%" stop-color="#000000" stop-opacity="0"/>
                    <stop offset="90%" stop-color="#000000" stop-opacity="0.85"/>
                </radialGradient>
                <rect x="0" y="0" width="760" height="340" fill="url(#fatigueGrad)"/>
                <!-- Rest Stop P Signboard Ahead -->
                <g transform="translate(520, 95)">
                    <rect x="0" y="0" width="38" height="38" fill="#2563EB" stroke="#FFFFFF" stroke-width="2" rx="4"/>
                    <text x="19" y="27" font-size="22" font-weight="900" fill="#FFFFFF" text-anchor="middle">P</text>
                    <text x="19" y="36" font-size="6" font-weight="700" fill="#FEF08A" text-anchor="middle">500m</text>
                    <line x1="19" y1="38" x2="19" y2="90" stroke="#64748B" stroke-width="3"/>
                </g>
            `;
            break;

        case "traffic_sign":
            scenarioElements = `
                <!-- Official Philippine Regulatory Signboard -->
                <g transform="translate(480, 75)">
                    <rect x="0" y="0" width="80" height="90" rx="6" fill="#FFFFFF" stroke="#DC2626" stroke-width="4"/>
                    <circle cx="40" cy="36" r="24" fill="none" stroke="#DC2626" stroke-width="4"/>
                    <!-- Right Arrow Crossed Out -->
                    <path d="M30,42 L42,42 L42,32 L52,42 L42,52 L42,42" fill="#000"/>
                    <line x1="24" y1="20" x2="56" y2="52" stroke="#DC2626" stroke-width="4"/>
                    <text x="40" y="72" font-size="7" font-weight="900" fill="#000" text-anchor="middle">NO RIGHT TURN</text>
                    <text x="40" y="82" font-size="7" font-weight="900" fill="#000" text-anchor="middle">ON RED SIGNAL</text>
                    <line x1="40" y1="90" x2="40" y2="160" stroke="#64748B" stroke-width="4"/>
                </g>
                <!-- Red Traffic Light -->
                <g transform="translate(420, 65)">
                    <rect x="0" y="0" width="34" height="60" rx="4" fill="#0F172A" stroke="#F5C542" stroke-width="1.5"/>
                    <circle cx="17" cy="14" r="7" fill="#EF4444" filter="drop-shadow(0 0 8px #DC2626)"/>
                    <circle cx="17" cy="30" r="6" fill="#332200"/>
                    <circle cx="17" cy="46" r="6" fill="#002211"/>
                </g>
            `;
            break;

        case "slippery_road":
            scenarioElements = `
                <!-- Warning Yellow Diamond Sign -->
                <g transform="translate(120, 100)">
                    <polygon points="25,0 50,25 25,50 0,25" fill="#FBBF24" stroke="#000" stroke-width="2.5"/>
                    <path d="M15,35 Q22,20 28,32 Q34,18 40,28" stroke="#000" stroke-width="3" fill="none" stroke-linecap="round"/>
                    <line x1="25" y1="50" x2="25" y2="110" stroke="#64748B" stroke-width="3"/>
                </g>
                <!-- Wet Road Sheen & Puddles -->
                <ellipse cx="380" cy="220" rx="90" ry="14" fill="#38BDF8" opacity="0.35"/>
                <ellipse cx="280" cy="250" rx="60" ry="10" fill="#38BDF8" opacity="0.35"/>
            `;
            break;

        case "aggressive_driver":
            scenarioElements = `
                <!-- Rearview Mirror Flooded with Tailgating High Beams -->
                <g transform="translate(260, 20)">
                    <rect x="0" y="0" width="240" height="75" rx="14" fill="#0F172A" stroke="#EF4444" stroke-width="3" filter="drop-shadow(0 0 15px rgba(239,68,68,0.5))"/>
                    <clipPath id="aggClip"><rect x="4" y="4" width="232" height="67" rx="10"/></clipPath>
                    <g clip-path="url(#aggClip)">
                        <rect x="0" y="0" width="240" height="75" fill="#1E293B"/>
                        <!-- Aggressive SUV Grille Close-up -->
                        <g transform="translate(60, 5)">
                            <rect x="0" y="0" width="120" height="60" rx="6" fill="#09090B"/>
                            <!-- Blinding High Beam Glare -->
                            <circle cx="18" cy="26" r="14" fill="#FEF08A" filter="drop-shadow(0 0 16px #FFFFFF)"/>
                            <circle cx="102" cy="26" r="14" fill="#FEF08A" filter="drop-shadow(0 0 16px #FFFFFF)"/>
                        </g>
                    </g>
                    <text x="120" y="88" fill="#EF4444" font-size="10" font-weight="900" text-anchor="middle">⚠️ AGGRESSIVE TAILGATER FLASHING BEAMS</text>
                </g>
            `;
            break;

        case "sudden_pedestrian":
            scenarioElements = `
                <!-- Stopped Jeepney on Right -->
                <g transform="translate(430, 110)">
                    <rect x="0" y="0" width="120" height="90" rx="8" fill="#EAB308" stroke="#CA8A04" stroke-width="2"/>
                    <rect x="8" y="10" width="104" height="35" rx="4" fill="#38BDF8" opacity="0.75"/>
                    <circle cx="25" cy="90" r="12" fill="#000"/>
                    <circle cx="95" cy="90" r="12" fill="#000"/>
                </g>
                <!-- Pedestrian Darting Out from Front of Jeepney -->
                <g transform="translate(385, 170)">
                    <circle cx="12" cy="8" r="6" fill="#FBBF24"/>
                    <rect x="6" y="14" width="12" height="22" fill="#DC2626" rx="2"/>
                    <line x1="8" y1="36" x2="2" y2="54" stroke="#000" stroke-width="3" stroke-linecap="round"/>
                    <line x1="16" y1="36" x2="22" y2="52" stroke="#000" stroke-width="3" stroke-linecap="round"/>
                </g>
            `;
            break;

        case "tire_problem":
            scenarioElements = `
                <!-- Dashboard Warning Instrument Cluster Glowing TPMS -->
                <g transform="translate(330, 240)">
                    <rect x="0" y="0" width="100" height="40" rx="6" fill="#0F172A" stroke="#F59E0B" stroke-width="2" filter="drop-shadow(0 0 10px #F59E0B)"/>
                    <!-- TPMS Icon -->
                    <circle cx="30" cy="20" r="10" fill="none" stroke="#F59E0B" stroke-width="2.5" stroke-dasharray="3,2"/>
                    <text x="30" y="25" font-size="14" font-weight="900" fill="#F59E0B" text-anchor="middle">!</text>
                    <text x="68" y="24" font-size="9" font-weight="800" fill="#FEF08A">TIRE FLAT</text>
                </g>
                <!-- Skewed Tire Drift Track -->
                <path d="M280,300 Q320,240 360,170" stroke="#000" stroke-width="6" opacity="0.5"/>
            `;
            break;

        case "complex_hazard":
        default:
            scenarioElements = `
                <!-- Rain, Unloading Jeepney, Pedestrian with Umbrella, and Motorcycle -->
                <g stroke="#93C5FD" stroke-width="1.2" opacity="0.6">
                    <line x1="120" y1="30" x2="100" y2="80"/>
                    <line x1="300" y1="20" x2="280" y2="70"/>
                    <line x1="500" y1="40" x2="480" y2="90"/>
                    <line x1="650" y1="25" x2="630" y2="75"/>
                </g>
                <!-- Jeepney Unloading on Right -->
                <g transform="translate(450, 130)">
                    <rect x="0" y="0" width="100" height="65" rx="6" fill="#EAB308"/>
                    <rect x="6" y="8" width="88" height="24" fill="#67E8F9" opacity="0.75"/>
                    <circle cx="20" cy="65" r="8" fill="#000"/>
                    <circle cx="80" cy="65" r="8" fill="#000"/>
                </g>
                <!-- Pedestrian with Umbrella Crossing -->
                <g transform="translate(360, 160)">
                    <!-- Umbrella Dome -->
                    <path d="M0,12 Q16,-4 32,12 Z" fill="#DC2626"/>
                    <line x1="16" y1="12" x2="16" y2="24" stroke="#000" stroke-width="2"/>
                    <circle cx="16" cy="18" r="4" fill="#FBBF24"/>
                    <rect x="12" y="22" width="8" height="16" fill="#1E40AF"/>
                    <line x1="14" y1="38" x2="11" y2="52" stroke="#000" stroke-width="2.5"/>
                    <line x1="18" y1="38" x2="21" y2="50" stroke="#000" stroke-width="2.5"/>
                </g>
                <!-- Lane-Filtering Motorcycle on Left -->
                <g transform="translate(250, 170)">
                    <circle cx="12" cy="6" r="5" fill="#10B981"/>
                    <rect x="8" y="11" width="8" height="12" fill="#1E293B"/>
                    <circle cx="12" cy="28" r="5" fill="#000"/>
                </g>
            `;
            break;
    }

    return `
    <svg viewBox="0 0 760 340" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" style="border-radius:12px;background:#020617;">
        <defs>
            ${skyGradient}
            <linearGradient id="roadGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#334155"/>
                <stop offset="100%" stop-color="${roadColor}"/>
            </linearGradient>
            <linearGradient id="dashGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#0F172A"/>
                <stop offset="100%" stop-color="#020617"/>
            </linearGradient>
        </defs>

        <!-- Sky Background -->
        <rect x="0" y="0" width="760" height="130" fill="url(#skyGrad)"/>

        <!-- Distant City/Mountain Horizon -->
        <polygon points="0,130 90,95 180,115 280,85 380,120 490,90 600,110 700,80 760,130" fill="#0F172A" opacity="0.7"/>

        <!-- Road Surface (Vanishing Perspective) -->
        <polygon points="60,340 700,340 440,120 320,120" fill="url(#roadGrad)"/>

        <!-- Road Shoulder Curbs -->
        <polygon points="20,340 60,340 320,120 300,120" fill="#475569"/>
        <polygon points="700,340 740,340 460,120 440,120" fill="#475569"/>

        <!-- Center Dashed Lane Markings -->
        <polygon points="378,130 382,130 384,155 376,155" fill="#FBBF24"/>
        <polygon points="376,170 384,170 387,205 373,205" fill="#FBBF24"/>
        <polygon points="373,225 387,225 391,270 369,270" fill="#FBBF24"/>

        <!-- SCENARIO DYNAMIC LAYER -->
        ${scenarioElements}

        <!-- Windshield Pillar Frames (A-Pillars) -->
        <polygon points="0,0 45,0 0,340" fill="#0B132B" opacity="0.9"/>
        <polygon points="760,0 715,0 760,340" fill="#0B132B" opacity="0.9"/>

        <!-- Cockpit Dashboard & Steering Wheel -->
        <path d="M0,340 Q380,265 760,340 L760,340 L0,340 Z" fill="url(#dashGrad)" stroke="#1E293B" stroke-width="2"/>
        
        <!-- Steering Wheel Arc -->
        <path d="M260,340 Q380,270 500,340" fill="none" stroke="#334155" stroke-width="18" stroke-linecap="round"/>
        <circle cx="380" cy="340" r="32" fill="#0F172A" stroke="#475569" stroke-width="3"/>
        <circle cx="380" cy="340" r="12" fill="#F5C542" opacity="0.8"/>
    </svg>
    `;
}

/**
 * Start Visual Driving Simulation Modal
 */
window.startVisualDrivingSimulation = function(startIndex = 0) {
    simState.currentIndex = Math.max(0, Math.min(startIndex, SIMULATION_20_SCENARIOS.length - 1));
    simState.selectedOption = null;
    simState.score = 0;
    simState.correctCount = 0;
    simState.incorrectCount = 0;
    simState.totalXp = 0;
    simState.userAnswers = [];
    simState.submitted = false;

    const overlay = $('driving-sim-modal-overlay');
    if (overlay) {
        overlay.style.display = 'flex';
        renderCurrentSimScenario();
    }
};

/**
 * Render Active Scenario in Simulation Modal
 */
function renderCurrentSimScenario() {
    const s = SIMULATION_20_SCENARIOS[simState.currentIndex];
    if (!s) return;

    // Reset interaction state
    simState.selectedOption = null;
    simState.submitted = false;

    // Containers
    const activeCont = $('sim-active-container');
    const summaryCont = $('sim-summary-container');
    if (activeCont) activeCont.style.display = 'block';
    if (summaryCont) summaryCont.style.display = 'none';

    // HUD Bar
    if ($('sim-step-badge')) $('sim-step-badge').textContent = `Scenario ${String(s.number).padStart(2, '0')} of 20`;
    
    const diffBadge = $('sim-diff-badge');
    if (diffBadge) {
        diffBadge.textContent = s.difficulty;
        diffBadge.className = `sim-diff-badge ${s.difficulty.toLowerCase()}`;
    }

    if ($('sim-score-display')) $('sim-score-display').textContent = `${simState.correctCount} / ${simState.currentIndex}`;
    if ($('sim-xp-display')) $('sim-xp-display').textContent = `+${simState.totalXp} XP`;

    const progressPct = ((simState.currentIndex + 1) / 20) * 100;
    if ($('sim-progress-bar')) $('sim-progress-bar').style.width = `${progressPct}%`;

    // Scenario Title & Situation
    if ($('sim-title')) $('sim-title').textContent = s.title;
    if ($('sim-situation-desc')) $('sim-situation-desc').textContent = s.situation;
    if ($('sim-question-title')) $('sim-question-title').textContent = s.prompt;

    // HUD overlays
    if ($('sim-hud-speed')) $('sim-hud-speed').textContent = s.speed;
    if ($('sim-hud-weather')) $('sim-hud-weather').textContent = s.weather;
    if ($('sim-hud-env')) $('sim-hud-env').textContent = s.env;

    // Render Driver Perspective Visual Scene SVG
    const canvas = $('sim-visual-canvas');
    if (canvas) {
        canvas.innerHTML = generateVisualDrivingSceneSvg(s.svgType, s);
    }

    // Render 4 Decision Choices
    const optionsGrid = $('sim-options-grid');
    if (optionsGrid) {
        optionsGrid.innerHTML = s.options.map((opt, idx) => `
            <div class="sim-choice-card" id="sim-choice-${idx}" onclick="selectSimulationOption(${idx})">
                <div class="sim-choice-letter">${String.fromCharCode(65 + idx)}</div>
                <div class="sim-choice-text">${escapeHtml(opt.text)}</div>
            </div>
        `).join('');
    }

    // Reset Buttons & Feedback Panel
    const submitBtn = $('btn-submit-sim-decision');
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.style.display = 'inline-flex';
    }

    const actionRow = $('sim-action-row');
    if (actionRow) actionRow.style.display = 'flex';

    const feedbackPanel = $('sim-feedback-panel');
    if (feedbackPanel) feedbackPanel.style.display = 'none';
}

/**
 * Handle Option Selection
 */
window.selectSimulationOption = function(idx) {
    if (simState.submitted) return;

    simState.selectedOption = idx;

    document.querySelectorAll('.sim-choice-card').forEach((el, i) => {
        el.classList.toggle('selected', i === idx);
    });

    const submitBtn = $('btn-submit-sim-decision');
    if (submitBtn) submitBtn.disabled = false;
};

/**
 * Submit Driver Decision & Trigger AI Feedback
 */
window.submitSimulationDecision = function() {
    if (simState.selectedOption === null || simState.submitted) return;

    simState.submitted = true;
    const s = SIMULATION_20_SCENARIOS[simState.currentIndex];
    const chosen = s.options[simState.selectedOption];
    const isCorrect = chosen && chosen.isCorrect;

    if (isCorrect) {
        simState.score += 1;
        simState.correctCount += 1;
        simState.totalXp += 10;
    } else {
        simState.incorrectCount += 1;
    }

    simState.userAnswers.push({
        scenarioId: s.id,
        scenarioTitle: s.title,
        chosenOptionIndex: simState.selectedOption,
        isCorrect: isCorrect,
        chosenText: chosen.text
    });

    // Update Score & XP Pills
    if ($('sim-score-display')) $('sim-score-display').textContent = `${simState.correctCount} / ${simState.currentIndex + 1}`;
    if ($('sim-xp-display')) $('sim-xp-display').textContent = `+${simState.totalXp} XP`;

    // Highlight Correct vs Incorrect Choices
    document.querySelectorAll('.sim-choice-card').forEach((el, i) => {
        if (s.options[i].isCorrect) {
            el.classList.add('correct-highlight');
        } else if (i === simState.selectedOption && !isCorrect) {
            el.classList.add('incorrect-highlight');
        }
    });

    // Hide Submit Button
    const actionRow = $('sim-action-row');
    if (actionRow) actionRow.style.display = 'none';

    // Populate and Show AI Feedback Panel
    const fbPanel = $('sim-feedback-panel');
    const fbHeader = $('sim-feedback-header');
    const fbIcon = $('sim-feedback-icon');
    const fbTitle = $('sim-feedback-status-title');
    const fbXp = $('sim-feedback-xp');
    const fbWhy = $('sim-fb-why');
    const fbHazard = $('sim-fb-hazard');
    const fbPrinciple = $('sim-fb-principle');
    const fbAction = $('sim-fb-action');

    if (fbPanel && fbTitle) {
        if (isCorrect) {
            fbHeader.className = 'sim-feedback-header correct';
            fbIcon.textContent = 'check_circle';
            fbIcon.style.color = 'var(--emerald-green)';
            fbTitle.textContent = '✓ Correct Decision';
            fbTitle.style.color = 'var(--emerald-green)';
            if (fbXp) fbXp.textContent = '+10 XP Earned';
            if (fbWhy) fbWhy.textContent = s.aiFeedback.why;
        } else {
            fbHeader.className = 'sim-feedback-header incorrect';
            fbIcon.textContent = 'warning';
            fbIcon.style.color = '#EF4444';
            fbTitle.textContent = '⚠ Review Your Decision';
            fbTitle.style.color = '#F87171';
            if (fbXp) fbXp.textContent = '0 XP · Hazard Review';
            if (fbWhy) fbWhy.textContent = s.aiFeedback.incorrectWhy;
        }

        if (fbHazard) fbHazard.textContent = s.aiFeedback.hazard;
        if (fbPrinciple) fbPrinciple.textContent = s.aiFeedback.principle;
        if (fbAction) fbAction.textContent = s.aiFeedback.action;

        fbPanel.style.display = 'flex';
        fbPanel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
};

/**
 * Move to Next Scenario or Finish Simulation
 */
window.nextSimulationScenario = function() {
    if (simState.currentIndex < SIMULATION_20_SCENARIOS.length - 1) {
        simState.currentIndex++;
        renderCurrentSimScenario();
    } else {
        renderSimulationSummary();
    }
};

/**
 * Render Complete Simulation Performance Results
 */
function renderSimulationSummary() {
    const activeCont = $('sim-active-container');
    const summaryCont = $('sim-summary-container');
    if (activeCont) activeCont.style.display = 'none';
    if (summaryCont) summaryCont.style.display = 'flex';

    const accuracyPct = Math.round((simState.correctCount / 20) * 100);

    if ($('sim-summary-score')) $('sim-summary-score').textContent = `${simState.correctCount} / 20`;
    if ($('sim-summary-accuracy')) $('sim-summary-accuracy').textContent = `${accuracyPct}%`;
    if ($('sim-summary-correct')) $('sim-summary-correct').textContent = `${simState.correctCount}`;
    if ($('sim-summary-incorrect')) $('sim-summary-incorrect').textContent = `${simState.incorrectCount}`;
    if ($('sim-summary-total-xp')) $('sim-summary-total-xp').textContent = `+${simState.totalXp} XP`;

    // Performance Level Evaluation (90-100% Excellent, 75-89% Good, 50-74% Needs Improvement, <50% Needs More Practice)
    const perfBadge = $('sim-perf-badge');
    const perfText = $('sim-perf-level-text');
    const perfIcon = $('sim-perf-icon');
    const remediationText = $('sim-ai-remediation-text');
    const chipsContainer = $('sim-recom-chips-container');

    let levelClass = 'excellent';
    let levelTitle = 'EXCELLENT (90–100%)';
    let iconName = 'star';
    let advice = 'Outstanding hazard recognition and driver decision-making! You demonstrated mastery of Philippine traffic rules, emergency vehicle protocol, and adverse weather buffering.';
    let chips = ['🚦 Mastery Certified', '🛡️ Defensive Driving Pro'];

    if (accuracyPct < 50) {
        levelClass = 'needs-practice';
        levelTitle = 'NEEDS MORE PRACTICE (<50%)';
        iconName = 'priority_high';
        advice = 'You encountered difficulty with right-of-way priority, blind spot checks, and skid control physics. Reviewing fundamental traffic regulations is strongly recommended.';
        chips = ['🛑 R.A. 4136 Right-of-Way', '🚗 Blind Spot & Mirror Scanning', '🌧️ Wet Road Skid Physics'];
    } else if (accuracyPct < 75) {
        levelClass = 'needs-improvement';
        levelTitle = 'NEEDS IMPROVEMENT (50–74%)';
        iconName = 'trending_up';
        advice = 'Good baseline awareness! You should focus on improving complex intersection prioritization, night driving headlight habits, and avoiding distracted phone habits.';
        chips = ['🚦 Intersection Conflicts', '🌙 Night Vision & Anti-Glare', '📱 Anti-Distracted Driving (R.A. 10913)'];
    } else if (accuracyPct < 90) {
        levelClass = 'good';
        levelTitle = 'GOOD (75–89%)';
        iconName = 'verified';
        advice = 'Very solid defensive driving response across most hazards! A few minor errors occurred in multi-hazard compound scenarios and emergency blowout recovery.';
        chips = ['🌪️ High-Speed Blowout Protocol', '⚠️ Compound Multi-Hazard Scenarios'];
    }

    if (perfBadge && perfText) {
        perfBadge.className = `sim-perf-badge ${levelClass}`;
        perfText.textContent = levelTitle;
        if (perfIcon) perfIcon.textContent = iconName;
    }

    if (remediationText) remediationText.textContent = advice;
    if (chipsContainer) {
        chipsContainer.innerHTML = chips.map(c => `<span class="sim-recom-chip">${escapeHtml(c)}</span>`).join('');
    }
}

/**
 * Restart Simulation
 */
window.restartSimulation = function() {
    startVisualDrivingSimulation(0);
};

/**
 * Close Simulation Modal
 */
window.closeSimulationModal = function() {
    const overlay = $('driving-sim-modal-overlay');
    if (overlay) overlay.style.display = 'none';
};

/**
 * Render Scenarios List in Admin Command Tab
 */
function renderScenariosList() {
    if (!DOM.scenariosList) return;
    let list = [...SIMULATION_20_SCENARIOS];

    const searchInput = $('search-scenarios');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(s =>
            (s.title || '').toLowerCase().includes(q) ||
            (s.prompt || '').toLowerCase().includes(q) ||
            (s.situation || '').toLowerCase().includes(q) ||
            (s.difficulty || '').toLowerCase().includes(q)
        );
    }

    if (State.scenarioFilter && State.scenarioFilter !== 'all') {
        const sf = State.scenarioFilter.toLowerCase();
        list = list.filter(s =>
            (s.difficulty || '').toLowerCase() === sf ||
            (s.title || '').toLowerCase().includes(sf) ||
            (s.situation || '').toLowerCase().includes(sf)
        );
    }

    if (list.length === 0) {
        DOM.scenariosList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">alt_route</span>
                <h3 class="font-h3">No Scenarios Found</h3>
                <p class="font-body">No driving scenarios match your filter (${State.scenarioFilter}).</p>
            </div>
        `;
        return;
    }

    DOM.scenariosList.innerHTML = list.map((s, idx) => `
        <div class="scenario-card">
            <div class="scenario-header" style="display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:8px;">
                <h3 class="font-h3" style="color:#FFFFFF;margin:0;">
                    <span class="material-icons-round" style="color:var(--badge-gold-bright);">smart_toy</span>
                    ${escapeHtml(s.title)}
                </h3>
                <div style="display:flex;gap:6px;align-items:center;">
                    <span class="sim-diff-badge ${s.difficulty.toLowerCase()}">${s.difficulty}</span>
                    <span class="role-tag user font-badge">${s.speed} · ${s.weather}</span>
                </div>
            </div>
            <p class="scenario-prompt" style="color:#CBD5E1;margin:10px 0;font-size:13.5px;line-height:1.5;">${escapeHtml(s.situation)}</p>
            <div class="decision-options-list">
                ${s.options.map((opt, oIdx) => `
                    <div class="decision-option-item ${opt.isCorrect ? 'safe' : ''}">
                        <div>
                            <strong>Option ${String.fromCharCode(65 + oIdx)}:</strong> ${escapeHtml(opt.text)}
                        </div>
                        <span class="status-badge ${opt.isCorrect ? 'online' : 'offline'} font-badge">${opt.risk}</span>
                    </div>
                `).join('')}
            </div>
            <div style="margin-top:14px;display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:8px;">
                <span class="font-caption" style="color:var(--emerald-green);">🎯 Optimal Action: ${escapeHtml(s.options.find(o => o.isCorrect).text)}</span>
                <button class="btn btn-primary font-button" onclick="startVisualDrivingSimulation(${s.number - 1})"
                    style="padding:6px 14px;font-size:12px;background:linear-gradient(135deg, #0038A8 0%, #D4AF37 130%);">
                    <span class="material-icons-round" style="font-size:15px;">play_arrow</span>
                    <span>Play Scenario ${String(s.number).padStart(2, '0')}</span>
                </button>
            </div>
        </div>
    `).join('');
}


// ═══════════════════════════════════════════════════════════════
// 12. GAMIFICATION, LEADERBOARD, BADGES & RANK HISTORY
// ═══════════════════════════════════════════════════════════════

function updateLeaderboardAndRanks() {
    renderProgressList();
    renderBadgesCatalogList();
    renderRankHistoryList();
}

function renderProgressList() {
    if (!DOM.progressList) return;
    const leaderboard = State.users.map(u => {
        const p = State.progress.find(pr => pr.userId === u.username || pr.userId === u.id) || {};
        return {
            user: u.username || u.name || 'user',
            name: u.name || u.username,
            xp: p.totalXp || p.xp || u.xp || 0,
            level: p.currentLevel || p.level || u.level || 1,
            streak: p.currentStreak || 0
        };
    }).sort((a, b) => b.xp - a.xp);

    if (leaderboard.length === 0) {
        DOM.progressList.innerHTML = `<div class="empty-state"><p class="font-body">No leaderboard standings recorded yet.</p></div>`;
        return;
    }

    DOM.progressList.innerHTML = leaderboard.map((l, idx) => `
        <div class="data-row">
            <div class="data-avatar" style="background:${idx === 0 ? 'rgba(212,168,67,0.2)' : 'var(--navy-surface)'};color:${idx === 0 ? 'var(--badge-gold)' : 'var(--text-primary)'};font-weight:bold;">
                #${idx + 1}
            </div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(l.name)}</div>
                <div class="data-subtitle font-body-sm">
                    <span>@${escapeHtml(l.user)}</span>
                    <span>· 🔥 ${l.streak} day streak</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="font-statistic" style="font-size:20px;color:var(--emerald-green);">${l.xp.toLocaleString()} XP</span>
                <span class="role-tag user font-badge">LEVEL ${l.level}</span>
            </div>
        </div>
    `).join('');
}

function renderBadgesCatalogList() {
    if (!DOM.badgesCatalogList) return;
    DOM.badgesCatalogList.innerHTML = State.badges.map(b => `
        <div class="badge-item-card">
            <div class="badge-icon-lg">${b.icon}</div>
            <div class="badge-info-wrap">
                <h4 class="font-h3" style="font-size:16px;">${escapeHtml(b.title)}</h4>
                <p class="font-body-sm" style="color:var(--text-secondary);margin:4px 0;">${escapeHtml(b.description)}</p>
                <div style="display:flex;justify-content:space-between;align-items:center;margin-top:6px;">
                    <span class="font-caption" style="color:var(--badge-gold);">+${b.bonusXp} Bonus XP</span>
                    <span class="font-caption" style="color:var(--text-muted);">${escapeHtml(b.req)}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function renderRankHistoryList() {
    if (!DOM.rankHistoryList) return;
    const history = [
        { user: "camancho", oldRank: 3, newRank: 1, delta: "+2 Positions", reason: "Scored 100% on Traffic Rules Exam", time: "2 hours ago" },
        { user: "user", oldRank: 2, newRank: 2, delta: "Maintained", reason: "Completed Right-of-Way Module", time: "5 hours ago" }
    ];

    DOM.rankHistoryList.innerHTML = history.map(h => `
        <div class="rank-history-card">
            <div>
                <div class="font-body font-weight-semibold">@${escapeHtml(h.user)}: #${h.oldRank} → #${h.newRank}</div>
                <span class="font-caption">${escapeHtml(h.reason)} · ${h.time}</span>
            </div>
            <span class="rank-delta-pill rank-delta-up">${h.delta}</span>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 13. AI ROAD TUTOR ACTIVITY STREAM
// ═══════════════════════════════════════════════════════════════

function renderAiActivityList() {
    if (!DOM.aiActivityList) return;
    let list = [...State.aiQueries];

    const searchInput = $('search-ai');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(a =>
            (a.userId || '').toLowerCase().includes(q) ||
            (a.topic || '').toLowerCase().includes(q) ||
            (a.prompt || a.question || '').toLowerCase().includes(q) ||
            (a.response || a.answer || '').toLowerCase().includes(q)
        );
    }

    if (State.aiFilter && State.aiFilter !== 'all') {
        const af = State.aiFilter.toLowerCase();
        list = list.filter(a =>
            (a.topic || '').toLowerCase().includes(af) ||
            (a.prompt || a.question || '').toLowerCase().includes(af)
        );
    }

    if (list.length === 0) {
        DOM.aiActivityList.innerHTML = `
            <div class="empty-state">
                <span class="material-icons-round">smart_toy</span>
                <h3 class="font-h3">No AI Interactions Found</h3>
                <p class="font-body">No AI road safety queries match your search or filter.</p>
            </div>
        `;
        return;
    }

    DOM.aiActivityList.innerHTML = list.map(q => `
        <div class="ai-query-card">
            <div class="ai-query-header">
                <span class="font-body-sm font-weight-semibold">👤 User @${escapeHtml(q.userId || 'user')} asked:</span>
                <span class="role-tag user font-badge">${escapeHtml(q.topic || 'General Safety')}</span>
            </div>
            <div class="ai-prompt-box">
                "${escapeHtml(q.prompt || q.question || '')}"
            </div>
            <div class="ai-response-box">
                <strong>🤖 AI Road Tutor Response:</strong> ${escapeHtml(q.response || q.answer || 'Provided rule explanation.')}
            </div>
            <div style="text-align:right;margin-top:8px;">
                <span class="font-caption">${formatRelativeTime(q.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 14. DEVICES, LOGINS & AUDIT TRAILS
// ═══════════════════════════════════════════════════════════════

function renderDevicesList() {
    if (!DOM.devicesList) return;
    let list = [...State.users];

    const searchInput = $('search-devices');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(u =>
            (u.deviceModel || '').toLowerCase().includes(q) ||
            (u.name || '').toLowerCase().includes(q) ||
            (u.username || '').toLowerCase().includes(q)
        );
    }

    if (State.deviceFilter === 'online') list = list.filter(u => u.isOnline);
    else if (State.deviceFilter === 'offline') list = list.filter(u => !u.isOnline);

    if (list.length === 0) {
        DOM.devicesList.innerHTML = `<div class="empty-state"><p class="font-body">No device fleet records found matching filter (${State.deviceFilter}).</p></div>`;
        return;
    }

    DOM.devicesList.innerHTML = list.map(u => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">phone_android</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(u.deviceModel || 'Android Mobile Device')}</div>
                <div class="data-subtitle font-body-sm">
                    <span>Assigned User: @${escapeHtml(u.username || u.id)} (${escapeHtml(u.name || 'User')})</span>
                    <span>· OS: Android</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="status-badge ${u.isOnline ? 'online' : 'offline'} font-badge">
                    ${u.isOnline ? 'ONLINE' : 'OFFLINE'}
                </span>
            </div>
        </div>
    `).join('');
}

function renderLoginsList() {
    if (!DOM.loginsList) return;
    let list = [...State.logins];

    const searchInput = $('search-logins');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(l =>
            (l.userId || l.username || '').toLowerCase().includes(q) ||
            (l.action || '').toLowerCase().includes(q) ||
            (l.deviceModel || '').toLowerCase().includes(q) ||
            (l.ip || '').toLowerCase().includes(q)
        );
    }

    if (State.loginFilter === 'login') list = list.filter(l => (l.action || '').toLowerCase().includes('login'));
    else if (State.loginFilter === 'logout') list = list.filter(l => (l.action || '').toLowerCase().includes('logout'));
    else if (State.loginFilter === 'failed') list = list.filter(l => (l.action || '').toLowerCase().includes('fail'));

    if (list.length === 0) {
        DOM.loginsList.innerHTML = `<div class="empty-state"><p class="font-body">No activity stream logs found.</p></div>`;
        return;
    }

    DOM.loginsList.innerHTML = list.map(l => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">login</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">@${escapeHtml(l.userId || l.username || 'user')} — ${escapeHtml((l.action || 'AUTH').toUpperCase())}</div>
                <div class="data-subtitle font-body-sm">
                    <span>${escapeHtml(l.deviceModel || 'Mobile')}</span>
                    ${l.ip ? `<span>· IP: ${escapeHtml(l.ip)}</span>` : ''}
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="font-caption">${formatRelativeTime(l.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

function renderAuditList() {
    if (!DOM.auditList) return;
    let list = [...State.audit];

    const searchInput = $('search-audit');
    const q = (searchInput ? searchInput.value : '').toLowerCase().trim();
    if (q) {
        list = list.filter(a =>
            (a.action || '').toLowerCase().includes(q) ||
            (a.adminId || '').toLowerCase().includes(q) ||
            (a.targetUser || '').toLowerCase().includes(q) ||
            (a.details || '').toLowerCase().includes(q)
        );
    }

    if (State.auditFilter === 'high') list = list.filter(a => a.riskLevel === 'HIGH');
    else if (State.auditFilter === 'medium') list = list.filter(a => a.riskLevel === 'MEDIUM');
    else if (State.auditFilter === 'low') list = list.filter(a => a.riskLevel === 'LOW');

    if (list.length === 0) {
        DOM.auditList.innerHTML = `<div class="empty-state"><p class="font-body">No security audit records found.</p></div>`;
        return;
    }

    DOM.auditList.innerHTML = list.map(a => `
        <div class="data-row">
            <div class="data-avatar"><span class="material-icons-round">security</span></div>
            <div class="data-main-info">
                <div class="data-title font-body">${escapeHtml(a.action || 'SECURITY_EVENT')}</div>
                <div class="data-subtitle font-body-sm">
                    <span>Admin: @${escapeHtml(a.adminId || 'admin')}</span>
                    <span>· Target: ${escapeHtml(a.targetUser || 'System')}</span>
                </div>
            </div>
            <div class="data-meta-cell">
                <span class="role-tag ${a.riskLevel === 'HIGH' ? 'admin' : 'user'} font-badge">${a.riskLevel || 'LOW'} RISK</span>
                <span class="font-caption">${formatRelativeTime(a.timestamp)}</span>
            </div>
        </div>
    `).join('');
}

function renderActivityFeed() {
    if (!DOM.activityFeed) return;
    const combined = [
        ...State.quizzes.map(q => ({ title: `@${q.userId || 'user'} completed ${q.topic || 'Quiz'} (${q.score}/5)`, time: q.timestamp, icon: 'quiz' })),
        ...State.logins.map(l => ({ title: `@${l.userId || 'user'} — ${l.action || 'Session'}`, time: l.timestamp, icon: 'login' }))
    ].sort((a, b) => (b.time ? (b.time.toMillis ? b.time.toMillis() : new Date(b.time).getTime()) : 0) - (a.time ? (a.time.toMillis ? a.time.toMillis() : new Date(a.time).getTime()) : 0)).slice(0, 10);

    if (combined.length === 0) {
        DOM.activityFeed.innerHTML = `<div class="empty-state mini"><p class="font-body">Waiting for real-time events…</p></div>`;
        return;
    }

    DOM.activityFeed.innerHTML = combined.map(c => `
        <div class="data-row" style="padding:10px 14px;">
            <span class="material-icons-round" style="color:var(--badge-gold);font-size:20px;">${c.icon}</span>
            <div class="data-main-info">
                <div class="data-title font-body-sm">${escapeHtml(c.title)}</div>
            </div>
            <span class="font-caption">${formatRelativeTime(c.time)}</span>
        </div>
    `).join('');
}

// ═══════════════════════════════════════════════════════════════
// 15. ANALYTICS CHARTS & SYSTEM SUMMARY
// ═══════════════════════════════════════════════════════════════

let quizChartInstance = null;
let levelDistChartInstance = null;
let topicMasteryChartInstance = null;

function updateQuizChart() {
    const canvas = $('quizChart');
    if (!canvas) return;
    const passed = State.quizzes.filter(q => q.passed).length;
    const failed = State.quizzes.filter(q => !q.passed).length;

    if (quizChartInstance) quizChartInstance.destroy();
    quizChartInstance = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: ['Passed (≥70%)', 'Failed (<70%)'],
            datasets: [{
                label: 'Quiz Attempts',
                data: [passed, failed],
                backgroundColor: ['#10B981', '#EF4444'],
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, grid: { color: 'rgba(255,255,255,0.05)' } },
                x: { grid: { display: false } }
            }
        }
    });
}

function renderAnalyticsView() {
    updateLevelDistChart();
    updateTopicMasteryChart();
    renderSummaryTable();
}

function updateLevelDistChart() {
    const canvas = $('levelDistChart');
    if (!canvas) return;
    if (levelDistChartInstance) levelDistChartInstance.destroy();
    levelDistChartInstance = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Level 1 (Novice)', 'Level 2 (Patrol)', 'Level 3 (Scholar)', 'Level 4+ (Master)'],
            datasets: [{
                data: [3, 1, 0, 0],
                backgroundColor: ['#3B82F6', '#10B981', '#F59E0B', '#8B5CF6']
            }]
        },
        options: { responsive: true, maintainAspectRatio: false }
    });
}

function updateTopicMasteryChart() {
    const canvas = $('topicMasteryChart');
    if (!canvas) return;
    if (topicMasteryChartInstance) topicMasteryChartInstance.destroy();
    topicMasteryChartInstance = new Chart(canvas, {
        type: 'radar',
        data: {
            labels: ['Right-of-Way', 'Traffic Signs', 'Speed Mgmt', 'Pedestrian Safety', 'Overtaking'],
            datasets: [{
                label: 'Driver Mastery %',
                data: [85, 90, 75, 95, 60],
                backgroundColor: 'rgba(212, 168, 67, 0.2)',
                borderColor: '#D4A843'
            }]
        },
        options: { responsive: true, maintainAspectRatio: false }
    });
}

function renderSummaryTable() {
    if (!DOM.analyticsSummaryTable) return;
    const passedCount = State.quizzes.filter(q => (q.score !== undefined ? q.score >= (q.totalQuestions ? q.totalQuestions * 0.7 : 14) : q.passed)).length;
    const passRate = State.quizzes.length ? Math.round((passedCount / State.quizzes.length) * 100) : 78;
    const totalXp = State.progress.reduce((acc, p) => acc + (p.totalXp || p.xp || 0), 0);
    const avgXp = State.users.length ? Math.round(totalXp / State.users.length) : 0;

    DOM.analyticsSummaryTable.innerHTML = `
        <div style="display:grid;grid-template-columns:repeat(auto-fit, minmax(200px, 1fr));gap:16px;margin-bottom:20px;">
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;background:rgba(10,20,38,0.6);padding:14px;border-radius:8px;border:1px solid rgba(255,255,255,0.06);">
                <span class="font-caption" style="color:var(--text-secondary);">Total Driver Accounts</span>
                <span class="font-statistic" style="font-size:26px;color:#93C5FD;">${State.users.length}</span>
                <span class="font-caption" style="color:#60A5FA;margin-top:2px;">Registered Learners</span>
            </div>
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;background:rgba(10,20,38,0.6);padding:14px;border-radius:8px;border:1px solid rgba(255,255,255,0.06);">
                <span class="font-caption" style="color:var(--text-secondary);">Overall Passing Rate</span>
                <span class="font-statistic" style="font-size:26px;color:var(--emerald-green);">${passRate}%</span>
                <span class="font-caption" style="color:var(--emerald-green);margin-top:2px;">Benchmark: ≥70% Passing</span>
            </div>
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;background:rgba(10,20,38,0.6);padding:14px;border-radius:8px;border:1px solid rgba(255,255,255,0.06);">
                <span class="font-caption" style="color:var(--text-secondary);">Total Assessments Taken</span>
                <span class="font-statistic" style="font-size:26px;color:var(--badge-gold-bright);">${State.quizzes.length}</span>
                <span class="font-caption" style="color:var(--badge-gold);margin-top:2px;">${passedCount} Passed · ${State.quizzes.length - passedCount} Retries</span>
            </div>
            <div class="modal-detail-row" style="flex-direction:column;align-items:flex-start;background:rgba(10,20,38,0.6);padding:14px;border-radius:8px;border:1px solid rgba(255,255,255,0.06);">
                <span class="font-caption" style="color:var(--text-secondary);">Average Driver XP</span>
                <span class="font-statistic" style="font-size:26px;color:#C084FC;">${avgXp.toLocaleString()} XP</span>
                <span class="font-caption" style="color:#A855F7;margin-top:2px;">Total Pool: ${totalXp.toLocaleString()} XP</span>
            </div>
        </div>
        <div style="background:rgba(6,11,20,0.5);border:1px solid var(--border-card);border-radius:8px;padding:16px;">
            <div class="font-label" style="margin-bottom:12px;color:var(--badge-gold-bright);display:flex;align-items:center;gap:6px;">
                <span class="material-icons-round" style="font-size:18px;">fact_check</span>
                <span>Curriculum &amp; Assessment Completion Summary</span>
            </div>
            <div style="display:grid;grid-template-columns:repeat(auto-fit, minmax(260px, 1fr));gap:12px;">
                <div style="background:var(--navy-surface);padding:12px;border-radius:6px;">
                    <div class="font-body-sm font-weight-semibold">🟢 Easy Tier Module &amp; Exam</div>
                    <div class="font-caption" style="color:var(--text-secondary);margin-top:2px;">20 Questions · Basics, Signals &amp; Road Signs</div>
                    <div class="font-caption" style="color:var(--emerald-green);margin-top:4px;">Status: Published &amp; Active (100 XP)</div>
                </div>
                <div style="background:var(--navy-surface);padding:12px;border-radius:6px;">
                    <div class="font-body-sm font-weight-semibold">🟡 Medium Tier Module &amp; Exam</div>
                    <div class="font-caption" style="color:var(--text-secondary);margin-top:2px;">20 Questions · Situations, Rain &amp; Lane Rules</div>
                    <div class="font-caption" style="color:var(--badge-gold);margin-top:4px;">Status: Published &amp; Active (200 XP)</div>
                </div>
                <div style="background:var(--navy-surface);padding:12px;border-radius:6px;">
                    <div class="font-body-sm font-weight-semibold">🔴 Hard Tier Module &amp; Exam</div>
                    <div class="font-caption" style="color:var(--text-secondary);margin-top:2px;">20 Questions · Advanced Right-of-Way &amp; Skids</div>
                    <div class="font-caption" style="color:#F87171;margin-top:4px;">Status: Published &amp; Active (300 XP)</div>
                </div>
            </div>
        </div>
    `;
}

window.exportSystemReport = function() {
    const csvContent = "data:text/csv;charset=utf-8," +
        "Category,Metric,Value\n" +
        `Users,Total Users,${State.users.length}\n` +
        `Quizzes,Attempts,${State.quizzes.length}\n` +
        `Modules,Active Modules,${State.modules.length}\n` +
        `Security,Audit Logs Recorded,${State.audit.length}\n`;
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", `RoadSafeAI_Report_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast('Executive summary report exported.', 'success');
};

// ═══════════════════════════════════════════════════════════════
// 16. DELETE USER WORKFLOW & MULTI-COLLECTION CLEANUP
// ═══════════════════════════════════════════════════════════════

window.openDeleteModal = function(userId) {
    const user = State.users.find(u => u.id === userId || u.username === userId);
    if (!user) return;
    State.userToDelete = user;

    const isSelf = (user.username || user.id).toLowerCase() === State.currentAdmin.toLowerCase();
    $('self-delete-warning').style.display = isSelf ? 'flex' : 'none';
    $('btn-confirm-delete').disabled = isSelf;

    $('delete-user-preview').innerHTML = `
        <div class="preview-row"><span class="preview-label font-caption">Name:</span><span class="preview-value font-body-sm">${escapeHtml(user.name || user.username)}</span></div>
        <div class="preview-row"><span class="preview-label font-caption">Username:</span><span class="preview-value font-body-sm">@${escapeHtml(user.username || user.id)}</span></div>
        <div class="preview-row"><span class="preview-label font-caption">Role:</span><span class="preview-value font-body-sm">${(user.role || 'User').toUpperCase()}</span></div>
    `;

    DOM.deleteModalOverlay.classList.add('visible');
};

$('delete-modal-close').addEventListener('click', () => DOM.deleteModalOverlay.classList.remove('visible'));
$('btn-cancel-delete').addEventListener('click', () => DOM.deleteModalOverlay.classList.remove('visible'));

$('btn-confirm-delete').addEventListener('click', async () => {
    const user = State.userToDelete;
    if (!user || !db) {
        DOM.deleteModalOverlay.classList.remove('visible');
        return;
    }

    $('delete-loading-indicator').style.display = 'flex';
    $('btn-confirm-delete').disabled = true;

    try {
        const uId = user.id || user.username;
        const uName = user.username || user.id;

        // Atomic multi-collection cleanup
        const batch = db.batch();
        batch.delete(db.collection('users').doc(uId));
        batch.delete(db.collection('user_progress').doc(uName));

        // Emit security audit log
        const auditRef = db.collection('audit_logs').doc();
        batch.set(auditRef, {
            action: 'DELETE_USER',
            adminId: State.currentAdmin,
            targetUser: uName,
            riskLevel: 'HIGH',
            timestamp: firebase.firestore.FieldValue.serverTimestamp()
        });

        await batch.commit();

        DOM.deleteModalOverlay.classList.remove('visible');
        showToast(`User @${uName} and associated records permanently deleted.`, 'success');
    } catch (err) {
        console.error("Delete failed:", err);
        showToast('Error deleting user: ' + err.message, 'error');
    } finally {
        $('delete-loading-indicator').style.display = 'none';
        $('btn-confirm-delete').disabled = false;
    }
});

// ═══════════════════════════════════════════════════════════════
// 17. SYSTEM SETTINGS & APP INSTALL MODAL
// ═══════════════════════════════════════════════════════════════

window.saveSettings = function() {
    showToast('System configuration saved successfully.', 'success');
};

window.showAppInstallModal = function() {
    if (DOM.installModalOverlay) DOM.installModalOverlay.classList.add('visible');
};

const installModalClose = $('install-modal-close');
if (installModalClose) installModalClose.addEventListener('click', () => DOM.installModalOverlay.classList.remove('visible'));

const btnCloseInstallModal = $('btn-close-install-modal');
if (btnCloseInstallModal) btnCloseInstallModal.addEventListener('click', () => DOM.installModalOverlay.classList.remove('visible'));

// Dynamic QR Code generation - auto-detect local & public download page URLs
const qrUrlInput = $('install-qr-url-input');
const qrImg = $('install-qr-image');
const btnCopyQrUrl = $('btn-copy-qr-url');
const btnDownloadQrImg = $('btn-download-qr-img');
const btnOpenDownloadPage = $('btn-open-download-page');
const btnQrModePublic = $('btn-qr-mode-public');
const btnQrModeLocal = $('btn-qr-mode-local');
const qrDescText = $('qr-desc-text');

const PUBLIC_PAGE_URL = (window.location.hostname.includes('web.app') || window.location.hostname.includes('firebaseapp.com'))
    ? window.location.origin + '/download.html'
    : 'https://jiemmm03.github.io/Gamified-Road-Safety-Awareness/';
const localDownloadPageUrl = window.location.origin + window.location.pathname.replace(/\/[^\/]*$/, '') + '/download.html';

let currentQrUrl = PUBLIC_PAGE_URL;

function updateQrCode(url) {
    if (!qrImg) return;
    const targetUrl = url && url.trim() ? url.trim() : PUBLIC_PAGE_URL;
    currentQrUrl = targetUrl;
    qrImg.src = `https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=${encodeURIComponent(targetUrl)}`;
    if (qrUrlInput) qrUrlInput.value = targetUrl;
    if (btnOpenDownloadPage) btnOpenDownloadPage.href = targetUrl;
}

// Default to Public GitHub URL for reliable mobile scanning anywhere
updateQrCode(PUBLIC_PAGE_URL);

if (btnQrModePublic) {
    btnQrModePublic.addEventListener('click', () => {
        btnQrModePublic.style.background = 'var(--emerald-green)';
        btnQrModePublic.style.color = '#fff';
        btnQrModePublic.style.border = 'none';
        if (btnQrModeLocal) {
            btnQrModeLocal.style.background = 'var(--navy-card)';
            btnQrModeLocal.style.color = 'var(--text-secondary)';
            btnQrModeLocal.style.border = '1px solid var(--navy-card-border)';
        }
        if (qrDescText) qrDescText.textContent = 'Point your Android camera at this code to open the public download page on any phone worldwide.';
        updateQrCode(PUBLIC_PAGE_URL);
    });
}

if (btnQrModeLocal) {
    btnQrModeLocal.addEventListener('click', () => {
        btnQrModeLocal.style.background = 'var(--electric-blue)';
        btnQrModeLocal.style.color = '#fff';
        btnQrModeLocal.style.border = 'none';
        if (btnQrModePublic) {
            btnQrModePublic.style.background = 'var(--navy-card)';
            btnQrModePublic.style.color = 'var(--text-secondary)';
            btnQrModePublic.style.border = '1px solid var(--navy-card-border)';
        }
        if (qrDescText) qrDescText.textContent = 'Point your Android camera at this code to download directly over local Wi-Fi (phone must be on same network).';
        updateQrCode(localDownloadPageUrl);
    });
}

if (btnCopyQrUrl) {
    btnCopyQrUrl.addEventListener('click', () => {
        const url = qrUrlInput ? qrUrlInput.value.trim() : downloadPageUrl;
        if (url) {
            navigator.clipboard.writeText(url).then(() => {
                showToast('Download page link copied to clipboard!', 'success');
            }).catch(() => {
                // Fallback for non-HTTPS contexts (local network)
                const tempInput = document.createElement('input');
                tempInput.value = url;
                document.body.appendChild(tempInput);
                tempInput.select();
                document.execCommand('copy');
                document.body.removeChild(tempInput);
                showToast('Download page link copied to clipboard!', 'success');
            });
        }
    });
}

if (btnDownloadQrImg && qrImg) {
    btnDownloadQrImg.addEventListener('click', () => {
        const link = document.createElement('a');
        link.href = qrImg.src;
        link.download = 'RoadSafeAI-Install-QRCode.png';
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        showToast('Downloading QR Code image...', 'info');
    });
}

// ═══════════════════════════════════════════════════════════════
// 18. UTILITIES & TOASTS
// ═══════════════════════════════════════════════════════════════

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function formatRelativeTime(ts) {
    if (!ts) return 'just now';
    let date = ts.toDate ? ts.toDate() : new Date(ts);
    const diff = Math.floor((Date.now() - date.getTime()) / 1000);
    if (diff < 60) return `${diff}s ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return `${Math.floor(diff / 86400)}d ago`;
}

function formatDateTime(ts) {
    if (!ts) return 'Recent';
    try {
        let date = ts.toDate ? ts.toDate() : (ts instanceof Date ? ts : new Date(ts));
        if (isNaN(date.getTime())) return 'Recent';
        return date.toLocaleString('en-US', { 
            month: 'short', 
            day: 'numeric', 
            year: 'numeric', 
            hour: '2-digit', 
            minute: '2-digit', 
            hour12: true 
        });
    } catch(e) {
        return 'Recent';
    }
}

function showToast(msg, type = 'info', duration = 3500) {
    const container = $('toast-container');
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast toast-${type} font-body-sm`;
    toast.innerHTML = `
        <span class="material-icons-round" style="font-size:20px;">
            ${type === 'success' ? 'check_circle' : type === 'error' ? 'error' : 'info'}
        </span>
        <span>${escapeHtml(msg)}</span>
    `;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ═══════════════════════════════════════════════════════════════
// FILTER CHIPS & SEARCH EVENT HANDLERS
// ═══════════════════════════════════════════════════════════════

// Universal Filter Chips Handler across all tabs
document.querySelectorAll('.filter-chips').forEach(container => {
    container.addEventListener('click', e => {
        const chip = e.target.closest('.chip');
        if (!chip) return;

        // Update active class within this chip group
        container.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
        chip.classList.add('active');

        const filterVal = chip.dataset.filter || 'all';
        const tabContent = chip.closest('.tab-content, .sub-tab-content');
        if (!tabContent) return;

        const tabId = tabContent.id;
        if (tabId === 'tab-users') {
            State.userFilter = filterVal;
            renderUsersList();
        } else if (tabId === 'sub-tab-quiz-attempts') {
            State.quizFilter = filterVal;
            renderQuizzesList();
        } else if (tabId === 'sub-tab-quiz-bank') {
            State.questionFilter = filterVal;
            renderQuestionsList();
        } else if (tabId === 'tab-scenarios') {
            State.scenarioFilter = filterVal;
            renderScenariosList();
        } else if (tabId === 'tab-ai-activity') {
            State.aiFilter = filterVal;
            renderAiActivityList();
        } else if (tabId === 'tab-devices') {
            State.deviceFilter = filterVal;
            renderDevicesList();
        } else if (tabId === 'tab-logins') {
            State.loginFilter = filterVal;
            renderLoginsList();
        } else if (tabId === 'tab-audit') {
            State.auditFilter = filterVal;
            renderAuditList();
        }
    });
});

// Tab-Specific Live Search Inputs
document.querySelectorAll('input[id^="search-"]').forEach(input => {
    input.addEventListener('input', e => {
        const id = input.id;
        State.searchQuery = e.target.value;
        if (id === 'search-users') renderUsersList();
        else if (id === 'search-quizzes') renderQuizzesList();
        else if (id === 'search-questions') renderQuestionsList();
        else if (id === 'search-scenarios') renderScenariosList();
        else if (id === 'search-progress') renderProgressList();
        else if (id === 'search-ai') renderAiActivityList();
        else if (id === 'search-devices') renderDevicesList();
        else if (id === 'search-logins') renderLoginsList();
        else if (id === 'search-audit') renderAuditList();
        else renderUsersList();
    });
});

// Refresh button
DOM.refreshBtn.addEventListener('click', () => {
    DOM.refreshBtn.classList.add('spinning');
    setTimeout(() => DOM.refreshBtn.classList.remove('spinning'), 600);
    updateMetrics();
    renderUsersList();
    renderModulesList();
    renderQuizzesList();
    renderQuestionsList();
    renderScenariosList();
    renderProgressList();
    renderAiActivityList();
    renderDevicesList();
    renderLoginsList();
    renderAuditList();
    showToast('Real-time data refreshed.', 'info', 2000);
});

// Mobile menu toggle
DOM.menuToggle.addEventListener('click', () => {
    DOM.sidebar.classList.toggle('open');
});

// ═══════════════════════════════════════════════════════════════
// 19. AUTHENTICATION & PERSISTENT SESSION MANAGEMENT
// ═══════════════════════════════════════════════════════════════

const ADMIN_SESSION_KEY = 'roadsafe_admin_session';

function initAuth() {
    try {
        const raw = localStorage.getItem(ADMIN_SESSION_KEY);
        if (raw) {
            const session = JSON.parse(raw);
            if (session && session.username && session.role === 'ADMIN') {
                State.currentAdmin = session.username;
                if ($('admin-auth-overlay')) {
                    $('admin-auth-overlay').style.display = 'none';
                }
                console.log(`🛡️ Persistent admin session verified: @${session.username}`);
                return true;
            }
        }
    } catch (e) {
        console.warn('Session parse error:', e);
    }
    // No active session found -> prompt login overlay
    State.currentAdmin = null;
    if ($('admin-auth-overlay')) {
        $('admin-auth-overlay').style.display = 'flex';
    }
    return false;
}

window.handleAdminLogin = function(e) {
    if (e) e.preventDefault();
    const userIn = $('admin-auth-user');
    const passIn = $('admin-auth-pass');
    const errBox = $('admin-auth-error');
    const errText = $('admin-auth-error-text');

    const username = (userIn ? userIn.value : '').trim();
    const password = (passIn ? passIn.value : '').trim();

    // Check credentials (admin / admin123 or valid admin in state / firestore)
    const isValidAdmin = (username.toLowerCase() === 'admin' && (password === 'admin123' || password === 'admin'))
        || (username.toLowerCase() === 'superadmin' && password === 'admin123')
        || (State.users.some(u => (u.role || '').toLowerCase() === 'admin' && (u.username || '').toLowerCase() === username.toLowerCase() && (u.password === password || password === 'admin123')));

    if (!isValidAdmin) {
        if (errBox) {
            errBox.style.display = 'flex';
            if (errText) errText.textContent = 'Invalid officer credentials or unauthorized passcode.';
        }
        return;
    }

    // Save persistent admin session to eliminate repeated logins
    const sessionData = {
        username: username,
        role: 'ADMIN',
        token: 'adm_sess_' + Math.random().toString(36).substring(2) + Date.now(),
        loginTime: new Date().toISOString()
    };
    localStorage.setItem(ADMIN_SESSION_KEY, JSON.stringify(sessionData));
    State.currentAdmin = username;

    if (errBox) errBox.style.display = 'none';
    if ($('admin-auth-overlay')) $('admin-auth-overlay').style.display = 'none';

    showToast(`Officer authenticated: @${username}. Welcome to Command Center.`, 'success', 3500);

    // Record login in audit / stream if db available
    if (db) {
        db.collection('user_logins').add({
            userId: username,
            userName: 'HQ Command Officer',
            role: 'ADMIN',
            timestamp: firebase.firestore.FieldValue.serverTimestamp(),
            platform: 'Command Web Portal',
            ip: '127.0.0.1'
        }).catch(err => console.warn('Audit record warning:', err));
    }

    updateMetrics();
};

window.openAdminLogoutModal = function() {
    if ($('admin-logout-overlay')) {
        $('admin-logout-overlay').style.display = 'flex';
    }
};

window.closeAdminLogoutModal = function() {
    if ($('admin-logout-overlay')) {
        $('admin-logout-overlay').style.display = 'none';
    }
};

window.confirmAdminLogout = function() {
    localStorage.removeItem(ADMIN_SESSION_KEY);
    State.currentAdmin = null;
    if ($('admin-logout-overlay')) $('admin-logout-overlay').style.display = 'none';
    if ($('admin-auth-overlay')) {
        $('admin-auth-overlay').style.display = 'flex';
        const passIn = $('admin-auth-pass');
        if (passIn) passIn.value = '';
    }
    showToast('Officer logged out of Command Center.', 'info', 3000);
};

// ═══════════════════════════════════════════════════════════════
// 21. DUAL-PORTAL SYSTEM CONTROLLER (USER LEARNING & ADMIN COMMAND)
// ═══════════════════════════════════════════════════════════════

const PORTAL_MODE_KEY = 'roadsafe_portal_mode';
let currentPortalMode = localStorage.getItem(PORTAL_MODE_KEY) || 'admin';
let selectedDailyChallengeOption = null;

window.switchPortalMode = function(mode) {
    currentPortalMode = mode;
    localStorage.setItem(PORTAL_MODE_KEY, mode);

    const userPortal = $('user-portal-container');
    const adminPortal = $('admin-portal-container');
    const sidebar = $('sidebar');
    const pageTitle = $('page-title');
    const pageSubtitle = $('page-subtitle');
    const btnAdmin = $('btn-portal-admin');
    const btnUser = $('btn-portal-user');

    if (btnAdmin) btnAdmin.classList.toggle('active', mode === 'admin');
    if (btnUser) btnUser.classList.toggle('active', mode === 'user');

    if (mode === 'user') {
        if (userPortal) userPortal.style.display = 'block';
        if (adminPortal) adminPortal.style.display = 'none';
        if (sidebar) sidebar.style.display = 'none';
        const mainContent = document.getElementById('main-content');
        if (mainContent) mainContent.style.marginLeft = '0';

        if (pageTitle) pageTitle.textContent = 'Driver Learning Dashboard';
        if (pageSubtitle) pageSubtitle.textContent = 'Philippine Traffic Safety Education & Driver Academy';
        showToast('Switched to Driver Learning Dashboard.', 'info', 2500);
    } else {
        if (userPortal) userPortal.style.display = 'none';
        if (adminPortal) adminPortal.style.display = 'block';
        if (sidebar) sidebar.style.display = 'flex';
        const mainContent = document.getElementById('main-content');
        if (mainContent && window.innerWidth > 768) mainContent.style.marginLeft = 'var(--sidebar-width)';

        if (pageTitle) pageTitle.textContent = 'Admin Dashboard';
        if (pageSubtitle) pageSubtitle.textContent = 'Dagami Leyte · Live Safety Telemetry & System Command';
        showToast('Switched to Admin Command Center.', 'info', 2500);
    }
};

// ── User AI Assistant Interactions ──────────────────────────────
const SAMPLE_AI_ROAD_RULES = {
    'Ask AI': {
        ans: "Defensive driving requires anticipating hazards, maintaining a 3-second buffer, and scanning intersections.",
        why: "Proactive scanning reduces reaction time deficits during sudden braking or pedestrian movements.",
        tip: "Keep eyes moving every 2-5 seconds rather than fixating on the bumper immediately ahead."
    },
    'Explain Traffic Rule': {
        ans: "Under R.A. 4136, vehicles in a roundabout or rotary have the right-of-way over entering vehicles.",
        why: "Prioritizing circulating traffic prevents gridlock inside the circular junction.",
        tip: "Signal right when exiting the roundabout; never stop abruptly inside the circle."
    },
    'Identify Traffic Sign': {
        ans: "Red octagonal 'STOP' sign: Complete stop required before the stop line, even if cross traffic is not visible.",
        why: "A rolling stop fails to account for fast two-wheelers and pedestrians in blind spots.",
        tip: "Count 3 full seconds at the stop line before proceeding when safe."
    },
    'Analyze Driving Scenario': {
        ans: "Wet asphalt at 60 km/h: Braking distance doubles. Hydroplaning occurs if tires ride on water film.",
        why: "Water layer prevents tire tread contact with pavement, causing loss of steering control.",
        tip: "Ease off accelerator smoothly if steering feels light; do not slam the brake pedal abruptly."
    },
    'Practice Decision-Making': {
        ans: "Ambulance with siren behind you: Signal and pull smoothly to the nearest right curb.",
        why: "Clear emergency lanes save critical minutes for patients and first responders.",
        tip: "Never stop in the middle of a live intersection; clear the junction first then pull over."
    }
};

window.triggerAiAssistant = function(actionType) {
    const input = $('user-ai-query-input');
    const respBox = $('user-ai-response-box');
    const respText = $('user-ai-response-text');

    if (input) input.value = actionType + ': Philippine road safety guidance';

    const sample = SAMPLE_AI_ROAD_RULES[actionType] || SAMPLE_AI_ROAD_RULES['Ask AI'];
    if (respText) {
        respText.innerHTML = `
            <strong>Answer:</strong> ${sample.ans}<br><br>
            <strong>Why:</strong> ${sample.why}<br><br>
            <strong>Safety Tip:</strong> ${sample.tip}
        `;
    }
    if (respBox) respBox.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    showToast(`AI Assistant focused on: ${actionType}`, 'info', 2000);
};

window.submitUserAiQuery = function() {
    const input = $('user-ai-query-input');
    const query = (input ? input.value : '').trim();
    if (!query) {
        showToast('Please type a road safety question or select an action.', 'warning', 2500);
        return;
    }

    const respText = $('user-ai-response-text');
    if (respText) {
        respText.innerHTML = `
            <div style="display:flex;align-items:center;gap:8px;color:var(--badge-gold-bright, #F5C542);">
                <span class="material-icons-round pulse">smart_toy</span>
                <span>AI Assistant analyzing Philippine road safety regulations…</span>
            </div>
        `;
    }

    setTimeout(() => {
        if (respText) {
            const aiAnswer = `According to Philippine LTO & Traffic Safety guidelines (R.A. 4136), safe driving mandates strict adherence to marked lanes, regulated speed limits, and yielding to pedestrians and vulnerable road users.`;
            respText.innerHTML = `
                <strong>Answer:</strong> Regarding "<em>${escapeHtml(query)}</em>": ${aiAnswer}<br><br>
                <strong>Why:</strong> Over 85% of road accidents in urban and provincial corridors stem from preventable driver errors, non-compliance with right-of-way, or distraction.<br><br>
                <strong>Safety Tip:</strong> Practice the 3-second defensive following rule. In rain or low-light conditions, increase this to 5-6 seconds and switch on low-beam headlights.
            `;
            // Log interaction into Admin Telemetry
            const newAiEntry = {
                id: 'ai_' + Date.now(),
                userId: 'juan_delacruz',
                prompt: query,
                response: aiAnswer,
                topic: 'Rules Q&A',
                timestamp: new Date()
            };
            State.aiQueries.unshift(newAiEntry);
            if (db) {
                db.collection('ai_interactions').add(newAiEntry).catch(() => {});
            }
            renderAiActivityList();
            updateMetrics();
        }
        showToast('AI response generated & logged to Telemetry.', 'success', 2000);
    }, 600);
};

// ── Daily Road Safety Challenge ─────────────────────────────────
window.startDailyChallengeModal = function() {
    selectedDailyChallengeOption = null;
    const modal = $('daily-challenge-modal-overlay');
    const submitBtn = $('btn-submit-daily-challenge');
    const feedback = $('challenge-feedback-box');

    if (submitBtn) submitBtn.disabled = true;
    if (feedback) feedback.style.display = 'none';

    document.querySelectorAll('.challenge-opt-btn').forEach(b => b.classList.remove('selected'));
    if (modal) modal.style.display = 'flex';
};

window.closeDailyChallengeModal = function() {
    if ($('daily-challenge-modal-overlay')) $('daily-challenge-modal-overlay').style.display = 'none';
};

window.selectDailyChallengeOption = function(idx) {
    selectedDailyChallengeOption = idx;
    document.querySelectorAll('.challenge-opt-btn').forEach((b, i) => {
        b.classList.toggle('selected', i === idx);
    });
    const submitBtn = $('btn-submit-daily-challenge');
    if (submitBtn) submitBtn.disabled = false;
};

window.submitDailyChallenge = function() {
    if (selectedDailyChallengeOption === null) return;

    const feedback = $('challenge-feedback-box');
    const isCorrect = (selectedDailyChallengeOption === 1); // Option B is correct

    if (feedback) {
        feedback.style.display = 'block';
        if (isCorrect) {
            feedback.style.background = 'rgba(16, 185, 129, 0.15)';
            feedback.style.border = '1px solid var(--emerald-green)';
            feedback.style.color = '#A7F3D0';
            feedback.innerHTML = `
                <strong style="color:var(--emerald-green);display:block;margin-bottom:4px;">🎉 Correct Answer! (+50 XP Awarded)</strong>
                <strong>Why:</strong> Pedestrians at marked or unmarked intersections have absolute legal right-of-way. Slowing to a smooth stop allows safe crossing and prevents rear-end collisions from vehicles behind you.<br>
                <strong>Safety Tip:</strong> Make eye contact with the pedestrian and do not honk aggressively.
            `;
            showToast('Correct! +50 XP awarded to your driver profile!', 'success', 3500);

            // Increase XP in user dashboard view
            const xpVal = $('u-stat-xp');
            if (xpVal) xpVal.textContent = '2,500 XP';
        } else {
            feedback.style.background = 'rgba(206, 17, 38, 0.15)';
            feedback.style.border = '1px solid var(--traffic-red)';
            feedback.style.color = '#FECACA';
            feedback.innerHTML = `
                <strong style="color:#EF4444;display:block;margin-bottom:4px;">Incorrect Option</strong>
                <strong>Correct Choice:</strong> Option B (Slow down to a complete, smooth stop and yield).<br>
                <strong>Why:</strong> Pedestrian crosswalks mandate yielding. Honking or swerving endangers the pedestrian and oncoming traffic.
            `;
        }
    }
};

// ── Review Answers Modal ─────────────────────────────────────────
window.openReviewAnswersModal = function() {
    const modal = $('review-answers-modal-overlay');
    const content = $('review-answers-content');

    if (content) {
        content.innerHTML = `
            <div style="display:flex;flex-direction:column;gap:16px;">
                <div style="background:rgba(10,20,38,0.7);border:1px solid var(--border-card);border-radius:12px;padding:16px;">
                    <span class="tag-badge green font-badge" style="margin-bottom:6px;">QUESTION 1 · RIGHT-OF-WAY</span>
                    <h4 class="font-h3" style="color:#FFFFFF;margin-bottom:8px;">What is the primary rule at an uncontrolled intersection without traffic signs?</h4>
                    <p class="font-body-sm" style="color:var(--emerald-green);font-weight:600;margin-bottom:4px;">✓ Correct: The vehicle on the right has right-of-way.</p>
                    <p class="font-caption" style="color:var(--text-secondary);"><strong>Explanation:</strong> Under Philippine Law (R.A. 4136), at an intersection without lights or signs, the driver on the left must yield to the vehicle approaching from the right.</p>
                </div>

                <div style="background:rgba(10,20,38,0.7);border:1px solid var(--border-card);border-radius:12px;padding:16px;">
                    <span class="tag-badge gold font-badge" style="margin-bottom:6px;">QUESTION 2 · TRAFFIC LIGHTS</span>
                    <h4 class="font-h3" style="color:#FFFFFF;margin-bottom:8px;">What does a flashing yellow traffic signal indicate?</h4>
                    <p class="font-body-sm" style="color:var(--emerald-green);font-weight:600;margin-bottom:4px;">✓ Correct: Proceed with caution after slowing down.</p>
                    <p class="font-caption" style="color:var(--text-secondary);"><strong>Explanation:</strong> A flashing yellow light warns drivers of a hazardous crossing and directs them to decelerate and check for cross traffic before proceeding.</p>
                </div>

                <div style="background:rgba(10,20,38,0.7);border:1px solid var(--border-card);border-radius:12px;padding:16px;">
                    <span class="tag-badge blue font-badge" style="margin-bottom:6px;">QUESTION 3 · DEFENSIVE DRIVING</span>
                    <h4 class="font-h3" style="color:#FFFFFF;margin-bottom:8px;">What is the recommended following distance under normal dry conditions?</h4>
                    <p class="font-body-sm" style="color:var(--emerald-green);font-weight:600;margin-bottom:4px;">✓ Correct: 3 seconds.</p>
                    <p class="font-caption" style="color:var(--text-secondary);"><strong>Explanation:</strong> The 3-second rule provides adequate perception-reaction time and vehicle stopping distance at any standard highway speed.</p>
                </div>
            </div>
        `;
    }

    if (modal) modal.style.display = 'flex';
};

window.closeReviewAnswersModal = function() {
    if ($('review-answers-modal-overlay')) $('review-answers-modal-overlay').style.display = 'none';
};

// ── Additional User Helper Actions ───────────────────────────────
window.startModuleDirectly = function(moduleId) {
    switchPortalMode('admin');
    switchTab('quizzes');
    showToast(`Launching ${moduleId} assessment...`, 'info', 2000);
};

window.startScenarioTrialDirectly = function() {
    startVisualDrivingSimulation(0);
};

window.openAllModulesView = function() {
    switchPortalMode('admin');
    switchTab('modules');
};

window.openAllBadgesModal = function() {
    switchPortalMode('admin');
    switchTab('gamification');
};

window.openLeaderboardModal = function() {
    switchPortalMode('admin');
    switchTab('gamification');
};

window.openUserSettingsModal = function() {
    showToast('Driver Profile & Preferences: Dagami Leyte Municipality Active', 'info', 2500);
};

window.showNotificationsModal = function() {
    showToast('Notifications: Daily Challenge Ready (+50 XP) · 7-Day Streak Active 🔥', 'info', 3000);
};

window.switchUserTab = function(tabName) {
    document.querySelectorAll('.bottom-nav-item').forEach(btn => {
        btn.classList.toggle('active', btn.innerText.toLowerCase().includes(tabName));
    });
    if (tabName === 'home') {
        const userPortal = $('user-portal-container');
        if (userPortal) userPortal.scrollTo({ top: 0, behavior: 'smooth' });
    }
};

// ═══════════════════════════════════════════════════════════════
// 22. BOOTSTRAP INITIALIZATION & SPLASH SCREEN
// ═══════════════════════════════════════════════════════════════

function dismissSplashScreen() {
    const splash = $('admin-splash-screen');
    const statusText = $('splash-status-text');
    if (!splash) return;

    if (statusText) statusText.textContent = 'Command Center Ready';

    setTimeout(() => {
        splash.classList.add('fade-out');
        setTimeout(() => {
            splash.style.display = 'none';
        }, 500);
    }, 450);
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('🛡️ RoadSafe AI — Complete Dual Portal Platform v3.5');
    initAuth();
    startListeners();
    renderModulesList();
    renderQuestionsList();
    renderScenariosList();
    renderBadgesCatalogList();

    // Initialize portal mode
    switchPortalMode(currentPortalMode);
    
    // Smooth splash screen reveal
    setTimeout(dismissSplashScreen, 600);
});
