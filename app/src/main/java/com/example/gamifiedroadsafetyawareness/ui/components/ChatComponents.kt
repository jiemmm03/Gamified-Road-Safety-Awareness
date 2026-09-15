package com.example.gamifiedroadsafetyawareness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale

/** Right-aligned bubble for the user's own messages. */
@Composable
fun UserMessageBubble(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        AppCard(
            modifier = Modifier.widthIn(max = 280.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = 2
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

/**
 * Left-aligned bubble for RoadSafe AI's messages.
 *
 * When [isStructured] is true, renders the full Answer / Explanation / Safety Tip layout
 * with color-coded section dividers. Otherwise renders a plain text bubble.
 *
 * @param text          Main response text (always rendered).
 * @param answerLabel   Optional "Result: ✅ Correct!" label shown at the top.
 * @param explanation   Optional explanation body shown under a divider.
 * @param safetyTip     Optional safety tip shown at the bottom with a shield icon.
 * @param topicTag      Optional topic name shown as a small chip.
 * @param isStructured  Whether to use the structured section layout.
 */
@Composable
fun AiMessageBubble(
    text: String,
    answerLabel: String = "",
    explanation: String = "",
    safetyTip: String = "",
    topicTag: String = "",
    isStructured: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        AppCard(modifier = Modifier.widthIn(max = 310.dp), elevation = 2) {
            Column(modifier = Modifier.padding(12.dp)) {

                // ── AI header row ────────────────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "RoadSafe AI",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ROADSAFE AI",
                        style = AppTypeScale.eyebrowLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (isStructured) {
                    // ── Structured Answer/Explanation/Tip layout ─────────────────

                    // Answer label (e.g. "✅ Correct!" or topic name)
                    if (answerLabel.isNotBlank()) {
                        Text(
                            text = answerLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                answerLabel.contains("✅") || answerLabel.contains("Tama") ->
                                    MaterialTheme.colorScheme.tertiary
                                answerLabel.contains("❌") || answerLabel.contains("Mali") ->
                                    MaterialTheme.colorScheme.error
                                answerLabel.contains("🟡") ->
                                    MaterialTheme.colorScheme.secondary
                                else ->
                                    MaterialTheme.colorScheme.primary
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Main text
                    if (text.isNotBlank()) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Explanation section
                    if (explanation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.width(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "EXPLANATION",
                                    style = AppTypeScale.eyebrowLabel,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Safety tip section
                    if (safetyTip.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                                .padding(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Security,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "SAFETY TIP",
                                    style = AppTypeScale.eyebrowLabel,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = safetyTip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                } else {
                    // ── Plain text bubble ────────────────────────────────────────
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/** A question's answer choices, rendered as full-width outlined buttons instead of free text. */
@Composable
fun AnswerOptionsColumn(
    options: List<String>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { index, option ->
            OutlinedButton(
                onClick = { onSelect(index) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = option, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

/** Discoverable-capabilities row of suggestion chips below the chat input. */
@Composable
fun QuickReplyChipRow(
    chips: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (chips.isEmpty()) return
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(chips) { chip ->
            AssistChip(
                onClick = { onSelect(chip) },
                label = { Text(chip) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    labelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
