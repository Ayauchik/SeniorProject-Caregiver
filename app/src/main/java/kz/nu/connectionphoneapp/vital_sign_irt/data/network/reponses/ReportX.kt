package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

data class ReportX(
    val anomaly_id: String,
    val decision_source: String,
    val feedback_received: Boolean,
    val final_outcome: Int,
    val meta_prediction: Int,
    val sensor_prediction: Int,
    val timestamp: String,
    val top_har: List<TopHar>,
    val ecg_data: List<Double>
)