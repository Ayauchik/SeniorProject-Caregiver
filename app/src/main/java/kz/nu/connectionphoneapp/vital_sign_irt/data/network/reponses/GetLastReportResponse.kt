package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class GetLastReportResponse(
    @SerializedName("last_predicted")
    val lastPredicted: List<Report>
)