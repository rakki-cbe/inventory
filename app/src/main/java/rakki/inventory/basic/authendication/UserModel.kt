package rakki.inventory.basic.authendication

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class UserModel {
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0
    private val userName: String? = null
    private val userPassword: String? = null
    private val userRole: Int = 0
}