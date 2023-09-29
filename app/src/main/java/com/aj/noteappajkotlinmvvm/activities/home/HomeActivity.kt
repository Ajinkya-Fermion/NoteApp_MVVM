package com.aj.noteappajkotlinmvvm.activities.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.shortToast
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.home.sharedviewmodel.HomeActivityViewModel
import com.aj.noteappajkotlinmvvm.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityHomeBinding
    private var doubleBackToExitPressedOnce = false
    private val homeActivityViewModel by viewModels<HomeActivityViewModel>()
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@HomeActivity,R.layout.activity_home)
        binding.homeActivityViewModel = homeActivityViewModel //Binding fabViewModel for FAB operations
        binding.lifecycleOwner = this
        //Don't use findNavController when using updating
        // from 'fragment' to 'androidx.fragment.app.FragmentContainerView'
        //https://issuetracker.google.com/issues/142847973?pli=1
//        navController = findNavController(R.id.nav_host_fragment_activity_home)
        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
                    as NavHostFragment
            navController = navHostFragment.navController
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment, R.id.referFragment, R.id.aboutUsFragment
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            //Below code we can select any fragment we want
            navView.setOnItemSelectedListener {
                    item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        selectBottomViewFragmentNavigation(0)
                        true
                    }
                    R.id.referFragment -> {
                        selectBottomViewFragmentNavigation(1)
                        true
                    }
                    R.id.aboutUsFragment -> {
                        selectBottomViewFragmentNavigation(2)
                        true
                    }
                    else -> false
                }
            }
        }

        // Observe changes to the isFabVisible flag
        homeActivityViewModel.isFabVisible.observe(this) { isFabVisible ->
            when(isFabVisible){
                true ->  binding.fabAddNote.show()
                false -> binding.fabAddNote.hide()
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress() //We need to terminate Activity manually
            }
        })
    }

    //Below is pure Navigation graph approach for BottomNavigationView handling.
    //Based upon last destination fragment "label" name route navigation action is chosen
    //Note : We can use "navController.currentDestination!!.displayName" also to get current 'Fragment' name
    private fun selectBottomViewFragmentNavigation(clickedIndexPos : Int){
        if(clickedIndexPos == 0){
            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_refer)){
                navController.navigate(R.id.action_referFragment_to_homeFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_aboutus)){
                navController.navigate(R.id.action_aboutUsFragment_to_homeFragment)
            }
        }
        else if(clickedIndexPos == 1){
            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_home)) {
                navController.navigate(R.id.action_homeFragment_to_referFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_aboutus)) {
                navController.navigate(R.id.action_aboutUsFragment_to_referFragment)
            }
        }
        else if(clickedIndexPos == 2){
            if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_home)) {
                navController.navigate(R.id.action_homeFragment_to_aboutUsFragment)
            }
            else if(navController.currentDestination!!.label.toString()
                == resources.getString(R.string.title_refer)) {
                navController.navigate(R.id.action_referFragment_to_aboutUsFragment)
            }
        }
    }

    fun onBackPress() {
        if (doubleBackToExitPressedOnce) {
            finish()
            finishAffinity() //Removes all activities in backstack
        }

        this.doubleBackToExitPressedOnce = true
        shortToast(resources.getString(R.string.press_back_again_exit))

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)// Delay of 2 seconds to allow for the second back press
    }
}