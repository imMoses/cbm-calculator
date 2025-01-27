package id.cbm.main.cbm_calculator.ui.sales.model

import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel

data class AtapAnakModel(
    var itemName: String = "",
    var value: String = "0",
    var fieldType: Int? = null,
    var listItemSpinner: MutableList<GeneralModel>? = null,
)
