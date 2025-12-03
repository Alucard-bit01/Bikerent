package mx.edu.utng.eapd.bikerent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.edu.utng.eapd.bikerent.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bienvenido a BikeRent") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centra el contenido
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Logo App",
                modifier = Modifier.size(150.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Encuentra la bicicleta perfecta para tu próxima aventura",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(Screen.BikeList.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Bicicletas Disponibles")
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.Profile.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mi Perfil")
            }
        }
    }
}