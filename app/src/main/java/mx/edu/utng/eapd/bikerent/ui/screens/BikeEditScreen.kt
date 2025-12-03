package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeEditScreen(
    navController: NavHostController,
    bikeId: String,
    authViewModel: AuthViewModel,
    bikeViewModel: BikeViewModel
) {

    LaunchedEffect(bikeId) {
        bikeViewModel.fetchBikeById(bikeId)
    }

    val bike by bikeViewModel.bike.collectAsState()

    var name by remember(bike) { mutableStateOf(bike?.name ?: "") }
    var description by remember(bike) { mutableStateOf(bike?.descripcion ?: "") }
    var price by remember(bike) { mutableStateOf(bike?.precio?.toString() ?: "") }
    var imageUrl by remember(bike) { mutableStateOf(bike?.imageUrl ?: "") }

    val currentUser by authViewModel.user.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Bicicleta") }) }
    ) { padding ->

        if (bike == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val isOwner = currentUser?.uid == bike?.userId

            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    enabled = isOwner,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    enabled = isOwner,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = isOwner,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de la imagen") },
                    enabled = isOwner,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        bike?.let {
                            val updatedBike = it.copy(
                                name = name,
                                descripcion = description,
                                precio = price.toDoubleOrNull() ?: 0.0,
                                imageUrl = imageUrl
                            )
                            bikeViewModel.updateBike(updatedBike)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isOwner && name.isNotBlank() && description.isNotBlank() && price.isNotBlank()
                ) {
                    Text("Actualizar")
                }

                if (!isOwner) {
                    Spacer(Modifier.height(16.dp))
                    Text("No tienes permiso para editar esta bicicleta.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
