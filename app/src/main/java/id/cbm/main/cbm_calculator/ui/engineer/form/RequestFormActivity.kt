package id.cbm.main.cbm_calculator.ui.engineer.form

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.widget.Toast
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityRequestFormBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity
import id.cbm.main.cbm_calculator.utils.Constants
import id.cbm.main.cbm_calculator.utils.CustomRegex
import id.cbm.main.cbm_calculator.utils.HelperTextSpannable
import id.cbm.main.cbm_calculator.utils.LetterModel
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class RequestFormActivity : BaseActivity<ActivityRequestFormBinding>() {

    private var bentangPlatY = 0.0
    private var bentangPlayX = 0.0

    private var dataPerhitunganNo = "-"
    private var dataCust = "-"
    private var dataProyek = "-"
    private var dataSales = "-"
    private var dataAsas = "-"

    override fun getViewBinding() = ActivityRequestFormBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntentExtras(savedInstanceState)
        initUI()
        initListener()
        resetKoefisienResult()
    }

    private fun initIntentExtras(savedInstanceState: Bundle?) {
        intent?.let {
            dataPerhitunganNo = intent.getStringExtra(DataCustomerActivity.DC_PERHITUNGAN) ?: "-"
            dataCust = intent.getStringExtra(DataCustomerActivity.DC_CUSTOMER) ?: "-"
            dataProyek = intent.getStringExtra(DataCustomerActivity.DC_PROYEK) ?: "-"
            dataSales = intent.getStringExtra(DataCustomerActivity.DC_SALES) ?: "-"
            dataAsas = intent.getStringExtra(DataCustomerActivity.DC_ASAS) ?: "-"

            binding.apply {
                ctvNoPerhitungan.setValueText(dataPerhitunganNo)
                ctvCustomer.setValueText(dataCust)
                ctvProyek.setValueText(dataProyek)
                ctvSales.setValueText(dataSales)
                ctvAsAs.setValueText(dataAsas)
            }
        }
    }

    private fun initUI() {
        binding.apply {
            appBar.setNavBackListener { onCustomBackPressed() }

            etKuatTarikBeton.setValueText("2,17")

            val textFy = HelperTextSpannable.subscriptText(
                rawText = "fy",
                startIndex = 1,
                endIndex = 2,
                proportionSize = 0.8f,
            )
            etMetalYield.setSymbolTextSpanneble(textFy)
            etPanjangBentangPlatX.setSymbolTextSpanneble(textFy)

            val textFc1 = HelperTextSpannable.subscriptText(
                rawText = resources.getString(R.string.symbol_fc1),
                startIndex = 1,
                endIndex = 2,
                proportionSize = 0.8f,
            )
            etKuatTekanBeton.setSymbolTextSpanneble(textFc1)

            val textFct = HelperTextSpannable.subscriptText(
                rawText = resources.getString(R.string.symbol_fct),
                startIndex = 1,
                endIndex = 3,
                proportionSize = 0.8f,
            )
            etKuatTarikBeton.setSymbolTextSpanneble(textFct)

            val textFst = HelperTextSpannable.subscriptText(
                rawText = "fst",
                startIndex = 1,
                endIndex = 3,
                proportionSize = 0.8f,
            )

            etTeganganLeleh.setSymbolTextSpanneble(textFst)

            val textLy = HelperTextSpannable.subscriptText(
                rawText = "Ly",
                startIndex = 1,
                endIndex = 2,
                proportionSize = 0.8f,
            )
            etPanjangBentangPlatY.setSymbolTextSpanneble(textLy)
        }

        init_IV_BebanPelatLantai()
        init_V_a_KapasitasMomentLapanganMetalDeckRencana()
        init_V_b_KapasitasMomentLapanganMetalDeckRencana()
        init_V_c_KapasitasMomenLapanganMetalDeckRencana()
        init_V_d_KapasitasMomenNegatifMetalDeckrencanaUntukPelatMenerus()
    }

    private fun init_IV_BebanPelatLantai() {
        binding.apply {
            val symbolQl = HelperTextSpannable.subscriptText(
                rawText = "QL",
                startIndex = 1,
                endIndex = 2,
            )
            lyBebanPlatLantai.etBebanHidupLantaiBangunan.setSymbolTextSpanneble(symbolQl)
            lyBebanPlatLantai.etBebanHidupLantaiBangunanFinal.setSymbolTextSpanneble(symbolQl)

            val listLetter = listOf(
                // QU
                LetterModel(
                    startIndex = 1,
                    endIndex = 2,
                ),
                // QD
                LetterModel(
                    startIndex = 10,
                    endIndex = 11,
                ),
                // QL
                LetterModel(
                    startIndex = 19,
                    endIndex = 20,
                ),
            )
            val symbolFormulaBebanRencanaTerfaktor = HelperTextSpannable.subscriptMultipleTextSingleLine(
                resources.getString(R.string.form_engineer_beban_rencana_terfaktor_formula),
                listLetter,
            )
            lyBebanPlatLantai.etBebanRencanaTerfaktor.setSymbolTextSpanneble(symbolFormulaBebanRencanaTerfaktor)
        }
    }

    private fun init_V_a_KapasitasMomentLapanganMetalDeckRencana() {
        binding.apply {
            lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "As",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fym",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMomenLapangan.etLenganMomengayaTarik.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "dmd",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMomenLapangan.etFccKuatTekanBeton.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fcc",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            // Title: M+cap = As*fym*dmd*(1-(ρ*fym/(2*fcc))
            lyKapasitasMomenLapangan.etFormulaMcap.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = resources.getString(R.string.form_engineer_mcap_formula_v_a),
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // cap
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // As
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // Fym
                        LetterModel(
                            startIndex = 12,
                            endIndex = 14,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // dmd
                        LetterModel(
                            startIndex = 16,
                            endIndex = 18,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // fym
                        LetterModel(
                            startIndex = 26,
                            endIndex = 28,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // fcc
                        LetterModel(
                            startIndex = 33,
                            endIndex = 35,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                    ),
                ),
            )
            val mcapFormula = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "M+cap",
                listLetter = listOf(
                    // +
                    LetterModel(
                        startIndex = 1,
                        endIndex = 2,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // cap
                    LetterModel(
                        startIndex = 2,
                        endIndex = 5,
                        type = Constants.SUBSCRIPT_LETTER,
                    ),
                ),
            )
            lyKapasitasMomenLapangan.etFormulaMcap.setSymbolTextSpanneble(mcapFormula)

            // Title: M+act = Qu*Lx2/16
            lyKapasitasMomenLapangan.etFormulaMact.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = resources.getString(R.string.form_engineer_mact_formula_v_a),
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // act
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // QU
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // LX
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
                            type = Constants.SUBSCRIPT_LETTER,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 13,
                            endIndex = 14,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),

                    ),
                ),
            )
            val mactFormula = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "M+act",
                listLetter = listOf(
                    // +
                    LetterModel(
                        startIndex = 1,
                        endIndex = 2,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // act
                    LetterModel(
                        startIndex = 2,
                        endIndex = 5,
                        type = Constants.SUBSCRIPT_LETTER,
                    ),
                ),
            )
            lyKapasitasMomenLapangan.etFormulaMact.setSymbolTextSpanneble(mactFormula)

            val syaratResult = String.format("Syarat %s", resources.getString(R.string.form_engineer_syarat_mcap_mact))
            // M+cap ≥ M+act
            lyKapasitasMomenLapangan.tvSyaratFormula.text = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = syaratResult,
                listLetter = listOf(
                    // +
                    LetterModel(
                        startIndex = 8,
                        endIndex = 9,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // cap
                    LetterModel(
                        startIndex = 9,
                        endIndex = 12,
                        type = Constants.SUBSCRIPT_LETTER,
                    ),
                    // +
                    LetterModel(
                        startIndex = 16,
                        endIndex = 17,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // act
                    LetterModel(
                        startIndex = 17,
                        endIndex = syaratResult.length,
                        type = Constants.SUBSCRIPT_LETTER,
                    ),

                ),
            )
        }
    }

    private fun init_V_b_KapasitasMomentLapanganMetalDeckRencana() {
        binding.apply {
            lyPerhitunganTulanganPositifTambahan.etTeganganLelehBajaTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fst",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etDiameterTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = resources.getString(R.string.symbol_diameter_tulangan),
                    startIndex = 1,
                    endIndex = 5,
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "As",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etLenganMomenGayaTarik.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "dst",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etPhi.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "As/(1000*dst)",
                    listOf(
                        // As
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // dst
                        LetterModel(
                            startIndex = 10,
                            endIndex = 12,
                        ),
                    ),
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etFormulaMadd.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+add =As*fst*d*(1-ρ*fst/(2*fcc))",
                    listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // add
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                        ),
                        // As
                        LetterModel(
                            startIndex = 7,
                            endIndex = 8,
                        ),
                        // fst
                        LetterModel(
                            startIndex = 10,
                            endIndex = 12,
                        ),
                        // fst
                        LetterModel(
                            startIndex = 22,
                            endIndex = 24,
                        ),
                        // fcc
                        LetterModel(
                            startIndex = 29,
                            endIndex = 31,
                        ),
                    ),
                ),
            )
            lyPerhitunganTulanganPositifTambahan.etFormulaMadd.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+add",
                    listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // add
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                        ),
                    ),
                ),
            )

            lyPerhitunganTulanganPositifTambahan.etFormulaMcaptotal.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+cap total = M+cap + M+add",
                    listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // cap total
                        LetterModel(
                            startIndex = 2,
                            endIndex = 11,
                        ),
                        // +
                        LetterModel(
                            startIndex = 15,
                            endIndex = 16,
                        ),
                        // cap
                        LetterModel(
                            startIndex = 16,
                            endIndex = 19,
                        ),
                        // +
                        LetterModel(
                            startIndex = 23,
                            endIndex = 24,
                        ),
                        // add
                        LetterModel(
                            startIndex = 24,
                            endIndex = 27,
                        ),
                    ),
                ),
            )
            lyPerhitunganTulanganPositifTambahan.etFormulaMcaptotal.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+cap total",
                    listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // cap total
                        LetterModel(
                            startIndex = 2,
                            endIndex = 11,
                        ),
                    ),
                ),
            )

            lyPerhitunganTulanganPositifTambahan.tvSyaratFormula.text = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "M+cap total ≥ M+act",
                listLetter = listOf(
                    // +
                    LetterModel(
                        startIndex = 1,
                        endIndex = 2,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // cap total
                    LetterModel(
                        startIndex = 2,
                        endIndex = 11,
                    ),
                    // +
                    LetterModel(
                        startIndex = 15,
                        endIndex = 16,
                    ),
                    // act
                    LetterModel(
                        startIndex = 16,
                        endIndex = 19,
                    ),
                ),
            )
        }
    }

    private fun init_V_c_KapasitasMomenLapanganMetalDeckRencana() {
        binding.apply {
            lyCekTulanganPositifTambahan.etbebanAkibatApi.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "QF = QD + 0.5 QL",
                    listLetter = listOf(
                        // QF
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // QD
                        LetterModel(
                            startIndex = 6,
                            endIndex = 7,
                        ),
                        // QL
                        LetterModel(
                            startIndex = 15,
                            endIndex = 16,
                        ),
                    ),
                ),
            )

            lyCekTulanganPositifTambahan.etMomentAkibatApiPadaTengahBentang.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+F = QF*Lx2/16",
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // F
                        LetterModel(
                            startIndex = 2,
                            endIndex = 3,
                        ),
                        // QF
                        LetterModel(
                            startIndex = 7,
                            endIndex = 8,
                        ),
                        // Lx
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 11,
                            endIndex = 12,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                    ),
                ),
            )

            lyCekTulanganPositifTambahan.etKapasistasMomenPositif.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+CAP F=As*d*fy*7%(1-(ρ*fy*7%)/(2*fcc))",
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // CAP F
                        LetterModel(
                            startIndex = 2,
                            endIndex = 7,
                        ),
                        // As
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                        ),
                        // fy
                        LetterModel(
                            startIndex = 14,
                            endIndex = 15,
                        ),
                        // fy
                        LetterModel(
                            startIndex = 25,
                            endIndex = 26,
                        ),
                        // fcc
                        LetterModel(
                            startIndex = 35,
                            endIndex = 37,
                        ),
                    ),
                ),
            )
            lyCekTulanganPositifTambahan.etKapasistasMomenPositif.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+CAP F",
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // CAP F
                        LetterModel(
                            startIndex = 2,
                            endIndex = 7,
                        ),
                    ),
                ),
            )

            lyCekTulanganPositifTambahan.etKapasistasMomenPositifTulanganTambahan.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+cap add =As*fy'*d*75%(1-ρ*fy'*75%/(2*fcc))",
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // CAP F
                        LetterModel(
                            startIndex = 2,
                            endIndex = 10,
                        ),
                        // As
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
                        ),
                        // fy
                        LetterModel(
                            startIndex = 15,
                            endIndex = 16,
                        ),
                        // fy
                        LetterModel(
                            startIndex = 29,
                            endIndex = 30,
                        ),
                        // fcc
                        LetterModel(
                            startIndex = 40,
                            endIndex = 42,
                        ),
                    ),
                ),
            )
            lyCekTulanganPositifTambahan.etKapasistasMomenPositifTulanganTambahan.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M+cap add",
                    listLetter = listOf(
                        // +
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // cap add
                        LetterModel(
                            startIndex = 2,
                            endIndex = 9,
                        ),
                    ),
                ),
            )

            lyCekTulanganPositifTambahan.tvSyaratFormula.text = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "M+F ≤ M+TOT F = M+CAP F + M+cap add",
                listLetter = listOf(
                    // +
                    LetterModel(
                        startIndex = 1,
                        endIndex = 2,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // F
                    LetterModel(
                        startIndex = 2,
                        endIndex = 3,
                    ),
                    // +
                    LetterModel(
                        startIndex = 7,
                        endIndex = 8,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // TOT F
                    LetterModel(
                        startIndex = 8,
                        endIndex = 13,
                    ),
                    // +
                    LetterModel(
                        startIndex = 17,
                        endIndex = 18,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // CAP F
                    LetterModel(
                        startIndex = 18,
                        endIndex = 23,
                    ),
                    // +
                    LetterModel(
                        startIndex = 27,
                        endIndex = 28,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),
                    // cap add
                    LetterModel(
                        startIndex = 28,
                        endIndex = 35,
                    ),
                ),
            )
        }
    }

    private fun init_V_d_KapasitasMomenNegatifMetalDeckrencanaUntukPelatMenerus() {

    }

    private fun initListener() {
        binding.apply {
            etPanjangBentangPlatY.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatY.setValueText(cleanText)
                                etPanjangBentangPlatY.setSelection(cleanText.length)
                            }
                        }
                    }
                }

                override fun afterTextChanged(textInput: Editable?) {
                    val text = textInput.toString()
                    if (text.isNotEmpty()) {
                        bentangPlatY = text.toDoubleOrNull() ?: 0.0
                        calculateKoefisien()
                    } else {
                        resetKoefisienResult()
                        bentangPlatY = 0.0
                    }
                }
            })

            etPanjangBentangPlatX.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatX.setValueText(cleanText)
                                etPanjangBentangPlatX.setSelection(cleanText.length)
                            }
                        }
                    }
                }

                override fun afterTextChanged(textInputted: Editable?) {
                    val text = textInputted.toString()
                    if (text.isNotEmpty()) {
                        bentangPlayX = text.toDoubleOrNull() ?: 0.0
                        calculateKoefisien()
                    } else {
                        resetKoefisienResult()
                        bentangPlayX = 0.0
                    }
                }
            })

            btnSave.setSafeOnClickListener {
                Toast.makeText(this@RequestFormActivity, "Tersimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateKoefisien() {
        if (bentangPlatY > 0.0 && bentangPlayX > 0.0) {
            val result = bentangPlatY / bentangPlayX

            setTextKoefisien(result)

            val koefisienThreshold = 2.0
            if (result >= koefisienThreshold) {
                binding.tvResultPossibility.text =
                    resources.getString(R.string.form_engineer_koefisien_pelat_calc_correct)
            } else {
                binding.tvResultPossibility.text =
                    resources.getString(R.string.form_engineer_koefisien_pelat_calc_incorrect)
            }
        }
    }

    private fun setTextKoefisien(resCalculation: Double?) {
        val textLetter = if (resCalculation == null) {
            String.format(
                "%s / %s = -",
                resources.getString(R.string.symbol_Ly),
                "Lx",
            )
        } else {
            String.format(
                "%s / %s = %.1f",
                resources.getString(R.string.symbol_Ly),
                "Lx",
                resCalculation,
            )
        }

        val textKoefisien = SpannableString(textLetter)
        // Ly
        textKoefisien.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textKoefisien.setSpan(RelativeSizeSpan(0.8f), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Lx
        textKoefisien.setSpan(SubscriptSpan(), 6, 7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textKoefisien.setSpan(RelativeSizeSpan(0.8f), 6, 7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvResultCalculation.text = textKoefisien
    }

    private fun resetKoefisienResult() {
        setTextKoefisien(null)
        binding.tvResultPossibility.text = "-"
    }
}
