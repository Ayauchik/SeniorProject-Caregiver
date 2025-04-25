package kz.nu.connectionphoneapp.vital_sign_irt.data

import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetReportsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GlobalRepository
import kz.nu.connectionphoneapp.vital_sign_irt.domain.ReportModel

class GetReportsUseCaseImpl(
    private val globalRepository: GlobalRepository
) : GetReportsUseCase {
    override suspend fun getAllReports(): ReportModel {
        return globalRepository.getAllReports()
    }

    override suspend fun getReportsByDate(fromDate: String, toDate: String): ReportModel {
        return globalRepository.getReportsByDate(fromDate, toDate)
    }

    override suspend fun getTodayReport(today: String): ReportModel {
        return globalRepository.getTodayReport(today)
    }

    override suspend fun getLastReport(): ReportModel {
        return globalRepository.getLastReport()
    }
}