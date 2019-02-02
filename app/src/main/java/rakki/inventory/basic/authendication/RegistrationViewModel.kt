package rakki.inventory.basic.authendication

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import rakki.inventory.basic.Entities
import rakki.inventory.basic.InventoryDatabase
import rakki.inventory.basic.UserRepository
import kotlin.coroutines.CoroutineContext


class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    var view: RegistrationView? = null
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

    fun validateRegisterData() {
        if (view != null)
            if (view!!.getFullName().isEmpty()) {
                view!!.errorFullName()
            } else if (view!!.getUserName().isEmpty()) {
                view!!.errorUserName()
            } else if (view!!.getPasswor().isEmpty()) {
                view!!.errorPasswor()
            } else if (view!!.getRole().isEmpty()) {
                view!!.errorRole()
            }

    }

    fun insert(userDetails: Entities.UserDetails) = scope.launch(Dispatchers.IO) {
        repository.insertUser(userDetails)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


}