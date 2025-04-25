package kz.nu.connectionphoneapp.vital_sign_irt.data.network

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ApiResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.MessageRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationAPI {
    @POST("/register")
    suspend fun registerUser(@Body request: RegisterRequest): ApiResponse

    @POST("/send_message")
    suspend fun sendMessageBack(@Body request: MessageRequest): ApiResponse
}