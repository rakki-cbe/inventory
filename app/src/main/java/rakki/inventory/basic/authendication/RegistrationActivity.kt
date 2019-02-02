package rakki.inventory.basic.authendication

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.registration_activity.*
import rakki.inventory.basic.R
import rakki.inventory.basic.showToast

class RegistrationActivity : AppCompatActivity(), RegistrationView {
    override fun errorUserNameAlreadyPresent() {

    }

    override fun getFullName(): String {
        return reg_ed_fullName.text.toString()


    }

    override fun getUserName(): String {
        return reg_ed_user_name.text.toString()
    }

    override fun getPasswor(): String {
        return reg_ed_password.text.toString()
    }

    override fun getRole(): String {
        return reg_sp_role.selectedItem.toString()
    }

    override fun clearAll() {
        reg_ed_fullName.setText("")
        reg_ed_user_name.setText("")
        reg_ed_password.setText("")
        reg_sp_role.setSelection(0)
        reg_ed_fullName.error = null
        reg_ed_user_name.error = null
        reg_ed_password.error = null

    }

    override fun savedSuccess() {

    }

    override fun errorFullName() {
        reg_ed_fullName.error = getString(R.string.InvalidFullName)


    }

    override fun errorUserName() {
        reg_ed_user_name.error = getString(R.string.InvalidUserName)


    }

    override fun errorPasswor() {
        reg_ed_password.error = getString(R.string.InvalidPassword)
    }

    override fun errorRole() {
        showToast(getString(R.string.InvalidRole))

    }

    override fun showProgressBar(show: Boolean) {
        loadingBar.visibility = if (show) View.VISIBLE else View.GONE
    }


    lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
        viewModel.view = this
        reg_bt_save.setOnClickListener { viewModel.validateRegisterData() }
        reg_bt_clear.setOnClickListener { clearAll() }

    }

}
