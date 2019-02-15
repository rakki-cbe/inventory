package rakki.inventory.basic.authendication

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.BaseViewModel
import rakki.inventory.basic.Entities
import rakki.inventory.basic.encryptPass


class RegistrationViewModel : BaseViewModel() {
    val viewCommunicator: MutableLiveData<HashMap<ViewKey, String>> by lazy { MutableLiveData<HashMap<ViewKey, String>>() }

    fun validateRegisterData(fullName: String, userName: String, password: String, role: String) {
        val hashMap = HashMap<ViewKey, String>()
        if (fullName.isEmpty()) {
            hashMap.put(ViewKey.FullNameInvalid, "")
        } else if (userName.isEmpty()) {
            hashMap.put(ViewKey.UserNameInvalid, "")
        } else if (password.isEmpty()) {
            hashMap.put(ViewKey.PasswordInvalid, "")
        } else if (role.isEmpty()) {
            hashMap.put(ViewKey.RoleInvalid, "")
            } else {
            hashMap.put(ViewKey.ShowProgress, "")
                scope.launch(Dispatchers.Main) {
                    hashMap.clear()
                    val user = checkUserAlreadyPresent(userName)
                    if (user != null) {
                        hashMap.put(ViewKey.HideProgress, "")
                        hashMap.put(ViewKey.UserAlreadyPresent, "")

                    } else {
                        val encrytedData = encryptPass(password)
                        val userNew = Entities.UserDetails(
                            userName, encrytedData.encryptedData,
                            role,
                            fullName, encrytedData.iv
                        )
                        insert(userNew)
                        // Log.d("Encrypted string ", userNew.userPassword)
                        //Log.d("Decrypted string ", decrypt(userNew.userPassword!!, userNew.ivInfo!!))
                        hashMap.put(ViewKey.HideProgress, "")
                        hashMap.put(ViewKey.RegisterSuccess, "")

                    }
                    viewCommunicator.value = hashMap
                }

            }
        viewCommunicator.value = hashMap

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

    enum class ViewKey {
        ShowProgress, HideProgress, UserNameInvalid, PasswordInvalid,
        CredentialInvalid, FullNameInvalid, RoleInvalid, RegisterSuccess, UserAlreadyPresent
    }

}