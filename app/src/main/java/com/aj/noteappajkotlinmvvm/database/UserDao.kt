package com.aj.noteappajkotlinmvvm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aj.noteappajkotlinmvvm.activities.register.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) //ignore any new note that is exactly the same as one already in the list.
    suspend fun addUser(user: User): Long? //Long type added for returning single value which will be "userId"

    //Here we fetch only 'userId' in return as we only need that value & not other values
    @Query("SELECT userId FROM user WHERE userMobileNo =:mobileNo AND isRegistered =:regStatus")
    suspend fun getUserByMobileNumber(mobileNo : Long,regStatus : Boolean): Long?

    @Query("SELECT userId FROM user WHERE userId =:userId AND userPassword=:password")
    suspend fun getUserByPassword(userId : Long,password : String): Long?
}