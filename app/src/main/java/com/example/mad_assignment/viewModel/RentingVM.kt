package com.example.mad_assignment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects

class RentingVM: ViewModel() {
    private val rentingLD = MutableLiveData<List<Renting>>(emptyList())
    private var listener: ListenerRegistration? = null

    init {
        listener = RENTING.addSnapshotListener { snap, _ ->
            rentingLD.value = snap?.toObjects()
//            updateResult()
        }
    }

    override fun onCleared() {
        listener?.remove()
    }

    fun init() = Unit

    fun getRentingLD() = rentingLD

    fun getAll() = rentingLD.value ?: emptyList()

    fun get(id: String) = getAll().find { it.id == id }

    fun getAll(hostId: String) = getAll().filter { it.hostId == hostId }

    fun set (r: Renting) {
        RENTING.document(r.id).set(r)
    }

//    private val resultLD = MutableLiveData<List<Renting>>()
//    private var paymentStatus = ""
}