package kz.nu.connectionphoneapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yourapp.Screen
import com.example.yourapp.webRtcArguments
import kz.nu.connectionphoneapp.vital_sign_irt.notification.NotificationScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.ECGScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.PreviewReportCard
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.VitalSignsScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.WebRtcScreen
import kz.nu.connectionphoneapp.vital_sign_irt.report.ECGBroadcastDashboard
import kz.nu.connectionphoneapp.vital_sign_irt.report.ReportScreen
import kz.nu.connectionphoneapp.vital_sign_irt.report.ecg.EcgChartScreen

@Composable
fun AppContent(navigateTo: String? = null, anomalyId: String? = null, alertType: String? = null) {
    val navController = rememberNavController()
    val screens = listOf(Screen.VitalSigns, Screen.WebRTC, Screen.Report)
    val currentAlertType = remember { mutableStateOf<String?>(alertType) }

    LaunchedEffect(navigateTo, anomalyId, alertType) {
        if (navigateTo != null && navigateTo.startsWith("webrtc")) { // Check if it's a WebRTC route
            Log.d("AppContent", "Navigating to constructed route from Intent: $navigateTo")
            navController.navigate(navigateTo) { // Navigate using the full route from Intent
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            // Optional: Handle navigation to other initial routes if needed
            // else if (navigateRoutePattern != null) { ... }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                screens.forEach { screen ->
                    // Determine the base route for comparison (part before '?')
                    val baseRoute = screen.route.substringBefore("?")
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        // Select if the current route starts with the screen's base route
                        selected = currentRoute?.startsWith(baseRoute) ?: false,
                        onClick = {
                            // Navigate to the base route when clicking bottom bar items
                            if (currentRoute?.startsWith(baseRoute) != true) {
                                navController.navigate(baseRoute) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.VitalSigns.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.VitalSigns.route) {
                ECGScreen()
            }
            composable(
                route = Screen.WebRTC.route,// Use the route definition with optional param
                arguments = webRtcArguments // Use the defined arguments list
            ) { backStackEntry ->
                val anomalyIdArg: String? = backStackEntry.arguments?.getString("anomaly_id")
                val alertTypeArg: String? = backStackEntry.arguments?.getString("alertType")

                if (anomalyIdArg == null) {
                    Log.d("AppContent", "Anomaly ID is null")
                    return@composable
                }
                Log.d(
                    "AppContent",
                    "Displaying WebRTC Screen. AnomalyID: $anomalyIdArg, AlertType: $alertTypeArg"
                )
                WebRtcScreen(
                    anomalyId = anomalyIdArg,
                    alertType = alertTypeArg
                ) // Pass alertType
            }

//            composable(route = Screen.WebRTC.route, arguments = webRtcArguments) { backStackEntry ->
//
//                val anomalyId = backStackEntry.arguments?.getString("anomaly_id")
//                WebRtcScreen(anomalyId = anomalyId)
//            }
            composable(Screen.Report.route) {
                ReportScreen()
            }
//            composable(Screen.Notification.route){
//               // NotificationScreen()
//            }
        }
    }
}
