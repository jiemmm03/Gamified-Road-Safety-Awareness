package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.model.AiTutorEngine
import com.example.gamifiedroadsafetyawareness.model.TutorMessage
import com.example.gamifiedroadsafetyawareness.model.XpManager
import com.example.gamifiedroadsafetyawareness.ui.components.AiMessageBubble
import com.example.gamifiedroadsafetyawareness.ui.components.AnswerOptionsColumn
import com.example.gamifiedroadsafetyawareness.ui.components.AppTextField
import com.example.gamifiedroadsafetyawareness.ui.components.QuickReplyChipRow
import com.example.gamifiedroadsafetyawareness.ui.components.UserMessageBubble
import kotlinx.coroutines.launch

private data class ChatEntry(
    val fromUser: Boolean,
    val message: TutorMessage
)

@Composable
fun AiTutorScreen(
    username: String,
    xpManager: XpManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val engine = remember(username) { AiTutorEngine(xpManager, username) }
    val messages = remember { mutableStateListOf<ChatEntry>() }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages.add(ChatEntry(fromUser = false, message = engine.greet()))
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    fun sendFreeText(text: String) {
        if (text.isBlank() || isThinking) return
        messages.add(ChatEntry(fromUser = true, message = TutorMessage(text)))
        inputText = ""
        isThinking = true
        coroutineScope.launch {
            val reply = engine.handleFreeText(text)
            messages.add(ChatEntry(fromUser = false, message = reply))
            isThinking = false
        }
    }

    fun submitAnswer(entryIndex: Int, optionIndex: Int, optionLabel: String) {
        if (isThinking) return
        messages[entryIndex] = messages[entryIndex].copy(message = messages[entryIndex].message.copy(answerOptions = emptyList()))
        messages.add(ChatEntry(fromUser = true, message = TutorMessage(optionLabel)))
        isThinking = true
        coroutineScope.launch {
            val reply = engine.submitAnswer(optionIndex)
            messages.add(ChatEntry(fromUser = false, message = reply))
            isThinking = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "RoadSafe AI",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your local road safety tutor",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages.size) { index ->
                val entry = messages[index]
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (entry.fromUser) {
                        UserMessageBubble(text = entry.message.text)
                    } else {
                        AiMessageBubble(text = entry.message.text)
                        if (entry.message.answerOptions.isNotEmpty()) {
                            AnswerOptionsColumn(
                                options = entry.message.answerOptions,
                                onSelect = { optionIndex ->
                                    submitAnswer(index, optionIndex, entry.message.answerOptions[optionIndex])
                                }
                            )
                        }
                    }
                }
            }
            if (isThinking) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.width(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RoadSafe AI is thinking…",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        val latestQuickReplies = messages.lastOrNull { !it.fromUser }?.message?.quickReplies ?: emptyList()
        if (latestQuickReplies.isNotEmpty() && !isThinking) {
            QuickReplyChipRow(
                chips = latestQuickReplies,
                onSelect = { chip -> sendFreeText(chip) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = "Message RoadSafe AI",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { sendFreeText(inputText) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
