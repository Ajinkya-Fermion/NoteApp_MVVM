package com.aj.noteappajkotlinmvvm.repository.local

import com.aj.noteappajkotlinmvvm.activities.register.model.User
import com.aj.noteappajkotlinmvvm.database.NoteDatabase

class UserRepository(private val db: NoteDatabase) {

    //db.getUserDao.insertUser(user) :
    //1) db => singleTon DB instance param used to get 'getUserDao' interface,
    //2) getUserDao => this interface is used to call 'insertUser(user)' method in User class,
    //3) insertNote() => this is actual SQL query triggering with/without return type.
    suspend fun insertUser(user: User) = db.userDao().addUser(user)

    suspend fun getUserByMobileNo(mobileNo : Long, regStatus : Boolean) =
        db.userDao().getUserByMobileNumber(mobileNo,regStatus)

    suspend fun getUserByPassword(userId : Long, password : String) =
        db.userDao().getUserByPassword(userId, password)

}