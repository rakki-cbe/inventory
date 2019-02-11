package rakki.inventory.basic.authendication

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import rakki.inventory.basic.Entities

@Dao
interface UserDao {
    @Query("SELECT * from UserInfo ")
    fun getAllUsers(): LiveData<List<Entities.UserDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: Entities.UserDetails)

    @Query("DELETE FROM UserInfo")
    fun deleteAll()

    @Query("SELECT * FROM UserInfo WHERE userName=:mUserName")
    fun getUser(mUserName: String): Entities.UserDetails?

    @Query("SELECT * FROM UserInfo WHERE id=:userId")
    fun getUserById(userId: Int): Entities.UserDetails?
}