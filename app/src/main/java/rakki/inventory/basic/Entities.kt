package rakki.inventory.basic

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

sealed class Entities {

    @Entity(tableName = "UserInfo")
    data class UserDetails(
        var userName: String? = null,
        var userPassword: String? = null,
        var userRole: Int = 0,
        var userFullName: String? = null,
        var ivInfo: String? = null
    ) {
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null
    }


}