package kz.nu.connectionphoneapp.vital_sign_irt.domain

data class VitalSignModel(
    val timestamp: String,
    val heartRate: Int,
    val temperature: Double,
    val spo2: Int,
    val age: Int,
    val gender: Int,
)
