package name.kishinevsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenEmpty
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenError
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenFirstTime
import name.kishinevsky.michael.moonriseassistant.screens.MainScreenLoading
import name.kishinevsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Empty state previews ─────────────────────────────────────────

@Preview(name = "Empty – Light", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun EmptyLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenEmpty(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            nextFullMoonDate = SampleData.NEXT_FULL_MOON_DATE,
        )
    }
}

@Preview(name = "Empty – Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun EmptyDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        MainScreenEmpty(
            locationName = SampleData.LOCATION_NAME,
            today = SampleData.today,
            nextFullMoonDate = SampleData.NEXT_FULL_MOON_DATE,
        )
    }
}

// ── Loading state previews ───────────────────────────────────────

@Preview(name = "Loading – Light", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun LoadingLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenLoading(locationName = SampleData.LOCATION_NAME)
    }
}

@Preview(name = "Loading – Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun LoadingDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        MainScreenLoading(locationName = SampleData.LOCATION_NAME)
    }
}

// ── Error state previews ─────────────────────────────────────────

@Preview(name = "Error – Network", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorNetwork() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenError(
            locationName = SampleData.LOCATION_NAME,
            errorMessage = SampleData.ERROR_NETWORK,
            onRetry = {},
        )
    }
}

@Preview(name = "Error – Location", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorLocation() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenError(
            locationName = SampleData.LOCATION_NAME,
            errorMessage = SampleData.ERROR_LOCATION,
            onRetry = {},
        )
    }
}

@Preview(name = "Error – API", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorApi() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenError(
            locationName = SampleData.LOCATION_NAME,
            errorMessage = SampleData.ERROR_API,
            onRetry = {},
        )
    }
}

@Preview(name = "Error – Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        MainScreenError(
            locationName = SampleData.LOCATION_NAME,
            errorMessage = SampleData.ERROR_NETWORK,
            onRetry = {},
        )
    }
}

// ── First-time setup previews ────────────────────────────────────

@Preview(name = "First Time – Light", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun FirstTimeLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenFirstTime(onAddLocation = {})
    }
}

@Preview(name = "First Time – Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun FirstTimeDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        MainScreenFirstTime(onAddLocation = {})
    }
}

@Preview(name = "First Time – Landscape", widthDp = 840, heightDp = 400, showSystemUi = true)
@Composable
private fun FirstTimeLandscape() {
    MoonriseAssistantTheme(darkTheme = false) {
        MainScreenFirstTime(onAddLocation = {})
    }
}
