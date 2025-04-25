package kz.nu.connectionphoneapp.vital_sign_irt.domain

data class ReportModel(
    val reports: List<ReportItem>
) {
    data class ReportItem(
        val timestamp: String,
        val heartRate: Int,
        val temperature: Double,
        val spo2: Int,
        val age: Int,
        val gender: Int,
        val outcome: Int,
        val predicted: Int
    )
}
