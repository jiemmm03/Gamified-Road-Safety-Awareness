package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens

/**
 * Dedicated management hub screen for administrative control functions.
 * Houses User Management, Module Management, and Answer Review —
 * separating "Manage & Control" from the Admin dashboard's "Monitor & Overview" role.
 */
@Composable
fun AdminManagementScreen(
    onManageUsers: () -> Unit,
    onManageModules: () -> Unit,
    onReviewAnswers: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Dimens.dashboardHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.cardVerticalSpacing)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
            Text(
                text = "Management",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(Dimens.spacingTiny))
            Text(
                text = "Manage users, learning modules, quizzes, and submitted answers.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ── Management Cards ────────────────────────────────────────────────
        item {
            val managementCards = listOf(
                AdminHubCardSpec(
                    title = "User Management",
                    description = "Search, review, activate, and deactivate user accounts.",
                    icon = Icons.Rounded.ManageAccounts,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = onManageUsers
                ),
                AdminHubCardSpec(
                    title = "Module Management",
                    description = "Manage training modules, quizzes, and learning content.",
                    icon = Icons.AutoMirrored.Rounded.MenuBook,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    onClick = onManageModules
                ),
                AdminHubCardSpec(
                    title = "Answer Review",
                    description = "Review learner answers, quiz responses, and submitted results.",
                    icon = Icons.Rounded.RateReview,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    onClick = onReviewAnswers
                )
            )
            AdminHubCardGrid(cards = managementCards)
        }

        // ── Bottom safe-area spacer ─────────────────────────────────────────
        item {
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        }
    }
}
