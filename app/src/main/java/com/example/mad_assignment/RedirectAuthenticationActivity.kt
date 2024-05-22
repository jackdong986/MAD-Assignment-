package com.example.mad_assignment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_assignment.account.accManagement
import com.example.mad_assignment.accountHost.AuthHostActivity
import com.example.mad_assignment.databinding.ActivityRedirectAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

class RedirectAuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRedirectAuthenticationBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedirectAuthenticationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        binding.btnGoToCustomer.setOnClickListener {
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
        }

        binding.btnGoToHost.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is logged in, navigate to MainActivity
                val mainActivityIntent = Intent(this, HostActivity::class.java)
                startActivity(mainActivityIntent)
            } else {
                // No user is logged in, navigate to LoginActivity
                val loginIntent = Intent(this, AuthHostActivity::class.java)
                startActivity(loginIntent)
            }

        }

    }
}