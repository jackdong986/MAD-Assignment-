package com.example.mad_assignment.main

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentHostEditProfileBinding
import com.example.mad_assignment.util.crop
import com.example.mad_assignment.util.cropToBlob
import com.example.mad_assignment.util.errorDialog
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.util.toBlob
import com.example.mad_assignment.viewModel.Host
import com.example.mad_assignment.viewModel.HostVM
import com.google.firebase.auth.FirebaseAuth


class HostEditProfileFragment : Fragment() {
    private lateinit var binding: FragmentHostEditProfileBinding
    private val nav by lazy { findNavController() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val hostVM: HostVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostEditProfileBinding.inflate(inflater, container, false)

        val user = auth.currentUser
        if (user != null) {
            val hostId = user.uid
            observeHost(hostId)

            binding.imgHostProfileDetailPicture.setOnClickListener {
                select()
            }

            binding.btnBackToHostProfile.setOnClickListener {
                nav.navigateUp()
            }

            binding.btnSaveHostProfile.setOnClickListener {
                updateProfile(hostId)
            }
        } else {
            errorDialog("User not logged in")
        }

        return binding.root
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imgHostProfileDetailPicture.setImageURI(it)
    }

    private fun select() {
        // Select file
        getContent.launch("image/*")
    }

    private fun updateProfile(hostId: String) {
        val h = Host(
            hostId,
            hostName = binding.edtProfileDetailHostName.text.toString(),
            hostEmail = binding.edtProfileDetailHostEmail.text.toString(),
            hostImage = binding.imgHostProfileDetailPicture.cropToBlob(400, 400)
        )

        hostVM.set(h)
        nav.navigateUp()
    }

    private fun observeHost(hostId: String) {
        hostVM.getHostLD().observe(viewLifecycleOwner, Observer { hostList ->
            val host = hostList.find { it.id == hostId }
            if (host != null) {
                binding.edtProfileDetailHostName.setText(host.hostName)
                binding.edtProfileDetailHostEmail.setText(host.hostEmail)
                if(host.hostImage.toBytes().isNotEmpty())  {
                    binding.imgHostProfileDetailPicture.setImageBlob(host.hostImage)
                } else {
                    //set to sample avatar image
                    // Load the image as a Bitmap
                    val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.user)

                    // Crop and resize the Bitmap
                    val croppedBitmap = originalBitmap.crop(400, 400)

                    // Convert the cropped Bitmap to a Blob
                    val croppedBlob = croppedBitmap.toBlob()

                    // Set the Blob as the image source for the ImageView
                    binding.imgHostProfileDetailPicture.setImageBlob(croppedBlob)
                }

            } else {
                errorDialog("Host not found")
            }
        })
    }



}