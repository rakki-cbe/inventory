package rakki.inventory.basic.authendication

import android.app.Application
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.BaseViewModel
import rakki.inventory.basic.Entities
import rakki.inventory.basic.encryptPass


class RegistrationViewModel(application: Application) : BaseViewModel(application) {
    var view: RegistrationView? = null
    val allUsers: LiveData<List<Entities.UserDetails>> = repository.users

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
                view!!.showProgressBar(true)
                scope.launch(Dispatchers.Main) {
                    val user = checkUserAlreadyPresent(view!!.getUserName())
                    if (user != null) {
                        view!!.showProgressBar(false)
                        view!!.errorUserNameAlreadyPresent()
                    } else {
                        val encrytedData = encryptPass(view!!.getPasswor())
                        val userNew = Entities.UserDetails(
                            view!!.getUserName(), encrytedData.encryptedData,
                            1,
                            view!!.getFullName(), encrytedData.iv
                        )
                        insert(userNew)
                        // Log.d("Encrypted string ", userNew.userPassword)
                        //Log.d("Decrypted string ", decrypt(userNew.userPassword!!, userNew.ivInfo!!))
                        view!!.savedSuccess()
                        view!!.showProgressBar(false)
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