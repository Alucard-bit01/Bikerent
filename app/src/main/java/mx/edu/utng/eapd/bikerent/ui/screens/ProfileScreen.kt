package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import mx.edu.utng.eapd.bikerent.navigation.Screen
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.RentViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    nav: NavHostController,
    rentViewModel: RentViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()
    val rents by rentViewModel.rents.collectAsState()

    LaunchedEffect(user) {
        user?.uid?.let { userId ->
            rentViewModel.fetchRentsForUser(userId)
        }
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        nav.navigate(Screen.BikeList.route) {
                            popUpTo(Screen.BikeList.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Inicio")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = user?.displayName ?: "", style = MaterialTheme.typography.headlineSmall)
            Text(text = user?.email ?: "", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { nav.navigate("edit_profile") }) {
                Text("Editar Perfil")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.logout()
                nav.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Text("Cerrar sesión")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Historial de Rentas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (rents.isEmpty()) {
                Text("Aún no tienes rentas registradas.")
            }

            rents.forEach { rent ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Cliente: ${rent.customerName}")
                        Text("Bicicleta ID: ${rent.bikeId}")
                        Text("Días: ${rent.days}")
                        Text("Fecha de Renta: ${dateFormat.format(rent.date.toDate())}")
                        Text("Fecha Límite: ${dateFormat.format(rent.dueDate.toDate())}")
                    }
                }
            }
        }
    }
}
