package com.aj.noteappajkotlinmvvm.activities.home.sharedviewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeActivityViewModel : ViewModel(), Observable {
    private val _fabValue = MutableLiveData<Boolean>().apply {
        value = true
    }
    val isFabVisible = _fabValue

    val isButtonClicked = MutableLiveData<Boolean>()

    init {
        isButtonClicked.value = false
    }

    fun addNote(){
        isButtonClicked.value = true
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}