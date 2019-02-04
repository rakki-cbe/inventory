package rakki.inventory.basic

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import rakki.inventory.basic.authendication.UserDao

class UserRepository(private val userDao: UserDao) {
    val users: LiveData<List<Entities.UserDetails>> = userDao.getAllUsers()
    @WorkerThread
    fun insertUser(user: Entities.UserDetails) {
        userDao.insert(user)
    }

    @WorkerThread
    fun getUserBasedOnUserName(userName: String): Entities.UserDetails? {
        return userDao.getUser(userName)
    }
}