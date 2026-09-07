package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.AchievementDefinitions
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.model.db.XpHistoryEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedProgressRing
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedXpCounter
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.components.BadgeTile
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyProgressScreen(
    username: String,
    xpManager: XpManager? = null,
    onNavigateToXpHistory: () -> Unit = {},
    onNavigateToLearningHistory: () -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf<UserProgressEntity?>(null) }
    var recentHistory by remember { mutableStateOf<List<XpHistoryEntity>>(emptyList()) }
    var unlockedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var moduleUnlockStatus by remember { mutableStateOf<Map<String, Pair<Boolean, String>>>(emptyMap()) }
    var bestScoreByModule by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    LaunchedEffect(username, xpManager) {
        val manager = xpManager ?: return@LaunchedEffect
        if (username.isNotBlank()) {
            progress = manager.getProgress(username)
            recentHistory = manager.getXpHistory(username, 5)
            unlockedIds = manager.getUnlockedAchievementIds(username)
            moduleUnlockStatus = manager.getModuleUnlockStatus(username)
            bestScoreByModule = manager.getAttemptsForUser(username)
                .groupBy { it.moduleId }
                .mapValues { (_, attempts) -> attempts.maxOf { it.scorePercent } }
        }
    }

    val p = progress
    val level = p?.currentLevel ?: 1
    val totalXp = p?.totalXp ?: 0
    val streak = p?.currentStreak ?: 0
    val longestStreak = p?.longestStreak ?: 0
    val quizzes = p?.quizzesCompleted ?: 0
    val perfectQuizzes = p?.perfectQuizCount ?: 0
    val bestAnswerStreak = p?.bestAnswerStreak ?: 0
    val completedModules = p?.completedModuleIds?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
    val levelName = GamificationConstants.getLevelName(level)
    val xpForNext = GamificationConstants.getXpForNextLevel(level)
    val xpIntoLevel = GamificationConstants.getCurrentLevelXp(totalXp, level)
    val levelProgress = GamificationConstants.getLevelProgress(totalXp, level)
    val xpRemaining = (xpForNext - xpIntoLevel).coerceAtLeast(0)

    val badges = AchievementDefinitions.toBadgeItems(unlockedIds)

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
                text = "My Training Progress",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Hero Level Card ──────────────────────────────────────────────────
        item {
            AppCard(modifier = Modifier.fillMaxWidth(), elevation = 12) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f),
                                Color.Transparent
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedProgressRing(
                            progress = levelProgress,
                            size = 100.dp,
                            strokeWidth = 8.dp,
                            ringColor = BadgeGold
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$level",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = BadgeGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Officer Training Level $level — $levelName",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { levelProgress.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.outline,
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$xpIntoLevel / $xpForNext XP · $xpRemaining XP until Level ${level + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        AnimatedXpCounter(
                            targetValue = totalXp,
                            style = MaterialTheme.typography.titleLarge,
                            suffix = " Total Training XP",
                            color = BadgeGold
                        )
                    }
                }
            }
        }

        // ── Stats Grid ───────────────────────────────────────────────────────
        item {
            Text(
                text = "Training Statistics",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Total XP", "$totalXp", Icons.Rounded.Star, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                StatCard("Modules", "${completedModules.size}/8", Icons.Rounded.CheckCircle, EmeraldGreen, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Current Streak", "$streak days", Icons.Rounded.LocalFireDepartment, AmberYellow, Modifier.weight(1f))
                StatCard("Best Streak", "$longestStreak days", Icons.Rounded.LocalFireDepartment, AmberYellow, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Quizzes", "$quizzes", Icons.Rounded.Quiz, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                StatCard("Perfect Scores", "$perfectQuizzes", Icons.Rounded.EmojiEvents, EmeraldGreen, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Best Answer Streak", "$bestAnswerStreak", Icons.Rounded.LocalFireDepartment, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                StatCard("Scenarios", "${p?.scenariosCompleted ?: 0}", Icons.Rounded.Timeline, MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            }
        }

        // ── Module Progress Cards ────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Module Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavigateToLearningHistory) {
                    Text("Learning History")
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "${completedModules.size} of ${MockData.learningModules.size} modules completed",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(MockData.learningModules.sortedBy { it.moduleType.ordinal }) { module ->
            val isCompleted = module.id in completedModules
            val unlockInfo = moduleUnlockStatus[module.id]
            val isUnlocked = unlockInfo?.first ?: (module.id == "mod_road_safety_fundamentals")
            val lockReason = unlockInfo?.second ?: ""

            ModuleProgressCard(
                title = module.title,
                xpReward = GamificationConstants.ModuleXp.getModuleXp(module.id),
                isCompleted = isCompleted,
                isUnlocked = isUnlocked,
                lockReason = lockReason,
                difficulty = module.moduleType.label,
                bestScorePercent = bestScoreByModule[module.id]
            )
        }

        // ── Achievements Preview ─────────────────────────────────────────────
        item {
            Text(
                text = "Safety Certifications",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(badges) { badge ->
                    BadgeTile(badge = badge)
                }
            }
        }

        // ── Recent XP History Preview ────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Training XP Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavigateToXpHistory) {
                    Text("View All")
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        items(recentHistory) { entry ->
            XpHistoryRow(entry = entry)
        }

        if (recentHistory.isEmpty()) {
            item {
                Text(
                    text = "No activity yet. Start learning to earn XP!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier, elevation = 4) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ModuleProgressCard(
    title: String,
    xpReward: Int,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    lockReason: String,
    difficulty: String,
    bestScorePercent: Int? = null
) {
    val bgColor = when {
        isCompleted -> EmeraldGreen.copy(alpha = 0.08f)
        !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surface
    }
    val borderColor = when {
        isCompleted -> EmeraldGreen.copy(alpha = 0.3f)
        !isUnlocked -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outline
    }

    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = bgColor,
        borderColor = borderColor,
        elevation = if (isCompleted) 4 else 2
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> EmeraldGreen.copy(alpha = 0.15f)
                            !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isCompleted -> Icons.Rounded.CheckCircle
                        !isUnlocked -> Icons.Rounded.Lock
                        else -> Icons.Rounded.Star
                    },
                    contentDescription = null,
                    tint = when {
                        isCompleted -> EmeraldGreen
                        !isUnlocked -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (!isUnlocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when {
                        isCompleted -> "✓ Completed · +$xpReward XP earned"
                        !isUnlocked -> "🔒 $lockReason"
                        else -> "+$xpReward XP available · $difficulty"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        isCompleted -> EmeraldGreen
                        !isUnlocked -> AmberYellow
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                if (isUnlocked && bestScorePercent != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { bestScorePercent / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isCompleted) EmeraldGreen else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.outline,
                            strokeCap = StrokeCap.Round
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Best: $bestScorePercent%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun XpHistoryRow(entry: XpHistoryEntity) {
    val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(entry.createdAt))
    val typeIcon = when {
        entry.activityType.startsWith("MODULE") -> "📚"
        entry.activityType.startsWith("QUIZ") -> "📝"
        entry.activityType.startsWith("SIMULATION") -> "🚗"
        entry.activityType == "ACHIEVEMENT_UNLOCK" -> "🏆"
        entry.activityType == "ADMIN_ADJUSTMENT" -> "⚙️"
        entry.activityType.contains("STREAK") -> "🔥"
        else -> "✨"
    }

    AppCard(modifier = Modifier.fillMaxWidth(), elevation = 2) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = typeIcon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.activityName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (entry.totalAwarded >= 0) "+${entry.totalAwarded}" else "${entry.totalAwarded}",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (entry.totalAwarded >= 0) EmeraldGreen else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "XP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
