package kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses

import com.google.gson.annotations.SerializedName

data class GetLastVitalSignsResponse(
    @SerializedName("last_sensor_data")
    val lastSensorData: List<LastSensorData>
)