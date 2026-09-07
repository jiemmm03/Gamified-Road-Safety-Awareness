package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.ui.theme.Dimens

import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import com.example.gamifiedroadsafetyawareness.ui.theme.PnpTacticalNavy
import com.example.gamifiedroadsafetyawareness.ui.theme.PoliceBlue
import com.example.gamifiedroadsafetyawareness.ui.theme.PureWhite

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    elevation: Int = 4,
    content: @Composable ColumnScope.() -> Unit
) {
    // Law enforcement command console card with subtle hairline border and structured elevation
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = (elevation / 2).dp),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.4f)),
        content = content
    )
}

/**
 * Official Philippine Police / HPG Shield Insignia Badge
 */
@Composable
fun PoliceBadgeInsignia(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    badgeColor: Color = BadgeGold,
    baseColor: Color = PnpTacticalNavy
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 4))
            .background(Brush.verticalGradient(listOf(baseColor, PoliceBlue)))
            .border(1.dp, badgeColor.copy(alpha = 0.6f), RoundedCornerShape(size / 4)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Shield,
            contentDescription = "Official Police Shield",
            tint = badgeColor,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

/**
 * Official Officer Rank Chip with gold stars / brass styling
 */
@Composable
fun OfficerRankChip(
    rankTitle: String,
    level: Int,
    modifier: Modifier = Modifier,
    isDarkNavy: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isDarkNavy) PnpTacticalNavy.copy(alpha = 0.85f) else BadgeGold.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, BadgeGold.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = BadgeGold,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = "LVL $level • ${rankTitle.uppercase()}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    fontSize = 10.sp
                ),
                color = if (isDarkNavy) PureWhite else NavyPrimary
            )
        }
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight)
            .shadow(
                elevation = if (enabled) Dimens.elevationHigh else Dimens.elevationNone,
                shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
                spotColor = containerColor.copy(alpha = 0.5f)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
    ) {
        val contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor
        )
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.outline
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .then(if (scrollable) Modifier.verticalScroll(scrollState) else Modifier)
            .padding(horizontal = Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

/**
 * Standard search box — extracted from the pattern already used independently in
 * AuditDashboardScreen/XpHistoryScreen so new admin screens reuse it instead of
 * re-implementing the same OutlinedTextField+Search-icon+clear-icon block.
 */
@Composable
fun SearchFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search..."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                }
            }
        },
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * Generic Cancel/Confirm dialog for destructive or otherwise consequential actions,
 * so this pattern isn't hand-rolled per screen.
 */
@Composable
fun ConfirmActionDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmLabel: String = "Confirm",
    destructive: Boolean = true
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        text = { Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    confirmLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
