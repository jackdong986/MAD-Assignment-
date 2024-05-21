package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyHostBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.adapter.PropertyHostAdapter
import com.example.mad_assignment.databinding.FragmentHostProfileBinding
import com.example.mad_assignment.databinding.FragmentHostReservationBinding
import com.example.mad_assignment.viewModel.PropertyVM


class HostProfileFragment : Fragment() {

    private lateinit var binding: FragmentHostProfileBinding
    private val nav by lazy { findNavController() }

    private val propertyVM: PropertyVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostProfileBinding.inflate(inflater, container, false)




        return binding.root
    }


}
