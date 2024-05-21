package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_assignment.account.accManagement
import com.example.mad_assignment.account.login
import com.example.mad_assignment.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val splashScreenDuration = 2000L // 2 seconds
        val splashScreenRunnable = Runnable {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is logged in, navigate to MainActivity
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
            } else {
                // No user is logged in, navigate to LoginActivity
                val loginIntent = Intent(this, accManagement::class.java)
                startActivity(loginIntent)
            }
            finish()
        }
        // Schedule the splash screen to finish after a delay
        window.decorView.postDelayed(splashScreenRunnable, splashScreenDuration)
    }
}
