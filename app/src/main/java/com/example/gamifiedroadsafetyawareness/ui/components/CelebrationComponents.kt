package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.gamifiedroadsafetyawareness.ui.theme.BadgeGold
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.NavyPrimary
import com.example.gamifiedroadsafetyawareness.ui.theme.PoliceBlue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiParticle(
    val angleDegrees: Float,
    val distance: Float,
    val radius: Float,
    val color: Color
)

/**
 * One-shot confetti burst radiating from the center of its bounds. Plays whenever [trigger]
 * transitions to true; place inside a Box layered above the content it should celebrate.
 */
@Composable
fun CelebrationBurst(
    trigger: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 20,
    colors: List<Color> = listOf(BadgeGold, EmeraldGreen, PoliceBlue, NavyPrimary)
) {
    val progress = remember { Animatable(0f) }
    val particles = remember {
        List(particleCount) {
            ConfettiParticle(
                angleDegrees = Random.nextFloat() * 360f,
                distance = 70f + Random.nextFloat() * 90f,
                radius = 4f + Random.nextFloat() * 5f,
                color = colors[it % colors.size]
            )
        }
    }

    LaunchedEffect(trigger) {
        if (trigger) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing))
        }
    }

    if (progress.value > 0f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val alpha = (1f - progress.value).coerceIn(0f, 1f)
            particles.forEach { particle ->
                val radians = Math.toRadians(particle.angleDegrees.toDouble())
                val travelled = particle.distance * progress.value
                val x = center.x + (cos(radians) * travelled).toFloat()
                val y = center.y + (sin(radians) * travelled).toFloat() - (60f * progress.value)
                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = particle.radius,
                    center = Offset(x, y)
                )
            }
        }
    }
}
