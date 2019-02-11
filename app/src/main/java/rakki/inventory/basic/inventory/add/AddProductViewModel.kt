package rakki.inventory.basic.inventory.add

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.Entities
import rakki.inventory.basic.inventory.InventoryBaseViewModel


class AddProductViewModel : InventoryBaseViewModel() {

    val category = MutableLiveData<ArrayList<String>>()
    val viewAction = MutableLiveData<ArrayList<ViewCommunicator>>()

    fun getCategoryName() {
        val list = ArrayList<String>()
        scope.launch(Dispatchers.IO) {
            list.add(CategorySpinnerDefaultItem.SelectCategoryName.name)
            val listDb = inventoryRepo.getAllCategory()
            list.addAll(getStringFromList(listDb))
            list.add(CategorySpinnerDefaultItem.CreateCategory.name)
            scope.launch(Dispatchers.Main) {
                category.value = list
            }
        }

    }

    private fun getStringFromList(listDb: List<Entities.Category>): Collection<String> {
        val list = ArrayList<String>()
        for (cat in listDb) {
            list.add(cat.categoryName)
        }
        return list
    }

    fun categoryItemSelected(selectedItem: String) {
        if (selectedItem.equals(CategorySpinnerDefaultItem.CreateCategory.name)) {
            val action: ArrayList<ViewCommunicator> = arrayListOf()
            action.add(ViewCommunicator.ShowCreateCategoryScreen)
            viewAction.value = action
        }

    }

    fun addCategory(name: String) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        action.add(ViewCommunicator.ShowProgress)
        viewAction.value = action
        scope.launch(Dispatchers.Main) {
            val category = checkCategoryAlreadyPresent(name)
            if (category == null) {
                insert(Entities.Category(name, "", null))
                getCategoryName()
                action.clear()
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.CategoryCreated)

            } else {
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.CategoryAlreadyPresent)

            }
            viewAction.value = action

        }

    }

    fun insert(category: Entities.Category) = scope.launch(Dispatchers.IO) {
        inventoryRepo.insert(category)
    }


    private suspend fun checkCategoryAlreadyPresent(cateName: String): Entities.Category? {


        return scope.async(Dispatchers.IO) { inventoryRepo.getCategoryBasedOnName(cateName) }.await()


    }

    enum class CategorySpinnerDefaultItem {
        SelectCategoryName, CreateCategory
    }

    enum class ViewCommunicator {
        ShowCreateCategoryScreen, ShowProgress, HideProgress, CategoryCreated, CategoryAlreadyPresent, InvalidName
    }

}
