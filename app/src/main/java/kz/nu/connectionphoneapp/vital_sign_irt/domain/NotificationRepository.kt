package kz.nu.connectionphoneapp.vital_sign_irt.domain

interface NotificationRepository {
    suspend fun registerUser(userId: String, token: String): String
    suspend fun sendMessage(userId: String, message: String): String
}