package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.CompetencyMetric
import com.example.gamifiedroadsafetyawareness.ui.theme.AmberYellow
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

private fun competencyColor(percentage: Int): Color = when {
    percentage >= 85 -> EmeraldGreen
    percentage >= 70 -> AmberYellow
    else -> TrafficRed
}

@Composable
fun CompetencyBarChart(
    metrics: List<CompetencyMetric>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        metrics.forEach { metric ->
            CompetencyBarRow(metric)
        }
    }
}

@Composable
private fun CompetencyBarRow(metric: CompetencyMetric) {
    val barColor = competencyColor(metric.percentage)
    val animatedFraction by animateFloatAsState(
        targetValue = metric.percentage / 100f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "competencyBar"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = metric.skillName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                if (metric.isFocusArea) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "🎯", style = MaterialTheme.typography.labelSmall)
                }
            }
            Text(
                text = "${metric.percentage}%",
                style = MaterialTheme.typography.labelMedium,
                color = barColor,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = if (metric.isFocusArea) 1.dp else 0.dp,
                    color = if (metric.isFocusArea) barColor.copy(alpha = 0.6f) else Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction.coerceIn(0f, 1f))
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(barColor)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = metric.statusLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Minimal hand-rolled line chart with a filled area beneath it — no charting library in this
 * project, matches the Canvas-based pattern already used by AnimatedProgressRing/CelebrationBurst.
 */
@Composable
fun TrendLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    if (values.isEmpty()) return
    val maxValue = (values.maxOrNull() ?: 0f).coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val stepX = if (values.size > 1) size.width / (values.size - 1) else 0f
        val points = values.mapIndexed { index, value ->
            Offset(
                x = index * stepX,
                y = size.height - (value / maxValue) * size.height
            )
        }

        val fillPath = Path().apply {
            moveTo(points.first().x, size.height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height)
            close()
        }
        drawPath(path = fillPath, color = lineColor.copy(alpha = 0.15f))

        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        points.forEach { point ->
            drawCircle(color = lineColor, radius = 4.dp.toPx(), center = point)
        }
    }
}
