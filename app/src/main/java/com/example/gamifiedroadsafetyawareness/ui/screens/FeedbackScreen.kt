package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.DecisionOption
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedXpCounter
import com.example.gamifiedroadsafetyawareness.ui.components.AppButton
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.AppOutlinedButton
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.ui.components.CelebrationBurst
import com.example.gamifiedroadsafetyawareness.ui.components.LevelUpModal
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

@Composable
fun FeedbackScreen(
    selectedOption: DecisionOption?,
    xpEarned: Int = 0,
    leveledUp: Boolean = false,
    newLevel: Int = 0,
    onNextScenario: () -> Unit,
    onRetry: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLevelUpModal by remember(leveledUp) { mutableStateOf(leveledUp) }
    val isCorrect = selectedOption?.isCorrect == true
    val themeColor = if (isCorrect) EmeraldGreen else TrafficRed
    val icon = if (isCorrect) "✅" else "❌"
    val title = if (isCorrect) "DECISION APPROVED" else "CRITICAL ERROR"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onReturnHome, modifier = Modifier.align(Alignment.Start)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // AI Analysis Card
            AppCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = themeColor.copy(alpha = 0.5f),
                elevation = 16
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(themeColor.copy(alpha = 0.15f))
                            .border(2.dp, themeColor.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = icon, fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = themeColor,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "AI TELEMETRY ANALYSIS",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = selectedOption?.explanation ?: "No data recorded.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    if (xpEarned > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(themeColor.copy(alpha = 0.1f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⭐", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                AnimatedXpCounter(
                                    targetValue = xpEarned,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = themeColor,
                                    prefix = "+",
                                    suffix = " TRAINING XP GRANTED"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            if (isCorrect) {
                AppButton(
                    text = "Next Scenario",
                    onClick = onNextScenario
                )
            } else {
                AppButton(
                    text = "Retry Scenario",
                    onClick = onRetry,
                    containerColor = themeColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            AppOutlinedButton(
                text = "Return to Dashboard",
                onClick = onReturnHome
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        CelebrationBurst(trigger = isCorrect, modifier = Modifier.fillMaxSize())
    }

    if (showLevelUpModal) {
        LevelUpModal(
            newLevel = newLevel,
            levelName = GamificationConstants.getLevelName(newLevel),
            onDismiss = { showLevelUpModal = false }
        )
    }
}
