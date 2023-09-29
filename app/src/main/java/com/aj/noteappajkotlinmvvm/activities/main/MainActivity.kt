package com.aj.noteappajkotlinmvvm.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getDataStoreManager
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.home.HomeActivity
import com.aj.noteappajkotlinmvvm.activities.login.LoginActivity
import com.aj.noteappajkotlinmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var factory : MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {

        // Handle the splash screen transition.
        val splashScreen = installSplashScreen() //This approach can be used when manipulating any advance splashscreen transition.
        super.onCreate(savedInstanceState)
        //Here we are keeping Splashscreen on until 'checkUserIdExist()' finishes it's work
        splashScreen.setKeepOnScreenCondition {
            true
        }
        setUpBinding()
//        checkUserIdExist() //Check whether userId exist
    }

    private fun setUpBinding(){
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        factory = MainActivityViewModelFactory(getDataStoreManager(this))
        viewModel = ViewModelProvider(this,factory)[MainActivityViewModel::class.java]
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this

        //Based upon value user is redirected.
        viewModel.isUserLoggedIn.observe(this){ loggedInStatus->
            //using 'when'
            when(loggedInStatus){
                true -> goToHome()
                false -> goToLogin()
            }
        }
    }

    //We check whether userId exist ot not.
//    private fun checkUserIdExist() {
//
//        val mySharedPreferences : SharedPreferences =
//            getSharedPreferences(Constants.USER_STORE, MODE_PRIVATE)
//
//        val userId : String? = mySharedPreferences.getString(Constants.USER_ID,"")
//
//        if (userId != null) {
//            if(userId.isEmpty()){
//                goToLogin()
//            }
//            //UserId exist so go to home
//            else {
//                Constants.userID = userId.toLong() //Required for Quick access post Login
//                goToHome()
//            }
//        }
//    }

    private fun goToHome(){
        val myIntent = Intent(this, HomeActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }

    private fun goToLogin(){
        val myIntent = Intent(this, LoginActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }
}