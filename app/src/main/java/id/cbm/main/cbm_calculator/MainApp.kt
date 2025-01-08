package id.cbm.main.cbm_calculator // ktlint-disable package-name

import android.app.Application
import id.cbm.main.cbm_calculator.di.networkModule
import id.cbm.main.cbm_calculator.di.remoteDataSourceModule
import id.cbm.main.cbm_calculator.di.repositoryModule
import id.cbm.main.cbm_calculator.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApp)
            androidLogger()
            modules(networkModule, remoteDataSourceModule, repositoryModule, viewModelModule)
        }
    }
}
