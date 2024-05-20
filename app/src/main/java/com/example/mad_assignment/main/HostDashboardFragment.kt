package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostDashboardBinding
import com.example.mad_assignment.util.toast
import com.example.mad_assignment.viewModel.RESTORE


class HostDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHostDashboardBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostDashboardBinding.inflate(inflater, container, false)

        binding.restore.setOnClickListener { restore() }

        return binding.root
    }

    private fun restore() {
        RESTORE(requireContext())
        toast("Done")
    }

}