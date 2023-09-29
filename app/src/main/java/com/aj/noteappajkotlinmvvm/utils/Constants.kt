package com.aj.noteappajkotlinmvvm.utils

class Constants {

    companion object {
        var NOTE_ACTION : String = "" //This action value will define nature of operation on NoteActivity
        var NOTE_CALLBACK_ACTION = "" //Default will be empty always

        //All action defined for notes
        const val ADD_NOTE = "addNote"
        const val UPDATE_NOTE = "updateNote"

        //Callback action from NoteActivity to HomeFragment
        const val REFRESH_NOTES = "refreshNotes"

        //This is a custom storage name for sharedPrefernce under which multiple key-value data will be stored
        // i.e. we will use for 'userId' storage.
        const val USER_STORE = "UserStore"

        //Below constant value used for sharedPreference & other things
        const val USER_ID = "userId"

        var userID = -1L //This will be quick static variable for userID using post Login process
    }

}