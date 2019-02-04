package rakki.inventory.basic.authendication

import rakki.inventory.basic.BaseView

interface LoginView : BaseView {
    fun clearAll()
    fun loggedSuccess()
    fun errorUserName()
    fun errorPasswor()
    fun getUserName(): String
    fun getPasswor(): String
    fun invalidCredential()

}