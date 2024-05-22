package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyHostDetailBinding
import com.example.mad_assignment.util.cropToBlob
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.PropertyVM


class PropertyHostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPropertyHostDetailBinding
    private val nav by lazy { findNavController() }
    private val propertyId by lazy { arguments?.getString("propertyId") ?: "" }

    private val propertyVM: PropertyVM by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentPropertyHostDetailBinding.inflate(inflater, container, false)

        reset()
        binding.imgProperty.setOnClickListener { select() }
        binding.btnUpdateProperty.setOnClickListener{ update() }
        binding.btnDeleteProperty.setOnClickListener{ delete() }

        val property = propertyVM.get(propertyId)
        if (property == null) {
            nav.navigateUp()
            return null
        }

        return binding.root
    }

    private fun reset() {
        val property = propertyVM.get(propertyId)
        if (property == null) {
            nav.navigateUp()
            return
        }



        binding.edtPropertyName.setText(property.propertyName)
        binding.edtPropertyPrice.setText(property.propertyPrice.toString())
        binding.imgProperty.setImageBlob(property.propertyImage)
        binding.edtAddress.setText(property.propertyAddress)
        binding.edtCity.setText(property.propertyCity)

        val propertyState = property.propertyState
        val statesArray = resources.getStringArray(R.array.states_updates)
        val stateIndex = statesArray.indexOfFirst { it == propertyState }

        if(stateIndex !=-1){
            binding.spnStateUpdate.setSelection(stateIndex)
        } else {
            binding.spnStateUpdate.setSelection(0)
        }
        binding.edtTTLBathroom.setText(property.ttlBathrooms.toString())
        binding.edtTTLBedroom.setText(property.ttlBedrooms.toString())
        binding.edtDescription.setText(property.propertyDescription)

        binding.edtPropertyName.requestFocus()


    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imgProperty.setImageURI(it)
    }

    private fun select() {
        // Select file
        getContent.launch("image/*")
    }

    private fun update() {

        val p = Property(
            propertyName = binding.edtPropertyName.text.toString().trim(),
            propertyPrice = binding.edtPropertyPrice.text.toString().toDoubleOrNull() ?: 0.0,
            propertyImage = binding.imgProperty.cropToBlob(300, 300),
            propertyAddress = binding.edtAddress.text.toString().trim(),
            propertyCity = binding.edtCity.text.toString().trim(),
            propertyState = binding.spnStateUpdate.selectedItem.toString(),
            ttlBathrooms = binding.ttlHostBathroom.text.toString().toIntOrNull() ?: 0,
            ttlBedrooms = binding.ttlHostBedroom.text.toString().toIntOrNull() ?: 0,
            propertyDescription = binding.edtDescription.text.toString().trim()
        )

//        val e = propertyVM.validate(p)
//        if (e != "") {
//            errorDialog(e)
//            return
//        }

        propertyVM.set(p)
        nav.navigateUp()
    }

    private fun delete() {
        propertyVM.delete(propertyId)
        nav.navigateUp()
    }



}