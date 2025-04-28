package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kz.nu.connectionphoneapp.vital_sign_irt.webrtc.WebRtcSurfaceView
import org.koin.androidx.compose.koinViewModel


@Composable
fun WebRtcScreen(
    viewModel: WebRtcViewModel = koinViewModel(),
    anomalyId: String?,
    alertType: String?
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isFeedbackSent by viewModel.isFeedbackSent.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()
    val videoTrack by viewModel.videoTrack.collectAsState()


    val lifecycleOwner = LocalLifecycleOwner.current

    Log.e("WebRtcScreen", "anomalyId: $anomalyId, alertType: $alertType")
    val showFeedbackButtons = !anomalyId.isNullOrBlank() &&
            (alertType == "caregiver_alert_user_timeout" || alertType == "caregiver_alert_meta_confirm")
    Log.e("WebRtcScreen", "showFeedbackButtons: $showFeedbackButtons")


    // Observe lifecycle for cleanup
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.disconnect()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = anomalyId) {
        if (!anomalyId.isNullOrBlank()) {
            viewModel.connect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Raspberry Pi WebRTC Stream",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        // Video View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .weight(1f)
        ) {
            // WebRTC Surface Renderer
            AndroidView(
                factory = { ctx ->
                    WebRtcSurfaceView(ctx).apply {
                        initialize(viewModel.eglContext)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    videoTrack?.addSink(view)
                    val currentTrack = videoTrack
                    if (currentTrack != null) {
                        currentTrack.addSink(view)
                    } else {

                    }
                }
            )

            // Connection status overlay
            if (isLoading || !isConnected) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Connecting...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            Text(
                                text = errorMessage ?: "Not connected",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Connection controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            if (showFeedbackButtons) {
                // Show Anomaly / Not Anomaly buttons
                Button(
                    onClick = {
                        viewModel.sendFeedback(anomalyId!!, realLabel = 1)
//                        if (anomalyId != null) {
//                            viewModel.sendFeedback(anomalyId, realLabel = 1)
//                        } // 1 for Anomaly
                    },
                    enabled = isConnected && !isFeedbackSent,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Anomaly")
                }

                Button(
                    onClick = {
                        viewModel.sendFeedback(anomalyId!!, realLabel = 0)

                    },
                    enabled = (isConnected && !isFeedbackSent),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Not Anomaly")
                }
            } else {
                // Default Connect / Disconnect buttons
                Button(
                    onClick = { viewModel.connect() },
                    enabled = !isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Connect")
                }

                Button(
                    onClick = { viewModel.disconnect() },
                    enabled = isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Disconnect")
                }
            }
        }
    }
}
