package com.adityaverma.notes.db

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Aditya Verma on 29-01-2021.
 */
class NotesRepo(app: Application) {
    var noteDao: NoteDAO? = NoteDatabase.getDatabase(app)?.noteDao()

    fun insert(note: Note): Long? {
        return InsertAsync(
            noteDao
        ).execute(note).get()
    }

    fun delete(note: Note) {
        DeleteAsync(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateAsync(noteDao).execute(note)
    }

    fun getNotes(uEmail: String): LiveData<List<Note>> {
        return GetNotesAsync(
            noteDao
        ).execute(uEmail).get()
    }

    class InsertAsync(var noteDao: NoteDAO?) : AsyncTask<Note, Void, Long?>() {
        override fun doInBackground(vararg params: Note): Long? {
            return noteDao?.insert(params[0])
        }
    }

    class DeleteAsync(var noteDao: NoteDAO?) : AsyncTask<Note, Void, Unit>() {
        override fun doInBackground(vararg params: Note) {
            noteDao?.delete(params[0])
        }
    }

    class UpdateAsync(private var noteDao: NoteDAO?) : AsyncTask<Note, Void, Unit>() {
        override fun doInBackground(vararg params: Note) {
            noteDao?.update(params[0])
        }
    }

    class GetNotesAsync(private var noteDao: NoteDAO?) : AsyncTask<String, Void, LiveData<List<Note>>>() {
        override fun doInBackground(vararg params: String): LiveData<List<Note>>? {
            return noteDao?.getNotes(params[0])
        }
    }
}