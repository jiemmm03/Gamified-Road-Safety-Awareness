package com.example.gamifiedroadsafetyawareness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.LeaderboardEntry
import com.example.gamifiedroadsafetyawareness.model.MockData
import com.example.gamifiedroadsafetyawareness.model.BadgeItem

// Define colors
private val DarkBackground = Color(0xFF0B0E1A)
private val CardDarkGlass = Color(0xFF141829)
private val CardBorder = Color(0xFF2A2F45)
private val TextPrimary = Color(0xFFF0F0F5)
private val TextSecondary = Color(0xFF8E93A6)
private val Amber = Color(0xFFFFD740)
private val AmberDark = Color(0xFFFF8F00)
private val Emerald = Color(0xFF00E676)
private val Cyan = Color(0xFF00E5FF)
private val GlassBg = Color(0xFF1A1F35)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamificationScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedFilter by remember { mutableStateOf("Global") }
    
    val tabs = listOf("Leaderboard", "Badges", "Challenges")
    val filters = listOf("Global", "Friends", "Campus", "City")

    Scaffold(
        containerColor = DarkBackground,
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Rankings & Rewards", color = TextPrimary, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                
                // Top Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(Amber, AmberDark)))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Current Rank: Diamond League", color = Color(0xFF1A1F35), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Top 5% of Safe Riders this week!", color = Color(0xFF1A1F35).copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
                
                // Tab Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Amber else GlassBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else CardBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedTab = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color(0xFF1A1F35) else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedTab == 0) {
                // Filters
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filters.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Cyan.copy(alpha = 0.2f) else GlassBg)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Cyan else CardBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedFilter = filter }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filter,
                                    color = if (isSelected) Cyan else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                // Leaderboard List
                items(MockData.leaderboardEntries) { entry ->
                    LeaderboardRow(entry = entry)
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Invite */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF1A1F35), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Invite Friends", color = Color(0xFF1A1F35), fontWeight = FontWeight.Bold)
                    }
                }
            } else if (selectedTab == 1) {
                // Badges
                items(MockData.badges) { badge ->
                    BadgeRow(badge)
                }
            } else {
                // Challenges
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Emerald.copy(alpha = 0.08f))
                            .border(1.dp, Emerald, RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Emerald, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("University Challenge", color = Emerald, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Maintain 95% compliance rate for 5 days.", color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Reward: Free campus parking pass for a week", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            LinearProgressIndicator(
                                progress = { 0.6f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Emerald,
                                trackColor = Emerald.copy(alpha = 0.2f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("3/5 Days Completed", color = Emerald, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {
    val isCurrentUser = entry.isCurrentUser
    val bgColor = if (isCurrentUser) Amber.copy(alpha = 0.08f) else CardDarkGlass
    val borderColor = if (isCurrentUser) Amber else CardBorder
    
    val rankColor = when (entry.rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> TextSecondary
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (entry.rank <= 3) rankColor.copy(alpha = 0.2f) else GlassBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${entry.rank}",
                    color = rankColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            // Name and Level
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name,
                    color = if (isCurrentUser) Amber else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Lvl ${entry.level} • ${entry.leagueTag}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            
            // Stats
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.xp} XP",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${entry.complianceRate}% Safe",
                    color = Emerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun BadgeRow(badge: BadgeItem) {
    val borderColor = if (badge.isUnlocked) Emerald.copy(alpha = 0.3f) else CardBorder
    val opacity = if (badge.isUnlocked) 1f else 0.5f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardDarkGlass)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = badge.icon,
                fontSize = 32.sp,
                modifier = Modifier.alpha(opacity)
            )
            
            Column(modifier = Modifier.weight(1f).alpha(opacity)) {
                Text(
                    text = badge.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = badge.description,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = badge.progressText ?: "",
                    color = if (badge.isUnlocked) Emerald else Cyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
