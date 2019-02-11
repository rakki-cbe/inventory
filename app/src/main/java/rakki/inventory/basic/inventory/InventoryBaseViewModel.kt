package rakki.inventory.basic.inventory

import rakki.inventory.basic.ApplicationCustom
import rakki.inventory.basic.BaseViewModel
import javax.inject.Inject

abstract class InventoryBaseViewModel : BaseViewModel() {
    @Inject
    lateinit var inventoryRepo: InventoryRepository

    init {
        ApplicationCustom.appComponent.inject(this@InventoryBaseViewModel)
    }

}