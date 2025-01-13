package id.cbm.main.cbm_calculator.ui.engineer.form // ktlint-disable package-name

import android.app.DownloadManager
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.core.listener.IPermissionListener
import id.cbm.main.cbm_calculator.data.remote.ApiResultHandler
import id.cbm.main.cbm_calculator.data.remote.dto.BaseApiResponse
import id.cbm.main.cbm_calculator.data.remote.dto.LinkPDFResponse
import id.cbm.main.cbm_calculator.data.remote.dto.request.RequestEngineerParam
import id.cbm.main.cbm_calculator.databinding.ActivityRequestFormBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity
import id.cbm.main.cbm_calculator.utils.Constants
import id.cbm.main.cbm_calculator.utils.CustomRegex
import id.cbm.main.cbm_calculator.utils.DialogAlertHelper
import id.cbm.main.cbm_calculator.utils.HelperTextSpannable
import id.cbm.main.cbm_calculator.utils.LetterModel
import id.cbm.main.cbm_calculator.utils.PermissionHelper
import id.cbm.main.cbm_calculator.utils.SavingFileHelper
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.util.Locale

class RequestFormActivity : BaseActivity<ActivityRequestFormBinding>() {

    private var bentangPlatY = 0.0
    private var bentangPlayX = 0.0

    private var dataPerhitunganNo = "-"
    private var dataCust = "-"
    private var dataProyek = "-"
    private var dataSales = "-"
    private var dataAsas = "-"

    private val viewModel: RequestFormViewModel by inject()

