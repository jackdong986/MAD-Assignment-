package com.example.mad_assignment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertySearchBinding
import com.example.mad_assignment.adapter.PropertyAdapter
import com.example.mad_assignment.viewModel.Property
import com.google.firebase.firestore.FirebaseFirestore

class propertySearch : Fragment() {

    private lateinit var binding: FragmentPropertySearchBinding
    private val nav by lazy { findNavController() }
    private lateinit var propertyAdapter: PropertyAdapter
    private val db = FirebaseFirestore.getInstance()
    private var properties: List<Property> = listOf()
    private var filteredProperties: List<Property> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        propertyAdapter = PropertyAdapter(filteredProperties) { property ->
            val action = propertySearchDirections.actionPropertySearchToPropertyDetails(
                id = property.id,
                propertyName = property.propertyName,
                propertyPrice = property.propertyPrice.toInt(),
                propertyImage = property.propertyImage.toBytes().toString(),
                propertyAddress = property.propertyAddress,
                propertyCity = property.propertyCity,
                propertyState = property.propertyState,
                propertyBathrooms = property.ttlBathrooms,
                propertyBedrooms = property.ttlBedrooms,
                propertyDescription = property.propertyDescription,
                hostId = property.hostId
            )
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = propertyAdapter

        // Setup dropdowns
        setupDropdown(binding.spinnerBedrooms, R.array.bedroom_options)
        setupDropdown(binding.spinnerBathrooms, R.array.bathroom_options)

        // Fetch properties from Firestore
        fetchProperties()

        // Setup SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProperties()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProperties()
                return false
            }
        })

        // Setup Filter Button
        binding.searchButton.setOnClickListener {
            filterProperties()
        }
    }

    private fun setupDropdown(spinner: Spinner, arrayResId: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun fetchProperties() {
        db.collection("properties")
            .get()
            .addOnSuccessListener { result ->
                properties = result.documents.mapNotNull { it.toObject(Property::class.java) }
                filterProperties()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching properties", exception)
            }
    }

    private fun filterProperties() {
        val query = binding.searchView.query.toString().lowercase()
        val selectedBedrooms = binding.spinnerBedrooms.selectedItem.toString().toIntOrNull()
        val selectedBathrooms = binding.spinnerBathrooms.selectedItem.toString().toIntOrNull()

        filteredProperties = properties.filter { property ->
            property.propertyName.lowercase().contains(query) &&
                    (selectedBedrooms == null || property.ttlBedrooms == selectedBedrooms) &&
                    (selectedBathrooms == null || property.ttlBathrooms == selectedBathrooms)
        }

        propertyAdapter.updateProperties(filteredProperties)
    }
}
