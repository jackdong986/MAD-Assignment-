package com.example.mad_assignment.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects

class PropertyVM: ViewModel() {

    private val propertyLD = MutableLiveData<List<Property>>(emptyList())
    private var listener: ListenerRegistration? = null

    init {
        listener = PROPERTIES.addSnapshotListener { snap, _ ->
            propertyLD.value = snap?.toObjects()
            updateResult()
        }
    }

    override fun onCleared() {
        listener?.remove()
    }

    fun init() = Unit

    fun getPropertyLD() = propertyLD

    fun getAll() = propertyLD.value ?: emptyList()

    fun get(id: String) = getAll().find { it.id == id }

    fun getAll(hostId: String) = getAll().filter { it.hostId == hostId }

    fun delete(id: String) {
        PROPERTIES.document(id).delete()
    }

    fun set(p: Property) {
        PROPERTIES.document(p.id).set(p)
    }

    fun addProperty(p: Property) {
        val newPropertyRef = PROPERTIES.document() // Auto-generated ID
        p.id = newPropertyRef.id // Set the ID in the property object
        newPropertyRef.set(p)
    }

    private val resultLD = MutableLiveData<List<Property>>()
    private var name = ""
    private var state = ""
    private var sortField = ""
    private var hostId = ""

    fun getResultLD() = resultLD

    fun search(name: String) {
        this.name = name
        updateResult()
    }

    fun filter(state: String) {
        this.state = state
        updateResult()
    }

    fun sort(field: String) {
        this.sortField = field
        updateResult()
    }

    fun setHostId(hostId: String) {
        this.hostId = hostId
        updateResult()
    }


    fun updateResult() {
        var list = getAll(hostId)

        list = list.filter {
            it.propertyName.contains(name, ignoreCase = true) && (state == "All" || it.propertyState == state)
        }

        list = when (sortField) {
            "Name" -> list.sortedBy { it.propertyName }
            "Price" -> list.sortedBy { it.propertyPrice }
            else -> list
        }


        resultLD.value = list
    }

    fun validate(p: Property): String {
        var errorMsg = ""

        if (p.propertyName == "") errorMsg += "- Name is required.\n"
        else if (p.propertyName.length < 3) errorMsg += "- Name is too short (at least 3 letters).\n"
        else if (p.propertyName.length > 100) errorMsg += "- Name is too long (at most 100 letters).\n"
        else if (getAll().any { it.propertyName == p.propertyName }) errorMsg += "- Name is duplicated.\n"

        if (p.propertyPrice == 0.0) errorMsg += "- Price is required.\n"
        else if (p.propertyPrice < 0) errorMsg += "- Price is invalid.\n"

        errorMsg += if (p.propertyImage.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        if(p.propertyAddress == "") errorMsg += "- Address is required.\n"
        if (p.propertyCity == "") errorMsg += "- City is required.\n"
        if (p.propertyState == "") errorMsg += "- State is required.\n"

        if (p.ttlBathrooms == 0) errorMsg += "- Total bathrooms is required.\n"
        else if (p.ttlBathrooms < 0) errorMsg += "- Total bathrooms is invalid.\n"

        if (p.ttlBedrooms == 0) errorMsg += "- Total bedrooms is required.\n"
        else if (p.ttlBedrooms < 0) errorMsg += "- Total bedrooms is invalid.\n"

        return errorMsg
    }


}