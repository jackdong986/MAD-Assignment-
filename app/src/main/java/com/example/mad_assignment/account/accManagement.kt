package com.example.mad_assignment.account

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.ActivityAccManagementBinding
import com.example.mad_assignment.databinding.ActivityMainBinding

class accManagement : AppCompatActivity() {
    private lateinit var binding: ActivityAccManagementBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.login_nav)!!.findNavController() }
    private lateinit var abc: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(
                R.id.login,
                R.id.registration,
                R.id.forgotPassword
            ),
            binding.root
        )
    }
    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (nav.currentDestination?.id == R.id.property_Home) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    super.onBackPressed()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        } else {
            super.onBackPressed()
        }
    }
}