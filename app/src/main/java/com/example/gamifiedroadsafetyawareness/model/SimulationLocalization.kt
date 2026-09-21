package com.example.gamifiedroadsafetyawareness.model

/**
 * High quality Filipino / Tagalog translations for the 20 Driver Decision Simulation Scenarios.
 */
object SimulationLocalization {

    data class LocalizedScenarioData(
        val titleFil: String,
        val descriptionFil: String,
        val promptFil: String,
        val hazardsFil: List<String>,
        val optionDescriptionsFil: Map<String, String>,
        val optionExplanationsFil: Map<String, String>,
        val hazardIdentifiedFil: String,
        val safetyPrincipleFil: String,
        val recommendedActionFil: String
    )

    private val scenarioTranslations: Map<Int, LocalizedScenarioData> = mapOf(
        1 to LocalizedScenarioData(
            titleFil = "01 — Tawiran ng Tao (Pedestrian Crossing)",
            descriptionFil = "Nagmamaneho ka nang 35 km/h sa isang karaniwang lansangan sa lungsod. Sa unahan, may nakitang markadong tawiran (zebra crossing) at may mga taong nakatayo sa bangketa na handang tumawid.",
            promptFil = "Ano ang pinakaligtas at naaayon sa batas na dapat gawin?",
            hazardsFil = listOf(
                "⚠️ Panganib 1: Mga taong handang tumawid sa zebra crossing",
                "⚠️ Panganib 2: Daloy ng trapiko na nangangailangan ng malinaw na senyas"
            ),
            optionDescriptionsFil = mapOf(
                "s01_a" to "Bilisan nang bahagya ang takbo upang makalagpas bago pa man makatapak sa kalsada ang mga tao.",
                "s01_b" to "Bumusina nang paulit-ulit at magpatuloy sa pagmamaneho sa parehong bilis.",
                "s01_c" to "Magdahan-dahan sa pagpreno, ganap na huminto bago ang stop line, at magbigay-daan sa mga tumatawid.",
                "s01_d" to "Lumipat sa kabilang linya upang iwasan ang tawiran nang hindi humihinto."
            ),
            optionExplanationsFil = mapOf(
                "s01_a" to "Ang pagbilis sa tapat ng tawiran ay direktang naglalagay sa panganib sa mga tao at labag sa R.A. 4136.",
                "s01_b" to "Ang pagbusina ay hindi nagbibigay ng karapatan sa driver. Ang mga naglalakad sa zebra crossing ang may prayoridad.",
                "s01_c" to "Ang maagang pagbagal at pagbibigay-daan ay nagbibigay ng ligtas na tawiran at nag-aalis ng panganib sa banggaan.",
                "s01_d" to "Ang pagkabig sa kasalubong na linya ay lumilikha ng matinding panganib ng salpukan ng mga sasakyan."
            ),
            hazardIdentifiedFil = "Mga taong tumatawid sa markadong tawiran na maaaring biglang gumalaw.",
            safetyPrincipleFil = "R.A. 4136 — Ganap na tungkulin ng driver na magbigay-daan sa mga naglalakad sa markadong tawiran.",
            recommendedActionFil = "Bitiwan ang silinyador, dahan-dahang tapakan ang preno bago ang puting linya, at maghintay hanggang makatawid nang buo ang mga tao."
        ),
        2 to LocalizedScenarioData(
            titleFil = "02 — Pagpapalit ng Ilaw-Trapiko",
            descriptionFil = "Papalapit ka sa isang interseksyon nang 40 km/h, humigit-kumulang 35 metro ang layo. Ang ilaw-trapiko ay biglang nagpalit mula berde patungong dilaw (amber).",
            promptFil = "Ano ang pinakaligtas at naaayon sa batas na desisyon?",
            hazardsFil = listOf(
                "⚠️ Panganib 1: Paparating na pulang ilaw",
                "⚠️ Panganib 2: Mga sasakyan sa kabilang kalsada na naghahandang umabante"
            ),
            optionDescriptionsFil = mapOf(
                "s02_a" to "Bilisan agad ang takbo upang maunahan ang pulang ilaw.",
                "s02_b" to "Tumingin sa rearview mirror at magpreno nang kontrolado upang huminto bago ang stop line.",
                "s02_c" to "Biglang apakan ang emergency brake nang hindi tinitingnan ang sumusunod na sasakyan.",
                "s02_d" to "Bumusina at dumiretso sa interseksyon nang hindi nagbabawas ng bilis."
            ),
            optionExplanationsFil = mapOf(
                "s02_a" to "Ang pagbilis sa dilaw na ilaw ay karaniwang nagdudulot ng T-bone collision habang umaabante ang kabilang trapiko.",
                "s02_b" to "Ang dilaw ay babala na maghandang huminto kung ligtas itong magagawa. Sa 35m at 40 km/h, ligtas ang kontroladong paghinto.",
                "s02_c" to "Ang biglaang pagpreno nang walang pagsusuri sa salamin ay maaaring maging sanhi ng pagkakabangga sa likod.",
                "s02_d" to "Ang pagbalewala sa dilaw na ilaw ay isang paglabag sa batas trapiko at nagdudulot ng aksidente."
            ),
            hazardIdentifiedFil = "Dilemma zone sa interseksyon at panganib ng maagang pag-usad ng kabilang linya.",
            safetyPrincipleFil = "Ang dilaw na ilaw ay hudyat upang maghandang huminto, hindi hudyat upang bilisan ang takbo.",
            recommendedActionFil = "Suriin ang salamin sa likod, magpreno nang banayad, at huminto bago ang stop line."
        ),
        3 to LocalizedScenarioData(
            titleFil = "03 — Paparating na Ambulansya o Emergency Vehicle",
            descriptionFil = "Nagmamaneho ka sa gitnang linya ng isang highway. Mula sa likuran, may mabilis na papalapit na ambulansya na may umiilaw na sirena at alarma.",
            promptFil = "Ano ang tamang aksyon ayon sa batas trapiko ng Pilipinas?",
            hazardsFil = listOf(
                "⚠️ Panganib 1: Mabilis na ambulansya na nangangailangan ng agarang daan",
                "⚠️ Panganib 2: Iba pang sasakyan na sabay-sabay na lumilipat ng linya"
            ),
            optionDescriptionsFil = mapOf(
                "s03_a" to "Manatili sa kasalukuyang linya at magpatuloy sa parehong bilis.",
                "s03_b" to "Mag-signal pakanan, lumipat sa kanang linya kapag ligtas, at magbigay-daan sa ambulansya.",
                "s03_c" to "Bumilis upang manguna sa ambulansya at tulungan itong magbukas ng daan.",
                "s03_d" to "Biglang huminto sa gitna mismo ng kalsada."
            ),
            optionExplanationsFil = mapOf(
                "s03_a" to "Ang pagharang sa emergency vehicle na may aktibong sirena ay labag sa batas at naglalagay ng buhay sa peligro.",
                "s03_b" to "Ayon sa R.A. 4136, ang lahat ng driver ay dapat magbigay-daan sa mga emergency vehicle sa pamamagitan ng pagtabi sa kanan.",
                "s03_c" to "Hindi dapat nakikipagkarera o humahawi ng daan para sa emergency vehicle.",
                "s03_d" to "Ang biglaang paghinto sa gitna ng linya ay nagiging sanhi ng matinding banggaan mula sa likod."
            ),
            hazardIdentifiedFil = "Emergency vehicle na may agarang pangangailangan sa right-of-way.",
            safetyPrincipleFil = "R.A. 4136 Seksyon 43 — Ganap na prayoridad sa emergency vehicles na may aktibong sirena/ilaw.",
            recommendedActionFil = "Mag-signal pakanan, suriin ang mga salamin at blind spot, at maayos na tumabi sa kanang linya."
        ),
        4 to LocalizedScenarioData(
            titleFil = "04 — Malakas na Ulan at Madulas na Kalsada",
            descriptionFil = "Biglang bumuhos ang napakalakas na ulan. Bumababa ang kapit ng gulong nang 30% at nababawasan ang paningin sa daan.",
            promptFil = "Ano ang pinakaligtas na hakbang sa pagmamaneho sa basang kalsada?",
            hazardsFil = listOf(
                "⚠️ Panganib 1: Hydroplaning at pagkawala ng kontrol sa manibela",
                "⚠️ Panganib 2: Mas mahabang distansya bago ganap na huminto ang sasakyan"
            ),
            optionDescriptionsFil = mapOf(
                "s04_a" to "Panatilihin ang bilis na 80 km/h at mag-hazard lights habang tumatakbo.",
                "s04_b" to "Bawasan ang bilis nang 30-40%, buksan ang low-beam headlights, at doblehin ang following distance.",
                "s04_c" to "Mag-overtake sa mga mababagal na sasakyan upang makaiwas agad sa ulan.",
                "s04_d" to "Gumamit ng high beam upang mas luminaw ang paningin sa malakas na ulan."
            ),
            optionExplanationsFil = mapOf(
                "s04_a" to "Ang hazard lights habang umaandar ay nakakalito at labag sa batas; kailangang magbawas ng bilis sa basang kalsada.",
                "s04_b" to "Ang pagbabawas ng bilis at pagdagdag ng distansya ay pumipigil sa hydroplaning at nagbibigay ng sapat na oras sa pagpreno.",
                "s04_c" to "Ang pag-overtake sa madulas na kalsada ay may napakataas na tsansa ng spin-out at pagkabangga.",
                "s04_d" to "Ang high beam sa ulan ay nagba-bounce pabalik (glare) at mas lalong nagpapalabo sa paningin."
            ),
            hazardIdentifiedFil = "Bawas na kapit ng gulong at limitadong paningin dulot ng malakas na ulan.",
            safetyPrincipleFil = "Iangkop ang bilis ayon sa kondisyon ng panahon; doblehin ang distansya kapag basa ang kalsada.",
            recommendedActionFil = "Magbawas ng takbo, iwasan ang biglaang pagkabig o pagpreno, at buksan ang regular low-beam headlights."
        ),
        5 to LocalizedScenarioData(
            titleFil = "05 — T-Interseksyon na Walang Ilaw-Trapiko",
            descriptionFil = "Nasa sumasangang kalsada ka (side road) at papasok sa isang pangunahing kalsada (major highway) sa isang T-intersection na walang traffic light.",
            promptFil = "Sino ang may karapatang mauna (right-of-way)?",
            hazardsFil = listOf(
                "⚠️ Panganib 1: Mabilis na mga sasakyan sa pangunahing kalsada",
                "⚠️ Panganib 2: Limitadong paningin sa mga blind corner"
            ),
            optionDescriptionsFil = mapOf(
                "s05_a" to "Ikaw, dahil ikaw ang unang nakarating sa kanto ng T-intersection.",
                "s05_b" to "Ang mga sasakyang bumibiyahe sa pangunahing kalsada (through-highway).",
                "s05_c" to "Kung sino ang pinakamalaking sasakyan o pinakamabilis bumusina.",
                "s05_d" to "Kahit sino, basta't magkasya sa pagitan ng mga sasakyan."
            ),
            optionExplanationsFil = mapOf(
                "s05_a" to "Sa T-intersection, ang sasakyang galing sa sumasangang kalsada ay dapat magbigay-daan sa pangunahing kalsada.",
                "s05_b" to "Ayon sa batas, ang mga sasakyan sa pangunahing highway ang may absolute right-of-way laban sa papasok mula sa side street.",
                "s05_c" to "Ang laki ng sasakyan o lakas ng busina ay hindi basehan ng batas trapiko.",
                "s05_d" to "Ang pamimilit sumingit ay sanhi ng mga malulubhang banggaan sa highway."
            ),
            hazardIdentifiedFil = "Pagsalubong sa mabilis na daloy ng trapiko mula sa subordinate road.",
            safetyPrincipleFil = "Ang mga sasakyan sa major road ay may right-of-way laban sa mga sasakyang pumapasok mula sa minor road.",
            recommendedActionFil = "Ganap na huminto bago ang kanto, tumingin sa kaliwa, kanan, at kaliwa ulit, at lumiko lamang kung ligtas at walang parating."
        )
    )

    fun getLocalizedScenario(scenario: SimulationScenario, isFilipino: Boolean): SimulationScenario {
        if (!isFilipino) return scenario
        val translation = scenarioTranslations[scenario.scenarioNumber] ?: return scenario

        val localizedOptions = scenario.options.map { option ->
            val descFil = translation.optionDescriptionsFil[option.id] ?: option.description
            val expFil = translation.optionExplanationsFil[option.id] ?: option.explanation
            option.copy(
                description = descFil,
                explanation = expFil,
                descriptionFil = descFil,
                explanationFil = expFil
            )
        }

        return scenario.copy(
            title = translation.titleFil.ifBlank { scenario.title },
            description = translation.descriptionFil.ifBlank { scenario.description },
            prompt = translation.promptFil.ifBlank { scenario.prompt },
            hazards = if (translation.hazardsFil.isNotEmpty()) translation.hazardsFil else scenario.hazards,
            options = localizedOptions,
            hazardIdentified = translation.hazardIdentifiedFil.ifBlank { scenario.hazardIdentified },
            safetyPrinciple = translation.safetyPrincipleFil.ifBlank { scenario.safetyPrinciple },
            recommendedAction = translation.recommendedActionFil.ifBlank { scenario.recommendedAction }
        )
    }
}
