package kz.nu.connectionphoneapp.vital_sign_irt.domain

interface GlobalRepository {
    suspend fun getLastVitalSigns(): VitalSignModel
    suspend fun getAllReports(): ReportModel
    suspend fun getReportsByDate(fromDate: String, toDate: String): ReportModel
    suspend fun getTodayReport(today: String): ReportModel

    suspend fun getLastReport(): ReportModel
    suspend fun sendToken(token: String): Result<String>
    //suspend fun registerUser(userId: String, token: String): String
    //suspend fun sendMessage(userId: String, message: String): String
}