package kz.nu.connectionphoneapp.vital_sign_irt.domain

interface GetLastVitalSignsUseCase {
    suspend fun invoke(): VitalSignModel
}