package kz.nu.connectionphoneapp.di

import kz.nu.connectionphoneapp.vital_sign_irt.notification.NotificationViewModel
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.EcgScreenViewModel
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.VitalSignsViewModel
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.WebRtcViewModel
import org.koin.dsl.module

val viewModelModule = module{
    factory { VitalSignsViewModel(get(), get()) }
    factory { NotificationViewModel() }
    factory { WebRtcViewModel(get(), get()) }
    factory { EcgScreenViewModel(get()) }
}
