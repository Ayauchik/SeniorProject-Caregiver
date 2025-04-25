package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class LastSensorData(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("HR")
    val heartRate: Int,
    @SerializedName("BT")
    val temperature: Double,
    @SerializedName("SpO2")
    val spo2: Int,
    @SerializedName("Age")
    val age: Int,
    @SerializedName("Gender")
    val gender: Int,
)