package com.example.mad_assignment.main

import android.annotation.SuppressLint
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
import com.example.mad_assignment.viewModel.Property_datatype
import com.example.mad_assignment.adapter.PassWishlist

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

        val args = propertyDetailsArgs.fromBundle(requireArguments())

        binding.propertyName.text = args.propertyName
        binding.propertyPrice.text = args.propertyPrice.toString()
        binding.propertyAddress.text = args.propertyAddress
        binding.propertyCity.text = args.propertyCity
        binding.propertyState.text = args.propertyState
        binding.propertyBathroom.text = args.propertyBathrooms.toString()
        binding.propertyBedroom.text = args.propertyBedrooms.toString()
        binding.propertyDescription.text = args.propertyDescription

        Glide.with(this)
            .load(args.propertyImage)
            .placeholder(R.drawable.icon_image_not_found_free_vector)
            .error(R.drawable.icon_image_not_found_free_vector)
            .into(binding.propertyImage)

        val propertyDatatype = Property_datatype(
            propertyName = args.propertyName,
            propertyPrice = args.propertyPrice,
            propertyImage = args.propertyImage,
            propertyAddress = args.propertyAddress,
            propertyCity = args.propertyCity,
            propertyState = args.propertyState,
            ttlBathrooms = args.propertyBathrooms,
            ttlBedrooms = args.propertyBedrooms,
            propertyDescription = args.propertyDescription
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
