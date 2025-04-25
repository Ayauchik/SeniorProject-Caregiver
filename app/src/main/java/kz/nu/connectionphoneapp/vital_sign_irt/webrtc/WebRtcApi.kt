package kz.nu.connectionphoneapp.vital_sign_irt.webrtc

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// API Service interface
interface WebRtcApi {
    @POST("offer")
    suspend fun sendOffer(@Body offer: SessionDescription): Response<SessionDescription>
}
