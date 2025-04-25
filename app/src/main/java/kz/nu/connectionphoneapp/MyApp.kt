package kz.nu.connectionphoneapp

import android.app.Application
import com.google.firebase.FirebaseApp
import kz.nu.connectionphoneapp.di.Constants
import kz.nu.connectionphoneapp.di.mapperModule
import kz.nu.connectionphoneapp.di.networkModule
import kz.nu.connectionphoneapp.di.repositoryModule
import kz.nu.connectionphoneapp.di.useCaseModule
import kz.nu.connectionphoneapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class MyApp : Application() {

    private val modulesToUse = listOf(
        viewModelModule,
        mapperModule,
        repositoryModule,
        networkModule,
        useCaseModule,
    )

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@MyApp)
            parametersOf(Constants.BASE_URL)
            modules(modulesToUse)
        }
    }
}
