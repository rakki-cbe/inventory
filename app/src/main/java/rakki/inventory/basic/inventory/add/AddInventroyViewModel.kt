package rakki.inventory.basic.inventory.add

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.Entities
import rakki.inventory.basic.inventory.InventoryBaseViewModel


class AddInventroyViewModel : InventoryBaseViewModel() {
    private var listCateDb: List<Entities.Category> = arrayListOf()
    private var listSubCateDb: List<Entities.SubCategory> = arrayListOf()
    private var listBrandDb: List<Entities.Brand> = arrayListOf()
    private var selectedCategory: Entities.Category? = null
    private var selectedSubCategory: Entities.SubCategory? = null
    private var selectedBrand: Entities.Brand? = null
    val category = MutableLiveData<ArrayList<String>>()
    val subCategory = MutableLiveData<ArrayList<String>>()
    val brand = MutableLiveData<ArrayList<String>>()
    val viewAction = MutableLiveData<ArrayList<ViewCommunicator>>()

    fun getSelectedCategory(): Entities.Category? {
        return selectedCategory
    }

    fun getSelectedSubCategory(): Entities.SubCategory? {
        return selectedSubCategory
    }

    fun getSelectedBrand(): Entities.Brand? {
        return selectedBrand
    }
    fun getCategoryName() {
        val list = ArrayList<String>()
        scope.launch(Dispatchers.IO) {
            list.add(ViewCommunicator.Select.name)
            listCateDb = inventoryRepo.getAllCategory()
            list.addAll(getStringFromListCate(listCateDb))
            list.add(ViewCommunicator.CreateNew.name)
            scope.launch(Dispatchers.Main) {
                category.value = list
            }
        }

    }

    fun getBrandName() {
        val list = ArrayList<String>()
        scope.launch(Dispatchers.IO) {
            list.add(ViewCommunicator.Select.name)
            listBrandDb = inventoryRepo.getAllBrand()
            list.addAll(getStringFromListBrand(listBrandDb))
            list.add(ViewCommunicator.CreateNew.name)
            scope.launch(Dispatchers.Main) {
                brand.value = list
            }
        }

    }

    fun getSubCategory(category: Entities.Category) {
        val list = ArrayList<String>()
        scope.launch(Dispatchers.IO) {
            list.add(ViewCommunicator.Select.name)
            listSubCateDb = inventoryRepo.getAllSubCategory(category.id!!)
            list.addAll(getStringFromListSub(listSubCateDb))
            list.add(ViewCommunicator.CreateNew.name)
            scope.launch(Dispatchers.Main) {
                subCategory.value = list
            }
        }

    }


    private fun getStringFromListCate(listDb: List<Entities.Category>): Collection<String> {
        val list = ArrayList<String>()
        for (cat in listDb) {
            list.add(cat.categoryName)
        }
        return list
    }

    private fun getStringFromListBrand(listDb: List<Entities.Brand>): Collection<String> {
        val list = ArrayList<String>()
        for (brand in listDb) {
            list.add(brand.brandName)
        }
        return list
    }

    private fun getStringFromListSub(listDb: List<Entities.SubCategory>): Collection<String> {
        val list = ArrayList<String>()
        for (cat in listDb) {
            list.add(cat.subCategoryName)
        }
        return list
    }

