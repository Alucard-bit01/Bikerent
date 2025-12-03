package mx.edu.utng.eapd.bikerent.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.edu.utng.eapd.bikerent.model.Bike
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeAddScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    bikeViewModel: BikeViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val user by authViewModel.user.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Bicicleta") }) }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio por día") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull() ?: 0.0
                    val currentUser = user

                    if (currentUser != null) {
                        val bike = Bike(
                            userId = currentUser.uid,
                            name = name,
                            descripcion = description,
                            precio = priceDouble,
                            imageUrl = imageUrl
                        )
                        bikeViewModel.addBike(bike, currentUser.displayName, currentUser.photoUrl)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Error: Debes iniciar sesión para agregar una bicicleta", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && description.isNotBlank() && price.isNotBlank() && user != null
            ) {
                Text("Guardar")
            }
        }
    }
}
