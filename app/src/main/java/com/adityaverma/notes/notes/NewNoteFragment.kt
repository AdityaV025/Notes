package com.adityaverma.notes.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.adityaverma.notes.R
import com.adityaverma.notes.db.Note
import com.adityaverma.notes.db.NotesViewModel
import com.adityaverma.notes.utils.ConvertToDate
import kotlinx.android.synthetic.main.fragment_new_note.*
import java.util.*

class NewNoteFragment : Fragment() {

    private lateinit var mUserEmail: String
    private lateinit var preferences: SharedPreferences
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var mView: View
    private lateinit var mOldNote : Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        preferences = context?.getSharedPreferences("com.adityaverma.notes", MODE_PRIVATE)!!
        mUserEmail = preferences.getString("email", "none").toString()
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        val toEdit: Boolean = arguments?.getBoolean("edit")!!
        if (toEdit){
            mOldNote = (arguments?.getSerializable("note") as Note?)!!
            noteTitle.setText(mOldNote.title)
            noteContent.setText(mOldNote.note_content)
            saveNoteBtn.text = "Update"
        }
        saveNoteBtn.setOnClickListener {
            if (checkIfEmpty()){
                if (toEdit){
                    mOldNote.title = noteTitle.text.toString()
                    mOldNote.note_content = noteContent.text.toString()
                    mOldNote.date = ConvertToDate().convertTimeStampToDate(System.currentTimeMillis())
                    notesViewModel.updateNote(mOldNote)
                    mView.findNavController().popBackStack()
                    hideSoftKeyboard()
                }else {
                    val newNote = Note(
                        title = noteTitle.text.toString(),
                        note_content = noteContent.text.toString(),
                        date = ConvertToDate().convertTimeStampToDate(System.currentTimeMillis()),
                        card_color = getRandomColor(),
                        email = mUserEmail
                    )
                    notesViewModel.insertNote(newNote)
                    mView.findNavController().popBackStack()
                    hideSoftKeyboard()
                }
            }
        }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goToNotesFragment()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        backBtn.setOnClickListener{ goToNotesFragment() }
    }

    private fun getRandomColor() : Int {
        val androidColors = resources.getIntArray(R.array.card_colors)
        return androidColors[Random().nextInt(androidColors.size)]
    }

    private fun checkIfEmpty() : Boolean {
        return if (noteTitle.length() == 0 || noteContent.length() == 0){
            Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            false
        }else {
            true
        }
    }

    private fun hideSoftKeyboard() {
        val imm: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun goToNotesFragment() {
        hideSoftKeyboard()
        mView.findNavController().navigate(R.id.action_newNoteFragment_to_notesFragment)
    }

}
