package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Warning
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.R
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.MotivationTemplates
import com.example.gamifiedroadsafetyawareness.model.db.UserProgressEntity
import com.example.gamifiedroadsafetyawareness.ui.components.AiInsightCard
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedProgressRing
import com.example.gamifiedroadsafetyawareness.ui.components.AnimatedXpCounter
import com.example.gamifiedroadsafetyawareness.ui.components.DailyStreakBanner
import com.example.gamifiedroadsafetyawareness.ui.components.SkeletonLoader
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun DashboardScreen(
    userName: String,
    onLaunchSimulation: () -> Unit,
    onNavigateToGamification: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    modifier: Modifier = Modifier,
    userProgress: UserProgressEntity? = null,
    hoursUntilStreakExpires: Long = 0
) {
    val userXp = userProgress?.totalXp ?: 0
    val userLevel = userProgress?.currentLevel ?: 1
    val userStreak = userProgress?.currentStreak ?: 0
    val safetyScore = ((userXp.coerceAtMost(5000).toFloat() / 5000f) * 100).toInt().coerceIn(0, 100)
    val levelProgress = GamificationConstants.getLevelProgress(userXp, userLevel)
    val xpIntoLevel = GamificationConstants.getCurrentLevelXp(userXp, userLevel)
    val xpForNextLevel = GamificationConstants.getXpForNextLevel(userLevel)
    val levelName = GamificationConstants.getLevelName(userLevel)
    val completedModules = userProgress?.completedModuleIds
        ?.split(",")?.filter { it.isNotBlank() }?.size ?: 0
    val totalModules = MockData.learningModules.size
    val overallProgress = if (totalModules > 0) completedModules.toFloat() / totalModules else 0f

    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
    }

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> stringResource(R.string.dashboard_greeting_morning)
        in 12..17 -> stringResource(R.string.dashboard_greeting_afternoon)
        else -> stringResource(R.string.dashboard_greeting_evening)
    }

    if (isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.dashboardHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
        ) {
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
            SkeletonLoader(height = 56.dp)
            SkeletonLoader(height = 180.dp)
            SkeletonLoader(height = 140.dp)
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)) {
                SkeletonLoader(height = 120.dp, modifier = Modifier.weight(1f))
                SkeletonLoader(height = 120.dp, modifier = Modifier.weight(1f))
            }
            SkeletonLoader(height = 80.dp)
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimens.dashboardHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
    ) {
        // ── ① Official Law Enforcement Command Header ────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.35f)),
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    NavyPrimary.copy(alpha = 0.04f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Shield,
                                    contentDescription = null,
                                    tint = BadgeGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "OFFICIAL DISPATCH • ${greeting.uppercase()}",
                                    style = AppTypeScale.eyebrowLabel.copy(letterSpacing = 1.sp),
                                    color = BadgeGold
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = userName.ifBlank { "Officer Cadet" },
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            com.example.gamifiedroadsafetyawareness.ui.components.OfficerRankChip(
                                rankTitle = levelName,
                                level = userLevel
                            )
                        }

                        com.example.gamifiedroadsafetyawareness.ui.components.PoliceBadgeInsignia(size = 52.dp)
                    }
                }
            }
        }

        // ── Streak Banner ───────────────────────────────────────────────────
        item {
            DailyStreakBanner(progress = userProgress, hoursUntilExpiry = hoursUntilStreakExpires)
        }

        // ── ② Learning Progress Summary Card ────────────────────────────────
        item {
            DashboardCard {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Text(
                        text = stringResource(R.string.dashboard_learning_progress).uppercase(),
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Progress ring
                        AnimatedProgressRing(
                            progress = overallProgress,
                            size = 80.dp,
                            strokeWidth = 8.dp,
                            ringColor = EmeraldGreen
                        ) {
                            Text(
                                text = "${(overallProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(Dimens.spacingLarge))

                        // Stats column
                        Column(modifier = Modifier.weight(1f)) {
                            // Level + Level Name
                            Text(
                                text = "Level $userLevel · $levelName",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(Dimens.spacingTiny))

                            // XP
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AnimatedXpCounter(
                                    targetValue = userXp,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    suffix = " XP"
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                            // Level progress bar
                            LinearProgressIndicator(
                                progress = { levelProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = BadgeGold,
                                trackColor = MaterialTheme.colorScheme.outline,
                                strokeCap = StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                            Text(
                                text = "$xpIntoLevel / $xpForNextLevel XP to Level ${userLevel + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                            // Streak + Modules completed
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = if (userStreak >= 7) BadgeGold else AmberYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$userStreak day streak",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (userStreak >= 7) BadgeGold else AmberYellow
                                    )
                                }
                                Text(
                                    text = "$completedModules / $totalModules modules",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── ③ Continue Learning / AI Recommended Card ───────────────────────
        item {
            DashboardCard(
                modifier = Modifier.clickable(
                    onClickLabel = stringResource(R.string.dashboard_start_simulation),
                    role = Role.Button,
                    onClick = onLaunchSimulation
                )
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                            Text(
                                text = stringResource(R.string.dashboard_ai_recommended_module).uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                style = AppTypeScale.eyebrowLabel
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                        Text(
                            text = stringResource(R.string.dashboard_recommended_module_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(Dimens.titleToDescriptionSpacing))
                        Text(
                            text = stringResource(R.string.dashboard_recommended_module_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                        Button(
                            onClick = onLaunchSimulation,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.dashboard_start_simulation),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── ④ Training Modules Preview ──────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dashboard_modules_section),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
                contentPadding = PaddingValues(end = Dimens.spacingSmall)
            ) {
                items(MockData.learningModules) { module ->
                    val isCompleted = userProgress?.completedModuleIds
                        ?.split(",")?.contains(module.id) == true
                    val xpReward = GamificationConstants.ModuleXp.getModuleXp(module.id)

                    DashboardCard(
                        modifier = Modifier.width(200.dp)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.spacingMedium)) {
                            // Emoji icon container
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (module.moduleType) {
                                            com.example.gamifiedroadsafetyawareness.model.ModuleType.EASY -> EmeraldGreen.copy(alpha = 0.12f)
                                            com.example.gamifiedroadsafetyawareness.model.ModuleType.MEDIUM -> AmberYellow.copy(alpha = 0.12f)
                                            com.example.gamifiedroadsafetyawareness.model.ModuleType.HARD -> TrafficRed.copy(alpha = 0.12f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (module.moduleType) {
                                        com.example.gamifiedroadsafetyawareness.model.ModuleType.EASY -> "🟢"
                                        com.example.gamifiedroadsafetyawareness.model.ModuleType.MEDIUM -> "🟡"
                                        com.example.gamifiedroadsafetyawareness.model.ModuleType.HARD -> "🔴"
                                    },
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                            Text(
                                text = module.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                            Text(
                                text = "+$xpReward XP · ${module.moduleType.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(Dimens.spacingSmall))

                            // Status indicator
                            LinearProgressIndicator(
                                progress = { if (isCompleted) 1f else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (isCompleted) EmeraldGreen else MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.outline,
                                strokeCap = StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                            Text(
                                text = if (isCompleted) "Completed ✓" else "Available",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isCompleted) EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // ── ⑤ Quiz & Challenge Card ─────────────────────────────────────────
        item {
            DashboardCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClickLabel = stringResource(R.string.dashboard_start_quiz),
                        role = Role.Button,
                        onClick = onLaunchSimulation
                    )
            ) {
                Row(
                    modifier = Modifier.padding(Dimens.cardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.iconContainerSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Quiz,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(Dimens.spacingMedium))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.dashboard_quiz_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                        Text(
                            text = stringResource(R.string.dashboard_quiz_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ── ⑥ Gamification Quick Stats ──────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
            ) {
                // Safety Score
                DashboardCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAnalytics() }
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.spacingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_safety_score).uppercase(),
                            style = AppTypeScale.eyebrowLabel,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                        AnimatedProgressRing(
                            progress = safetyScore / 100f,
                            size = 72.dp,
                            strokeWidth = 7.dp,
                            ringColor = EmeraldGreen
                        ) {
                            Text(
                                text = "$safetyScore",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Training Level
                DashboardCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToGamification() }
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.spacingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_training_level).uppercase(),
                            style = AppTypeScale.eyebrowLabel,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                        AnimatedProgressRing(
                            progress = levelProgress,
                            size = 72.dp,
                            strokeWidth = 7.dp,
                            ringColor = BadgeGold
                        ) {
                            Text(
                                text = "$userLevel",
                                style = MaterialTheme.typography.titleLarge,
                                color = BadgeGold
                            )
                        }
                    }
                }
            }
        }

        // ── Level progress summary ──────────────────────────────────────────
        item {
            Text(
                text = stringResource(R.string.dashboard_level_progress_summary, userLevel, xpIntoLevel, xpForNextLevel, (levelProgress * 100).toInt()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ── ⑦ AI Recommendation Card ────────────────────────────────────────
        item {
            AiInsightCard(insight = MotivationTemplates.generate(userProgress))
        }

        // ── ⑧ Daily Safety Tip ──────────────────────────────────────────────
        item {
            DashboardCard {
                Row(
                    modifier = Modifier.padding(Dimens.cardPadding),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Shield,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.dashboard_safety_tip_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.height(Dimens.titleToDescriptionSpacing))
                        Text(
                            text = stringResource(R.string.dashboard_safety_tip),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ── ⑨ Infraction Alert ──────────────────────────────────────────────
        item {
            DashboardCard(
                borderColor = TrafficRed.copy(alpha = 0.2f),
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                Row(
                    modifier = Modifier.padding(Dimens.cardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TrafficRed.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = stringResource(R.string.dashboard_alert),
                            tint = TrafficRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.dashboard_infraction_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = TrafficRed
                        )
                        Spacer(modifier = Modifier.height(Dimens.spacingTiny))
                        Text(
                            text = stringResource(R.string.dashboard_infraction_detail),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ── Bottom safe-area spacer ─────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        }
    }
}

// ─── Dashboard Card (shared with AdminDashboardScreen) ──────────────────────

/**
 * Dashboard-specific card with [Dimens.cardCornerRadius] (20dp) and subtle elevation.
 */
@Composable
internal fun DashboardCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationLow),
        border = BorderStroke(1.dp, borderColor),
        content = content
    )
}
