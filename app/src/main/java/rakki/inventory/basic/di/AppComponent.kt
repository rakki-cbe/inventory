package rakki.inventory.basic.di

import android.content.Context
import dagger.Component
import rakki.inventory.basic.BaseViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [BaseViewModelModule::class, AppModule::class])
interface AppComponent {
    fun getContext(): Context
    fun inject(viewModel: BaseViewModel)

}