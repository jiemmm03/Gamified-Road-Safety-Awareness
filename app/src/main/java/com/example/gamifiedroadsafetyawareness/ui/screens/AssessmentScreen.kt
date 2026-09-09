package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.LearningModule
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.GraphiteInk

@Composable
fun AssessmentScreen(
    username: String,
    xpManager: XpManager? = null,
    onLaunchSimulation: () -> Unit,
    onStartQuiz: (String) -> Unit = {},
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
            Text(
                text = "Complete modules to earn Training XP and advance your Officer Training Level.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        val unlockedStates = modules.associate { module ->
            val unlockInfo = moduleUnlockStatus[module.id]
            val adminEnabled = GamificationConstants.ContentSettings.isModuleEnabled(module.id)
            val isUnlocked = adminEnabled && (unlockInfo?.first ?: true)
            val lockReason = if (!adminEnabled) "Currently Unavailable" else (unlockInfo?.second ?: "")
            module.id to (isUnlocked to lockReason)
        }
        // Only the first not-yet-completed, unlocked module in order is genuinely "up next" —
        // the rest that are unlocked but untouched are just "Available", not all simultaneously next.
        val nextModuleId = modules.firstOrNull { module ->
            module.id !in completedModuleIds && unlockedStates[module.id]?.first == true
        }?.id

        items(modules) { module ->
            val (isUnlocked, lockReason) = unlockedStates.getValue(module.id)

            ModuleCard(
                module = module,
                isCompleted = module.id in completedModuleIds,
                isUnlocked = isUnlocked,
                isNextUp = module.id == nextModuleId,
                lockReason = lockReason,
                onStartClick = { onStartQuiz(module.id) },
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
    onCompleteClick: () -> Unit = {}
) {
    val isRecommended = module.isRecommended && isUnlocked
    val bgColor = when {
        !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        isRecommended -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surface
    }
    val elevation = if (isRecommended) 8 else 2

    // Real progress/status, not the module's static placeholder defaults — a quiz module is
    // binary (completed or not), so "in progress" percentages don't apply here.
    val effectiveStatus = when {
        isCompleted -> "Completed"
        !isUnlocked -> "Locked"
        isNextUp -> "Up Next"
        else -> "Available"
    }
    val effectiveProgress = if (isCompleted) 1f else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = effectiveProgress,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "moduleProgress"
    )

    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = bgColor,
        elevation = elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = module.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (isRecommended) {
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Difficulty + module-type badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "AI: ${module.moduleType.label}".uppercase(),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val xpReward = GamificationConstants.ModuleXp.getModuleXp(module.id)
                    Text(
                        text = "${module.moduleType.label} · +$xpReward XP".uppercase(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${effectiveStatus.uppercase()} · ${(effectiveProgress * 100).toInt()}%",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline,
                        strokeCap = StrokeCap.Round
                    )
                }

                if (!isUnlocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Locked",
                            tint = AmberYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = lockReason,
                            color = AmberYellow,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onStartClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecommended) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (module.progressPercentage > 0f) "Continue" else "Start",
                            color = if (isRecommended) Color.White else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            if (isUnlocked && !isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))

                val xpReward = GamificationConstants.ModuleXp.getModuleXp(module.id)
                TextButton(
                    onClick = onCompleteClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mark Complete (+$xpReward XP)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
