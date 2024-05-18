package com.example.mad_assignment.viewModel

// PropertyViewModel.kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.main.Property

class PropertyViewModel : ViewModel() {
    private val _wishlist = MutableLiveData<MutableList<Property>>(mutableListOf())
    val wishlist: LiveData<MutableList<Property>> get() = _wishlist

    fun addToWishlist(property: Property) {
        _wishlist.value?.add(property)
        _wishlist.value = _wishlist.value
    }
}
