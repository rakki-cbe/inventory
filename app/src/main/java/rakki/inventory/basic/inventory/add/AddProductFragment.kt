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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.add_product_fragment.*
import rakki.inventory.basic.R
import rakki.inventory.basic.inventory.DialogViewForCreate
import rakki.inventory.basic.showToast


class AddProductFragment : Fragment() {
    private lateinit var dialog: DialogViewForCreate
    private lateinit var viewModel: AddProductViewModel
    companion object {
        fun newInstance() = AddProductFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(rakki.inventory.basic.R.layout.add_product_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        dialog = DialogViewForCreate(this)
        setUpObserverForCategory()
        setUpObserverForBrand()
        setUpObserverForSubCategory()
        setUpViewAction()
        viewModel.getCategoryName()
        viewModel.getBrandName()
        setUpCategorySpinner()
        setUpBrandSpinner()
        setUpSubCategorySpinner()
        button_add.setOnClickListener {
            viewModel.saveProductInfo(
                add_product_ed_name.text.toString(),
                add_product_ed_quantity_item_pack.text.toString(),
                add_product_sp_unit.selectedItem.toString(),
                add_product_ed_quantity_unit.text.toString(),
                add_product_ed_amount.text.toString(), add_product_ed_sale_amount.text.toString(),
                add_product_ed_product_code.text.toString(), add_product_ed_description.text.toString(),
                add_product_ed_location.text.toString(), add_product_sp_category.selectedItemPosition,
                add_product_sp_sub_category.selectedItemPosition, add_product_sp_brand.selectedItemPosition
            )
        }
        button_close.setOnClickListener { clearProductInfo() }

        add_product_ed_name.addTextChangedListener(ProductCodeGenerater())
        add_product_ed_quantity_unit.addTextChangedListener(ProductCodeGenerater())


    }

    private fun setUpCategorySpinner() {
        add_product_sp_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.categoryItemSelected(add_product_sp_category.selectedItem.toString(), position)
            }

        }

    }

    private fun setUpSubCategorySpinner() {
        add_product_sp_sub_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.subCategoryItemSelected(add_product_sp_sub_category.selectedItem.toString(), position)
            }

        }

    }

    private fun setUpBrandSpinner() {
        add_product_sp_brand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.brandSelected(add_product_sp_brand.selectedItem.toString(), position)
            }

        }

    }


    private fun hideProgress() {
        add_product_login_progress.visibility = View.GONE
    }

    private fun showProgress() {
        add_product_login_progress.visibility = View.VISIBLE
    }


    private fun showCategoryAddDialog() {
        dialog.showAlert()
        dialog.setTittle(getString(R.string.CreateCategory))
        dialog.setParentInfoVisibility(false)
        dialog.getAddButton().setOnClickListener {
            if (!dialog.getName().isEmpty()) {
                viewModel.addCategory(dialog.getName())
                dialog.dismiss()
            } else invalidName()
        }
    }

    private fun categoryCreatedSuccess() {
        showToast(getString(R.string.CategoryCreated))
    }

    private fun subCategoryCreatedSuccess() {
        showToast(getString(R.string.subCategoryCreated))
    }

    private fun brandCreatedSuccess() {
        showToast(getString(R.string.brandCreatedSuccess))
    }

    private fun productCreatedSuccessfully() {
        showToast(getString(R.string.productCreated))
    }

    private fun errorProductAlreadyPresent() {
        showToast(getString(R.string.productAlreadyPresent))
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

    private fun errorCategoryAlreadyPresent() {
        showToast(getString(R.string.CategoryAlreadyPresent))
    }

    private fun errorSubCategoryAlreadyPresent() {
        showToast(getString(R.string.SubCategoryAlreadyPresent))
    }

    private fun errorBrandAlreadyPresent() {
        showToast(getString(R.string.brandAlreadyPresent))
    }

    private fun invalidName() {

    }


    private fun showSubCategoryAddDialog() {
        dialog.showAlert()
        dialog.setTittle(getString(R.string.CreateSubCategory))
        dialog.setParentInfo(add_product_sp_category.selectedItem.toString())
        dialog.setParentInfoVisibility(true)

        dialog.getAddButton().setOnClickListener {
            if (!dialog.getName().isEmpty()) {
                viewModel.addSubCategory(dialog.getName(), add_product_sp_category.selectedItemPosition)
                dialog.dismiss()
            } else invalidName()
        }
    }

    private fun showBrandAddDialog() {
        dialog.showAlert()
        dialog.setTittle(getString(R.string.CreateBrand))
        dialog.setParentInfoVisibility(false)
        dialog.getAddButton().setOnClickListener {
            if (!dialog.getName().isEmpty()) {
                viewModel.addBrand(dialog.getName())
                dialog.dismiss()
            } else invalidName()
        }
    }

    private fun hideSubCategorySpinner() {
        add_product_ll_subCategory.visibility = View.GONE
        if (add_product_sp_sub_category.adapter != null)
            add_product_sp_sub_category.setSelection(0)
    }

    private fun showSubCategorySpinner() {
        add_product_ll_subCategory.visibility = View.VISIBLE
    }

    private fun hideProductInputDetailScreen() {
        clearProductInfo()
        add_product_CL_product.visibility = View.GONE
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

    private fun showProductInputDetailScreen() {
        generateProductCode()
        add_product_CL_product.visibility = View.VISIBLE
    }

    fun generateProductCode() {
        add_product_ed_product_code.setText(
            viewModel.getProductCodeSuggestedText(
                add_product_sp_brand.selectedItem.toString(), add_product_ed_name.text.toString(),
                add_product_ed_quantity_unit.text.toString()
            )
        )
    }

    private fun setUpObserverForCategory() {
        viewModel.category.observe(this, Observer {
            add_product_sp_category.adapter =
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, it)
        })
    }

    private fun setUpObserverForSubCategory() {
        viewModel.subCategory.observe(this, Observer {
            add_product_sp_sub_category.adapter =
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, it)
        })
    }

    private fun setUpObserverForBrand() {
        viewModel.brand.observe(this, Observer {
            add_product_sp_brand.adapter =
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, it)
        })
    }


    private fun setUpViewAction() {
        viewModel.viewAction.observe(this, Observer {
            if (it != null) {
                for (item in it) {
                    when (item) {
                        AddProductViewModel.ViewCommunicator.ShowProgress -> showProgress()
                        AddProductViewModel.ViewCommunicator.HideProgress -> hideProgress()
                        AddProductViewModel.ViewCommunicator.ShowCreateCategoryScreen -> showCategoryAddDialog()
                        AddProductViewModel.ViewCommunicator.CategoryAlreadyPresent -> errorCategoryAlreadyPresent()
                        AddProductViewModel.ViewCommunicator.CategoryCreated -> categoryCreatedSuccess()
                        AddProductViewModel.ViewCommunicator.ShowSubCategorySpinner -> showSubCategorySpinner()
                        AddProductViewModel.ViewCommunicator.HideSubCategorySpinner -> hideSubCategorySpinner()
                        AddProductViewModel.ViewCommunicator.ShowCreateSubCategoryScreen -> showSubCategoryAddDialog()
                        AddProductViewModel.ViewCommunicator.SubCategoryAlreadyPresent -> errorSubCategoryAlreadyPresent()
                        AddProductViewModel.ViewCommunicator.SubCategoryCreated -> subCategoryCreatedSuccess()
                        AddProductViewModel.ViewCommunicator.ShowCreateBrandScreen -> showBrandAddDialog()
                        AddProductViewModel.ViewCommunicator.BrandAlreadyPresent -> errorBrandAlreadyPresent()
                        AddProductViewModel.ViewCommunicator.BrandCreated -> brandCreatedSuccess()
                        AddProductViewModel.ViewCommunicator.ShowProductInputDetailsScreen -> showProductInputDetailScreen()
                        AddProductViewModel.ViewCommunicator.HideProductInputDetailsScreen -> hideProductInputDetailScreen()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidProductName -> errorProductName()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidItemPerUnit -> errorItemPerUnit()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidQuantityUnit -> errorQuantityUnit()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidPurseAmount -> errorPurchaseAmount()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidSaleAmount -> errorSaleAmount()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidProductCode -> errorProductCode()
                        AddProductViewModel.ViewCommunicator.ErrorInvalidProductWithSameDetailsPresent ->
                            errorProductAlreadyPresent()
                        AddProductViewModel.ViewCommunicator.ProductCreatedSuccessfully -> productCreatedSuccessfully()
                        AddProductViewModel.ViewCommunicator.InvalidName -> invalidName()
                        AddProductViewModel.ViewCommunicator.ClearProductInfo -> clearProductInfo()

                    }

                }
            }
        })
    }



    override fun onDetach() {
        super.onDetach()
        dialog.dismiss()
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

}