    fun categoryItemSelected(selectedItem: String, position: Int) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        selectedCategory = null
        if (selectedItem.equals(ViewCommunicator.CreateNew.name)) {

            action.add(ViewCommunicator.ShowCreateCategoryScreen)
            action.add(ViewCommunicator.HideSubCategorySpinner)
        } else if (selectedItem.equals(ViewCommunicator.Select.name)) {
            action.add(ViewCommunicator.HideSubCategorySpinner)
        } else {
            selectedCategory = listCateDb.get(position - 1)
            getSubCategory(selectedCategory!!)
            action.add(ViewCommunicator.ShowSubCategorySpinner)

        }
        checkAllSpinnerState(action)
        viewAction.value = action


    }

    private fun checkAllSpinnerState(action: ArrayList<AddInventroyViewModel.ViewCommunicator>) {
        if (selectedCategory != null && selectedBrand != null && selectedSubCategory != null) {
            action.add(ViewCommunicator.ShowProductInputDetailsScreen)
        } else
            action.add(ViewCommunicator.HideProductInputDetailsScreen)
    }

    fun subCategoryItemSelected(selectedItem: String, position: Int) {
        selectedSubCategory = null
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        if (selectedItem.equals(ViewCommunicator.CreateNew.name)) {

            action.add(ViewCommunicator.ShowCreateSubCategoryScreen)

        } else if (selectedItem.equals(ViewCommunicator.Select.name)) {

        } else {
            selectedSubCategory = listSubCateDb.get(position - 1)
        }
        checkAllSpinnerState(action)
        viewAction.value = action

    }

    fun brandSelected(selectedItem: String, position: Int) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        selectedBrand = null
        if (selectedItem.equals(ViewCommunicator.CreateNew.name)) {
            action.add(ViewCommunicator.ShowCreateBrandScreen)

        } else if (selectedItem.equals(ViewCommunicator.Select.name)) {

        } else {
            selectedBrand = listBrandDb.get(position - 1)
        }
        checkAllSpinnerState(action)
        viewAction.value = action
        viewAction.value = action
    }


    fun addCategory(name: String) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        action.add(ViewCommunicator.ShowProgress)
        viewAction.value = action
        scope.launch(Dispatchers.Main) {
            val category = checkCategoryAlreadyPresent(name)
            if (category == null) {
                insert(Entities.Category(name, ""))
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

    fun addSubCategory(subCateName: String, catePostion: Int) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        action.add(ViewCommunicator.ShowProgress)
        viewAction.value = action
        scope.launch(Dispatchers.IO) {
            val subCategory = inventoryRepo.getSubCategoryBasedOnName(subCateName, listCateDb.get(catePostion - 1).id!!)
            if (subCategory == null) {
                inventoryRepo.insert(
                    Entities.SubCategory(
                        listCateDb.get(catePostion - 1).id!!, subCateName,
                        "", null
                    )
                )
                getSubCategory(listCateDb.get(catePostion - 1))
                action.clear()
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.SubCategoryCreated)

            } else {
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.SubCategoryAlreadyPresent)

            }
            scope.launch(Dispatchers.Main) { viewAction.value = action; }

        }
    }

    fun addBrand(name: String) {
        val action: ArrayList<ViewCommunicator> = arrayListOf()
        action.add(ViewCommunicator.ShowProgress)
        viewAction.value = action
        scope.launch(Dispatchers.IO) {
            val subCategory = inventoryRepo.getbrandBasedOnName(name)
            if (subCategory == null) {
                inventoryRepo.insert(
                    Entities.Brand(
                        name,
                        "", null
                    )
                )
                action.clear()
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.BrandCreated)

            } else {
                action.add(ViewCommunicator.HideProgress)
                action.add(ViewCommunicator.BrandAlreadyPresent)

            }
            scope.launch(Dispatchers.Main) { viewAction.value = action; }

        }

    }


    fun insert(category: Entities.Category) = scope.launch(Dispatchers.IO) {
        inventoryRepo.insert(category)
    }


    private suspend fun checkCategoryAlreadyPresent(cateName: String): Entities.Category? {


        return scope.async(Dispatchers.IO) { inventoryRepo.getCategoryBasedOnName(cateName) }.await()


    }


    fun showProgress() {
        val list: ArrayList<AddInventroyViewModel.ViewCommunicator> = arrayListOf()
        list.add(AddInventroyViewModel.ViewCommunicator.ShowProgress)
        viewAction.value = list
    }

    fun hideProgress() {
        val list: ArrayList<AddInventroyViewModel.ViewCommunicator> = arrayListOf()
        list.add(AddInventroyViewModel.ViewCommunicator.HideProgress)
        viewAction.value = list
    }


    enum class ViewCommunicator {
        ShowCreateCategoryScreen,
        ShowProgress,
        HideProgress,
        CategoryCreated,
        CategoryAlreadyPresent,
        InvalidName,
        SubCategoryCreated,
        SubCategoryAlreadyPresent,
        ShowCreateSubCategoryScreen,
        ShowSubCategorySpinner,
        HideSubCategorySpinner,
        ShowCreateBrandScreen,
        BrandAlreadyPresent,
        BrandCreated, Select,
        CreateNew,
        ShowProductInputDetailsScreen,
        HideProductInputDetailsScreen,

    }

}
