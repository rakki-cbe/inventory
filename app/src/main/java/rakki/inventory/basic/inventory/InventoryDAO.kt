package rakki.inventory.basic.inventory

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import rakki.inventory.basic.Entities

@Dao
interface InventoryDAO {

    @Query("SELECT * from category ")
    fun getAllCategory(): List<Entities.Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Entities.Category)

    @Query("DELETE FROM category")
    fun deleteAll()

    @Query("SELECT * FROM category WHERE categoryName=:mName")
    fun getCategoryBasedOnName(mName: String): Entities.Category?

    @Query("SELECT * FROM category WHERE id=:categoryId")
    fun getCategoryById(categoryId: Long): Entities.Category?

    /**Sub category **/
    @Query("SELECT * from SubCategory WHERE categoryId=:id")
    fun getAllSubCategory(id: Long): List<Entities.SubCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subCategory: Entities.SubCategory)

    @Query("DELETE FROM SubCategory")
    fun deleteAllSubCategory()

    @Query("SELECT * FROM SubCategory WHERE subCategoryName=:mName and categoryId=:cateId")
    fun getSubCategoryBasedOnName(mName: String, cateId: Long): Entities.SubCategory?

    @Query("SELECT * FROM SubCategory WHERE id=:subCategoryId")
    fun getSubCategoryById(subCategoryId: Long): Entities.SubCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(brand: Entities.Brand)

    @Query("SELECT * FROM Brand WHERE brandName=:brandName")
    fun getBandBasedOnName(brandName: String): Entities.Brand?

    @Query("SELECT * from Brand WHERE id=:brandId")
    fun getBrandById(brandId: Long): Entities.Brand?

    @Query("SELECT * from Brand ")
    fun getAllBrand(): List<Entities.Brand>

    @Query(
        "SELECT * from Product WHERE categoryId=:categoryItem and subcategoryId=:subCategoryItem and " +
                "brandId=:brandItem and unitQuantity=:quantityPerItem and amount=:purchaseAmount and saleAmount=:saleAmount and " +
                "productName=:name limit 1"
    )
    fun checkProductAlreadyPresent(
        categoryItem: Long, subCategoryItem: Long, brandItem: Long, name: String,
        quantityPerItem: Double, purchaseAmount: Double,
        saleAmount: Double
    ): Entities.Product?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(product: Entities.Product): Long?
}