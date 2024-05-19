package com.example.mad_assignment.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.adapter.Property
import com.example.mad_assignment.databinding.FragmentPropertyDetailsBinding
import com.example.mad_assignment.viewModel.PropertyViewModel

class propertyDetails : Fragment() {

    private lateinit var binding: FragmentPropertyDetailsBinding
    private val nav by lazy{findNavController()}
    private val propertyViewModel: PropertyViewModel by activityViewModels()



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

        binding.propertyImage.setImageResource(args.propertyImageResId)
        binding.propertyName.text = args.propertyName
        binding.propertyPrice.text = args.propertyPrice
        binding.propertyAddress.text = args.propertyAddress
        binding.propertyCity.text = args.propertyCity
        binding.propertyState.text = args.propertyState
        binding.propertyBathroom.text = args.propertyBathrooms.toString()
        binding.propertyBedroom.text = args.propertyBedrooms.toString()
        binding.propertyDescription.text = args.propertyDescription

        val property = Property(args.propertyName, args.propertyPrice, args.propertyImageResId, args.propertyAddress, args.propertyCity, args.propertyState, args.propertyBathrooms, args.propertyBedrooms, args.propertyDescription)

        if(propertyViewModel.wishlist.value?.contains(property) == true){
            binding.buttonAddToFavorites.text = "Remove from Favorites"
        }
        else{
            binding.buttonAddToFavorites.text = "Add to Favorites"

        }

        binding.buttonAddToFavorites.setOnClickListener {
            if(propertyViewModel.wishlist.value?.contains(property) == false){
                context?.let { it1 -> propertyViewModel.addToWishlist(property, it1) }
                binding.buttonAddToFavorites.text = "Remove from Favorites"
            }else{
                context?.let { it1 -> propertyViewModel.removeFromWishlist(property, it1) }
                binding.buttonAddToFavorites.text = "Add to Favorites"
            }

        }

        binding.buttonRentNow.setOnClickListener{
            findNavController().navigate(R.id.propertyCheckout)
        }
    }

}