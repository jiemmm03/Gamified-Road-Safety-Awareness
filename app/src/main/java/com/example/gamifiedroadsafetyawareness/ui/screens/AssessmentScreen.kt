package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.LearningModule
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.ModuleType
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.GraphiteInk
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import com.example.gamifiedroadsafetyawareness.ui.theme.PureWhite
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

@Composable
fun AssessmentScreen(
    username: String,
    xpManager: XpManager? = null,
    onLaunchSimulation: () -> Unit,
    onStartQuiz: (String) -> Unit = {},
    onViewResults: (String) -> Unit = {},
    onModuleComplete: (LearningModule) -> Unit = {},
    completedModuleIds: Set<String> = emptySet(),
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modules = MockData.learningModules.sortedBy { it.moduleType.ordinal }
    var moduleUnlockStatus by remember { mutableStateOf<Map<String, Pair<Boolean, String>>>(emptyMap()) }
    val moduleSettingsState by GamificationConstants.ContentSettings.moduleSettingsFlow.collectAsState()

    LaunchedEffect(username, xpManager) {
        val manager = xpManager ?: return@LaunchedEffect
        if (username.isNotBlank()) {
            moduleUnlockStatus = manager.getModuleUnlockStatus(username)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Training Modules",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Complete modules and interactive simulations to advance your Driver Awareness Level.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }

        // ── Visual Simulation Hero Card ──────────────────────────────────────
        item {
            AppCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                elevation = 4
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "AI VISUAL SIMULATION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BadgeGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "+2,400 XP Max",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BadgeGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "20-Scenario Visual Driving Simulation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Experience 20 realistic Philippine road scenarios covering pedestrian crossings, traffic signals, heavy rain, blind spots, emergency vehicles, and complex intersections.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onLaunchSimulation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "START SIMULATION",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        val unlockedStates = modules.associate { module ->
            val unlockInfo = moduleUnlockStatus[module.id]
            val adminEnabled = GamificationConstants.ContentSettings.isModuleEnabled(module.id)
            val isUnlocked = adminEnabled && (unlockInfo?.first ?: true)
            val lockReason = if (!adminEnabled) "Currently Unavailable" else (unlockInfo?.second ?: "")
            module.id to (isUnlocked to lockReason)
        }
        val nextModuleId = modules.firstOrNull { module ->
            module.id !in completedModuleIds && unlockedStates[module.id]?.first == true
        }?.id

        items(modules, key = { it.id }) { module ->
            val (isUnlocked, lockReason) = unlockedStates.getValue(module.id)
            val isCompleted = module.id in completedModuleIds

            ModuleCard(
                module = module,
                isCompleted = isCompleted,
                isUnlocked = isUnlocked,
                isNextUp = module.id == nextModuleId,
                lockReason = lockReason,
                onStartClick = { onStartQuiz(module.id) },
                onViewResultsClick = { onViewResults(module.id) },
                onCompleteClick = { onModuleComplete(module) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ModuleCard(
    module: LearningModule,
    isCompleted: Boolean = false,
    isUnlocked: Boolean = true,
    isNextUp: Boolean = false,
    lockReason: String = "",
    onStartClick: () -> Unit,
    onViewResultsClick: () -> Unit = onStartClick,
    onCompleteClick: () -> Unit = {}
) {
    val isRecommended = module.isRecommended && isUnlocked
    val bgColor = when {
        !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        isRecommended -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        else -> MaterialTheme.colorScheme.surface
    }
    val elevation = if (isRecommended) 6 else 2

    val difficultyDot = when (module.moduleType) {
        ModuleType.EASY -> "🟢"
        ModuleType.MEDIUM -> "🟡"
        ModuleType.HARD -> "🔴"
    }

    val xpReward = GamificationConstants.ModuleXp.getModuleXp(module.id)

    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = bgColor,
        elevation = elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Difficulty + Title and Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = "$difficultyDot ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = module.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isCompleted) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldGreen.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Completed",
                                color = EmeraldGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (isRecommended) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BadgeGold)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✨ AI PICK",
                            color = GraphiteInk,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = module.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Row: Metadata Chip + Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Simplified clean metadata chip: "AI Module · +100 XP"
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "AI Module · +$xpReward XP",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Action Area
                if (!isUnlocked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Locked",
                            tint = AmberYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = lockReason,
                            color = AmberYellow,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                } else if (isCompleted) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onStartClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = "RETAKE",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        Button(
                            onClick = onViewResultsClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = "RESULTS",
                                color = PureWhite,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                } else {
                    val buttonLabel = if (module.progressPercentage > 0f) "CONTINUE" else "START"
                    Button(
                        onClick = onStartClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 96.dp)
                            .height(42.dp)
                    ) {
                        Text(
                            text = buttonLabel,
                            color = PureWhite,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}
