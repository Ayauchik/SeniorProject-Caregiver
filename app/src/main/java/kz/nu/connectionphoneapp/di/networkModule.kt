package kz.nu.connectionphoneapp.di

import kz.nu.connectionphoneapp.vital_sign_irt.data.network.FeedbackApi
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.GlobalAPI
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.NotificationAPI
import kz.nu.connectionphoneapp.vital_sign_irt.webrtc.WebRtcApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    factory<Converter.Factory> { GsonConverterFactory.create() }
    factory { HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("globalRetrofit")) {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get<OkHttpClient>())
           // .addInterceptor(get<HttpLoggingInterceptor>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GlobalAPI> {
        get<Retrofit>(named("globalRetrofit")).create(GlobalAPI::class.java)
    }


    single(named("webrtcRetrofit")) {
        Retrofit.Builder()
            .baseUrl(Constants.TEST_URL_WEBRTC)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // WebRtcApi instance
    single<WebRtcApi> {
        get<Retrofit>(named("webrtcRetrofit")).create(WebRtcApi::class.java)
    }

    single(named("notificationRetrofit")) {
        Retrofit.Builder()
            .baseUrl(Constants.TEST_URL_NOTIFICATION)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // WebRtcApi instance
    single<FeedbackApi> {
        get<Retrofit>(named("notificationRetrofit")).create(FeedbackApi::class.java)
    }
}