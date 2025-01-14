package id.cbm.main.cbm_calculator.di // ktlint-disable package-name

import com.google.gson.GsonBuilder
import id.cbm.main.cbm_calculator.BuildConfig
import id.cbm.main.cbm_calculator.data.remote.ApiService
import id.cbm.main.cbm_calculator.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLogginInterceptor = HttpLoggingInterceptor()
    httpLogginInterceptor.level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }

    return httpLogginInterceptor
}

fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()
}

fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create(
    GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create(),
)

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

val networkModule = module {
    single { provideLoggingInterceptor() }
    single { provideHttpClient(get()) }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
}
