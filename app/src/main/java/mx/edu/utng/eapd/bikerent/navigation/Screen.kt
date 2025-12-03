package mx.edu.utng.eapd.bikerent.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object BikeList : Screen("bike_list")

    object BikeDetail : Screen("bike/detail/{bikeId}") {
        fun createRoute(bikeId: String) = "bike/detail/$bikeId"
    }

    object BikeAdd : Screen("bike_add")

    object BikeEdit : Screen("bike/edit/{bikeId}") {
        fun createRoute(bikeId: String) = "bike/edit/$bikeId"
    }

    object Rent : Screen("rent/{bikeId}") {
        fun createRoute(bikeId: String) = "rent/$bikeId"
    }

    object Profile : Screen("profile")
}
