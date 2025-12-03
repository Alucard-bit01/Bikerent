package mx.edu.utng.eapd.bikerent.ui.screens.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import mx.edu.utng.eapd.bikerent.R
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val firebaseUser by authViewModel.firebaseUser.collectAsState()
    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authViewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google Sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    )

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
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
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            authViewModel.signUp(email, password)
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Sign up with Google")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { navController.navigate("login") }, enabled = !isLoading) {
                    Text("Already have an account? Login")
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}
