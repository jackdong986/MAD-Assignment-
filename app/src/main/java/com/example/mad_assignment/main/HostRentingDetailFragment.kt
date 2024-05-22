package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostRentingDetailBinding
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.viewModel.PROPERTIES
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.RentingVM
import java.text.SimpleDateFormat
import java.util.Locale


class HostRentingDetailFragment : Fragment() {
   private lateinit var binding: FragmentHostRentingDetailBinding
   private val nav by lazy { findNavController() }
    private val rentingId by lazy { arguments?.getString("rentingId") ?: "" }

    private val ratingVM: RentingVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHostRentingDetailBinding.inflate(inflater, container, false)

        val renting = ratingVM.get(rentingId)
        if (renting == null) {
            nav.navigateUp()
            return null
        }

        val propertyID = renting.propertyId
        // find property Image, name, address from firebase based on property ID
        PROPERTIES.document(propertyID).get().addOnSuccessListener { document ->
            if (document != null) {
                val property = document.toObject(Property::class.java)
                if (property != null) {
                    // Set property image,  name, address, city, state
                    binding.imgRentingDetailProperty.setImageBlob(property.propertyImage)
                    binding.txtRentingDetailPropertyName.text = property.propertyName
                    binding.txtRentingDetailPropertyAddress.text = property.propertyAddress
                    binding.txtRentingDetailPropertyCity.text = property.propertyCity
                    binding.txtRentingDetailPropertyState.text = property.propertyState
                }
            }
        }

        binding.txtRentingDetailPropertyAmount.text = renting.propertyAmount.toString()
        binding.txtRentingDetailPropertyAmountPerMonth.text = renting.propertyAmount.toString()
        binding.txtRentingDetailTotalMonth.text = renting.totalMonth.toString()
        binding.txtRentingDetailTotalAmount.text = renting.totalAmount.toString()

        // format date to (dd MM yyyy), EG 21 June 2021
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        binding.txtRentingDetailStartDate.text = dateFormat.format(renting.rentingStartDate)
        binding.txtRentingDetailEndDate.text = dateFormat.format(renting.rentingEndDate)


        binding.btnBacktoRentingHost.setOnClickListener { nav.navigateUp() }




        return binding.root
    }


}