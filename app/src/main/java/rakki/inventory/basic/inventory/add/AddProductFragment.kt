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
import android.widget.Spinner
import kotlinx.android.synthetic.main.add_product_fragment.*
import kotlinx.android.synthetic.main.add_product_fragment.view.*
import rakki.inventory.basic.R
import rakki.inventory.basic.inventory.DialogViewForCreate
import rakki.inventory.basic.showToast


class AddProductFragment : Fragment() {
    lateinit var CategorySpinner: Spinner
    lateinit var dialog: DialogViewForCreate

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(rakki.inventory.basic.R.layout.add_product_fragment, container, false)
        CategorySpinner = view.add_product_sp_category
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        setUpObserverForCategory()
        setUpViewAction()
        viewModel.getCategoryName()
        setUpCategorySpinner()
        dialog = DialogViewForCreate(this)
        return view
    }

    private fun setUpCategorySpinner() {
        CategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.categoryItemSelected(CategorySpinner.selectedItem.toString())
            }

        }

    }

    private fun setUpViewAction() {
        viewModel.viewAction.observe(this, Observer {
            if (it != null) {
                for (item in it) {
                    when (item) {
                        AddProductViewModel.ViewCommunicator.ShowCreateCategoryScreen -> showCategoryAddDialog()
                        AddProductViewModel.ViewCommunicator.ShowProgress -> showProgress()
                        AddProductViewModel.ViewCommunicator.HideProgress -> hideProgress()
                        AddProductViewModel.ViewCommunicator.CategoryAlreadyPresent -> errorCategoryAlreadyPresent()
                        AddProductViewModel.ViewCommunicator.CategoryCreated -> categoryCreatedSuccess()
                        AddProductViewModel.ViewCommunicator.InvalidName -> invalidName()
                    }

                }
            }
        })
    }

    private fun invalidName() {

    }

    private fun categoryCreatedSuccess() {
        showToast(getString(R.string.CategoryCreated))
    }

    private fun errorCategoryAlreadyPresent() {
        showToast(getString(R.string.CategoryAlreadyPresent))
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
        dialog.getAddButton().setOnClickListener {
            if (!dialog.getName().isEmpty()) {
                viewModel.addCategory(dialog.getName())
                dialog.dismiss()
            } else invalidName()
        }
    }

    private fun setUpObserverForCategory() {
        viewModel.category.observe(this, Observer {
            CategorySpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, it)
        })
    }


    override fun onDetach() {
        super.onDetach()
        dialog.dismiss()
    }


}
