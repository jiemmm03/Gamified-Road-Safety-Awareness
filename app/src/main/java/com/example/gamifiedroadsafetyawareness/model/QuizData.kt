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
                question = "What side of the road should vehicles generally keep to in the Philippines?",
                options = listOf("Right side", "Left side", "Center"),
                correctAnswerIndex = 0,
                questionFil = "Saang bahagi ng kalsada dapat karaniwang pumusisyon ang mga sasakyan sa Pilipinas?",
                optionsFil = listOf("Kanan na bahagi", "Kaliwa na bahagi", "Gitna")
            ),
            QuizQuestion(
                id = 2,
                question = "What should a driver do when approaching a red traffic light?",
                options = listOf("Stop", "Speed up", "Overtake"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag papalapit sa pulang ilaw-trapiko?",
                optionsFil = listOf("Huminto", "Bilisan", "Mag-overtake")
            ),
            QuizQuestion(
                id = 3,
                question = "What should a driver do before changing lanes?",
                options = listOf("Check mirrors and signal", "Accelerate immediately", "Turn off the headlights"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago lumipat ng linya (lane)?",
                optionsFil = listOf("Tumingin sa mga salamin at mag-signal", "Bumilis agad", "Patayin ang headlights")
            ),
            QuizQuestion(
                id = 4,
                question = "What does a STOP sign require a driver to do?",
                options = listOf("Slow down only", "Come to a complete stop", "Continue if there is no traffic"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang ipinag-uutos ng STOP sign sa isang driver?",
                optionsFil = listOf("Magbagal lamang", "Ganap na huminto", "Magpatuloy kung walang trapiko")
            ),
            QuizQuestion(
                id = 5,
                question = "What should a driver do when approaching a pedestrian crossing?",
                options = listOf("Speed up", "Be prepared to stop and yield when required", "Honk continuously"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin ng driver kapag papalapit sa tawiran ng tao (pedestrian crossing)?",
                optionsFil = listOf("Bilisan ang takbo", "Maging handang huminto at magbigay-daan kung kinakailangan", "Bumusina nang tuloy-tuloy")
            ),
            QuizQuestion(
                id = 6,
                question = "What is the main purpose of traffic rules?",
                options = listOf("To make driving more difficult", "To promote safe and orderly traffic", "To increase vehicle speed"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang pangunahing layunin ng mga batas at patakaran sa trapiko?",
                optionsFil = listOf("Gawing mas mahirap ang pagmamaneho", "Itaguyod ang ligtas at maayos na daloy ng trapiko", "Pabilisin ang takbo ng lahat ng sasakyan")
            ),
            QuizQuestion(
                id = 7,
                question = "What should a driver do when a traffic officer gives a signal that differs from the traffic light?",
                options = listOf("Follow the traffic officer's signal", "Ignore the officer", "Follow the vehicle ahead"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag ang senyas ng traffic officer ay iba sa ilaw-trapiko?",
                optionsFil = listOf("Sundin ang senyas ng traffic officer", "Sundin ang ilaw-trapiko", "Balewalain ang dalawa")
            ),
            QuizQuestion(
                id = 8,
                question = "What should a driver do before starting the vehicle?",
                options = listOf("Make sure the vehicle and surroundings are safe", "Immediately accelerate", "Honk continuously"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago paandarin ang sasakyan?",
                optionsFil = listOf("Tiyaking ligtas ang sasakyan at ang paligid nito", "Bumilis agad", "Huwag nang suriin ang mga salamin")
            ),
            QuizQuestion(
                id = 9,
                question = "What is a traffic intersection?",
                options = listOf("A place where roads meet or cross", "A vehicle parking area", "A gasoline station"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang isang interseksyon ng trapiko?",
                optionsFil = listOf("Lugar kung saan nagtatagpo o nagkrus ang mga kalsada", "Lugar para lamang sa paradahan", "Lugar para lamang sa paglalakad")
            ),
            QuizQuestion(
                id = 10,
                question = "What should a driver do when traffic is congested?",
                options = listOf("Drive aggressively", "Remain patient and follow traffic rules", "Use the sidewalk"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin ng driver kapag masikip ang daloy ng trapiko?",
                optionsFil = listOf("Manatiling pasensyoso at sundin ang mga patakaran sa trapiko", "Gamitin ang bangketa para mag-overtake", "Bumusina nang tuloy-tuloy")
            ),
            QuizQuestion(
                id = 11,
                question = "What kind of speed should a driver maintain?",
                options = listOf("A careful and prudent speed", "The fastest possible speed", "The same speed as every other vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang bilis na dapat panatilihin ng driver sa lahat ng oras?",
                optionsFil = listOf("Maingat at makatwirang bilis ayon sa kondisyon", "Pinakamabilis na posibleng bilis", "Kaparehong bilis ng iba pang sasakyan")
            ),
            QuizQuestion(
                id = 12,
                question = "Under RA 4136, what is the maximum speed for cars and motorcycles on certain open highways with no blind corners or closely bordered habitations, when no other speed restriction applies?",
                options = listOf("40 km/h", "60 km/h", "80 km/h"),
                correctAnswerIndex = 2,
                questionFil = "Ano ang pangkalahatang speed limit sa mga bukas na kalsada sa bansa para sa mga pampasaherong sasakyan?",
                optionsFil = listOf("80 km/h", "120 km/h", "140 km/h")
            ),
            QuizQuestion(
                id = 13,
                question = "What is the maximum speed for cars and motorcycles on city or municipal streets under the RA 4136 table, when no other speed restriction applies?",
                options = listOf("20 km/h", "30 km/h", "50 km/h"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang karaniwang speed limit sa mga abalang lansangan ng lungsod o bayan?",
                optionsFil = listOf("30 km/h", "70 km/h", "90 km/h")
            ),
            QuizQuestion(
                id = 14,
                question = "What speed is prescribed for crowded streets and similar dangerous circumstances under RA 4136, when no other speed restriction applies?",
                options = listOf("20 km/h", "40 km/h", "60 km/h"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang karaniwang speed limit kapag dumaraan sa mga school zone at matataong lugar?",
                optionsFil = listOf("20 km/h", "50 km/h", "80 km/h")
            ),
            QuizQuestion(
                id = 15,
                question = "Why should drivers reduce speed during dangerous road conditions?",
                options = listOf("To improve safety and control", "To save tire color", "To make the vehicle louder"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat sundin ng mga driver ang mga itinakdang speed limit?",
                optionsFil = listOf("Upang mapabuti ang kaligtasan at kontrol sa sasakyan", "Upang maingatan ang kulay ng gulong", "Upang mas maging maingay ang sasakyan")
            ),
            QuizQuestion(
                id = 16,
                question = "What should you do when visibility is poor?",
                options = listOf("Reduce speed and drive carefully", "Drive faster", "Close your eyes briefly"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nagmamaneho sa madulas o basang kalsada?",
                optionsFil = listOf("Bawasan ang bilis at magmaneho nang maingat", "Magmaneho nang mas mabilis", "Pumikit sandali habang nagmamaneho")
            ),
            QuizQuestion(
                id = 17,
                question = "What should a driver do when approaching a blind curve?",
                options = listOf("Reduce speed and exercise caution", "Overtake immediately", "Accelerate"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag mababa ang visibility dahil sa makapal na ulan o hamog?",
                optionsFil = listOf("Bawasan ang bilis at mag-ingat nang husto", "Mag-overtake agad", "Bumilis lalo")
            ),
            QuizQuestion(
                id = 18,
                question = "What is one danger of excessive speed?",
                options = listOf("Reduced reaction and stopping time", "Better vehicle control", "Better visibility"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang panganib ng pagpapatakbo nang sobrang bilis?",
                optionsFil = listOf("Nabawasang oras para mag-react at huminto", "Mas magandang kontrol sa sasakyan", "Mas malinaw na paningin")
            ),
            QuizQuestion(
                id = 19,
                question = "What should you do when approaching a school zone?",
                options = listOf("Slow down and watch for pedestrians", "Speed up", "Overtake all vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag dumaraan sa mga pamilihan o matataong lugar?",
                optionsFil = listOf("Magbagal at magbantay sa mga pedestrian", "Bilisan ang takbo", "Mag-overtake sa lahat ng sasakyan")
            ),
            QuizQuestion(
                id = 20,
                question = "What should a driver do when road conditions become hazardous?",
                options = listOf("Adjust speed and driving behavior", "Ignore the conditions", "Drive faster"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nagbago ang kondisyon ng kalsada o panahon?",
                optionsFil = listOf("Iangkop ang bilis at paraan ng pagmamaneho", "Balewalain ang mga kondisyon", "Magmaneho nang mas mabilis")
            ),
            QuizQuestion(
                id = 21,
                question = "When overtaking another vehicle, where should you generally pass?",
                options = listOf("On the left", "On the sidewalk", "On the shoulder"),
                correctAnswerIndex = 0,
                questionFil = "Saang bahagi karaniwang dapat isagawa ang pag-overtake sa Pilipinas?",
                optionsFil = listOf("Sa kaliwa", "Sa bangketa", "Sa shoulder o gilid ng kalsada")
            ),
            QuizQuestion(
                id = 22,
                question = "Before overtaking, what should a driver check?",
                options = listOf("Whether the maneuver can be made safely", "Only the vehicle's radio", "Only the fuel gauge"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat tiyakin ng driver bago mag-overtake?",
                optionsFil = listOf("Kung magagawa ang paglipat nang ligtas", "Tanging ang radyo ng sasakyan", "Tanging ang metro ng gasolina")
            ),
            QuizQuestion(
                id = 23,
                question = "What should the driver being overtaken do?",
                options = listOf("Increase speed", "Give way and not increase speed until completely passed", "Block the overtaking vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang dapat gawin ng driver kapag may ibang sasakyang nag-o-overtake sa kanya?",
                optionsFil = listOf("Magbigay-daan at huwag magpabilis hanggang makalagpas ito", "Bilisan ang takbo", "Harangan ang nag-o-overtake na sasakyan")
            ),
            QuizQuestion(
                id = 24,
                question = "Is overtaking generally allowed at a blind curve?",
                options = listOf("Yes, always", "No, when the view is obstructed", "Yes, if the vehicle is fast"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang pag-overtake sa kurbada kung saan hindi tanaw ang kasalubong?",
                optionsFil = listOf("Oo, ipinagbabawal kapag obstructed ang paningin", "Hindi, palaging pinapayagan", "Oo, ngunit kung mabilis lamang ang sasakyan")
            ),
            QuizQuestion(
                id = 25,
                question = "Is overtaking generally allowed at a railway crossing?",
                options = listOf("Yes, always", "No, subject to the legal exceptions", "Only at night"),
                correctAnswerIndex = 1,
                questionFil = "Pinapayagan ba ang pag-overtake sa isang interseksyon?",
                optionsFil = listOf("Hindi, maliban sa mga itinakdang legal na eksepsiyon", "Oo, palagi", "Sa gabi lamang")
            ),
            QuizQuestion(
                id = 26,
                question = "What should you do if you cannot clearly see the road ahead while considering an overtake?",
                options = listOf("Do not overtake", "Overtake immediately", "Use the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago ang tulay kung saan makitid ang kalsada?",
                optionsFil = listOf("Huwag mag-overtake", "Mag-overtake agad", "Gamitin ang bangketa")
            ),
            QuizQuestion(
                id = 27,
                question = "What should you do when approaching a no-passing zone?",
                options = listOf("Do not overtake", "Overtake quickly", "Drive on the shoulder"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag papalapit sa tawiran ng tren (railway crossing)?",
                optionsFil = listOf("Huwag mag-overtake", "Mag-overtake nang mabilis", "Magmaneho sa shoulder")
            ),
            QuizQuestion(
                id = 28,
                question = "When is overtaking safer?",
                options = listOf("When there is sufficient clear distance and visibility", "At a blind curve", "At a pedestrian crossing"),
                correctAnswerIndex = 0,
                questionFil = "Kailan lamang dapat isagawa ang pag-overtake?",
                optionsFil = listOf("Kapag may sapat na malinaw na distansya at visibility", "Sa isang blind curve", "Sa tawiran ng pedestrian")
            ),
            QuizQuestion(
                id = 29,
                question = "What should you do after overtaking another vehicle?",
                options = listOf("Return to the proper lane only when safely clear", "Immediately cut in", "Stop in front of the vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver matapos mag-overtake?",
                optionsFil = listOf("Bumalik sa tamang lane kapag ligtas at malinaw na nakalagpas", "Agad na sumingit nang bigla", "Huminto sa harap ng sasakyan")
            ),
            QuizQuestion(
                id = 30,
                question = "What should you avoid when another vehicle is overtaking you?",
                options = listOf("Increasing your speed", "Maintaining a safe speed", "Giving way"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ipinagbabawal habang may ibang sasakyang sumusubok mag-overtake sa iyo?",
                optionsFil = listOf("Ang pagpapabilis ng iyong takbo", "Ang pagpapanatili ng ligtas na bilis", "Ang pagbibigay-daan")
            ),
            QuizQuestion(
                id = 31,
                question = "If two vehicles approach an intersection at approximately the same time, which vehicle generally has the right-of-way?",
                options = listOf("Vehicle on the right", "Vehicle on the left", "Faster vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Kapag dalawang sasakyan ang sabay na dumating sa interseksyon, alin ang karaniwang may right-of-way?",
                optionsFil = listOf("Ang sasakyang nasa kanan", "Ang sasakyang nasa kaliwa", "Ang mas mabilis na sasakyan")
            ),
            QuizQuestion(
                id = 32,
                question = "What should a vehicle entering a highway from a private road do?",
                options = listOf("Yield to vehicles already on the highway", "Force its way into traffic", "Stop traffic completely"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng sasakyang pumapasok sa pambansang highway mula sa pribadong daan?",
                optionsFil = listOf("Magbigay-daan sa mga sasakyang nasa highway na", "Pumilit pumasok sa trapiko", "Patigilin ang buong trapiko")
            ),
            QuizQuestion(
                id = 33,
                question = "Who should generally be given right-of-way at a crosswalk in a business or residential district?",
                options = listOf("Pedestrians crossing within the crosswalk", "Parked vehicles", "Vehicles entering from a driveway"),
                correctAnswerIndex = 0,
                questionFil = "Sino ang may right-of-way sa isang minarkahang pedestrian crossing?",
                optionsFil = listOf("Mga pedestrian na tumatawid sa loob ng crosswalk", "Mga nakaparadang sasakyan", "Mga sasakyang galing sa driveway")
            ),
            QuizQuestion(
                id = 34,
                question = "What should you do when an ambulance approaches with an audible signal?",
                options = listOf("Give way", "Race ahead", "Block its path"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag papalapit ang isang emergency vehicle na may sirena at umiilaw?",
                optionsFil = listOf("Magbigay-daan", "Makipagkarera sa emergency vehicle", "Harangan ang daanan nito")
            ),
            QuizQuestion(
                id = 35,
                question = "What should you do when a police vehicle on official business approaches with an audible signal?",
                options = listOf("Give way", "Follow closely", "Block the vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag may paparating na bumbero patungo sa sunog?",
                optionsFil = listOf("Magbigay-daan", "Sumunod nang sobrang lapit", "Harangan ang bumbero")
            ),
            QuizQuestion(
                id = 36,
                question = "What should a driver do when entering a through highway?",
                options = listOf("Yield to vehicles approaching on the through highway", "Immediately enter without checking", "Drive against traffic"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag pumapasok sa through highway mula sa kalsadang may STOP sign?",
                optionsFil = listOf("Magbigay-daan sa mga sasakyang paparating sa through highway", "Agad na pumasok nang hindi tumitingin", "Magmaneho laban sa daloy ng trapiko")
            ),
            QuizQuestion(
                id = 37,
                question = "What should you do when another vehicle already occupies the intersection?",
                options = listOf("Allow it to proceed when required by right-of-way rules", "Force your way through", "Overtake it inside the intersection"),
                correctAnswerIndex = 0,
                questionFil = "Kapag may sasakyang nakapasok na sa interseksyon bago ka dumating, ano ang dapat mong gawin?",
                optionsFil = listOf("Hayaan itong makadaan ayon sa alituntunin sa right-of-way", "Pumilit na makalusot", "I-overtake ito sa loob ng interseksyon")
            ),
            QuizQuestion(
                id = 38,
                question = "Does driving at an unlawful speed affect your right-of-way?",
                options = listOf("Yes", "No", "Only at night"),
                correctAnswerIndex = 0,
                questionFil = "Dapat bang laging mag-ingat ang driver kahit sila ang may legal na right-of-way?",
                optionsFil = listOf("Oo", "Hindi", "Sa gabi lamang")
            ),
            QuizQuestion(
                id = 39,
                question = "What is the purpose of right-of-way rules?",
                options = listOf("To prevent conflicts between road users", "To make vehicles travel faster", "To allow drivers to ignore traffic signs"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang layunin ng mga alituntunin sa right-of-way?",
                optionsFil = listOf("Maiwasan ang alitan at banggaan ng mga gumagamit ng kalsada", "Pabilisin ang takbo ng mga sasakyan", "Pahintulutan ang mga driver na balewalain ang mga sign")
            ),
            QuizQuestion(
                id = 40,
                question = "What should a driver do when unsure who has the right-of-way?",
                options = listOf("Proceed cautiously and avoid forcing the situation", "Speed up", "Honk continuously and proceed"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag hindi sigurado kung sino ang may right-of-way sa interseksyon?",
                optionsFil = listOf("Magpatuloy nang may pag-iingat at huwag ipilit ang daan", "Bilisan ang takbo", "Tuloy-tuloy na bumusina at dumiretso")
            ),
            QuizQuestion(
                id = 41,
                question = "What should a driver do before turning?",
                options = listOf("Make sure the movement can be made safely", "Turn without checking", "Close the windows"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago lumiko sa isang interseksyon?",
                optionsFil = listOf("Tiyaking magagawa ang pagliko nang ligtas", "Lumiko nang hindi tumitingin", "Isara ang mga bintana")
            ),
            QuizQuestion(
                id = 42,
                question = "What should a driver use to indicate a turn?",
                options = listOf("Turn signal", "Headlights only", "Hazard lights only"),
                correctAnswerIndex = 0,
                questionFil = "Anong signal ang dapat gamitin ng driver bago lumiko?",
                optionsFil = listOf("Turn signal (signal light)", "Headlights lamang", "Hazard lights lamang")
            ),
            QuizQuestion(
                id = 43,
                question = "Before changing direction, what should you check?",
                options = listOf("Traffic and surrounding road users", "Only the dashboard", "Only the radio"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat suriin ng driver bago lumiko?",
                optionsFil = listOf("Trapiko at iba pang gumagamit ng kalsada", "Tanging ang dashboard", "Tanging ang radyo")
            ),
            QuizQuestion(
                id = 44,
                question = "When turning right, which side should the vehicle generally approach from?",
                options = listOf("The lane nearest the right side of the highway", "The opposite lane", "The sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Saang linya dapat pumuwesto ang driver bago kumanan (right turn)?",
                optionsFil = listOf("Sa linyang pinakamalapit sa kanang bahagi ng kalsada", "Sa kabilang linya (kasalubong)", "Sa bangketa")
            ),
            QuizQuestion(
                id = 45,
                question = "What should a driver do before making a left turn?",
                options = listOf("Signal and check for approaching traffic", "Turn suddenly", "Accelerate without checking"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago kumaliwa (left turn)?",
                optionsFil = listOf("Mag-signal at magbantay sa mga paparating na sasakyan", "Biglang lumiko", "Bumilis nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 46,
                question = "Why are turn signals important?",
                options = listOf("They communicate the driver's intention", "They increase engine power", "They reduce tire pressure"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang paggamit ng turn signal?",
                optionsFil = listOf("Ipinapaalam nito ang intensyon ng driver sa iba", "Nagpapalakas ito ng makina", "Nagbabawas ito ng presyon ng gulong")
            ),
            QuizQuestion(
                id = 47,
                question = "When should a turn signal be used?",
                options = listOf("Before making a turn or lane movement", "Only after turning", "Only when stopped"),
                correctAnswerIndex = 0,
                questionFil = "Kailan dapat magbigay ng turn signal ang driver?",
                optionsFil = listOf("Bago simulan ang pagliko o paglipat ng linya", "Pagkatapos lamang lumiko", "Kapag nakahinto lamang")
            ),
            QuizQuestion(
                id = 48,
                question = "What should you do if another vehicle may be affected by your turn?",
                options = listOf("Give an appropriate signal", "Turn without warning", "Speed up"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago huminto o bumagal nang malaki?",
                optionsFil = listOf("Magbigay ng angkop na signal (brake light / hand signal)", "Huminto nang walang babala", "Bilisan ang takbo")
            ),
            QuizQuestion(
                id = 49,
                question = "What should you do if turning is unsafe because of approaching traffic?",
                options = listOf("Wait until it is safe", "Turn immediately", "Drive onto the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag lumiliko pakaliwa at may paparating na kasalubong?",
                optionsFil = listOf("Maghintay hanggang sa maging ligtas bago lumiko", "Lumiko agad bago sila makarating", "Umakyat sa bangketa")
            ),
            QuizQuestion(
                id = 50,
                question = "What should you do after signaling a turn?",
                options = listOf("Check that the movement is safe before turning", "Turn automatically without checking", "Accelerate immediately"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin bago mag-U-turn?",
                optionsFil = listOf("Tiyaking ligtas at legal ang pag-U-turn sa lugar na iyon", "Mag-U-turn agad nang hindi tumitingin", "Bumilis agad")
            ),
            QuizQuestion(
                id = 51,
                question = "Is parking allowed in the middle of an intersection?",
                options = listOf("Yes", "No", "Only at night"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang pagparada sa loob ng interseksyon?",
                optionsFil = listOf("Oo", "Hindi", "Sa gabi lamang")
            ),
            QuizQuestion(
                id = 52,
                question = "Is parking on a pedestrian crosswalk allowed?",
                options = listOf("Yes", "No", "Only for motorcycles"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang pagparada sa tawiran ng pedestrian (crosswalk)?",
                optionsFil = listOf("Oo", "Hindi", "Para lamang sa mga motorsiklo")
            ),
            QuizQuestion(
                id = 53,
                question = "Is parking in front of a private driveway allowed?",
                options = listOf("Yes", "No", "Only for five minutes"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang pagparada sa tapat o malapit sa fire hydrant?",
                optionsFil = listOf("Oo", "Hindi", "Pwede hanggang limang minuto lamang")
            ),
            QuizQuestion(
                id = 54,
                question = "Is parking on a sidewalk intended for pedestrians allowed?",
                options = listOf("Yes", "No", "Only when traffic is heavy"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang pagparada sa mga tulay o flyover?",
                optionsFil = listOf("Oo", "Hindi", "Kung mabigat lamang ang trapiko")
            ),
            QuizQuestion(
                id = 55,
                question = "What should you do when parking an unattended vehicle on a highway?",
                options = listOf("Turn off the engine and apply the hand brake", "Leave the engine running", "Leave the vehicle in gear without securing it"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag iiwanang nakaparada ang sasakyan?",
                optionsFil = listOf("Patayin ang makina at ikabit ang handbrake", "Iwanang umaandar ang makina", "Iwanang nakakambiyo nang walang handbrake")
            ),
            QuizQuestion(
                id = 56,
                question = "Can you park where an official sign prohibits parking?",
                options = listOf("Yes", "No", "Only during the day"),
                correctAnswerIndex = 1,
                questionFil = "Ipinagbabawal ba ang double parking (pagparada sa tabi ng nakaparada nang sasakyan)?",
                optionsFil = listOf("Oo", "Hindi", "Sa araw lamang")
            ),
            QuizQuestion(
                id = 57,
                question = "Why should drivers avoid blocking driveways?",
                options = listOf("To allow vehicles to enter and exit", "To save fuel", "To increase traffic"),
                correctAnswerIndex = 0,
                questionFil = "Bakit ipinagbabawal ang pagparada sa tapat ng pribadong driveway?",
                optionsFil = listOf("Upang makalabas at makapasok ang mga sasakyan nang maayos", "Upang makatipid sa gasolina", "Upang dumami ang trapiko")
            ),
            QuizQuestion(
                id = 58,
                question = "Should a vehicle be parked where it obstructs traffic?",
                options = listOf("No", "Yes", "Always"),
                correctAnswerIndex = 0,
                questionFil = "Maaari bang pumarada ang driver sa lugar na may 'NO PARKING' sign?",
                optionsFil = listOf("Hindi", "Oo", "Palagi")
            ),
            QuizQuestion(
                id = 59,
                question = "What should you do before leaving a parked vehicle?",
                options = listOf("Secure the vehicle properly", "Leave it running", "Leave the hand brake released"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag nagpaparada sa pataas o pababang kalsada (slope/incline)?",
                optionsFil = listOf("I-secure nang maayos ang sasakyan (handbrake at tamang pihit ng gulong)", "Iwanang umaandar ang makina", "Huwag gamitin ang handbrake")
            ),
            QuizQuestion(
                id = 60,
                question = "What should you do when parking near a fire hydrant?",
                options = listOf("Avoid parking there", "Park directly in front of it", "Block it temporarily"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may nakitang emergency exit o fire station driveway?",
                optionsFil = listOf("Iwasang pumarada roon", "Pumarada mismo sa tapat nito", "Harangan ito pansamantala")
            ),
            QuizQuestion(
                id = 61,
                question = "What does a red traffic light generally mean?",
                options = listOf("Stop", "Go", "Speed up"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ibig sabihin ng pulang ilaw-trapiko?",
                optionsFil = listOf("Huminto", "Magpatuloy", "Bilisan")
            ),
            QuizQuestion(
                id = 62,
                question = "What does a green traffic light generally mean?",
                options = listOf("Proceed when the way is clear and safe", "Stop immediately in all situations", "Reverse"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ibig sabihin ng berdeng ilaw-trapiko?",
                optionsFil = listOf("Magpatuloy kung malinaw at ligtas ang daan", "Huminto agad sa lahat ng pagkakataon", "Umatras")
            ),
            QuizQuestion(
                id = 63,
                question = "What does a yellow traffic light generally warn?",
                options = listOf("The signal is changing; proceed with caution", "Speed up", "Park immediately"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ibig sabihin ng dilaw na ilaw-trapiko?",
                optionsFil = listOf("Maghanda sa paghinto dahil magpapalit na ang ilaw", "Bilisan ang takbo", "Pumarada agad")
            ),
            QuizQuestion(
                id = 64,
                question = "What is the purpose of a STOP sign?",
                options = listOf("To require a stop", "To indicate parking", "To indicate a gasoline station"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing layunin ng isang STOP sign?",
                optionsFil = listOf("Mag-utos ng ganap na paghinto", "Magpahiwatig ng paradahan", "Magturo ng gasolinahan")
            ),
            QuizQuestion(
                id = 65,
                question = "What is the purpose of traffic signs?",
                options = listOf("To provide information, warnings, or regulations", "To decorate roads", "To increase vehicle speed"),
                correctAnswerIndex = 0,
                questionFil = "Bakit inilalagay ang mga traffic sign sa mga kalsada?",
                optionsFil = listOf("Upang magbigay ng impormasyon, babala, o regulasyon", "Pampaganda lamang ng kalsada", "Upang pabilisin ang mga sasakyan")
            ),
            QuizQuestion(
                id = 66,
                question = "What should you do when you see a warning sign?",
                options = listOf("Be alert and adjust your driving as necessary", "Ignore it", "Speed up"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag may nakitang warning sign (tatsulok o diyamante)?",
                optionsFil = listOf("Maging alerto at iangkop ang pagmamaneho kung kinakailangan", "Balewalain ito", "Bilisan ang takbo")
            ),
            QuizQuestion(
                id = 67,
                question = "What should you do when a sign prohibits an action?",
                options = listOf("Follow the restriction", "Ignore it", "Follow only if other drivers do"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag may nakitang regulatory sign (tulad ng Speed Limit o No Entry)?",
                optionsFil = listOf("Sundin ang regulasyon o pagbabawal", "Balewalain ito", "Sundin lamang kung may ibang driver na sumusunod")
            ),
            QuizQuestion(
                id = 68,
                question = "What should drivers do when road markings indicate a restriction on crossing or overtaking?",
                options = listOf("Follow the road marking", "Ignore it", "Drive on the shoulder"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may nakitang solid white line sa gitna ng kalsada?",
                optionsFil = listOf("Sundin ang linya at iwasang magpalit ng lane o mag-overtake", "Balewalain ito", "Magmaneho sa shoulder")
            ),
            QuizQuestion(
                id = 69,
                question = "What is the purpose of road markings?",
                options = listOf("To guide and regulate road users", "To make roads attractive", "To increase engine performance"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang layunin ng mga road pavement markings (mga pinta sa kalsada)?",
                optionsFil = listOf("Gabayan at isaayos ang mga gumagamit ng kalsada", "Pampaganda lamang ng daan", "Pataasin ang lakas ng makina")
            ),
            QuizQuestion(
                id = 70,
                question = "What should a driver do when a traffic signal is not functioning?",
                options = listOf("Proceed cautiously and follow applicable traffic rules", "Speed through the intersection", "Ignore other vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag kumikislap ang dilaw na ilaw-trapiko (flashing yellow)?",
                optionsFil = listOf("Magpatuloy nang may pag-iingat at magmatyag sa trapiko", "Harurutin ang pagtawid sa interseksyon", "Balewalain ang ibang sasakyan")
            ),
            QuizQuestion(
                id = 71,
                question = "Should a driver operate a motor vehicle while intoxicated by liquor or narcotic drugs?",
                options = listOf("Yes", "No", "Only on empty roads"),
                correctAnswerIndex = 1,
                questionFil = "Kailangan bang magkaroon ng balidong lisensya ang bawat driver bago magmaneho?",
                optionsFil = listOf("Oo", "Hindi", "Sa mga bakanteng kalsada lamang")
            ),
            QuizQuestion(
                id = 72,
                question = "What is reckless driving?",
                options = listOf("Driving without reasonable caution and endangering others", "Driving slowly", "Driving with headlights on"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ibig sabihin ng reckless driving (walang ingat na pagmamaneho)?",
                optionsFil = listOf("Pagmamaneho nang walang sapat na pag-iingat na naglalagay sa iba sa panganib", "Mabagal na pagmamaneho", "Pagmamaneho nang nakabukas ang headlights")
            ),
            QuizQuestion(
                id = 73,
                question = "What should a responsible driver prioritize?",
                options = listOf("Safety", "Speed", "Competition"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat maging pangunahing prayoridad ng isang responsableng driver?",
                optionsFil = listOf("Kaligtasan", "Bilis", "Pakikipagkumpitensya")
            ),
            QuizQuestion(
                id = 74,
                question = "Should drivers obey traffic laws?",
                options = listOf("Yes", "No", "Only when police are present"),
                correctAnswerIndex = 0,
                questionFil = "Dapat bang magsuot ng seatbelt ang driver sa sasakyang mayroon nito?",
                optionsFil = listOf("Oo", "Hindi", "Kapag may pulis lamang")
            ),
            QuizQuestion(
                id = 75,
                question = "What should a driver do when tired and unable to drive safely?",
                options = listOf("Stop and rest", "Drive faster", "Ignore the fatigue"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nakakaramdam ng matinding antok o pagod habang nagmamaneho?",
                optionsFil = listOf("Huminto sa ligtas na lugar at magpahinga", "Magpatakbo nang mas mabilis", "Balewalain ang pagod")
            ),
            QuizQuestion(
                id = 76,
                question = "What should a driver do when visibility is reduced by weather?",
                options = listOf("Drive more cautiously", "Speed up", "Ignore the weather"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nagmamaneho sa masamang panahon?",
                optionsFil = listOf("Magmaneho nang mas maingat at magbawas ng bilis", "Bilisan ang takbo", "Balewalain ang lagay ng panahon")
            ),
            QuizQuestion(
                id = 77,
                question = "What should drivers do when approaching pedestrians?",
                options = listOf("Exercise caution", "Speed up", "Ignore them"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may mga bata o matatandang naglalakad malapit sa kalsada?",
                optionsFil = listOf("Mag-ingat nang husto at maghandang magpreno", "Bilisan ang takbo", "Balewalain sila")
            ),
            QuizQuestion(
                id = 78,
                question = "What should a driver do if the road becomes slippery?",
                options = listOf("Reduce speed and maintain control", "Accelerate sharply", "Brake aggressively at all times"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag nagmamaneho sa madulas na kalsada?",
                optionsFil = listOf("Bawasan ang bilis at panatilihin ang kontrol sa manibela", "Biglang bumilis", "Magpreno nang bigla sa lahat ng oras")
            ),
            QuizQuestion(
                id = 79,
                question = "Should a driver intentionally obstruct traffic?",
                options = listOf("No", "Yes", "Only during rush hour"),
                correctAnswerIndex = 0,
                questionFil = "Ipinagbabawal ba ang pagmamaneho sa ilalim ng impluwensya ng alak o droga?",
                optionsFil = listOf("Hindi (Bawal)", "Oo (Pinapayagan)", "Tuwing rush hour lamang")
            ),
            QuizQuestion(
                id = 80,
                question = "What should a driver do when another road user makes a mistake?",
                options = listOf("Stay calm and avoid creating another hazard", "Chase the vehicle", "Drive aggressively"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may isa pang driver na nagpapakita ng agresibong pagmamaneho (road rage)?",
                optionsFil = listOf("Manatiling kalmado at iwasang lumikha ng karagdagang panganib", "Habulan ang sasakyan", "Makipaggirian nang agresibo")
            ),
            QuizQuestion(
                id = 81,
                question = "What should you do when an ambulance with its siren approaches?",
                options = listOf("Give way", "Race the ambulance", "Block the lane"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may paparating na ambulansyang may sirena?",
                optionsFil = listOf("Magbigay-daan agad sa pamamagitan ng pagtabi sa kanan", "Makipagkarera sa ambulansya", "Harangan ang lane")
            ),
            QuizQuestion(
                id = 82,
                question = "What should you do when a fire truck approaches with an audible warning?",
                options = listOf("Give way", "Follow closely", "Block it"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may paparating na patrol car ng pulis na may sirena at umiilaw?",
                optionsFil = listOf("Magbigay-daan", "Sumunod nang sobrang lapit sa likod nito", "Harangan ito")
            ),
            QuizQuestion(
                id = 83,
                question = "What should you do when a police vehicle approaches on official business with an audible signal?",
                options = listOf("Give way", "Overtake it", "Block it"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag may convoy ng emergency vehicles na may sirena?",
                optionsFil = listOf("Magbigay-daan", "I-overtake ang convoy", "Harangan ito")
            ),
            QuizQuestion(
                id = 84,
                question = "Why should emergency vehicles be given priority?",
                options = listOf("They may be responding to an emergency", "They are always faster", "They are exempt from all traffic rules"),
                correctAnswerIndex = 0,
                questionFil = "Bakit may espesyal na right-of-way ang mga emergency vehicle?",
                optionsFil = listOf("Maaaring rumeresponde sila sa gipit na sitwasyon upang magligtas ng buhay", "Dahil palagi silang mas mabilis", "Dahil exempted sila sa lahat ng batas")
            ),
            QuizQuestion(
                id = 85,
                question = "What should you do when an emergency vehicle is approaching from behind?",
                options = listOf("Safely move toward the right and stop when required", "Speed up", "Block the vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang ligtas na paraan ng pagbibigay-daan sa emergency vehicle?",
                optionsFil = listOf("Ligtas na tumabi pakanan at huminto kung kinakailangan", "Bilisan ang takbo", "Harangan ang daan")
            ),
            QuizQuestion(
                id = 86,
                question = "Should you follow an emergency vehicle closely just to pass traffic?",
                options = listOf("No", "Yes", "Always"),
                correctAnswerIndex = 0,
                questionFil = "Pinapayagan ba ang pagsunod nang napakalapit sa likod ng rumerespondeng emergency vehicle?",
                optionsFil = listOf("Hindi", "Oo", "Palagi")
            ),
            QuizQuestion(
                id = 87,
                question = "What should you do if a traffic officer directs you to stop?",
                options = listOf("Stop safely", "Ignore the officer", "Accelerate"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag pinatitigil ka ng isang traffic police officer?",
                optionsFil = listOf("Ligtas na huminto", "Balewalain ang pulis", "Bumilis lalo")
            ),
            QuizQuestion(
                id = 88,
                question = "What should you do when an accident is blocking part of the road?",
                options = listOf("Slow down and proceed with caution", "Speed through the area", "Drive onto the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag nagmamaneho sa lugar kung saan may aksidente sa kalsada?",
                optionsFil = listOf("Magbagal at magpatuloy nang may pag-iingat", "Harurutin ang pagdaan sa lugar", "Umakyat sa bangketa")
            ),
            QuizQuestion(
                id = 89,
                question = "What should you do when road workers are present?",
                options = listOf("Slow down and follow warning signs", "Overtake aggressively", "Ignore them"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag dumaraan sa lugar na may road work o konstruksyon?",
                optionsFil = listOf("Magbagal at sundin ang mga babalang sign", "Mag-overtake nang agresibo", "Balewalain ang mga sign")
            ),
            QuizQuestion(
                id = 90,
                question = "Why should drivers be cautious around road construction?",
                options = listOf("Road conditions and traffic patterns may change", "Construction makes vehicles faster", "There are no hazards"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat mag-ingat nang husto sa mga road work zone?",
                optionsFil = listOf("Maaaring magbago ang kondisyon ng kalsada at daloy ng trapiko", "Dahil bumibilis ang takbo ng sasakyan sa construction", "Dahil walang anumang panganib doon")
            ),
            QuizQuestion(
                id = 91,
                question = "What should a driver check before driving?",
                options = listOf("The vehicle's basic safety condition", "Only the radio", "Only the paint"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat regular na suriin ng driver sa kanyang sasakyan bago bumiyahe?",
                optionsFil = listOf("Ang kaligtasan at kondisyon ng sasakyan (BLOWBAGETS)", "Tanging ang radyo", "Tanging ang pintura")
            ),
            QuizQuestion(
                id = 92,
                question = "Why are functioning brakes important?",
                options = listOf("They help the driver slow down and stop safely", "They increase fuel consumption", "They make the vehicle louder"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang maayos na kondisyon ng preno (brakes) ng sasakyan?",
                optionsFil = listOf("Tinutulungan nito ang driver na magbagal at huminto nang ligtas", "Nagpapataas ito ng konsumo sa gasolina", "Ginagawa nitong mas maingay ang sasakyan")
            ),
            QuizQuestion(
                id = 93,
                question = "Why are functioning lights important?",
                options = listOf("They improve visibility and communication with other road users", "They increase engine power", "They make the vehicle heavier"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang maayos na gumaganang headlights at taillights?",
                optionsFil = listOf("Pinapabuti nito ang paningin at pakikipag-ugnayan sa ibang motorista", "Nagpapalakas ito ng makina", "Pinapabigat nito ang sasakyan")
            ),
            QuizQuestion(
                id = 94,
                question = "Why should tires have proper condition and inflation?",
                options = listOf("For safe vehicle control and road contact", "To make the vehicle louder", "To increase the horn volume"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalagang may tamang presyon at sapat na tread ang mga gulong?",
                optionsFil = listOf("Para sa ligtas na kontrol at kapit ng sasakyan sa kalsada", "Para mas maging maingay ang sasakyan", "Para lumakas ang tunog ng busina")
            ),
            QuizQuestion(
                id = 95,
                question = "What is the purpose of a seat belt?",
                options = listOf("To help protect occupants during a crash", "To increase speed", "To improve the radio signal"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing silbi ng seatbelt sa loob ng sasakyan?",
                optionsFil = listOf("Protektahan ang mga sakay sa oras ng banggaan", "Pabilisin ang takbo ng sasakyan", "Palakasin ang signal ng radyo")
            ),
            QuizQuestion(
                id = 96,
                question = "Should a driver use a vehicle with a serious safety defect?",
                options = listOf("No", "Yes", "Always"),
                correctAnswerIndex = 0,
                questionFil = "Ligtas ba ang magmaneho ng sasakyang may sirang preno o pundidong ilaw?",
                optionsFil = listOf("Hindi", "Oo", "Palagi")
            ),
            QuizQuestion(
                id = 97,
                question = "What should you do if your vehicle suddenly develops a serious problem while driving?",
                options = listOf("Safely slow down and move to a safe location when possible", "Continue at high speed", "Ignore the problem"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag nagkaroon ng mekanikal na aberya ang sasakyan habang nagmamaneho?",
                optionsFil = listOf("Ligtas na magbagal at tumabi sa ligtas na lugar (shoulder)", "Magpatuloy sa mabilis na takbo", "Balewalain ang problema")
            ),
            QuizQuestion(
                id = 98,
                question = "Why is proper vehicle maintenance important?",
                options = listOf("It helps maintain safe vehicle operation", "It makes traffic lights change faster", "It removes the need for a driver's license"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang regular na preventive maintenance sa sasakyan?",
                optionsFil = listOf("Nakatutulong itong mapanatili ang ligtas at maayos na operasyon ng sasakyan", "Pinapabilis nito ang pagpapalit ng ilaw-trapiko", "Tinatanggal nito ang pangangailangan ng lisensya")
            ),
            QuizQuestion(
                id = 99,
                question = "What should a driver do before opening the vehicle door beside moving traffic?",
                options = listOf("Check for approaching road users", "Open it immediately", "Leave the door open"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago buksan ang pinto ng sasakyan kapag nakaparada sa tabi ng kalsada?",
                optionsFil = listOf("Tumingin sa salamin at sumilip para sa mga paparating na siklista, motorista, o tao", "Buksan agad nang buong lakas", "Iwanang laging nakabukas ang pinto")
            ),
            QuizQuestion(
                id = 100,
                question = "What is the most important principle of responsible driving?",
                options = listOf("Safety of all road users", "Driving faster than others", "Winning against other drivers"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pinakamahalagang prinsipyo ng responsableng pagmamaneho?",
                optionsFil = listOf("Kaligtasan ng lahat ng gumagamit ng kalsada", "Pagpapatakbo nang mas mabilis kaysa sa iba", "Pakikipagkarera sa ibang mga driver")
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
                question = "Two vehicles arrive at an intersection at nearly the same time. If there is no traffic sign or signal controlling the movement, which vehicle generally has the right-of-way?",
                options = listOf("Vehicle on the left", "Vehicle on the right", "Faster vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Dalawang sasakyan ang halos sabay na dumating sa interseksyon na walang traffic sign o signal. Aling sasakyan ang may right-of-way?",
                optionsFil = listOf("Sasakyang nasa kaliwa", "Sasakyang nasa kanan", "Mas mabilis na sasakyan")
            ),
            QuizQuestion(
                id = 2,
                question = "A driver approaches an intersection where another vehicle is already crossing. What should the driver do?",
                options = listOf("Speed up and cross first", "Yield and allow the vehicle to clear the intersection", "Honk and continue"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa interseksyon kung saan may tumatawid nang ibang sasakyan. Ano ang dapat gawin?",
                optionsFil = listOf("Bilisan at maunang tumawid", "Magbigay-daan at hayaang makalagpas ang sasakyan", "Bumusina at dumiretso")
            ),
            QuizQuestion(
                id = 3,
                question = "A vehicle is entering a highway from a private road. What should the driver do?",
                options = listOf("Yield to traffic already on the highway", "Immediately enter the highway", "Force approaching vehicles to stop"),
                correctAnswerIndex = 0,
                questionFil = "Papasok ang isang sasakyan sa pambansang highway mula sa pribadong daan. Ano ang dapat gawin ng driver?",
                optionsFil = listOf("Magbigay-daan sa trapiko na nasa highway na", "Agad na pumasok sa highway", "Puwersahing huminto ang mga paparating na sasakyan")
            ),
            QuizQuestion(
                id = 4,
                question = "When making a left turn at an intersection, what should the driver do before turning?",
                options = listOf("Check approaching traffic and signal", "Move directly into the opposite lane", "Turn without slowing"),
                correctAnswerIndex = 0,
                questionFil = "Bago lumiko pakaliwa sa isang interseksyon, ano ang dapat gawin ng driver?",
                optionsFil = listOf("Suriin ang paparating na trapiko at mag-signal", "Agad na lumipat sa kabilang linya", "Lumiko nang hindi nagbabawas ng bilis")
            ),
            QuizQuestion(
                id = 5,
                question = "A driver reaches a STOP sign but sees no approaching vehicles. What should the driver do?",
                options = listOf("Slow down and continue", "Make a complete stop before proceeding", "Ignore the sign"),
                correctAnswerIndex = 1,
                questionFil = "Nakarating ang driver sa isang STOP sign ngunit walang nakikitang paparating na sasakyan. Ano ang dapat gawin?",
                optionsFil = listOf("Magbagal at magpatuloy", "Ganap na huminto bago magpatuloy", "Huwag pansinin ang karatula")
            ),
            QuizQuestion(
                id = 6,
                question = "When a traffic officer's hand signal conflicts with the traffic light, which should the driver follow?",
                options = listOf("Traffic light", "Traffic officer", "Vehicle ahead"),
                correctAnswerIndex = 1,
                questionFil = "Kapag ang senyas ng kamay ng traffic officer ay sumasalungat sa ilaw-trapiko, ano ang dapat sundin ng driver?",
                optionsFil = listOf("Ilaw-trapiko", "Traffic officer", "Sasakyan sa unahan")
            ),
            QuizQuestion(
                id = 7,
                question = "A driver is approaching a pedestrian who is crossing at a proper crosswalk. What should the driver do?",
                options = listOf("Yield and allow the pedestrian to cross safely", "Accelerate before the pedestrian reaches the lane", "Sound the horn and continue"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa taong tumatawid sa tamang pedestrian crosswalk. Ano ang dapat gawin?",
                optionsFil = listOf("Magbigay-daan at hayaang makatawid nang ligtas ang pedestrian", "Bumilis bago makarating ang tao sa lane", "Bumusina at magpatuloy")
            ),
            QuizQuestion(
                id = 8,
                question = "A driver approaches a railroad crossing where a train is approaching. What should the driver do?",
                options = listOf("Stop and wait until it is safe", "Cross quickly before the train arrives", "Overtake vehicles waiting at the crossing"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa tawiran ng tren kung saan may paparating na tren. Ano ang dapat gawin?",
                optionsFil = listOf("Huminto at maghintay hanggang sa maging ligtas", "Mabilis na tumawid bago dumating ang tren", "I-overtake ang mga nakahintong sasakyan")
            ),
            QuizQuestion(
                id = 9,
                question = "Why should a driver avoid entering an intersection when traffic is backed up on the other side?",
                options = listOf("It may block the intersection", "It saves fuel", "It increases traffic flow"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ng driver ang pagpasok sa interseksyon kung barado ang kabilang bahagi ng kalsada?",
                optionsFil = listOf("Maaari nitong maharangan ang interseksyon (gridlock)", "Nakakatipid ito ng gasolina", "Nagpapabilis ito ng daloy ng trapiko")
            ),
            QuizQuestion(
                id = 10,
                question = "When two roads have no traffic signs controlling the intersection, which factor is important in determining right-of-way?",
                options = listOf("Position and direction of the vehicles", "Color of the vehicles", "Size of the vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Kapag walang traffic signs sa interseksyon, anong salik ang mahalaga sa pagtukoy ng right-of-way?",
                optionsFil = listOf("Posisyon at direksyon ng mga sasakyan", "Kulay ng mga sasakyan", "Laki ng mga sasakyan")
            ),
            QuizQuestion(
                id = 11,
                question = "Before overtaking, a driver should first determine whether there is enough clear distance to complete the maneuver. Why?",
                options = listOf("To prevent collisions with approaching traffic", "To increase engine power", "To reduce tire wear"),
                correctAnswerIndex = 0,
                questionFil = "Bago mag-overtake, bakit dapat tiyakin ng driver na may sapat na malinaw na distansya?",
                optionsFil = listOf("Upang maiwasan ang banggaan sa kasalubong na trapiko", "Upang madagdagan ang lakas ng makina", "Upang mabawasan ang pudpod ng gulong")
            ),
            QuizQuestion(
                id = 12,
                question = "A vehicle ahead is slowing down near a pedestrian crossing. What should you do?",
                options = listOf("Overtake immediately", "Slow down and determine why the vehicle stopped", "Drive onto the shoulder"),
                correctAnswerIndex = 1,
                questionFil = "Bumabagal ang sasakyan sa unahan malapit sa tawiran ng tao. Ano ang dapat mong gawin?",
                optionsFil = listOf("Mag-overtake agad", "Magbagal at alamin kung bakit huminto ang sasakyan", "Magmaneho sa shoulder")
            ),
            QuizQuestion(
                id = 13,
                question = "Why is overtaking near a blind curve dangerous?",
                options = listOf("The driver may not see approaching vehicles", "The road is always wider", "The vehicle uses less fuel"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang pag-overtake malapit sa isang blind curve (kurbadang walang paningin)?",
                optionsFil = listOf("Maaaring hindi makita ng driver ang mga paparating na kasalubong", "Dahil palaging mas malapad ang kalsada roon", "Dahil mas kaunti ang nagagamit na gasolina")
            ),
            QuizQuestion(
                id = 14,
                question = "A driver begins overtaking another vehicle, but an approaching vehicle suddenly becomes visible. What should the driver do?",
                options = listOf("Continue because the maneuver has started", "Safely return to the proper lane when possible", "Drive onto the sidewalk"),
                correctAnswerIndex = 1,
                questionFil = "Nagsimulang mag-overtake ang driver ngunit biglang may nakitang kasalubong. Ano ang dapat gawin?",
                optionsFil = listOf("Ituloy dahil nasimulan na ang maniobra", "Ligtas na bumalik sa tamang lane kung posible", "Umakyat sa bangketa")
            ),
            QuizQuestion(
                id = 15,
                question = "When another vehicle is overtaking you, what should you avoid doing?",
                options = listOf("Increasing your speed", "Maintaining control", "Staying in your lane"),
                correctAnswerIndex = 0,
                questionFil = "Kapag may ibang sasakyang nag-o-overtake sa iyo, ano ang dapat mong IWASANG gawin?",
                optionsFil = listOf("Ang pagpapabilis ng iyong takbo", "Ang pagpapanatili ng kontrol sa sasakyan", "Ang pananatili sa iyong linya")
            ),
            QuizQuestion(
                id = 16,
                question = "What should a driver do before moving into another lane?",
                options = listOf("Check mirrors, blind spots, and signal", "Turn suddenly", "Accelerate without checking"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver bago lumipat sa ibang linya (lane)?",
                optionsFil = listOf("Suriin ang mga salamin, blind spot, at mag-signal", "Biglang lumiko", "Bumilis nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 17,
                question = "Why should drivers avoid unnecessary lane changes?",
                options = listOf("They can increase the risk of conflicts with other vehicles", "They always reduce fuel use", "They increase road width"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ng mga driver ang hindi kinakailangang palipat-lipat ng linya (swerving)?",
                optionsFil = listOf("Maaari nitong dagdagan ang panganib ng banggaan sa iba", "Palagi nitong binabawasan ang konsumo sa gasolina", "Pinapalapad nito ang kalsada")
            ),
            QuizQuestion(
                id = 18,
                question = "A driver sees a solid line that indicates crossing is restricted. What should the driver do?",
                options = listOf("Cross whenever traffic is light", "Follow the restriction indicated by the road marking", "Cross if the vehicle is powerful"),
                correctAnswerIndex = 1,
                questionFil = "Nakakita ang driver ng solid line na nagbabawal sa pagtawid ng linya. Ano ang dapat gawin?",
                optionsFil = listOf("Tumawid kung maluwag ang trapiko", "Sundin ang pagbabawal na ipinapahiwatig ng marka", "Tumawid kung malakas ang makina")
            ),
            QuizQuestion(
                id = 19,
                question = "When is overtaking generally safer?",
                options = listOf("When visibility and road conditions allow the maneuver to be completed safely", "At the crest of a hill", "At a blind intersection"),
                correctAnswerIndex = 0,
                questionFil = "Kailan karaniwang mas ligtas ang pag-overtake?",
                optionsFil = listOf("Kapag ang visibility at kondisyon ng kalsada ay nagpapahintulot nang ligtas", "Sa tuktok ng burol", "Sa blind intersection")
            ),
            QuizQuestion(
                id = 20,
                question = "Why should a driver return to the proper lane after overtaking?",
                options = listOf("To maintain orderly traffic flow", "To prevent other vehicles from passing", "To increase speed"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat bumalik sa tamang linya ang driver matapos mag-overtake?",
                optionsFil = listOf("Upang mapanatili ang maayos na daloy ng trapiko", "Upang pigilan ang ibang sasakyan na dumaan", "Upang lalong bumilis")
            ),
            QuizQuestion(
                id = 21,
                question = "A driver is traveling within the posted speed limit but encounters heavy rain. What should the driver do?",
                options = listOf("Maintain the maximum speed", "Reduce speed according to conditions", "Increase speed to reach the destination sooner"),
                correctAnswerIndex = 1,
                questionFil = "Nasa loob ng speed limit ang driver ngunit inabutan ng malakas na ulan. Ano ang dapat gawin?",
                optionsFil = listOf("Panatilihin ang pinakamataas na bilis", "Bawasan ang bilis ayon sa kondisyon", "Bilisan upang makarating agad")
            ),
            QuizQuestion(
                id = 22,
                question = "Why is the posted speed limit not always a speed that must be maintained?",
                options = listOf("Drivers must consider actual road and traffic conditions", "Drivers should always drive below 10 km/h", "Speed limits apply only at night"),
                correctAnswerIndex = 0,
                questionFil = "Bakit ang nakapaskil na speed limit ay hindi palaging bilis na dapat panatilihin?",
                optionsFil = listOf("Dapat isaalang-alang ng driver ang aktwal na kondisyon ng daan at trapiko", "Dapat palaging magpatakbo nang mas mababa sa 10 km/h", "Sa gabi lamang umiiral ang speed limit")
            ),
            QuizQuestion(
                id = 23,
                question = "A driver approaches a crowded street with many pedestrians. What is the safest action?",
                options = listOf("Reduce speed and remain alert", "Maintain high speed", "Overtake every vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa mataong kalye na maraming naglalakad. Ano ang pinakaligtas na aksyon?",
                optionsFil = listOf("Bawasan ang bilis at manatiling alerto", "Panatilihin ang matulin na takbo", "I-overtake ang lahat ng sasakyan")
            ),
            QuizQuestion(
                id = 24,
                question = "Why should speed be reduced on a wet road?",
                options = listOf("Wet surfaces can reduce tire grip", "Wet roads increase tire grip", "Vehicles cannot use brakes on dry roads"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat magbawas ng bilis kapag basa ang kalsada?",
                optionsFil = listOf("Ang basang kalsada ay nagpapababa ng kapit ng gulong (traction)", "Ang basang daan ay nagpapadagdag ng kapit ng gulong", "Hindi gumagana ang preno sa tuyong kalsada")
            ),
            QuizQuestion(
                id = 25,
                question = "A driver is approaching a sharp curve. What should be done before entering the curve?",
                options = listOf("Adjust speed to a safe level", "Accelerate sharply", "Overtake another vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa matalas na kurbada. Ano ang dapat gawin bago pumasok sa kurbada?",
                optionsFil = listOf("Iangkop at ibaba ang bilis sa ligtas na antas", "Biglang bumilis", "Mag-overtake sa ibang sasakyan")
            ),
            QuizQuestion(
                id = 26,
                question = "What is a major danger of driving too fast for road conditions?",
                options = listOf("Loss of vehicle control", "Improved stopping ability", "Better visibility"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing panganib ng pagpapatakbo nang napakabilis para sa kondisyon ng kalsada?",
                optionsFil = listOf("Pagkawala ng kontrol sa sasakyan", "Mas mabilis na paghinto", "Mas magandang paningin")
            ),
            QuizQuestion(
                id = 27,
                question = "Why should following distance be increased when driving at higher speeds?",
                options = listOf("More distance is needed to react and stop", "It makes the vehicle faster", "It prevents fuel consumption"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat lakihan ang distansya sa sinusundan kapag nagpapatakbo nang mabilis?",
                optionsFil = listOf("Mas malaking distansya ang kailangan para mag-react at huminto", "Pinapabilis nito ang sasakyan", "Pinipigilan nito ang pagkonsumo ng gasolina")
            ),
            QuizQuestion(
                id = 28,
                question = "A driver is approaching a school area during dismissal time. What is appropriate?",
                options = listOf("Reduce speed and watch for children", "Increase speed", "Overtake stopped vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa paaralan sa oras ng uwian ng mga estudyante. Ano ang angkop na gawin?",
                optionsFil = listOf("Bawasan ang bilis at magbantay sa mga bata", "Bilisan ang takbo", "I-overtake ang mga nakahintong sasakyan")
            ),
            QuizQuestion(
                id = 29,
                question = "A driver notices fog reducing visibility. What should the driver do?",
                options = listOf("Reduce speed and increase caution", "Increase speed", "Turn off all lights"),
                correctAnswerIndex = 0,
                questionFil = "Napansin ng driver na may makapal na hamog (fog) na nagpapalabo sa paningin. Ano ang dapat gawin?",
                optionsFil = listOf("Bawasan ang bilis at dagdagan ang pag-iingat", "Bilisan ang takbo", "Patayin ang lahat ng ilaw")
            ),
            QuizQuestion(
                id = 30,
                question = "Why should a driver adjust speed when road conditions change?",
                options = listOf("Safe speed depends on more than the posted limit", "Speed limits become irrelevant", "Vehicles automatically become safer"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iangkop ng driver ang bilis kapag nagbago ang kondisyon ng daan?",
                optionsFil = listOf("Ang ligtas na bilis ay nakadepende sa higit pa sa nakapaskil na limit", "Nawawalan ng saysay ang speed limit", "Kusang nagiging ligtas ang sasakyan")
            ),
            QuizQuestion(
                id = 31,
                question = "A driver wants to park near an intersection. What should the driver consider?",
                options = listOf("Whether the location is legally permitted and does not obstruct traffic", "Whether the vehicle looks attractive there", "Whether other drivers are parked illegally"),
                correctAnswerIndex = 0,
                questionFil = "Nais pumarada ng driver malapit sa interseksyon. Ano ang dapat niyang isaalang-alang?",
                optionsFil = listOf("Kung legal na pinapayagan at hindi makahahadlang sa trapiko", "Kung maganda tingnan ang sasakyan doon", "Kung may ibang ilegal na nakaparada")
            ),
            QuizQuestion(
                id = 32,
                question = "Why is parking on a pedestrian crossing dangerous?",
                options = listOf("It can obstruct pedestrians and reduce visibility", "It makes pedestrians walk faster", "It improves traffic flow"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang pagparada sa pedestrian crossing?",
                optionsFil = listOf("Maaari nitong harangan ang mga pedestrian at bawasan ang visibility", "Pinapabilis nito ang paglakad ng tao", "Pinapaganda nito ang daloy ng trapiko")
            ),
            QuizQuestion(
                id = 33,
                question = "A driver parks on a road at night where visibility is poor. What should be considered?",
                options = listOf("Proper lighting, visibility, and legal parking requirements", "Vehicle color only", "Radio volume"),
                correctAnswerIndex = 0,
                questionFil = "Pumarada ang driver sa kalsada sa gabi kung saan madilim. Ano ang dapat isaalang-alang?",
                optionsFil = listOf("Wastong ilaw, visibility, at mga legal na kinakailangan sa paradahan", "Kulay lamang ng sasakyan", "Lakas ng radyo")
            ),
            QuizQuestion(
                id = 34,
                question = "Why should a driver avoid parking in front of a driveway?",
                options = listOf("It can prevent vehicles from entering or leaving", "It improves driveway access", "It makes parking safer"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ng driver ang pagparada sa harap ng driveway?",
                optionsFil = listOf("Maaari nitong pigilan ang mga sasakyan sa pagpasok o paglabas", "Pinapadali nito ang paglabas", "Ginagawa nitong ligtas ang paradahan")
            ),
            QuizQuestion(
                id = 35,
                question = "What should a driver do when stopping temporarily in traffic?",
                options = listOf("Avoid blocking intersections, crossings, and other restricted areas", "Stop wherever convenient", "Stop on the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag pansamantalang humihinto sa trapiko?",
                optionsFil = listOf("Iwasang harangan ang mga interseksyon, tawiran, at bawal na lugar", "Huminto kahit saan maginhawa", "Huminto sa bangketa")
            ),
            QuizQuestion(
                id = 36,
                question = "Before leaving a parked vehicle unattended, what should the driver do?",
                options = listOf("Secure it against unintended movement", "Leave it in neutral without the hand brake", "Leave the engine running"),
                correctAnswerIndex = 0,
                questionFil = "Bago iwanang nakaparada ang sasakyan nang walang bantay, ano ang dapat gawin?",
                optionsFil = listOf("I-secure ito laban sa hindi sinasadyang paggulong (handbrake)", "Iwanang naka-neutral nang walang handbrake", "Iwanang umaandar ang makina")
            ),
            QuizQuestion(
                id = 37,
                question = "Why should drivers observe parking signs?",
                options = listOf("They indicate restrictions or conditions for parking", "They are only suggestions", "They apply only to motorcycles"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat sundin ng mga driver ang mga karatula sa paradahan?",
                optionsFil = listOf("Ipinapahiwatig ng mga ito ang mga paghihigpit at kondisyon sa pagparada", "Mungkahi lamang ang mga ito", "Para lamang sa motorsiklo ang mga ito")
            ),
            QuizQuestion(
                id = 38,
                question = "A driver finds a convenient parking space but it blocks a fire hydrant. What should the driver do?",
                options = listOf("Find another legal parking location", "Park there briefly", "Park there if hazard lights are on"),
                correctAnswerIndex = 0,
                questionFil = "Nakakita ng bakanteng paradahan ang driver ngunit nakaharang ito sa fire hydrant. Ano ang dapat gawin?",
                optionsFil = listOf("Humanap ng ibang legal na lokasyon ng paradahan", "Pumarada sandali", "Pumarada roon kung nakabukas ang hazard lights")
            ),
            QuizQuestion(
                id = 39,
                question = "Why is double parking dangerous?",
                options = listOf("It can obstruct traffic and reduce road space", "It increases road capacity", "It improves traffic flow"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang double parking?",
                optionsFil = listOf("Maaari nitong harangan ang daloy at bawasan ang espasyo ng daan", "Nagpapalawak ito ng kapasidad ng kalsada", "Nagpapaganda ito ng trapiko")
            ),
            QuizQuestion(
                id = 40,
                question = "A driver stops on a road because of a mechanical problem. What should the driver do when possible?",
                options = listOf("Move to a safe location and avoid obstructing traffic", "Leave the vehicle in the middle of the lane", "Continue driving at high speed"),
                correctAnswerIndex = 0,
                questionFil = "Huminto ang sasakyan dahil sa mekanikal na problema. Ano ang dapat gawin kapag posible?",
                optionsFil = listOf("Pumunta sa ligtas na lugar (shoulder) at iwasang humarang sa trapiko", "Iwanan ang sasakyan sa gitna ng linya", "Magpatuloy sa mabilis na takbo")
            ),
            QuizQuestion(
                id = 41,
                question = "What is defensive driving mainly about?",
                options = listOf("Anticipating hazards and taking preventive action", "Driving faster than others", "Avoiding all traffic signals"),
                correctAnswerIndex = 0,
                questionFil = "Tungkol saan ang pangunahing konsepto ng defensive driving?",
                optionsFil = listOf("Pag-asa sa posibleng panganib at pagsasagawa ng maagang pag-iingat", "Pagpapatakbo nang mas mabilis kaysa sa iba", "Pag-iwas sa lahat ng traffic signals")
            ),
            QuizQuestion(
                id = 42,
                question = "A vehicle ahead suddenly brakes. What should a defensive driver have done beforehand?",
                options = listOf("Maintained a safe following distance", "Followed very closely", "Overtaken without checking"),
                correctAnswerIndex = 0,
                questionFil = "Biglang nagpreno ang sasakyan sa unahan. Ano ang dapat ginawa ng defensive driver bago pa man ito nangyari?",
                optionsFil = listOf("Nagpanatili ng ligtas na agwat sa pagsunod (following distance)", "Sumunod nang napakalapit", "Nag-overtake nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 43,
                question = "Why should drivers regularly check their mirrors?",
                options = listOf("To remain aware of surrounding traffic", "To make the vehicle look good", "To increase engine power"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat regular na sumulyap ang driver sa kanyang mga salamin (mirrors)?",
                optionsFil = listOf("Upang manatiling alerto sa paligid na trapiko", "Upang magmukhang maganda ang sasakyan", "Upang lumakas ang makina")
            ),
            QuizQuestion(
                id = 44,
                question = "A driver notices a motorcycle in the blind spot. What should the driver do?",
                options = listOf("Wait until the motorcycle is safely clear before changing lanes", "Change lanes immediately", "Accelerate into the motorcycle's path"),
                correctAnswerIndex = 0,
                questionFil = "Napansin ng driver ang isang motorsiklo sa blind spot. Ano ang dapat gawin?",
                optionsFil = listOf("Maghintay hanggang sa makalagpas nang ligtas ang motorsiklo bago lumipat", "Lumipat agad ng linya", "Bumilis patungo sa daan ng motorsiklo")
            ),
            QuizQuestion(
                id = 45,
                question = "Why is maintaining a safe following distance important?",
                options = listOf("It provides time to react to sudden situations", "It guarantees no accidents", "It allows faster driving"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang pagpapanatili ng ligtas na following distance?",
                optionsFil = listOf("Nagbibigay ito ng oras upang mag-react sa mga biglaang sitwasyon", "Ginagarantiya nitong walang aksidente kailanman", "Nagpapahintulot ito ng mas mabilis na takbo")
            ),
            QuizQuestion(
                id = 46,
                question = "A driver sees a ball roll into the street near children. What should the driver expect?",
                options = listOf("A child may follow the ball into the road", "Nothing will happen", "Traffic will automatically stop"),
                correctAnswerIndex = 0,
                questionFil = "Nakakita ang driver ng bolang gumugulong sa kalsada malapit sa mga bata. Ano ang dapat asahan?",
                optionsFil = listOf("Maaaring may batang sumunod sa bola sa kalsada", "Walang mangyayari", "Kusang hihinto ang trapiko")
            ),
            QuizQuestion(
                id = 47,
                question = "What should a driver do when approaching a parked vehicle with a person nearby?",
                options = listOf("Slow down and be prepared for sudden movement", "Speed up", "Drive as close as possible"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag papalapit sa nakaparadang sasakyan na may taong nakatayo malapit dito?",
                optionsFil = listOf("Magbagal at maghanda sa biglaang paggalaw o pagbukas ng pinto", "Bilisan ang takbo", "Magmaneho nang pinakamalapit sa kanila")
            ),
            QuizQuestion(
                id = 48,
                question = "Why should drivers avoid distractions while driving?",
                options = listOf("Distractions reduce attention to the road", "Distractions improve reaction time", "Distractions improve visibility"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ng mga driver ang mga distraksyon habang nagmamaneho?",
                optionsFil = listOf("Binabawasan ng distraksyon ang atensyon sa kalsada", "Pinapabilis ng distraksyon ang reaction time", "Pinapalinaw nito ang paningin")
            ),
            QuizQuestion(
                id = 49,
                question = "What should a driver do if another driver behaves aggressively?",
                options = listOf("Avoid confrontation and maintain safe driving", "Challenge the driver", "Follow the vehicle closely"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag may ibang driver na agresibo ang pagmamaneho (road rage)?",
                optionsFil = listOf("Iwasan ang komprontasyon at panatilihin ang ligtas na pagmamaneho", "Hamunin ang driver", "Buntutan nang malapitan ang sasakyan")
            ),
            QuizQuestion(
                id = 50,
                question = "What is one characteristic of a defensive driver?",
                options = listOf("Anticipates possible hazards", "Drives aggressively", "Ignores other road users"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang isang pangunahing katangian ng isang defensive driver?",
                optionsFil = listOf("Inaasahan at pinaghahandaan ang mga posibleng panganib", "Agresibong magpatakbo", "Hindi pinapansin ang ibang gumagamit ng daan")
            ),
            QuizQuestion(
                id = 51,
                question = "What is the main purpose of a warning road sign?",
                options = listOf("To alert drivers to possible hazards", "To allow parking", "To increase the speed limit"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pangunahing layunin ng isang warning road sign (babala)?",
                optionsFil = listOf("Babalaan ang mga driver sa mga posibleng panganib sa unahan", "Magpahintulot ng paradahan", "Magpataas ng speed limit")
            ),
            QuizQuestion(
                id = 52,
                question = "What should a driver do after seeing a warning sign for a sharp curve?",
                options = listOf("Adjust speed and prepare for the curve", "Accelerate", "Overtake immediately"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin matapos makakita ng warning sign para sa matalas na kurbada?",
                optionsFil = listOf("Iangkop ang bilis at maghanda sa kurbada", "Bumilis lalo", "Mag-overtake agad")
            ),
            QuizQuestion(
                id = 53,
                question = "What does a regulatory sign generally tell road users?",
                options = listOf("Rules or restrictions that must be followed", "Tourist information only", "Weather conditions"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang karaniwang ipinapabatid ng isang regulatory sign sa mga gumagamit ng daan?",
                optionsFil = listOf("Mga patakaran o pagbabawal na dapat sundin ayon sa batas", "Impormasyong panturista lamang", "Lagay ng panahon")
            ),
            QuizQuestion(
                id = 54,
                question = "What is the purpose of a pedestrian crossing marking?",
                options = listOf("To identify a designated crossing area", "To provide vehicle parking", "To mark a racing lane"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang layunin ng marka ng pedestrian crossing (zebra lines)?",
                optionsFil = listOf("Tukuyin ang itinalagang tawiran ng mga tao", "Magbigay ng paradahan ng sasakyan", "Markahan ang karerahan")
            ),
            QuizQuestion(
                id = 55,
                question = "Why should road markings be followed even when there are no traffic officers present?",
                options = listOf("They are part of traffic control", "They are optional decorations", "They apply only to buses"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat sundin ang mga marka sa kalsada kahit walang nakatayong traffic officer?",
                optionsFil = listOf("Ang mga ito ay opisyal na bahagi ng regulasyon sa trapiko", "Dekorasyon lamang ang mga ito", "Para lamang sa mga bus ang mga ito")
            ),
            QuizQuestion(
                id = 56,
                question = "A driver sees a no-entry sign. What should the driver do?",
                options = listOf("Do not enter that road or lane", "Enter if there is no traffic", "Enter at low speed"),
                correctAnswerIndex = 0,
                questionFil = "Nakakita ang driver ng 'NO ENTRY' sign. Ano ang dapat gawin?",
                optionsFil = listOf("Huwag pumasok sa kalsada o linyang iyon", "Pumasok kung walang kasalubong", "Pumasok sa mabagal na bilis")
            ),
            QuizQuestion(
                id = 57,
                question = "A driver sees a speed-limit sign. What does it indicate?",
                options = listOf("The maximum permitted speed under the applicable conditions", "The minimum speed at all times", "The recommended vehicle color"),
                correctAnswerIndex = 0,
                questionFil = "Nakakita ang driver ng speed-limit sign. Ano ang ipinapahiwatig nito?",
                optionsFil = listOf("Ang pinakamataas na pinapahintulutang bilis sa ilalim ng angkop na kondisyon", "Ang pinakamababang bilis sa lahat ng oras", "Inirerekumendang kulay ng sasakyan")
            ),
            QuizQuestion(
                id = 58,
                question = "Why are signs placed before hazards?",
                options = listOf("To give drivers time to react appropriately", "To make roads longer", "To increase vehicle speed"),
                correctAnswerIndex = 0,
                questionFil = "Bakit inilalagay ang mga road sign bago marating ang aktwal na panganib?",
                optionsFil = listOf("Upang bigyan ng sapat na oras ang mga driver na makapag-react nang maayos", "Upang pahabain ang mga kalsada", "Upang pabilisin ang mga sasakyan")
            ),
            QuizQuestion(
                id = 59,
                question = "What should a driver do when a road sign is partially blocked but still recognizable?",
                options = listOf("Slow down and interpret it carefully", "Ignore it completely", "Speed up"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag bahagyang nahaharangan ang road sign ngunit nakikilala pa rin?",
                optionsFil = listOf("Magbagal at unawain ito nang maingat", "Balewalain ito nang tuluyan", "Bilisan ang takbo")
            ),
            QuizQuestion(
                id = 60,
                question = "Why should drivers learn common road signs?",
                options = listOf("To understand instructions and hazards on the road", "To avoid using mirrors", "To increase engine performance"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalagang matutuhan ng mga driver ang mga karaniwang traffic sign?",
                optionsFil = listOf("Upang maunawaan ang mga tagubilin at panganib sa kalsada", "Upang maiwasan ang paggamit ng salamin", "Upang mapahusay ang lakas ng makina")
            ),
            QuizQuestion(
                id = 61,
                question = "Why is a driver's license important?",
                options = listOf("It authorizes a qualified person to operate a motor vehicle", "It allows unlimited speeding", "It permits ignoring traffic laws"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang lisensya sa pagmamaneho (driver's license)?",
                optionsFil = listOf("Nagbibigay ito ng legal na awtoridad sa kwalipikadong tao na magmaneho ng sasakyan", "Pinapayagan nito ang walang limitasyong pagpapatakbo", "Pinapahintulutan nitong balewalain ang mga batas")
            ),
            QuizQuestion(
                id = 62,
                question = "Should a driver lend a driver's license to another person?",
                options = listOf("No", "Yes", "Only to a family member"),
                correctAnswerIndex = 0,
                questionFil = "Dapat bang ipahiram ng driver ang kanyang lisensya sa ibang tao?",
                optionsFil = listOf("Hindi", "Oo", "Sa kapamilya lamang")
            ),
            QuizQuestion(
                id = 63,
                question = "Why should drivers carry the required driving credentials when operating a vehicle?",
                options = listOf("To comply with applicable licensing requirements", "To increase vehicle speed", "To avoid vehicle maintenance"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat dalhin ng driver ang orihinal na lisensya at rehistro (OR/CR) habang nagmamaneho?",
                optionsFil = listOf("Upang sumunod sa mga legal na kinakailangan sa pagmamaneho", "Upang mapabilis ang takbo ng sasakyan", "Upang maiwasan ang maintenance")
            ),
            QuizQuestion(
                id = 64,
                question = "What should a driver do if a license has expired?",
                options = listOf("Renew it before continuing to drive as required by law", "Continue using it indefinitely", "Give it to another driver"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nag-expire na ang kanyang lisensya?",
                optionsFil = listOf("I-renew ito sa LTO bago muling magmaneho ayon sa batas", "Patuloy na gamitin ito nang walang takda", "Ibigay ito sa ibang driver")
            ),
            QuizQuestion(
                id = 65,
                question = "What is one responsibility of a licensed driver?",
                options = listOf("Follow traffic laws", "Ignore road signs", "Drive regardless of condition"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang isa sa mga pangunahing tungkulin ng isang lisensyadong driver?",
                optionsFil = listOf("Sundin ang lahat ng batas at patakaran sa trapiko", "Huwag pansinin ang mga road sign", "Magmaneho kahit anong kondisyon ng sarili")
            ),
            QuizQuestion(
                id = 66,
                question = "Why should drivers know the restrictions or conditions attached to their license?",
                options = listOf("To operate only within their legal authorization", "To increase fuel economy", "To avoid using turn signals"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat alamin ng driver ang mga restriction code / driver license codes sa kanyang lisensya?",
                optionsFil = listOf("Upang magmaneho lamang sa loob ng pinapahintulutang kategorya ng sasakyan", "Upang makatipid sa gasolina", "Upang maiwasan ang turn signal")
            ),
            QuizQuestion(
                id = 67,
                question = "What should a driver do if stopped by an authorized traffic enforcer?",
                options = listOf("Follow lawful instructions and provide required documents", "Drive away immediately", "Argue before stopping"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag pinahinto ng awtorisadong traffic enforcer?",
                optionsFil = listOf("Sundin ang legal na tagubilin at ipakita ang mga kinakailangang dokumento", "Agad na tumakas", "Makipagtalo bago huminto")
            ),
            QuizQuestion(
                id = 68,
                question = "Why is proper driver training important?",
                options = listOf("It develops knowledge and safe driving skills", "It guarantees immunity from traffic violations", "It allows unlimited speed"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalaga ang maayos na driver education at training bago magmaneho?",
                optionsFil = listOf("Bumubuo ito ng sapat na kaalaman at kasanayan sa ligtas na pagmamaneho", "Nagbibigay ito ng immunity sa mga paglabag", "Nagpapahintulot ito ng walang limitasyong bilis")
            ),
            QuizQuestion(
                id = 69,
                question = "Should a driver operate a vehicle without being legally authorized to drive it?",
                options = listOf("No", "Yes", "Only on quiet roads"),
                correctAnswerIndex = 0,
                questionFil = "Dapat bang magmaneho ang isang tao nang walang legal na lisensya o awtorisasyon?",
                optionsFil = listOf("Hindi", "Oo", "Sa tahimik na kalsada lamang")
            ),
            QuizQuestion(
                id = 70,
                question = "What should a driver do after receiving information about a traffic rule change?",
                options = listOf("Learn and follow the updated applicable rule", "Ignore it", "Follow only old rules"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag may bagong patakaran o ordinansa sa trapiko na ipinatupad?",
                optionsFil = listOf("Alamin at sundin ang na-update na patakaran", "Balewalain ito", "Sundin lamang ang mga lumang patakaran")
            ),
            QuizQuestion(
                id = 71,
                question = "Why is driving under the influence of alcohol dangerous?",
                options = listOf("It can impair judgment and reaction time", "It improves concentration", "It improves braking ability"),
                correctAnswerIndex = 0,
                questionFil = "Bakit lubhang mapanganib ang pagmamaneho nang nakainom ng alak?",
                optionsFil = listOf("Pinapahina nito ang paghuhusga, koordinasyon, at reaction time", "Pinapabuti nito ang konsentrasyon", "Pinapahusay nito ang pagpreno")
            ),
            QuizQuestion(
                id = 72,
                question = "What should a driver do after consuming alcohol?",
                options = listOf("Do not drive while impaired", "Drive slowly regardless of impairment", "Drive only on empty roads"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng isang tao matapos uminom ng alak?",
                optionsFil = listOf("Huwag magmaneho habang nasa ilalim ng impluwensya ng alak", "Magpatakbo nang mabagal kahit nakainom", "Magmaneho lamang sa bakanteng kalsada")
            ),
            QuizQuestion(
                id = 73,
                question = "Why can using a mobile phone while driving be dangerous?",
                options = listOf("It can distract the driver's attention", "It improves awareness", "It improves steering control"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang paggamit ng cellphone habang nagmamaneho?",
                optionsFil = listOf("Inaalis nito ang atensyon ng driver sa kalsada", "Pinapataas nito ang kamalayan", "Pinapahusay nito ang paghawak sa manibela")
            ),
            QuizQuestion(
                id = 74,
                question = "A driver receives an urgent message while driving. What is the safest action?",
                options = listOf("Stop in a safe and legal location before using the phone", "Read it while moving", "Type a reply at a traffic light regardless of conditions"),
                correctAnswerIndex = 0,
                questionFil = "Nakatanggap ang driver ng importanteng mensahe habang nagmamaneho. Ano ang pinakaligtas na aksyon?",
                optionsFil = listOf("Huminto sa ligtas at legal na lugar bago gamitin ang telepono", "Basahin ito habang umaandar", "Mag-type ng reply sa traffic light")
            ),
            QuizQuestion(
                id = 75,
                question = "Why is fatigue dangerous for drivers?",
                options = listOf("It can reduce alertness and reaction ability", "It improves concentration", "It improves vision"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mapanganib ang labis na pagod (fatigue) sa pagmamaneho?",
                optionsFil = listOf("Maaari nitong bawasan ang pagiging alerto at kakayahang mag-react", "Pinapabuti nito ang konsentrasyon", "Pinapalinaw nito ang paningin")
            ),
            QuizQuestion(
                id = 76,
                question = "A driver feels extremely sleepy while driving. What should the driver do?",
                options = listOf("Stop safely and rest", "Increase speed", "Open the window and continue indefinitely"),
                correctAnswerIndex = 0,
                questionFil = "Nakakaramdam ng matinding antok ang driver habang nasa biyahe. Ano ang dapat gawin?",
                optionsFil = listOf("Ligtas na huminto sa rest area o gasolinahan at magpahinga", "Bilisan ang takbo para makarating agad", "Buksan ang bintana at magpatuloy nang walang tigil")
            ),
            QuizQuestion(
                id = 77,
                question = "What is the safest approach to driving after taking a substance that may impair driving ability?",
                options = listOf("Do not drive while impaired", "Drive faster", "Drive only at night"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pinakaligtas na desisyon matapos uminom ng gamot na nagdudulot ng antok o pagkahilo?",
                optionsFil = listOf("Huwag magmaneho habang apektado ng gamot", "Magmaneho nang mas mabilis", "Magmaneho lamang sa gabi")
            ),
            QuizQuestion(
                id = 78,
                question = "Why should eating or handling objects while driving be minimized?",
                options = listOf("It can take attention and control away from driving", "It improves steering", "It increases visibility"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ang pagkain o paghawak ng mga bagay habang nagmamaneho?",
                optionsFil = listOf("Inaalis nito ang atensyon at pisikal na kontrol sa manibela", "Pinapabuti nito ang manibela", "Nagpapadagdag ito ng paningin")
            ),
            QuizQuestion(
                id = 79,
                question = "What should a driver do if passengers are distracting them?",
                options = listOf("Focus on driving and address the distraction safely", "Ignore road conditions", "Drive faster"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kung ang mga pasahero ay labis na nag-iingay o nakakadisturbo sa pagmamaneho?",
                optionsFil = listOf("Manatiling nakatutok sa kalsada at ligtas na pagsabihan ang mga pasahero", "Balewalain ang kondisyon ng daan", "Magmaneho nang mas mabilis")
            ),
            QuizQuestion(
                id = 80,
                question = "What is the best way to avoid distraction from a mobile phone?",
                options = listOf("Keep it out of use while driving", "Hold it below the steering wheel", "Read messages at low speed"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pinakamahusay na paraan upang maiwasan ang distraksyon mula sa cellphone?",
                optionsFil = listOf("Huwag itong gamitin o ilagay sa Do Not Disturb habang nagmamaneho", "Hawakan ito sa ilalim ng manibela", "Magbasa ng mensahe kapag mabagal ang takbo")
            ),
            QuizQuestion(
                id = 81,
                question = "Why should drivers give motorcycles sufficient space?",
                options = listOf("Motorcycles are vulnerable road users", "Motorcycles cannot use roads", "Motorcycles always have priority"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat bigyan ng sapat na espasyo at agwat ang mga motorsiklo sa kalsada?",
                optionsFil = listOf("Ang mga motorsiklo ay vulnerable road users na walang protective cabin", "Hindi pwedeng gumamit ng kalsada ang motorsiklo", "Palaging may prayoridad ang motorsiklo")
            ),
            QuizQuestion(
                id = 82,
                question = "A motorcycle is traveling beside your vehicle. What should you do before changing lanes?",
                options = listOf("Check the motorcycle's position and blind spot", "Change lanes immediately", "Accelerate toward it"),
                correctAnswerIndex = 0,
                questionFil = "May katabing motorsiklo ang iyong sasakyan. Ano ang dapat gawin bago lumipat ng linya?",
                optionsFil = listOf("Tingnan ang posisyon ng motorsiklo at suriin ang blind spot", "Agad na lumipat ng linya", "Bumilis patungo sa kinaroroonan nito")
            ),
            QuizQuestion(
                id = 83,
                question = "Why should drivers be especially careful around bicycles?",
                options = listOf("They provide less physical protection to riders", "They are always faster than cars", "They cannot stop"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat maging lubos na maingat ang mga driver sa paligid ng mga bisikleta?",
                optionsFil = listOf("Mas kaunti ang pisikal na proteksyon ng mga nagbibisikleta", "Palagi silang mas mabilis sa kotse", "Hindi marunong huminto ang bisikleta")
            ),
            QuizQuestion(
                id = 84,
                question = "A cyclist is traveling ahead near the edge of the road. What should a driver do when passing?",
                options = listOf("Give sufficient clearance and pass safely", "Pass extremely close", "Force the cyclist off the road"),
                correctAnswerIndex = 0,
                questionFil = "May siklistang bumibiyahe sa gilid ng kalsada sa unahan. Ano ang dapat gawin kapag lalagpasan ito?",
                optionsFil = listOf("Magbigay ng sapat na espasyo (clearance) at ligtas na lumagpas", "Dumaan nang napakalapit sa kanya", "Puwersahing itabi ang siklista palabas ng daan")
            ),
            QuizQuestion(
                id = 85,
                question = "Why can motorcycles be difficult to notice?",
                options = listOf("Their smaller size can make them less visible", "They always have no lights", "They cannot travel at night"),
                correctAnswerIndex = 0,
                questionFil = "Bakit kung minsan ay mahirap mapansin ang mga motorsiklo sa trapiko?",
                optionsFil = listOf("Ang kanilang maliit na sukat ay nagpapahirap sa kanila na agad makita", "Palagi silang walang ilaw", "Hindi sila bumibiyahe sa gabi")
            ),
            QuizQuestion(
                id = 86,
                question = "What should a driver do when approaching a pedestrian near the roadway?",
                options = listOf("Slow down and be prepared for movement", "Speed up", "Ignore the pedestrian"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kapag papalapit sa taong naglalakad sa gilid ng kalsada?",
                optionsFil = listOf("Magbagal at maging handa sa biglaang paggalaw o pagtawid", "Bilisan ang takbo", "Huwag pansinin ang pedestrian")
            ),
            QuizQuestion(
                id = 87,
                question = "Why should drivers be cautious near children?",
                options = listOf("Children may behave unpredictably near traffic", "Children always know traffic rules", "Children can stop vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat labis na mag-ingat ang mga driver kapag may mga bata sa tabi ng daan?",
                optionsFil = listOf("Maaaring kumilos ang mga bata nang hindi inaasahan sa trapiko", "Alam na alam ng mga bata ang lahat ng batas-trapiko", "Kayang patigilin ng mga bata ang sasakyan")
            ),
            QuizQuestion(
                id = 88,
                question = "A pedestrian is waiting near a crosswalk. What should a driver do?",
                options = listOf("Reduce speed and be prepared to yield as required", "Speed up", "Drive around the pedestrian on the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "May naghihintay na pedestrian malapit sa crosswalk. Ano ang dapat gawin ng driver?",
                optionsFil = listOf("Bawasan ang bilis at maghandang magbigay-daan ayon sa batas", "Bilisan ang takbo", "Umakyat sa bangketa upang iwasan ang tao")
            ),
            QuizQuestion(
                id = 89,
                question = "Why should drivers avoid blocking bicycle lanes where designated?",
                options = listOf("It can obstruct vulnerable road users", "It improves bicycle safety", "It increases road capacity"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ng mga driver ang pagharang o pagdaan sa mga itinalagang bicycle lane?",
                optionsFil = listOf("Maaari nitong harangan at ilagay sa panganib ang mga vulnerable road users", "Pinapabuti nito ang kaligtasan ng bisikleta", "Nagpapadagdag ito ng kapasidad ng kalsada")
            ),
            QuizQuestion(
                id = 90,
                question = "What should drivers do when passing vulnerable road users?",
                options = listOf("Exercise extra caution and provide safe space", "Drive as close as possible", "Honk continuously"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag dumaraan sa tabi ng mga vulnerable road users (tao, bike, motor)?",
                optionsFil = listOf("Magsagawa ng ibayong pag-iingat at magbigay ng ligtas na espasyo", "Magpatakbo nang pinakamalapit sa kanila", "Tuloy-tuloy na bumusina nang malakas")
            ),
            QuizQuestion(
                id = 91,
                question = "Why should brakes be checked regularly?",
                options = listOf("They are essential for slowing and stopping the vehicle", "They increase radio volume", "They improve paint quality"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat regular na suriin ang preno (brakes) ng sasakyan?",
                optionsFil = listOf("Mahalaga ang mga ito sa pagbagal at ligtas na paghinto ng sasakyan", "Nagpapalakas ang mga ito ng radyo", "Nagpapaganda ang mga ito ng pintura")
            ),
            QuizQuestion(
                id = 92,
                question = "What can worn tires affect?",
                options = listOf("Traction and vehicle control", "Radio reception", "License validity"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang maaaring maapektuhan ng mga pudpod o kalbong gulong?",
                optionsFil = listOf("Traksyon, kapit sa daan, at kontrol sa sasakyan", "Pagtanggap ng signal ng radyo", "Bisa ng lisensya")
            ),
            QuizQuestion(
                id = 93,
                question = "Why should headlights and signal lights be functioning properly?",
                options = listOf("They help visibility and communication with other road users", "They increase engine horsepower", "They reduce road width"),
                correctAnswerIndex = 0,
                questionFil = "Bakit mahalagang gumagana nang maayos ang lahat ng headlights at signal lights?",
                optionsFil = listOf("Tumutulong ang mga ito sa paningin sa dilim at komunikasyon sa ibang motorista", "Nagpapadagdag ang mga ito ng horsepower ng makina", "Nagpapakitid ang mga ito ng kalsada")
            ),
            QuizQuestion(
                id = 94,
                question = "A tire suddenly loses pressure while driving. What should the driver generally do?",
                options = listOf("Maintain control, slow down gradually, and move to a safe location", "Brake suddenly and turn sharply", "Accelerate"),
                correctAnswerIndex = 0,
                questionFil = "Biglang na-flat o sumabog ang gulong habang nagmamaneho. Ano ang dapat gawin ng driver?",
                optionsFil = listOf("Panatilihin ang kontrol sa manibela, dahan-dahang magbagal, at tumabi sa ligtas na lugar", "Biglang tumapak sa preno at biglang kumabig", "Bumilis lalo")
            ),
            QuizQuestion(
                id = 95,
                question = "What should a driver do if the vehicle begins to skid?",
                options = listOf("Remain calm and avoid sudden movements", "Accelerate sharply", "Turn the steering wheel violently"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver kapag nagsimulang mag-slide o mag-skid ang sasakyan sa madulas na daan?",
                optionsFil = listOf("Manatiling kalmado, iwasan ang biglaang pagpreno o biglang pagkabig", "Biglang apakan ang silinyador", "Mabilis na pihitin nang marahas ang manibela")
            ),
            QuizQuestion(
                id = 96,
                question = "Why should a driver avoid sudden braking when unnecessary?",
                options = listOf("It may cause loss of control or a rear-end collision", "It always improves traffic flow", "It increases tire grip"),
                correctAnswerIndex = 0,
                questionFil = "Bakit dapat iwasan ang biglaang pagpreno kung hindi naman kinakailangan?",
                optionsFil = listOf("Maaari itong magdulot ng pagkawala ng kontrol o mabangga mula sa likod (rear-end collision)", "Palagi nitong pinapabuti ang daloy ng trapiko", "Nagpapadagdag ito ng kapit ng gulong")
            ),
            QuizQuestion(
                id = 97,
                question = "What should a driver do if the engine overheats?",
                options = listOf("Stop safely and address the problem appropriately", "Continue driving at high speed", "Ignore the temperature warning"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin kung mag-overheat ang makina ng sasakyan?",
                optionsFil = listOf("Ligtas na tumabi at huminto, at palamigin ang makina bago suriin", "Magpatuloy sa pagpapatakbo nang napakabilis", "Balewalain ang temperature warning light")
            ),
            QuizQuestion(
                id = 98,
                question = "What should a driver do after being involved in a road crash?",
                options = listOf("Stop and follow applicable legal and safety procedures", "Immediately leave the scene in every situation", "Hide the vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang dapat gawin ng driver matapos masangkot sa isang aksidente sa kalsada?",
                optionsFil = listOf("Huminto at sundin ang mga legal at pangkaligtasang pamamaraan ayon sa batas", "Agad na tumakas sa pinangyarihan sa lahat ng sitwasyon", "Itago ang sasakyan")
            ),
            QuizQuestion(
                id = 99,
                question = "Why should hazard lights not be used as a substitute for proper signaling while driving normally?",
                options = listOf("They can confuse other road users about the vehicle's intentions", "They make the vehicle faster", "They replace all traffic signs"),
                correctAnswerIndex = 0,
                questionFil = "Bakit hindi dapat gamitin ang hazard lights bilang kapalit ng turn signal habang normal na nagmamaneho?",
                optionsFil = listOf("Maaari nitong lituhin ang ibang motorista tungkol sa iyong intensyon", "Pinapabilis nito ang takbo ng sasakyan", "Pinapalitan nito ang lahat ng traffic signs")
            ),
            QuizQuestion(
                id = 100,
                question = "What is the best response when a sudden hazard appears ahead?",
                options = listOf("Stay calm, assess the situation, and take safe corrective action", "Panic and turn sharply without checking", "Accelerate toward the hazard"),
                correctAnswerIndex = 0,
                questionFil = "Ano ang pinakamahusay na tugon kapag may biglaang panganib na lumitaw sa unahan ng daan?",
                optionsFil = listOf("Manatiling kalmado, suriin ang sitwasyon, at gumawa ng ligtas na aksyong pagwawasto", "Mag-panic at biglang kumabig nang hindi tumitingin", "Bumilis patungo sa panganib")
            )
        )
    )

    // ═══════════════════════════════════════════════════════════════════════════
    // 🔴  HARD — 20 Questions
    // ═══════════════════════════════════════════════════════════════════════════
    val quiz_hard = Quiz(
        id = "quiz_hard",
        title = "Hard Quiz – Advanced Situations",
        moduleType = ModuleType.HARD,
        questions = listOf(
            QuizQuestion(
                id = 1,
                question = "Two vehicles reach an intersection at approximately the same time. Vehicle A is on the left and Vehicle B is on the right. Who should yield?",
                options = listOf("Vehicle A", "Vehicle B", "Both vehicles"),
                correctAnswerIndex = 0,
                questionFil = "Dalawang sasakyan ang sabay na dumating sa interseksyon. Ang Sasakyan A ay nasa kaliwa at ang Sasakyan B ay nasa kanan. Sino ang dapat magbigay-daan (yield)?",
                optionsFil = listOf("Sasakyan A", "Sasakyan B", "Parehong sasakyan")
            ),
            QuizQuestion(
                id = 2,
                question = "A vehicle approaches an intersection while another vehicle is already within the intersection. Who generally has the right-of-way?",
                options = listOf("The approaching vehicle", "The vehicle already within the intersection", "The faster vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang sasakyan sa interseksyon habang may isa nang sasakyan sa loob nito. Sino sa pangkalahatan ang may right-of-way?",
                optionsFil = listOf("Ang paparating na sasakyan", "Ang sasakyang nasa loob na ng interseksyon", "Ang mas mabilis na sasakyan")
            ),
            QuizQuestion(
                id = 3,
                question = "A vehicle approaches an intersection and another vehicle is turning left across its line of travel. The turning vehicle has properly signaled. Who should yield?",
                options = listOf("The approaching vehicle", "The turning vehicle", "Both must stop permanently"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang sasakyan sa interseksyon at may isa pang sasakyan na lumiliko pakaliwa sa kanyang daraanan na maayos na nag-signal. Sino ang dapat magbigay-daan?",
                optionsFil = listOf("Ang paparating na sasakyan", "Ang lumilikong sasakyan", "Parehong dapat permanenteng huminto")
            ),
            QuizQuestion(
                id = 4,
                question = "A driver is traveling at an unlawful speed and claims the right-of-way at an intersection. What happens to that right-of-way?",
                options = listOf("It remains automatically valid", "It may be forfeited", "It becomes stronger"),
                correctAnswerIndex = 1,
                questionFil = "Ang isang driver ay nagpapatakbo sa labag-sa-batas na bilis (speeding) at iginigiit ang right-of-way sa interseksyon. Ano ang mangyayari sa kanyang right-of-way?",
                optionsFil = listOf("Kusang mananatiling balido ito", "Maaari itong mawala (forfeited)", "Lalo itong magiging matibay")
            ),
            QuizQuestion(
                id = 5,
                question = "A vehicle enters a highway from a private driveway while another vehicle is approaching on the highway. Who must yield?",
                options = listOf("The vehicle on the highway", "The vehicle entering from the private road", "Both vehicles"),
                correctAnswerIndex = 1,
                questionFil = "Papasok ang sasakyan sa highway mula sa pribadong driveway habang may paparating na sasakyan sa highway. Sino ang dapat magbigay-daan?",
                optionsFil = listOf("Ang sasakyang nasa highway", "Ang sasakyang pumapasok mula sa pribadong daan", "Parehong sasakyan")
            ),
            QuizQuestion(
                id = 6,
                question = "A driver approaches a through highway from a side road. What is the proper action?",
                options = listOf("Enter immediately if the vehicle is faster", "Yield to vehicles approaching on the through highway", "Sound the horn and continue without slowing"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa through highway mula sa isang side road. Ano ang tamang aksyon?",
                optionsFil = listOf("Pumasok agad kung mas mabilis ang sasakyan", "Magbigay-daan sa mga sasakyang papalapit sa through highway", "Bumusina at dumiretso nang hindi nagbabagal")
            ),
            QuizQuestion(
                id = 7,
                question = "A pedestrian is crossing within a crosswalk in a business or residential district, and no traffic officer or signal is controlling the movement. Who generally has the right-of-way?",
                options = listOf("The vehicle", "The pedestrian", "Whoever moves first"),
                correctAnswerIndex = 1,
                questionFil = "Tumatawid ang pedestrian sa loob ng crosswalk sa business o residential district nang walang traffic officer o signal. Sino ang may right-of-way?",
                optionsFil = listOf("Ang sasakyan", "Ang pedestrian", "Kung sino ang unang gumalaw")
            ),
            QuizQuestion(
                id = 8,
                question = "A pedestrian crosses a business/residential highway outside a crosswalk. Under RA 4136, who generally has the right-of-way?",
                options = listOf("The pedestrian", "The vehicle on the highway", "Both equally"),
                correctAnswerIndex = 1,
                questionFil = "Tumawid ang pedestrian sa labas ng crosswalk sa business/residential highway. Sa ilalim ng RA 4136, sino sa pangkalahatan ang may right-of-way?",
                optionsFil = listOf("Ang pedestrian", "Ang sasakyan sa highway", "Pareho silang pantay")
            ),
            QuizQuestion(
                id = 9,
                question = "An ambulance on official business approaches with an audible signal. What should other drivers do?",
                options = listOf("Maintain speed", "Yield the right-of-way", "Follow closely behind it"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang ambulansyang nasa opisyal na tungkulin na may naririnig na sirena. Ano ang dapat gawin ng ibang driver?",
                optionsFil = listOf("Panatilihin ang bilis", "Magbigay-daan sa right-of-way nito", "Buntutan ito nang malapitan")
            ),
            QuizQuestion(
                id = 10,
                question = "A police vehicle approaches with an audible signal. A driver should normally:",
                options = listOf("Move as near as possible to the right and stop clear of the intersection", "Move to the left and accelerate", "Stop in the middle of the intersection"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang sasakyan ng pulis na may naririnig na sirena. Ang driver ay dapat karaniwang:",
                optionsFil = listOf("Tumabi pakanan hangga't maaari at huminto malayo sa interseksyon", "Lumipat pakaliwa at bumilis", "Huminto sa gitna ng interseksyon")
            ),
            QuizQuestion(
                id = 11,
                question = "Under the general rule, a driver overtaking another vehicle should pass:",
                options = listOf("On the left", "On the right", "On either side"),
                correctAnswerIndex = 0,
                questionFil = "Sa ilalim ng pangkalahatang patakaran, ang driver na nag-o-overtake sa ibang sasakyan ay dapat dumaan:",
                optionsFil = listOf("Sa kaliwa", "Sa kanan", "Sa alinmang panig")
            ),
            QuizQuestion(
                id = 12,
                question = "When may passing on the right generally be permitted on a highway within a business or residential district?",
                options = listOf("When there are two or more lanes moving in the same direction", "Whenever the driver is in a hurry", "Only on a one-lane road"),
                correctAnswerIndex = 0,
                questionFil = "Kailan maaaring pahintulutan ang pag-overtake sa kanan sa isang highway sa business o residential district?",
                optionsFil = listOf("Kapag may dalawa o higit pang linyang bumibiyahe sa parehong direksyon", "Kahit kailan nagmamadali ang driver", "Sa isang one-lane road lamang")
            ),
            QuizQuestion(
                id = 13,
                question = "A vehicle is about to be overtaken. The overtaking driver gives a suitable audible signal. What should the slower driver do?",
                options = listOf("Increase speed", "Give way and avoid increasing speed", "Move immediately to the opposite lane"),
                correctAnswerIndex = 1,
                questionFil = "Mao-overtake ang isang sasakyan at nagbigay ng angkop na busina ang nag-o-overtake. Ano ang dapat gawin ng mabagal na driver?",
                optionsFil = listOf("Bilisan ang takbo", "Magbigay-daan at iwasang magpabilis hanggang makalagpas ito", "Agad na lumipat sa kabilang linya")
            ),
            QuizQuestion(
                id = 14,
                question = "Before moving to the left side of the center line to overtake, the driver must ensure that:",
                options = listOf("The road ahead is clearly visible and sufficiently free of oncoming traffic", "The vehicle behind is close enough", "The road has a curve"),
                correctAnswerIndex = 0,
                questionFil = "Bago lumipat sa kaliwang bahagi ng center line upang mag-overtake, dapat tiyakin ng driver na:",
                optionsFil = listOf("Malinaw na tanaw ang daan sa unahan at may sapat na ligtas na distansya sa kasalubong", "Napakalapit ng sasakyan sa likod", "May kurbada ang kalsada")
            ),
            QuizQuestion(
                id = 15,
                question = "A driver approaches the crest of a grade and cannot see far enough ahead. Is overtaking generally allowed?",
                options = listOf("Yes", "No", "Only if the horn is used"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa tuktok ng matarik na burol (crest of a grade) kung saan hindi tanaw ang unahan. Pinapayagan ba ang pag-overtake?",
                optionsFil = listOf("Oo", "Hindi", "Kung bubusina lamang")
            ),
            QuizQuestion(
                id = 16,
                question = "A driver approaches a curve where the view is obstructed within 500 feet. May the driver overtake by crossing the center line?",
                options = listOf("Yes, if the vehicle is powerful", "No, unless a statutory exception applies", "Yes, if the horn is sounded"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa kurbada kung saan may harang sa paningin sa loob ng 500 talampakan. Maaari bang mag-overtake sa center line?",
                optionsFil = listOf("Oo, kung malakas ang makina", "Hindi, maliban kung may statutory exception na umiiral", "Oo, kung bubusina")
            ),
            QuizQuestion(
                id = 17,
                question = "A driver attempts to overtake another vehicle at a railway grade crossing. Under the general rule, this is:",
                options = listOf("Permitted", "Prohibited", "Required"),
                correctAnswerIndex = 1,
                questionFil = "Sumubok mag-overtake ang driver sa isang railway grade crossing. Sa pangkalahatang patakaran, ito ay:",
                optionsFil = listOf("Pinapayagan", "Ipinagbabawal", "Kinakailangan")
            ),
            QuizQuestion(
                id = 18,
                question = "Overtaking at an intersection is generally prohibited unless the intersection is:",
                options = listOf("Controlled by a traffic signal or permitted by a watchman/peace officer", "Located in a residential area", "Empty of pedestrians"),
                correctAnswerIndex = 0,
                questionFil = "Ang pag-overtake sa interseksyon ay ipinagbabawal maliban kung ang interseksyon ay:",
                optionsFil = listOf("Kinokontrol ng traffic signal o pinapayagan ng traffic officer", "Matatagpuan sa residential area", "Walang mga tumatawid na tao")
            ),
            QuizQuestion(
                id = 19,
                question = "Temporary warning signs indicate that workers are performing road work. May a driver overtake another vehicle between the indicated points?",
                options = listOf("Yes", "No", "Only at night"),
                correctAnswerIndex = 1,
                questionFil = "May mga pansamantalang babala na may mga manggagawang nagtatrabaho sa kalsada. Maaari bang mag-overtake sa pagitan ng mga markang ito?",
                optionsFil = listOf("Oo", "Hindi", "Sa gabi lamang")
            ),
            QuizQuestion(
                id = 20,
                question = "A driver is inside an officially marked no-passing zone. What is the correct action?",
                options = listOf("Overtake if the road looks clear", "Do not overtake", "Overtake only motorcycles"),
                correctAnswerIndex = 1,
                questionFil = "Nasa loob ng opisyal na minarkahang no-passing zone ang driver. Ano ang tamang aksyon?",
                optionsFil = listOf("Mag-overtake kung mukhang maluwag ang daan", "Huwag mag-overtake", "I-overtake ang mga motorsiklo lamang")
            ),
            QuizQuestion(
                id = 21,
                question = "Under RA 4136, the maximum speed for cars and motorcycles on city or municipal streets with light traffic, when not designated as through streets, is generally:",
                options = listOf("20 km/h", "30 km/h", "40 km/h"),
                correctAnswerIndex = 1,
                questionFil = "Sa ilalim ng RA 4136, ano ang pangkalahatang speed limit ng mga kotse sa mga lansangan ng lungsod o bayan na may magaan na trapiko?",
                optionsFil = listOf("20 km/h", "30 km/h", "40 km/h")
            ),
            QuizQuestion(
                id = 22,
                question = "A driver is passing through a crowded street or approaching a blind corner. The applicable statutory maximum under the listed circumstances is generally:",
                options = listOf("20 km/h", "40 km/h", "60 km/h"),
                correctAnswerIndex = 0,
                questionFil = "Dumaraan ang driver sa mataong kalye o papalapit sa isang blind corner. Ano ang statutory maximum speed sa ilalim ng RA 4136?",
                optionsFil = listOf("20 km/h", "40 km/h", "60 km/h")
            ),
            QuizQuestion(
                id = 23,
                question = "A driver is traveling on a designated through street or boulevard. For cars and motorcycles, the statutory maximum listed by RA 4136 is generally:",
                options = listOf("30 km/h", "40 km/h", "80 km/h"),
                correctAnswerIndex = 1,
                questionFil = "Bumibiyahe ang driver sa isang itinalagang through street o boulevard. Para sa mga kotse, ano ang statutory maximum ayon sa RA 4136?",
                optionsFil = listOf("30 km/h", "40 km/h", "80 km/h")
            ),
            QuizQuestion(
                id = 24,
                question = "A car is traveling on an open country road without blind corners and not closely bordered by habitations. The statutory maximum for cars/motorcycles is generally:",
                options = listOf("40 km/h", "60 km/h", "80 km/h"),
                correctAnswerIndex = 2,
                questionFil = "Bumibiyahe ang kotse sa bukas na country road na walang blind corners at hindi matao. Ano ang statutory maximum speed?",
                optionsFil = listOf("40 km/h", "60 km/h", "80 km/h")
            ),
            QuizQuestion(
                id = 25,
                question = "A driver is approaching a school zone. Even if the road normally permits a higher speed, the driver should:",
                options = listOf("Maintain the normal maximum", "Observe the lower statutory limit applicable to the dangerous circumstance", "Accelerate to clear the area quickly"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa school zone. Kahit mas mataas ang normal na bilis ng kalsada, ano ang dapat gawin?",
                optionsFil = listOf("Panatilihin ang normal na bilis", "Sundin ang mas mababang statutory limit na 20 km/h para sa mapanganib na lugar", "Bumilis upang makalagpas agad")
            ),
            QuizQuestion(
                id = 26,
                question = "Which situation is specifically associated with the 20 km/h statutory speed limit?",
                options = listOf("Open country road with no blind corners", "Approaching a blind corner", "Light-traffic through street"),
                correctAnswerIndex = 1,
                questionFil = "Aling sitwasyon ang partikular na itinakda sa ilalim ng 20 km/h statutory speed limit?",
                optionsFil = listOf("Bukas na country road na walang kurbada", "Papalapit sa blind corner o interseksyon", "Through street na maluwag ang trapiko")
            ),
            QuizQuestion(
                id = 27,
                question = "A driver is passing a stationary vehicle on a road where the statutory dangerous-circumstance limit applies. What maximum speed is generally specified?",
                options = listOf("20 km/h", "40 km/h", "80 km/h"),
                correctAnswerIndex = 0,
                questionFil = "Lalampas ang driver sa isang nakahintong pampasaherong sasakyan sa mapanganib na lugar. Ano ang statutory maximum speed?",
                optionsFil = listOf("20 km/h", "40 km/h", "80 km/h")
            ),
            QuizQuestion(
                id = 28,
                question = "A driver argues that a local government can freely establish a different maximum speed from the statutory limits in RA 4136. Which statement is correct under Section 36?",
                options = listOf("The Act provides uniform statutory maximum speeds", "Any local authority can replace them without restriction", "Drivers may choose their own maximum"),
                correctAnswerIndex = 0,
                questionFil = "Maaari bang basta magpalit ang lokal na pamahalaan ng speed limits na sumasalungat sa RA 4136 Section 36?",
                optionsFil = listOf("Ang RA 4136 ay nagbibigay ng unipormeng statutory maximum speeds na dapat gabay", "Maaari itong palitan ng sinumang opisyal nang walang limitasyon", "Maaaring pumili ang driver ng sariling bilis")
            ),
            QuizQuestion(
                id = 29,
                question = "Which driver may fall under a statutory exception to the prescribed speed rates?",
                options = listOf("A driver racing friends", "A hospital ambulance responding to an emergency", "A private driver running late"),
                correctAnswerIndex = 1,
                questionFil = "Aling driver ang sakop ng statutory emergency speed exception sa ilalim ng Section 35?",
                optionsFil = listOf("Driver na nakikipagkarera sa kaibigan", "Ambulansya ng ospital na rumeresponde sa emergency", "Pribadong driver na nahuhuli sa trabaho")
            ),
            QuizQuestion(
                id = 30,
                question = "A statutory emergency speed exception means the driver may:",
                options = listOf("Drive recklessly without limitation", "Ignore all traffic rules", "Respond under the circumstances specified by law, without allowing useless or unnecessary fast driving"),
                correctAnswerIndex = 2,
                questionFil = "Ano ang ibig sabihin ng statutory emergency speed exception?",
                optionsFil = listOf("Maaaring magmaneho nang reckless nang walang pananagutan", "Balewalain ang lahat ng patakaran sa trapiko", "Rumesponde ayon sa itinakda ng batas, nang hindi pinahihintulutan ang walang saysay o mapanganib na bilis")
            ),
            QuizQuestion(
                id = 31,
                question = "Before turning from a direct line, a driver must first determine that the movement:",
                options = listOf("Can be made safely", "Will be faster than other traffic", "Requires no signal"),
                correctAnswerIndex = 0,
                questionFil = "Bago lumihis mula sa tuwid na linya (turning), ano ang unang dapat tiyakin ng driver?",
                optionsFil = listOf("Na magagawa ang maniobra nang ligtas", "Na mas mabilis ito kaysa sa ibang trapiko", "Na hindi na kailangan ng signal")
            ),
            QuizQuestion(
                id = 32,
                question = "When starting, stopping, or turning could affect another vehicle, the driver must give:",
                options = listOf("A plainly visible signal", "No signal if traffic is light", "Only a verbal warning"),
                correctAnswerIndex = 0,
                questionFil = "Kapag ang pagsisimula, paghinto, o pagliko ay maaaring makaapekto sa ibang sasakyan, dapat magbigay ang driver ng:",
                optionsFil = listOf("Isang malinaw na nakikitang signal (turn light o hand signal)", "Walang signal kung kakaunti ang sasakyan", "Pasalitang babala lamang")
            ),
            QuizQuestion(
                id = 33,
                question = "A driver intending to turn right at an intersection should generally approach in:",
                options = listOf("The lane nearest the right side of the highway", "The lane nearest the center line", "Any lane"),
                correctAnswerIndex = 0,
                questionFil = "Bago kumanan (right turn) sa interseksyon, saang linya dapat pumuwesto ang driver sa paglapit?",
                optionsFil = listOf("Sa linyang pinakamalapit sa kanang gilid ng highway", "Sa linyang pinakamalapit sa center line", "Sa kahit saang linya")
            ),
            QuizQuestion(
                id = 34,
                question = "When making a right turn, the driver should keep:",
                options = listOf("As close as possible to the right curb or edge", "As close as possible to the center line", "On the opposite side of the road"),
                correctAnswerIndex = 0,
                questionFil = "Habang lumiliko pakanan sa interseksyon, saan dapat manatili ang driver?",
                optionsFil = listOf("Kasing lapit hangga't maaari sa kanang bangketa o gilid ng kalsada", "Kasing lapit hangga't maaari sa center line", "Sa kabilang linya ng kasalubong")
            ),
            QuizQuestion(
                id = 35,
                question = "A driver intending to turn left should generally approach in the lane:",
                options = listOf("To the right of and nearest the center line", "Nearest the right curb", "Intended for parking"),
                correctAnswerIndex = 0,
                questionFil = "Bago lumiko pakaliwa (left turn) sa two-way road, saang linya dapat pumuwesto ang driver?",
                optionsFil = listOf("Sa linyang nasa kanan at pinakamalapit sa center line", "Sa pinakakanang bangketa", "Sa paradahan")
            ),
            QuizQuestion(
                id = 36,
                question = "In making a normal left turn at an intersection, the vehicle should pass:",
                options = listOf("To the left of the center of the intersection", "To the right of the center of the intersection", "Over the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Sa normal na pagliko pakaliwa sa interseksyon, saan dapat dumaan ang sasakyan?",
                optionsFil = listOf("Sa kaliwa ng gitna ng interseksyon (left of center)", "Sa kanan ng gitna ng interseksyon", "Sa ibabaw ng bangketa")
            ),
            QuizQuestion(
                id = 37,
                question = "On a one-way highway, a left turn should generally be made from:",
                options = listOf("The left lane in the direction of travel", "The right shoulder", "Any opposing lane"),
                correctAnswerIndex = 0,
                questionFil = "Sa isang one-way highway, saan dapat isagawa ang pagliko pakaliwa?",
                optionsFil = listOf("Mula sa pinakakaliwang linya sa direksyon ng biyahe", "Mula sa kanang shoulder", "Mula sa kasalubong na linya")
            ),
            QuizQuestion(
                id = 38,
                question = "A driver turns without checking whether the movement is safe. Which requirement has been violated?",
                options = listOf("The duty to ensure the movement can be made safely", "The parking rule", "The vehicle registration rule"),
                correctAnswerIndex = 0,
                questionFil = "Lumiko ang driver nang hindi muna sinusuri kung ligtas ang maniobra. Aling panuntunan ang nilabag?",
                optionsFil = listOf("Ang tungkuling tiyaking ligtas ang paggalaw bago lumiko", "Ang patakaran sa paradahan", "Ang patakaran sa rehistro ng sasakyan")
            ),
            QuizQuestion(
                id = 39,
                question = "A driver’s turn may affect a pedestrian. Under the traffic rules, the driver should also provide:",
                options = listOf("A clearly audible signal when required", "A high-speed maneuver", "No warning"),
                correctAnswerIndex = 0,
                questionFil = "Ang pagliko ng driver ay maaaring makaapekto sa tumatawid na pedestrian. Dapat ding magbigay ang driver ng:",
                optionsFil = listOf("Isang malinaw na maririnig na busina kung kinakailangan", "Isang mabilis na maniobra", "Walang anumang babala")
            ),
            QuizQuestion(
                id = 40,
                question = "Which is the safest interpretation of signaling before a maneuver?",
                options = listOf("Signal only after completing the maneuver", "Signal the intention before making the movement", "Signal only when a police officer is present"),
                correctAnswerIndex = 1,
                questionFil = "Ano ang pinakaligtas na interpretasyon sa pagbibigay ng signal bago magmaniobra?",
                optionsFil = listOf("Mag-signal lamang pagkatapos lumiko", "Ipaalam ang intensyon sa pamamagitan ng signal bago simulan ang paggalaw", "Mag-signal lamang kung may pulis")
            ),
            QuizQuestion(
                id = 41,
                question = "Parking within an intersection is:",
                options = listOf("Allowed for less than one minute", "Prohibited", "Allowed with hazard lights"),
                correctAnswerIndex = 1,
                questionFil = "Ang pagparada sa loob ng isang interseksyon ay:",
                optionsFil = listOf("Pinapayagan nang mas mababa sa isang minuto", "Ipinagbabawal", "Pinapayagan kapag may hazard lights")
            ),
            QuizQuestion(
                id = 42,
                question = "Parking directly on a crosswalk is:",
                options = listOf("Prohibited", "Allowed at night", "Allowed when no pedestrian is present"),
                correctAnswerIndex = 0,
                questionFil = "Ang pagparada mismo sa ibabaw ng pedestrian crosswalk ay:",
                optionsFil = listOf("Ipinagbabawal", "Pinapayagan sa gabi", "Pinapayagan kung walang pedestrian")
            ),
            QuizQuestion(
                id = 43,
                question = "How far from the intersection of curb lines is parking prohibited under Section 46?",
                options = listOf("Within 2 meters", "Within 4 meters", "Within 6 meters"),
                correctAnswerIndex = 2,
                questionFil = "Gaano kalayo mula sa tagpuan ng mga linya ng bangketa (curb lines) sa interseksyon ipinagbabawal ang pagparada sa Section 46?",
                optionsFil = listOf("Sa loob ng 2 metro", "Sa loob ng 4 na metro", "Sa loob ng 6 na metro")
            ),
            QuizQuestion(
                id = 44,
                question = "Parking within four meters of a fire hydrant is:",
                options = listOf("Permitted", "Prohibited", "Required"),
                correctAnswerIndex = 1,
                questionFil = "Ang pagparada sa loob ng apat na metro mula sa isang fire hydrant ay:",
                optionsFil = listOf("Pinapayagan", "Ipinagbabawal", "Kinakailangan")
            ),
            QuizQuestion(
                id = 45,
                question = "Parking within four meters of a fire station driveway is:",
                options = listOf("Prohibited", "Allowed if the engine is running", "Allowed during daytime"),
                correctAnswerIndex = 0,
                questionFil = "Ang pagparada sa loob ng apat na metro mula sa driveway ng fire station ay:",
                optionsFil = listOf("Ipinagbabawal", "Pinapayagan kung umaandar ang makina", "Pinapayagan sa araw")
            ),
            QuizQuestion(
                id = 46,
                question = "A driver parks directly in front of a private driveway. This is:",
                options = listOf("Permitted if hazard lights are on", "Prohibited", "Required during emergencies"),
                correctAnswerIndex = 1,
                questionFil = "Pumarada ang driver sa tapat ng pribadong driveway. Ito ay:",
                optionsFil = listOf("Pinapayagan kung may hazard lights", "Ipinagbabawal", "Kinakailangan sa emergency")
            ),
            QuizQuestion(
                id = 47,
                question = "A driver parks beside another vehicle in a way that leaves the parked vehicle on the roadway side. This is commonly called double parking and is:",
                options = listOf("Prohibited under the specified parking rule", "Required on narrow streets", "Allowed if the driver stays inside"),
                correctAnswerIndex = 0,
                questionFil = "Pumarada ang driver sa tabi ng isa pang nakaparadang sasakyan (double parking). Ito ay:",
                optionsFil = listOf("Ipinagbabawal sa ilalim ng panuntunan sa paradahan", "Kinakailangan sa makikitid na kalye", "Pinapayagan kung nasa loob ang driver")
            ),
            QuizQuestion(
                id = 48,
                question = "An official no-parking sign is posted. The driver should:",
                options = listOf("Park there briefly", "Avoid parking there", "Park only with hazard lights"),
                correctAnswerIndex = 1,
                questionFil = "May nakapaskil na opisyal na 'NO PARKING' sign. Ang driver ay dapat:",
                optionsFil = listOf("Pumarada roon sandali", "Iwasang pumarada roon", "Pumarada lamang gamit ang hazard lights")
            ),
            QuizQuestion(
                id = 49,
                question = "When an unattended vehicle is parked on a highway, the driver must:",
                options = listOf("Leave the engine running", "Turn off the ignition and effectively apply the hand brake", "Leave the transmission in neutral only"),
                correctAnswerIndex = 1,
                questionFil = "Kapag ipinaparada ang sasakyan sa highway nang walang bantay, dapat gawin ng driver ang:",
                optionsFil = listOf("Iwanang umaandar ang makina", "Patayin ang ignition at epektibong ikabit ang handbrake", "Iwanang naka-neutral lamang")
            ),
            QuizQuestion(
                id = 50,
                question = "A driver stops only long enough to quickly pick up a waiting passenger and immediately continues. Under the definition in RA 4136, this is generally:",
                options = listOf("Not considered parking if done without delay", "Always considered parking", "Illegal in all situations"),
                correctAnswerIndex = 0,
                questionFil = "Huminto ang driver nang panandalian lamang upang mabilis na magpasakay ng naghihintay na pasahero at agad umalis. Sa RA 4136, ito ay:",
                optionsFil = listOf("Hindi itinuturing na parking kung ginawa nang walang pagkaantala (loading/unloading)", "Palaging itinuturing na parking", "Ilegal sa lahat ng sitwasyon")
            ),
            QuizQuestion(
                id = 51,
                question = "An ambulance with an audible signal approaches while you are near an intersection. Where should you stop?",
                options = listOf("As near as possible to the right-hand edge, clear of the intersection", "In the center of the intersection", "On the left side"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang ambulansyang may sirena habang ikaw ay malapit sa interseksyon. Saan ka dapat huminto?",
                optionsFil = listOf("Kasing lapit hangga't maaari sa kanang gilid, malayo sa interseksyon", "Sa gitna mismo ng interseksyon", "Sa kaliwang bahagi")
            ),
            QuizQuestion(
                id = 52,
                question = "A fire department vehicle approaches with an audible signal. Other drivers should:",
                options = listOf("Compete for the same lane", "Yield and stop as required", "Follow it closely"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang rumerespondeng bumbero na may sirena. Ang ibang driver ay dapat:",
                optionsFil = listOf("Makipag-agawan sa linya", "Magbigay-daan at huminto ayon sa kinakailangan", "Buntutan ito nang malapitan")
            ),
            QuizQuestion(
                id = 53,
                question = "A police vehicle approaches on official business but gives no audible signal. Does Section 43 automatically impose the same audible-signal emergency right-of-way requirement?",
                options = listOf("Yes, regardless of signal", "No; the provision specifies an audible signal", "Only if it is blue"),
                correctAnswerIndex = 1,
                questionFil = "Bumibiyahe ang police car sa opisyal na tungkulin ngunit WALANG tunog ng sirena. Awtomatiko bang ipinapataw ng Section 43 ang emergency right-of-way?",
                optionsFil = listOf("Oo, kahit walang sirena", "Hindi; ang probisyon ay partikular na nagtatakda ng audible signal (sirena)", "Kung kulay asul lamang ito")
            ),
            QuizQuestion(
                id = 54,
                question = "An emergency vehicle is approaching, but a peace officer gives a different direction. The driver should:",
                options = listOf("Follow the peace officer's direction", "Ignore the officer", "Follow another private vehicle"),
                correctAnswerIndex = 0,
                questionFil = "May paparating na emergency vehicle, ngunit may peace officer na nagbibigay ng ibang direksyon. Ano ang dapat sundin ng driver?",
                optionsFil = listOf("Sundin ang direksyon ng peace officer", "Huwag pansinin ang opisyal", "Sundan ang ibang pribadong sasakyan")
            ),
            QuizQuestion(
                id = 55,
                question = "A driver encounters a road condition requiring slower speed even though the posted/statutory maximum is higher. What should the driver do?",
                options = listOf("Always drive at the maximum", "Adjust speed according to safety and road conditions", "Accelerate"),
                correctAnswerIndex = 1,
                questionFil = "May mapanganib na kondisyon sa daan na nangangailangan ng mabagal na takbo kahit mataas ang speed limit. Ano ang dapat gawin?",
                optionsFil = listOf("Palaging magpatakbo sa maximum speed", "Iangkop ang bilis ayon sa kaligtasan at kondisyon ng kalsada", "Bumilis lalo")
            ),
            QuizQuestion(
                id = 56,
                question = "Which action is most consistent with the reckless-driving prohibition?",
                options = listOf("Adjusting speed for visibility and traffic conditions", "Driving in a manner that endangers people or property", "Maintaining reasonable caution"),
                correctAnswerIndex = 1,
                questionFil = "Aling aksyon ang pinakatumutugma sa pagbabawal sa reckless driving (walang ingat na pagmamaneho)?",
                optionsFil = listOf("Pag-aangkop ng bilis sa trapiko at panahon", "Pagmamaneho sa paraang naglalagay sa panganib sa tao o ari-arian", "Pagpapanatili ng makatwirang pag-iingat")
            ),
            QuizQuestion(
                id = 57,
                question = "A driver deliberately drives aggressively through heavy traffic and creates danger for other road users. This may constitute:",
                options = listOf("Defensive driving", "Reckless driving", "Proper overtaking"),
                correctAnswerIndex = 1,
                questionFil = "Sinadyang magmaneho nang agresibo ng driver sa masikip na trapiko na nagdulot ng peligro sa iba. Ito ay maituturing na:",
                optionsFil = listOf("Defensive driving", "Reckless driving (Section 48)", "Tamang pag-overtake")
            ),
            QuizQuestion(
                id = 58,
                question = "A driver uses a sidewalk as a shortcut to avoid traffic. Under RA 4136, this is:",
                options = listOf("Permitted when traffic is heavy", "Prohibited", "Required during congestion"),
                correctAnswerIndex = 1,
                questionFil = "Ginamit ng driver ang bangketa (sidewalk) bilang shortcut upang makaiwas sa trapiko. Sa RA 4136, ito ay:",
                optionsFil = listOf("Pinapayagan kung masikip ang trapiko", "Ipinagbabawal", "Kinakailangan tuwing rush hour")
            ),
            QuizQuestion(
                id = 59,
                question = "A driver stops in a way that blocks the free passage of other vehicles while loading passengers. This may violate the rule against:",
                options = listOf("Obstruction of traffic", "Overtaking", "Right-side driving"),
                correctAnswerIndex = 0,
                questionFil = "Huminto ang driver sa paraang humaharang sa malayang daanan ng ibang sasakyan habang nagpapasakay. Nilabag nito ang:",
                optionsFil = listOf("Obstruction of traffic (Section 54)", "Overtaking rule", "Right-side driving rule")
            ),
            QuizQuestion(
                id = 60,
                question = "A passenger hangs on the outside rear portion of a moving vehicle. The driver knowingly permits it. This is:",
                options = listOf("Allowed at low speed", "Prohibited", "Allowed in residential areas"),
                correctAnswerIndex = 1,
                questionFil = "May pasaherong nakakapit o nakasabit sa labas na likurang bahagi ng umaandar na sasakyan nang may pahintulot ng driver. Ito ay:",
                optionsFil = listOf("Pinapayagan sa mabagal na bilis", "Ipinagbabawal (Section 51)", "Pinapayagan sa residential areas")
            ),
            QuizQuestion(
                id = 61,
                question = "After a vehicular accident, the driver present should provide:",
                options = listOf("Only the vehicle plate number", "Driver's license, true name, and address, plus the owner's true name and address", "Only an insurance policy"),
                correctAnswerIndex = 1,
                questionFil = "Matapos ang aksidente sa kalsada, ano ang dapat ibigay ng driver na sangkot?",
                optionsFil = listOf("Plate number lamang", "Driver's license, tunay na pangalan at tirahan, at tunay na pangalan at tirahan ng may-ari", "Insurance policy lamang")
            ),
            QuizQuestion(
                id = 62,
                question = "A driver involved in an accident leaves immediately without assisting the victim or meeting a legal exception. This may violate the driver's duty to:",
                options = listOf("Remain and aid the victim", "Overtake another vehicle", "Park on the sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Umalis agad ang driver sa pinangyarihan ng aksidente nang hindi tinutulungan ang biktima at walang legal na exception. Nilabag nito ang:",
                optionsFil = listOf("Tungkuling manatili at tumulong sa biktima (Duty in case of accident)", "Tungkulin sa pag-overtake", "Tungkulin sa bangketa")
            ),
            QuizQuestion(
                id = 63,
                question = "Which is an exception that may justify leaving the accident scene?",
                options = listOf("The driver wants to avoid traffic", "The driver is in imminent danger of serious harm", "The driver does not want to wait"),
                correctAnswerIndex = 1,
                questionFil = "Alin sa mga sumusunod ang legal na eksepsiyon na nagpapahintulot sa driver na pansamantalang umalis sa aksidente?",
                optionsFil = listOf("Gusto ng driver na makaiwas sa trapiko", "Ang driver ay nasa napipintong panganib ng malubhang pananakit (imminent danger)", "Ayaw maghintay ng driver")
            ),
            QuizQuestion(
                id = 64,
                question = "After an accident, a driver may leave the scene to:",
                options = listOf("Buy food", "Summon a physician or nurse to aid the victim", "Repair the vehicle immediately"),
                correctAnswerIndex = 1,
                questionFil = "Matapos ang aksidente, pinapayagan ang driver na umalis sa pinangyarihan upang:",
                optionsFil = listOf("Bumili ng pagkain", "Tumawag o magdala ng doktor o nars upang saklolohan ang biktima", "Ipaayos agad ang sasakyan")
            ),
            QuizQuestion(
                id = 65,
                question = "A driver leaves an accident scene to report the accident to the nearest law officer. This is:",
                options = listOf("One of the specified exceptions", "Always prohibited", "Considered reckless overtaking"),
                correctAnswerIndex = 0,
                questionFil = "Umalis ang driver sa pinangyarihan ng aksidente upang agad itong i-report sa pinakamalapit na opisyal ng batas. Ito ay:",
                optionsFil = listOf("Isa sa mga kinikilalang legal na eksepsiyon sa Section 55", "Palaging ilegal", "Itinuturing na reckless overtaking")
            ),
            QuizQuestion(
                id = 66,
                question = "Which information must an accident-involved driver provide under Section 55?",
                options = listOf("Only the driver's nickname", "True name and address and relevant owner information", "Only the vehicle color"),
                correctAnswerIndex = 1,
                questionFil = "Anong impormasyon ang DAPAT ibigay ng driver na nasangkot sa aksidente ayon sa Section 55?",
                optionsFil = listOf("Palayaw lamang", "Tunay na pangalan, tirahan, at impormasyon ng may-ari ng sasakyan", "Kulay lamang ng sasakyan")
            ),
            QuizQuestion(
                id = 67,
                question = "A driver causes an accident and refuses to identify himself to the authorities. Which duty is potentially violated?",
                options = listOf("Duty of a driver involved in an accident", "Parking duty only", "Overtaking duty only"),
                correctAnswerIndex = 0,
                questionFil = "Nagdulot ng aksidente ang driver at tumangging magpakilala sa mga awtoridad. Aling tungkulin ang nilabag?",
                optionsFil = listOf("Tungkulin ng driver na nasangkot sa aksidente (Section 55)", "Tungkulin sa paradahan lamang", "Tungkulin sa pag-overtake lamang")
            ),
            QuizQuestion(
                id = 68,
                question = "A driver leaves the scene solely because he is afraid of receiving a traffic ticket. Is this one of the listed exceptions?",
                options = listOf("Yes", "No", "Only at night"),
                correctAnswerIndex = 1,
                questionFil = "Umalis ang driver sa aksidente dahil natatakot lamang siyang matiketan ng traffic violation. Ito ba ay legal na eksepsiyon?",
                optionsFil = listOf("Oo", "Hindi", "Sa gabi lamang")
            ),
            QuizQuestion(
                id = 69,
                question = "Which situation most clearly satisfies the purpose of the accident-scene duty?",
                options = listOf("Providing or obtaining necessary assistance for an injured victim", "Leaving immediately to avoid responsibility", "Moving to another city"),
                correctAnswerIndex = 0,
                questionFil = "Aling sitwasyon ang pinakamalinaw na tumutugon sa layunin ng tungkulin sa aksidente?",
                optionsFil = listOf("Pagbibigay o pagkuha ng kinakailangang tulong para sa nasugatang biktima", "Agad na pagtakas upang makaiwas sa pananagutan", "Paglipat sa ibang lungsod")
            ),
            QuizQuestion(
                id = 70,
                question = "A driver involved in an accident needs medical assistance for the victim and leaves specifically to summon a physician. Under Section 55, this may be:",
                options = listOf("A recognized exception", "Automatically illegal", "Considered parking"),
                correctAnswerIndex = 0,
                questionFil = "Kailangan ng biktima ng agarang medikal na tulong at umalis ang driver partikular upang tumawag ng doktor. Sa Section 55, ito ay:",
                optionsFil = listOf("Isang kinikilalang legal na eksepsiyon", "Awtomatikong ilegal", "Itinuturing na parking violation")
            ),
            QuizQuestion(
                id = 71,
                question = "Two vehicles arrive at an intersection simultaneously. Your vehicle is on the left. Even if you believe you can cross first, the legal default is to:",
                options = listOf("Yield to the vehicle on the right", "Accelerate", "Sound the horn and proceed"),
                correctAnswerIndex = 0,
                questionFil = "Sabay na dumating sa interseksyon ang dalawang sasakyan. Nasa kaliwa ang iyong sasakyan. Kahit sa tingin mo ay kaya mong mauna, ano ang legal na default?",
                optionsFil = listOf("Magbigay-daan sa sasakyang nasa kanan", "Bumilis", "Bumusina at dumiretso")
            ),
            QuizQuestion(
                id = 72,
                question = "You are approaching a crosswalk and see a pedestrian already crossing. The safest legal decision is to:",
                options = listOf("Continue because the vehicle is larger", "Yield", "Overtake another vehicle"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa crosswalk at may pedestrian na tumatawid na. Ano ang pinakaligtas at legal na desisyon?",
                optionsFil = listOf("Magpatuloy dahil mas malaki ang sasakyan", "Magbigay-daan at huminto", "I-overtake ang ibang sasakyan")
            ),
            QuizQuestion(
                id = 73,
                question = "You are about to overtake, but the oncoming lane is not clearly visible. What should you do?",
                options = listOf("Overtake quickly", "Wait until it is clearly visible and safe", "Sound the horn and cross"),
                correctAnswerIndex = 1,
                questionFil = "Mao-overtake ka ngunit hindi malinaw na tanaw ang kasalubong na linya. Ano ang dapat mong gawin?",
                optionsFil = listOf("Mabilis na mag-overtake", "Maghintay hanggang maging malinaw at ligtas ang pananaw", "Bumusina at tumawid sa linya")
            ),
            QuizQuestion(
                id = 74,
                question = "You are behind a slow vehicle near the crest of a hill. What is the best legal decision?",
                options = listOf("Overtake immediately", "Wait until overtaking can be done legally and safely", "Use the shoulder"),
                correctAnswerIndex = 1,
                questionFil = "Nasa likod ka ng mabagal na sasakyan malapit sa tuktok ng burol. Ano ang pinakamahusay na legal na desisyon?",
                optionsFil = listOf("Mag-overtake agad", "Maghintay hanggang sa makalagpas sa tuktok kung saan legal at ligtas na", "Gamitin ang shoulder")
            ),
            QuizQuestion(
                id = 75,
                question = "You are approaching a railway crossing and want to pass a slower vehicle. What should you do?",
                options = listOf("Overtake before reaching the crossing", "Do not overtake at the railway crossing", "Use the opposite shoulder"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ka sa tawiran ng tren (railway crossing) at nais mong lagpasan ang mabagal na sasakyan. Ano ang dapat gawin?",
                optionsFil = listOf("Mag-overtake bago marating ang tawiran", "Huwag mag-overtake sa railway crossing", "Gamitin ang kabilang shoulder")
            ),
            QuizQuestion(
                id = 76,
                question = "You see temporary signs indicating highway workers ahead. A vehicle in front is moving slowly. What should you do?",
                options = listOf("Overtake within the restricted area", "Avoid overtaking between the warning points", "Drive on the sidewalk"),
                correctAnswerIndex = 1,
                questionFil = "May mga pansamantalang babala na may highway workers sa unahan. Mabagal ang sasakyan sa harap. Ano ang dapat mong gawin?",
                optionsFil = listOf("Mag-overtake sa loob ng restricted zone", "Iwasang mag-overtake sa pagitan ng mga babalang karatula", "Magmaneho sa bangketa")
            ),
            QuizQuestion(
                id = 77,
                question = "You are entering a highway from a private driveway and see a motorcycle approaching. Who has priority?",
                options = listOf("Your vehicle because you are entering", "The motorcycle already on the highway", "Whoever sounds the horn first"),
                correctAnswerIndex = 1,
                questionFil = "Papasok ka sa highway mula sa pribadong driveway at may paparating na motorsiklo. Sino ang may prayoridad?",
                optionsFil = listOf("Ang iyong sasakyan dahil ikaw ang pumapasok", "Ang motorsiklong nasa highway na", "Kung sino ang unang bumusina")
            ),
            QuizQuestion(
                id = 78,
                question = "You are approaching a through highway. Traffic appears light. What should you do before entering?",
                options = listOf("Yield as required and ensure it is safe", "Enter without slowing", "Assume through traffic will stop"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ka sa through highway at tila maluwag ang trapiko. Ano ang dapat gawin bago pumasok?",
                optionsFil = listOf("Magbigay-daan ayon sa batas at tiyaking ligtas", "Pumasok nang hindi nagbabagal", "Ipagpalagay na hihinto ang trapiko sa highway")
            ),
            QuizQuestion(
                id = 79,
                question = "You are traveling at the statutory maximum but visibility suddenly becomes poor. What is the proper decision?",
                options = listOf("Maintain maximum speed because it is legal", "Reduce speed as needed for safety", "Accelerate through the area"),
                correctAnswerIndex = 1,
                questionFil = "Bumibiyahe ka sa statutory maximum speed ngunit biglang lumabo ang paningin dahil sa sama ng panahon. Ano ang tamang desisyon?",
                optionsFil = listOf("Panatilihin ang bilis dahil legal naman ito", "Bawasan ang bilis ayon sa kinakailangan para sa kaligtasan", "Bumilis upang makalabas agad sa lugar")
            ),
            QuizQuestion(
                id = 80,
                question = "You are carrying passengers and need to stop briefly, but your vehicle would block moving traffic. What should you prioritize?",
                options = listOf("Avoid obstructing traffic", "Stop wherever convenient", "Stop in the middle of the lane"),
                correctAnswerIndex = 0,
                questionFil = "May sakay kang pasahero at kailangang huminto sandali, ngunit mahaharangan ang gumagalaw na trapiko. Ano ang dapat mong unahin?",
                optionsFil = listOf("Iwasang makaharang sa trapiko (Section 54)", "Huminto kahit saan maginhawa", "Huminto sa gitna ng linya")
            ),
            QuizQuestion(
                id = 81,
                question = "Vehicle A is approaching an intersection from the left. Vehicle B is already inside the intersection and turning left across A's path with a visible signal. Who should yield?",
                options = listOf("Vehicle A", "Vehicle B", "Neither"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang Sasakyan A sa interseksyon mula sa kaliwa. Ang Sasakyan B ay nasa loob na ng interseksyon at lumiliko pakaliwa na may malinaw na signal. Sino ang dapat magbigay-daan?",
                optionsFil = listOf("Sasakyan A", "Sasakyan B", "Wala sa dalawa")
            ),
            QuizQuestion(
                id = 82,
                question = "Vehicle A is traveling above the lawful speed and reaches an intersection at the same time as Vehicle B on its right. Can A claim the normal right-of-way?",
                options = listOf("Yes, because A arrived first", "No, unlawful speed can cause forfeiture of right-of-way", "Yes, if A sounds the horn"),
                correctAnswerIndex = 1,
                questionFil = "Ang Sasakyan A ay nagpapatakbo nang lampas sa speed limit at sabay na nakarating sa interseksyon sa Sasakyan B na nasa kanyang kanan. Maaari bang igiit ni A ang normal na right-of-way?",
                optionsFil = listOf("Oo, dahil nauna si A", "Hindi, ang labag sa batas na bilis ay nagdudulot ng pagkawala (forfeiture) ng right-of-way", "Oo, kung bubusina si A")
            ),
            QuizQuestion(
                id = 83,
                question = "A driver approaches a curve with an obstructed view but the road has two or more lanes moving in the same direction. Which statement is most accurate?",
                options = listOf("The general prohibition has an exception for passing on such multi-lane roads under the conditions stated in the law", "Passing is always prohibited in every circumstance", "Passing is required"),
                correctAnswerIndex = 0,
                questionFil = "Papalapit ang driver sa kurbadang limitado ang paningin ngunit may 2 o higit pang linyang pareho ang direksyon. Aling pahayag ang pinakatumpak?",
                optionsFil = listOf("Ang pangkalahatang pagbabawal ay may eksepsiyon para sa paglagpas sa kalsadang may maraming linya ayon sa batas", "Palaging bawal mag-overtake sa anumang sitwasyon", "Obligadong mag-overtake")
            ),
            QuizQuestion(
                id = 84,
                question = "A driver wants to pass another vehicle at an intersection. The intersection is controlled by a traffic signal. Under Section 41, this situation may fall under:",
                options = listOf("An exception to the general intersection overtaking prohibition", "An absolute prohibition with no exception", "A parking violation"),
                correctAnswerIndex = 0,
                questionFil = "Nais lagpasan ng driver ang sasakyan sa interseksyon na kontrolado ng traffic signal. Sa ilalim ng Section 41, ito ay:",
                optionsFil = listOf("Isang legal na eksepsiyon sa pangkalahatang pagbabawal sa pag-overtake sa interseksyon", "Isang ganap na pagbabawal na walang eksepsiyon", "Isang parking violation")
            ),
            QuizQuestion(
                id = 85,
                question = "A driver wants to pass on the right because the vehicle ahead is about to make a left turn. Under RA 4136, this maneuver may be permitted when:",
                options = listOf("It can be done safely and under the applicable rule", "The driver is speeding", "The road is a sidewalk"),
                correctAnswerIndex = 0,
                questionFil = "Nais lagpasan ng driver sa kanan ang sasakyan sa unahan na liliko pakaliwa. Sa RA 4136, pinapayagan ito kapag:",
                optionsFil = listOf("Magagawa ito nang ligtas at sa ilalim ng angkop na patakaran (Section 39)", "Mabilis ang takbo ng driver", "Bangketa ang kalsada")
            ),
            QuizQuestion(
                id = 86,
                question = "You are overtaking a vehicle and have not yet safely cleared it. When should you return to the right side?",
                options = listOf("Immediately after entering the opposite lane", "Only after safely clearing the overtaken vehicle", "Before passing it"),
                correctAnswerIndex = 1,
                questionFil = "Nag-o-overtake ka at hindi mo pa ganap na nalalagpasan ang kabilang sasakyan. Kailan ka dapat bumalik sa kanang linya?",
                optionsFil = listOf("Agad pagkapasok sa kabilang linya", "Kapag ligtas at malinaw nang nakalagpas sa sasakyan", "Bago ito malagpasan")
            ),
            QuizQuestion(
                id = 87,
                question = "A driver is about to be overtaken and accelerates to prevent the other vehicle from passing. This is:",
                options = listOf("Correct defensive driving", "Contrary to the rule requiring the overtaken driver not to increase speed until completely passed", "Required by RA 4136"),
                correctAnswerIndex = 1,
                questionFil = "Mao-overtake ang isang driver at binilisan niya ang takbo upang pigilan ang kabilang sasakyan na makalagpas. Ito ay:",
                optionsFil = listOf("Tamang defensive driving", "Labag sa patakarang nagbabawal sa pagpapabilis hanggang ganap na makalagpas ang nag-o-overtake", "Iniaatas ng RA 4136")
            ),
            QuizQuestion(
                id = 88,
                question = "A driver is approaching a blind corner and sees a stopped vehicle ahead. Which consideration is most important?",
                options = listOf("The higher statutory speed", "The dangerous road condition requiring reduced speed", "The vehicle's engine power"),
                correctAnswerIndex = 1,
                questionFil = "Papalapit ang driver sa blind corner at may nakitang nakahintong sasakyan. Aling konsiderasyon ang pinakamahalaga?",
                optionsFil = listOf("Ang mas mataas na statutory speed", "Ang mapanganib na kondisyon ng daan na nag-aatas ng pinababang bilis (20 km/h)", "Ang lakas ng makina ng sasakyan")
            ),
            QuizQuestion(
                id = 89,
                question = "A driver parks four meters from a fire hydrant but the vehicle is unattended. Is the parking permitted?",
                options = listOf("Yes, because it is exactly four meters away", "No, because parking within four meters is prohibited", "Yes, if hazard lights are on"),
                correctAnswerIndex = 1,
                questionFil = "Pumarada ang driver nang 4 na metro mula sa fire hydrant at iniwang walang bantay. Pinapayagan ba ang paradahan?",
                optionsFil = listOf("Oo, dahil sakto itong 4 na metro", "Hindi, dahil ipinagbabawal ang pagparada sa loob ng 4 na metro", "Oo, kung may hazard lights")
            ),
            QuizQuestion(
                id = 90,
                question = "A vehicle is parked within six meters of an intersection's curb-line intersection. The driver argues that the road is empty. Is the parking still prohibited?",
                options = listOf("Yes", "No", "Only during rush hour"),
                correctAnswerIndex = 0,
                questionFil = "Pumarada ang sasakyan sa loob ng 6 na metro mula sa kanto ng interseksyon. Iginiit ng driver na bakante naman ang kalye. Ipinagbabawal pa rin ba ito?",
                optionsFil = listOf("Oo", "Hindi", "Tuwing rush hour lamang")
            ),
            QuizQuestion(
                id = 91,
                question = "Which combination is correct?",
                options = listOf("Crosswalk—pedestrian right-of-way; private driveway—entering vehicle yields", "Crosswalk—vehicle always has priority; private driveway—entering vehicle has priority", "Both are always controlled by the faster vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Aling kumbinasyon ang TAMA ayon sa batas-trapiko?",
                optionsFil = listOf("Crosswalk — pedestrian ang may right-of-way; Pribadong driveway — ang pumapasok na sasakyan ang magbibigay-daan", "Crosswalk — sasakyan ang may prayoridad; Driveway — pumapasok na sasakyan ang may prayoridad", "Pareho silang kontrolado ng mas mabilis na sasakyan")
            ),
            QuizQuestion(
                id = 92,
                question = "Which situation can cause a driver to lose an otherwise available right-of-way?",
                options = listOf("Traveling at an unlawful speed", "Driving slowly", "Using a signal"),
                correctAnswerIndex = 0,
                questionFil = "Aling sitwasyon ang maaaring maging dahilan upang MAWALA ng driver ang kanyang right-of-way?",
                optionsFil = listOf("Pagmamaneho sa labag sa batas na bilis (unlawful speed)", "Mabagal na pagpapatakbo", "Paggamit ng turn signal")
            ),
            QuizQuestion(
                id = 93,
                question = "Which combination correctly describes overtaking?",
                options = listOf("Generally pass left; return right only after safely clearing", "Always pass right; return left immediately", "Pass on any side without checking traffic"),
                correctAnswerIndex = 0,
                questionFil = "Aling kumbinasyon ang wastong naglalarawan ng pag-overtake?",
                optionsFil = listOf("Karaniwang mag-overtake sa kaliwa; bumalik sa kanan kapag ligtas at malinaw nang nakalagpas", "Palaging mag-overtake sa kanan; bumalik agad sa kaliwa", "Mag-overtake sa alinmang panig nang hindi tumitingin")
            ),
            QuizQuestion(
                id = 94,
                question = "Which situation is specifically prohibited?",
                options = listOf("Overtaking within a no-passing zone", "Waiting for a safe passing opportunity", "Yielding to an overtaking vehicle"),
                correctAnswerIndex = 0,
                questionFil = "Aling sitwasyon ang partikular na IPINAGBABAWAL sa batas?",
                optionsFil = listOf("Pag-overtake sa loob ng isang no-passing zone", "Paghintay sa ligtas na pagkakataon sa pag-overtake", "Pagbibigay-daan sa nag-o-overtake na sasakyan")
            ),
            QuizQuestion(
                id = 95,
                question = "A driver wants to make a left turn at a normal two-way intersection. Which approach is generally correct?",
                options = listOf("Approach in the lane to the right of and nearest the center line", "Approach from the far-right lane", "Approach from the shoulder"),
                correctAnswerIndex = 0,
                questionFil = "Nais lumiko pakaliwa ng driver sa normal na two-way intersection. Aling paglapit ang tama?",
                optionsFil = listOf("Lumapit sa linyang nasa kanan at pinakamalapit sa center line", "Lumapit mula sa pinakakanang linya", "Lumapit mula sa shoulder")
            ),
            QuizQuestion(
                id = 96,
                question = "Which combination contains only places where parking is prohibited under Section 46?",
                options = listOf("Crosswalk, intersection, in front of a private driveway", "Legal parking area, private garage, designated parking space", "Open parking lot, legal shoulder, private garage"),
                correctAnswerIndex = 0,
                questionFil = "Aling kumbinasyon ang naglalaman LAMANG ng mga lugar kung saan bawal pumarada sa ilalim ng Section 46?",
                optionsFil = listOf("Crosswalk, intersection, tapat ng pribadong driveway", "Legal parking area, pribadong garahe, designated parking space", "Bukas na parking lot, legal shoulder, garahe")
            ),
            QuizQuestion(
                id = 97,
                question = "A driver hears an ambulance's audible signal while approaching an intersection. Which sequence is most appropriate?",
                options = listOf("Stop clear of the intersection near the right edge and allow the ambulance to pass", "Stop in the center and wait", "Race the ambulance through the intersection"),
                correctAnswerIndex = 0,
                questionFil = "Narinig ng driver ang sirena ng ambulansya habang papalapit sa interseksyon. Aling hakbang ang pinaka-angkop?",
                optionsFil = listOf("Huminto sa kanang gilid ng kalsada malayo sa interseksyon at hayaang makadaan ang ambulansya", "Huminto sa gitna at maghintay", "Makipagkarera sa ambulansya sa interseksyon")
            ),
            QuizQuestion(
                id = 98,
                question = "Which action best follows the rule against obstruction of traffic?",
                options = listOf("Loading passengers while blocking the free passage of vehicles", "Loading or unloading without unnecessarily blocking traffic", "Stopping in the middle of the roadway whenever convenient"),
                correctAnswerIndex = 1,
                questionFil = "Aling aksyon ang pinakamahusay na sumusunod sa patakaran laban sa obstruction of traffic?",
                optionsFil = listOf("Pagsasakay ng pasahero habang humaharang sa daan", "Pagsasakay o pagbababa nang hindi kinakailangang humaharang sa trapiko", "Paghinto sa gitna ng kalsada kahit kailan maginhawa")
            ),
            QuizQuestion(
                id = 99,
                question = "Which situation is most consistent with RA 4136's reckless-driving prohibition?",
                options = listOf("Driving with reasonable caution according to traffic and road conditions", "Operating a vehicle recklessly in a manner that endangers persons or property", "Reducing speed near a dangerous area"),
                correctAnswerIndex = 1,
                questionFil = "Aling sitwasyon ang pinakatumutugma sa pagbabawal sa reckless driving sa RA 4136?",
                optionsFil = listOf("Pagmamaneho nang may makatwirang pag-iingat ayon sa trapiko", "Pagpapatakbo nang walang ingat na naglalagay sa panganib sa mga tao o ari-arian", "Pagbabawas ng bilis malapit sa mapanganib na lugar")
            ),
            QuizQuestion(
                id = 100,
                question = "A driver must choose between legally overtaking a vehicle and waiting because visibility is insufficient. What is the best decision?",
                options = listOf("Wait until the maneuver can be completed safely and legally", "Overtake immediately because traffic is slow", "Use the sidewalk to pass"),
                correctAnswerIndex = 0,
                questionFil = "Dapat pumili ang driver sa pagitan ng pag-overtake o paghintay dahil hindi malinaw ang pananaw sa kalsada. Ano ang pinakamahusay na desisyon?",
                optionsFil = listOf("Maghintay hanggang sa magawa ang maniobra nang ligtas at legal", "Mag-overtake agad dahil mabagal ang trapiko", "Gamitin ang bangketa upang lumagpas")
            )
        )
    )
}
