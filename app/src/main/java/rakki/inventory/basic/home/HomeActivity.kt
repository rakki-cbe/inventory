package rakki.inventory.basic.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.home_activity.*
import rakki.inventory.basic.R
import rakki.inventory.basic.inventory.add.AddProductFragment


class HomeActivity : AppCompatActivity() {
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        if (savedInstanceState == null) {
            setSaleFragment()
        }
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.checkUserPresent()
        home_im_addItem.setOnClickListener { setAddProductFragment() }
        home_im_report.setOnClickListener { setSaleFragment() }
        home_im_settings.setOnClickListener { setAddProductFragment() }
        home_im_sale.setOnClickListener { setAddProductFragment() }


    }

    private fun setSaleFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_container, HomeFragment.newInstance())
            .commitNow()
    }

    private fun setAddProductFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_container, AddProductFragment.newInstance())
            .commitNow()
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

}
