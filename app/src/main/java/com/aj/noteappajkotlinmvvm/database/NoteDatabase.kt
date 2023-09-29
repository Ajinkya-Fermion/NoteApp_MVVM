package com.aj.noteappajkotlinmvvm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aj.noteappajkotlinmvvm.activities.note.model.Note
import com.aj.noteappajkotlinmvvm.activities.register.model.User

//Here inside entities we can add multiple tables [Table1,Table2]
//version is used when you have to increase the number when you do "changes to the database schema" on your next app update
//exportScheme set to true to be able to use "automated migration" in the future updates
@Database(
    entities = [Note::class, User::class],
    version = 1,
    exportSchema = true
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun userDao(): UserDao //This declaration is required for UserDao to work

    companion object {

        @Volatile //@Volatile, which means the results will be visible to other threads
        private var INSTANCE: NoteDatabase? = null

        //In "getDatabase(..){..}" we check if the database has been created. If not, we call the buildDatabase method to build
        // the database using the databaseBuilder from Room
        fun getDatabase(context: Context): NoteDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): NoteDatabase {
            //Note: Usually, you need only one RoomDatabase() class for the whole app here we have
            // that DB name "notes_database"
            return Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notes_database"
            ).build()
        }
    }
}