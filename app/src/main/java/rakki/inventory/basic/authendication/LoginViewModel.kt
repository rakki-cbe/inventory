package rakki.inventory.basic.authendication

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.BaseViewModel
import rakki.inventory.basic.Entities
import rakki.inventory.basic.decrypt


class LoginViewModel : BaseViewModel() {
    val viewCommunicator: MutableLiveData<HashMap<ViewKey, String>> by lazy { MutableLiveData<HashMap<ViewKey, String>>() }
    fun validateLoginData(userName: String, password: String) {
        val result: HashMap<ViewKey, String> = HashMap()
        if (userName.isEmpty()) {
            result.put(ViewKey.UserNameInvalid, "")
        } else if (password.isEmpty()) {
            result.put(ViewKey.PasswordInvalid, "")
            } else {
            result.put(ViewKey.ShowProgress, "")
            viewCommunicator.value = result
            result.clear()
                scope.launch(Dispatchers.Main) {
                    val user = checkUserAlreadyPresent(userName)
                    if (user == null) {
                        result.put(ViewKey.CredentialInvalid, "")
                        result.put(ViewKey.HideProgress, "")

                    } else {
                        val passwordDb = decrypt(user.userPassword!!, user.ivInfo!!)
                        if (passwordDb.equals(password, true)) {
                            setUserInfoToPreference(user.id!!)
                        } else {
                            result.put(ViewKey.CredentialInvalid, "")
                            result.put(ViewKey.HideProgress, "")
                        }
                    }

                    viewCommunicator.value = result

                }


        }
        viewCommunicator.value = result


    }

    private fun setUserInfoToPreference(id: Long) {
        val result: HashMap<ViewKey, String> = HashMap()
        scope.launch(Dispatchers.Main) {
            repository.setUserLoggedInfo(id)
            result.put(ViewKey.HideProgress, "")
            result.put(ViewKey.LoginSuccess, "")
            viewCommunicator.value = result
        }
    }


    private suspend fun checkUserAlreadyPresent(userName: String): Entities.UserDetails? {


        return scope.async(Dispatchers.IO) { repository.getUserBasedOnUserName(userName) }.await()


    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun checkUserLoggedIn() {
        val result: HashMap<ViewKey, String> = HashMap()
        val id = repository.getUserLoggedInfo()
        if (id != -1.toLong()) {
            result.put(ViewKey.HideProgress, "")
            result.put(ViewKey.LoginSuccess, "")
            viewCommunicator.value = result
        }

    }

    enum class ViewKey {
        ShowProgress, HideProgress, UserNameInvalid, PasswordInvalid, CredentialInvalid, LoginSuccess
    }


}