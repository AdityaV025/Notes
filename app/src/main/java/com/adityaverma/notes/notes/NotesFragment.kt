package com.adityaverma.notes.notes

import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adityaverma.notes.R
import com.adityaverma.notes.adapter.NotesAdapter
import com.adityaverma.notes.db.Note
import com.adityaverma.notes.db.NotesViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment(),NotesAdapter.OnItemClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserEmail: String
    private lateinit var preferences: SharedPreferences
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var Notes : List<Note>
    private lateinit var notesAdapter : NotesAdapter
    private lateinit var getNotes : LiveData<List<Note>>
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        init()
        fetchAllNotes()
        addNote.setOnClickListener {
            newNoteFragment()
        }
        logOutBtn.setOnClickListener {
            signOut()
        }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    private fun init() {
        preferences = context?.getSharedPreferences(getString(R.string.preference_name),MODE_PRIVATE)!!
        mUserEmail = preferences.getString("email","none").toString()
        mAuth = FirebaseAuth.getInstance()
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private fun fetchAllNotes() {
        getNotes = notesViewModel.getNotes(mUserEmail)
        getNotes.observe(requireActivity(), {
            Notes = getNotes.value!!
            if (Notes.isEmpty()){
                notesRecyclerView.visibility = GONE
                emptyListText.visibility = VISIBLE
            }else {
                notesRecyclerView.setHasFixedSize(true)
                notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
                notesAdapter = NotesAdapter(Notes,requireContext(),this)
                notesRecyclerView.adapter = notesAdapter
            }
            })
    }

    private fun signOut() {
        context?.let { it ->
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setMessage("Are you sure you want to log out ?")
                .setPositiveButton("Log Out") { _: DialogInterface?, _: Int ->
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }
                    Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show()
                    mGoogleSignInClient?.signOut()?.addOnCompleteListener {
                        if (it.isSuccessful){
                            setEmailEmpty()
                            mAuth.signOut()
                            mView.findNavController().navigate(R.id.action_notesFragment_to_authFragment)
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun newNoteFragment() {
        val bundle = Bundle()
        bundle.putBoolean("edit",false)
        NewNoteFragment().arguments = bundle
        mView.findNavController().navigate(R.id.action_notesFragment_to_newNoteFragment,bundle)
    }

    private fun setEmailEmpty(){
        preferences.edit()?.putString("email","none")?.apply()
    }

    override fun onItemClicked(position: Int) {
        val bundle = Bundle()
        bundle.putString("email", mUserEmail)
        bundle.putSerializable("note", Notes[position])
        NoteEditDelFragment().arguments = bundle
        mView.findNavController().navigate(R.id.action_notesFragment_to_bottomSheet,bundle)
    }

}