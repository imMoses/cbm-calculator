package id.cbm.main.cbm_calculator.ui.mainmenu // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.data.local.LocalDataController
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivityMainEngineerBinding
import id.cbm.main.cbm_calculator.ui.engineer.adapter.MainMenuGridAdapter
import id.cbm.main.cbm_calculator.ui.engineer.adapter.MenuEngineerItem
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity
import id.cbm.main.cbm_calculator.ui.sales.form.customer.DataCustFormSalesActivity
import id.cbm.main.cbm_calculator.ui.signin.SignInActivity
import id.cbm.main.cbm_calculator.utils.DialogAlertHelper
import id.cbm.main.cbm_calculator.utils.LocalData

class MainMenuActivity : BaseActivity<ActivityMainEngineerBinding>() {

    private lateinit var adapterMainMenu: MainMenuGridAdapter
    private var userData: SignInResponse? = null

    override fun getViewBinding() = ActivityMainEngineerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initAdapter()
    }

    private fun initUI() {
        binding.apply {
            userData = LocalDataController.General().getModel(
                context = this@MainMenuActivity,
                key = LocalData.Key.USER_PERSONAL_DATA,
                objType = SignInResponse::class.java,
                sharedPref = LocalData.DB.ADDITIONAL_DATA_USER,
            )

            tvWelcome.text = "Hi, ${userData?.name ?: "-"}"

            appBarMainEngineer.setLogoutButtonListener {
                DialogAlertHelper.showDialogMessage(
                    context = this@MainMenuActivity,
                    title = "Logout Account?",
                    message = " Jika iya tekan button OK",
                    isCancelable = true,
                    listener = object : DialogAlertHelper.DialogInfoListener {
                        override fun onClickOk() {
                            LocalDataController.General().resetLocalDB(
                                context = this@MainMenuActivity,
                                key = LocalData.Key.USER_PERSONAL_DATA,
                                sharedPref = LocalData.DB.ADDITIONAL_DATA_USER,
                            )

                            startActivity(
                                Intent(this@MainMenuActivity, SignInActivity::class.java),
                            )
                            finishAffinity()
                        }
                    },

                )
            }
        }
    }

    private fun initAdapter() {
        val mainMenu = userData?.menu

        val list = ArrayList<MenuEngineerItem>()

        mainMenu?.let {
            if (it.menuEngineer == 1) {
                list.add(MenuEngineerItem("1", resources.getString(R.string.menu_engineer_request_form), ContextCompat.getDrawable(this, R.drawable.ic_menu_request_engineer)))
            }

            if (it.menuSales == 1) {
                list.add(MenuEngineerItem("2", "Sales Form", ContextCompat.getDrawable(this, R.drawable.ic_menu_request_engineer)))
            }

//            list.add(MenuEngineerItem("3", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)))
//            list.add(MenuEngineerItem("4", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)))
//            list.add(MenuEngineerItem("5", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)))
        }

        if (list.isNotEmpty()) {
            binding.rvMainMenuEngineer.visibility = View.VISIBLE
            binding.iconTextNoMenus.visibility = View.GONE

            adapterMainMenu = MainMenuGridAdapter(
                listItem = list.toMutableList(),
                listener = object : MainMenuGridAdapter.MainMenuGridAdapterListener {
                    override fun onClickItem(item: MenuEngineerItem) {
                        when (item.id) {
                            "1" -> {
                                startActivity(
                                    Intent(
                                        this@MainMenuActivity,
                                        DataCustomerActivity::class.java,
                                    ),
                                )
                            }
                            "2" -> {
                                startActivity(
                                    Intent(
                                        this@MainMenuActivity,
                                        DataCustFormSalesActivity::class.java,
                                    ),
                                )
                            }
                            else -> {
                                Toast.makeText(this@MainMenuActivity, "click item grid", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
            )
            val gridLayoutManager = GridLayoutManager(this, 2)
            binding.rvMainMenuEngineer.layoutManager = gridLayoutManager
            binding.rvMainMenuEngineer.adapter = adapterMainMenu
        } else {
            binding.rvMainMenuEngineer.visibility = View.GONE
            binding.iconTextNoMenus.visibility = View.VISIBLE
        }
    }
}
