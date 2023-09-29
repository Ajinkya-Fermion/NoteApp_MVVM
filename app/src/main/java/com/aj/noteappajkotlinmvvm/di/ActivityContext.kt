package com.aj.noteappajkotlinmvvm.di

import android.content.Context

//This class created for Manual Dependency injection at activity level.
//This can be used by any fragment to get context
class ActivityContext(private val activityContext: Context) {

    //Activity context used for
    // 1) Launching intents
    // 2) strings, layouts, images access through getResources() method.
    // 3) View inflation for 'Views', 'fragments' 4) Dialog creation (Alert Dialog, Progress Dialog box)
    // 4) Action Bar (setting title, icons and navigation.)

    //Method to inject
    fun getActivityContext() : Context {
        return activityContext
    }
}