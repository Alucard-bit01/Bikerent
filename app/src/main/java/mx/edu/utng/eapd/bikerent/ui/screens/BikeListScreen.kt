package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import mx.edu.utng.eapd.bikerent.model.Bike
import mx.edu.utng.eapd.bikerent.navigation.Screen
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeListScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    bikeViewModel: BikeViewModel = viewModel()
) {
    val bikes by bikeViewModel.bikes.collectAsState()
    val currentUser by authViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Listado de Bicicletas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.BikeAdd.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (bikes.isEmpty()) {
                item {
                    Text(
                        "No hay bicicletas registradas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                items(bikes) { bike ->
                    BikeCard(
                        bike = bike,
                        isOwner = bike.userId == currentUser?.uid,
                        onRentClick = {
                            navController.navigate(Screen.Rent.createRoute(bike.id))
                        },
                        onEditClick = {
                            navController.navigate(Screen.BikeEdit.createRoute(bike.id))
                        },
                        onDeleteClick = { bikeViewModel.deleteBike(bike.id) },
                        onCardClick = {
                            navController.navigate(Screen.BikeDetail.createRoute(bike.id))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun BikeCard(
    bike: Bike,
    isOwner: Boolean,
    onRentClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bike.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${bike.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(bike.ownerPhotoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil del propietario",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = bike.ownerName, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = bike.name, style = MaterialTheme.typography.titleMedium)
                Text(text = bike.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${bike.precio} por día",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onRentClick) {
                        Text("Rentar")
                    }
                    if (isOwner) {
                        Row {
                            IconButton(onClick = onEditClick) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = onDeleteClick) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
