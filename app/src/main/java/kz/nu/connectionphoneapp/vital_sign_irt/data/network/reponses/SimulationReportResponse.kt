package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class SimulationReportResponse(
    @SerializedName("report")
    val report: List<ReportX?>
)