package com.example.mad_assignment.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyCheckOutBinding
import com.example.mad_assignment.viewModel.Renting
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class propertyCheckOut : Fragment() {
    private lateinit var binding: FragmentPropertyCheckOutBinding
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()
    private var propertyPricePerMonth: Double = 0.0 // Example price, replace with actual price

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

        // Set up listener for the next button
        binding.nextButton.setOnClickListener {
            if (validateDates()) {
                val rentingData = prepareRentingData()
                navigateToMakePayment(rentingData)
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

    private fun validateDates(): Boolean {
        val startDateStr = binding.startDate.text.toString()
        val endDateStr = binding.endDate.text.toString()

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please select start and end dates", Toast.LENGTH_SHORT).show()
            return false
        }

        // Perform additional date validation if needed
        // Example: Ensure endDate is after startDate

        return true
    }

    private fun prepareRentingData(): Renting {
        val months = calculateMonthDifference(startDate, endDate)
        val totalAmount = months * propertyPricePerMonth
        val args = propertyCheckOutArgs.fromBundle(requireArguments())
        val hostId = args.hostId
        val propertyId = args.id

        // Prepare Renting object
        return Renting(
            propertyAmount = propertyPricePerMonth,
            rentingStartDate = startDate.time,
            rentingEndDate = endDate.time,
            totalMonth = months,
            totalAmount = totalAmount,
            propertyId = propertyId,
            hostId = hostId,
            custId = "customer_id_here", // Replace with actual customer ID
            paymentStatus = "Pending",// Initial payment status
            createdAt = Date()

        )
    }

    private fun navigateToMakePayment(rentingData: Renting) {
        val action = propertyCheckOutDirections.actionPropertyCheckoutToMakePayment2(rentingData)
        findNavController().navigate(action)
    }
}
