package com.example.gamifiedroadsafetyawareness.ui.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

/** Left-aligned bubble for RoadSafe AI's messages, reusing the AutoAwesome + eyebrow-label AI branding. */
@Composable
fun AiMessageBubble(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        AppCard(modifier = Modifier.widthIn(max = 280.dp), elevation = 2) {
            Column(modifier = Modifier.padding(12.dp)) {
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
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
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
