package rakki.inventory.basic

import android.app.Application
import rakki.inventory.basic.di.AppComponent
import rakki.inventory.basic.di.AppModule
import rakki.inventory.basic.di.DaggerAppComponent

class ApplicationCustom : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    init {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }


}