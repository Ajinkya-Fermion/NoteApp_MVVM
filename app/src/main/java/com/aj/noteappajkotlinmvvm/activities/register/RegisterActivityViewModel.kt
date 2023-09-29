package com.aj.noteappajkotlinmvvm.activities.register

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getAppContext
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.shortToast
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.register.model.User
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository
import com.aj.noteappajkotlinmvvm.utils.Constants
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager
import com.aj.noteappajkotlinmvvm.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivityViewModel(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository,
    private val mobileNo: String
) : ViewModel(), Observable {

    val isRegistrationStatus = MutableLiveData<String>()

    @Bindable
    var inputUserName = MutableLiveData<String>()

    @Bindable
    var inputEmailId = MutableLiveData<String>()

    @Bindable
    var inputPassword = MutableLiveData<String>()

    @Bindable
    var inputConfirmPassword = MutableLiveData<String>()

    init {
        isRegistrationStatus.value = "freshLaunch" //Initial value given not empty

        //Initially values set to empty
        inputUserName.value = ""
        inputEmailId.value = ""
        inputPassword.value = ""
        inputConfirmPassword.value = ""
    }

    fun validateRegisterUser(){

        var userId : Long

        //Always add Trim to remove unwanted start & end of the text content.
        val userName = inputUserName.value!!.trim()
        val emailId = inputEmailId.value!!.trim()
        val password = inputPassword.value!!.trim()
        val confPassword = inputConfirmPassword.value!!.trim()

        //Step 1: Check username empty or not
        if (isNameValid(userName).isNotBlank()) {
            isRegistrationStatus.value = isNameValid(userName)
            //Below we have implemented Application level toast method which only receives "content" as param.
            //(Optional implementation)
            //Here we are toasting message in viewmodel itself.
            shortToast(isRegistrationStatus.value)
        }
        //Step 2: Check emailId empty or not
        else if(isEmailValid(emailId).isNotBlank()){
            isRegistrationStatus.value = isEmailValid(emailId)
            shortToast(isRegistrationStatus.value)
        }
        //Step 3: Check password empty or not
        else if(isPasswordValid(password,"Password").isNotBlank()){
            isRegistrationStatus.value = isPasswordValid(password,"Password")
            shortToast(isRegistrationStatus.value)
        }
        //Step 4: Check confirm password empty or not
        else if(isPasswordValid(confPassword,"Confirm password").isNotBlank()){
            isRegistrationStatus.value = isPasswordValid(confPassword,"Confirm password")
            shortToast(isRegistrationStatus.value)
        }
        //Step 5: Check both 'password' & 'confirm password' are matching or not.
        else if(password != confPassword){
            isRegistrationStatus.value = getAppContext()!!.resources.getString(R.string.pwdMismatch)
            shortToast(isRegistrationStatus.value)
        }
        else{
            viewModelScope.launch(Dispatchers.IO) {
                //Here we will do pure Insert DB operation.
                userId = insertUserData(userName,emailId,mobileNo,password)

                dataStoreManager.saveUserId(userId.toString())
                Constants.userID = userId //Required for Quick access post Register

                //Updating the UI part
                withContext(Dispatchers.Main) {
                    clearFields() //This to be done in 'Main' thread as it's UI based operation.
                    isRegistrationStatus.value = "" //Empty will indicate moving to HomeScreen.
                }
            }
        }
    }

    private fun isNameValid(userName: String): String {
        if(userName.isBlank()){
            // Write error response code for same to be shown in EditText field in future.
            return getAppContext()!!.resources.getString(R.string.nameEmpty)
        }
        else if(!Util.isValidName(userName)){
            return getAppContext()!!.resources.getString(R.string.nameOnlyAlphabet)
        }
        return ""
    }

    private fun isEmailValid(emailId: String): String {
        if(emailId.isEmpty()){
            return getAppContext()!!.resources.getString(R.string.emailEmpty)
        }
        else if(!Util.isValidEmail(emailId)){
            return getAppContext()!!.resources.getString(R.string.emailInvalid)
        }
        return ""
    }

    //This method is used for both password & confirm password field.
    private fun isPasswordValid(password: String, concatenateKeyword : String): String {
        if(password.isBlank()){
            // Here we have used dynamic string concept where we are concatenating "Password" keyword.
            return String.format(getAppContext()!!.resources.getString(R.string.pwdEmpty),concatenateKeyword)
        }
        else if(password.length < 8){
            return String.format(getAppContext()!!.resources.getString(R.string.pwd8Characters),concatenateKeyword)
        }
        return ""
    }

    private suspend fun insertUserData(name: String, email: String, mobileNo: String, pwd: String):Long{
        //Rest all params mobileNo, name, pwd, email are entered as it is.
        val user = User(mobileNo.toLong(),name,pwd,email,true)
        val userId: Long = userRepository.insertUser(user) as Long
        return if (userId > 0) {
            // Mobile number exists
//            println("UserId exists: "+userId)
            userId
        } else {
            // Mobile number does not exist
//            println("UserId does not exist: "+userId)
            0
        }
    }

    //Post successful register clear InputFields
    private fun clearFields() {
        inputUserName.value = ""
        inputEmailId.value = ""
        inputPassword.value = ""
        inputConfirmPassword.value = ""
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}