package kz.nu.connectionphoneapp.vital_sign_irt.data.network

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ApiResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastVitalSignsResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ReportX
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.SimulationReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.RegisterRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.TokenRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SimulationAPI {

    @GET("report")
    suspend fun getEcgReport(
        @Query("include_ecg") includeEcg: Boolean = true
    ): SimulationReportResponse


    @GET("event/{anomaly_id}") // <-- Use placeholder in the path
    suspend fun getEventByAnomalyId(@Path("anomaly_id") anomalyId: String): ReportX

}