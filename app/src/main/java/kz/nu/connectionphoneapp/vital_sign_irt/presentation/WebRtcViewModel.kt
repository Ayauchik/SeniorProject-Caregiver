package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.nu.connectionphoneapp.di.Constants
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.FeedbackApi
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.FeedbackRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.NotificationAPI
import kz.nu.connectionphoneapp.vital_sign_irt.webrtc.ConnectionState
import kz.nu.connectionphoneapp.vital_sign_irt.webrtc.WebRtcClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.webrtc.EglBase
import org.webrtc.VideoTrack
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebRtcViewModel(
    application: Application,
    private val feedbackApi: FeedbackApi
) : ViewModel() {

    // private val application = Application()
    // Server URL (update with your Raspberry Pi's IP and port)
    private val serverUrl = Constants.TEST_URL_WEBRTC

    // WebRTC client
    private val webRtcClient = WebRtcClient(application, serverUrl)

    // State flows
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isFeedbackSent = MutableStateFlow(false)
    val isFeedbackSent: StateFlow<Boolean> = _isFeedbackSent

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _videoTrack = MutableStateFlow<VideoTrack?>(null)
    val videoTrack: StateFlow<VideoTrack?> = _videoTrack

    // EGL context for rendering
    val eglContext: EglBase.Context?
        get() = webRtcClient.getEglContext()


    init {
        // Observe connection state
        viewModelScope.launch {
            webRtcClient.connectionState.collectLatest { state ->
                when (state) {
                    ConnectionState.CONNECTED -> {
                        _isConnected.value = true
                        _isLoading.value = false
                        _errorMessage.value = null
                    }

                    ConnectionState.FAILED -> {
                        _isConnected.value = false
                        _isLoading.value = false
                        _errorMessage.value = "Connection failed. Please try again."
                    }

                    ConnectionState.DISCONNECTED -> {
                        _isConnected.value = false
                        _errorMessage.value = null
                        _isLoading.value = false
                    }

                    ConnectionState.CONNECTING -> {
                        _isLoading.value = true
                        _isConnected.value = false
                        _errorMessage.value = null
                    }
                }
            }
        }

        viewModelScope.launch {
            webRtcClient.remoteVideoTrack.collectLatest { track ->
                _videoTrack.value = track
            }
        }
    }

    // Start WebRTC connection
    fun connect() {
        _errorMessage.value = null
        // _isLoading.value = true
        webRtcClient.start()
    }

    // Disconnect
    fun disconnect() {
        webRtcClient.stop()
    }

    // Clean up resources
    override fun onCleared() {
        super.onCleared()
        webRtcClient.release()
    }

    fun sendFeedback(anomalyId: String, realLabel: Int) {
        if (anomalyId.isBlank()) {
            Log.e("WebRtcVM", "Cannot send feedback without an anomaly ID.")
            _errorMessage.value = "Cannot send feedback: Missing Anomaly ID."
            return // Don't proceed
        }
        viewModelScope.launch {
            try {
                val response = feedbackApi.sendFeedback(
                    FeedbackRequest(anomaly_id = anomalyId, real_label = realLabel)
                )
                if (response.isSuccessful) {
                    Log.d("WebRtcVM", "✅ Feedback sent successfully")
                    _isFeedbackSent.value = true
                } else {
                    Log.e("WebRtcVM", "❌ Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("WebRtcVM", "❌ Feedback failed: ${e.message}")
            }
        }
    }
}