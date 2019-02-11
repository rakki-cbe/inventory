package rakki.inventory.basic.authendication

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {
    private val sharePreference: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    fun setUserInfoToFile(userId: Int) {
        val edit = sharePreference.edit()
        edit.putInt("userId", userId)
        edit.apply()
    }

    fun getLoggedUser(): Int {
        return sharePreference.getInt("userId", -1)
    }
}