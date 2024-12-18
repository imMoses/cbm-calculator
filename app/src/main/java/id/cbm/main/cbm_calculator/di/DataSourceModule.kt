package id.cbm.main.cbm_calculator.di // ktlint-disable package-name

import id.cbm.main.cbm_calculator.data.remote.RemoteDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {
    factory { RemoteDataSource(get()) }
}
