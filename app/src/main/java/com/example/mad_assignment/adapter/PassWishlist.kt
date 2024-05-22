package com.example.mad_assignment.adapter

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.viewModel.Property
import com.google.firebase.firestore.FirebaseFirestore

class PassWishlist : ViewModel() {
    private val _wishlist = MutableLiveData<MutableList<Property>>(mutableListOf())
    val wishlist: LiveData<MutableList<Property>> get() = _wishlist
    private val firestore = FirebaseFirestore.getInstance()
    private val wishlistCollection = firestore.collection("wishlist")

    init {
        loadWishlist()
    }

    private fun loadWishlist() {
        wishlistCollection.get()
            .addOnSuccessListener { result ->
                val properties = result.map { document ->
                    document.toObject(Property::class.java)
                }.toMutableList()
                _wishlist.value = properties
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun addToWishlist(property: Property, context: Context) {
        if (_wishlist.value?.contains(property) == true) {
            Toast.makeText(context, "This property is already in your wishlist", Toast.LENGTH_SHORT).show()
            return
        }

        wishlistCollection.document(property.id).set(property)
            .addOnSuccessListener {
                _wishlist.value?.add(property)
                _wishlist.value = _wishlist.value
                Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show()
            }
    }

    fun removeFromWishlist(property: Property, context: Context) {
        if (_wishlist.value?.contains(property) == true) {
            wishlistCollection.document(property.id).delete()
                .addOnSuccessListener {
                    _wishlist.value?.remove(property)
                    _wishlist.value = _wishlist.value
                    Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to remove from Wishlist", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "This property is already removed from your wishlist", Toast.LENGTH_SHORT).show()
        }
    }
}
