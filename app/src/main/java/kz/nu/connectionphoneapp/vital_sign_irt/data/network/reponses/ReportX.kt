package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class ReportX(
    @SerializedName("anomaly_id")
    val anomaly_id: String,
    @SerializedName("decision_source")
    val decision_source: String,
    @SerializedName("feedback_received")
    val feedback_received: Boolean,
    @SerializedName("final_outcome")
    val final_outcome: Int,
    @SerializedName("meta_prediction")
    val meta_prediction: Int,
    @SerializedName("sensor_prediction")
    val sensor_prediction: Int,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("top_har")
    val top_har: List<TopHar>,
    @SerializedName("ecg_data")
    val ecg_data: List<Double>?,
    @SerializedName("user_responded")
    val user_responded: Boolean

)