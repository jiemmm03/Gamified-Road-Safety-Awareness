package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.DecisionOption
import com.example.gamifiedroadsafetyawareness.model.MockData

@Composable
fun FeedbackScreen(
    selectedOption: DecisionOption?,
    onNextScenario: () -> Unit,
    onRetry: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFF0B0E1A)
    val textPrimary = Color(0xFFF0F0F5)
    val textSecondary = Color(0xFF8E93A6)
    val emerald1 = Color(0xFF00E676)
    val emerald2 = Color(0xFF00C853)
    val coral1 = Color(0xFFFF5252)
    val coral2 = Color(0xFFD50000)
    val cyan = Color(0xFF00E5FF)
    val violet = Color(0xFF7C4DFF)
    val darkGlass = Color(0xFF141829)
    val darkGlassBorder = Color(0xFF2A2F45)
    val amber = Color(0xFFFFC107)

    val isCorrect = selectedOption?.isCorrect == true

    val headerGradient = if (isCorrect) {
        listOf(emerald1, emerald2)
    } else {
        listOf(coral1, coral2)
    }

    val riskLevelString = selectedOption?.riskLevel ?: ""
    val lawExplanationColor = if (isCorrect) {
        emerald1
    } else if (riskLevelString.contains("Dangerous", ignoreCase = true) || riskLevelString.contains("High", ignoreCase = true)) {
        coral1
    } else {
        amber
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.verticalGradient(headerGradient))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = textPrimary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isCorrect) "Excellent Decision!" else "Room for Improvement",
                        color = textPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedOption?.description ?: "No option selected.",
                        color = textPrimary.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Reward Banner (if correct)
        if (isCorrect) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, emerald1.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = darkGlass)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "XP",
                            tint = emerald1,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Reward Earned",
                                color = textSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "+50 XP",
                                color = emerald1,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Telemetry Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, darkGlassBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = darkGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Telemetry Analysis",
                        color = textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    HorizontalDivider(color = darkGlassBorder)
                    TelemetryRow(
                        icon = Icons.Default.Info,
                        label = "Reaction Time",
                        value = "1.2s",
                        valueColor = amber
                    )
                    TelemetryRow(
                        icon = Icons.Default.CheckCircle,
                        label = "Rules Compliance",
                        value = if (isCorrect) "100%" else "0%",
                        valueColor = if (isCorrect) emerald1 else coral1
                    )
                    TelemetryRow(
                        icon = Icons.Default.Warning,
                        label = "Risk Score",
                        value = riskLevelString.ifEmpty { "Unknown" },
                        valueColor = cyan
                    )
                }
            }
        }

        // Law Explanation Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, violet.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = darkGlass.copy(alpha = 0.95f))
            ) {
                Box(modifier = Modifier.background(violet.copy(alpha = 0.05f))) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = lawExplanationColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Traffic Law Context",
                                color = textPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = selectedOption?.explanation ?: "Understanding traffic rules is key to safety.",
                            color = textSecondary,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // AI Adaptive Curriculum Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, violet.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = violet.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AI Adaptive Curriculum",
                        color = violet,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isCorrect) "Your performance has improved. The next scenario will introduce more complex traffic patterns." else "We will review similar intersection rules in upcoming scenarios.",
                        color = textPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Action Buttons
        item {
            Spacer(modifier = Modifier.height(8.dp))
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.horizontalGradient(listOf(cyan, violet)))
                        .clickable { onNextScenario() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Next Scenario",
                        color = textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cyan)
                ) {
                    Text(
                        text = "Retry Scenario",
                        color = backgroundColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Return Home
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onReturnHome() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Return to Home",
                    color = textSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TelemetryRow(icon: ImageVector, label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = valueColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = Color(0xFF8E93A6),
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
