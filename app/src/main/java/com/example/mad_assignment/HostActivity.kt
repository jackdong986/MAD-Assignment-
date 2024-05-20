package com.example.mad_assignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mad_assignment.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.nav_host)!!.findNavController() }
    private lateinit var abc: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(
                R.id.hostDashboardFragment,
                R.id.propertyHostFragment,
                R.id.hostReservationFragment,
                R.id.hostProfileFragment,
            )
        )
        
        binding.bv.setupWithNavController(nav)


    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp()
    }
}