package com.example.mad_assignment.client.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mad_assignment.R
import com.example.mad_assignment.viewModel.Feedback
import com.google.firebase.firestore.FirebaseFirestore

class userFeedback : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var etFeedback: EditText
    private lateinit var btnSubmit: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_feedback, container, false)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Bind views
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        ratingBar = view.findViewById(R.id.ratingBar)
        etFeedback = view.findViewById(R.id.etFeedback)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        // Set click listener for the submit button
        btnSubmit.setOnClickListener {
            submitFeedback()
        }

        return view
    }

    private fun submitFeedback() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val rating = ratingBar.rating
        val feedbackText = etFeedback.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || feedbackText.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Feedback object
        val feedback = Feedback(name, email, rating, feedbackText)

        // Save feedback to Firestore
        db.collection("feedback")
            .add(feedback)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                // Optionally, clear the form fields
                etName.text.clear()
                etEmail.text.clear()
                ratingBar.rating = 0f
                etFeedback.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to submit feedback: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
