package id.cbm.main.cbm_calculator.ui.engineer.form

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityRequestFormBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity
import id.cbm.main.cbm_calculator.utils.Constants
import id.cbm.main.cbm_calculator.utils.CustomRegex
import id.cbm.main.cbm_calculator.utils.HelperTextSpannable
import id.cbm.main.cbm_calculator.utils.LetterModel
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.Locale

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

            etMetalYield.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.setValueText(
                            text
                        )
                    }
                }
            })

            etMetalThickness.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val calculate = 1219.0 * text.toDouble()
                        lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.setValueText(
                            String.format(Locale.US, "%.2f", calculate)
                        )
                    }
                }
            })
            etKuatTekanBeton.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val calculate = 0.1 * (text.toDoubleOrNull() ?: 0.0)
                        etKuatTarikBeton.setValueText(calculate.toString())
                        lyKapasitasMomenLapangan.etFccKuatTekanBeton.setValueText(text)
                    } else {
                        etKuatTarikBeton.setValueText("0.0")
                        lyKapasitasMomenLapangan.etFccKuatTekanBeton.setValueText("0")
                    }
                }
            })
            etKuatTekanBeton.setValueText("21.7")

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
            // a Beban Mati (DEAD LOAD) table
            lyBebanPlatLantai.cettBeratSendiri.setBeratSatuan("2400")

            lyBebanPlatLantai.cettBeratFinishingLantai.setBeratSatuan("2200")
            lyBebanPlatLantai.cettBeratFinishingLantai.setTebalSatuan("0.025")

            lyBebanPlatLantai.cettBeratPlafondRangka.setTebalSatuan("-")
            lyBebanPlatLantai.cettBeratPlafondRangka.getBeratSatuanEditText().addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(text: Editable?) {
                    lyBebanPlatLantai.cettBeratPlafondRangka.setValueQ(text.toString())
                    updateTotalBebanMatiQD()
                }
            })


            lyBebanPlatLantai.cettBeratInstalasiME.setTebalSatuan("-")
            lyBebanPlatLantai.cettBeratInstalasiME.getBeratSatuanEditText().addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(text: Editable?) {
                    lyBebanPlatLantai.cettBeratInstalasiME.setValueQ(text.toString())
                    updateTotalBebanMatiQD()

                }
            })

            etTebalPelatLantai.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun afterTextChanged(text: Editable?) {
                    val tebalPelatLantai = text.toString()
                    if (tebalPelatLantai.isEmpty().not()) {
                        val tebal = tebalPelatLantai.toDouble()
                        val resultTebal = (tebal / 1000.00) - 0.02
                        lyBebanPlatLantai.cettBeratSendiri.setTebalSatuan(String.format("%.3f", resultTebal))

                        val resultLenganMomenGayaTarik = tebal - 20
                        lyKapasitasMomenLapangan.etLenganMomengayaTarik.setValueText(String.format(
                            Locale.US, "%.2f", resultLenganMomenGayaTarik))
                    } else {
                        lyBebanPlatLantai.cettBeratSendiri.setTebalSatuan("0.0")
                        lyKapasitasMomenLapangan.etLenganMomengayaTarik.setValueText("0.0")
                    }
                }
            })

            val symbolQl = HelperTextSpannable.subscriptText(
                rawText = "QL",
                startIndex = 1,
                endIndex = 2,
            )
            lyBebanPlatLantai.etBebanHidupLantaiBangunan.setSymbolTextSpanneble(symbolQl)
            lyBebanPlatLantai.etBebanHidupLantaiBangunanFinal.setSymbolTextSpanneble(symbolQl)
            lyBebanPlatLantai.etBebanHidupLantaiBangunan.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p: Editable?) {
                    val text = p.toString()
                    if (text.isNotEmpty()) {
                        val bebanHidupLantaiBangunan = text.toDoubleOrNull() ?: 0.0
                        val calculate = bebanHidupLantaiBangunan * 0.01
                        lyBebanPlatLantai.etBebanHidupLantaiBangunanFinal.setValueText(calculate.toString())
                    } else {
                        lyBebanPlatLantai.etBebanHidupLantaiBangunanFinal.setValueText("0")
                    }
                }
            })
            lyBebanPlatLantai.etBebanHidupLantaiBangunanFinal.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val totalBebanMati = lyBebanPlatLantai.cetTotalBebanPelatLantai.getEtFinalTotalQ().text.toString().toDoubleOrNull() ?: 0.0
                        val totalBebanHidupLantai = text.toDoubleOrNull() ?: 0.0
                        val calculate = (1.2 * totalBebanMati) + (1.6 * totalBebanHidupLantai)

                        lyBebanPlatLantai.etBebanRencanaTerfaktor.setValueText(String.format(Locale.US, "%.2f", calculate))

                        // result M+act in Kapasitas Moment Lapangan Metal
                        val bebanRencanaTerfaktor = lyBebanPlatLantai.etBebanRencanaTerfaktor.getText().toDoubleOrNull() ?: 0.0
                        val panjangBentangPelat = etPanjangBentangPlatX.getText().toDoubleOrNull() ?: 0.0
                        val resultFormulaMact = (bebanRencanaTerfaktor * Math.pow(panjangBentangPelat, 2.0)) / 16
                        lyKapasitasMomenLapangan.etFormulaMact.setValueText(String.format(Locale.US, "%.2f", resultFormulaMact))
                    } else {
                        lyBebanPlatLantai.etBebanRencanaTerfaktor.setValueText("0.0")
                        lyKapasitasMomenLapangan.etFormulaMact.setValueText("0.0")
                    }
                }
            })

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

    private fun updateTotalBebanMatiQD() {
        binding.apply {
            val QBeratSendiri = lyBebanPlatLantai.cettBeratSendiri.getQValue().toDoubleOrNull() ?: 0.0
            val QBeratFinishing = lyBebanPlatLantai.cettBeratFinishingLantai.getQValue().toDoubleOrNull() ?: 0.0
            val QBeratPlafond = lyBebanPlatLantai.cettBeratPlafondRangka.getQValue().toDoubleOrNull() ?: 0.0
            val QBeratInstalasi = lyBebanPlatLantai.cettBeratInstalasiME.getQValue().toDoubleOrNull() ?: 0.0
            val summary = QBeratSendiri + QBeratFinishing + QBeratPlafond + QBeratInstalasi

            lyBebanPlatLantai.cetTotalBebanPelatLantai.getEtTotalQ().setText(summary.toString())
            lyBebanPlatLantai.cetTotalBebanPelatLantai.getEtFinalTotalQ().setText((summary / 100.0).toString())
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
            lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val luasPenampangNominalMetalDeck = lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.getText().toString().toDoubleOrNull() ?: 0.0
                        val calculate = luasPenampangNominalMetalDeck / (1000 * text.toDouble())

                        lyKapasitasMomenLapangan.etPhi.setValueText(String.format(Locale.US, "%.5f", calculate))

                        val luasPenampang = text.toDoubleOrNull() ?: 0.0
                        val teganganLeleh = lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val lenganMomen = lyKapasitasMomenLapangan.etLenganMomengayaTarik.getText().toDoubleOrNull() ?: 0.0
                        val phi = lyKapasitasMomenLapangan.etPhi.getText().toDoubleOrNull() ?: 0.0
                        val fcc = lyKapasitasMomenLapangan.etFccKuatTekanBeton.getText().toDoubleOrNull() ?: 0.0
                        val tenPow = Math.pow(10.0, 6.0)
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh/(2*fcc))) * 0.3 / tenPow

                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText(String.format(Locale.US, String.format("%.2f", summary)))
                    } else {
                        lyKapasitasMomenLapangan.etPhi.setValueText("0.0")
                    }
                }
            })


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
            lyKapasitasMomenLapangan.etLenganMomengayaTarik.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val luasPenampangNominalMetalDeck = lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val calculate = luasPenampangNominalMetalDeck / (1000 * text.toDouble())

                        lyKapasitasMomenLapangan.etPhi.setValueText(String.format(Locale.US, "%.5f", calculate))

                        val luasPenampang = lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val teganganLeleh = lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val lenganMomen = text.toDoubleOrNull() ?: 0.0
                        val phi = lyKapasitasMomenLapangan.etPhi.getText().toDoubleOrNull() ?: 0.0
                        val fcc = lyKapasitasMomenLapangan.etFccKuatTekanBeton.getText().toDoubleOrNull() ?: 0.0
                        val tenPow = Math.pow(10.0, 6.0)
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh/(2*fcc))) * 0.3 / tenPow

                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText(String.format(Locale.US, String.format("%.2f", summary)))
                    } else {
                        lyKapasitasMomenLapangan.etPhi.setValueText("0.0")
                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText("0.0")
                    }
                }
            })

            lyKapasitasMomenLapangan.etPhi.addTextChangeListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val luasPenampang = lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val teganganLeleh = lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val lenganMomen = lyKapasitasMomenLapangan.etLenganMomengayaTarik.getText().toDoubleOrNull() ?: 0.0
                        val phi = text.toDoubleOrNull() ?: 0.0
                        val fcc = lyKapasitasMomenLapangan.etFccKuatTekanBeton.getText().toDoubleOrNull() ?: 0.0
                        val tenPow = Math.pow(10.0, 6.0)
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh/(2*fcc))) * 0.3 / tenPow

                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText(String.format(Locale.US, String.format("%.2f", summary)))
                    } else {
                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText("0.0")
                    }
                }
            })

            lyKapasitasMomenLapangan.etFccKuatTekanBeton.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fcc",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )
            lyKapasitasMomenLapangan.etFccKuatTekanBeton.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val luasPenampang = lyKapasitasMomenLapangan.etLuasPenampangNominalMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val teganganLeleh = lyKapasitasMomenLapangan.etTegangnLelehMetalDeck.getText().toDoubleOrNull() ?: 0.0
                        val lenganMomen = lyKapasitasMomenLapangan.etLenganMomengayaTarik.getText().toDoubleOrNull() ?: 0.0
                        val phi = lyKapasitasMomenLapangan.etPhi.getText().toDoubleOrNull() ?: 0.0
                        val fcc = text.toDoubleOrNull() ?: 0.0
                        val tenPow = Math.pow(10.0, 6.0)
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh/(2*fcc))) * 0.3 / tenPow

                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText(String.format(Locale.US, String.format("%.2f", summary)))
                    } else {
                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText("0.0")
                    }
                }
            })


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
            lyKapasitasMomenLapangan.etFormulaMcap.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val mCap = text.toDoubleOrNull() ?: 0.0
                        val mAct = lyKapasitasMomenLapangan.etFormulaMact.getText().toDoubleOrNull() ?: 0.0
                        lyKapasitasMomenLapangan.tvResult.text = if (mCap >= mAct) {
                            "OK, Tidak Butuh Tulangan Positif pada suhu kamar"
                        } else {
                            "NOT OK, Butuh Tulangan Positif Tambahan"
                        }
                    } else {
                        lyKapasitasMomenLapangan.tvResult.text = "-"
                    }
                }
            })

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
            lyKapasitasMomenLapangan.etFormulaMact.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val mCap = lyKapasitasMomenLapangan.etFormulaMcap.getText().toDoubleOrNull() ?: 0.0
                        val mAct = text.toDoubleOrNull() ?: 0.0
                        lyKapasitasMomenLapangan.tvResult.text = if (mCap >= mAct) {
                            "OK, Tidak Butuh Tulangan Positif pada suhu kamar"
                        } else {
                            "NOT OK, Butuh Tulangan Positif Tambahan"
                        }
                    } else {
                        lyKapasitasMomenLapangan.tvResult.text = "-"
                    }
                }
            })

            lyKapasitasMomenLapangan.tvResult.text = "-"
            val syaratResult = String.format("Syarat %s", resources.getString(R.string.form_engineer_syarat_mcap_mact))
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
            lyPerhitunganTulanganPositifTambahan.etTeganganLelehBajaTulangan.setValueText("490")

            lyPerhitunganTulanganPositifTambahan.etDiameterTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = resources.getString(R.string.symbol_diameter_tulangan),
                    startIndex = 1,
                    endIndex = 5,
                ),
            )
            lyPerhitunganTulanganPositifTambahan.etDiameterTulangan.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val diameterTulangan = text.toDoubleOrNull() ?: 0.0
                        val jarakTulangan = lyPerhitunganTulanganPositifTambahan.etJarakTulangan.getText().toDoubleOrNull() ?: 0.0
                        if (diameterTulangan == 0.0 || jarakTulangan == 0.0) {
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText("0")
                        } else {
                            val resultCalc = (0.25 * (22/7) * Math.pow((diameterTulangan-0.3), 2.0)) * 1000 / jarakTulangan
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText(String.format(
                                Locale.US, "%.3f", resultCalc))
                        }
                    } else {
                        lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText("0")
                    }
                }
            })
            lyPerhitunganTulanganPositifTambahan.etJarakTulangan.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    val text = p0.toString()
                    if (text.isNotEmpty()) {
                        val diameterTulangan = lyPerhitunganTulanganPositifTambahan.etDiameterTulangan.getText().toDoubleOrNull() ?: 0.0
                        val jarakTulangan = text.toDoubleOrNull() ?: 0.0
                        if (diameterTulangan == 0.0 || jarakTulangan == 0.0) {
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText("0")
                        } else {
                            val resultCalc = (0.25 * (22/7) * Math.pow((diameterTulangan-0.3), 2.0)) * 1000 / jarakTulangan
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText(String.format(
                                Locale.US, "%.3f", resultCalc))
                        }
                    } else {
                        lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText("0")
                    }
                }
            })

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
        binding.apply {
            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMomenNegatifUltimate.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M-act = Qu*Lx2/10",
                    listLetter = listOf(
                        // -
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // act
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                        ),
                        // Qu
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                        ),
                        // Lx
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
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
            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMomenNegatifUltimate.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "M-act",
                    listLetter = listOf(
                        // -
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // act
                        LetterModel(
                            startIndex = 2,
                            endIndex = 5,
                        ),
                    ),
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etLebarFiktif.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "bfict = (bu/bd)*1000",
                    listLetter = listOf(
                        // fict
                        LetterModel(
                            startIndex = 1,
                            endIndex = 5,
                        ),
                        // bu
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 13,
                            endIndex = 14,
                        ),
                    ),
                ),
            )
            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etLebarFiktif.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "bfict",
                    listLetter = listOf(
                        // fict
                        LetterModel(
                            startIndex = 1,
                            endIndex = 5,
                        ),
                    ),
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etLenganMomenGayaTarikTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "dmd",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMomenRelatifM.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "m = M-act/(bfict*d2*fcc)",
                    listLetter = listOf(
                        // -
                        LetterModel(
                            startIndex = 5,
                            endIndex = 6,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // act
                        LetterModel(
                            startIndex = 6,
                            endIndex = 9,
                        ),
                        // fict
                        LetterModel(
                            startIndex = 12,
                            endIndex = 16,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 18,
                            endIndex = 19,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // fcc
                        LetterModel(
                            startIndex = 21,
                            endIndex = 23,
                        ),
                    ),
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "ω = 1-√(1-2*m)",
                    null,
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement2.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "0.8X < hw",
                    listOf(
                        LetterModel(
                            startIndex = 8,
                            endIndex = 9,
                        ),
                    ),
                ),
            )
            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement2.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "0.8X=ω*d",
                    listLetter = null,
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement3.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "ωbd = 0.8/(fy/733.3+1)",
                    listLetter = listOf(
                        // bd
                        LetterModel(
                            startIndex = 1,
                            endIndex = 3,
                        ),
                        // fy
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
                        ),
                    ),
                ),
            )
            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement3.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "ωbd",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMoementNegatifMetalDeckrencanaPelatMenerus.tvSyaratFormula.text = HelperTextSpannable.subscriptMultipleTextSingleLine(
                rawText = "ωbd > ω",
                listLetter = listOf(
                    LetterModel(
                        startIndex = 1,
                        endIndex = 3,
                    ),
                ),
            )
        }
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
