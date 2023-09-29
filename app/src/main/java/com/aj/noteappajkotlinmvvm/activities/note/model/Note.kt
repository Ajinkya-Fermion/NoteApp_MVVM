package com.aj.noteappajkotlinmvvm.activities.note.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aj.noteappajkotlinmvvm.activities.register.model.User
import kotlinx.parcelize.Parcelize

//We are using below class for 2 purposes:
// 1) For creating Database class 'notes' table creation required for SQL operation.
// 2) For transferring data as object(Note) required in NoteActivity.
//Please check below solution for Foreign key warning.
//https://stackoverflow.com/questions/44480761/android-room-compile-time-warning-about-column-in-foreign-key-not-part-of-an-ind
@Entity(tableName = "notes",foreignKeys = [ForeignKey(
    entity = User::class,
    childColumns = ["userId"],
    parentColumns = ["userId"]
)])
@Parcelize
data class Note(
    @ColumnInfo(name = "noteTitle") //"@ColumnInfo" is name of the column in DB
    val noteTitle: String,
    @ColumnInfo(name = "noteContent")
    val noteContent : String,
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    @ColumnInfo(name="userId") //This will be used as foreign key referencing 'User' as parent table
    val userId:Long
): Parcelable
