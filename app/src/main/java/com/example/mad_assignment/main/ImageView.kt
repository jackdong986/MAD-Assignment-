package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentImageViewBinding

class ImageView : Fragment() {

    private lateinit var binding: FragmentImageViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageViewBinding.inflate(inflater, container, false)

        // Retrieve the image resource ID from the arguments
        val imageResId = arguments?.getInt("imageResId") ?: R.drawable.icon_image_not_found_free_vector

        // Set the image resource to the ImageView
        binding.fullImageView.setImageResource(imageResId)

        return binding.root
    }
}