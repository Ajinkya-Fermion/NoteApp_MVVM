package com.aj.noteappajkotlinmvvm.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.repository.local.NoteRepository
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager

class HomeViewModelFactory(private val dataStoreManager: DataStoreManager, private val noteRepository: NoteRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(dataStoreManager,noteRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}