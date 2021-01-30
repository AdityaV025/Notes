package com.adityaverma.notes.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adityaverma.notes.R
import com.adityaverma.notes.db.Note
import com.adityaverma.notes.db.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_note_edit_del.*

class NoteEditDelFragment : BottomSheetDialogFragment() {

    private lateinit var mView: View
    private lateinit var mUserEmail: String
    private lateinit var newNote: Note
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_edit_del, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        mUserEmail = arguments?.getString("email")!!
        newNote = (arguments?.getSerializable("note") as Note?)!!
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        editNote.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("note",newNote)
            bundle.putBoolean("edit",true)
            NewNoteFragment().arguments = bundle
            dismiss()
            findNavController().navigate(R.id.action_bottomSheet_to_newNoteFragment,bundle)
        }
        deleteNote.setOnClickListener {
            notesViewModel.deleteNote(newNote)
            dismiss()
        }
    }
}