package kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.GetLastVitalSignsResponse
import kz.nu.connectionphoneapp.vital_sign_irt.domain.VitalSignModel

class VitalSignMapper {
    fun fromRemoteToDomain(getLastVitalSignsResponse: GetLastVitalSignsResponse): VitalSignModel {
        return VitalSignModel(
            timestamp = getLastVitalSignsResponse.lastSensorData[0].timestamp,
            heartRate = getLastVitalSignsResponse.lastSensorData[0].heartRate,
            temperature = getLastVitalSignsResponse.lastSensorData[0].temperature,
            spo2 = getLastVitalSignsResponse.lastSensorData[0].spo2,
            age = getLastVitalSignsResponse.lastSensorData[0].age,
            gender = getLastVitalSignsResponse.lastSensorData[0].gender,
        )
    }
}