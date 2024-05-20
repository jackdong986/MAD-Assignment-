package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val adapter = PropertyHostAdapter { h, p ->
            h.binding.btnPropertyDetail.setOnClickListener { detail(p.id) }
        }
        binding.rvProperytHost.adapter = adapter
//        binding.rvProperytHost.layoutManager = LinearLayoutManager(context)

        propertyVM.getPropertyLD().observe(viewLifecycleOwner) {
            binding.txtCount.text = "${it.size} Record(s)"


            adapter.submitList(it)
        }

        return binding.root
    }

    private fun detail(propertyId: String) {
//        nav.navigate(R.id., bundleOf(
//            "propertyId" to propertyId
//        ))
    }
}