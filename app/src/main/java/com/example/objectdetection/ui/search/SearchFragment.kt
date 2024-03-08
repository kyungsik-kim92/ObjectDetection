package com.example.objectdetection.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.objectdetection.R
import com.example.objectdetection.base.BaseFragment
import com.example.objectdetection.databinding.FragmentSearchBinding
import com.example.objectdetection.ui.search.detect.DetectActivity
import com.example.objectdetection.ui.search.word.WordActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivDetect.setOnClickListener {
            startActivity(Intent(requireActivity(), DetectActivity::class.java))
        }
        binding.ivWord.setOnClickListener {
            startActivity(Intent(requireActivity(), WordActivity::class.java))
        }
    }
}