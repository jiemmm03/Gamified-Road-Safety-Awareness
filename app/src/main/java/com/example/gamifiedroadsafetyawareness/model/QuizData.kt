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
