package id.cbm.main.cbm_calculator.ui.sales.model

import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel

// ktlint-disable package-name

data class AreaModel(
    var labelName: String = "",
    var value: String = "",
    var fieldType: Int? = null,
    var listItemSpinner: List<GeneralModel>? = null,
)
