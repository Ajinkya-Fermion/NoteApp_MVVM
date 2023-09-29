package com.aj.noteappajkotlinmvvm.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getDataStoreManager
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.main.MainActivity
import com.aj.noteappajkotlinmvvm.activities.register.RegisterActivity
import com.aj.noteappajkotlinmvvm.database.NoteDatabase
import com.aj.noteappajkotlinmvvm.databinding.ActivityLoginBinding
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository

class LoginActivity : AppCompatActivity() {

    //We are making the userId Global as we require it's value in another function
    var userId : Long = -1 //Default value setting as '-1' for non registered user

    lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    lateinit var factory : LoginActivityViewModelFactory

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
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        //Passing instance of Shared preference which is required in LoginViewModel
        //Passing context required for toast message and other operations in LoginActivity
        //Here we are passing DB instance to 'UserRepository'
        val userRepository = UserRepository(NoteDatabase.getDatabase(this))
        //Here we are passing userRepository for db queries access.
        factory = LoginActivityViewModelFactory(
            getDataStoreManager(this),
            userRepository
        )
        viewModel = ViewModelProvider(this,factory)[LoginActivityViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
        setupButtonClick()

        //Below code for hiding toolbar which is not required
        val toolbar = supportActionBar
        toolbar!!.hide()
    }

    private fun setupButtonClick() {

        binding.apply {
            edtxtPwd.visibility = View.GONE //Setting initially password field invisible for fresh users.
            ivExit.setOnClickListener {
                onBackPress()
            }
        }

        //Below logic not required as context based Toast msg & Activity redirection
        // can be done in LoginViewModel class.
        viewModel.isLoginStatus.observe(this) { loginStatus ->
            when(loginStatus){
                //Empty indicates no login error msg returned so navigate to HomeActivity
                "" -> {
                    goToHome()
                }
            }
        }

        viewModel.isUserExistAndRegistered.observe(this) { regStatus ->
            when(regStatus){
                "0" -> goToRegister()
                "1" -> {
                    //Making password field visible for user to type password.
                    binding.edtxtPwd.visibility = View.VISIBLE //This has to be done from Activity only
                }
            }
        }
    }

    private fun goToHome(){
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }

    private fun goToRegister(){
        val myIntent = Intent(this, RegisterActivity::class.java)
        myIntent.putExtra("mobileNo", binding.edtxtMobileLogin.text.toString()) //Optional parameters
        startActivity(myIntent)
        viewModel.isUserExistAndRegistered.value = "" //Resetting
    }

    fun onBackPress() {
        finish()
        finishAffinity() //Removes all activities in backstack
    }
}