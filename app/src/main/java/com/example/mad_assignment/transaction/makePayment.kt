package com.example.mad_assignment.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentMakePaymentBinding

class makePayment : Fragment() {

    private var _binding: FragmentMakePaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSuccess.setOnClickListener {
            binding.textResult.text = "Payment Successfully"
            Toast.makeText(requireContext(), "Payment Successfully", Toast.LENGTH_SHORT).show()
            navigateToPropertyHome()
        }

        binding.buttonFailed.setOnClickListener {
            binding.textResult.text = "Payment Failed"
            Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()
            navigateToPropertyHome()
        }
    }

    private fun navigateToPropertyHome() {
        // Assuming you have an action from makePayment to propertyDetails in your nav_graph.xml
        findNavController().navigate(R.id.property_Home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
