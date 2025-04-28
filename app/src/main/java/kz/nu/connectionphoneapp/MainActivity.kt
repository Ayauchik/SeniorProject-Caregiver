package kz.nu.connectionphoneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kz.nu.connectionphoneapp.ui.theme.ConnectionPhoneAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kz.nu.connectionphoneapp.navigation.AppContent
import kz.nu.connectionphoneapp.vital_sign_irt.notification.NotificationScreen
import kz.nu.connectionphoneapp.vital_sign_irt.notification.NotificationViewModel
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.VitalSignsScreen
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.WebRtcScreen
import org.webrtc.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        val navigateTo = intent.getStringExtra("navigate_to")
        val anomalyId = intent.getStringExtra("anomaly_id")
        val alertType = intent.getStringExtra("alertType")


        setContent {
            ConnectionPhoneAppTheme {
                AppContent(navigateTo = navigateTo, anomalyId = anomalyId, alertType = alertType)
            }
        }
    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}

