package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.adapter.RentingHostAdapter
import com.example.mad_assignment.databinding.FragmentHostRentingBinding
import com.example.mad_assignment.viewModel.RentingVM


class HostRentingFragment : Fragment() {

    private lateinit var binding: FragmentHostRentingBinding
    private val nav by lazy { findNavController() }

    private val rentingVM: RentingVM by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostRentingBinding.inflate(inflater, container, false)

        val adapter = RentingHostAdapter { h, r ->
            h.binding.btnViewRentingDetail.setOnClickListener {
                detail(r.id)
            }
        }

        binding.rvHostRenting.adapter = adapter

        rentingVM.getResultLD().observe(viewLifecycleOwner) {
//            binding.txtCount.text = "${it.size} Record(s)"
            adapter.submitList(it)
        }

        binding.svHostRenting.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true

            override fun onQueryTextChange(name: String): Boolean {
                rentingVM.search(name)
                return true
            }
        })

        binding.spnRentingPaymentStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val field = binding.spnRentingPaymentStatus.selectedItem.toString()
                rentingVM.filter(field)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        return binding.root
    }

    private fun detail(id: String) {
        nav.navigate(R.id.hostRentingDetailFragment, bundleOf("rentingId" to id))
    }


}