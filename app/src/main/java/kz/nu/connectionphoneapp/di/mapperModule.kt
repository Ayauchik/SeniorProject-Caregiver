package kz.nu.connectionphoneapp.di

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper.ReportMapper
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.mapper.VitalSignMapper
import org.koin.dsl.module

val mapperModule = module{
    factory { VitalSignMapper() }
    factory { ReportMapper() }
}