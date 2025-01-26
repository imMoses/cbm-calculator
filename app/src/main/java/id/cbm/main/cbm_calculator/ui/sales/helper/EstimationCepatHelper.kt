package id.cbm.main.cbm_calculator.ui.sales.helper // ktlint-disable package-name

import android.content.Context
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.ui.sales.adapter.AreaAdapter
import id.cbm.main.cbm_calculator.ui.sales.adapter.ChildAtapAnakAdapter
import id.cbm.main.cbm_calculator.ui.sales.model.AreaModel
import id.cbm.main.cbm_calculator.ui.sales.model.AtapAnakModel
import id.cbm.main.cbm_calculator.ui.sales.model.HasilEstimasiModel
import id.cbm.main.cbm_calculator.ui.sales.model.ParentAtapAnakModel

class EstimationCepatHelper(private val context: Context) {

    fun addAreaItems(): List<AreaModel> {
        val list = listOf(
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_bentang_mm),
                value = "2300",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_panjang_mm),
                value = "4800",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_oh_kiri),
                value = "2300",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_oh_kanan),
                value = "4800",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_sudut),
                value = "12.5",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_jenis_penutup),
                value = "Beton",
                fieldType = AreaAdapter.ITEM_VIEW_SPINNER,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_type_atap),
                value = "0",
                fieldType = AreaAdapter.ITEM_VIEW_SPINNER,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_sudut_apex),
                value = "0",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_panjang_tcbc),
                value = "6",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_panjang_web),
                value = "6",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
            AreaModel(
                labelName = context.resources.getString(R.string.form_est_cepat_reng),
                value = "6",
                fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
                listItemSpinner = null,
            ),
        )

        return list
    }

    fun addAnakAtap(latestIndex: Int): ParentAtapAnakModel {
        val childItems = listOf(
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_bentang_mm),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_panjang_mm),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_oh_kiri),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_oh_kanan),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_sudut),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_jumlah),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.CHILD_ITEM_VIEW_SPINNER,
            ),
        )

        val parentItem = ParentAtapAnakModel(
            title = "Atap Anak ${latestIndex + 1}",
            listItemAtap = childItems,
        )

        return parentItem
    }

    fun addListMaterial(): List<HasilEstimasiModel> {

        val listMaterial = listOf(
            HasilEstimasiModel(
                materialName = "TC-BC C_75.35.75",
                qty = "57",
                harga = "201000",
                disc = "0",
                total = "11457000"
            ),
            HasilEstimasiModel(
                materialName = "Web C_75.35.75",
                qty = "38",
                harga = "201000",
                disc = "0",
                total = "7638000"
            ),
            HasilEstimasiModel(
                materialName = "Reng 30.15.45",
                qty = "138",
                harga = "81000",
                disc = "0",
                total = "11178000"
            ),
        )

        return listMaterial
    }
}
