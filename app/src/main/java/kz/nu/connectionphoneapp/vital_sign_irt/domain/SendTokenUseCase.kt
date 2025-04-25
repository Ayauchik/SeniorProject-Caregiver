package kz.nu.connectionphoneapp.vital_sign_irt.domain

interface SendTokenUseCase {
    suspend fun sendToken(token:String): Result<String>
}