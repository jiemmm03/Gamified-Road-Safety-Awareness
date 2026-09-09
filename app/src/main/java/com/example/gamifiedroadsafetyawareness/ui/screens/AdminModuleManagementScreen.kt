package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.audit.ActionType
import com.example.gamifiedroadsafetyawareness.audit.AuditManager
import com.example.gamifiedroadsafetyawareness.audit.AuditResult
import com.example.gamifiedroadsafetyawareness.audit.Module as AuditModule
import com.example.gamifiedroadsafetyawareness.audit.RiskLevel
import com.example.gamifiedroadsafetyawareness.model.GamificationConstants
import com.example.gamifiedroadsafetyawareness.model.LearningModule
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed

@Composable
fun AdminModuleManagementScreen(
    currentAdminUsername: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val auditManager = remember { AuditManager(context) }
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val modules = remember { MockData.learningModules.sortedBy { it.moduleType.ordinal } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Module Management",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Control which modules & quizzes are available to learners",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(modules, key = { it.id }) { module ->
            key(refreshTrigger) {
                ModuleManagementCard(
                    module = module,
                    onToggle = { enabled ->
                        GamificationConstants.ContentSettings.setModuleEnabled(module.id, enabled)
                        com.example.gamifiedroadsafetyawareness.firebase.FirebaseSyncManager.getInstance()
                            .syncModuleSetting(module.id, enabled)
                        auditManager.logAction(
                            userId = currentAdminUsername,
                            fullName = currentAdminUsername,
                            username = currentAdminUsername,
                            role = "ADMIN",
                            actionType = ActionType.RECORD_EDITED,
                            module = AuditModule.CONTENT_DATA,
                            description = "${if (enabled) "Enabled" else "Disabled"} module: ${module.title}",
                            previousValue = (!enabled).toString(),
                            newValue = enabled.toString(),
                            result = AuditResult.SUCCESS,
                            riskLevel = RiskLevel.MEDIUM
                        )
                        refreshTrigger++
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun ModuleManagementCard(module: LearningModule, onToggle: (Boolean) -> Unit) {
    var enabled by remember(module.id) {
        mutableStateOf(GamificationConstants.ContentSettings.isModuleEnabled(module.id))
    }
    val xpReward = remember(module.id) { GamificationConstants.ModuleXp.getModuleXp(module.id) }

    AppCard(modifier = Modifier.fillMaxWidth(), elevation = 2) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        enabled = it
                        onToggle(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = module.moduleType.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+$xpReward XP".uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!enabled) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TrafficRed.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "UNAVAILABLE TO LEARNERS",
                            style = MaterialTheme.typography.labelSmall,
                            color = TrafficRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
