// FeedbackApi.kt
package kz.nu.connectionphoneapp.vital_sign_irt.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class FeedbackRequest(
    val anomaly_id: String,
    val real_label: Int
)

interface FeedbackApi {
    @POST("feedback")
    suspend fun sendFeedback(@Body request: FeedbackRequest): Response<Unit>
}
