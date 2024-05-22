package com.example.mad_assignment.accountHost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.HostActivity
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostLoginBinding
import com.example.mad_assignment.util.errorDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class HostLoginFragment : Fragment() {
    private lateinit var binding: FragmentHostLoginBinding
    private val nav by lazy { findNavController() }
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHostLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnHostLogin.setOnClickListener {
            val email = binding.editTextHostEmailAddress.text.toString().trim()
            val password = binding.editTextHostPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                errorDialog("Please enter email and password")
            }
        }

        binding.btnHostRegister.setOnClickListener {
            nav.navigate(R.id.hostRegisterFragment)
        }

        binding.btnForgotPassword.setOnClickListener {
            nav.navigate(R.id.hostForgotPasswordFragment)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        checkIfHost(userId)
                    } else {
                        errorDialog("User ID not found")
                    }
                } else {
                    errorDialog("Login failed")
                }
            }
    }

    private fun checkIfHost(userId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val hostRef = firestore.collection("hosts").document(userId)
        hostRef.get().addOnSuccessListener { document: DocumentSnapshot ->
            if (document.exists()) {
                val intent = Intent(requireContext(), HostActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                auth.signOut() // Sign out the user as they are not a host
                errorDialog("Access denied: You are not registered as a host")
            }
        }.addOnFailureListener { exception: Exception ->
            errorDialog("Failed to check host status: ${exception.message}")
        }
    }
}