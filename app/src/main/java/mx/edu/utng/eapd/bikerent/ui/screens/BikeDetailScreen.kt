package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import mx.edu.utng.eapd.bikerent.navigation.Screen
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeDetailScreen(
    navController: NavHostController,
    bikeId: String,
    authViewModel: AuthViewModel,
    bikeViewModel: BikeViewModel
) {
    LaunchedEffect(bikeId) {
        bikeViewModel.fetchBikeById(bikeId)
    }

    val bike by bikeViewModel.bike.collectAsState()
    val currentUser by authViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bike?.name ?: "Detalle de la Bicicleta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
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
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bike!!.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de ${bike!!.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))
                Text("Descripción: ${bike!!.descripcion}")
                Text("Precio por día: $${bike!!.precio}")

                Spacer(Modifier.height(30.dp))

                val isOwner = currentUser?.uid == bike!!.userId

                if (isOwner) {
                    Button(
                        onClick = { navController.navigate(Screen.BikeEdit.createRoute(bike!!.id)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Editar Bicicleta")
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            bikeViewModel.deleteBike(bike!!.id)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate(Screen.Rent.createRoute(bike!!.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Rentar bicicleta")
                }
            }
        }
    }
}
