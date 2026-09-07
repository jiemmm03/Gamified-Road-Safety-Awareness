package com.example.gamifiedroadsafetyawareness.model

object QuizData {

    // ═══════════════════════════════════════════════════════════════════════════
    // 🟢  EASY — 20 Questions
    // ═══════════════════════════════════════════════════════════════════════════
    val quiz_easy = Quiz(
        id = "quiz_easy",
        title = "Easy Quiz – Road Safety Basics",
        moduleType = ModuleType.EASY,
        questions = listOf(
            QuizQuestion(
                id = 1,
                question = "What should you do when the traffic light turns red?",
                options = listOf("Stop", "Speed up", "Overtake", "Turn immediately"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat mong gawin kapag pumula ang traffic light?",
                optionsFil = listOf("Tumigil", "Bilisan", "Mag-overtake", "Agad na lumiko")
            ),
            QuizQuestion(
                id = 2,
                question = "What does a green traffic light generally mean?",
                options = listOf("Stop", "Proceed when safe", "Reverse", "Park"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang karaniwang ibig sabihin ng berdeng ilaw trapiko?",
                optionsFil = listOf("Tumigil", "Magpatuloy kung ligtas na", "Umatras", "Pumarada")
            ),
            QuizQuestion(
                id = 3,
                question = "What does a yellow traffic light warn drivers about?",
                options = listOf("Prepare to stop", "Speed up", "Park", "Overtake"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang binababalaan ng dilaw na ilaw trapiko sa mga driver?",
                optionsFil = listOf("Maghanda tumigil", "Bilisan", "Pumarada", "Mag-overtake")
            ),
            QuizQuestion(
                id = 4,
                question = "What should a driver do before starting a trip?",
                options = listOf("Check the vehicle", "Increase the radio volume", "Drive immediately", "Ignore the vehicle condition"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago magsimula ng biyahe?",
                optionsFil = listOf("Suriin ang sasakyan", "Lakasan ang radyo", "Agad na magmaneho", "Huwag pansinin ang kalagayan ng sasakyan")
            ),
            QuizQuestion(
                id = 5,
                question = "What is the purpose of a seat belt?",
                options = listOf("Improve fuel economy", "Protect occupants during a crash", "Increase vehicle speed", "Improve engine power"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang layunin ng seatbelt?",
                optionsFil = listOf("Makatipid sa gasolina", "Protektahan ang mga sakay kapag may aksidente", "Pabilisin ang sasakyan", "Palakasin ang makina")
            ),
            QuizQuestion(
                id = 6,
                question = "What should you do when approaching a pedestrian crossing?",
                options = listOf("Speed up", "Slow down and be prepared to stop", "Honk continuously", "Overtake other vehicles"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin kapag papalapit sa tawiran ng mga pedestrian?",
                optionsFil = listOf("Bilisan", "Magbagal at maghanda tumigil", "Patuloy na bumusina", "Mag-overtake sa ibang sasakyan")
            ),
            QuizQuestion(
                id = 7,
                question = "What does a STOP sign require a driver to do?",
                options = listOf("Slow down only", "Come to a complete stop", "Speed up", "Turn around"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang kinakailangang gawin ng driver sa isang STOP sign?",
                optionsFil = listOf("Magbagal lamang", "Tumigil nang husto", "Bilisan", "Bumalik")
            ),
            QuizQuestion(
                id = 8,
                question = "Why are turn signals used?",
                options = listOf("To communicate intended movement", "To increase speed", "To warn about engine problems", "To save fuel"),
                correctAnswerIndex = 0,
                questionFil = "Bakit ginagamit ang signal light?",
                optionsFil = listOf("Para ipaalam ang balak gawin", "Para pabilisin", "Para balaan sa problema ng makina", "Para makatipid sa gasolina")
            ),
            QuizQuestion(
                id = 9,
                question = "What should you do when driving behind another vehicle?",
                options = listOf("Follow extremely closely", "Maintain a safe following distance", "Drive beside it constantly", "Flash headlights continuously"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin kapag sumusunod sa likod ng ibang sasakyan?",
                optionsFil = listOf("Sumunod nang sobrang lapit", "Panatilihin ang ligtas na agwat", "Palaging sumabay sa tabi nito", "Patuloy na kumurap ng headlights")
            ),
            QuizQuestion(
                id = 10,
                question = "What is the main purpose of traffic signs?",
                options = listOf("Decoration", "Provide information and regulate traffic", "Increase vehicle speed", "Advertise vehicles"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang pangunahing layunin ng mga road sign?",
                optionsFil = listOf("Pampaganda lamang", "Magbigay impormasyon at magregula ng trapiko", "Pabilisin ang sasakyan", "Mag-advertise ng sasakyan")
            ),
            QuizQuestion(
                id = 11,
                question = "Who should wear a seat belt in a vehicle equipped with seat belts?",
                options = listOf("Only the driver", "Only passengers", "Driver and passengers as required", "Nobody"),
                correctAnswerIndex = 2,
                questionFil = "Sino ang dapat magsuot ng seatbelt sa sasakyang may seatbelt?",
                optionsFil = listOf("Ang driver lamang", "Ang mga pasahero lamang", "Driver at mga pasahero, ayon sa kinakailangan", "Walang dapat magsuot")
            ),
            QuizQuestion(
                id = 12,
                question = "What should you do if you feel very tired while driving?",
                options = listOf("Continue driving faster", "Stop at a safe place and rest", "Open the windows and continue indefinitely", "Ignore the tiredness"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin kapag sobrang pagod ka habang nagmamaneho?",
                optionsFil = listOf("Ituloy magmaneho nang mas mabilis", "Tumigil sa ligtas na lugar at magpahinga", "Buksan ang bintana at ituloy nang walang tigil", "Huwag pansinin ang pagod")
            ),
            QuizQuestion(
                id = 13,
                question = "What does a pedestrian crossing primarily provide?",
                options = listOf("A place for pedestrians to cross the road", "A parking area", "A loading zone", "An overtaking lane"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing layunin ng tawiran (pedestrian crossing)?",
                optionsFil = listOf("Lugar para tumawid ang mga pedestrian", "Lugar na paradahan", "Loading zone", "Lane para sa overtaking")
            ),
            QuizQuestion(
                id = 14,
                question = "What should you do before changing lanes?",
                options = listOf("Check surrounding traffic", "Close your eyes", "Accelerate without checking", "Immediately move over"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin bago lumipat ng lane?",
                optionsFil = listOf("Tingnan ang paligid na trapiko", "Ipikit ang mata", "Bilisan nang hindi tumitingin", "Agad na lumipat")
            ),
            QuizQuestion(
                id = 15,
                question = "What should a responsible driver obey?",
                options = listOf("Traffic laws and regulations", "Only other drivers", "Only passengers", "No rules"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat sundin ng responsableng driver?",
                optionsFil = listOf("Mga batas at regulasyon sa trapiko", "Ibang driver lamang", "Mga pasahero lamang", "Walang dapat sundin")
            ),
            QuizQuestion(
                id = 16,
                question = "What is the safest approach to driving?",
                options = listOf("Aggressive driving", "Defensive driving", "Racing", "Constant overtaking"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang pinakaligtas na paraan ng pagmamaneho?",
                optionsFil = listOf("Agresibong pagmamaneho", "Defensive driving (maingat na pagmamaneho)", "Karera", "Palaging pag-overtake")
            ),
            QuizQuestion(
                id = 17,
                question = "What should you do when you see a warning sign?",
                options = listOf("Ignore it", "Be alert and adjust driving as necessary", "Speed up", "Stop anywhere"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin kapag may nakitang warning sign?",
                optionsFil = listOf("Huwag pansinin", "Maging alerto at iangkop ang pagmamaneho kung kinakailangan", "Bilisan", "Tumigil kahit saan")
            ),
            QuizQuestion(
                id = 18,
                question = "What is the purpose of a vehicle's brakes?",
                options = listOf("Increase speed", "Slow down or stop the vehicle", "Improve the radio", "Increase fuel consumption"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang layunin ng preno ng sasakyan?",
                optionsFil = listOf("Pabilisin", "Bagalan o patigilin ang sasakyan", "Pahusayin ang radyo", "Dagdagan ang gastos sa gasolina")
            ),
            QuizQuestion(
                id = 19,
                question = "What should you do when approaching an intersection?",
                options = listOf("Observe traffic and signs", "Close your eyes", "Always accelerate", "Ignore other vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag papalapit sa interseksyon?",
                optionsFil = listOf("Obserbahan ang trapiko at mga sign", "Ipikit ang mata", "Laging bilisan", "Huwag pansinin ang ibang sasakyan")
            ),
            QuizQuestion(
                id = 20,
                question = "Why should drivers follow speed limits?",
                options = listOf("To promote road safety", "To use more fuel", "To make the trip longer", "To prevent all traffic"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat sundin ng mga driver ang speed limit?",
                optionsFil = listOf("Para itaguyod ang kaligtasan sa daan", "Para gumamit ng mas maraming gasolina", "Para tumagal ang biyahe", "Para pigilan ang lahat ng trapiko")
            )
        )
    )

    // ═══════════════════════════════════════════════════════════════════════════
    // 🟡  MEDIUM — 20 Questions
    // ═══════════════════════════════════════════════════════════════════════════
    val quiz_medium = Quiz(
        id = "quiz_medium",
        title = "Medium Quiz – Applied Traffic Knowledge",
        moduleType = ModuleType.MEDIUM,
        questions = listOf(
            QuizQuestion(
                id = 1,
                question = "You are approaching an intersection and the traffic signal changes from green to yellow. What should you do?",
                options = listOf("Always accelerate", "Prepare to stop when safe", "Reverse immediately", "Overtake"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa interseksyon at nagbago ang ilaw mula berde patungong dilaw. Ano ang dapat mong gawin?",
                optionsFil = listOf("Laging bilisan", "Maghanda tumigil kung ligtas", "Agad na umatras", "Mag-overtake")
            ),
            QuizQuestion(
                id = 2,
                question = "You want to change lanes on a busy road. What should you do first?",
                options = listOf("Move immediately", "Check mirrors and surrounding traffic", "Honk and turn suddenly", "Accelerate without checking"),
                correctAnswerIndex = 1,
                questionFil = "Gusto mong lumipat ng lane sa abalang daan. Ano ang unang dapat gawin?",
                optionsFil = listOf("Agad lumipat", "Tingnan ang salamin at paligid na trapiko", "Bumusina at bigla lumiko", "Bilisan nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 3,
                question = "A vehicle in front of you suddenly slows down. What is the safest response?",
                options = listOf("Follow closely", "Maintain control and increase stopping space", "Overtake immediately", "Use the shoulder"),
                correctAnswerIndex = 1,
                questionFil = "Biglang bumagal ang sasakyan sa unahan mo. Ano ang pinakaligtas na gagawin?",
                optionsFil = listOf("Sumunod nang malapit", "Panatilihin ang kontrol at dagdagan ang agwat sa pagtigil", "Agad mag-overtake", "Gamitin ang shoulder (gilid ng daan)")
            ),
            QuizQuestion(
                id = 4,
                question = "You approach a pedestrian who is preparing to cross at a designated crossing. What should you do?",
                options = listOf("Speed up", "Slow down and yield as required", "Ignore the pedestrian", "Overtake another vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa isang pedestrian na naghahanda tumawid sa itinalagang tawiran. Ano ang dapat mong gawin?",
                optionsFil = listOf("Bilisan", "Magbagal at magbigay-daan ayon sa kinakailangan", "Huwag pansinin ang pedestrian", "Mag-overtake sa ibang sasakyan")
            ),
            QuizQuestion(
                id = 5,
                question = "When is overtaking safest?",
                options = listOf("When visibility is adequate and it is legally permitted", "At every intersection", "On a blind curve", "When approaching a pedestrian crossing"),
                correctAnswerIndex = 0,
                questionFil = "Kailan pinakaligtas ang mag-overtake?",
                optionsFil = listOf("Kapag malinaw ang paningin at legal na gawin ito", "Sa bawat interseksyon", "Sa likong walang malinaw na paningin", "Kapag papalapit sa tawiran")
            ),
            QuizQuestion(
                id = 6,
                question = "Why is following another vehicle too closely dangerous?",
                options = listOf("It reduces reaction and stopping time", "It improves visibility", "It saves fuel", "It makes traffic move faster"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang sumunod nang sobrang lapit sa ibang sasakyan?",
                optionsFil = listOf("Nababawasan ang oras para umreact at tumigil", "Napapabuti ang paningin", "Nakakatipid ng gasolina", "Pinapabilis ang trapiko")
            ),
            QuizQuestion(
                id = 7,
                question = "A driver behind you is attempting to overtake. What should you generally do?",
                options = listOf("Block the vehicle", "Maintain appropriate speed and allow safe passing when permitted", "Accelerate aggressively", "Move unpredictably"),
                correctAnswerIndex = 1,
                questionFil = "May driver sa likod mo na sumusubok mag-overtake. Ano sa pangkalahatan ang dapat mong gawin?",
                optionsFil = listOf("Harangin ang sasakyan", "Panatilihin ang tamang bilis at payagan ang ligtas na pagdaan kung pinapayagan", "Bilisan nang agresibo", "Gumalaw nang di-inaasahan")
            ),
            QuizQuestion(
                id = 8,
                question = "You are driving in heavy rain. What adjustment should you make?",
                options = listOf("Increase speed", "Reduce speed and increase following distance", "Drive closer to other vehicles", "Turn off all lights"),
                correctAnswerIndex = 1,
                questionFil = "Nagmamaneho ka sa malakas na ulan. Anong ayos ang dapat mong gawin?",
                optionsFil = listOf("Pabilisin", "Bagalan at dagdagan ang agwat sa pagsunod", "Lumapit sa ibang sasakyan", "Patayin ang lahat ng ilaw")
            ),
            QuizQuestion(
                id = 9,
                question = "What should you do if your view of the road is temporarily blocked?",
                options = listOf("Continue at the same speed", "Slow down and proceed only when visibility is adequate", "Accelerate", "Overtake immediately"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin kung pansamantalang naharangan ang iyong tanaw sa daan?",
                optionsFil = listOf("Ituloy sa parehong bilis", "Magbagal at magpatuloy lamang kapag malinaw na ang paningin", "Bilisan", "Agad mag-overtake")
            ),
            QuizQuestion(
                id = 10,
                question = "You see a vehicle stopped near a pedestrian crossing. What should you consider?",
                options = listOf("A pedestrian may be crossing or preparing to cross", "The vehicle is always parked illegally", "You should immediately overtake", "The road is automatically clear"),
                correctAnswerIndex = 0,
                questionFil = "May nakita kang sasakyan na nakatigil malapit sa tawiran. Ano ang dapat mong isaalang-alang?",
                optionsFil = listOf("Maaaring may tumatawid o naghahandang tumawid na pedestrian", "Palaging iligal na nakaparada ang sasakyan", "Dapat kang agad mag-overtake", "Awtomatikong malinaw ang daan")
            ),
            QuizQuestion(
                id = 11,
                question = "When driving downhill, why should you maintain proper control of your vehicle?",
                options = listOf("The vehicle may gain speed", "The engine automatically stops", "Brakes become unnecessary", "Traffic signs disappear"),
                correctAnswerIndex = 0,
                questionFil = "Kapag pababa sa isang burol, bakit dapat mong panatilihin ang tamang kontrol sa sasakyan?",
                optionsFil = listOf("Maaaring bumilis ang sasakyan", "Awtomatikong huminto ang makina", "Hindi na kailangan ang preno", "Nawawala ang mga road sign")
            ),
            QuizQuestion(
                id = 12,
                question = "What is a good practice when approaching a sharp curve?",
                options = listOf("Reduce speed before entering the curve", "Accelerate heavily", "Overtake immediately", "Drive on the opposite lane"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang mabuting gawin kapag papalapit sa matalim na liko?",
                optionsFil = listOf("Bagalan bago pumasok sa liko", "Bilisan nang husto", "Agad mag-overtake", "Magmaneho sa kabilang lane")
            ),
            QuizQuestion(
                id = 13,
                question = "A traffic officer is directing traffic while the traffic signal shows a different indication. What should you follow?",
                options = listOf("The traffic officer's lawful direction", "The radio", "Another driver's action", "The vehicle behind you"),
                correctAnswerIndex = 0,
                questionFil = "May traffic enforcer na nagdirekta ng trapiko habang iba ang ipinapakita ng traffic light. Ano ang dapat mong sundin?",
                optionsFil = listOf("Ang legal na utos ng traffic enforcer", "Ang radyo", "Ang ginagawa ng ibang driver", "Ang sasakyan sa likod mo")
            ),
            QuizQuestion(
                id = 14,
                question = "What should you do before turning?",
                options = listOf("Signal and check for other road users", "Turn suddenly", "Ignore pedestrians", "Accelerate without checking"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin bago lumiko?",
                optionsFil = listOf("Mag-signal at tingnan ang ibang gumagamit ng daan", "Biglang lumiko", "Huwag pansinin ang mga pedestrian", "Bilisan nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 15,
                question = "Why should you avoid unnecessary distractions while driving?",
                options = listOf("They can reduce attention and reaction ability", "They increase concentration", "They improve road visibility", "They make traffic signs clearer"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat mong iwasan ang hindi kailangang distraction habang nagmamaneho?",
                optionsFil = listOf("Nababawasan ang pansin at kakayahang umreact", "Pinapataas ang konsentrasyon", "Pinapabuti ang paningin sa daan", "Pinapalinaw ang mga road sign")
            ),
            QuizQuestion(
                id = 16,
                question = "If another driver behaves aggressively toward you, what is the safest response?",
                options = listOf("Challenge the driver", "Remain calm and avoid escalating the situation", "Race the driver", "Follow the driver closely"),
                correctAnswerIndex = 1,
                questionFil = "Kung agresibo ang kilos ng ibang driver sa iyo, ano ang pinakaligtas na gagawin?",
                optionsFil = listOf("Hamunin ang driver", "Manatiling kalmado at iwasang palalain ang sitwasyon", "Karerahin ang driver", "Sumunod nang malapit sa driver")
            ),
            QuizQuestion(
                id = 17,
                question = "When approaching a roadwork area, you should:",
                options = listOf("Follow temporary signs and adjust speed", "Ignore signs", "Drive through barriers", "Overtake workers"),
                correctAnswerIndex = 0,
                questionFil = "Kapag papalapit sa lugar na may roadwork, dapat:",
                optionsFil = listOf("Sundin ang pansamantalang sign at iangkop ang bilis", "Huwag pansinin ang mga sign", "Dumaan sa mga harang", "Mag-overtake sa mga manggagawa")
            ),
            QuizQuestion(
                id = 18,
                question = "Why should you check your mirrors regularly?",
                options = listOf("To monitor surrounding traffic", "To increase engine power", "To reduce tire wear", "To change the traffic light"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat mong regular na tingnan ang mga salamin (mirrors)?",
                optionsFil = listOf("Para bantayan ang paligid na trapiko", "Para palakasin ang makina", "Para bawasan ang pagkasira ng gulong", "Para baguhin ang traffic light")
            ),
            QuizQuestion(
                id = 19,
                question = "What is defensive driving primarily intended to do?",
                options = listOf("Anticipate hazards and reduce collision risks", "Make driving more aggressive", "Encourage speeding", "Eliminate traffic rules"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing layunin ng defensive driving?",
                optionsFil = listOf("Aasahan ang panganib at bawasan ang posibilidad ng banggaan", "Gawing mas agresibo ang pagmamaneho", "Hikayatin ang overspeeding", "Alisin ang mga batas trapiko")
            ),
            QuizQuestion(
                id = 20,
                question = "When parking, what should you consider first?",
                options = listOf("Whether parking is permitted and safe at the location", "How quickly you can leave", "Whether another car is nearby only", "Whether you can block part of the road"),
                correctAnswerIndex = 0,
                questionFil = "Kapag pumaparada, ano ang unang dapat isaalang-alang?",
                optionsFil = listOf("Kung pinapayagan at ligtas magparada sa lugar", "Kung gaano kabilis makakaalis", "Kung may kalapit na sasakyan lamang", "Kung maaari kang humarang sa bahagi ng daan")
            )
        )
    )

    // ═══════════════════════════════════════════════════════════════════════════
    // 🔴  HARD — 20 Questions
    // ═══════════════════════════════════════════════════════════════════════════
    val quiz_hard = Quiz(
        id = "quiz_hard",
        title = "Hard Quiz – Advanced Situational Driving",
        moduleType = ModuleType.HARD,
        questions = listOf(
            QuizQuestion(
                id = 1,
                question = "You approach an intersection with no traffic signal. Another vehicle is already approaching from a direction that has the applicable right-of-way. What should you do?",
                options = listOf("Accelerate to reach the intersection first", "Yield according to the applicable right-of-way rule", "Ignore the vehicle", "Use the shoulder"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa isang interseksyon na walang traffic light. May sasakyan nang papalapit mula sa direksyon na may karapatang mauna (right-of-way). Ano ang dapat mong gawin?",
                optionsFil = listOf("Bilisan para makauna sa interseksyon", "Magbigay-daan ayon sa naaangkop na right-of-way rule", "Huwag pansinin ang sasakyan", "Gamitin ang shoulder")
            ),
            QuizQuestion(
                id = 2,
                question = "You are driving at night and an oncoming vehicle has bright headlights. What is the safest response?",
                options = listOf("Look directly into the headlights", "Reduce speed as necessary and avoid being blinded by staring at the lights", "Turn your headlights off", "Accelerate toward the vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Nagmamaneho ka sa gabi at may paparating na sasakyan na may maliwanag na headlights. Ano ang pinakaligtas na gagawin?",
                optionsFil = listOf("Tumingin diretso sa headlights", "Bagalan kung kinakailangan at iwasang mabulag sa pagtitig sa ilaw", "Patayin ang sariling headlights", "Bilisan patungo sa sasakyan")
            ),
            QuizQuestion(
                id = 3,
                question = "You are approaching a curve where you cannot see vehicles coming from the opposite direction. Should you overtake?",
                options = listOf("Yes, if you honk", "No, because visibility is insufficient", "Yes, if you accelerate", "Yes, if the vehicle ahead is slow"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa isang liko kung saan hindi mo makita ang mga sasakyang paparating mula sa kabilang direksyon. Dapat ka bang mag-overtake?",
                optionsFil = listOf("Oo, kung bubusina ka", "Hindi, dahil hindi sapat ang paningin", "Oo, kung bibilisan mo", "Oo, kung mabagal ang sasakyan sa unahan")
            ),
            QuizQuestion(
                id = 4,
                question = "You are driving in heavy rain and notice water accumulating on the road. What is the safest approach?",
                options = listOf("Increase speed to cross quickly", "Reduce speed and maintain control", "Follow the vehicle ahead closely", "Make sudden steering movements"),
                correctAnswerIndex = 1,
                questionFil = "Nagmamaneho ka sa malakas na ulan at napansin mong may naiipong tubig sa daan. Ano ang pinakaligtas na paraan?",
                optionsFil = listOf("Bilisan para makatawid agad", "Bagalan at panatilihin ang kontrol", "Sumunod nang malapit sa unahang sasakyan", "Gumawa ng biglaang galaw sa manibela")
            ),
            QuizQuestion(
                id = 5,
                question = "You are approaching an intersection while an emergency vehicle is approaching with its warning devices activated. What should you do?",
                options = listOf("Compete for the intersection", "Give way as required and avoid obstructing it", "Follow closely behind it", "Overtake it"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa interseksyon habang may paparating na emergency vehicle na naka-siren/ilaw. Ano ang dapat mong gawin?",
                optionsFil = listOf("Makipagpaligsahan sa interseksyon", "Magbigay-daan ayon sa kinakailangan at iwasang harangin ito", "Sumunod nang malapit sa likod nito", "I-overtake ito")
            ),
            QuizQuestion(
                id = 6,
                question = "You are preparing to overtake, but the road markings and traffic conditions do not permit a safe maneuver. What should you do?",
                options = listOf("Overtake anyway", "Wait until overtaking is legal and safe", "Drive on the sidewalk", "Use the opposite lane regardless of conditions"),
                correctAnswerIndex = 1,
                questionFil = "Naghahanda kang mag-overtake, pero hindi pinapayagan ng road markings at kalagayan ng trapiko ang ligtas na paggawa nito. Ano ang dapat mong gawin?",
                optionsFil = listOf("Mag-overtake pa rin", "Maghintay hanggang legal at ligtas na mag-overtake", "Magmaneho sa bangketa", "Gamitin ang kabilang lane kahit ano pa ang kalagayan")
            ),
            QuizQuestion(
                id = 7,
                question = "A vehicle suddenly enters your lane from a side road. What should be your first priority?",
                options = listOf("Maintain safety and avoid collision", "Sound the horn continuously", "Accelerate toward the vehicle", "Chase the vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Biglang pumasok sa lane mo ang sasakyan mula sa isang side road. Ano ang dapat mong unang unahin?",
                optionsFil = listOf("Panatilihin ang kaligtasan at iwasan ang banggaan", "Patuloy na bumusina", "Bilisan patungo sa sasakyan", "Habulin ang sasakyan")
            ),
            QuizQuestion(
                id = 8,
                question = "You are driving behind a large truck that blocks your view of the road ahead. What should you do?",
                options = listOf("Follow extremely closely", "Increase following distance to improve visibility and reaction time", "Overtake immediately without checking", "Drive beside the truck indefinitely"),
                correctAnswerIndex = 1,
                questionFil = "Nagmamaneho ka sa likod ng malaking trak na humaharang sa iyong tanaw sa daan sa unahan. Ano ang dapat mong gawin?",
                optionsFil = listOf("Sumunod nang sobrang lapit", "Dagdagan ang agwat sa pagsunod para mapabuti ang paningin at oras ng pag-react", "Agad mag-overtake nang hindi tumitingin", "Magmaneho sa tabi ng trak nang walang tigil")
            ),
            QuizQuestion(
                id = 9,
                question = "You miss your intended turn on a busy road. What is the safest choice?",
                options = listOf("Stop and reverse immediately", "Continue safely and find a legal place to turn or reroute", "Make a sudden U-turn", "Drive against traffic"),
                correctAnswerIndex = 1,
                questionFil = "Nalampasan mo ang balak mong likuan sa isang abalang daan. Ano ang pinakaligtas na pagpipilian?",
                optionsFil = listOf("Tumigil at agad umatras", "Magpatuloy nang ligtas at humanap ng legal na lugar para lumiko o mag-iba ng ruta", "Gumawa ng biglaang U-turn", "Magmaneho laban sa daloy ng trapiko")
            ),
            QuizQuestion(
                id = 10,
                question = "You are approaching a pedestrian crossing while another vehicle in the adjacent lane has stopped. Why should you be cautious?",
                options = listOf("A pedestrian may be hidden from your view", "The road is always empty", "You should overtake the stopped vehicle immediately", "The stopped vehicle automatically gives you priority"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ka sa tawiran habang nakatigil ang ibang sasakyan sa katabing lane. Bakit dapat kang mag-ingat?",
                optionsFil = listOf("Maaaring may pedestrian na hindi mo makita", "Palaging walang laman ang daan", "Dapat mong agad i-overtake ang nakatigil na sasakyan", "Awtomatikong binibigyan ka ng priyoridad ng nakatigil na sasakyan")
            ),
            QuizQuestion(
                id = 11,
                question = "A driver behind you is following too closely. What is the safest response?",
                options = listOf("Brake suddenly to teach the driver a lesson", "Maintain a safe pace and, when appropriate, allow the vehicle to pass", "Race the vehicle", "Block the vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Sobrang lapit na sumusunod ang driver sa likod mo. Ano ang pinakaligtas na gagawin?",
                optionsFil = listOf("Biglang magpreno para turuan ang driver", "Panatilihin ang ligtas na bilis at, kung angkop, payagan itong makadaan", "Karerahin ang sasakyan", "Harangin ang sasakyan")
            ),
            QuizQuestion(
                id = 12,
                question = "Your vehicle begins to skid on a slippery surface. What should you avoid?",
                options = listOf("Sudden, aggressive steering or braking", "Remaining calm", "Maintaining vehicle control", "Adjusting speed appropriately"),
                correctAnswerIndex = 0,
                questionFil = "Nagsimulang mag-skid ang sasakyan mo sa madulas na daan. Ano ang dapat mong iwasan?",
                optionsFil = listOf("Biglaan at agresibong pagliko ng manibela o pagpreno", "Panatilihing kalmado", "Panatilihin ang kontrol sa sasakyan", "Angkop na pag-aayos ng bilis")
            ),
            QuizQuestion(
                id = 13,
                question = "You are approaching a road intersection where your view is obstructed by a parked vehicle. What should you do?",
                options = listOf("Proceed quickly", "Slow down and ensure the way is clear", "Accelerate through the intersection", "Ignore the obstruction"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa isang interseksyon kung saan hinaharangan ng nakaparadang sasakyan ang iyong tanaw. Ano ang dapat mong gawin?",
                optionsFil = listOf("Magpatuloy nang mabilis", "Bagalan at siguraduhing malinaw ang daan", "Bilisan papasok sa interseksyon", "Huwag pansinin ang harang")
            ),
            QuizQuestion(
                id = 14,
                question = "You are tired, but you are only a few kilometers from your destination. What is the safest decision?",
                options = listOf("Continue because the destination is close", "Stop and rest if you are not fit to drive", "Drive faster to arrive sooner", "Drink something and continue regardless of fatigue"),
                correctAnswerIndex = 1,
                questionFil = "Pagod ka na, pero ilang kilometro na lang ang layo sa iyong patutunguhan. Ano ang pinakaligtas na desisyon?",
                optionsFil = listOf("Ituloy dahil malapit na ang patutunguhan", "Tumigil at magpahinga kung hindi ka na kayang magmaneho", "Magmaneho nang mas mabilis para makarating agad", "Uminom ng something at ituloy kahit pagod")
            ),
            QuizQuestion(
                id = 15,
                question = "You see a temporary traffic sign that differs from the normal road arrangement because of road construction. What should you do?",
                options = listOf("Follow the temporary traffic control", "Ignore it", "Follow the old road arrangement", "Drive around the barriers"),
                correctAnswerIndex = 0,
                questionFil = "May nakita kang pansamantalang traffic sign na iba sa normal na ayos ng daan dahil sa konstruksyon. Ano ang dapat mong gawin?",
                optionsFil = listOf("Sundin ang pansamantalang traffic control", "Huwag pansinin ito", "Sundin ang lumang ayos ng daan", "Umikot sa mga harang")
            ),
            QuizQuestion(
                id = 16,
                question = "You are entering a road where pedestrians, motorcycles, bicycles, and vehicles are all present. What is the best driving strategy?",
                options = listOf("Assume everyone will move out of your way", "Maintain awareness, reduce risk, and anticipate possible movements", "Drive at maximum speed", "Focus only on vehicles"),
                correctAnswerIndex = 1,
                questionFil = "Papasok ka sa daan kung saan magkakasamang naroroon ang mga pedestrian, motorsiklo, bisikleta, at sasakyan. Ano ang pinakamahusay na estratehiya sa pagmamaneho?",
                optionsFil = listOf("Ipagpalagay na lahat ay aalis sa iyong daraanan", "Manatiling alerto, bawasan ang panganib, at asahan ang posibleng galaw ng iba", "Magmaneho nang pinakamabilis", "Pansinin lamang ang mga sasakyan")
            ),
            QuizQuestion(
                id = 17,
                question = "A vehicle ahead signals that it intends to turn, but you are also approaching the same area. What should you do?",
                options = listOf("Ignore the signal", "Adjust your speed and position safely while considering the vehicle's movement", "Overtake immediately", "Drive beside it without checking"),
                correctAnswerIndex = 1,
                questionFil = "Nag-signal ang sasakyan sa unahan na balak nitong lumiko, pero papalapit ka rin sa parehong lugar. Ano ang dapat mong gawin?",
                optionsFil = listOf("Huwag pansinin ang signal", "Iayos ang iyong bilis at posisyon nang ligtas habang isinasaalang-alang ang galaw ng sasakyan", "Agad mag-overtake", "Sumabay dito nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 18,
                question = "You are driving on a road with a posted speed limit, but traffic, weather, and visibility conditions are poor. What should you prioritize?",
                options = listOf("Maximum speed", "Safe speed appropriate to the conditions while obeying the applicable limit", "Keeping up with the fastest vehicle", "Driving faster than the posted limit"),
                correctAnswerIndex = 1,
                questionFil = "Nagmamaneho ka sa daan na may nakasaad na speed limit, pero masama ang kalagayan ng trapiko, panahon, at paningin. Ano ang dapat mong unahin?",
                optionsFil = listOf("Pinakamabilis na bilis", "Ligtas na bilis na akma sa kalagayan habang sinusunod ang naaangkop na limitasyon", "Sabayan ang pinakamabilis na sasakyan", "Magmaneho nang mas mabilis sa nakasaad na limitasyon")
            ),
            QuizQuestion(
                id = 19,
                question = "A driver becomes angry after you make a legal maneuver. What is the best defensive-driving response?",
                options = listOf("Confront the driver", "Maintain composure and create distance from the aggressive driver", "Follow the driver", "Make an aggressive maneuver"),
                correctAnswerIndex = 1,
                questionFil = "Nagalit ang isang driver matapos mong gawin ang isang legal na galaw. Ano ang pinakamahusay na tugon sa defensive driving?",
                optionsFil = listOf("Harapin ang driver", "Manatiling kalmado at lumayo sa agresibong driver", "Sundan ang driver", "Gumawa ng agresibong galaw")
            ),
            QuizQuestion(
                id = 20,
                question = "You are approaching an intersection with several potential hazards: a pedestrian near the crossing, a motorcycle beside you, and a vehicle approaching from another direction. What should you do?",
                options = listOf("Focus only on the vehicle ahead", "Slow down, scan all relevant road users, and proceed only when safe and permitted", "Accelerate through the intersection", "Sound the horn and continue without checking"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa isang interseksyon na may ilang posibleng panganib: pedestrian malapit sa tawiran, motorsiklo sa tabi mo, at sasakyang paparating mula sa ibang direksyon. Ano ang dapat mong gawin?",
                optionsFil = listOf("Pansinin lamang ang sasakyan sa unahan", "Bagalan, tingnan ang lahat ng kaugnay na gumagamit ng daan, at magpatuloy lamang kapag ligtas at pinapayagan", "Bilisan papasok sa interseksyon", "Bumusina at magpatuloy nang hindi tumitingin")
            )
        )
    )
}
