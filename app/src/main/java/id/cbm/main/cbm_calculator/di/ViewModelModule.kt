package id.cbm.main.cbm_calculator.di

import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RequestFormViewModel(get(), get()) }
}