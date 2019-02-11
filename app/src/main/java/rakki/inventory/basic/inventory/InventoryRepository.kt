package rakki.inventory.basic.inventory

import android.support.annotation.WorkerThread
import rakki.inventory.basic.Entities
import rakki.inventory.basic.authendication.UserPreference

class InventoryRepository(private val inventoryDao: InventoryDAO, val userPreference: UserPreference) {
    //val users: LiveData<List<Entities.Category>> = userDao.getAllCategory()

    @WorkerThread
    fun insert(user: Entities.Category) {
        inventoryDao.insert(user)
    }

    @WorkerThread
    fun getCategoryBasedOnName(userName: String): Entities.Category? {
        return inventoryDao.getCategoryBasedOnName(userName)
    }

    @WorkerThread
    fun getUserBasedOnId(userId: Int): Entities.Category? {
        return inventoryDao.getCategoryById(userId)
    }

    @WorkerThread
    fun getAllCategory(): List<Entities.Category> {
        return inventoryDao.getAllCategory()
    }


}