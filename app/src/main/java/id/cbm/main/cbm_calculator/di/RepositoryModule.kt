package id.cbm.main.cbm_calculator.di

import id.cbm.main.cbm_calculator.data.Repository
import org.koin.dsl.module

val repositoryModule = module {
    factory { Repository(get()) }
}