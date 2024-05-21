package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.databinding.FragmentHostRentingBinding
import com.example.mad_assignment.viewModel.PropertyVM


class HostRentingFragment : Fragment() {

    private lateinit var binding: FragmentHostRentingBinding
    private val nav by lazy { findNavController() }

    private val propertyVM: PropertyVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostRentingBinding.inflate(inflater, container, false)




        return binding.root
    }


}