package rakki.inventory.basic.inventory

import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import rakki.inventory.basic.R

class DialogViewForCreate// Set the alert dialog title
// Finally, make the alert dialog using builder
    (fragment: Fragment) {
    val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(fragment.activity)
        val inflater = fragment.layoutInflater
        val dialogView = inflater.inflate(rakki.inventory.basic.R.layout.product_dialog_create, null)
        builder.setView(dialogView)
        dialog = builder.create()
    }

    fun setTittle(title: String) {
        dialog.findViewById<TextView>(R.id.pro_dialog_tv_title).text = title
    }

    fun setParentInfo(parent: String) {
        dialog.findViewById<TextView>(R.id.pro_dialog_tv_parent).text = parent
    }

    fun getName(): String {
        return dialog.findViewById<TextView>(R.id.add_product_name).text.toString()
    }

    fun getAddButton(): View {
        return dialog.findViewById<TextView>(R.id.button_add)
    }

    fun showAlert() {
        dialog.show()
        dialog.findViewById<View>(R.id.button_close).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}