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
import com.example.mad_assignment.databinding.FragmentPropertyHostInsertBinding
import com.example.mad_assignment.util.cropToBlob
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.PropertyVM


class PropertyHostInsertFragment : Fragment() {

    private lateinit var binding: FragmentPropertyHostInsertBinding
    private val nav by lazy { findNavController() }

    private val propertyVM: PropertyVM by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentPropertyHostInsertBinding.inflate(inflater, container, false)

        reset()
        binding.imgPropertyInsert.setOnClickListener { select() }
        binding.btnSubmitProperty.setOnClickListener{ submit() }
        binding.btnResetProperty.setOnClickListener{ reset() }


        return binding.root
    }
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imgPropertyInsert.setImageURI(it)
    }

    private fun select() {
        // Select file
        getContent.launch("image/*")
    }

    private fun reset() {

        //TODO: Auto generate ID
        binding.edtPropertyID.text.clear()
        binding.edtPropertyName.text.clear()
        binding.edtPropertyPrice.text.clear()
        binding.imgPropertyInsert.setImageDrawable(null)
        binding.edtAddress.text.clear()
        binding.edtCity.text.clear()
        binding.spnStateInsert.setSelection(0)
        binding.edtTTLBathroom.text.clear()
        binding.edtTTLBedroom.text.clear()
        binding.edtDescription.text.clear()

        binding.edtPropertyName.requestFocus()


    }



    private fun submit() {

        val p = Property(
            id = binding.edtPropertyID.text.toString().trim(),
            propertyName = binding.edtPropertyName.text.toString().trim(),
            propertyPrice = binding.edtPropertyPrice.text.toString().toDoubleOrNull() ?: 0.0,
            propertyImage = binding.imgPropertyInsert.cropToBlob(300, 300),
            propertyAddress = binding.edtAddress.text.toString().trim(),
            propertyCity = binding.edtCity.text.toString().trim(),
            propertyState = binding.spnStateInsert.selectedItem.toString(),
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


}