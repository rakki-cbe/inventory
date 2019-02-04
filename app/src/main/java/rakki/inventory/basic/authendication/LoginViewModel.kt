package rakki.inventory.basic.authendication

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import rakki.inventory.basic.Entities
import rakki.inventory.basic.InventoryDatabase
import rakki.inventory.basic.UserRepository
import rakki.inventory.basic.decrypt
import kotlin.coroutines.CoroutineContext


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var view: LoginView? = null
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: UserRepository
    val allUsers: LiveData<List<Entities.UserDetails>>

    init {
        val userDao = InventoryDatabase.getDatabase(application).getUserDao()
        repository = UserRepository(userDao)
        allUsers = repository.users
    }

    fun validateLoginData() {

        if (view != null)
            if (view!!.getUserName().isEmpty()) {
                view!!.errorUserName()
            } else if (view!!.getPasswor().isEmpty()) {
                view!!.errorPasswor()
            } else {
                scope.launch(Dispatchers.Main) {
                    val user = checkUserAlreadyPresent(view!!.getUserName())
                    if (user == null) {
                        view!!.invalidCredential()
                    } else {
                        val password = decrypt(user.userPassword!!, user.ivInfo!!)
                        if (password.equals(view!!.getPasswor(), true))
                            view!!.loggedSuccess()
                        else
                            view!!.invalidCredential()
                    }
                }

            }

    }


    private suspend fun checkUserAlreadyPresent(userName: String): Entities.UserDetails? {


        return scope.async(Dispatchers.IO) { repository.getUserBasedOnUserName(userName) }.await()


    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


}