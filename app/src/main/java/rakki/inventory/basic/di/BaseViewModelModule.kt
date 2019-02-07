package rakki.inventory.basic.di

import android.content.Context
import dagger.Module
import dagger.Provides
import rakki.inventory.basic.InventoryDatabase
import rakki.inventory.basic.UserRepository
import javax.inject.Singleton

@Module
class BaseViewModelModule {
    @Singleton
    @Provides
    fun getRepository(context: Context): UserRepository {
        val userDao = InventoryDatabase.getDatabase(context).getUserDao()
        return UserRepository(userDao)
    }

}