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
            Log.d("PropertyVM", "Data fetched: ${propertyLD.value}") // Add logging here
//            updateResult()
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

    private val resultLD = MutableLiveData<List<Property>>()
    private var name = ""

//    fun getResultLD() = resultLD
//
//    fun search(name: String) {
//        this.name = name
////        updateResult()
//    }
//
//    fun updateResult() {
//        var list = getAll()
//
//        list = list.filter {
//            it.propertyName.contains(name, ignoreCase = true)
//        }
//
//        resultLD.value = list
//    }


}