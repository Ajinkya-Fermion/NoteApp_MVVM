package com.aj.noteappajkotlinmvvm.activities.login

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.getAppContext
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.shortToast
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.repository.local.UserRepository
import com.aj.noteappajkotlinmvvm.utils.Constants
import com.aj.noteappajkotlinmvvm.utils.DataStoreManager
import com.aj.noteappajkotlinmvvm.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//We required 'context' here for many purposes but it's not not good practice to pass context
// as it creates leak chances.

//But if we want to use 'context' we want to replace 'ViewModel()' with 'AndroidViewModel()' but still it's not good practice.

//For resolving this we need to use Dependency Injection(Manual or Dagger 2 or Hilt).

//We are injecting ActivityContext for
// - Intent based navigation
class LoginActivityViewModel(
    private val dataStoreManager: DataStoreManager,
    private val userRepository: UserRepository
) : ViewModel(), Observable {

    var userId: Long = -1 //Default value setting as '-1' for non registered user
    val isLoginStatus = MutableLiveData<String>()

    //Below variable used only for mobile number checking
    val isUserExistAndRegistered = MutableLiveData<String>()

    @Bindable
    var inputMobileNo = MutableLiveData<String>()

    @Bindable
    var inputPassword = MutableLiveData<String>()

    init {
        isLoginStatus.value = "freshLaunch" //Initial value given
        //Below value used for checking mobile no status i.e. whether it exist or not in db
        //If exist then -> Registered User (1)
        //If not exist then -> New User (0)
        isUserExistAndRegistered.value = "" //Default value always empty
        //Initially values set to empty
        inputMobileNo.value = ""
        inputPassword.value = ""
    }

    fun validateLoginUser() {
        val mobileNo = inputMobileNo.value!!
        val password = inputPassword.value!!

        //Step 1: Check mobile no empty or not
        //StringUtils.isNotBlank() takes it a step forward. It not only checks if the String is not empty
        // and not null, but also checks if it is not only a whitespace string
        if (isMobileNoValid(mobileNo).isNotBlank()) {
            isLoginStatus.value = isMobileNoValid(mobileNo)
            //Below we have implemented Application level toast method which only receives "content" as param.
            //(Optional implementation)
            //Here we are toasting message in viewmodel itself.
            shortToast(isLoginStatus.value)
        }
        //Step 2: Check mobile no exist in db or not
        //Initial value will always be empty as we are checking for first time.
        else if (isUserExistAndRegistered.value == "") {
            //Below function will decide further flow of validation.
            isUserMobileNotRegistered(mobileNo)
        } else if (isUserExistAndRegistered.value == "1") {
            if (isPasswordValid(password).isNotBlank()) {
                isLoginStatus.value = isPasswordValid(password)
                shortToast(isLoginStatus.value)
            } else {
                validateLoginCredentials(userId, password)
            }
        }
    }

    //Post successful login clear InputFields
    private fun clearFields() {
        inputMobileNo.value = ""
        inputPassword.value = ""
    }

    private fun isMobileNoValid(mobileNo: String): String {
        // Validation logic for MobileNo
        if (mobileNo.isBlank()) {
            // Write error response code for same to be shown in EditText field in future.
            return getAppContext()!!.resources.getString(R.string.mobileNoEmpty)
        } else if (mobileNo.length < 10) {
            return getAppContext()!!.resources.getString(R.string.mobileNo10digit)
        } else if (!Util.isValidMobileNumber(mobileNo)) {
            return getAppContext()!!.resources.getString(R.string.mobileNoInvalid)
        }
        return ""
    }

    private fun isUserMobileNotRegistered(mobileNo: String) {

        //Below blog link for understanding 'ViewModelScope' usage as a rightful approach than CoroutineScope
        //https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471#:~:text=viewModelScope%20contributes%20to%20structured%20concurrency,when%20the%20ViewModel%20is%20destroyed.
        viewModelScope.launch(Dispatchers.IO) {
            //Check whether user exist in DB and user has completed registration
            userId = checkUserExistAndRegistered(mobileNo.toLong()) //Fetching value for usage

            //Below we use 'withContext' with 'Main' thread Dispatcher for UI/View level updating through LiveData
            withContext(Dispatchers.Main) {
                if (userId >= 0) {
                    println("*** User Exist! Please enter password")
                    //If exist here we are updating value for making 'password' field visible.
                    isUserExistAndRegistered.value = "1" //This will update UI
                } else {
                    println("*** User don't Exist!")
                    isUserExistAndRegistered.value = "0" //"0" indicator added to register fresh.
                }
            }
        }
    }

    private suspend fun checkUserExistAndRegistered(mobileNo: Long): Long {
        //Here we use repository to call RoomDB method for 'User' table.
        val userId = userRepository.getUserByMobileNo(mobileNo, true) ?: 0L
        return if (userId != 0L) {
            // Mobile number exists
            userId
        } else {
            // Mobile number does not exist
            -1L //-1 means no user exist
        }
    }

    private fun isPasswordValid(password: String): String {
        if(password.isBlank()){
            return String.format(getAppContext()!!.resources.getString(R.string.pwdEmpty),"Password")
        }
        return ""
    }

    private fun validateLoginCredentials(userId : Long, password : String){
        viewModelScope.launch(Dispatchers.IO) {
            if(checkPasswordMatchingForUser(userId,password)){
                //Save the "userId" in SharedPreference
                //Single line code
//                sharedPreferences.edit().putString(Constants.USER_ID, userId.toString()).apply()

                //Saving 'userId' post valid login in 'datastore'
                dataStoreManager.saveUserId(userId.toString())
                Constants.userID = userId //Required for Quick access post Login

                //Updating the UI part
                withContext(Dispatchers.Main){
                    isLoginStatus.value = ""
                    clearFields()
                }
            }
            else{
                //Updating the UI part
                withContext(Dispatchers.Main) {
                    isLoginStatus.value = "Incorrect Password"
                    shortToast(isLoginStatus.value)
                }
            }
        }
    }

    //Here we are using 'userId' with 'password' combination to make the search faster as 'userId' in primary key
    // w.r.t 'mobileNo' & 'password' combination will be little laggy.
    private suspend fun checkPasswordMatchingForUser(userId : Long, password : String):Boolean {
        val userIdFetched : Long = userRepository.getUserByPassword(userId, password) ?: -1L
        return userIdFetched == userId && userId > -1L //This returns boolean status
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}