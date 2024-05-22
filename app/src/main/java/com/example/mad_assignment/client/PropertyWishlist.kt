package com.example.mad_assignment.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.databinding.FragmentPropertyWishlistBinding
import com.example.mad_assignment.adapter.PassWishlist
import com.example.mad_assignment.adapter.PropertyAdapter

class PropertyWishlist : Fragment() {
    private lateinit var binding: FragmentPropertyWishlistBinding
    private val nav by lazy{findNavController()}
    private val passWishlist: PassWishlist by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        passWishlist.wishlist.observe(viewLifecycleOwner) { properties ->
            recyclerView.adapter = PropertyAdapter(properties) { property ->
                // Handle property click if needed
                val action = PropertyWishlistDirections.actionPropertyWishlistToPropertyDetails(
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
                nav.navigate(action)
            }
        }
    }
}