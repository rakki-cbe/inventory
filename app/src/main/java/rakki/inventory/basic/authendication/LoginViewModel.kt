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
import rakki.inventory.basic.decrypt


class LoginViewModel(application: Application) : BaseViewModel(application) {
    var view: LoginView? = null
    val allUsers: LiveData<List<Entities.UserDetails>> = repository.users

    fun validateLoginData() {

        if (view != null)
            if (view!!.getUserName().isEmpty()) {
                view!!.errorUserName()
            } else if (view!!.getPasswor().isEmpty()) {
                view!!.errorPasswor()
            } else {
                view!!.showProgressBar(true)
                scope.launch(Dispatchers.Main) {
                    val user = checkUserAlreadyPresent(view!!.getUserName())
                    if (user == null) {
                        view?.invalidCredential()

                    } else {
                        val password = decrypt(user.userPassword!!, user.ivInfo!!)
                        if (password.equals(view!!.getPasswor(), true))
                            view?.loggedSuccess()
                        else
                            view?.invalidCredential()
                    }
                    view?.showProgressBar(false)
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