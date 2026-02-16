package name.kishinevsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kishinevsky.michael.moonriseassistant.components.ForecastListItem
import name.kishinevsky.michael.moonriseassistant.components.TodaySection
import name.kishinevsky.michael.moonriseassistant.screens.MainScreen
import name.kishinevsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Full screen previews ───────────────────────────────────────────

@Preview(name = "Portrait Light", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun PortraitLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreen(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            upcomingDays = SampleData.upcomingDays,
        )
    }
}

@Preview(name = "Portrait Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun PortraitDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        MainScreen(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            upcomingDays = SampleData.upcomingDays,
        )
    }
}

@Preview(name = "Landscape", widthDp = 840, heightDp = 400, showSystemUi = true)
@Composable
private fun Landscape() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreen(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            upcomingDays = SampleData.upcomingDays,
        )
    }
}

@Preview(name = "Large Font", device = Devices.PIXEL_7, fontScale = 1.5f, showSystemUi = true)
@Composable
private fun LargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreen(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            upcomingDays = SampleData.upcomingDays,
        )
    }
}

// ── Component previews ─────────────────────────────────────────────

@Preview(name = "Today Section", showBackground = true)
@Composable
private fun TodaySectionPreview() {
    MoonriseAssistantTheme {
        TodaySection(day = SampleData.today)
    }
}

@Preview(name = "Forecast Item – Good", showBackground = true)
@Composable
private fun ForecastItemGood() {
    MoonriseAssistantTheme {
        ForecastListItem(
            day = SampleData.upcomingDays.first(),
            onClick = {},
        )
    }
}

@Preview(name = "Forecast Item – Bad", showBackground = true)
@Composable
private fun ForecastItemBad() {
    MoonriseAssistantTheme {
        ForecastListItem(
            day = SampleData.upcomingDays[1],
            onClick = {},
        )
    }
}
