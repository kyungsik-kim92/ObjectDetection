package com.example.flowtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    val channel = Channel<Int>()


    lifecycleScope.launch{
        repeat(3){
            delay(100)
            channel.send(it)
            println("send: $it")
        }
    }

        repeat(3){
            delay(300)
            println("receive ${channel.receive()}")
        }


    }


}



