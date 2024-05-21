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
import com.example.mad_assignment.viewModel.PropertyVM


class PropertyHostFragment : Fragment() {

    private lateinit var binding: FragmentPropertyHostBinding
    private val nav by lazy { findNavController() }

    private val propertyVM: PropertyVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPropertyHostBinding.inflate(inflater, container, false)

        binding.btnAddProperty.setOnClickListener {
            nav.navigate(R.id.propertyHostInsertFragment)
        }

        val adapter = PropertyHostAdapter { h, p ->
            h.binding.btnPropertyDetail.setOnClickListener { detail(p.id) }
        }
        binding.rvProperytHost.adapter = adapter
//        binding.rvProperytHost.layoutManager = LinearLayoutManager(context)

        propertyVM.getResultLD().observe(viewLifecycleOwner) {
            binding.txtCount.text = "${it.size} Record(s)"


            adapter.submitList(it)
        }

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true

            override fun onQueryTextChange(name: String): Boolean {
                propertyVM.search(name)
                return true
            }
        })

        binding.spnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val field = binding.spnSort.selectedItem.toString()
                propertyVM.sort(field)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        binding.spnState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val state = binding.spnState.selectedItem.toString()
                propertyVM.filter(state)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }



        return binding.root
    }

    private fun detail(propertyId: String) {
        nav.navigate(R.id.propertyHostDetailFragment, bundleOf(
            "propertyId" to propertyId
        ))
    }
}