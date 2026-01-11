package com.example.presentation.ui.search.word

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.presentation.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WordDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)
    }
}