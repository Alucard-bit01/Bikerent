package mx.edu.utng.eapd.bikerent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.utng.eapd.bikerent.model.Bike

class BikeViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val bikesCollection = db.collection("bikes")

    private val _bikes = MutableStateFlow<List<Bike>>(emptyList())
    val bikes = _bikes.asStateFlow()

    private val _bike = MutableStateFlow<Bike?>(null)
    val bike = _bike.asStateFlow()

    init {
        fetchAllBikes()
    }

    private fun fetchAllBikes() {
        viewModelScope.launch {
            bikesCollection.addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    _bikes.value = it.toObjects(Bike::class.java)
                }
            }
        }
    }

    fun fetchBikeById(bikeId: String) {
        viewModelScope.launch {
            val document = bikesCollection.document(bikeId).get().await()
            _bike.value = document.toObject<Bike>()
        }
    }

    fun addBike(bike: Bike, ownerName: String, ownerPhotoUrl: String) {
        viewModelScope.launch {
            val newBike = bike.copy(ownerName = ownerName, ownerPhotoUrl = ownerPhotoUrl)
            bikesCollection.add(newBike).await()
        }
    }

    fun updateBike(bike: Bike) {
        viewModelScope.launch {
            bikesCollection.document(bike.id).set(bike).await()
        }
    }

    fun deleteBike(bikeId: String) {
        viewModelScope.launch {
            bikesCollection.document(bikeId).delete().await()
        }
    }
}
