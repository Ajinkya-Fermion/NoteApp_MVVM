package com.aj.noteappajkotlinmvvm.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager

class MainActivityViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(dataStoreManager) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}