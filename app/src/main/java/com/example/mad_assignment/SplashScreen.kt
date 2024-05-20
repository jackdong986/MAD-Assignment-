package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_assignment.databinding.ActivityMainBinding
import com.example.mad_assignment.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val splashScreenDuration = 2000L // 2 seconds
//        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val hostActivityIntent = Intent(this, HostActivity::class.java)
        val splashScreenRunnable = Runnable {
//            startActivity(mainActivityIntent)
            startActivity(hostActivityIntent)
            finish()
        }
        // Schedule the splash screen to finish after a delay
        window.decorView.postDelayed(splashScreenRunnable, splashScreenDuration)
    }
}