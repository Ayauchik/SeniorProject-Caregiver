package kz.nu.connectionphoneapp.vital_sign_irt.notification

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.yourapp.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.nu.connectionphoneapp.MainActivity
import kz.nu.connectionphoneapp.R
import kz.nu.connectionphoneapp.ui.theme.ConnectionPhoneAppTheme
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.FeedbackApi
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.FeedbackRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.SimulationAPI
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ReportX
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.ECGGraph
import org.koin.core.component.KoinComponent
import org.koin.android.ext.android.inject

class AlarmActivity(

) : ComponentActivity(), KoinComponent {

    // private lateinit var api: SimulationAPI
    private val api: SimulationAPI by inject()
    private val feedbackApi: FeedbackApi by inject()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var vibrator: Vibrator

    private var isLoading by mutableStateOf(true)
    private var errorState by mutableStateOf<String?>(null)
    private var yAxisValues by mutableStateOf<List<Double>>(emptyList())
    private var reportX by mutableStateOf<ReportX?>(null)
    private var currentAlertType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val titleText = findViewById<TextView>(R.id.alarmTitle)
        val bodyText = findViewById<TextView>(R.id.alarmBody)
        //   val dismissButton = findViewById<Button>(R.id.dismissButton)
        val anomalyButton = findViewById<Button>(R.id.anomalyButton)
        val notAnomalyButton = findViewById<Button>(R.id.notAnomalyButton)
        val watchCameraButton = findViewById<Button>(R.id.watchCameraButton)
        val ecgComposeView = findViewById<ComposeView>(R.id.ecgComposeView)

        // Retrieve text from intent
        titleText.text = intent.getStringExtra("title") ?: "🚨 Alert!"
        bodyText.text = intent.getStringExtra("body") ?: "Anomaly detected!"


        val anomalyId = intent.getStringExtra("anomaly_id")
        Log.d("AlarmActivity", "Received anomaly ID: $anomalyId")

        currentAlertType = intent.getStringExtra("alertType")
        Log.d("AlarmActivity", "Received alert type: $currentAlertType")
        val showFeedbackButtons =
            currentAlertType == "caregiver_alert_user_timeout" || currentAlertType == "caregiver_alert_meta_confirm"
        Log.d("AlarmActivity", "Show feedback buttons: $showFeedbackButtons")

        anomalyButton.visibility = if (showFeedbackButtons) View.VISIBLE else View.GONE
        notAnomalyButton.visibility = if (showFeedbackButtons) View.VISIBLE else View.GONE

        if (anomalyId != null && currentAlertType != "manual_sos") {
            fetchEventData(anomalyId) { result -> // Replace with a real ID or pass via Intent
                result.onSuccess { report ->
                    isLoading = false // Stop loading once result is available
                    errorState = null
                    yAxisValues = report.ecg_data!!
                    reportX = report
                    Log.d(
                        "AlarmActivity",
                        "ECG Data fetched successfully. Points: ${yAxisValues.size}"
                    )

                }.onFailure { throwable ->
                    Log.e(
                        "TestComposeActivity",
                        "API Error: ${throwable.message}",
                        throwable
                    )
                    isLoading = false
                    errorState = throwable.localizedMessage ?: "An unknown error occurred"
                }
            }
        }



        ecgComposeView.setContent {
            ConnectionPhoneAppTheme {
                if (anomalyId != null && currentAlertType != "manual_sos") {
                    Column(
                        modifier = androidx.compose.ui.Modifier,
                        // horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Top HAR:")
//                        Spacer(modifier = Modfiier.size(4.dp))
                        reportX?.top_har?.forEach {
                            Text("- ${it.activity}: ${"%.3f".format(it.probability)}")
                        }
                        ECGGraph(yAxisValues)
                    }

                } else {
                    Text("")
                }
            }
        }


        // Play alarm sound
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        // Vibrate the phone
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(5000)
        }

        anomalyButton.setOnClickListener {
            if (anomalyId != null) {
                sendFeedbackAndFinish(anomalyId, 1) // Send 1 for Anomaly
            } else {
                Log.w("AlarmActivity", "Anomaly ID is null, cannot send feedback.")
                finish() // Still finish the activity
                // }
            }
        }

        notAnomalyButton.setOnClickListener {
            if (anomalyId != null) {
                sendFeedbackAndFinish(anomalyId, 0) // Send 0 for Not Anomaly
            } else {
                Log.w("AlarmActivity", "Anomaly ID is null, cannot send feedback.")
                finish() // Still finish the activity
            }
        }

        // Stop alarm when button is pressed
        watchCameraButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            vibrator.cancel()


            // Construct the correct route based on alertType
            val webRtcRoute = if (currentAlertType == "manual_sos") {
                // For SOS, use the helper that doesn't require anomalyId
                Screen.WebRTC.createRouteForSos(currentAlertType!!) // Pass alertType
            } else if (anomalyId != null) {
                // For other alerts with an ID, use the standard helper
                Screen.WebRTC.createRoute(anomalyId, currentAlertType)
            } else {
                // Fallback or error case - maybe navigate to a default state?
                // For now, let's log an error and potentially just show the WebRTC screen without params
                Log.e(
                    "AlarmActivity",
                    "Cannot create WebRTC route: anomalyId is null for non-SOS alert."
                )
                "webrtc" // Navigate to base route - WebRtcScreen should handle nulls
            }

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("navigate_to", webRtcRoute) // Pass the fully constructed route string
            }

            startActivity(intent)

            finish() // Close the activity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        vibrator.cancel()
    }

    private fun sendFeedbackAndFinish(anomalyId: String, label: Int) {
        Log.d("AlarmActivity", "Sending feedback: ID=$anomalyId, Label=$label")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()

        // Launch API call in the background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = feedbackApi.sendFeedback(
                    FeedbackRequest(anomaly_id = anomalyId, real_label = label)
                )
                if (response.isSuccessful) {
                    Log.d("AlarmActivity", "✅ Feedback sent successfully from AlarmActivity")
                } else {
                    Log.e("AlarmActivity", "❌ Server error sending feedback: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AlarmActivity", "❌ Feedback failed from AlarmActivity: ${e.message}")
            }
        }

        finish() // Close the activity immediately after initiating the feedback call
    }

    private fun fetchEventData(anomalyId: String, callback: (Result<ReportX>) -> Unit) {
        Log.d("AlarmActivity", "fetchEventData called with ID: $anomalyId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("AlarmActivity", "Attempting API call to getEventByAnomalyId($anomalyId)")
                val report = api.getEventByAnomalyId(anomalyId)
                withContext(Dispatchers.Main) {
                    Log.d("AlarmActivity", "API call successful for $anomalyId")
                    callback(Result.success(report))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(Result.failure(e))
                }
            }
        }
    }
}
