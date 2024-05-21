package com.example.mad_assignment.main

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyDetailsBinding
import com.example.mad_assignment.adapter.PassWishlist
import com.example.mad_assignment.viewModel.Property
import com.google.firebase.firestore.Blob

class propertyDetails : Fragment() {

    private lateinit var binding: FragmentPropertyDetailsBinding
    private val nav by lazy { findNavController() }
    private val passWishlist: PassWishlist by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments
        val propertyName = propertyDetailsArgs.fromBundle(requireArguments()).propertyName
        val propertyPrice = propertyDetailsArgs.fromBundle(requireArguments()).propertyPrice.toDouble()
        val propertyAddress = propertyDetailsArgs.fromBundle(requireArguments()).propertyAddress
        val propertyCity = propertyDetailsArgs.fromBundle(requireArguments()).propertyCity
        val propertyState = propertyDetailsArgs.fromBundle(requireArguments()).propertyState
        val propertyBathrooms = propertyDetailsArgs.fromBundle(requireArguments()).propertyBathrooms
        val propertyBedrooms = propertyDetailsArgs.fromBundle(requireArguments()).propertyBedrooms
        val propertyDescription = propertyDetailsArgs.fromBundle(requireArguments()).propertyDescription

        // Load propertyImage using Glide
        val propertyImage = Blob.fromBytes(propertyDetailsArgs.fromBundle(requireArguments()).propertyImage.toByteArray())
        Glide.with(requireContext())
            .load(propertyImage)
            .placeholder(R.drawable.icon_image_not_found_free_vector)
            .error(R.drawable.icon_image_not_found_free_vector)
            .into(binding.propertyImage)


        val propertyDatatype = Property(
            propertyName = propertyName,
            propertyPrice = propertyPrice,
            propertyImage = propertyImage,
            propertyAddress = propertyAddress,
            propertyCity = propertyCity,
            propertyState = propertyState,
            ttlBathrooms = propertyBathrooms,
            ttlBedrooms = propertyBedrooms,
            propertyDescription = propertyDescription
        )

        if (passWishlist.wishlist.value?.contains(propertyDatatype) == true) {
            binding.buttonAddToFavorites.text = "Remove from Favorites"
        } else {
            binding.buttonAddToFavorites.text = "Add to Favorites"
        }

        binding.buttonAddToFavorites.setOnClickListener {
            if (passWishlist.wishlist.value?.contains(propertyDatatype) == false) {
                context?.let { passWishlist.addToWishlist(propertyDatatype, it) }
                binding.buttonAddToFavorites.text = "Remove from Favorites"
            } else {
                context?.let { passWishlist.removeFromWishlist(propertyDatatype, it) }
                binding.buttonAddToFavorites.text = "Add to Favorites"
            }
        }

        binding.buttonRentNow.setOnClickListener {
            findNavController().navigate(R.id.propertyCheckout)
        }
    }
}
