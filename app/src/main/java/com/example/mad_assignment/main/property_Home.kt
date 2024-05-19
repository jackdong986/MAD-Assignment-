package com.example.mad_assignment.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mad_assignment.R
import com.example.mad_assignment.adapter.ImageSliderAdapter
import com.example.mad_assignment.adapter.Property
import com.example.mad_assignment.adapter.PropertyAdapter
import com.example.mad_assignment.databinding.FragmentPropertyHomeBinding

class property_Home : Fragment() {
    private lateinit var binding: FragmentPropertyHomeBinding
    private val nav by lazy { findNavController() }

    //to set the image slider in 3 seconds
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentIndex = 0
    private val slideInterval = 3000L // 3 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyHomeBinding.inflate(inflater, container, false)
        val images = listOf(R.drawable.__frontal, R.drawable.frontal2, R.drawable.frontal3)

        // Set up ViewPager2 adapter
        val imageSliderAdapter = ImageSliderAdapter(images)
        binding.imageSlider.adapter = imageSliderAdapter

        // Initialize Handler and Runnable
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (currentIndex == images.size) {
                    currentIndex = 0
                }
                binding.imageSlider.currentItem = currentIndex
                currentIndex++
                handler.postDelayed(this, slideInterval)
            }
        }

        // Start auto-slide
        handler.postDelayed(runnable, slideInterval)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val properties = listOf(
            Property(
                "Property 1",
                "$1000/month",
                R.drawable.property1,
                "123 Main St",
                "Any town",
                "AnyState",
                2,
                3,
                "This cozy property is located in a quiet neighborhood..."
            ),
            Property(
                "Property 2",
                "$1200/month",
                R.drawable.__frontal,
                "456 Elm St",
                "Other ville",
                "AnotherState",
                2,
                2,
                "This spacious property features modern amenities..."
            ),
            // Add more properties here
        )

        val adapter = PropertyAdapter(properties) { property ->
            val action = property_HomeDirections.actionPropertyHomeToPropertyDetails(
                propertyName = property.name,
                propertyPrice = property.price,
                propertyImageResId = property.imageResId,
                propertyAddress = property.address,
                propertyCity = property.city,
                propertyState = property.state,
                propertyBathrooms = property.bathrooms,
                propertyBedrooms = property.bedrooms,
                propertyDescription = property.description
            )
            nav.navigate(action)
        }
        recyclerView.adapter = adapter
    }
}


