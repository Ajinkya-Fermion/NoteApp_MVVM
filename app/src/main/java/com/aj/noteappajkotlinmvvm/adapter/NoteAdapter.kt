package com.aj.noteappajkotlinmvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aj.noteappajkotlinmvvm.activities.note.model.Note
import com.aj.noteappajkotlinmvvm.databinding.NoteItemLayoutBinding
import com.aj.noteappajkotlinmvvm.fragments.home.HomeFragmentDirections
import com.aj.noteappajkotlinmvvm.fragments.home.HomeViewModel

//We have used 'HomeViewModel' to get access to 'delete' functionality
class NoteAdapter(private val homeViewModel: HomeViewModel) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val itemBinding : NoteItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    //DiffUtil is used for utility class to calculate differentiate between
    //  toList and out list of output operation that converts first list into second list.
    //To calculate updates from write operation like insert, delete, update & read operation query.
    private val differCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteId == newItem.noteId &&
                    oldItem.noteContent == newItem.noteContent &&
                    oldItem.noteTitle == newItem.noteTitle
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    //AsyncListDiffer will handle all the data synchronization whenever we change one or all
    // elements in the list using 'DiffUtil' asynchronously without loosing
    // features like recycler view's 'animation'
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        //Here we inflate layout and we create the view
        return NoteViewHolder(
            NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.txvNoteTitle.text = currentNote.noteTitle

        holder.itemBinding.ibDelete.setOnClickListener {
            //This is the operation for which we have passed 'HomeViewModel' instance.
            homeViewModel.deleteNote(currentNote)
        }

        //Handling onclick within Adapter itself.
        holder.itemView.setOnClickListener{
            //Here we carry Note data(currentNote) to other screen
            val direction = HomeFragmentDirections.actionNavigationHomeToNoteActivity(currentNote)
            //Below is the navigation approach used using 'findNavController'
            it.findNavController().navigate(direction)
        }
    }
}