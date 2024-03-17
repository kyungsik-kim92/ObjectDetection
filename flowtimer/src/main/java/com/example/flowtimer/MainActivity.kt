package com.example.flowtimer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flowtimer.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            countDownTimer()
        }

    }


    private suspend fun countDownTimer() {
        viewModel.countdownState.collect { countdown ->
            binding.counter.text = "$countdown"

            if (countdown == 0) {
                binding.counter.text = "종료"
            }
        }
    }

}



