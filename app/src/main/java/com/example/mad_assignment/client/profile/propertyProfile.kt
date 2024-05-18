package com.example.mad_assignment.client.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyProfileBinding
import com.example.mad_assignment.databinding.FragmentPropertySearchBinding
import com.example.mad_assignment.databinding.FragmentPropertyWishlistBinding

class propertyProfile : Fragment() {

    private lateinit var binding: FragmentPropertyProfileBinding
    private val nav by lazy{findNavController()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profilePicture.setOnClickListener {
            // Handle changing profile picture
            Toast.makeText(requireContext(), "Change Profile Picture", Toast.LENGTH_SHORT).show()
        }

        binding.userName.setOnClickListener {
            // Handle changing user name
            Toast.makeText(requireContext(), "Change User Name", Toast.LENGTH_SHORT).show()
        }

        binding.btnUserFeedback.setOnClickListener {
            // Navigate to user feedback page
            findNavController().navigate(R.id.userFeedback)
        }

        binding.btnPaymentHistory.setOnClickListener {
            // Navigate to payment history page
            findNavController().navigate(R.id.paymentHistory)
        }

        binding.btnReviewRating.setOnClickListener {
            // Navigate to review and rating page
            //findNavController().navigate(R.id.action_propertyProfile_to_reviewRating)
        }
    }

}