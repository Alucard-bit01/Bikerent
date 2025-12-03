package mx.edu.utng.eapd.bikerent.model

import com.google.firebase.firestore.DocumentId

data class Bike(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val ownerName: String = "",
    val ownerPhotoUrl: String = "",
    val name: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imageUrl: String = ""
)
