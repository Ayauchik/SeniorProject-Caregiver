package kz.nu.connectionphoneapp.vital_sign_irt.data

import kz.nu.connectionphoneapp.vital_sign_irt.domain.GlobalRepository
import kz.nu.connectionphoneapp.vital_sign_irt.domain.SendTokenUseCase

class SendTokenUseCaseImpl(
    private val globalRepository: GlobalRepository
): SendTokenUseCase  {
    override suspend fun sendToken(token: String): Result<String> {
        return globalRepository.sendToken(token)
    }
}