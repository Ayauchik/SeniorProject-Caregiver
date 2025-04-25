package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

data class LastPredicted(
    val Age: Int,
    val BT: Double,
    val Gender: Int,
    val HR: Int,
    val Outcome: Int,
    val Predicted: Int,
    val SpO2: Int,
    val timestamp: String
)