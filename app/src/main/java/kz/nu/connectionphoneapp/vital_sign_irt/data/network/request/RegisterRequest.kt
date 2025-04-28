package kz.nu.connectionphoneapp.vital_sign_irt.data.network.request

data class RegisterRequest(
    val token: String,
    val type: String = "caregiver"
)
