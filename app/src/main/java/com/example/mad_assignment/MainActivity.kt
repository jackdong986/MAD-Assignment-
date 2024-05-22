package com.example.mad_assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.mad_assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    private lateinit var abc: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(
                R.id.property_Home,
                R.id.propertyDetails,
                R.id.propertySearch,
                R.id.propertyWishlist,
                R.id.propertyProfile,
                R.id.userFeedback,
                R.id.paymentHistory,
                R.id.propertyCheckout,
                R.id.makePayment,
                R.id.imageView
            ),
            binding.root
        )
        binding.bv.setupWithNavController(nav)

        nav.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.property_Home,
                R.id.propertySearch,
                R.id.propertyWishlist,
                R.id.propertyProfile -> binding.bv.visibility = View.VISIBLE
                R.id.userFeedback,
                R.id.propertyDetails,
                R.id.propertyCheckout,
                R.id.imageView,
                R.id.makePayment,
                R.id.paymentHistory -> binding.bv.visibility = View.GONE
                else -> binding.bv.visibility = View.VISIBLE
            }
        }

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