package com.example.mad_assignment.client.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.databinding.FragmentPaymentHistoryBinding
import com.example.mad_assignment.payment.PaymentHistoryAdapter
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.Renting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class paymentHistory : Fragment() {
    private lateinit var binding: FragmentPaymentHistoryBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        fetchRentingData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        paymentHistoryAdapter = PaymentHistoryAdapter(listOf(), mapOf())
        binding.recyclerView.adapter = paymentHistoryAdapter
    }

    private fun fetchRentingData() {

        firestore.collection("renting")
            .get()
            .addOnSuccessListener { rentingResult ->
                val rentingList = mutableListOf<Renting>()
                val propertyIds = mutableSetOf<String>()

                for (document in rentingResult) {
                    val renting = document.toObject(Renting::class.java)
                    rentingList.add(renting)
                    propertyIds.add(renting.propertyId)
                }

                fetchProperties(propertyIds) { propertiesMap ->
                    paymentHistoryAdapter.updateData(rentingList, propertiesMap)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to fetch data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchProperties(propertyIds: Set<String>, callback: (Map<String, Property>) -> Unit) {
        firestore.collection("properties")
            .get()
            .addOnSuccessListener { propertyResult ->
                val propertiesMap = mutableMapOf<String, Property>()

                for (document in propertyResult) {
                    val property = document.toObject(Property::class.java)
                    propertiesMap[property.id] = property
                }

                callback(propertiesMap)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to fetch properties: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}