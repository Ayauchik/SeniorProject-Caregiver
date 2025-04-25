package kz.nu.connectionphoneapp.vital_sign_irt.domain

interface GetReportsUseCase {

    suspend fun getAllReports(): ReportModel

    suspend fun getReportsByDate(fromDate: String, toDate: String): ReportModel

    suspend fun getTodayReport(today: String): ReportModel

    suspend fun getLastReport(): ReportModel
}
