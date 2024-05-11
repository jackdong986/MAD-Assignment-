package com.example.mad_assignment.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertySearchBinding
import com.example.mad_assignment.databinding.FragmentPropertyWishlistBinding

class propertyWishlist : Fragment() {
    private lateinit var binding: FragmentPropertyWishlistBinding
    private val nav by lazy{findNavController()}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }
}