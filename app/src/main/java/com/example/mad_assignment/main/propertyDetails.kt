package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = propertyDetailsArgs.fromBundle(requireArguments())
        binding.propertyImage.setImageResource(args.propertyImageResId)
        binding.propertyName.text = args.propertyName
        binding.propertyPrice.text = args.propertyPrice

        binding.buttonAddToFavorites.setOnClickListener {
            val property = Property(args.propertyName, args.propertyPrice, args.propertyImageResId)
            propertyViewModel.addToWishlist(property)
            Toast.makeText(requireContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show()
        }
    }

}