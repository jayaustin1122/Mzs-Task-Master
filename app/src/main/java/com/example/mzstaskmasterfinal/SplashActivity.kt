package com.example.mzstaskmasterfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mzstaskmasterfinal.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@SplashActivity,"Welcome",Toast.LENGTH_SHORT).show()
            delay(3000L)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }
}