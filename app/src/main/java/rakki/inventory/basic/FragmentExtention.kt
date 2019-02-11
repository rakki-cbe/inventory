package rakki.inventory.basic

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.showToast(msg: String) {
    Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
}