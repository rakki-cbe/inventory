package rakki.inventory.basic.authendication

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import rakki.inventory.basic.R

class RegistrationActivity : AppCompatActivity() {
    lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
    }
}
