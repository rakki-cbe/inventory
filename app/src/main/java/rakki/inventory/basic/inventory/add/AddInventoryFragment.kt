package rakki.inventory.basic.inventory.add

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_add_inventory.*
import rakki.inventory.basic.R
import rakki.inventory.basic.inventory.DialogViewForCreate
import rakki.inventory.basic.showToast


class AddInventoryFragment : Fragment() {
    private lateinit var dialog: DialogViewForCreate
    private lateinit var viewModel: AddInventroyViewModel
    var addProductInfoFragment: AddProductInfoFragment? = null
    companion object {
        fun newInstance() = AddInventoryFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(rakki.inventory.basic.R.layout.fragment_add_inventory, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddInventroyViewModel::class.java)
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
        if (addProductInfoFragment != null) {
            childFragmentManager.beginTransaction()
                .remove(addProductInfoFragment!!)
                .commitNow()
            addProductInfoFragment = null
        }
    }

    private fun showProductInputDetailScreen() {
        if (addProductInfoFragment == null) {
            addProductInfoFragment = AddProductInfoFragment.newInstance()
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.add_product_Fl_product, addProductInfoFragment!!)
            .commitNow()
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
                        AddInventroyViewModel.ViewCommunicator.ShowProgress -> showProgress()
                        AddInventroyViewModel.ViewCommunicator.HideProgress -> hideProgress()
                        AddInventroyViewModel.ViewCommunicator.ShowCreateCategoryScreen -> showCategoryAddDialog()
                        AddInventroyViewModel.ViewCommunicator.CategoryAlreadyPresent -> errorCategoryAlreadyPresent()
                        AddInventroyViewModel.ViewCommunicator.CategoryCreated -> categoryCreatedSuccess()
                        AddInventroyViewModel.ViewCommunicator.ShowSubCategorySpinner -> showSubCategorySpinner()
                        AddInventroyViewModel.ViewCommunicator.HideSubCategorySpinner -> hideSubCategorySpinner()
                        AddInventroyViewModel.ViewCommunicator.ShowCreateSubCategoryScreen -> showSubCategoryAddDialog()
                        AddInventroyViewModel.ViewCommunicator.SubCategoryAlreadyPresent -> errorSubCategoryAlreadyPresent()
                        AddInventroyViewModel.ViewCommunicator.SubCategoryCreated -> subCategoryCreatedSuccess()
                        AddInventroyViewModel.ViewCommunicator.ShowCreateBrandScreen -> showBrandAddDialog()
                        AddInventroyViewModel.ViewCommunicator.BrandAlreadyPresent -> errorBrandAlreadyPresent()
                        AddInventroyViewModel.ViewCommunicator.BrandCreated -> brandCreatedSuccess()
                        AddInventroyViewModel.ViewCommunicator.ShowProductInputDetailsScreen -> showProductInputDetailScreen()
                        AddInventroyViewModel.ViewCommunicator.HideProductInputDetailsScreen -> hideProductInputDetailScreen()
                        AddInventroyViewModel.ViewCommunicator.InvalidName -> invalidName()


                    }

                }
            }
        })
    }



    override fun onDetach() {
        super.onDetach()
        dialog.dismiss()
    }


}
