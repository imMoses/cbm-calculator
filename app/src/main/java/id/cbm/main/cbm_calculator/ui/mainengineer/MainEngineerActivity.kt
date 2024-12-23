package id.cbm.main.cbm_calculator.ui.mainengineer // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityMainEngineerBinding
import id.cbm.main.cbm_calculator.ui.engineer.adapter.MainMenuGridAdapter
import id.cbm.main.cbm_calculator.ui.engineer.adapter.MenuEngineerItem
import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormActivity
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity

class MainEngineerActivity : BaseActivity<ActivityMainEngineerBinding>() {

    private lateinit var adapterMainMenu: MainMenuGridAdapter

    override fun getViewBinding() = ActivityMainEngineerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initAdapter()
    }

    private fun initUI() {
        binding.apply {
            tvWelcome.text = "Hi, John Doe"
        }
    }

    private fun initAdapter() {
        adapterMainMenu = MainMenuGridAdapter(
            listItem = listOf(
                MenuEngineerItem("1", resources.getString(R.string.menu_engineer_request_form), ContextCompat.getDrawable(this, R.drawable.ic_menu_request_engineer)),
                MenuEngineerItem("2", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)),
                MenuEngineerItem("3", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)),
                MenuEngineerItem("4", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)),
                MenuEngineerItem("5", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)),
                MenuEngineerItem("6", resources.getString(R.string.menu_sample), ContextCompat.getDrawable(this, R.drawable.ic_menu_sample)),
            ),
            listener = object : MainMenuGridAdapter.MainMenuGridAdapterListener {
                override fun onClickItem(item: MenuEngineerItem) {
                    when(item.id) {
                        "1" -> {
                            startActivity(
                                Intent(
                                    this@MainEngineerActivity,
                                    DataCustomerActivity::class.java,
                                ),
                            )
                        }
                        else -> {
                            Toast.makeText(this@MainEngineerActivity, "click item grid", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
        )
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvMainMenuEngineer.layoutManager = gridLayoutManager
        binding.rvMainMenuEngineer.adapter = adapterMainMenu
    }
}
