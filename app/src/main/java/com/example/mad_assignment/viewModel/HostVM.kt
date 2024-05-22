package com.example.mad_assignment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects

class HostVM: ViewModel() {

    private val hostLD = MutableLiveData<List<Host>>(emptyList())
    private var listener: ListenerRegistration? = null

    init {
        listener = HOSTS.addSnapshotListener { snap, _ ->
            hostLD.value = snap?.toObjects()
        }
    }

    override fun onCleared() {
        listener?.remove()
    }

    fun init() = Unit

    fun getHostLD() = hostLD

    fun getAll() = hostLD.value ?: emptyList()

    fun get(id: String) = getAll().find { it.id == id }

    fun delete(id: String) {
        HOSTS.document(id).delete()
    }

    fun set(h: Host) {
        HOSTS.document(h.id).set(h)
    }
}