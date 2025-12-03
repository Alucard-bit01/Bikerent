package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()

    var displayName by remember(user) { mutableStateOf(user?.displayName ?: "") }
    var photoUrl by remember(user) { mutableStateOf(user?.photoUrl ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("URL de la foto de perfil") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        user?.let {
                            val updatedUser = it.copy(displayName = displayName, photoUrl = photoUrl)
                            authViewModel.updateUser(updatedUser)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}
