package rakki.inventory.basic.home

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.BaseViewModel
import rakki.inventory.basic.Entities


class HomeViewModel : BaseViewModel() {
    val viewCommunicator: MutableLiveData<HashMap<ViewKey, String>> by lazy { MutableLiveData<HashMap<ViewKey, String>>() }

    var user: Entities.UserDetails? = null
    fun checkUserPresent() {
        val id = repository.getUserLoggedInfo()
        if (id != -1.toLong()) {

            scope.launch(Dispatchers.Main) {
                user = scope.async(Dispatchers.IO) {
                    repository.getUserBasedOnUserId(id)
                }.await()


            }
        }

    }

    enum class ViewKey {
        ShowProgress, HideProgress, UserNameInvalid, PasswordInvalid, CredentialInvalid, LoginSuccess
    }


}
