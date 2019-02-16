package rakki.inventory.basic.inventory.add

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.launch
import rakki.inventory.basic.Entities
import rakki.inventory.basic.inventory.InventoryBaseViewModel
import kotlin.random.Random


class AddProductInfoViewModel : InventoryBaseViewModel() {
    private var selectedCategory: Entities.Category? = null
    private var selectedSubCategory: Entities.SubCategory? = null
    private var selectedBrand: Entities.Brand? = null
    val viewAction = MutableLiveData<ArrayList<ViewCommunicator>>()


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
        categoryItem: Entities.Category?,
        subCategoryItem: Entities.SubCategory?,
        brandItem: Entities.Brand?
    ) {
        val list: ArrayList<ViewCommunicator> = arrayListOf()
        list.add(ViewCommunicator.ShowProgress)
        var quantityUnitDb = 0.0
        var quantityPerItemDb = 0.0
        var purchaseAmountDb = 0.0
        var saleAmountDb = 0.0

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
        viewAction.value = list

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
        list.clear()


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
        ShowProgress,
        HideProgress,
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
