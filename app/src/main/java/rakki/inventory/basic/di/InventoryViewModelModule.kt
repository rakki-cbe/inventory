package rakki.inventory.basic.di

import android.content.Context
import dagger.Module
import dagger.Provides
import rakki.inventory.basic.InventoryDatabase
import rakki.inventory.basic.authendication.UserPreference
import rakki.inventory.basic.inventory.InventoryRepository
import javax.inject.Singleton

@Module
class InventoryViewModelModule {
    @Singleton
    @Provides
    fun getInventoryRepo(context: Context): InventoryRepository {
        val inventoryDao = InventoryDatabase.getDatabase(context).getInventoryDao()
        return InventoryRepository(inventoryDao, UserPreference(context))
    }


}