package mx.edu.utng.eapd.bikerent.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val firebaseUser by authViewModel.firebaseUser.collectAsState()
    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate("home") { // O la ruta principal de tu app
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("signup") }, enabled = !isLoading) {
                Text("Don't have an account? Sign up")
            }
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
