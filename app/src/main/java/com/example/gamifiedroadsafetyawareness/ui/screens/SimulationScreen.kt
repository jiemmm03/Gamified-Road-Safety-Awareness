package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.DecisionOption
import com.example.gamifiedroadsafetyawareness.model.MockData
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationScreen(
    onSubmitDecision: (DecisionOption) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scenario = MockData.activeScenario
    var selectedOption by remember { mutableStateOf<DecisionOption?>(null) }
    var timeLeft by remember { mutableIntStateOf(scenario.timeLimitSeconds) }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val bgColor = Color(0xFF0B0E1A)
    val textPrimary = Color(0xFFF0F0F5)
    val textSecondary = Color(0xFF8E93A6)
    val coral = Color(0xFFFF5252)
    val violet = Color(0xFF7C4DFF)
    val cyan = Color(0xFF00E5FF)
    val emerald = Color(0xFF00E676)
    val amber = Color(0xFFFFC107)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulation", color = textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = bgColor,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Scenario Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF1A1F35), Color(0xFF0B0E1A))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Scenario ${scenario.scenarioNumber}",
                                color = textSecondary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = scenario.title,
                                color = textPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = String.format("00:%02d", timeLeft),
                            color = coral,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Indicators
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AI Mode indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(violet)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "AI: ${scenario.aiMode}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Weather indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E2A3A))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Weather: ${scenario.weather}",
                            color = cyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Viewport
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1A2332), RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = scenario.description,
                            color = textPrimary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Detected Hazards:",
                            color = textSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            scenario.hazards.forEach { hazard ->
                                val hazardTextColor = when {
                                    hazard.contains("car", ignoreCase = true) -> cyan
                                    hazard.contains("ambulance", ignoreCase = true) -> Color.White // coral background, white text stands out better, but keeping to specs
                                    hazard.contains("pedestrian", ignoreCase = true) -> amber
                                    hazard.contains("traffic light", ignoreCase = true) -> Color.Yellow
                                    else -> Color.White
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Brush.horizontalGradient(listOf(coral, Color(0xFFD50000))))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = hazard,
                                        color = if (hazardTextColor == Color.White) Color.White else hazardTextColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Decision Prompt
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141829))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Action Required",
                            color = coral,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = scenario.prompt,
                            color = textPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Options
            items(scenario.options) { option ->
                val isSelected = selectedOption == option
                val borderColor = if (isSelected) emerald else Color(0xFF2A2F45)
                val optionBgColor = if (isSelected) emerald.copy(alpha = 0.08f) else Color(0xFF141829)
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                        .clickable { selectedOption = option },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = optionBgColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = emerald,
                                unselectedColor = textSecondary
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = option.label,
                                color = textPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (option.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = option.description,
                                    color = textSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Telemetry Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = violet.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, violet.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Telemetry",
                            tint = violet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Grip Reduction: ${scenario.roadGripReduction} | XP Reward: ${scenario.xpReward}",
                            color = textSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Hint Action */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, amber),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = amber)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "AI Hint",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Hint", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { selectedOption?.let { onSubmitDecision(it) } },
                        enabled = selectedOption != null,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = emerald,
                            disabledContainerColor = emerald.copy(alpha = 0.3f)
                        )
                    ) {
                        Text("Confirm", fontWeight = FontWeight.Bold, color = Color(0xFF0B0E1A))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
