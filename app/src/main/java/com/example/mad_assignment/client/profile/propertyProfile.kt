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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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

        loadUserProfile()
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
                uploadImageToFirebase(imageUri)
                Toast.makeText(requireContext(), "Change Profile Picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //upload image to firebase
    private fun uploadImageToFirebase(imageUri: Uri) {
        val user = auth.currentUser
        if (user != null) {
            val storageRef = storage.reference
            val profilePicRef = storageRef.child("profile_pictures/${user.email}.jpg")

            profilePicRef.putFile(imageUri)
                .addOnSuccessListener {
                    profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                        val profilePicUrl = uri.toString()
                        saveProfilePictureUrlToFirestore(profilePicUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //save profile picture url to firestore
    private fun saveProfilePictureUrlToFirestore(profilePicUrl: String) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.email!!)
                .update("profilePicture", profilePicUrl)
                .addOnSuccessListener {
                    binding.profilePicture.setImageURI(Uri.parse(profilePicUrl))
                    Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update profile picture: ${e.message}", Toast.LENGTH_SHORT).show()
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
            saveUserNameToFirestore(newName)
            dialog.dismiss()
            Toast.makeText(requireContext(), "Change User Name", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    //save user name
    private fun saveUserNameToFirestore(newName: String) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.email!!)
                .update("userName", newName)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "User name updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update user name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //load user profile
    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.email!!)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("userName")
                        val profilePicture = document.getString("profilePicture")

                        binding.userName.text = userName
                        if (!profilePicture.isNullOrEmpty()) {
                            binding.profilePicture.setImageURI(Uri.parse(profilePicture))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to load user profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}




