import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.viewModel.Property
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class PassWishlist : ViewModel() {
    private val _wishlist = MutableLiveData<MutableList<Property>>(mutableListOf())
    val wishlist: LiveData<MutableList<Property>> get() = _wishlist
    private val firestore = FirebaseFirestore.getInstance()
    private val wishlistCollection = firestore.collection("wishlist")
    private var wishlistListener: ListenerRegistration? = null

    init {
        attachWishlistListener()
    }

    private fun attachWishlistListener() {
        wishlistListener = wishlistCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle failure
                return@addSnapshotListener
            }

            snapshot?.let { snap ->
                val properties = snap.documents.map { document ->
                    document.toObject(Property::class.java)
                }.filterNotNull().toMutableList()

                _wishlist.value = properties
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        detachWishlistListener()
    }

    private fun detachWishlistListener() {
        wishlistListener?.remove()
    }

    fun addToWishlist(property: Property, context: Context) {
        if (_wishlist.value?.contains(property) == true) {
            Toast.makeText(context, "This property is already in your wishlist", Toast.LENGTH_SHORT).show()
            return
        } else {
            wishlistCollection.document(property.id).set(property)
                .addOnSuccessListener {
                    // No need to manually update _wishlist here
                    Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun removeFromWishlist(property: Property, context: Context) {
        if (_wishlist.value?.contains(property) == true) {
            wishlistCollection.document(property.id).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to remove from Wishlist", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Property is not in wishlist, so you can't remove it
            Toast.makeText(context, "This property is not in your wishlist", Toast.LENGTH_SHORT).show()
        }
    }


}
