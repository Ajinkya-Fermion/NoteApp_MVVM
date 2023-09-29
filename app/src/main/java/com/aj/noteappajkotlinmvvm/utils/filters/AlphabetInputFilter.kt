package com.aj.noteappajkotlinmvvm.utils.filters

import android.text.InputFilter
import android.text.Spanned

//https://in-kotlin.com/android/edittext/#:~:text=To%20set%20an%20input%20filter,input%20filters%20for%20the%20EditText.
class AlphabetInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val regex = Regex("[^[a-zA-Z ]]") // Matches any alphabet,space characters only
        return source?.replace(regex, "")
    }
}