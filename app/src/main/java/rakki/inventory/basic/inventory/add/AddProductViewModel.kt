package rakki.inventory.basic.inventory.add

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rakki.inventory.basic.Entities
import rakki.inventory.basic.inventory.InventoryBaseViewModel
import kotlin.random.Random


class AddProductViewModel : InventoryBaseViewModel() {
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

    private fun checkAllSpinnerState(action: ArrayList<AddProductViewModel.ViewCommunicator>) {
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

    fun saveProductInfo(
        name: String,
        quantityUnit: String,
        quantityUnitKey: String,
        quantityPerItem: String,
        purchaseAmount: String,
        saleAmount: String,
        productCode: String,
        description: String,
        location: String,
        category: Int,
        subCategory: Int,
        brand: Int
    ) {
        val list: ArrayList<ViewCommunicator> = arrayListOf()
        list.add(ViewCommunicator.ShowProgress)
        var quantityUnitDb = 0.0
        var quantityPerItemDb = 0.0
        var purchaseAmountDb = 0.0
        var saleAmountDb = 0.0
        var brandItem: Entities.Brand? = null
        var categoryItem: Entities.Category? = null
        var subCategoryItem: Entities.SubCategory? = null

        if (name.isEmpty()) list.add(ViewCommunicator.ErrorInvalidProductName)
        if (quantityUnit.isEmpty()) list.add(ViewCommunicator.ErrorInvalidQuantityUnit)
        try {
            quantityUnitDb = quantityUnit.toDouble()
        } catch (error: NumberFormatException) {
            list.add(ViewCommunicator.ErrorInvalidQuantityUnit)
        }
        if (quantityPerItem.isEmpty()) list.add(ViewCommunicator.ErrorInvalidItemPerUnit)
        try {
            quantityPerItemDb = quantityPerItem.toDouble()
        } catch (error: NumberFormatException) {
            list.add(ViewCommunicator.ErrorInvalidItemPerUnit)
        }
        if (purchaseAmount.isEmpty()) list.add(ViewCommunicator.ErrorInvalidPurseAmount)
        try {
            purchaseAmountDb = purchaseAmount.toDouble()
        } catch (error: NumberFormatException) {
            list.add(ViewCommunicator.ErrorInvalidPurseAmount)
        }
        if (saleAmount.isEmpty()) list.add(ViewCommunicator.ErrorInvalidSaleAmount)
        try {
            saleAmountDb = saleAmount.toDouble()
        } catch (error: NumberFormatException) {
            list.add(ViewCommunicator.ErrorInvalidSaleAmount)
        }
        if (productCode.isEmpty()) list.add(ViewCommunicator.ErrorInvalidProductCode)
        if (category <= 0 && category == this.category.value!!.size - 1) list.add(ViewCommunicator.ErrorInvalidCategory)
        else categoryItem = this.listCateDb.get(category - 1)
        if (subCategory <= 0 && subCategory == this.subCategory.value!!.size - 1) list.add(ViewCommunicator.ErrorInvalidSubCategory)
        else subCategoryItem = this.listSubCateDb.get(subCategory - 1)
        if (brand <= 0 && brand == this.brand.value!!.size - 1) list.add(ViewCommunicator.ErrorInvalidBrand)
        else brandItem = this.listBrandDb.get(brand - 1)
        viewAction.value = list
        list.clear()
        if (list.size <= 1 && categoryItem != null && subCategoryItem != null && brandItem != null) {
            /**Validation passed need to check db validation**/
            scope.launch(Dispatchers.IO) {
                var product = inventoryRepo.checkProductAlreadyPresent(
                    categoryItem.id!!, subCategoryItem.id!!, brandItem.id!!, name,
                    quantityPerItemDb,
                    purchaseAmountDb, saleAmountDb
                )
                if (product == null) {
                    product = Entities.Product(
                        categoryItem.id!!, subCategoryItem.id!!, brandItem.id!!, productCode, name,
                        quantityPerItemDb,
                        quantityUnitKey,
                        purchaseAmountDb,
                        saleAmountDb,
                        quantityUnitDb,
                        description,
                        location
                    )

                    product.id = System.currentTimeMillis()
                    val id = inventoryRepo.addNewProduct(product)
                    if (id != null && id > -1) {
                        list.add(ViewCommunicator.ProductCreatedSuccessfully)
                        list.add(ViewCommunicator.ClearProductInfo)
                        list.add(ViewCommunicator.HideProgress)
                    }
                } else {
                    list.add(ViewCommunicator.ErrorInvalidProductWithSameDetailsPresent)
                    list.add(ViewCommunicator.HideProgress)
                }
                scope.launch(Dispatchers.Main) {
                    viewAction.value = list

                }
            }

        } else {
            list.add(ViewCommunicator.HideProgress)
            viewAction.value = list
        }


    }

    fun getProductCodeSuggestedText(brand: String, name: String, quantity: String): String {
        val quantityInt = if (quantity.isEmpty()) 0 else quantity.toInt()
        var productCode = brand.substring(0, if (brand.length > 6) 6 else brand.length) + "_" +
                name.substring(0, if (name.length > 6) 6 else name.length) + "_" +
                quantityInt.toString().substring(0, quantityInt.toString().length)
        val rand: Int = Random.nextInt(1000, 99999)
        productCode += "_" + rand
        return productCode
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
        ErrorInvalidProductName,
        ErrorInvalidQuantityUnit,
        ErrorInvalidItemPerUnit,
        ErrorInvalidPurseAmount,
        ErrorInvalidSaleAmount,
        ErrorInvalidProductCode,
        ErrorInvalidProductWithSameDetailsPresent,
        ProductCreatedSuccessfully,
        ClearProductInfo,
        ClearScreen,

        ErrorInvalidCategory,

        ErrorInvalidSubCategory,

        ErrorInvalidBrand
    }

}
