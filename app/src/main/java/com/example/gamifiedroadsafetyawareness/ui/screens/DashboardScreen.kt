package com.example.gamifiedroadsafetyawareness.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.LearningModule
import com.example.gamifiedroadsafetyawareness.model.MockData

// Local design tokens
private val BgDark = Color(0xFF0B0E1A)
private val CardBorderColor = Color(0xFF2A2F45)
private val TxtPrimary = Color(0xFFF0F0F5)
private val TxtSecondary = Color(0xFF8E93A6)
private val Cyan = Color(0xFF00E5FF)
private val Violet = Color(0xFF7C4DFF)
private val Emerald = Color(0xFF00E676)
private val Amber = Color(0xFFFFD740)
private val Coral = Color(0xFFFF5252)
private val CardGrad = listOf(Color(0xFF1A1F35), Color(0xFF141829))
private val HeroAiGrad = listOf(Cyan.copy(alpha = 0.15f), Violet.copy(alpha = 0.15f))

@Composable
fun DashboardScreen(
    onLaunchSimulation: () -> Unit,
    onNavigateToGamification: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user = MockData.currentUser
    val modules = MockData.learningModules

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            // 1. Hero Greeting Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(CardGrad))
                    .border(1.dp, CardBorderColor, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Welcome back,",
                                color = TxtSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = user.name,
                                color = TxtPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Amber, Coral))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Lv${user.level}",
                                color = BgDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "XP Progress",
                                color = TxtSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = { user.currentXp.toFloat() / user.nextLevelXp.toFloat() },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = Amber,
                                    trackColor = CardBorderColor,
                                    strokeCap = StrokeCap.Round
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${user.currentXp}/${user.nextLevelXp}",
                                    color = TxtPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🔥",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user.dailyStreakDays} Days",
                                color = TxtPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            // 2. AI Coach Recommendation Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(HeroAiGrad))
                    .border(1.dp, Violet.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🤖", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Coach Recommendation",
                            color = Cyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Analysis: You missed 2 Right-of-Way questions in yesterday's quiz. AI has prepared a targeted interactive simulation to reinforce your decision-making.",
                        color = TxtPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(Cyan, Violet)))
                            .clickable { onLaunchSimulation() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🚀 Launch AI Recommended Simulation",
                            color = BgDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        item {
            // 3. Section Title
            Text(
                text = "Learning Modules",
                color = TxtSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // 4. Module cards
        items(modules) { module ->
            ModuleCard(module = module, onStartClick = {
                if (module.isRecommended) {
                    onLaunchSimulation()
                } else {
                    Toast.makeText(context, "Opening ${module.title}...", Toast.LENGTH_SHORT).show()
                }
            })
        }

        item {
            // 5. Gamification summary card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF2B2211), Color(0xFF1F180B))))
                    .border(1.dp, Amber.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable { onNavigateToGamification() }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🏆 Your League Rank",
                            color = Amber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "#${user.rankInLgu} in ${user.lguLeagueName}",
                            color = TxtPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Recent Badge: 🛡️ Defensive Driving Pro",
                            color = TxtSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Text(text = "🏆", fontSize = 32.sp)
                }
            }
        }

        item {
            // 6. AI Analytics quick card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF0D241C), Color(0xFF091A14))))
                    .border(1.dp, Emerald.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable { onNavigateToAnalytics() }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📊 Safety Score Analytics",
                            color = Emerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Overall Score: ${user.overallSafetyScore} / 100",
                            color = TxtPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Status: LTO Exam Ready! ✅",
                            color = Emerald,
                            fontSize = 12.sp
                        )
                    }
                    Text(text = "📊", fontSize = 32.sp)
                }
            }
        }

        item {
            // 7. Daily quiz banner card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF331414), Color(0xFF1F0B0B))))
                    .border(1.dp, Coral.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⚡ Daily Challenge",
                            color = Coral,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Answer 5 dynamic questions in 3 mins to earn +100 XP & protect your streak.",
                            color = TxtPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "Starting Daily Quiz! Good luck!", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Coral),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Start", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ModuleCard(module: LearningModule, onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(CardGrad))
            .border(
                1.dp,
                if (module.isRecommended) Cyan.copy(alpha = 0.5f) else CardBorderColor,
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        color = TxtPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = module.description,
                        color = TxtSecondary,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
                if (module.isRecommended) {
                    Text(text = "✨", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Difficulty badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (module.aiDifficulty) {
                            "Easy" -> Emerald.copy(alpha = 0.1f)
                            "Medium" -> Amber.copy(alpha = 0.1f)
                            "Adaptive" -> Violet.copy(alpha = 0.1f)
                            else -> Coral.copy(alpha = 0.1f)
                        }
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "AI: ${module.aiDifficulty}",
                    color = when (module.aiDifficulty) {
                        "Easy" -> Emerald
                        "Medium" -> Amber
                        "Adaptive" -> Violet
                        else -> Coral
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CardBorderColor)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${module.status} · ${(module.progressPercentage * 100).toInt()}%",
                        color = TxtSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { module.progressPercentage },
                        modifier = Modifier
                            .width(120.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (module.isRecommended) Cyan else Emerald,
                        trackColor = CardBorderColor,
                        strokeCap = StrokeCap.Round
                    )
                }

                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (module.isRecommended) Cyan else Color(0xFF2A2F45)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (module.progressPercentage > 0f) "Continue" else "Start",
                        color = if (module.isRecommended) BgDark else TxtPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
