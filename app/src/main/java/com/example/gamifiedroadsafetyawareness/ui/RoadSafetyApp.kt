package com.example.gamifiedroadsafetyawareness.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamifiedroadsafetyawareness.model.DecisionOption
import com.example.gamifiedroadsafetyawareness.ui.screens.AnalyticsScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.DashboardScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.FeedbackScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.GamificationScreen
import com.example.gamifiedroadsafetyawareness.ui.screens.SimulationScreen

sealed class Screen(val route: String, val title: String, val icon: String) {
    object Dashboard : Screen("dashboard", "Dashboard", "🏠")
    object Simulation : Screen("simulation", "Simulation", "🎮")
    object Feedback : Screen("feedback", "Feedback", "🤖")
    object Gamification : Screen("gamification", "Gamification", "🏆")
    object Analytics : Screen("analytics", "Analytics", "📊")
}

@Composable
fun RoadSafetyApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    var lastSubmittedOption by remember { mutableStateOf<DecisionOption?>(null) }
    val context = LocalContext.current
    
    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Gamification,
        Screen.Analytics
    )
    
    val showBottomBar = bottomNavItems.contains(currentScreen)
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Column {
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFF2A2F45))
                    NavigationBar(
                        containerColor = Color(0xFF0F1224),
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { screen ->
                            val selected = currentScreen == screen
                            NavigationBarItem(
                                selected = selected,
                                onClick = { currentScreen = screen },
                                icon = {
                                    Text(
                                        text = screen.icon,
                                        fontSize = if (selected) 22.sp else 18.sp
                                    )
                                },
                                label = {
                                    Text(
                                        text = screen.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF00E5FF),
                                    selectedTextColor = Color(0xFF00E5FF),
                                    unselectedIconColor = Color(0xFF8E93A6),
                                    unselectedTextColor = Color(0xFF8E93A6),
                                    indicatorColor = Color(0xFF00E5FF).copy(alpha = 0.12f)
                                )
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFF0B0E1A)
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0B0E1A)
        ) {
            when (currentScreen) {
                Screen.Dashboard -> {
                    DashboardScreen(
                        onLaunchSimulation = { currentScreen = Screen.Simulation },
                        onNavigateToGamification = { currentScreen = Screen.Gamification },
                        onNavigateToAnalytics = { currentScreen = Screen.Analytics },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Simulation -> {
                    SimulationScreen(
                        onSubmitDecision = { option ->
                            lastSubmittedOption = option
                            currentScreen = Screen.Feedback
                        },
                        onBackClick = { currentScreen = Screen.Dashboard },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Feedback -> {
                    FeedbackScreen(
                        selectedOption = lastSubmittedOption,
                        onNextScenario = { 
                            Toast.makeText(context, "Loading next scenario...", Toast.LENGTH_SHORT).show()
                            currentScreen = Screen.Simulation 
                        },
                        onRetry = { currentScreen = Screen.Simulation },
                        onReturnHome = { currentScreen = Screen.Dashboard },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Gamification -> {
                    GamificationScreen(
                        onBackClick = { currentScreen = Screen.Dashboard },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                Screen.Analytics -> {
                    AnalyticsScreen(
                        onBackClick = { currentScreen = Screen.Dashboard },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
