package com.adityaverma.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adityaverma.notes.R
import com.adityaverma.notes.db.Note
import kotlinx.android.synthetic.main.custom_single_note.view.*

/**
 * Created by Aditya Verma on 29-01-2021.
 */
class NotesAdapter(var notes: List<Note>,
                   private val context: Context,
                   private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.custom_single_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTextView.text = notes[position].title
        holder.noteContentTextView.text = notes[position].note_content
        holder.noteDate.text = notes[position].date
        holder.noteCard.setCardBackgroundColor(notes[position].card_color)
        holder.setOnLongClickListener(itemClickListener,position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTextView : TextView = itemView.noteTitleText
        val noteContentTextView: TextView = itemView.noteContentText
        val noteDate : TextView = itemView.dateText
        val noteCard: CardView = itemView.singleCard
        fun setOnLongClickListener(clickListener: OnItemClickListener,position: Int){
            noteCard.setOnClickListener { clickListener.onItemClicked(position) }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    interface OnItemClickListener{
        fun onItemClicked(position: Int)
    }

}