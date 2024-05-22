package com.example.mad_assignment.transaction

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentMakePaymentBinding
import com.example.mad_assignment.viewModel.Renting
import com.google.firebase.firestore.FirebaseFirestore

class makePayment : Fragment() {

    private var _binding: FragmentMakePaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = makePaymentArgs.fromBundle(requireArguments())
        val rentingData = args.rentingData


        binding.buttonSuccess.setOnClickListener {
            updatePaymentStatus(rentingData, "Success")
            binding.textResult.text = "Payment Successfully"
            Toast.makeText(requireContext(), "Payment Successfully", Toast.LENGTH_SHORT).show()
            navigateToPropertyHome()
        }

        binding.buttonFailed.setOnClickListener {
            updatePaymentStatus(rentingData, "Failed")
            binding.textResult.text = "Payment Failed"
            Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()
            navigateToPropertyHome()
        }
    }

    private fun updatePaymentStatus(rentingData: Renting, newStatus: String) {
        // Update the payment status in Firestore
        firestore.collection("renting")
            .document(rentingData.id)
            .update("paymentStatus", newStatus)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Payment status updated successfully", Toast.LENGTH_SHORT).show()
                navigateToPropertyHome()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to update payment status: ${e.message}", Toast.LENGTH_SHORT).show()
                navigateToPropertyHome() // Navigate even if update fails
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
