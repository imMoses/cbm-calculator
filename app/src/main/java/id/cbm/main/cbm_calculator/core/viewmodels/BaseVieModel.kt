package id.cbm.main.cbm_calculator.core.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BaseVieModel(application: Application) : AndroidViewModel(application) {
    protected val context
        get() = getApplication<Application>()
}