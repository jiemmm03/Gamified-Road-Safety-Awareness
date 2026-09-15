package com.example.gamifiedroadsafetyawareness.model

/**
 * Deterministic topic ontology for the Philippine Road Safety AI Assistant.
 * Each topic has a display name, keyword list for classification, a concise English explanation,
 * a Filipino/Tagalog explanation, and bilingual safety tips.
 *
 * Keywords are matched against quiz question text and free-text user queries.
 * Anything unmatched falls back to ROAD_COURTESY.
 *
 * Content is grounded in Philippine traffic law (R.A. 4136), DPWH road-sign standards,
 * LTO driver's manuals, and the Anti-Distracted Driving Act (R.A. 10913).
 * Penalty amounts and specific schedules are intentionally omitted — users are directed
 * to verify current figures with the official LTO source.
 */
enum class RoadSafetyTopic(
    val displayName: String,
    val keywords: List<String>,
    val explanation: String,
    val explanationFil: String,
    val safetyTip: String,
    val safetyTipFil: String
) {
    RIGHT_OF_WAY(
        displayName = "Right-of-Way",
        keywords = listOf(
            "right of way", "right-of-way", "yield", "priority", "who goes first",
            "intersection", "uncontrolled intersection", "roundabout", "four-way stop",
            "stop sign", "tamaan", "una"
        ),
        explanation = "Right-of-way rules decide who may proceed first when two or more vehicles' " +
            "paths cross — at intersections, roundabouts, or when merging. In the Philippines, " +
            "under R.A. 4136, the driver on the right has the right-of-way at uncontrolled " +
            "intersections. At a four-way stop, the first to stop goes first; if simultaneous, " +
            "the driver to the right proceeds. Having the right-of-way does not mean forcing " +
            "your way through — safety always takes precedence over legal priority.",
        explanationFil = "Ang 'right-of-way' ay ang karapatang mauna sa daan. Sa Pilipinas " +
            "ayon sa R.A. 4136, sa walang trapiko-senyal na interseksyon, ang sasakyan sa " +
            "kanan ang may karapatang mauna. Sa four-way stop, ang unang huminto ang umauna; " +
            "kung sabay-sabay, ang nasa kanan ang umauna. Ang pagkakaroon ng right-of-way " +
            "ay hindi lisensya para pumilit — ang kaligtasan ang palaging una.",
        safetyTip = "When in doubt about who has the right-of-way, slow down and yield. " +
            "Being 'technically correct' is not worth a collision.",
        safetyTipFil = "Kapag hindi sigurado, huminto muna at magbigay-daan. " +
            "Hindi sulit ang aksidente kahit tama ka."
    ),

    TRAFFIC_SIGNS(
        displayName = "Traffic Signs & Signals",
        keywords = listOf(
            "sign", "signal", "traffic light", "red light", "yellow light", "green light",
            "stop sign", "marking", "road marking", "flashing", "blinking", "pedestrian signal",
            "arrow", "warning sign", "regulatory sign", "informative sign", "senyas", "ilaw",
            "pulang ilaw", "asul", "dilaw na ilaw", "berdeng ilaw", "palatandaan"
        ),
        explanation = "Traffic signs are divided into three categories:\n" +
            "• Regulatory Signs (e.g., Stop, No Entry, Speed Limit) — you must follow these.\n" +
            "• Warning Signs (triangular with red border) — alert you to hazards ahead.\n" +
            "• Informative/Guide Signs — help with navigation and destinations.\n\n" +
            "Traffic Lights:\n" +
            "• 🔴 Red = Stop completely before the stop line.\n" +
            "• 🟡 Yellow = Prepare to stop; only proceed if you cannot stop safely.\n" +
            "• 🟢 Green = Go if the intersection is clear.\n" +
            "• Flashing Red = Treat as a Stop sign — stop, yield, then go.\n" +
            "• Flashing Yellow = Proceed with caution.",
        explanationFil = "Ang mga senyas sa daan ay may tatlong uri:\n" +
            "• Regulatory Signs (Stop, No Entry, Speed Limit) — kailangan sundin.\n" +
            "• Warning Signs (triangulo na may pulang hangganan) — babala sa panganib.\n" +
            "• Informative Signs — gabay sa destinasyon.\n\n" +
            "Mga Ilaw sa Trapiko:\n" +
            "• 🔴 Pula = Ganap na huminto bago ang stop line.\n" +
            "• 🟡 Dilaw = Maghanda na huminto; huwag bilisan para makapasa.\n" +
            "• 🟢 Berde = Sige kung ligtas ang interseksyon.\n" +
            "• Kumikislap na Pula = Ituring na Stop sign — huminto, magbigay-daan, saka lumakad.\n" +
            "• Kumikislap na Dilaw = Magpatuloy nang may pag-iingat.",
        safetyTip = "A yellow light means prepare to stop, not speed up to beat it.",
        safetyTipFil = "Ang dilaw na ilaw ay hudyat para huminto, hindi para bilisan ang takbo."
    ),

    SPEED_MANAGEMENT(
        displayName = "Speed Management",
        keywords = listOf(
            "speed", "speeding", "velocity", "km/h", "kph", "limit", "speed limit",
            "60 kph", "80 kph", "100 kph", "overspeed", "school zone", "residential",
            "expressway", "highway", "bilis", "mabilis", "bilis ng sasakyan", "limitasyon ng bilis"
        ),
        explanation = "Under R.A. 4136, general speed limits in the Philippines are:\n" +
            "• Residential and school zones: 30 km/h\n" +
            "• City and municipal roads: 50 km/h\n" +
            "• Open roads (national highways): 80 km/h\n" +
            "• Expressways: as posted (usually 80–100 km/h)\n\n" +
            "Speed limits are the maximum for ideal conditions, not a target. " +
            "Rain, poor visibility, heavy traffic, or an unfamiliar road all call for " +
            "driving below the posted limit. For specific current speed limits by road " +
            "classification, verify with the latest LTO or DPWH guidelines.",
        explanationFil = "Ayon sa R.A. 4136, ang pangkalahatang limitasyon ng bilis sa Pilipinas ay:\n" +
            "• Residential at school zones: 30 km/h\n" +
            "• Mga lungsod at bayan: 50 km/h\n" +
            "• Pangunahing kalsada: 80 km/h\n" +
            "• Expressways: ayon sa nakapost (karaniwan 80–100 km/h)\n\n" +
            "Ang limitasyon ng bilis ay para sa pinakamainam na kondisyon, " +
            "hindi target na dapat abutin. Kapag umuulan o mahirap ang daan, " +
            "bumagal nang higit pa sa limitasyon.",
        safetyTip = "Higher speed means longer stopping distance and less time to react — " +
            "adjust for conditions, not just the posted sign.",
        safetyTipFil = "Mas mataas ang bilis, mas matagal ang pagtigil — i-adjust ang bilis " +
            "ayon sa kondisyon ng daan, hindi lang sa sign."
    ),

    FOLLOWING_DISTANCE(
        displayName = "Following Distance",
        keywords = listOf(
            "following distance", "tailgat", "stopping distance", "sudden stop", "brake",
            "rear-end", "3-second", "safe gap", "buffer", "space", "distansya", "pagitan"
        ),
        explanation = "Safe following distance is your buffer if the vehicle ahead brakes " +
            "suddenly. Use the 3-second rule:\n" +
            "• Choose a fixed object (e.g., a sign post) the car ahead passes.\n" +
            "• Count: 'one thousand one, one thousand two, one thousand three.'\n" +
            "• You should not reach that marker before you finish counting.\n\n" +
            "Increase to 4–6 seconds in rain, fog, or heavy traffic. " +
            "At highway speeds (80–100 km/h), following too close eliminates any chance " +
            "to react before a collision.",
        explanationFil = "Ang ligtas na distansya ay ang iyong espasyo kapag biglang nag-brake " +
            "ang sasakyan sa harap. Gamitin ang 3-second rule:\n" +
            "• Pumili ng nakatayo na bagay na dadaanan ng sasakyan sa harap mo.\n" +
            "• Bilangin: 'isa, dalawa, tatlo' — hindi ka dapat makarating doon bago matapos bilang.\n\n" +
            "Dagdagan hanggang 4–6 segundo kapag umuulan, makulimlim, o mabigat ang trapiko.",
        safetyTip = "Use the 3-second rule in good conditions; double it in rain or poor visibility.",
        safetyTipFil = "Gamitin ang 3-second rule sa magandang panahon; doblehin kapag umuulan."
    ),

    PEDESTRIAN_SAFETY(
        displayName = "Pedestrian Safety",
        keywords = listOf(
            "pedestrian", "crosswalk", "crossing", "sidewalk", "school zone", "tawiran",
            "zebra crossing", "peatonal", "paa", "naglalakad", "babae", "bata", "estudyante",
            "pedestrian lane", "pedestrian priority"
        ),
        explanation = "Pedestrians have the right-of-way on marked crosswalks and pedestrian " +
            "lanes. Philippine law requires drivers to:\n" +
            "• Stop for pedestrians already on or about to enter a crosswalk.\n" +
            "• Slow down significantly near school zones and playgrounds.\n" +
            "• Yield to persons with disabilities and elderly pedestrians.\n\n" +
            "Pedestrians have no physical protection in a collision, making driver " +
            "anticipation critical — always slow down near crosswalks, bus stops, and " +
            "areas with limited visibility.",
        explanationFil = "Ang mga taong naglalakad ay may karapatan sa mga itinalagang tawiran. " +
            "Ang batas sa Pilipinas ay nag-aatas sa mga driver na:\n" +
            "• Huminto para sa mga tao na nasa tawiran na o papasok pa lang.\n" +
            "• Bumagal sa mga school zone at parke.\n" +
            "• Magbigay-daan sa mga may kapansanan at matatandang naglalakad.\n\n" +
            "Walang proteksyon ang mga pedestrian sa aksidente — palaging mag-ingat " +
            "sa mga tawiran at lugar na limitado ang visibility.",
        safetyTip = "Always slow down near crosswalks, even if you don't see anyone yet — " +
            "parked vehicles or blind spots can hide pedestrians.",
        safetyTipFil = "Palaging bumabagal malapit sa tawiran, kahit walang nakikita — " +
            "maaaring nagtago sa blind spot ang isang tao."
    ),

    OVERTAKING(
        displayName = "Overtaking & Lane Discipline",
        keywords = listOf(
            "overtak", "overtake", "passing", "pass another", "lane change", "lane marking",
            "solid line", "broken line", "white line", "yellow line", "no overtaking",
            "blind curve", "crest", "hill", "lampasan", "pagpapalampas", "linya"
        ),
        explanation = "Overtaking is permitted only where:\n" +
            "• The road shows broken (dashed) center lines.\n" +
            "• Visibility ahead is clear for the full maneuver.\n" +
            "• There is no oncoming traffic and enough space.\n\n" +
            "Overtaking is PROHIBITED when:\n" +
            "• There are solid white or yellow center lines.\n" +
            "• Approaching intersections, pedestrian crossings, bridges, or school zones.\n" +
            "• On blind curves or just past the crest of a hill.\n" +
            "• The vehicle ahead is at or near the speed limit.\n\n" +
            "Signal your intent, check mirrors, check blind spots, then overtake " +
            "decisively — never hesitate mid-maneuver.",
        explanationFil = "Ang pagpapalampas ay pinahihintulutan lamang kung:\n" +
            "• Ang gitnang linya ng daan ay putol-putol (broken line).\n" +
            "• Malinaw ang paningin para sa buong pagpapalampas.\n" +
            "• Walang darating na sasakyan at sapat ang espasyo.\n\n" +
            "BAWAL ang pagpapalampas kapag:\n" +
            "• May solid na puting o dilaw na gitnang linya.\n" +
            "• Papalapit sa interseksyon, tawiran, tulay, o school zone.\n" +
            "• Sa blind curve o sa ibabaw ng bundok/burol.\n\n" +
            "Mag-signal, tingnan ang salamin at blind spot, saka lamang lumampas — " +
            "huwag mag-alinlangan sa kalagitnaan ng pagpapalampas.",
        safetyTip = "If you are not 100% certain you have enough space and visibility, " +
            "do not start the overtake.",
        safetyTipFil = "Kung hindi ka 100% sigurado sa espasyo at visibility, " +
            "huwag magsimulang lumampas."
    ),

    ROAD_MARKINGS(
        displayName = "Road Markings & Pavement Signs",
        keywords = listOf(
            "road marking", "pavement", "solid line", "broken line", "double yellow", "double white",
            "stop line", "lane", "edge line", "center line", "arrow marking", "box junction",
            "markings", "guhit", "linya sa daan"
        ),
        explanation = "Road markings provide guidance and restrictions:\n" +
            "• Solid White Line (edge/lane) — do not cross; marks lane boundaries or road edge.\n" +
            "• Broken/Dashed White Line — lane-change or overtaking permitted if safe.\n" +
            "• Solid Yellow Center Line — no overtaking from either direction.\n" +
            "• Double Yellow Center Line — absolutely no crossing.\n" +
            "• Broken Yellow Line — overtaking permitted for the vehicle beside the broken side.\n" +
            "• White Stop Line — stop your front axle before this line at red lights or stop signs.\n" +
            "• Zebra Stripes — pedestrian crossing; yield to pedestrians.",
        explanationFil = "Ang mga marka sa daan ay nagbibigay ng gabay at pagbabawal:\n" +
            "• Solid White Line — huwag tumawid; hangganan ng linya o gilid ng daan.\n" +
            "• Broken White Line — maaaring lumipat ng linya o lumampas kung ligtas.\n" +
            "• Solid Yellow Center Line — walang pagpapalampas mula sa magkabilang direksiyon.\n" +
            "• Double Yellow Center Line — ganap na bawal tumawid.\n" +
            "• Broken Yellow Line — ang sasakyan sa tabi ng putol na linya ay maaaring lumampas.\n" +
            "• White Stop Line — itigil ang sasakyan sa linya bago ang pula o stop sign.\n" +
            "• Zebra Stripes — tawiran ng mga paa; bigyan ng karapatan ang mga pedestrian.",
        safetyTip = "Never treat a solid line as optional — it marks a point where crossing " +
            "creates real danger.",
        safetyTipFil = "Huwag palampasin ang solid na linya — ito ay nagpapahiwatig ng tunay na panganib."
    ),

    MOTORCYCLE_SAFETY(
        displayName = "Motorcycle & Bicycle Safety",
        keywords = listOf(
            "motorcycle", "motorbike", "rider", "helmet", "bicycle", "bike", "cyclist",
            "lane filter", "lane split", "motor", "motorsiklo", "bisikleta", "helmet law",
            "vulnerable road user"
        ),
        explanation = "Motorcycles and bicycles are smaller and harder to see than cars:\n" +
            "• Drivers must check mirrors AND do a shoulder check before changing lanes — " +
            "motorcycles often occupy a car's blind spot.\n" +
            "• Maintain at least the 3-second following distance from motorcycles, " +
            "as they can brake faster.\n" +
            "• Under Philippine law, all motorcycle riders and passengers must wear an " +
            "approved helmet at all times.\n" +
            "• At night or in rain, motorcycles are much harder to see — increase following " +
            "distance and watch for their narrower silhouette.",
        explanationFil = "Ang mga motorsiklo at bisikleta ay mas maliit at mas mahirap makita:\n" +
            "• Ang mga driver ay kailangang tingnan ang salamin AT gumawa ng shoulder check " +
            "bago lumipat ng linya — madalas na nasa blind spot ang mga motorsiklo.\n" +
            "• Panatilihing hindi bababa sa 3 segundo ang distansya mula sa motorsiklo.\n" +
            "• Ayon sa batas Pilipino, lahat ng nagmamaneho at sakay ng motorsiklo " +
            "ay kailangang magsuot ng aprubadong helmet sa lahat ng oras.\n" +
            "• Sa gabi o ulan, mas mahirap makita ang motorsiklo — magbigay ng mas malaking distansya.",
        safetyTip = "Always do a shoulder check before changing lanes — a motorcycle can be " +
            "hidden in your mirror's blind spot.",
        safetyTipFil = "Palaging tingnan ang blind spot bago lumipat ng linya — " +
            "maaaring naka-blind spot ang motorsiklo."
    ),

    IMPAIRED_OR_DISTRACTED(
        displayName = "Impaired & Distracted Driving",
        keywords = listOf(
            "drunk", "alcohol", "impair", "distract", "phone", "texting", "mobile",
            "fatigue", "tired", "sleepy", "drowsy", "drug", "dui", "dwi",
            "anti-distracted", "lasing", "pagod", "antok", "telepono", "mensahe habang nagmamaneho"
        ),
        explanation = "Impaired and distracted driving are leading causes of fatal road crashes " +
            "in the Philippines.\n\n" +
            "Under the Anti-Drunk and Drugged Driving Act (R.A. 10586), it is illegal to " +
            "drive with a Blood Alcohol Concentration (BAC) at or above the legal limit. " +
            "For specific BAC thresholds and penalties, verify with the latest LTO regulations.\n\n" +
            "Under the Anti-Distracted Driving Act (R.A. 10913), holding or using a " +
            "mobile phone or electronic device while driving is prohibited.\n\n" +
            "Fatigue impairs reaction time and judgment in ways similar to alcohol. " +
            "If you are drowsy, pull over safely before you continue.",
        explanationFil = "Ang pagmamaneho habang lasing o distracted ay isa sa pangunahing " +
            "dahilan ng mga nakamamatay na aksidente sa Pilipinas.\n\n" +
            "Sa ilalim ng Anti-Drunk and Drugged Driving Act (R.A. 10586), ilegal ang " +
            "pagmamaneho na may Blood Alcohol Concentration (BAC) na higit sa legal na limitasyon. " +
            "Para sa eksaktong BAC at parusa, suriin ang pinakabagong regulasyon ng LTO.\n\n" +
            "Sa ilalim ng Anti-Distracted Driving Act (R.A. 10913), bawal hawakan o gamitin " +
            "ang telepono habang nagmamaneho.\n\n" +
            "Ang pagod ay nakaka-apekto sa reaksyon at pagpapasya katulad ng alak. " +
            "Kung inaantok ka, huminto nang ligtas bago magpatuloy.",
        safetyTip = "If you are too tired, too distracted, or have consumed any alcohol, " +
            "do not drive — pull over safely or arrange an alternative.",
        safetyTipFil = "Kung pagod ka, distracted, o nainom ng anumang alak, " +
            "huwag magmaneho — huminto o maghanap ng ibang paraan."
    ),

    EMERGENCY_PROCEDURES(
        displayName = "Emergency Vehicle Protocols",
        keywords = listOf(
            "emergency", "ambulance", "siren", "fire truck", "police", "hazard", "breakdown",
            "emergency vehicle", "pullover", "pull over", "give way", "emergency light",
            "ambulansya", "bombero", "pulis", "emergency", "siren"
        ),
        explanation = "When an emergency vehicle (ambulance, fire truck, police) with active " +
            "sirens and lights approaches:\n" +
            "1. Check your mirrors and identify where the vehicle is coming from.\n" +
            "2. Safely pull to the right and stop — do not block the emergency lane.\n" +
            "3. Do not follow emergency vehicles through intersections.\n" +
            "4. Remain stopped until the emergency vehicle has safely passed.\n\n" +
            "In case of vehicle breakdown on a highway:\n" +
            "• Move as far off the road as possible.\n" +
            "• Activate hazard lights immediately.\n" +
            "• Place early warning devices (triangles) 50–100 m behind your vehicle.",
        explanationFil = "Kapag may dumarating na sasakyang pangkuryente (ambulansya, bumbero, " +
            "pulis) na may aktibong siren at ilaw:\n" +
            "1. Tingnan ang iyong salamin at alamin kung saan nanggagaling.\n" +
            "2. Ligtas na lumiko sa kanan at huminto — huwag harangan ang emergency lane.\n" +
            "3. Huwag sumunod sa emergency vehicles sa interseksyon.\n" +
            "4. Manatiling nakatigil hanggang makalampas ang sasakyan.\n\n" +
            "Kapag nasira ang sasakyan sa highway:\n" +
            "• Galaw palayo sa daan hangga't maaari.\n" +
            "• Agad i-on ang hazard lights.\n" +
            "• Maglagay ng early warning device 50–100 m sa likod ng sasakyan.",
        safetyTip = "When you hear a siren, check mirrors, signal, and pull smoothly to the " +
            "right — never brake hard in the middle of the lane.",
        safetyTipFil = "Kapag narinig ang siren, tingnan ang salamin, mag-signal, at lumiko " +
            "nang maayos sa kanan — huwag biglang i-brake sa gitna ng daan."
    ),

    DEFENSIVE_DRIVING(
        displayName = "Defensive Driving",
        keywords = listOf(
            "defensive", "hazard", "anticipate", "awareness", "observation", "scan", "mirror",
            "blind spot", "risk", "safe driving", "buffer", "predictive", "mahusay na pagmamaneho",
            "ligtas na pagmamaneho", "hazard anticipation"
        ),
        explanation = "Defensive driving means expecting the unexpected and reducing risk:\n" +
            "• Scan the road at least 12–15 seconds ahead — look for hazards early.\n" +
            "• Check mirrors every 5–8 seconds to stay aware of vehicles around you.\n" +
            "• Always maintain an escape route — never box yourself between other vehicles.\n" +
            "• Anticipate what other drivers might do — do not assume they will follow the rules.\n" +
            "• Adjust speed and position proactively, not reactively.\n" +
            "• Stay calm — aggressive driving elevates risk for everyone on the road.",
        explanationFil = "Ang defensive driving ay ang pagiging handa sa hindi inaasahan:\n" +
            "• Tingnan ang daan 12–15 segundo pasulong — alamin ang mga panganib nang maaga.\n" +
            "• Suriin ang mga salamin bawat 5–8 segundo para malaman ang mga sasakyan sa paligid.\n" +
            "• Palaging magpanatili ng 'escape route' — huwag hayaang masalupaan ng ibang sasakyan.\n" +
            "• Asahan ang maaaring gawin ng ibang driver — huwag ipagpalagay na susundin nila ang batas.\n" +
            "• Mag-adjust ng bilis at posisyon nang maaga, hindi kapag huli na.\n" +
            "• Manatiling kalmado — ang marahas na pagmamaneho ay nagpapataas ng panganib.",
        safetyTip = "You can only control your own driving — defensive driving means planning " +
            "for other drivers' mistakes, not just avoiding your own.",
        safetyTipFil = "Kontrolado mo lang ang sariling pagmamaneho — ang defensive driving " +
            "ay paghahanda sa mga pagkakamali ng iba, hindi lang sa sariling pagkakamali."
    ),

    WEATHER_AND_ROAD_CONDITIONS(
        displayName = "Weather & Road Conditions",
        keywords = listOf(
            "rain", "wet", "slippery", "flood", "fog", "night", "dark", "glare",
            "hydroplane", "aquaplane", "mud", "construction", "rough road", "pothole",
            "ulan", "basa", "madulas", "baha", "ulap", "gabi", "maliwanag"
        ),
        explanation = "Road conditions dramatically affect safe driving:\n\n" +
            "In rain:\n" +
            "• Reduce speed (30–40% slower than normal).\n" +
            "• Increase following distance to 5–6 seconds.\n" +
            "• If hydroplaning (tires losing grip on water), ease off the accelerator gently — " +
            "do not brake sharply or steer aggressively.\n" +
            "• Do not drive through flooded roads if water depth is unknown.\n\n" +
            "At night:\n" +
            "• Use headlights from dusk to dawn and in poor visibility.\n" +
            "• Dim your high beams when within 150 m of an oncoming vehicle.\n" +
            "• Reduce speed — your stopping distance should not exceed your headlight range.",
        explanationFil = "Ang kondisyon ng daan ay malaki ang epekto sa ligtas na pagmamaneho:\n\n" +
            "Sa ulan:\n" +
            "• Bawasan ang bilis ng 30–40% kaysa karaniwan.\n" +
            "• Dagdagan ang distansya hanggang 5–6 segundo.\n" +
            "• Kapag nag-hydroplane (nawalan ng grip ang gulong), dahan-dahang bitawan ang accelerator " +
            "— huwag biglang i-brake o ibalin ang manibela.\n" +
            "• Huwag tawirin ang binabaha kung hindi alam ang lalim ng tubig.\n\n" +
            "Sa gabi:\n" +
            "• Gamitin ang headlights mula takipsilim hanggang madaling araw.\n" +
            "• I-dim ang high beams kapag 150 m na lamang ang layo sa darating na sasakyan.\n" +
            "• Bawasan ang bilis — ang pagtigil ay hindi dapat higit sa hanay ng iyong headlights.",
        safetyTip = "In rain, if you can't see clearly, pull over safely and wait for conditions " +
            "to improve — no trip is worth a life.",
        safetyTipFil = "Sa ulan, kung hindi malinaw ang paningin, huminto nang ligtas at " +
            "hintayin na gumaling ang kondisyon — walang biyahe na katumbas ng buhay."
    ),

    ROAD_COURTESY(
        displayName = "Road Courtesy & Driver Responsibility",
        keywords = listOf(
            "courtesy", "responsib", "aggressive", "horn", "obstacle", "attitude",
            "road rage", "responsibility", "consideration", "driver behavior", "etiketa",
            "magalang", "responsibilidad", "ugali ng driver"
        ),
        explanation = "Beyond specific rules, safe driving is a shared responsibility:\n" +
            "• Use your horn sparingly and only as a warning, not out of frustration.\n" +
            "• Avoid aggressive driving — tailgating, cutting off, and flashing headlights escalate risk.\n" +
            "• Allow merging vehicles adequate space.\n" +
            "• Respond to road rage by staying calm — never engage with aggressive drivers.\n" +
            "• Keep your vehicle roadworthy — faulty brakes, tires, or lights endanger everyone.\n" +
            "• Always wear your seatbelt and ensure all passengers do the same.",
        explanationFil = "Higit sa mga partikular na panuntunan, ang ligtas na pagmamaneho " +
            "ay responsibilidad ng lahat:\n" +
            "• Gamitin ang horn nang matipid at bilang babala lamang, hindi sa frustrasyon.\n" +
            "• Iwasan ang marahas na pagmamaneho — nagpapataas ito ng panganib para sa lahat.\n" +
            "• Bigyan ng sapat na espasyo ang mga sasakyang sumasali sa iyong linya.\n" +
            "• Sa road rage, manatiling kalmado — huwag makipag-away sa agresibong driver.\n" +
            "• Panatilihing maayos ang sasakyan — sira na preno, gulong, o ilaw ay mapanganib.\n" +
            "• Palaging magsuot ng seatbelt at tiyaking ganoon din ang lahat ng sakay.",
        safetyTip = "You can't control other drivers' behavior, only your own — defensive " +
            "driving means planning for their mistakes, not just avoiding your own.",
        safetyTipFil = "Hindi mo kontrolado ang ugali ng iba, sarili mo lang — " +
            "ang defensive driving ay paghahanda sa pagkakamali ng iba."
    );

    companion object {
        /**
         * Keyword-match a question's text to a topic. Falls back to ROAD_COURTESY.
         * Checks both the question text and any provided option strings.
         */
        fun classify(questionText: String, options: List<String> = emptyList()): RoadSafetyTopic {
            val haystack = (listOf(questionText) + options).joinToString(" ").lowercase()
            return entries.firstOrNull { topic ->
                topic.keywords.any { haystack.contains(it) }
            } ?: ROAD_COURTESY
        }

        /**
         * Detect whether a query is in Filipino/Tagalog by checking for common Filipino keywords.
         */
        fun isFilipino(text: String): Boolean {
            val lower = text.lowercase()
            val filipinoIndicators = listOf(
                "ano", "paano", "bakit", "kailan", "saan", "sino", "kung", "dapat", "magmaneho",
                "sasakyan", "daan", "ilaw", "tawiran", "trapiko", "kalsada", "lisensya",
                "batas", "bilis", "ligtas", "bawal", "pinahintulutan", "ng", "sa", "at", "ang",
                "na", "mga", "ay", "ko", "mo", "namin", "natin", "nila", "po", "ba", "din",
                "rin", "pa", "lang", "lamang", "kaya", "kapag", "pag", "pagmamaneho"
            )
            val count = filipinoIndicators.count { lower.contains(it) }
            return count >= 2
        }

        /**
         * Returns whether a user query is related to road safety / the project's scope.
         * Used by AiTutorEngine to enforce off-topic protection.
         */
        fun isOnTopic(text: String): Boolean {
            val lower = text.lowercase()
            val onTopicKeywords = listOf(
                "drive", "driving", "road", "traffic", "vehicle", "car", "motor", "speed",
                "sign", "signal", "lane", "highway", "intersection", "pedestrian", "helmet",
                "seatbelt", "seat belt", "accident", "collision", "safety", "license", "lto",
                "quiz", "module", "question", "scenario", "decision", "xp", "progress",
                "level", "badge", "overtake", "yield", "brake", "horn", "headlight",
                // Filipino keywords
                "daan", "sasakyan", "trapiko", "ilaw", "kalsada", "bilis", "ligtas",
                "magmaneho", "pagmamaneho", "tawiran", "interseksyon", "lisensya",
                "motorsiklo", "bisikleta", "helmet", "seatbelt", "senyal", "batas",
                "bawal", "paano", "ano ang tamang", "tamang gawin", "module", "aralin"
            )
            return onTopicKeywords.any { lower.contains(it) }
        }
    }
}
