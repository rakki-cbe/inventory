package rakki.inventory.basic.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application) {
    @Provides
    fun getApplicationContext(): Context {
        return application.applicationContext
    }


}