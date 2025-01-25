package id.cbm.main.cbm_calculator.ui.sales.helper // ktlint-disable package-name

import android.content.Context
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.ui.sales.adapter.AreaAdapter
import id.cbm.main.cbm_calculator.ui.sales.adapter.ChildAtapAnakAdapter
import id.cbm.main.cbm_calculator.ui.sales.model.AreaModel
import id.cbm.main.cbm_calculator.ui.sales.model.AtapAnakModel
import id.cbm.main.cbm_calculator.ui.sales.model.ParentAtapAnakModel

class EstimationCepatHelper(private val context: Context) {

    fun addAreaItems(): List<AreaModel> {
        val list = ArrayList<AreaModel>()

        val bentangmm = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_bentang_mm),
            value = "2300",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val panjangmm = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_panjang_mm),
            value = "4800",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val ohkiri = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_oh_kiri),
            value = "2300",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val ohkanan = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_oh_kanan),
            value = "4800",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val sudut = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_sudut),
            value = "12.5",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val jenispenutup = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_jenis_penutup),
            value = "Beton",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val sudutApex = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_sudut_apex),
            value = "Sering",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val typeAtap = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_type_atap),
            value = "0",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val panjangTCBC = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_panjang_tcbc),
            value = "6",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val panjangWeb = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_panjang_web),
            value = "6",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        val reng = AreaModel(
            labelName = context.resources.getString(R.string.form_est_cepat_reng),
            value = "6",
            fieldType = AreaAdapter.ITEM_VIEW_EDITTEXT,
            listItemSpinner = null,
        )

        list.add(bentangmm)
        list.add(panjangmm)
        list.add(ohkiri)
        list.add(ohkanan)
        list.add(sudut)
        list.add(jenispenutup)
        list.add(typeAtap)
        list.add(sudutApex)
        list.add(panjangTCBC)
        list.add(panjangWeb)
        list.add(reng)

        return list.toList()
    }

    fun addAnakAtap(latestIndex: Int): ParentAtapAnakModel {

        val childItems = listOf(
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_bentang_mm),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_panjang_mm),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_oh_kiri),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_oh_kanan),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_sudut),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
            AtapAnakModel(
                itemName = context.resources.getString(R.string.form_est_cepat_jumlah),
                value = 0.0,
                fieldType = ChildAtapAnakAdapter.ITEM_VIEW_EDITTEXT,
            ),
        )

        val parentItem = ParentAtapAnakModel(
            title = "Atap Anak ${latestIndex + 1}",
            listItemAtap = childItems,
        )

        return parentItem
    }

}
