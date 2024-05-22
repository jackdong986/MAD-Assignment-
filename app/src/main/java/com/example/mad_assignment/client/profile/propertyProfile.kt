package com.example.mad_assignment.client.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.RedirectAuthenticationActivity
import com.example.mad_assignment.account.accManagement
import com.example.mad_assignment.databinding.FragmentPropertyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class propertyProfile : Fragment() {

    private lateinit var binding: FragmentPropertyProfileBinding
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

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
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.profilePicture.setOnClickListener {
            openGalleryForImage()
        }

        binding.userName.setOnClickListener {
            showChangeNameDialog()
        }

        binding.btnUserFeedback.setOnClickListener {
            findNavController().navigate(R.id.userFeedback)
        }

        binding.btnPaymentHistory.setOnClickListener {
            findNavController().navigate(R.id.paymentHistory)
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), RedirectAuthenticationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        loadUserProfile()
    }

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
                uploadImageToFirebase(imageUri)
                Toast.makeText(requireContext(), "Change Profile Picture", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImageToFirebase(imageUri: Uri) {
        val user = auth.currentUser
        if (user != null) {
            val storageRef = storage.reference
            val profilePicRef = storageRef.child("profile_pictures/${user.email}.jpg")

            // Get the Bitmap from the Uri
            try {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                // Convert the Bitmap to Base64 string
                val base64String = encodeImageToBase64(bitmap)

                Log.d("UploadImage", "Base64 String Length: ${base64String.length}")

                saveProfilePictureUrlToFirestore(base64String)
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Failed to decode image: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("UploadImage", "IOException: ${e.message}")
            }
        }
    }


    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveProfilePictureUrlToFirestore(profilePicBase64: String) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("customer").document(user.email!!)
                .update("cusImage", profilePicBase64)
                .addOnSuccessListener {
                    // Optionally, you can update the UI with the new profile picture here
                    Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun showChangeNameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Change User Name")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newName = input.text.toString()
            binding.userName.text = newName
            saveUserNameToFirestore(newName)
            dialog.dismiss()
            Toast.makeText(requireContext(), "Change User Name", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun saveUserNameToFirestore(newName: String) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("customer").document(user.email!!)
                .update("customerName", newName)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "User name updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update user name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("customer").document(user.email!!)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("customerName")
                        val profilePicture = document.getString("cusImage")

                        updateUserProfile(userName, profilePicture)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to load user profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateUserProfile(userName: String?, profilePicture: String?) {
        if (userName != null) {
            binding.userName.text = userName
        }

        if (!profilePicture.isNullOrEmpty()) {
            // Decode Base64 string to bytes
            val decodedBytes = Base64.decode(profilePicture, Base64.DEFAULT)

            // Create Bitmap from decoded bytes
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Load Bitmap into ImageView using Glide
            Glide.with(this).load(bitmap).into(binding.profilePicture)
        }
    }

}
