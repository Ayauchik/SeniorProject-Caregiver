package kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.domain.ReportModel

class ReportMapper {
    fun fromRemoteToDomain(response: GetReportResponse): ReportModel {
        return ReportModel(
            reports = response.report.map{
                ReportModel.ReportItem(
                    timestamp = it.timestamp,
                    heartRate = it.heartRate,
                    temperature = it.temperature,
                    spo2 = it.spo2,
                    age = it.age,
                    gender = it.gender,
                    outcome = it.outcome,
                    predicted = it.predicted
                )
            }
        )
    }

    fun fromRemoteLastToDomain(response: GetLastReportResponse): ReportModel{
        return ReportModel(
            reports = response.lastPredicted.map{
                ReportModel.ReportItem(
                    timestamp = it.timestamp,
                    heartRate = it.heartRate,
                    temperature = it.temperature,
                    spo2 = it.spo2,
                    age = it.age,
                    gender = it.gender,
                    outcome = it.outcome,
                    predicted = it.predicted
                )
            }
        )
    }
}
