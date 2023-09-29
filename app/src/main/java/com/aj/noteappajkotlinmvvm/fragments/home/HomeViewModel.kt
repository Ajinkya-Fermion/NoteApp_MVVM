package com.aj.noteappajkotlinmvvm.fragments.home

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aj.noteappajkotlinmvvm.activities.note.model.Note
import com.aj.noteappajkotlinmvvm.repository.local.NoteRepository
import com.aj.noteappajkotlinmvvm.utils.Constants
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val dataStoreManager: DataStoreManager, private val noteRepository : NoteRepository)
    : ViewModel(), Observable {

    //Below mutable entity will be required for read,delete DB operation
    val notesList = MutableLiveData<List<Note>>()

    val isUserLoggedIn = MutableLiveData<Boolean>()

    init {
        isUserLoggedIn.value = true //Default status is always true and is required for avoiding crashes.
        notesList.value= arrayListOf() //This is required to be launched at initial time.
    }

    fun getAllNotes(userId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val noteListValue : List<Note> = noteRepository.getNotes(userId)
            withContext(Dispatchers.Main){
                notesList.value = noteListValue
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
            getAllNotes(Constants.userID)
        }
    }

    fun logOutUser(){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveUserId("")
            Constants.userID = 0L

            withContext(Dispatchers.Main){
                isUserLoggedIn.value = false
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}