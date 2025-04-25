package kz.nu.connectionphoneapp.vital_sign_irt.data.network

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ApiResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastVitalSignsResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetReportResponse
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.TokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GlobalAPI {

    @GET("/vital_signs")
    suspend fun getLastVitalSigns(): GetLastVitalSignsResponse

    @GET("report")
    suspend fun getAllReports(): GetReportResponse

    @GET("report")
    suspend fun getReportsByDate(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String
    ): GetReportResponse

    @GET("report")
    suspend fun getTodayReport(
        @Query("from_date") fromDate: String
    ): GetReportResponse

    @GET("/last_predicted")
    suspend fun getLastReport(): GetLastReportResponse

    @POST("/register")
    suspend fun registerToken(@Body request: TokenRequest): Response<ApiResponse>

}