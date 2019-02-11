package rakki.inventory.basic.authendication

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import rakki.inventory.basic.Entities

class UserRepository(private val userDao: UserDao, val userPreference: UserPreference) {
    val users: LiveData<List<Entities.UserDetails>> = userDao.getAllUsers()

    @WorkerThread
    fun insertUser(user: Entities.UserDetails) {
        userDao.insert(user)
    }

    @WorkerThread
    fun getUserBasedOnUserName(userName: String): Entities.UserDetails? {
        return userDao.getUser(userName)
    }

    @WorkerThread
    fun getUserBasedOnUserId(userId: Int): Entities.UserDetails? {
        return userDao.getUserById(userId)
    }

    @WorkerThread
    fun setUserLoggedInfo(userId: Int) {
        userPreference.setUserInfoToFile(userId)
    }

    fun getUserLoggedInfo(): Int {
        return userPreference.getLoggedUser()
    }
}