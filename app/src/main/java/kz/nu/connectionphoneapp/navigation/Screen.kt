package com.example.yourapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object VitalSigns : Screen("vital_signs", "Vital Signs", Icons.Default.Favorite)
    object WebRTC : Screen("webrtc/{anomaly_id}", "WebRTC", Icons.Default.Call)
    object Report: Screen("report", "Report", Icons.Filled.Info)
}
