package mx.edu.utng.eapd.bikerent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.utng.eapd.bikerent.model.Rent
import java.util.Calendar

class RentViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val rentsCollection = db.collection("rents")

    private val _rents = MutableStateFlow<List<Rent>>(emptyList())
    val rents = _rents.asStateFlow()

    fun fetchRentsForUser(userId: String) {
        viewModelScope.launch {
            rentsCollection.whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, _ ->
                    snapshot?.let {
                        _rents.value = it.toObjects(Rent::class.java)
                    }
                }
        }
    }

    fun addRent(rent: Rent) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.time = rent.date.toDate()
            calendar.add(Calendar.DAY_OF_YEAR, rent.days)
            val dueDate = Timestamp(calendar.time)

            val newRent = rent.copy(dueDate = dueDate)

            rentsCollection.add(newRent).await()
        }
    }
}
