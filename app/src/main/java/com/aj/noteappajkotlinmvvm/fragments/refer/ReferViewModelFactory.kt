package com.aj.noteappajkotlinmvvm.fragments.refer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.di.ActivityContext

class ReferViewModelFactory(
    private val activityContext : ActivityContext,
    private val mobileNo : String
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(ReferViewModel::class.java)){
            return ReferViewModel(activityContext,mobileNo) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}