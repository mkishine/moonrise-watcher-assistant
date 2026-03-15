package name.kishinevsky.michael.moonriseassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import name.kishinevsky.michael.moonriseassistant.MoonriseApplication
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationContext
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationScreen
import name.kishinevsky.michael.moonriseassistant.screens.LocationInputMode
import name.kishinevsky.michael.moonriseassistant.screens.MainScreen
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenEmpty
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenError
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenFirstTime
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenLoading
import name.kishinevsky.michael.moonriseassistant.screens.SettingsScreen
import name.kishinevsky.michael.moonriseassistant.viewmodel.AddLocationUiState
import name.kishinevsky.michael.moonriseassistant.viewmodel.AddLocationViewModel
import name.kishinevsky.michael.moonriseassistant.viewmodel.MainUiState
import name.kishinevsky.michael.moonriseassistant.viewmodel.MainViewModel
import name.kishinevsky.michael.moonriseassistant.viewmodel.SettingsUiState
import name.kishinevsky.michael.moonriseassistant.viewmodel.SettingsViewModel

@Composable
fun MoonriseNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val app = LocalContext.current.applicationContext as MoonriseApplication
    val container = app.container

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
    ) {
        composable(Routes.MAIN) { backStackEntry ->
            val vm: MainViewModel = viewModel(
                factory = MainViewModel.Factory(
                    container.locationRepository,
                    container.forecastRepository,
                    container.settingsRepository,
                ),
            )

            // Refresh when returning from AddLocation
            val refreshSignal = backStackEntry.savedStateHandle
                .getStateFlow("refresh", false)
                .collectAsState()
            LaunchedEffect(refreshSignal.value) {
                if (refreshSignal.value) {
                    backStackEntry.savedStateHandle["refresh"] = false
                    vm.refresh()
                }
            }

            val uiState by vm.uiState.collectAsState()

            when (val state = uiState) {
                is MainUiState.Loading -> {
                    MainScreenLoading(
                        locationName = "Loading...",
                        onMenuClick = { navController.navigate(Routes.SETTINGS) },
                    )
                }
                is MainUiState.Content -> {
                    val today = state.today
                    if (today == null) {
                        MainScreenLoading(
                            locationName = state.locationName,
                            onMenuClick = { navController.navigate(Routes.SETTINGS) },
                        )
                    } else if (state.forecast.isEmpty()) {
                        MainScreenEmpty(
                            locationName = state.locationName,
                            today = today,
                            nextFullMoonDate = "",
                            onMenuClick = { navController.navigate(Routes.SETTINGS) },
                        )
                    } else {
                        MainScreen(
                            locationName = state.locationName,
                            today = today,
                            upcomingDays = state.forecast,
                            maxMoonriseTime = state.maxMoonriseTime,
                            isRefreshing = state.isRefreshing,
                            onRefresh = { vm.refresh() },
                            lastUpdated = state.lastUpdated,
                            onMenuClick = { navController.navigate(Routes.SETTINGS) },
                        )
                    }
                }
                is MainUiState.Error -> {
                    MainScreenError(
                        locationName = state.locationName,
                        errorMessage = state.message,
                        onRetry = { vm.refresh() },
                        onMenuClick = { navController.navigate(Routes.SETTINGS) },
                    )
                }
                is MainUiState.FirstTime -> {
                    MainScreenFirstTime(
                        onAddLocation = {
                            navController.navigate(Routes.addLocation(isFirstTime = true))
                        },
                    )
                }
            }
        }

        composable(Routes.SETTINGS) {
            val vm: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(container.settingsRepository),
            )
            val uiState by vm.uiState.collectAsState()

            when (val state = uiState) {
                is SettingsUiState.Loading -> {
                    // Brief loading — SettingsScreen with defaults while loading
                    SettingsScreen(
                        settings = name.kishinevsky.michael.moonriseassistant.model.AppSettings(),
                        onBack = { navController.popBackStack() },
                    )
                }
                is SettingsUiState.Content -> {
                    SettingsScreen(
                        settings = state.settings,
                        onBack = { navController.popBackStack() },
                        onDaysBeforeChange = { vm.updateDaysBefore(it) },
                        onDaysAfterChange = { vm.updateDaysAfter(it) },
                        onForecastPeriodChange = { vm.updateForecastPeriod(it) },
                        onToleranceChange = { vm.updateTolerance(it) },
                        onUnitToggle = { vm.updateUseMetric(it) },
                    )
                }
            }
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

            val vm: AddLocationViewModel = viewModel(
                factory = AddLocationViewModel.Factory(
                    container.locationRepository,
                    container.geocodingService,
                ),
            )
            val uiState by vm.uiState.collectAsState()

            var inputMode by remember { mutableStateOf(LocationInputMode.CITY) }
            var cityValue by remember { mutableStateOf("") }
            var latitudeValue by remember { mutableStateOf("") }
            var longitudeValue by remember { mutableStateOf("") }
            var nameValue by remember { mutableStateOf("") }

            // Navigate back on success
            LaunchedEffect(uiState) {
                if (uiState is AddLocationUiState.Success) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            }

            val errorMessage = (uiState as? AddLocationUiState.Error)?.message
            val isLoading = uiState is AddLocationUiState.Saving

            AddLocationScreen(
                context = context,
                inputMode = inputMode,
                cityValue = cityValue,
                latitudeValue = latitudeValue,
                longitudeValue = longitudeValue,
                nameValue = nameValue,
                errorMessage = errorMessage,
                isLoading = isLoading,
                onInputModeChange = {
                    inputMode = it
                    vm.resetState()
                },
                onCityChange = {
                    cityValue = it
                    vm.resetState()
                },
                onLatitudeChange = {
                    latitudeValue = it
                    vm.resetState()
                },
                onLongitudeChange = {
                    longitudeValue = it
                    vm.resetState()
                },
                onNameChange = {
                    nameValue = it
                    vm.resetState()
                },
                onSave = {
                    when (inputMode) {
                        LocationInputMode.COORDINATES -> {
                            val lat = latitudeValue.toDoubleOrNull() ?: Double.NaN
                            val lng = longitudeValue.toDoubleOrNull() ?: Double.NaN
                            vm.saveLocation(
                                name = nameValue.ifBlank { "$latitudeValue, $longitudeValue" },
                                cityState = null,
                                latitude = lat,
                                longitude = lng,
                            )
                        }
                        LocationInputMode.CITY -> {
                            vm.resolveAndSaveLocation(
                                cityQuery = cityValue,
                                customName = nameValue,
                            )
                        }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
