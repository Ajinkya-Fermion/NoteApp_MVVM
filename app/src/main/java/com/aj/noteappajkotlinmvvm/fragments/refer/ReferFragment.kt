package com.aj.noteappajkotlinmvvm.fragments.refer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.aj.noteappajkotlinmvvm.di.ActivityContext
import com.aj.noteappajkotlinmvvm.R
import com.aj.noteappajkotlinmvvm.databinding.FragmentReferBinding
import com.aj.noteappajkotlinmvvm.activities.home.sharedviewmodel.HomeActivityViewModel

class ReferFragment : Fragment(R.layout.fragment_refer) {

    private var _binding : FragmentReferBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReferViewModel
    private lateinit var factory : ReferViewModelFactory
    private val homeActivityViewModel by activityViewModels<HomeActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeActivityViewModel.isFabVisible.value = false
        _binding = FragmentReferBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activityContext = ActivityContext(root.context) //Getting Activity context
        factory = ReferViewModelFactory(activityContext,"9665494024")
        viewModel = ViewModelProvider(this,factory).get(ReferViewModel::class.java)
        binding.referViewModel = viewModel
//        binding.lifecycleOwner = this
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}