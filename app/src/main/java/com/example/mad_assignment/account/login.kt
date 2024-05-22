package com.example.mad_assignment.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val nav by lazy { findNavController() }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            nav.navigate(R.id.registration)
        }

        binding.forgotPasswordButton.setOnClickListener {
            nav.navigate(R.id.forgotPassword)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to home screen or main activity
                    saveAuthenticationData(email, password)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun saveAuthenticationData(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("customer").document(email)

        // Check if the user already exists in Firestore
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    // User already exists, update the existing document with the latest data
                    userRef.update("password", password)
                        .addOnSuccessListener {
                            println("DocumentSnapshot updated with password: $password")
                        }
                        .addOnFailureListener { e ->
                            println("Error updating document: $e")
                        }
                } else {
                    // User does not exist, add a new document
                    val userData = hashMapOf(
                        "id" to "default_id",
                        "customerName" to "Default Name",
                        "customerEmail" to email,
                        "cusImage" to "default_image_url",
                        "password" to password
                    )

                    db.collection("customer")
                        .document(email)
                        .set(userData)
                        .addOnSuccessListener {
                            println("DocumentSnapshot added with ID: $email")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }
                }
            } else {
                println("get failed with ${task.exception}")
            }
        }
    }


}
