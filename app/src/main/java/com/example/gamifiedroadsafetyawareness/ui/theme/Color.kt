package com.example.gamifiedroadsafetyawareness.ui.theme

import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════════════════════════════════════
// 🇵🇭 PHILIPPINE TRAFFIC & DRIVER SAFETY COMMAND IDENTITY PALETTE
// ══════════════════════════════════════════════════════════════════════════

// ── Primary Authority Navy ──
val TacticalNavy = Color(0xFF091931)        // Institutional dark navy — command presence & authority
val CommandNavy = Color(0xFF0E2344)         // Deep precinct command navy — cards & top app bars
val NavyPrimary = TacticalNavy              // Primary identity anchor
val GraphiteInk = Color(0xFF0C1424)         // High-contrast deep slate text base

// ── Secondary Safety Blue ──
val PatrolBlue = Color(0xFF1B3D78)          // Safety patrol blue — secondary & interactive highlights
val PoliceBlue = PatrolBlue                 // Royal safety blue
val PoliceLightBlue = Color(0xFF2563EB)     // Bright safety accent for indicators & links
val GuardianIndigo = NavyPrimary
val WarmCoral = PoliceBlue

// ── Official Brass Badge Gold & Rank Metals ──
val BadgeGold = Color(0xFFD4AF37)           // Official brass safety badge gold
val GoldMetallic = Color(0xFFE5B83B)        // Highlight shimmer & star emblems
val BrassBronze = Color(0xFF9A7B2C)         // Subdued official brass borders

// ── Traffic Enforcement Semantic Colors ──
val SirenRed = Color(0xFFDC2626)            // Emergency siren / violation / critical restricted action
val TrafficRed = SirenRed
val HazardAmber = Color(0xFFF59E0B)         // Road hazard / caution / advisory notification
val AmberYellow = HazardAmber
val ClearRoadGreen = Color(0xFF059669)      // Cleared inspection / passing / authorized road permit
val EmeraldGreen = ClearRoadGreen
val InfoBlue = PoliceLightBlue              // Official notice / informative bulletin

val SemanticSuccess = ClearRoadGreen
val SemanticWarning = HazardAmber
val SemanticError = SirenRed
val SemanticInfo = PoliceLightBlue

// ── Police Console Neutral Surfaces ──
val PureWhite = Color(0xFFFFFFFF)           // Clean police badge white
val CoolSteelGray = Color(0xFFF1F5F9)       // Police command background surface
val LightGray = CoolSteelGray
val SlateGray = Color(0xFF64748B)           // Muted secondary telemetry text
val SteelBorder = Color(0xFFCBD5E1)         // Official hairline card border

// ── Semantic Category Accents ──
val AdminPurple = Color(0xFF5B4E8C)         // Internal affairs / audit trail badge
val NeutralGray = Color(0xFF6B7280)

// ── Tactical Night Patrol (Dark Theme) ──
val DarkBackground = Color(0xFF08101E)      // Night patrol dark navy base
val DarkSurface = Color(0xFF0F1C33)         // Elevated night command console surface
val DarkSurfaceElevated = Color(0xFF162747) // Tactical card container surface
val DarkOutline = Color(0xFF253B66)         // Visible night patrol hairline divider
val DarkOnBackground = Color(0xFFF1F5F9)    // Night vision high-clarity text
val DarkOnSurfaceVariant = Color(0xFF94A3B8)// Muted secondary telemetry

// Pre-mixed solid container tints for dark mode
val IndigoContainerDark = Color(0xFF152A4E)
val CoralContainerDark = Color(0xFF1B3864)
val EmeraldContainerDark = Color(0xFF0A3324)
val AmberContainerDark = Color(0xFF452F08)
val TrafficRedContainerDark = Color(0xFF481212)
val GoldContainerDark = Color(0xFF453609)