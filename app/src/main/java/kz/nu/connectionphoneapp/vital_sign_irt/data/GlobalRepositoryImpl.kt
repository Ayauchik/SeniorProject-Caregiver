package kz.nu.connectionphoneapp.vital_sign_irt.data

import android.util.Log
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.GlobalAPI
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper.ReportMapper
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GlobalRepository
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper.VitalSignMapper
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.TokenRequest
import kz.nu.connectionphoneapp.vital_sign_irt.domain.ReportModel
import kz.nu.connectionphoneapp.vital_sign_irt.domain.VitalSignModel
import retrofit2.HttpException

class GlobalRepositoryImpl(
    private val api: GlobalAPI,
    private val mapper: VitalSignMapper,
    private val reportMapper: ReportMapper
) : GlobalRepository {
    override suspend fun getLastVitalSigns(): VitalSignModel {
        val response = api.getLastVitalSigns()
      //  Log.e("repository", "getLastVitalSigns response $response")
        val vitalSigns = mapper.fromRemoteToDomain(response)
       // Log.e("repository", "getLastVitalSigns vitalSigns $vitalSigns")
        return vitalSigns
    }

    override suspend fun getAllReports(): ReportModel {
        val response = api.getAllReports()
        //Log.e("repository", "getAllReports response $response")
        return reportMapper.fromRemoteToDomain(response)
    }

    override suspend fun getReportsByDate(fromDate: String, toDate: String): ReportModel {
        val response = api.getReportsByDate(fromDate, toDate)
        //Log.e("repository", "getReportsByDate response $response")
        return reportMapper.fromRemoteToDomain(response)
    }

    override suspend fun getTodayReport(today: String): ReportModel {
        val response = api.getTodayReport(today)
        //Log.e("repository", "getTodayReport response $response")
        return reportMapper.fromRemoteToDomain(response)
    }

    override suspend fun getLastReport(): ReportModel {
        val response = api.getLastReport()
      //  Log.e("repository", "getLastReport response $response")
        return reportMapper.fromRemoteLastToDomain(response)
    }

    override suspend fun sendToken(token: String): Result<String> {
        return try {
            val response = api.registerToken(TokenRequest(token))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("API", "Response: ${body.message}")
                    Result.success(body.message)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Log.e("API", "Failed: ${response.code()} ${response.message()}")
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Log.e("API", "Error: ${e.message}")
            Result.failure(e)
        }

    }
}