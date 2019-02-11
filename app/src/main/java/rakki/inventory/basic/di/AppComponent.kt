package rakki.inventory.basic.di

import android.content.Context
import dagger.Component
import rakki.inventory.basic.BaseViewModel
import rakki.inventory.basic.inventory.InventoryBaseViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [BaseViewModelModule::class, AppModule::class, InventoryViewModelModule::class])
interface AppComponent {
    fun getContext(): Context
    fun inject(baseViewModel: BaseViewModel)
    fun inject(addProductViewModel: InventoryBaseViewModel)
}