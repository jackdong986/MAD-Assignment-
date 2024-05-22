package com.example.mad_assignment.accountHost

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.ActivityAuthHostBinding

class AuthHostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthHostBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_login)!!.findNavController() }
    private lateinit var abc: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(
                R.id.hostLoginFragment,
                R.id.hostRegisterFragment,
                R.id.hostForgotPasswordFragment
            )
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp()
    }
}