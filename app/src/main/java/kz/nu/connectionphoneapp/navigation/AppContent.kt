package kz.nu.connectionphoneapp.navigation

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
import kz.nu.connectionphoneapp.vital_sign_irt.notification.NotificationScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.VitalSignsScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.WebRtcScreen
import kz.nu.connectionphoneapp.vital_sign_irt.report.ECGBroadcastDashboard
import kz.nu.connectionphoneapp.vital_sign_irt.report.ReportScreen
import kz.nu.connectionphoneapp.vital_sign_irt.report.ecg.EcgChartScreen

@Composable
fun AppContent(navigateTo: String? = null, anomalyId: String? = null) {
    val navController = rememberNavController()
    val screens = listOf(Screen.VitalSigns, Screen.WebRTC, Screen.Report)

    LaunchedEffect(navigateTo) {
        if (navigateTo != null) {
            navController.navigate(navigateTo)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
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
                //ECGBroadcastDashboard()
            //val context = LocalContext.current
                //EcgChartScreen(context)
            //ReportScreen()
               VitalSignsScreen()
            }
            composable(Screen.WebRTC.route) {
               // val anomalyId = it.arguments?.getString("anomaly_id")
                WebRtcScreen(anomalyId = anomalyId)
                //NotificationScreen()
            }
            composable(Screen.Report.route){
                ReportScreen()
            }
        }
    }
}
