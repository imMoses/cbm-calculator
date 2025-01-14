package id.cbm.main.cbm_calculator.di

import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormViewModel
import id.cbm.main.cbm_calculator.ui.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RequestFormViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
}
