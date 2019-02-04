package rakki.inventory.basic.authendication

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import rakki.inventory.basic.*
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
            } else {
                scope.launch(Dispatchers.Main) {
                    val user = checkUserAlreadyPresent(view!!.getUserName())
                    if (user != null) {
                        view!!.errorUserNameAlreadyPresent()
                    } else {
                        val encrytedData = encryptPass(view!!.getPasswor())
                        val userNew = Entities.UserDetails(
                            view!!.getUserName(), encrytedData.encryptedData,
                            1,
                            view!!.getFullName(), encrytedData.iv
                        )
                        insert(userNew)
                        Log.d("Encrypted string ", userNew.userPassword)
                        Log.d("Decrypted string ", decrypt(userNew.userPassword!!, userNew.ivInfo!!))
                        // Log.d("Decrypted string ", EncryptionHelper.decrypt(user.userPassword!!))
                        view!!.savedSuccess()
                    }
                }

            }

    }


    private suspend fun checkUserAlreadyPresent(userName: String): Entities.UserDetails? {


        return scope.async(Dispatchers.IO) { repository.getUserBasedOnUserName(userName) }.await()


    }

    fun insert(userDetails: Entities.UserDetails) = scope.launch(Dispatchers.IO) {
        repository.insertUser(userDetails)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


}