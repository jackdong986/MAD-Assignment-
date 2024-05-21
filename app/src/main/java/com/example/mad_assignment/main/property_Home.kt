package com.example.mad_assignment.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mad_assignment.R
import com.example.mad_assignment.adapter.ImageSliderAdapter
import com.example.mad_assignment.adapter.PropertyAdapter
import com.example.mad_assignment.databinding.FragmentPropertyHomeBinding
import com.example.mad_assignment.util.toBitmap
import com.example.mad_assignment.viewModel.Property
import com.google.firebase.firestore.FirebaseFirestore

class property_Home : Fragment() {
    private lateinit var binding: FragmentPropertyHomeBinding
    private val nav by lazy { findNavController() }

    // to set the image slider in 3 seconds
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var currentIndex = 0
    private val slideInterval = 3000L // 3 seconds

    // Firestore instance
    private lateinit var firestore: FirebaseFirestore

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

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Fetch data from Firestore
        fetchProperties()
    }

    private fun fetchProperties() {
        firestore.collection("properties")
            .get()
            .addOnSuccessListener { result ->
                val properties = mutableListOf<Property>()
                for (document in result) {
                    val property = document.toObject(Property::class.java)
                    properties.add(property)
                }
                val adapter = PropertyAdapter(properties) { property ->
                    val action = property_HomeDirections.actionPropertyHomeToPropertyDetails(
                        propertyName = property.propertyName,
                        propertyPrice = property.propertyPrice.toInt(),
                        propertyImage = property.propertyImage.toBytes().toString(),
                        propertyAddress = property.propertyAddress,
                        propertyCity = property.propertyCity,
                        propertyState = property.propertyState,
                        propertyBathrooms = property.ttlBathrooms,
                        propertyBedrooms = property.ttlBedrooms,
                        propertyDescription = property.propertyDescription
                    )
                    nav.navigate(action)
                }
                binding.recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle possible errors
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }

}
