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
    fun getCategoryBasedOnId(userId: Long): Entities.Category? {
        return inventoryDao.getCategoryById(userId)
    }

    @WorkerThread
    fun getAllCategory(): List<Entities.Category> {
        return inventoryDao.getAllCategory()
    }

    /**Brand**/

    @WorkerThread
    fun insert(brand: Entities.Brand) {
        inventoryDao.insert(brand)
    }

    @WorkerThread
    fun getbrandBasedOnName(brandName: String): Entities.Brand? {
        return inventoryDao.getBandBasedOnName(brandName)
    }

    @WorkerThread
    fun getBrandBasedOnId(brandId: Long): Entities.Brand? {
        return inventoryDao.getBrandById(brandId)
    }

    @WorkerThread
    fun getAllBrand(): List<Entities.Brand> {
        return inventoryDao.getAllBrand()
    }


    /**SubCategory**/
    @WorkerThread
    fun insert(subCategory: Entities.SubCategory) {
        inventoryDao.insert(subCategory)
    }

    @WorkerThread
    fun getSubCategoryBasedOnName(userName: String, cateId: Long): Entities.SubCategory? {
        return inventoryDao.getSubCategoryBasedOnName(userName, cateId)
    }

    @WorkerThread
    fun getSubCategoryBasedOnId(subCateId: Long): Entities.SubCategory? {
        return inventoryDao.getSubCategoryById(subCateId)
    }

    @WorkerThread
    fun getAllSubCategory(id: Long): List<Entities.SubCategory> {
        return inventoryDao.getAllSubCategory(id)
    }

    @WorkerThread
    fun checkProductAlreadyPresent(
        categoryItem: Long, subCategoryItem: Long,
        brandItem: Long, name: String, quantityPerItem: Double
        , purchaseAmount: Double, saleAmount: Double
    ): Entities.Product? {
        return inventoryDao.checkProductAlreadyPresent(
            categoryItem, subCategoryItem, brandItem, name,
            quantityPerItem, purchaseAmount, saleAmount
        )
    }

    fun addNewProduct(product: Entities.Product): Long? {
        return inventoryDao.insert(product)
    }


}