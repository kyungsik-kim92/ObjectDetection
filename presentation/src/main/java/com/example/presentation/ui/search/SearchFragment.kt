package com.example.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.presentation.databinding.FragmentSearchBinding
import com.example.presentation.ui.search.detect.DetectActivity
import com.example.presentation.ui.search.word.WordActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivDetect.setOnClickListener {
            startActivity<DetectActivity>()

        }
        binding.ivWord.setOnClickListener {
            startActivity<WordActivity>()
        }
    }

    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(requireActivity(), T::class.java))
    }
}