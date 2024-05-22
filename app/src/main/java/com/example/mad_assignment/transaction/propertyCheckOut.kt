package com.example.mad_assignment.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyCheckOutBinding
import com.example.mad_assignment.viewModel.Renting
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class propertyCheckOut : Fragment() {
    private lateinit var binding: FragmentPropertyCheckOutBinding
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()
    private var propertyPricePerMonth: Double = 0.0 // Example price, replace with actual price

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyCheckOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments
        val args = propertyCheckOutArgs.fromBundle(requireArguments())
        val propertyId = args.id
        val propertyName = args.propertyName
        val propertyPrice = args.propertyPrice
        val propertyImage = args.propertyImage
        val propertyAddress = args.propertyAddress
        val propertyDescription = args.propertyDescription
        val hostId = args.hostId

        // Set data to views
        binding.propertyName.text = propertyName
        binding.propertyPrice.text = propertyPrice.toString()
        binding.propertyAddress.text = propertyAddress
        binding.propertyDescription.text = propertyDescription

        // Load property image
        Glide.with(requireContext())
            .load(propertyImage)
            .placeholder(R.drawable.icon_image_not_found_free_vector)
            .error(R.drawable.icon_image_not_found_free_vector)
            .into(binding.propertyImage)

        // Initialize the price
        propertyPricePerMonth = propertyPrice.toDouble()

        // Set up listeners for date pickers
        binding.startDate.setOnClickListener {
            showDatePickerDialog(binding.startDate, startDate)
        }

        binding.endDate.setOnClickListener {
            showDatePickerDialog(binding.endDate, endDate)
        }

        // Set up listener for payment method group
        binding.paymentMethodGroup.setOnCheckedChangeListener { _, _ ->
            // Handle payment method selection if needed
        }

        // Set up listener for the success button
        binding.successButton.setOnClickListener {
            if (validateInput()) {
                calculateTotalAmount()
                uploadRentingData("Success")
            }
        }

        // Set up listener for the failed button
        binding.failedButton.setOnClickListener {
            if (validateInput()) {
                calculateTotalAmount()
                uploadRentingData("Failed")
            }
        }
    }

    private fun showDatePickerDialog(textView: TextView, calendar: Calendar) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(textView, calendar)
                calculateTotalAmount()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInView(textView: TextView, calendar: Calendar) {
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        textView.text = format.format(calendar.time)
    }

    private fun calculateTotalAmount() {
        if (binding.startDate.text.isNotEmpty() && binding.endDate.text.isNotEmpty()) {
            val months = calculateMonthDifference(startDate, endDate)
            val totalAmount = months * propertyPricePerMonth
            binding.totalAmount.text = "RM %.2f".format(totalAmount)
        }
    }

    private fun calculateMonthDifference(startDate: Calendar, endDate: Calendar): Int {
        val startYear = startDate.get(Calendar.YEAR)
        val startMonth = startDate.get(Calendar.MONTH)
        val endYear = endDate.get(Calendar.YEAR)
        val endMonth = endDate.get(Calendar.MONTH)
        return (endYear - startYear) * 12 + (endMonth - startMonth) + 1
    }

    private fun validateInput(): Boolean {
        val startDateText = binding.startDate.text.toString()
        val endDateText = binding.endDate.text.toString()
        val selectedPaymentMethod = binding.paymentMethodGroup.checkedRadioButtonId

        if (startDateText == "Select Start Date") {
            Toast.makeText(requireContext(), "Please select a start date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (endDateText == "Select End Date") {
            Toast.makeText(requireContext(), "Please select an end date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedPaymentMethod == -1) {
            Toast.makeText(requireContext(), "Please select a payment method", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun uploadRentingData(paymentStatus: String) {
        val args = propertyCheckOutArgs.fromBundle(requireArguments())
        val renting = Renting(
            propertyAmount = propertyPricePerMonth,
            rentingStartDate = startDate.time,
            rentingEndDate = endDate.time,
            totalMonth = calculateMonthDifference(startDate, endDate),
            totalAmount = propertyPricePerMonth * calculateMonthDifference(startDate, endDate),
            propertyId = args.id,
            hostId = args.hostId,
            custId = "yourCustomerId", // Replace with actual customer ID
            paymentStatus = paymentStatus,
            createdAt = Date()
        )

        firestore.collection("renting")
            .add(renting)
            .addOnSuccessListener { documentReference ->
                // Handle success
                val docId = documentReference.id
                println("DocumentSnapshot written with ID: $docId")
            }
            .addOnFailureListener { e ->
                // Handle failure
                println("Error adding document: $e")
            }
    }
}
