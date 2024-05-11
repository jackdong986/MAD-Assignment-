package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.databinding.FragmentPropertySearchBinding

class propertySearch : Fragment() {

    private lateinit var binding: FragmentPropertySearchBinding
    private val nav by lazy{findNavController()}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertySearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}