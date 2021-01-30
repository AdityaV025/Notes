package com.adityaverma.notes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Aditya Verma on 29-01-2021.
 */
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name=  "email") var email: String,
    @ColumnInfo(name = "note_content") var note_content: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "card_color") var card_color: Int
) : Serializable