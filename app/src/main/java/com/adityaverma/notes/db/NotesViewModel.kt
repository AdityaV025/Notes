package com.adityaverma.notes.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

/**
 * Created by Aditya Verma on 29-01-2021.
 */
class NotesViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: NotesRepo = NotesRepo(app)

    fun insertNote(note: Note):Long?{
        return repository.insert(note)
    }

    fun deleteNote(note: Note){
        repository.delete(note)
    }

    fun updateNote(note: Note){
        repository.update(note)
    }

    fun getNotes(email: String): LiveData<List<Note>> {
        return repository.getNotes(email)
    }
}