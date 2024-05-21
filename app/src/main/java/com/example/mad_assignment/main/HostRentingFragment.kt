package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.adapter.RentingHostAdapter
import com.example.mad_assignment.databinding.FragmentHostRentingBinding
import com.example.mad_assignment.viewModel.RentingVM


class HostRentingFragment : Fragment() {

    private lateinit var binding: FragmentHostRentingBinding
    private val nav by lazy { findNavController() }

    private val rentingVM: RentingVM by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostRentingBinding.inflate(inflater, container, false)

        val adapter = RentingHostAdapter { h, p ->
            h.binding.btnViewRentingDetail.setOnClickListener {
//                detail(p.id)
            }
        }

        binding.rvHostRenting.adapter = adapter

        rentingVM.getRentingLD().observe(viewLifecycleOwner) {
//            binding.txtCount.text = "${it.size} Record(s)"
            adapter.submitList(it)
        }


        return binding.root
    }


}