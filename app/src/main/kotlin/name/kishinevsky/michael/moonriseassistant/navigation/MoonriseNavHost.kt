package name.kishinevsky.michael.moonriseassistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationContext
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationScreen
import name.kishinevsky.michael.moonriseassistant.screens.MainScreen
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenFirstTime
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenLoading
import name.kishinevsky.michael.moonriseassistant.screens.SettingsScreen
import name.kishinevsky.michael.moonriseassistant.model.AppSettings

@Composable
fun MoonriseNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
    ) {
        composable(Routes.MAIN) {
            // Step 10 will wire this to MainViewModel.
            // For now, show FirstTime so navigation can be verified.
            MainScreenFirstTime(
                onAddLocation = {
                    navController.navigate(Routes.addLocation(isFirstTime = true))
                },
            )
        }

        composable(Routes.SETTINGS) {
            // Step 10 will wire this to SettingsViewModel.
            SettingsScreen(
                settings = AppSettings(),
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.ADD_LOCATION,
            arguments = listOf(
                navArgument("isFirstTime") { type = NavType.BoolType },
            ),
        ) { backStackEntry ->
            val isFirstTime = backStackEntry.arguments?.getBoolean("isFirstTime") ?: false
            val context = if (isFirstTime) {
                AddLocationContext.FIRST_TIME
            } else {
                AddLocationContext.ADDITIONAL
            }
            // Step 10 will wire this to AddLocationViewModel.
            AddLocationScreen(
                context = context,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
