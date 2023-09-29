package com.aj.noteappajkotlinmvvm.fragments.refer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.longToast
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.di.ActivityContext

class ReferViewModel(
    private val activityContext: ActivityContext,
    private val mobileNo: String
) : ViewModel() {

    fun onWhatsAppShareClicked() {
        val url =
            "https://api.whatsapp.com/send?phone=${mobileNo}&text=You%20can%20now%20send%20me%20audio%20and%20video%20messages%20on%20the%20app%20-%20Chirp.%20%0A%0Ahttps%3A//bit.ly/chirp_android"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            this.data = Uri.parse(url)
            this.`package` = "com.whatsapp"
        }

        try {
            activityContext.getActivityContext().startActivity(intent)
        } catch (ex : ActivityNotFoundException){
            longToast(activityContext.getActivityContext()
                .resources.getString(R.string.no_whatsapp_app_found))
        }
    }

    fun shareTextMessageOnly() {

        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("sms_body", "Hey this is Aj")
        sendIntent.type = "vnd.android-dir/mms-sms"

        try {
            activityContext.getActivityContext().startActivity(sendIntent)
        }
        catch (ex : ActivityNotFoundException){
            longToast(activityContext.getActivityContext()
                .resources.getString(R.string.no_sms_app_found))
        }
    }

    fun sendEmail() {
        Log.i("Send email", "")
        val to = arrayOf("ajinkyam@fermion.in","ajinkyamandavkar92@gmail.com")
        val cc = arrayOf("")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.setDataAndType(Uri.parse("mailto:"),"text/plain")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        emailIntent.putExtra(Intent.EXTRA_CC, cc)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Note App Demo")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the test email from NoteApp")
        try {
            activityContext.getActivityContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            Log.i("Finished sending email...", "")
        } catch (ex: ActivityNotFoundException) {
            longToast(activityContext.getActivityContext().resources
                .getString(R.string.no_email_client_installed))
        }
    }

}