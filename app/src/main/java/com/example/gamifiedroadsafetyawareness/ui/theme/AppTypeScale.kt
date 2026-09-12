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
    // UPPERCASE SYSTEM LABEL / EYEBROW TEXT (11–13sp, Semi-bold, letter-spacing 1.0–1.5sp)
    val eyebrowLabel = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp
    )

    // COMPACT BADGE / CHIP CONTENT (11–12sp, Medium/Semi-bold)
    val badgeLabel = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.2.sp
    )

    // DASHBOARD STAT VALUE (24–32sp, Bold / 700, Centered)
    val statValue = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    )

    // DASHBOARD STAT LABEL (12–14sp, Medium / 500, Centered)
    val statLabel = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    )
}
