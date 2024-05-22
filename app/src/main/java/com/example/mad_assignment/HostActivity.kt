package com.example.mad_assignment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mad_assignment.databinding.ActivityHostBinding
import com.example.mad_assignment.viewModel.HostVM
import com.example.mad_assignment.viewModel.PropertyVM
import com.example.mad_assignment.viewModel.RentingVM

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.nav_host)!!.findNavController() }
    private lateinit var abc: AppBarConfiguration

    private val propertyVM: PropertyVM by viewModels()
    private val rentingVM: RentingVM by viewModels()
    private val hostVM: HostVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        propertyVM.init()
        rentingVM.init()
        hostVM.init()

        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(
                R.id.hostDashboardFragment,
                R.id.propertyHostFragment,
                R.id.hostRentingFragment,
                R.id.hostProfileFragment,
            )
        )
        
        binding.bv.setupWithNavController(nav)


    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp()
    }
}