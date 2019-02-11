package rakki.inventory.basic

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import rakki.inventory.basic.authendication.UserDao
import rakki.inventory.basic.inventory.InventoryDAO


@Database(
    entities = [Entities.UserDetails::class, Entities.Category::class, Entities.SubCategory::class,
        Entities.Brand::class, Entities.Product::class], version = 2
)
@TypeConverters(TimeStampConverter::class)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getInventoryDao(): InventoryDAO

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventoryDb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}