package mx.edu.utng.eapd.bikerent.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Rent(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val bikeId: String = "", // Changed to String to match Bike's DocumentId
    val customerName: String = "",
    val days: Int = 0,
    val date: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp.now()
)
