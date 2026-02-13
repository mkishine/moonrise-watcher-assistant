package name.kisinievsky.michael.moonriseassistant.screens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import name.kisinievsky.michael.moonriseassistant.components.ForecastList
import name.kisinievsky.michael.moonriseassistant.components.TodaySection
import name.kisinievsky.michael.moonriseassistant.components.TopBar
import name.kisinievsky.michael.moonriseassistant.model.ForecastDay

@Composable
fun MainScreen(
    locationName: String,
    today: ForecastDay,
    upcomingDays: List<ForecastDay>,
    onMenuClick: () -> Unit = {},
    onDayClick: (ForecastDay) -> Unit = {},
) {
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
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val maxTodayHeight = screenHeight * 0.4f

                Column(modifier = Modifier.fillMaxSize()) {
                    TodaySection(
                        day = today,
                        modifier = Modifier.heightIn(max = maxTodayHeight),
                    )
                    ForecastList(
                        days = upcomingDays,
                        onDayClick = onDayClick,
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
                        onDayClick = onDayClick,
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}
