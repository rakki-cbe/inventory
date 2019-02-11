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
    fun getCategoryById(categoryId: Int): Entities.Category?
}