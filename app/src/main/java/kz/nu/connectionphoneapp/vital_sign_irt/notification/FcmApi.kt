package kz.nu.connectionphoneapp.vital_sign_irt.notification

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ApiResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.MessageRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("register")
    suspend fun registerUser(@Body request: RegisterRequest): ApiResponse

    @POST("send_message")
    suspend fun sendMessageBack(@Body request: MessageRequest): ApiResponse

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )

    @POST("send-notification")
    suspend fun sendNotification(@Body request: NotificationRequest)

}