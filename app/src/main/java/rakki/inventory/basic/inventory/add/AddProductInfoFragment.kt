package rakki.inventory.basic.inventory.add


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_product_info.*

import rakki.inventory.basic.R
import rakki.inventory.basic.showToast

class AddProductInfoFragment : Fragment() {
    lateinit var viewModel: AddProductInfoViewModel
    lateinit var viewModelInventory: AddInventroyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this.parentFragment != null)
            viewModelInventory = ViewModelProviders.of(this.parentFragment!!).get(AddInventroyViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(AddProductInfoViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewAction()
        button_add.setOnClickListener {
            viewModel.saveProductInfo(
                add_product_ed_name.text.toString(),
                add_product_ed_quantity_item_pack.text.toString(),
                add_product_sp_unit.selectedItem.toString(),
                add_product_ed_quantity_unit.text.toString(),
                add_product_ed_amount.text.toString(), add_product_ed_sale_amount.text.toString(),
                add_product_ed_product_code.text.toString(), add_product_ed_description.text.toString(),
                add_product_ed_location.text.toString(), viewModelInventory.getSelectedCategory(),
                viewModelInventory.getSelectedSubCategory(),
                viewModelInventory.getSelectedBrand()
            )
        }
        button_close.setOnClickListener { clearProductInfo() }

        add_product_ed_name.addTextChangedListener(ProductCodeGenerater())
        add_product_ed_quantity_unit.addTextChangedListener(ProductCodeGenerater())

    }

    private fun showProgress() {
        viewModelInventory.showProgress()
    }

    private fun hideProgress() {
        viewModelInventory.hideProgress()
    }

    private fun errorProductCode() {
        add_product_ed_product_code.error = getString(R.string.invalidProductCode)
    }

    private fun errorSaleAmount() {
        add_product_ed_sale_amount.error = getString(R.string.invalidSaleAmount)
    }

    private fun errorPurchaseAmount() {
        add_product_ed_amount.error = getString(R.string.invalidPurchaseAmount)
    }

    private fun errorQuantityUnit() {
        add_product_ed_quantity_unit.error = getString(R.string.invalidQuantityUnit)
    }

    private fun errorItemPerUnit() {
        add_product_ed_quantity_item_pack.error = getString(R.string.invalidInvidualItemQuantity)
    }

    private fun errorProductName() {
        add_product_ed_name.error = getString(R.string.invalidProductName)
    }

    private fun productCreatedSuccessfully() {
        showToast(getString(R.string.productCreated))
    }

    private fun errorProductAlreadyPresent() {
        showToast(getString(R.string.productAlreadyPresent))
    }


    private fun clearProductInfo() {
        add_product_ed_name.setText("")
        add_product_ed_name.error = null
        add_product_ed_quantity_unit.setText("")
        add_product_ed_quantity_unit.error = null
        add_product_ed_quantity_item_pack.setText("")
        add_product_ed_quantity_item_pack.error = null
        add_product_ed_amount.setText("")
        add_product_ed_amount.error = null
        add_product_ed_sale_amount.setText("")
        add_product_ed_sale_amount.error = null
        add_product_ed_product_code.setText("")
        add_product_ed_product_code.error = null
        add_product_ed_description.setText("")
        add_product_ed_description.error = null
        add_product_ed_location.setText("")
        add_product_ed_location.error = null

    }

    fun generateProductCode() {
        val brand =
            if (viewModelInventory.getSelectedBrand() == null) "" else viewModelInventory.getSelectedBrand()!!.brandName
        add_product_ed_product_code.setText(
            viewModel.getProductCodeSuggestedText(
                brand, add_product_ed_name.text.toString(),
                add_product_ed_quantity_unit.text.toString()
            )
        )
    }

    inner class ProductCodeGenerater : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!s.isNullOrEmpty())
                generateProductCode()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }


    private fun setUpViewAction() {
        viewModel.viewAction.observe(this, Observer {
            if (it != null) {
                for (item in it) {
                    when (item) {
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidProductName -> errorProductName()
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidItemPerUnit -> errorItemPerUnit()
                        AddProductInfoViewModel.ViewCommunicator.ShowProgress -> showProgress()
                        AddProductInfoViewModel.ViewCommunicator.HideProgress -> hideProgress()

                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidQuantityUnit -> errorQuantityUnit()
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidPurseAmount -> errorPurchaseAmount()
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidSaleAmount -> errorSaleAmount()
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidProductCode -> errorProductCode()
                        AddProductInfoViewModel.ViewCommunicator.ClearProductInfo -> clearProductInfo()
                        AddProductInfoViewModel.ViewCommunicator.ErrorInvalidProductWithSameDetailsPresent ->
                            errorProductAlreadyPresent()
                        AddProductInfoViewModel.ViewCommunicator.ProductCreatedSuccessfully -> productCreatedSuccessfully()

                    }

                }
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = AddProductInfoFragment()
    }
}
