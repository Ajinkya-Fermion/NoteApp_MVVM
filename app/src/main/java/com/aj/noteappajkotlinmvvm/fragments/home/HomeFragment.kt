package com.aj.noteappajkotlinmvvm.fragments.home

// Rename the Pair class from the Android framework to avoid a name clash
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getDataStoreManager
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.home.HomeActivity
import com.aj.noteappajkotlinmvvm.activities.home.sharedviewmodel.HomeActivityViewModel
import com.aj.noteappajkotlinmvvm.activities.login.LoginActivity
import com.aj.noteappajkotlinmvvm.activities.note.model.Note
import com.aj.noteappajkotlinmvvm.adapter.NoteAdapter
import com.aj.noteappajkotlinmvvm.database.NoteDatabase
import com.aj.noteappajkotlinmvvm.databinding.FragmentHomeBinding
import com.aj.noteappajkotlinmvvm.repository.local.NoteRepository
import com.aj.noteappajkotlinmvvm.utils.Constants

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var noteAdapter: NoteAdapter
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()
    private lateinit var factory : HomeViewModelFactory

    private var notes : MutableList<Note> = arrayListOf() //Here empty array annoted by "arrayListOf()"

    //Below is deprecated menu code which will not be used in future
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
////        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.top_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivityViewModel.isFabVisible.value = true
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteRepository = NoteRepository(NoteDatabase.getDatabase(view.context))
        factory = HomeViewModelFactory(getDataStoreManager(view.context),noteRepository)

        viewModel = ViewModelProvider(activity as HomeActivity,factory)[HomeViewModel::class.java]
        binding.homeFragmentViewModel = viewModel

        setUpRecyclerview()

        homeActivityViewModel.isButtonClicked.observe(viewLifecycleOwner){ isClickedStatus ->

            when(isClickedStatus){
                true -> {
                    val note = Note("", "", 0L, Constants.userID)
                    val action = HomeFragmentDirections.actionNavigationHomeToNoteActivity(note)
                    //findNavController best usage inside fragment.
                    findNavController().navigate(action)
                    //We are resetting the value so double instance of NoteActivity can be avoided.
                    homeActivityViewModel.isButtonClicked.value = false
                }
                false -> print("Fab button disabled")
            }
        }

        viewModel.isUserLoggedIn.observe(viewLifecycleOwner){ loginStatus ->
            when(loginStatus){
                false -> {
                    activity?.finish()
                    goToLogin()
                }
                true -> print("User status logged in")
            }
        }

        //https://medium.com/tech-takeaways/how-to-migrate-the-deprecated-oncreateoptionsmenu-b59635d9fe10
                (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.top_menu, menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        when(menuItem.itemId){
                            R.id.logout -> {
                                viewModel.logOutUser()
                                return true
                            }
                        }
                        return false
                    }

                },viewLifecycleOwner)
    }

    private fun goToLogin(){
        val myIntent = Intent(activity, LoginActivity::class.java)
        this.startActivity(myIntent)
    }

    private fun setUpRecyclerview() {
        noteAdapter = NoteAdapter(viewModel)
        //binding.view.apply used to get rid of 'binding' writing repeatedly
        binding.rvNoteList.apply {
            layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        //Below observer will observe change in NotesList and based upon that changes will be delivered.
        viewModel.notesList.observe(viewLifecycleOwner) { note ->
            noteAdapter.differ.submitList(note)
            updateUI(note)
            notes = note as MutableList<Note>
        }

        //Here we can get access to ongoing 'activity' instance
        loadNoteList()
    }

    private fun loadNoteList(){
        activity?.let {
            viewModel.getAllNotes(Constants.userID)
        }
    }

    private fun updateUI(note: List<Note>?) {
        if(note!!.isNotEmpty()){
            binding.cardView.visibility = View.GONE
            binding.rvNoteList.visibility = View.VISIBLE
        }
        else{
            binding.cardView.visibility = View.VISIBLE
            binding.rvNoteList.visibility = View.GONE
        }
    }

    //We are using resume method to check whether we need to refresh NotesList
    override fun onResume() {
        super.onResume()
        if(Constants.NOTE_CALLBACK_ACTION == Constants.REFRESH_NOTES){
            Constants.NOTE_CALLBACK_ACTION = "" //Resetting value to empty.
            loadNoteList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}