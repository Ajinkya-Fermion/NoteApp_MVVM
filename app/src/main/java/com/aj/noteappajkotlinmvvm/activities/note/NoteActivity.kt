package com.aj.noteappajkotlinmvvm.activities.note

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.database.NoteDatabase
import com.aj.noteappajkotlinmvvm.databinding.ActivityNoteBinding
import com.aj.noteappajkotlinmvvm.repository.local.NoteRepository
import com.aj.noteappajkotlinmvvm.utils.Constants


class NoteActivity : AppCompatActivity() {

    private val args: NoteActivityArgs by navArgs()

    lateinit var binding : ActivityNoteBinding
    private lateinit var viewModel: NoteActivityViewModel
    lateinit var factory : NoteActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindings()

        //Added for closing the app if clicked back & it also closes 'MainActivity' in stack.
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress() //We need to terminate Activity manually
            }
        })
    }

    private fun setupBindings() {

        binding = DataBindingUtil.setContentView(this@NoteActivity,R.layout.activity_note)
        val noteRepository = NoteRepository(NoteDatabase.getDatabase(this))
        //Here we are passing 'note' Object & 'noteRepository' for db queries access.
        factory = NoteActivityViewModelFactory(args.note,noteRepository)
        viewModel = ViewModelProvider(this,factory)[NoteActivityViewModel::class.java]
        binding.noteViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isAddOrUpdateNoteStatus.observe(this) { noteStatus ->
            when(noteStatus){
                //REFRESH_NOTES will refresh the RecyclerView list.
                Constants.REFRESH_NOTES -> {
                    Constants.NOTE_CALLBACK_ACTION = Constants.REFRESH_NOTES
                    onBackPress()
                }
            }
        }

        //Below back button support
        val toolbar = supportActionBar
        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(true)
            toolbar.setDisplayHomeAsUpEnabled(true)
            //Based upon argument type title of the NoteActivity changes
            toolbar.setTitle(
                if(args.note.noteId > 0L)
                    R.string.update_note_title
                else
                    R.string.add_note_title
            )
        }
    }

    // this method will make back button work.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPress()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Below backPress reusable for
    // 1) top left menu back button &
    // 2) hardware back button.
    fun onBackPress() {
        finish() //Just one step back we are navigating.
    }
}