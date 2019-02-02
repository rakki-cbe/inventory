package rakki.inventory.basic.authendication

import rakki.inventory.basic.BaseView

interface RegistrationView : BaseView {
    fun clearAll()
    fun savedSuccess()
    fun errorFullName()
    fun errorUserName()
    fun errorUserNameAlreadyPresent()
    fun errorPasswor()
    fun errorRole()
    fun getFullName(): String
    fun getUserName(): String
    fun getPasswor(): String
    fun getRole(): String

}