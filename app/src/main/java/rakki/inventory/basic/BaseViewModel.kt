package rakki.inventory.basic

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var parentJob = Job()
    val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    val scope = CoroutineScope(coroutineContext)

    val repository: UserRepository

    init {
        val userDao = InventoryDatabase.getDatabase(application).getUserDao()
        repository = UserRepository(userDao)

    }

}