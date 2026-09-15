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
