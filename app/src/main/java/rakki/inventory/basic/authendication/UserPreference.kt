package rakki.inventory.basic.authendication

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {
    private val sharePreference: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    fun setUserInfoToFile(userId: Long) {
        val edit = sharePreference.edit()
        edit.putLong("userId", userId)
        edit.apply()
    }

    fun getLoggedUser(): Long {
        return sharePreference.getLong("userId", -1)
    }
}