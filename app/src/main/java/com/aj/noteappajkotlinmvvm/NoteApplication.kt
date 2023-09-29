package com.aj.noteappajkotlinmvvm

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager


open class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    //“Application” context is tied to the application itself and remains alive as long as the application
    // is running. This means that it can be used across multiple activities, and is useful for accessing
    // global resources and classes that are not tied to any specific activity, such as
    // 1) "shared preferences" & "database helpers"(Room)
    // 2) "strings.xml" & "dimensions.xml"
    // 3) package name, application name, application version through Package Manager class
    // 4) Resource Caching − It uses resources such as layouts and drawables to improve the application performance
    // 5) send and receive application-level broadcasts using registerReceiver() and sendBroadCast() method

    companion object {
        lateinit var mInstance: NoteApplication

        @Volatile
        private var DATA_INSTANCE: DataStoreManager? = null

        //Singleton application context required for actions requiring global context
        //Ex: String.xml access in non activity/fragment classes
        fun getAppContext(): Context? {
            return mInstance.applicationContext
        }

        //Singleton class created for Datastore access in different screens
        fun getDataStoreManager(context: Context) : DataStoreManager {
            if (DATA_INSTANCE == null) {
                synchronized(this) {
                    DATA_INSTANCE = DataStoreManager(context)
                }
            }
            return DATA_INSTANCE!!
        }

        /**
         * Global context based display toast message so no dependence of activity context.
         *
         * @param data
         */
        //Custom method for short toast.
        fun shortToast(data: String?) {
            Toast.makeText(
                getAppContext(), data,
                Toast.LENGTH_SHORT
            ).show()
        }

        //Custom method for loner toast.
        fun longToast(data: String?) {
            Toast.makeText(
                getAppContext(), data,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}