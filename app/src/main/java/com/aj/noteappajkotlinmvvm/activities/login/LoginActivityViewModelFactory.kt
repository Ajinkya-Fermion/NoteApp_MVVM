package com.aj.noteappajkotlinmvvm.activities.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager

//Here we pass Shared preferences instance
class LoginActivityViewModelFactory(private val dataStoreManager: DataStoreManager,
                                    private val userRepository : UserRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginActivityViewModel::class.java)){
            return LoginActivityViewModel(dataStoreManager,userRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}