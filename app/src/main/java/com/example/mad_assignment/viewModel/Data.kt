package com.example.mad_assignment.viewModel

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat.getString
import com.example.mad_assignment.R
import com.example.mad_assignment.util.toBlob
import com.google.firebase.Firebase
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

data class Host(
    @DocumentId
    var id: String = "",
    var hostName: String = "",
    var hostEmail: String = "",
    var hostImage: Blob = Blob.fromBytes(ByteArray(0))
)

data class Property(
    @DocumentId
    var id: String = "",
    var propertyName: String = "",
    var propertyPrice: Double = 0.0, //Per month
    var propertyImage: Blob = Blob.fromBytes(ByteArray(0)),
    var propertyAddress: String = "",
    var propertyCity: String = "",
    var propertyState: String = "",
    var ttlBathrooms: Int = 0,
    var ttlBedrooms: Int = 0,
    var propertyDescription: String = "",
    var hostId: String = ""
) {
    @get:Exclude
    var host: Host = Host()
}

data class Renting(
    @DocumentId
    var id: String = "",
    var propertyAmount : Double = 0.0,
    var rentingStartDate: Date = Date(),
    var rentingEndDate: Date = Date(),
    var totalMonth: Int = 0,
    var totalAmount: Double = 0.0,
    var propertyId: String = "",
    var hostId: String = "",
    var custId: String = "",
    var paymentStatus: String = "" ,//Success, Failed, Pending
    val createdAt: Date = Date()
)

data class Feedback(
    @DocumentId
    val name: String = "",
    val email: String = "",
    val rating: Float = 0f,
    val feedback: String = ""
)


val HOSTS = Firebase.firestore.collection("hosts")
val PROPERTIES = Firebase.firestore.collection("properties")
val RENTING = Firebase.firestore.collection("renting")

fun RESTORE(ctx: Context){
    fun getBlob(res: Int) = BitmapFactory.decodeResource(ctx.resources, res).toBlob()

    val statesArray: Array<String> = ctx.resources.getStringArray(R.array.states)
    val properties = listOf(
        Property("P001", "House 1", 19.00, getBlob(R.raw.house1), "123 Street", "Kuala Lumpur", statesArray[1], 2, 3, "This is a house", "H001"),
        Property("P002", "House 2", 109.00, getBlob(R.raw.house2), "123 Street", "Kuala Lumpur", statesArray[2], 2, 3, "This is a house", "H001"),
        Property("P003", "House 3", 119.00, getBlob(R.raw.house3), "123 Street", "Kuala Lumpur", statesArray[1], 2, 3, "This is a house", "H001"),
        Property("P004", "House 4", 29.00, getBlob(R.raw.house4), "123 Street", "Kuala Lumpur", statesArray[3], 2, 3, "This is a house", "H001"),
        Property("P005", "House 5", 9.00, getBlob(R.raw.house5), "123 Street", "Kuala Lumpur", statesArray[1], 2, 3, "This is a house", "H001"),

    )

    properties.forEach(){
        PROPERTIES.document(it.id).set(it)
    }


    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val renting = listOf(
        Renting(
            "R001", 19.00,
            dateFormat.parse("2023-05-01 10:00:00")!!,
            dateFormat.parse("2024-05-01 10:00:00")!!,
            1, 19.00 * 1, "P001", "z19Hjn844vTIT5HVJwUmo47Guhz2", "C001", "Success",
            Date()
        ),
        Renting(
            "R002", 109.00,
            dateFormat.parse("2023-06-01 11:00:00")!!,
            dateFormat.parse("2024-06-01 11:00:00")!!,
            2, 109.00 * 2, "P002", "z19Hjn844vTIT5HVJwUmo47Guhz2", "C001", "Success",
            Date()
        ),
        Renting(
            "R003", 119.00,
            dateFormat.parse("2023-07-01 12:00:00")!!,
            dateFormat.parse("2024-07-01 12:00:00")!!,
            3, 119.00 * 3, "P003", "H001", "C001", "Success",
            Date()
        ),
        Renting(
            "R004", 29.00,
            dateFormat.parse("2023-08-01 13:00:00")!!,
            dateFormat.parse("2024-08-01 13:00:00")!!,
            4, 29.00 * 4, "P004", "H001", "C001", "Success",
            Date()
        ),
        Renting(
            "R005", 9.00,
            dateFormat.parse("2023-09-01 14:00:00")!!,
            dateFormat.parse("2024-09-01 14:00:00")!!,
            5, 9.00 * 4, "P005", "H001", "C001", "Success",
            Date()
        )
    )

    renting.forEach(){
        RENTING.document(it.id).set(it)
    }

}


