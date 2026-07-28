package com.example.gamifiedroadsafetyawareness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gamifiedroadsafetyawareness.ui.RoadSafetyApp
import com.example.gamifiedroadsafetyawareness.ui.theme.GamifiedRoadSafetyAwarenessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamifiedRoadSafetyAwarenessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0E1A)
                ) {
                    RoadSafetyApp()
                }
            }
        }
    }
}