    override fun getViewBinding() = ActivityRequestFormBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntentExtras(savedInstanceState)
        initUI()
        observeDownlaodingPDF()
//        observeDownloadingFilePDFAndSaveIt()
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
                            text,
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
                            String.format(Locale.US, "%.2f", calculate),
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
        init_V_d_2_KapasitasMomenNegatifMetalDeckrencanaUntukPelatMenerus()
        init_VI_gayaGeserPlatAkibatBebanTerfaktor()
        init_VII_1_defleksiKondisisaatPengecoranBeton()
        init_VII_1_a_kondisiSimpleSpan()
        init_VII_2_a_momentInersiaKomposit()
        init_VII_2_b_momentInersiaPenampangUncrack()
        init_VII_2_c_s_momentInersiaPenampang()
    }

    private fun init_IV_BebanPelatLantai() {
        binding.apply {
            // a Beban Mati (DEAD LOAD) table
            lyBebanPlatLantai.cettBeratSendiri.setBeratSatuan("2400")

            lyBebanPlatLantai.cettBeratFinishingLantai.setBeratSatuan("2200")
            lyBebanPlatLantai.cettBeratFinishingLantai.setTebalSatuan("0.025")

            lyBebanPlatLantai.cettBeratPlafondRangka.setTebalSatuan("-")
            lyBebanPlatLantai.cettBeratPlafondRangka.getBeratSatuanEditText().addTextChangedListener(object : TextWatcher {
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
            lyBebanPlatLantai.cettBeratInstalasiME.getBeratSatuanEditText().addTextChangedListener(object : TextWatcher {
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
                        lyKapasitasMomenLapangan.etLenganMomengayaTarik.setValueText(
                            String.format(
                                Locale.US,
                                "%.2f",
                                resultLenganMomenGayaTarik,
                            ),
                        )
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
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh / (2 * fcc))) * 0.3 / tenPow

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
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh / (2 * fcc))) * 0.3 / tenPow

                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText(String.format(Locale.US, String.format("%.2f", summary)))
                    } else {
                        lyKapasitasMomenLapangan.etPhi.setValueText("0.0")
                        lyKapasitasMomenLapangan.etFormulaMcap.setValueText("0.0")
                    }
                }
            })

            lyKapasitasMomenLapangan.etPhi.addTextChangeListener(object : TextWatcher {
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
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh / (2 * fcc))) * 0.3 / tenPow

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
                        val summary = luasPenampang * teganganLeleh * lenganMomen * (1 - (phi * teganganLeleh / (2 * fcc))) * 0.3 / tenPow

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
                            val resultCalc = (0.25 * (22 / 7) * Math.pow((diameterTulangan - 0.3), 2.0)) * 1000 / jarakTulangan
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText(
                                String.format(
                                    Locale.US,
                                    "%.3f",
                                    resultCalc,
                                ),
                            )
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
                            val resultCalc = (0.25 * (22 / 7) * Math.pow((diameterTulangan - 0.3), 2.0)) * 1000 / jarakTulangan
                            lyPerhitunganTulanganPositifTambahan.etLuasTulangan.setValueText(
                                String.format(
                                    Locale.US,
                                    "%.3f",
                                    resultCalc,
                                ),
                            )
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
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMomenNegatifUltimate.setFormulaTextInfoSpannable(
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
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMomenNegatifUltimate.setSymbolTextSpanneble(
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

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etLebarFiktif.setFormulaTextInfoSpannable(
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
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etLebarFiktif.setSymbolTextSpanneble(
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

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etLenganMomenGayaTarikTulangan.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "dmd",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMomenRelatifM.setFormulaTextInfoSpannable(
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

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "ω = 1-√(1-2*m)",
                    null,
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement2.setTitleText(
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
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement2.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "0.8X=ω*d",
                    listLetter = null,
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement3.setTitleText(
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
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMechanicalReinforcement3.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "ωbd",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.tvSyaratFormula.text = HelperTextSpannable.subscriptMultipleTextSingleLine(
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

    private fun init_V_d_2_KapasitasMomenNegatifMetalDeckrencanaUntukPelatMenerus() {
        binding.apply {
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etKebutuhanTulanganNegatif.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "As- = ω*bfict*dmd*fcc/fst",
                    listLetter = listOf(
                        // s
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // fict
                        LetterModel(
                            startIndex = 9,
                            endIndex = 13,
                        ),
                        // md
                        LetterModel(
                            startIndex = 15,
                            endIndex = 17,
                        ),
                        // cc
                        LetterModel(
                            startIndex = 19,
                            endIndex = 21,
                        ),
                        // st
                        LetterModel(
                            startIndex = 23,
                            endIndex = 25,
                        ),
                    ),
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etButuhTulangan.setSymbolTextSpanneble(
                SpannableString("Ø"),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etAst.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "Ast-",
                    listLetter = listOf(
                        LetterModel(
                            startIndex = 1,
                            endIndex = 3,
                        ),
                        LetterModel(
                            startIndex = 3,
                            endIndex = 4,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                    ),
                ),
            )

            val fstadd = "fst add"
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etTeganganLelehTulanganBaja.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = fstadd,
                    listLetter = listOf(
                        // st add
                        LetterModel(
                            startIndex = 1,
                            endIndex = fstadd.length,
                        ),
                    ),
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etButuhTulangan2.setSymbolTextSpanneble(
                SpannableString("Ø"),
            )

            val fastadd = "Ast add-"
            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etAstadd.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = fastadd,
                    listLetter = listOf(
                        // st add
                        LetterModel(
                            startIndex = 1,
                            endIndex = fastadd.length - 1,
                        ),
                        // -
                        LetterModel(
                            startIndex = fastadd.length - 1,
                            endIndex = fastadd.length,
                        ),
                    ),
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etKonversiLuasTulanganTambahanKe.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fst",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )

            lyKapasitasMomenNegatifMetalDeckrencanaPelatMenerus.etMakaLuasTulanganTambahan.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = fastadd,
                    listLetter = listOf(
                        // st add
                        LetterModel(
                            startIndex = 1,
                            endIndex = fastadd.length - 1,
                        ),
                        // -
                        LetterModel(
                            startIndex = fastadd.length - 1,
                            endIndex = fastadd.length,
                        ),
                    ),
                ),
            )
        }
    }

    private fun init_VI_gayaGeserPlatAkibatBebanTerfaktor() {
        binding.apply {
            lyGayaGeserPlat.etLebarRatarataProfil.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "bm",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )
            lyGayaGeserPlat.etLebarRatarataProfil.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "bm=b*bv/bd",
                    listLetter = listOf(
                        // bm
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // bv
                        LetterModel(
                            startIndex = 6,
                            endIndex = 7,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                        ),
                    ),
                ),
            )

            lyGayaGeserPlat.etSectionFormula1.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "fv",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )
            lyGayaGeserPlat.etSectionFormula1.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "fv = ή*(1+50*ρ)*0.30*fct",
                    listLetter = listOf(
                        // fv
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // fct
                        LetterModel(
                            startIndex = 22,
                            endIndex = 23,
                        ),
                    ),
                ),
            )

            lyGayaGeserPlat.etSectionFormula2.setSymbolTextSpanneble(SpannableString(("ρ")))
            lyGayaGeserPlat.etSectionFormula2.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptText(
                    rawText = "ρ = As/(b*d)",
                    startIndex = 5,
                    endIndex = 6,
                ),
            )

            lyGayaGeserPlat.etSectionFormula3.setSymbolTextSpanneble(SpannableString(("ή")))
            lyGayaGeserPlat.etSectionFormula3.setFormulaTextInfoSpannable(SpannableString("ή = 1.6-d"))

            lyGayaGeserPlat.etSectionFormula4AndSyarat.setSymbolTextSpanneble(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "Syarat Vact ≤ Vcu",
                    listLetter = listOf(
                        // Vact
                        LetterModel(
                            startIndex = 8,
                            endIndex = 11,
                        ),

                        // Vcu
                        LetterModel(
                            startIndex = 15,
                            endIndex = 16,
                        ),
                    ),
                ),
            )
            lyGayaGeserPlat.etSectionFormula4AndSyarat.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "Vact = (Qu*Lx)/2",
                    listLetter = listOf(
                        // Vact
                        LetterModel(
                            startIndex = 1,
                            endIndex = 4,
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
                    ),
                ),
            )

            lyGayaGeserPlat.etSectionFormula5.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Vcu",
                    startIndex = 1,
                    endIndex = 3,
                ),
            )
            lyGayaGeserPlat.etSectionFormula5.setFormulaTextInfoSpannable(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "Vcu = bm*d*fv",
                    listLetter = listOf(
                        // Vcu
                        LetterModel(
                            startIndex = 1,
                            endIndex = 3,
                        ),
                        // bm
                        LetterModel(
                            startIndex = 7,
                            endIndex = 8,
                        ),
                        // fv
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
                        ),
                    ),
                ),
            )

            lyGayaGeserPlat.tvResult.text = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "Syarat Vact ≤ Vcu",
                listLetter = listOf(
                    // Vact
                    LetterModel(
                        startIndex = 8,
                        endIndex = 11,
                    ),

                    // Vcu
                    LetterModel(
                        startIndex = 15,
                        endIndex = 16,
                    ),
                ),
            )
        }
    }

    private fun init_VII_1_defleksiKondisisaatPengecoranBeton() {
        binding.apply {
            lyDefleksiV1.etMassaJenisBeton.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Wc",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiV1.etTebalPelatLantai.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "ts",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiV1.etGarisNetralFloordeck.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "tn",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            val formulaBeratLantai = "Qw = (ts-tn)*Wc"
            lyDefleksiV1.etBeratLantai.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = formulaBeratLantai,
                    listOf(
                        // Qw
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // ts
                        LetterModel(
                            startIndex = 7,
                            endIndex = 8,
                        ),
                        // tn
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                        // Wc
                        LetterModel(
                            startIndex = 14,
                            endIndex = formulaBeratLantai.length,
                        ),

                    ),
                ),
            )
        }
    }

    private fun init_VII_1_a_kondisiSimpleSpan() {
        binding.apply {
            lyDefleksiV1A.etBentangPelatArahX.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Lx",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiV1A.etFormula1.setUpperFormulaText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "5 * Qw * L4",
                    listLetter = listOf(
                        // Qw
                        LetterModel(
                            startIndex = 5,
                            endIndex = 6,
                        ),
                        // L4
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),

                    ),
                ),
            )

            lyDefleksiV1A.etFormula1.setBottomFormulaText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "384 * E * Ix",
                    listLetter = listOf(
                        // Ix
                        LetterModel(
                            startIndex = 11,
                            endIndex = 12,
                        ),
                    ),
                ),
            )

            lyDefleksiV1A.etFormula2.setUpperFormulaText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "5 * Qw * L4",
                    listLetter = listOf(
                        // Qw
                        LetterModel(
                            startIndex = 5,
                            endIndex = 6,
                        ),
                        // L4
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                    ),
                ),
            )
            lyDefleksiV1A.etFormula2.setBottomFormulaText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "384 * E * Ix",
                    listLetter = listOf(
                        // Ix
                        LetterModel(
                            startIndex = 11,
                            endIndex = 12,
                        ),
                    ),
                ),
            )
        }
    }

    private fun init_VII_2_a_momentInersiaKomposit() {
        binding.apply {
            lyDefleksiVII2A.etMomenInersiaKompositFormula1.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "EI = EcIc/(1+Φe)",
                    listLetter = listOf(
                        // Ec
                        LetterModel(
                            startIndex = 6,
                            endIndex = 7,
                        ),
                        // Ic
                        LetterModel(
                            startIndex = 8,
                            endIndex = 9,
                        ),
                        // Φe
                        LetterModel(
                            startIndex = 14,
                            endIndex = 15,
                        ),
                    ),
                ),
            )
            lyDefleksiVII2A.etMomenInersiaKompositFormula1.setSymbolTextSpanneble(
                SpannableString("EI ="),
            )

            lyDefleksiVII2A.etMomenInersiaKompositFormula2.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "Ic = (I1+I2)/2",
                    listLetter = listOf(
                        // Ic
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // I1
                        LetterModel(
                            startIndex = 7,
                            endIndex = 8,
                        ),
                        // 12
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                    ),
                ),
            )
            lyDefleksiVII2A.etMomenInersiaKompositFormula2.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Ic =",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2A.etMomenInersiaKompositFormula3.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "Φe = MccΦ/M",
                    listLetter = listOf(
                        // Φe
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // Mcc
                        LetterModel(
                            startIndex = 6,
                            endIndex = 8,
                        ),
                    ),
                ),
            )
            lyDefleksiVII2A.etMomenInersiaKompositFormula3.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Φe ≈",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )
        }
    }

    private fun init_VII_2_b_momentInersiaPenampangUncrack() {
        binding.apply {
            lyDefleksiVII2B.etMomenInersiaUncrackFormula1.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "Ac = (b*h)+(α*As)-(hw(bd-b2)b/bd)",
                    listLetter = listOf(
                        // Ac
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // As
                        LetterModel(
                            startIndex = 15,
                            endIndex = 16,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 20,
                            endIndex = 21,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 23,
                            endIndex = 24,
                        ),
                        // b2
                        LetterModel(
                            startIndex = 26,
                            endIndex = 27,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 31,
                            endIndex = 32,
                        ),
                    )
                )
            )
            lyDefleksiVII2B.etMomenInersiaUncrackFormula1.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Ac =",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula4.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "As =",
                    startIndex = 1,
                    endIndex = 2,
                )
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula5.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "hw =",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula7.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "bd =",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula8.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "b2",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula9.setSymbolTextSpanneble(
                SpannableString("α =")
            )
            lyDefleksiVII2B.etMomenInersiaUncrackFormula9.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "α = Es(1+Φe)/Ec",
                    listLetter = listOf(
                        LetterModel(
                            startIndex = 5,
                            endIndex = 6,
                        ),
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                        LetterModel(
                            startIndex = 14,
                            endIndex = 15,
                        ),
                    )
                )
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula10.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "Is =",
                    startIndex = 1,
                    endIndex = 2,
                )
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula11.setSymbolTextSpanneble(
                SpannableString("ρ =")
            )
            lyDefleksiVII2B.etMomenInersiaUncrackFormula11.setTitleText(
                HelperTextSpannable.subscriptText(
                    rawText = "ρ = As/bd",
                    startIndex = 5,
                    endIndex = 6,
                ),
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula12.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "x1 =",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            val x1Formula = "x1 = [b*h2/2+α*As*d-hw(bd-b2)(h-(hw/2)b/bd)]/Ac"
            lyDefleksiVII2B.etMomenInersiaUncrackFormula12.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = x1Formula,
                    listLetter = listOf(
                        // x1
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // h2
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                            type = Constants.SUPERSCRIPT_LETTER
                        ),
                        // As
                        LetterModel(
                            startIndex = 16,
                            endIndex = 17,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 21,
                            endIndex = 22,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 24,
                            endIndex = 25,
                        ),
                        // b2
                        LetterModel(
                            startIndex = 27,
                            endIndex = 28,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 34,
                            endIndex = 35,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 41,
                            endIndex = 42,
                        ),
                        // Ac
                        LetterModel(
                            startIndex = 46,
                            endIndex = 47,
                        ),

                    )
                )
            )

            lyDefleksiVII2B.etMomenInersiaUncrackFormula13.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "I1",
                    startIndex = 1,
                    endIndex = 2,
                )
            )

            val I1Formula = "I1 = b(h-hw)3/12+b(h-hw)(x1-(h-hw)/2)2+α*Is+α*As(d-x1)2+b2*b*hw(hw2/12+(h-x1-hw/2)2)/bd"
            lyDefleksiVII2B.etMomenInersiaUncrackFormula13.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = I1Formula,
                    listLetter = listOf(
                        // I1
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 10,
                            endIndex = 11,
                        ),
                        // 3
                        LetterModel(
                            startIndex = 12,
                            endIndex = 13,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 22,
                            endIndex = 23,
                        ),
                        // x1
                        LetterModel(
                            startIndex = 26,
                            endIndex = 27,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 32,
                            endIndex = 33,
                        ),
                        // Is
                        LetterModel(
                            startIndex = 42,
                            endIndex = 43,
                        ),
                        // As
                        LetterModel(
                            startIndex = 47,
                            endIndex = 48,
                        ),
                        // x1
                        LetterModel(
                            startIndex = 52,
                            endIndex = 53,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 54,
                            endIndex = 55,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // b2
                        LetterModel(
                            startIndex = 57,
                            endIndex = 58,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 62,
                            endIndex = 63,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 65,
                            endIndex = 66,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 66,
                            endIndex = 67,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // x1
                        LetterModel(
                            startIndex = 75,
                            endIndex = 76,
                        ),
                        // hw
                        LetterModel(
                            startIndex = 78,
                            endIndex = 79,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 82,
                            endIndex = 83,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // bd
                        LetterModel(
                            startIndex = 86,
                            endIndex = I1Formula.length,
                        ),

                    )
                )
            )
        }
    }

    private fun init_VII_2_c_s_momentInersiaPenampang() {
        binding.apply {
            lyDefleksiVII2CD.etMomenInersiaPenampang1.setTitleText(
                HelperTextSpannable.subscriptText(
                    rawText = "x2 = d*ρ*α((√(1+2/ρ*α)-1)",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2CD.etMomenInersiaPenampang1.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "x2",
                    startIndex = 1,
                    endIndex = 2,
                ),
            )

            lyDefleksiVII2CD.etMomenInersiaPenampang2.setSymbolTextSpanneble(
                HelperTextSpannable.subscriptText(
                    rawText = "I2",
                    startIndex = 1,
                    endIndex = 2,
                )
            )
            lyDefleksiVII2CD.etMomenInersiaPenampang2.setTitleText(
                HelperTextSpannable.combineSubscriptSuperscriptLetter(
                    rawText = "I2 = b*x23/3+α(Is+As(d-x2)2)",
                    listLetter = listOf(
                        // I2
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // x2
                        LetterModel(
                            startIndex = 8,
                            endIndex = 9,
                        ),
                        // 3
                        LetterModel(
                            startIndex = 9,
                            endIndex = 10,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                        // Is
                        LetterModel(
                            startIndex = 16,
                            endIndex = 17,
                        ),
                        // As
                        LetterModel(
                            startIndex = 19,
                            endIndex = 20,
                        ),
                        // x2
                        LetterModel(
                            startIndex = 24,
                            endIndex = 25,
                        ),
                        // 2
                        LetterModel(
                            startIndex = 26,
                            endIndex = 27,
                            type = Constants.SUPERSCRIPT_LETTER,
                        ),
                    )
                )
            )

            lyDefleksiVII2CD.etBebanKerjaLayan.setTitleText(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "Beban kerja layan, Qs",
                    listLetter = listOf(
                        LetterModel(
                            startIndex = 20,
                            endIndex = 21,
                        ),
                    )
                )
            )
            lyDefleksiVII2CD.etBebanKerjaLayan.setFormulaTextInfoSpannable(
                HelperTextSpannable.subscriptMultipleTextSingleLine(
                    rawText = "Qs = Qw+Qll+Qfinish",
                    listLetter = listOf(
                        // Qs
                        LetterModel(
                            startIndex = 1,
                            endIndex = 2,
                        ),
                        // Qw
                        LetterModel(
                            startIndex = 6,
                            endIndex = 7,
                        ),
                        // Qll
                        LetterModel(
                            startIndex = 9,
                            endIndex = 11,
                        ),
                        // Qfinish
                        LetterModel(
                            startIndex = 13,
                            endIndex = 19,
                        ),
                    )
                )
            )

            lyDefleksiVII2CD.tvSyaratFormula.text = "Syarat: δ ≤ 1/360"
            lyDefleksiVII2CD.tvSyaratFormulaFormat.text = HelperTextSpannable.combineSubscriptSuperscriptLetter(
                rawText = "δ = 3*Qs*Lx3/384*EI  ≤  1/360",
                listLetter = listOf(
                    // Qs
                    LetterModel(
                        startIndex = 7,
                        endIndex = 8,
                    ),
                    // Lx
                    LetterModel(
                        startIndex = 10,
                        endIndex = 11,
                    ),
                    // 3
                    LetterModel(
                        startIndex = 11,
                        endIndex = 12,
                        type = Constants.SUPERSCRIPT_LETTER,
                    ),

                )
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
                Log.e(RequestFormActivity::class.simpleName, "click btnSave")
                loadingDialog()
                val dataRequest = RequestEngineerParam(
                    perhitunganNo = dataPerhitunganNo,
                    customerName = dataCust,
                    project = dataProyek,
                    sales = dataSales,
                    asName = dataAsas,
                )
                Log.e(RequestFormActivity::class.simpleName, "request body : ${Gson().toJson(dataRequest)}")
                viewModel.retrieveDownloadPdfRequestEngineer(
                    param = dataRequest,
                )
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

    private fun observeDownlaodingPDF() {
        try {
            viewModel.responseDownlaodPdfEngineer.observe(this) { response ->
                val apiResulthandler = ApiResultHandler<BaseApiResponse<LinkPDFResponse>>(
                    this@RequestFormActivity,
                    onLoading = {
                        loadingDialog()
                    },
                    onSuccess = {
//                        Log.e(RequestFormActivity::class.simpleName, "response raw: ${Gson().toJson(it)}")

                        it?.let { resultResponse ->
                            if (resultResponse.code != 200) {
                                dismissLoading()
                                DialogAlertHelper.showDialogMessage(
                                    context = this,
                                    title = "Error Service",
                                    message = it.message ?: "-",
                                    listener = object : DialogAlertHelper.DialogInfoListener {
                                        override fun onClickOk() {
                                            Toast.makeText(this@RequestFormActivity, "Silahkan dicoba lagi dengan data penomoran berbeda", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                )
                            } else {
                                it.data?.let { dataUrlDownlaod ->
                                    Log.w(RequestFormActivity::class.simpleName, "response API url_pdf: ${dataUrlDownlaod.urlPdf}")
                                    dataUrlDownlaod.urlPdf?.let { safeUrLLink ->

                                        dismissLoading()

                                        PermissionHelper.checkGrantedPermission(
                                            context = this@RequestFormActivity,
                                            perms = listOf(PermissionHelper.permissionMediaAccess()),
                                            requestCode = 122,
                                            listener = object : IPermissionListener {
                                                override fun onPermissionGranted() {
                                                    // set filename pdf
                                                    val filenamePdf = "roof_calc_engineer_${System.currentTimeMillis()}.pdf"

                                                    SavingFileHelper.downloadManagerAndSaveFile(
                                                        this@RequestFormActivity,
                                                        safeUrLLink,
                                                        filenamePdf,
                                                    ) { downloadID, downloadManager ->
                                                        lifecycleScope.launch {
                                                            loadingDialog("Generating PDF ...")

                                                            val filePdf = withContext(Dispatchers.IO) {
                                                                waitForDownloadCompletion(downloadID, downloadManager, filenamePdf)
                                                            }

                                                            delay(1000)
                                                            dismissLoading()

                                                            SavingFileHelper.openChooserPdf(
                                                                context = this@RequestFormActivity,
                                                                filePdf = filePdf,
                                                            )
                                                        }
                                                    }
                                                }

                                                override fun onFailed(requestCode: Int, perms: MutableList<String>) {
                                                    Toast.makeText(this@RequestFormActivity, "Permission WRITE EXTERNAL STORAGE not GRANTED!", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onFailure = { messageFailure ->
                        dismissLoading()
                        Toast.makeText(this, messageFailure ?: "-", Toast.LENGTH_SHORT).show()
                    },
                )
                apiResulthandler.handleApiResult(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(RequestFormActivity::class.simpleName, "Something wrong with observerDownlaodingPDF: ${e.message}")
        }
    }

    private suspend fun waitForDownloadCompletion(downloadID: Long, downloadManager: DownloadManager, filename: String): File {
        return withContext(Dispatchers.IO) {
            var downloading = true
            var filePath: File? = null

            while (downloading) {
                val query = DownloadManager.Query().setFilterById(downloadID)
                val cursor = downloadManager.query(query)

                if (cursor != null && cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloading = false
                            dismissLoading()

                            filePath = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                filename,
                            )
                        }
                        DownloadManager.STATUS_FAILED -> {
                            downloading = false
                            throw Exception("Download failed")
                        }
                    }
                }
                cursor?.close()
            }

            filePath ?: throw Exception("Download did not complete")
        }
    }
}
