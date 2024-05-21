package com.example.mad_assignment.client.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.account.accManagement
import com.example.mad_assignment.databinding.FragmentPropertyProfileBinding
import com.google.firebase.auth.FirebaseAuth

class propertyProfile : Fragment() {

    private lateinit var binding: FragmentPropertyProfileBinding
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()


        binding.profilePicture.setOnClickListener {
            openGalleryForImage()
        }

        binding.userName.setOnClickListener {
            showChangeNameDialog()
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

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), accManagement::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    //for changing profile picture
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                binding.profilePicture.setImageURI(imageUri)
                // You can also upload the image to your server or Firebase here
                Toast.makeText(requireContext(), "Change Profile Picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //change user name
    private fun showChangeNameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Change User Name")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newName = input.text.toString()
            binding.userName.text = newName
            // You can also update the name in your database here
            dialog.dismiss()
            Toast.makeText(requireContext(), "Change User Name", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }




}