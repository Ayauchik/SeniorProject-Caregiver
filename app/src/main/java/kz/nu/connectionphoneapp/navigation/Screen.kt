package com.example.yourapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object VitalSigns : Screen("vital_signs", "Vital Signs", Icons.Default.Favorite)
    object WebRTC : Screen("webrtc?anomaly_id={anomaly_id}&alertType={alertType}", "WebRTC", Icons.Default.Call){
        fun createRoute(anomalyId: String, alertType: String?) =
            "webrtc?anomaly_id=$anomalyId" + (alertType?.let { "&alertType=$it" } ?: "")

        fun createRouteForSos(alertType: String) = "webrtc?alertType=$alertType" // Keep this too
    }


    object Report : Screen("report", "Report", Icons.Filled.Info)
    object Notification : Screen("notification", "Notification", Icons.Filled.Info)
}

val webRtcArguments = listOf(
    navArgument("anomaly_id") { type = NavType.StringType; nullable = true }, // Default is null if not present
    navArgument("alertType") {
        type = NavType.StringType; nullable = true; defaultValue = null
    } // Add alertType arg
)