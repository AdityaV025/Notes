package com.adityaverma.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Aditya Verma on 29-01-2021.
 */
@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO

    companion object {
        var instance: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, NoteDatabase::class.java,
                    "notes_database"
                ).build()
            }
            return instance
        }
    }
}