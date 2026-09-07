package com.example.gamifiedroadsafetyawareness.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Supplementary text styles that don't map onto one of Material3's 15 standard
 * Typography slots. Kept small and named, following the same flat-object convention
 * as Dimens.kt.
 */
object AppTypeScale {
    // Short ALL-CAPS section kicker / badge label (e.g. "PROGRESS", "DRIVING SCENARIO").
    val eyebrowLabel = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    // Compact badge / chip content text (e.g. module name, risk level inside Badge()).
    val badgeLabel = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.2.sp
    )

    // Dashboard stat value (e.g. audit summary counts, summary-card numbers).
    val statValue = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )
}
