package kz.nu.connectionphoneapp.vital_sign_irt.webrtc

import com.google.gson.annotations.SerializedName

// Data models for WebRTC signaling
data class SessionDescription(
    @SerializedName("sdp") val sdp: String,
    @SerializedName("type") val type: String
)
