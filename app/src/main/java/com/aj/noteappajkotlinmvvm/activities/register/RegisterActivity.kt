package com.aj.noteappajkotlinmvvm.activities.register

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getDataStoreManager
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.home.HomeActivity
import com.aj.noteappajkotlinmvvm.database.NoteDatabase
import com.aj.noteappajkotlinmvvm.databinding.ActivityRegisterBinding
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository
import com.aj.noteappajkotlinmvvm.utils.filters.AlphabetInputFilter

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel
    private lateinit var factory : RegisterActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindings()
    }

    private fun setupBindings() {

        //Reading data from bundle
        val bundle: Bundle? = intent.extras
        val mobileNoData: String? = bundle?.getString("mobileNo")

        //Data binding stuff
        binding = DataBindingUtil.setContentView(this@RegisterActivity, R.layout.activity_register)
        val userRepository = UserRepository(NoteDatabase.getDatabase(this))
        factory = RegisterActivityViewModelFactory(
            getDataStoreManager(this),
            userRepository,
            mobileNoData.toString()
        )
        viewModel = ViewModelProvider(this,factory)[RegisterActivityViewModel::class.java]
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
        setupButtonClick(mobileNoData)

        //Below code for hiding toolbar which is not required
        val toolbar = supportActionBar
        toolbar!!.hide()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress() //We need to terminate Activity manually
            }
        })
    }

    private fun setupButtonClick(mobileNoData: String?) {
        binding.apply {
            edtxtMobile.isEnabled = false //Disabling editing of mobile number
            edtxtMobile.setText(mobileNoData) //Setting up the entered mobile No

            //Here we are setting up "alphabet" filter for username field.
            val filters = arrayOf<InputFilter>(AlphabetInputFilter())
            edtxtName.filters = filters

            ivExit.setOnClickListener {
                onBackPress()
            }
        }

        viewModel.isRegistrationStatus.observe(this) { registrationStatus ->
            when(registrationStatus){
                //Empty indicates no registration error msg returned so navigate to HomeActivity.
                "" -> goToHome()
            }
        }
    }

    private fun goToHome(){
        val myIntent = Intent(this, HomeActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }

    fun onBackPress() {
        finish()
    }
}