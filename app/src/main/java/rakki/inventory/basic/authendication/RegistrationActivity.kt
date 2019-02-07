package rakki.inventory.basic.authendication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.registration_activity.*
import rakki.inventory.basic.R
import rakki.inventory.basic.showToast

/****
 * Reference for encryption and decryption
 * https://medium.com/@josiassena/using-the-android-keystore-system-to-store-sensitive-information-3a56175a454b
 */
class RegistrationActivity : AppCompatActivity() {
    fun errorUserNameAlreadyPresent() {
        showToast(getString(R.string.UserAlreadyPresent))
    }

    fun getFullName(): String {
        return reg_ed_fullName.text.toString()


    }

    fun getUserName(): String {
        return reg_ed_user_name.text.toString()
    }

    fun getPasswor(): String {
        return reg_ed_password.text.toString()
    }

    fun getRole(): String {
        return reg_sp_role.selectedItem.toString()
    }

    fun clearAll() {
        reg_ed_fullName.setText("")
        reg_ed_user_name.setText("")
        reg_ed_password.setText("")
        reg_sp_role.setSelection(0)
        reg_ed_fullName.error = null
        reg_ed_user_name.error = null
        reg_ed_password.error = null

    }

    fun savedSuccess() {
        showToast(getString(R.string.pleaseLogin))
        startActivity(LoginActivity.getLaunchIntent(this))
    }

    fun errorFullName() {
        reg_ed_fullName.error = getString(R.string.InvalidFullName)


    }

    fun errorUserName() {
        reg_ed_user_name.error = getString(R.string.InvalidUserName)


    }

    fun errorPasswor() {
        reg_ed_password.error = getString(R.string.InvalidPassword)
    }

    fun errorRole() {
        showToast(getString(R.string.InvalidRole))

    }

    fun showProgressBar(show: Boolean) {
        loadingBar.visibility = if (show) View.VISIBLE else View.GONE
    }


    lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
        setObserver()
        reg_bt_save.setOnClickListener {
            viewModel.validateRegisterData(
                getFullName(), getUserName(), getPasswor(),
                getRole()
            )
        }
        reg_bt_clear.setOnClickListener { clearAll() }

    }

    private fun setObserver() {
        viewModel.viewCommunicator.observe(this, Observer {
            if (it != null) {
                for ((key, _) in it) {
                    if (key == RegistrationViewModel.ViewKey.ShowProgress) showProgressBar(true)
                    if (key == RegistrationViewModel.ViewKey.ShowProgress) showProgressBar(false)
                    if (key == RegistrationViewModel.ViewKey.UserNameInvalid) errorUserName()
                    if (key == RegistrationViewModel.ViewKey.PasswordInvalid) errorPasswor()
                    if (key == RegistrationViewModel.ViewKey.FullNameInvalid) errorFullName()
                    if (key == RegistrationViewModel.ViewKey.RoleInvalid) errorRole()
                    if (key == RegistrationViewModel.ViewKey.UserAlreadyPresent) errorUserNameAlreadyPresent()
                    if (key == RegistrationViewModel.ViewKey.RegisterSuccess) savedSuccess()
                }
            }
        })
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, RegistrationActivity::class.java)
        }
    }

}
