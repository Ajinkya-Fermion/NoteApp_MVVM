package com.aj.noteappajkotlinmvvm.fragments.aboutus

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aj.noteappajkotlinmvvm.NoteApplication.Companion.longToast
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.activities.home.sharedviewmodel.HomeActivityViewModel
import com.aj.noteappajkotlinmvvm.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment(R.layout.fragment_about_us) {

    private var _binding : FragmentAboutUsBinding? = null
    private val binding get() = _binding!!
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivityViewModel.isFabVisible.value = false
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.lifecycleOwner = this
        binding.apply {
            //We are passing progressBar to 'MyWebViewClient' as we can't access 'progressBar'
            // from Global access approach
            wvAboutus.webViewClient = MyWebViewClient(root.context as Activity,progressBar)
            wvAboutus.loadUrl("https://www.javatpoint.com/")
        }
        return root
    }

    class MyWebViewClient constructor(private val activity: Activity,private val progressBar: ProgressBar) : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            progressBar.visibility = View.VISIBLE
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            progressBar.visibility = View.VISIBLE
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            progressBar.visibility = View.GONE
            longToast("Got Error! $error")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}