package com.example.mad_assignment.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyHostBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_assignment.RedirectAuthenticationActivity
import com.example.mad_assignment.accountHost.AuthHostActivity
import com.example.mad_assignment.adapter.PropertyHostAdapter
import com.example.mad_assignment.databinding.FragmentHostProfileBinding
import com.example.mad_assignment.util.crop
import com.example.mad_assignment.util.errorDialog
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.util.toBlob
import com.example.mad_assignment.viewModel.HostVM
import com.example.mad_assignment.viewModel.PropertyVM
import com.google.firebase.auth.FirebaseAuth


class HostProfileFragment : Fragment() {

    private lateinit var binding: FragmentHostProfileBinding
    private val nav by lazy { findNavController() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val hostVM: HostVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostProfileBinding.inflate(inflater, container, false)

        //get host id from firebase auth
        //from host id get host email, host name, host profile image
//        binding.imgHostProfile.setImageBlob()
        val user = auth.currentUser
        if (user != null) {
            val hostId = user.uid
            observeHost(hostId)

            binding.btnEditHostProfile.setOnClickListener {
                nav.navigate(R.id.hostEditProfileFragment)
            }
        } else {
            errorDialog("User not logged in")
        }



        binding.btnHostLogout.setOnClickListener {
            auth.signOut()
            // Create an Intent to start the AuthHostActivity
            val intent = Intent(requireContext(), RedirectAuthenticationActivity::class.java)

            // Clear the back stack and start the AuthHostActivity as a new task
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Start the AuthHostActivity
            startActivity(intent)

            // Finish the current activity (the fragment's hosting activity)
            requireActivity().finish()

        }

        return binding.root
    }

    private fun observeHost(hostId: String) {
        hostVM.getHostLD().observe(viewLifecycleOwner, Observer { hostList ->
            val host = hostList.find { it.id == hostId }
            if (host != null) {
                binding.txtHostName.text = host.hostName
                binding.txtHostEmail.text = host.hostEmail
                if(host.hostImage.toBytes().isNotEmpty())  {
                    binding.imgHostProfile.setImageBlob(host.hostImage)
                } else {
                    //set to sample avatar image
                    // Load the image as a Bitmap
                    val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.user)

                    // Crop and resize the Bitmap
                    val croppedBitmap = originalBitmap.crop(200, 200)

                    // Convert the cropped Bitmap to a Blob
                    val croppedBlob = croppedBitmap.toBlob()

                    // Set the Blob as the image source for the ImageView
                    binding.imgHostProfile.setImageBlob(croppedBlob)
                }

            } else {
                errorDialog("Host not found")
            }
        })
    }


}
