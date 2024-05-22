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

    fun validate(h: Host): String {

        var e = ""

        if (h.hostName == "") e += "- Name is required.\n"
        else if (h.hostName.length < 3) e +=  "- Name is too short (at least 3 letters).\n"

        if(h.hostEmail == ""){
            e += "- Email is required.\n"
        }else if(!h.hostEmail.contains("@") || !h.hostEmail.contains(".")){
            e += "- Email format is invalid.\n"
        }

        return e
    }
}