package rakki.inventory.basic.authendication

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import rakki.inventory.basic.R

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var viewModel: LoginViewModel
    override fun invalidCredential() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loggedSuccess() {
        startActivity(RegistrationActivity.getLaunchIntent(this))
    }

    override fun errorUserName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun errorPasswor() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserName(): String {
        return log_ed_user_name.text.toString()
    }

    override fun getPasswor(): String {
        return log_ed_pass.text.toString()
    }

    override fun showProgressBar(show: Boolean) {

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
