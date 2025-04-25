package kz.nu.connectionphoneapp.di

import kz.nu.connectionphoneapp.vital_sign_irt.data.GetLastVitalSignsUseCaseImpl
import kz.nu.connectionphoneapp.vital_sign_irt.data.GetReportsUseCaseImpl
import kz.nu.connectionphoneapp.vital_sign_irt.data.SendTokenUseCaseImpl
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetLastVitalSignsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetReportsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.SendTokenUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetLastVitalSignsUseCase> { GetLastVitalSignsUseCaseImpl(get()) }
    factory<GetReportsUseCase> { GetReportsUseCaseImpl(get()) }
    factory<SendTokenUseCase> { SendTokenUseCaseImpl(get()) }
}