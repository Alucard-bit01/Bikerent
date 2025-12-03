package mx.edu.utng.eapd.bikerent.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = ""
)
