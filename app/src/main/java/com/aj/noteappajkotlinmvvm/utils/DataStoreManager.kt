package com.aj.noteappajkotlinmvvm.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {
    //reference ink : https://developer.android.com/topic/libraries/architecture/datastore
    private val Context.userStore : DataStore<Preferences> by preferencesDataStore(Constants.USER_STORE)
    private val userStore = context.userStore

    companion object{
        var userIdKey = stringPreferencesKey(Constants.USER_ID)
    }

    suspend fun saveUserId(userId : String){
        userStore.edit {
            pref -> pref[userIdKey] = userId
        }
    }

    suspend fun getUserId(): String {
        val preferences = userStore.data.first()
        return preferences[userIdKey] ?: ""
    }

}