package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

/**
 * Self-contained "+10 XP" style popup that rises and fades whenever [rewardKey] changes to a
 * new positive value — callers bump a counter per reward event rather than toggling visibility.
 */
@Composable
fun FloatingRewardPopup(
    rewardKey: Int,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(rewardKey) {
        if (rewardKey > 0) {
            visible = true
            delay(1100)
            visible = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(250)) { it / 2 } + fadeIn(animationSpec = tween(250)),
        exit = slideOutVertically(animationSpec = tween(400)) { -it } + fadeOut(animationSpec = tween(400)),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
