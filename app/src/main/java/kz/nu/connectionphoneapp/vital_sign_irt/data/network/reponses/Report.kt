package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("Age")
    val age: Int,
    @SerializedName("BT")
    val temperature: Double,
    @SerializedName("Gender")
    val gender: Int,
    @SerializedName("HR")
    val heartRate: Int,
    @SerializedName("Outcome")
    val outcome: Int,
    @SerializedName("Predicted")
    val predicted: Int,
    @SerializedName("SpO2")
    val spo2: Int,
    @SerializedName("anomaly_id")
    val anomalyId: String,
    @SerializedName("timestamp")
    val timestamp: String
)