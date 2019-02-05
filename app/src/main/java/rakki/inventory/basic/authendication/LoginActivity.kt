package rakki.inventory.basic.authendication

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
class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var viewModel: LoginViewModel
    override fun invalidCredential() {
        showToast(getString(R.string.InvalidCredential))
    }

    override fun clearAll() {
        log_ed_user_name.setText("")
        log_ed_user_name.error = null
        log_ed_pass.setText("")
        log_ed_pass.error = null
    }

    override fun loggedSuccess() {
        startActivity(RegistrationActivity.getLaunchIntent(this))
    }

    override fun errorUserName() {
        log_ed_user_name.error = getString(R.string.InvalidUserName)
    }

    override fun errorPasswor() {
        log_ed_pass.error = getString(R.string.InvalidPassword)
    }

    override fun getUserName(): String {
        return log_ed_user_name.text.toString()
    }

    override fun getPasswor(): String {
        return log_ed_pass.text.toString()
    }

    override fun showProgressBar(show: Boolean) {
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.view = this
        // Set up the login form.

        log_ed_pass.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                viewModel.validateLoginData()
                return@OnEditorActionListener true
            }
            false
        })

        log_bt_sign_in.setOnClickListener { viewModel.validateLoginData() }
        log_bt_register.setOnClickListener { startActivity(RegistrationActivity.getLaunchIntent(this)) }
    }

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}
