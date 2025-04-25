package kz.nu.connectionphoneapp.di

import kz.nu.connectionphoneapp.vital_sign_irt.data.GlobalRepositoryImpl
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GlobalRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<GlobalRepository> { GlobalRepositoryImpl(get(), get(), get()) }
}