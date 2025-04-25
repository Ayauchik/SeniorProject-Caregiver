package kz.nu.connectionphoneapp.vital_sign_irt.data

import android.util.Log
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetLastVitalSignsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GlobalRepository
import kz.nu.connectionphoneapp.vital_sign_irt.domain.VitalSignModel

class GetLastVitalSignsUseCaseImpl(
    private val repository: GlobalRepository
) : GetLastVitalSignsUseCase {
    override suspend fun invoke(): VitalSignModel {
        val vitalSigns = repository.getLastVitalSigns()
        //Log.e("use case", "invoke $vitalSigns")
        return vitalSigns
    }
}