package mx.edu.utng.eapd.bikerent.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.edu.utng.eapd.bikerent.ui.screens.*
import mx.edu.utng.eapd.bikerent.ui.screens.auth.LoginScreen
import mx.edu.utng.eapd.bikerent.ui.screens.auth.SignUpScreen
import mx.edu.utng.eapd.bikerent.viewmodel.AuthViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.BikeViewModel
import mx.edu.utng.eapd.bikerent.viewmodel.RentViewModel

@Composable
fun BikeRentNavHost() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("signup") {
            SignUpScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Rent.route,
            arguments = listOf(navArgument("bikeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bikeId = backStackEntry.arguments?.getString("bikeId") ?: ""
            RentScreen(
                navController = navController, 
                bikeId = bikeId, 
                authViewModel = authViewModel,
                rentViewModel = viewModel()
            )
        }

        composable(Screen.BikeList.route) {
            BikeListScreen(
                navController = navController, 
                authViewModel = authViewModel, 
                bikeViewModel = viewModel()
            )
        }

        composable(Screen.BikeAdd.route) {
            BikeAddScreen(
                navController = navController, 
                authViewModel = authViewModel, 
                bikeViewModel = viewModel()
            )
        }

        composable(
            route = Screen.BikeDetail.route,
            arguments = listOf(navArgument("bikeId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("bikeId") ?: ""
            BikeDetailScreen(
                navController = navController, 
                bikeId = id, 
                authViewModel = authViewModel, 
                bikeViewModel = viewModel()
            )
        }

        composable(
            Screen.BikeEdit.route,
            arguments = listOf(navArgument("bikeId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("bikeId") ?: ""
            BikeEditScreen(
                navController = navController, 
                bikeId = id, 
                authViewModel = authViewModel, 
                bikeViewModel = viewModel()
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                nav = navController, 
                authViewModel = authViewModel, 
                rentViewModel = viewModel()
            )
        }

        composable("edit_profile") {
            EditProfileScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}
