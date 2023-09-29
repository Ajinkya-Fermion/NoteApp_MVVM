package com.aj.noteappajkotlinmvvm.activities.register

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager

class RegisterActivityViewModelFactory(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository,
    private val mobileNo: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)){
            return RegisterActivityViewModel(dataStoreManager, userRepository, mobileNo) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}