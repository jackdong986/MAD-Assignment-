package com.example.mad_assignment.adapter

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.viewModel.Property


class PassWishlist : ViewModel() {
    private val _wishlist = MutableLiveData<MutableList<Property>>(mutableListOf())
    val wishlist: LiveData<MutableList<Property>> get() = _wishlist


    fun addToWishlist(propertyDatatype: Property, context: Context) {
        if (_wishlist.value?.contains(propertyDatatype) == true)
        {
            Toast.makeText(context, "This property is already in your wishlist", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            _wishlist.value?.add(propertyDatatype)
            _wishlist.value = _wishlist.value
            Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeFromWishlist(propertyDatatype: Property, context: Context) {
        if (_wishlist.value?.contains(propertyDatatype) == true)
        {
            _wishlist.value?.remove(propertyDatatype)
            _wishlist.value = _wishlist.value
            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "This property is already removed in your wishlist", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
