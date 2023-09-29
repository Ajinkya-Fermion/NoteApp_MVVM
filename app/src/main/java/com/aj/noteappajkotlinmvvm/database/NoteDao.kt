package com.aj.noteappajkotlinmvvm.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aj.noteappajkotlinmvvm.activities.note.model.Note

@Dao //Data Access Object
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) //ignore any new note that is exactly the same as one already in the list.
    suspend fun addNote(note: Note): Long?

    @Query("SELECT * FROM notes WHERE userId=:userId ORDER BY noteId DESC")
    fun getNotes(userId: Long): List<Note>
    //Flow is 'coroutine' concept which can update the UI automatically every time
    // we make a change to the list. In other words, we observe the changes

    //@Update keyword when updating whole object
    //When updating only specific fields we use only '@Query' annotations
    //Here 'Int' will only return type as callback to tell how many rows is updated.
    //https://stackoverflow.com/questions/48519896/room-update-or-insert-if-not-exist-rows-and-return-count-changed-rows
    @Query("UPDATE notes SET noteTitle = :noteTitle, noteContent = :noteContent WHERE noteId =:noteId")
    suspend fun updateNote(noteTitle : String, noteContent : String, noteId : Long):Int //Return type Int only

    @Delete
    suspend fun deleteNote(note: Note)

}