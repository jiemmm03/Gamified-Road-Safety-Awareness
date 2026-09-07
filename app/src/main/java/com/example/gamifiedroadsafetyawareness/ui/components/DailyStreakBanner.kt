package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import java.time.LocalDate

/**
 * In-app "notification" substitute for a daily-streak reminder — no OS notification channel,
 * just contextual Dashboard UI reflecting the same data DailyStreakBanner would otherwise page.
 */
@Composable
fun DailyStreakBanner(
    progress: UserProgressEntity?,
    hoursUntilExpiry: Long,
    modifier: Modifier = Modifier
) {
    if (progress == null || progress.currentStreak <= 0) return

    val activeToday = progress.lastActivityDate == LocalDate.now().toString()
    val (icon, message, tint, statusTitle) = when {
        activeToday -> Quadruple(
            Icons.Rounded.LocalFireDepartment,
            "${progress.currentStreak}-day consecutive patrol record active! Excellent road safety discipline.",
            BadgeGold,
            "PATROL LOG • STREAK ACTIVE"
        )
        else -> Quadruple(
            Icons.Rounded.LocalFireDepartment,
            "Streak status expires in $hoursUntilExpiry hour${if (hoursUntilExpiry == 1L) "" else "s"}. Complete training to maintain clearance.",
            AmberYellow,
            "DISPATCH ADVISORY • STREAK AT RISK"
        )
    }

    AppCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = tint.copy(alpha = 0.5f),
        elevation = 4
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Streak",
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = statusTitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = tint
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
