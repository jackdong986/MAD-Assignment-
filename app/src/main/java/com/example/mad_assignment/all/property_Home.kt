package com.example.mad_assignment.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyHomeBinding

class property_Home : Fragment() {
    private lateinit var binding: FragmentPropertyHomeBinding
    private val nav by lazy{findNavController()}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyHomeBinding.inflate(inflater, container, false)
        binding.testBtn.setOnClickListener{
            nav.navigate(R.id.propertyDetails)
        }

        return binding.root
    }
}