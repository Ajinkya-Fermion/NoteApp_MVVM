package com.aj.noteappajkotlinmvvm.utils

import android.content.Context
import android.widget.Toast

class Util {
    //Below approach used for static function
    companion object {

        fun isValidMobileNumber(number: String): Boolean {
            val regexPattern = "^[6-9]\\d{9}$"
            return number.matches(regexPattern.toRegex())
        }

        fun isValidEmail(email: String): Boolean {
            val regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
            return email.matches(regexPattern.toRegex())
        }

        fun isValidName(name: String): Boolean {
            val regexPattern = "^[A-Za-z]+$"
            return name.matches(regexPattern.toRegex())
        }
    }

}