package kz.nu.connectionphoneapp.vital_sign_irt.notification

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)

data class NotificationRequest(
    val userFcmToken: String,
    val message: String
)