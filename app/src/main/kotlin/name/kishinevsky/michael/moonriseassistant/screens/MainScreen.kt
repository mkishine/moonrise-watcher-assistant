package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.components.DetailSheetContent
import name.kishinevsky.michael.moonriseassistant.components.EmptyForecastMessage
import name.kishinevsky.michael.moonriseassistant.components.ErrorMessage
import name.kishinevsky.michael.moonriseassistant.components.FirstTimeSetup
import name.kishinevsky.michael.moonriseassistant.components.ForecastList
import name.kishinevsky.michael.moonriseassistant.components.LoadingSkeleton
import name.kishinevsky.michael.moonriseassistant.components.LocationSelectorContent
import name.kishinevsky.michael.moonriseassistant.components.TodaySection
import name.kishinevsky.michael.moonriseassistant.components.TopBar
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import java.time.Instant
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    locationName: String,
    today: ForecastDay,
    upcomingDays: List<ForecastDay>,
    maxMoonriseTime: LocalTime = LocalTime.of(23, 0),
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    lastUpdated: Instant? = null,
    onMenuClick: () -> Unit = {},
    onDayClick: (ForecastDay) -> Unit = {},
    locations: List<SavedLocation> = emptyList(),
    activeLocationId: String = "",
    onLocationSelect: (SavedLocation) -> Unit = {},
    onAddLocation: () -> Unit = {},
    onEditLocation: (SavedLocation) -> Unit = {},
    onDeleteLocation: (SavedLocation) -> Unit = {},
) {
    // Suppressed: IntelliJ doesn't model Compose state semantics — the `selectedDayIndex = null`
    // assignment in onDismissRequest triggers recomposition, which re-reads selectedDayIndex at
    // the `selectedDayIndex?.let { ... }` call below.
    @Suppress("AssignedValueIsNeverRead", "RedundantSuppression")
    var selectedDayIndex by remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    @Suppress("AssignedValueIsNeverRead", "RedundantSuppression")
    var showLocationSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                locationName = locationName,
                onMenuClick = onMenuClick,
                onRefreshClick = onRefresh,
                onLocationNameClick = { showLocationSelector = true },
            )
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (maxWidth < 600.dp) {
                // Portrait layout: column
                val maxTodayHeight = maxHeight * 0.4f

                Column(modifier = Modifier.fillMaxSize()) {
                    TodaySection(
                        day = today,
                        modifier = Modifier.heightIn(max = maxTodayHeight),
                    )
                    ForecastList(
                        days = upcomingDays,
                        onDayClick = { day ->
                            selectedDayIndex = upcomingDays.indexOf(day)
                            onDayClick(day)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        lastUpdated = lastUpdated,
                    )
                }
            } else {
                // Landscape layout: row
                Row(modifier = Modifier.fillMaxSize()) {
                    TodaySection(
                        day = today,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                    ForecastList(
                        days = upcomingDays,
                        onDayClick = { day ->
                            selectedDayIndex = upcomingDays.indexOf(day)
                            onDayClick(day)
                        },
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        lastUpdated = lastUpdated,
                    )
                }
            }
        }
    }

    @Suppress("AssignedValueIsNeverRead", "RedundantSuppression")
    selectedDayIndex?.let { index ->
        ModalBottomSheet(
            onDismissRequest = { selectedDayIndex = null },
            sheetState = sheetState,
        ) {
            DetailSheetContent(
                days = upcomingDays,
                initialIndex = index,
                maxMoonriseTime = maxMoonriseTime,
            )
        }
    }

    @Suppress("AssignedValueIsNeverRead", "RedundantSuppression")
    if (showLocationSelector) {
        ModalBottomSheet(
            onDismissRequest = { showLocationSelector = false },
        ) {
            LocationSelectorContent(
                locations = locations,
                activeLocationId = activeLocationId,
                onLocationSelect = { location ->
                    showLocationSelector = false
                    onLocationSelect(location)
                },
                onEditLocation = { location ->
                    showLocationSelector = false
                    onEditLocation(location)
                },
                onDeleteLocation = onDeleteLocation,
                onAddLocation = {
                    showLocationSelector = false
                    onAddLocation()
                },
                onClose = { showLocationSelector = false },
            )
        }
    }
}

@Composable
fun MainScreenEmpty(
    locationName: String,
    today: ForecastDay,
    nextFullMoonDate: String,
    onMenuClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopBar(locationName = locationName, onMenuClick = onMenuClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            TodaySection(day = today)
            EmptyForecastMessage(
                nextFullMoonDate = nextFullMoonDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}

@Composable
fun MainScreenLoading(
    locationName: String,
    onMenuClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopBar(locationName = locationName, onMenuClick = onMenuClick) },
    ) { innerPadding ->
        LoadingSkeleton(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
fun MainScreenError(
    locationName: String,
    errorMessage: String,
    onRetry: () -> Unit,
    onMenuClick: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopBar(locationName = locationName, onMenuClick = onMenuClick) },
    ) { innerPadding ->
        ErrorMessage(
            message = errorMessage,
            onRetry = onRetry,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenFirstTime(
    onAddLocation: () -> Unit,
    onHowItWorksClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Moonrise Assistant") },
            )
        },
    ) { innerPadding ->
        FirstTimeSetup(
            onAddLocation = onAddLocation,
            onHowItWorksClick = onHowItWorksClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
