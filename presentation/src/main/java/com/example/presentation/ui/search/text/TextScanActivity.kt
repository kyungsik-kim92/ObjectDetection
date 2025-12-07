package com.example.presentation.ui.search.text

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.presentation.databinding.ActivityTextScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}