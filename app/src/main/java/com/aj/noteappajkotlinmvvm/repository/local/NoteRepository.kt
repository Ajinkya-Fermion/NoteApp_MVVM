package com.aj.noteappajkotlinmvvm.repository.local

import com.aj.noteappajkotlinmvvm.activities.note.model.Note
import com.aj.noteappajkotlinmvvm.database.NoteDatabase

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) = db.noteDao().addNote(note)

    fun getNotes(userId:Long) = db.noteDao().getNotes(userId)

    suspend fun updateNote(noteTitle : String, noteContent : String, noteId : Long) =
        db.noteDao().updateNote(noteTitle,noteContent,noteId)

    suspend fun deleteNote(note : Note) = db.noteDao().deleteNote(note)

}