package rakki.inventory.basic.authendication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import rakki.inventory.basic.R
import rakki.inventory.basic.showToast

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginViewModel
    fun invalidCredential() {
        showToast(getString(R.string.InvalidCredential))
    }

    fun clearAll() {
        log_ed_user_name.setText("")
        log_ed_user_name.error = null
        log_ed_pass.setText("")
        log_ed_pass.error = null
    }

    fun loggedSuccess() {
        startActivity(RegistrationActivity.getLaunchIntent(this))
    }

    fun errorUserName() {
        log_ed_user_name.error = getString(R.string.InvalidUserName)
    }

    fun errorPasswor() {
        log_ed_pass.error = getString(R.string.InvalidPassword)
    }

    fun getUserName(): String {
        return log_ed_user_name.text.toString()
    }

    fun getPasswor(): String {
        return log_ed_pass.text.toString()
    }

    fun showProgressBar(show: Boolean) {
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setObserverForViewModel()
        // Set up the login form.

        log_ed_pass.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                viewModel.validateLoginData(getUserName(), getPasswor())
                return@OnEditorActionListener true
            }
            false
        })

        log_bt_sign_in.setOnClickListener { viewModel.validateLoginData(getUserName(), getPasswor()) }
        log_bt_register.setOnClickListener { startActivity(RegistrationActivity.getLaunchIntent(this)) }
    }

    private fun setObserverForViewModel() {
        viewModel.viewCommunicator.observe(this, Observer<HashMap<LoginViewModel.ViewKey, String>> { it ->
            if (it != null) {
                for ((key, _) in it) {
                    if (key == LoginViewModel.ViewKey.ShowProgress) showProgressBar(true)
                    if (key == LoginViewModel.ViewKey.ShowProgress) showProgressBar(false)
                    if (key == LoginViewModel.ViewKey.UserNameInvalid) errorUserName()
                    if (key == LoginViewModel.ViewKey.PasswordInvalid) errorPasswor()
                    if (key == LoginViewModel.ViewKey.CredentialInvalid) invalidCredential()
                    if (key == LoginViewModel.ViewKey.LoginSuccess) loggedSuccess()
                }
            }
        })
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}
