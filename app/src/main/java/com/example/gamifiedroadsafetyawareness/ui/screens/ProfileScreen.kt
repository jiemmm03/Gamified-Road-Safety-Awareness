package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gamifiedroadsafetyawareness.auth.AuthManager
import com.example.gamifiedroadsafetyawareness.auth.UserRole
import com.example.gamifiedroadsafetyawareness.ui.components.AppCard
import com.example.gamifiedroadsafetyawareness.ui.theme.AppTypeScale
import com.example.gamifiedroadsafetyawareness.ui.theme.EmeraldGreen
import com.example.gamifiedroadsafetyawareness.ui.theme.TrafficRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    displayName: String,
    username: String,
    role: UserRole,
    memberSince: Long,
    authManager: AuthManager? = null,
    onDisplayNameChanged: (String) -> Unit = {},
    onNavigateToAccountSecurity: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingName by remember(displayName) { mutableStateOf(false) }
    var nameEditValue by remember(displayName) { mutableStateOf(displayName) }
    var nameEditError by remember(displayName) { mutableStateOf<String?>(null) }

    val dateStr = if (memberSince > 0) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(memberSince))
    } else {
        "Unknown"
    }

    val roleColor = if (role == UserRole.ADMIN) MaterialTheme.colorScheme.secondary else EmeraldGreen
    val roleBgColor = if (role == UserRole.ADMIN) MaterialTheme.colorScheme.secondaryContainer else EmeraldGreen.copy(alpha = 0.1f)
    val initials = displayName.trim().split(" ").filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifBlank { "?" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar
        Box(
            modifier = Modifier
                .size(88.dp)
                .shadow(elevation = 8.dp, shape = CircleShape, spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditingName) {
            OutlinedTextField(
                value = nameEditValue,
                onValueChange = { nameEditValue = it; nameEditError = null },
                singleLine = true,
                isError = nameEditError != null,
                supportingText = nameEditError?.let { err -> { Text(err, color = TrafficRed) } },
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                TextButton(onClick = {
                    val trimmed = nameEditValue.trim()
                    when {
                        trimmed.isBlank() -> nameEditError = "Display name can't be empty"
                        authManager == null -> nameEditError = "Unable to update right now"
                        else -> {
                            val ok = authManager.updateDisplayName(username, trimmed)
                            if (ok) {
                                onDisplayNameChanged(trimmed)
                                isEditingName = false
                            } else {
                                nameEditError = "Unable to update display name"
                            }
                        }
                    }
                }) { Text("Save") }
                TextButton(onClick = { isEditingName = false; nameEditValue = displayName; nameEditError = null }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayName,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (authManager != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = { isEditingName = true }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit display name",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        Text(
            text = "@$username",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Role Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(roleBgColor)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "ROLE: ${role.name}",
                color = roleColor,
                style = AppTypeScale.eyebrowLabel
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Details Card
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "ACCOUNT DETAILS",
                    style = AppTypeScale.eyebrowLabel,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow("Member Since", dateStr)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                DetailRow("Authentication Status", "Verified ✅")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToAccountSecurity),
            elevation = 2
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Account & Security",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Password and sign-out",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateToSettings),
            elevation = 2
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Language, preferences, and support",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}
