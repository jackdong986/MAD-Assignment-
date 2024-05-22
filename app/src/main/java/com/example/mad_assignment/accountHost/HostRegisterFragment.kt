package com.example.mad_assignment.accountHost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostRegisterBinding
import com.example.mad_assignment.util.errorDialog
import com.example.mad_assignment.util.infoDialog
import com.example.mad_assignment.viewModel.HOSTS
import com.example.mad_assignment.viewModel.Host
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Blob


class HostRegisterFragment : Fragment() {
    private lateinit var binding: FragmentHostRegisterBinding
    private val nav by lazy { findNavController() }
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnHostRegisterRegister.setOnClickListener {
            val email = binding.edtHostRegisterEmail.text.toString().trim()
            val password = binding.edtHostRegisterPwd.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                errorDialog("Please enter email and password")
            }
        }

        binding.btnHostRegisterLogin.setOnClickListener {
            nav.navigate(R.id.hostLoginFragment)
        }

        binding.btnHostRegisterForgotPwd.setOnClickListener {
            nav.navigate(R.id.hostForgotPasswordFragment)
        }


    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.let {
                        createUserInFirestore(it)
                    }
                    infoDialog("Registration successful")
                    nav.navigate(R.id.hostLoginFragment)
                } else {
                    errorDialog("Registration failed")
                }
            }
    }

    private fun createUserInFirestore(user: FirebaseUser) {
        val host = Host(
            id = user.uid,
            hostName = binding.edtHostRegisterName.text.toString().trim(),
            hostEmail = user.email ?: "",
            hostImage = Blob.fromBytes(ByteArray(0)) // Default empty blob, you might want to handle image separately
        )

        HOSTS.document(user.uid).set(host)

    }
}