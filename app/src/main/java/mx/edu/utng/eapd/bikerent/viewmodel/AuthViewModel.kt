package mx.edu.utng.eapd.bikerent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.utng.eapd.bikerent.model.User

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUser = _firebaseUser.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _firebaseUser.value = user
            if (user != null) {
                listenForUserDocument(user.uid)
            } else {
                _user.value = null
            }
        }
    }

    private fun listenForUserDocument(uid: String) {
        usersCollection.document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                _error.value = "Failed to listen for user document: ${e.message}"
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                _user.value = snapshot.toObject<User>()
            } else {
                auth.currentUser?.let { createFirestoreUserDocument(it) }
            }
        }
    }

    private fun createFirestoreUserDocument(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            val newUser = User(
                uid = firebaseUser.uid,
                displayName = firebaseUser.displayName ?: firebaseUser.email?.substringBefore('@') ?: "Usuario",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
            try {
                usersCollection.document(firebaseUser.uid).set(newUser).await()
            } catch (e: Exception) {
                _error.value = "Failed to create user document: ${e.message}"
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                authResult.user?.let { createFirestoreUserDocument(it) }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                usersCollection.document(user.uid).set(user).await()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to update user: ${e.message}"
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}
