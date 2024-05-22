package com.example.mad_assignment.accountHost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostForgotPasswordBinding
import com.example.mad_assignment.util.errorDialog
import com.example.mad_assignment.util.infoDialog
import com.google.firebase.auth.FirebaseAuth

class HostForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentHostForgotPasswordBinding
    private val nav by lazy { findNavController() }
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.btnForgotSendEmail.setOnClickListener {
            val email = binding.edtForgotEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                infoDialog("Please enter your email")
            }
        }

        binding.btnBackToHostLogin.setOnClickListener {
            nav.navigate(R.id.hostLoginFragment)
        }

        binding.btnBackToHostRegister.setOnClickListener {
            nav.navigate(R.id.hostRegisterFragment)
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    errorDialog("Reset link sent to your email")
                    nav.navigate(R.id.hostLoginFragment)
                } else {
                    errorDialog("Unable to send reset mail")
                }
            }
    }


}