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
import name.kishinevsky.michael.moonriseassistant.components.TodaySection
import name.kishinevsky.michael.moonriseassistant.components.TopBar
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    locationName: String,
    today: ForecastDay,
    upcomingDays: List<ForecastDay>,
    maxMoonriseTime: LocalTime = LocalTime.of(23, 0),
    onMenuClick: () -> Unit = {},
    onDayClick: (ForecastDay) -> Unit = {},
) {
    // Suppressed: IntelliJ doesn't model Compose state semantics — the `selectedDay = null`
    // assignment in onDismissRequest triggers recomposition, which re-reads selectedDay at the
    // `selectedDay?.let { ... }` call below.
    @Suppress("AssignedValueIsNeverRead")
    var selectedDay by remember { mutableStateOf<ForecastDay?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = { TopBar(locationName = locationName, onMenuClick = onMenuClick) },
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
                            selectedDay = day
                            onDayClick(day)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
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
                            selectedDay = day
                            onDayClick(day)
                        },
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }

    selectedDay?.let { day ->
        ModalBottomSheet(
            onDismissRequest = { selectedDay = null },
            sheetState = sheetState,
        ) {
            DetailSheetContent(
                day = day,
                maxMoonriseTime = maxMoonriseTime,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
