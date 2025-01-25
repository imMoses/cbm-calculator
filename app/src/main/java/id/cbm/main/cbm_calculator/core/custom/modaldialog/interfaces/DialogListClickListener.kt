package id.cbm.main.cbm_calculator.core.custom.modaldialog.interfaces // ktlint-disable package-name

import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel

interface DialogListClickListener {
    fun itemClick(value: GeneralModel)
}
