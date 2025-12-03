package mx.edu.utng.eapd.bikerent.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mx.edu.utng.eapd.bikerent.model.Rent
import mx.edu.utng.eapd.bikerent.navigation.Screen
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.RentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentScreen(
    navController: NavHostController,
    bikeId: String,
    rentViewModel: RentViewModel = viewModel(),
    authViewModel: AuthViewModel,
    bikeViewModel: BikeViewModel = viewModel()
) {

    LaunchedEffect(bikeId) {
        bikeViewModel.fetchBikeById(bikeId)
    }

    val bike by bikeViewModel.bike.collectAsState()
    val currentUser by authViewModel.user.collectAsState()

    var name by remember(currentUser) { mutableStateOf(currentUser?.displayName ?: "") }
    var days by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Renta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (bike == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rentando: ${bike!!.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = days,
                    onValueChange = { days = it },
                    label = { Text("Días de renta") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val d = days.toIntOrNull()
                        val userId = currentUser?.uid

                        if (userId == null) {
                            Toast.makeText(
                                context,
                                "Error de usuario. Inicia sesión de nuevo.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }

                        if (d == null || d <= 0) {
                            Toast.makeText(context, "Ingresa un número válido de días", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        val rent = Rent(
                            userId = userId,
                            bikeId = bikeId,
                            customerName = name,
                            days = d
                        )
                        rentViewModel.addRent(rent)

                        Toast.makeText(context, "Renta registrada correctamente", Toast.LENGTH_LONG).show()

                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotBlank() && days.isNotBlank() && currentUser != null
                ) {
                    Text("Confirmar Renta")
                }
            }
        }
    }
}
