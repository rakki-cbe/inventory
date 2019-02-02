package rakki.inventory.basic

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

sealed class Entities {

    @Entity(tableName = "UserInfo")
    data class UserDetails(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var userName: String? = null,
        var userPassword: String? = null,
        var userRole: Int = 0,
        var userFullName: String? = null
    )

}