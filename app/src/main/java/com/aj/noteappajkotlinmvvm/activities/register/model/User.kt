package com.aj.noteappajkotlinmvvm.activities.register.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//userId(Primary Key), userMobileNo, userName, userPassword, userEmail, isRegistered.
@Entity(tableName = "user")
data class User(

//    @PrimaryKey(autoGenerate = true)
//    val userId: Long = 0, //"@PrimaryKey" annotation for primary in DB
//    @ColumnInfo //name param removed but it's accessing variable name in "UserDao"
//    val userId: Int,
    @ColumnInfo
    val userMobileNo : Long, //DataType set to Long for mobileNo
    @ColumnInfo
    val userName : String,
    @ColumnInfo
    val userPassword : String,
    @ColumnInfo
    val userEmail : String,
    @ColumnInfo(defaultValue = "0")
    val isRegistered : Boolean,

    ){
    //Below link resolves the error where we need to use 'var' instead of 'val'
    //https://stackoverflow.com/questions/44213446/cannot-find-setter-for-field-using-kotlin-with-room-database
    //We are specifying here as this value is auto incremented and will be updated by RoomDB
    //This makes it optional value at time of insertion
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0
}