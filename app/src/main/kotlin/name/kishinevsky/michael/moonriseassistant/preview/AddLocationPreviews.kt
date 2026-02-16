package name.kishinevsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationContext
import name.kishinevsky.michael.moonriseassistant.screens.AddLocationScreen
import name.kishinevsky.michael.moonriseassistant.screens.LocationInputMode
import name.kishinevsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── First-time setup previews ─────────────────────────────────────

@Preview(name = "Add Location – First Time (City)", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun FirstTimeCityLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.FIRST_TIME,
            inputMode = LocationInputMode.CITY,
            cityValue = "Seattle, WA",
            nameValue = "Home",
        )
    }
}

@Preview(name = "Add Location – First Time Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun FirstTimeCityDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        AddLocationScreen(
            context = AddLocationContext.FIRST_TIME,
            inputMode = LocationInputMode.CITY,
            cityValue = "Seattle, WA",
            nameValue = "Home",
        )
    }
}

@Preview(name = "Add Location – First Time Empty", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun FirstTimeEmpty() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.FIRST_TIME,
            inputMode = LocationInputMode.CITY,
        )
    }
}

// ── Additional location previews ──────────────────────────────────

@Preview(name = "Add Location – Additional (Coordinates)", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun AdditionalCoordinates() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.ADDITIONAL,
            inputMode = LocationInputMode.COORDINATES,
            latitudeValue = "47.6062",
            longitudeValue = "-122.3321",
            nameValue = "Observatory",
        )
    }
}

// ── Error state previews ──────────────────────────────────────────

@Preview(name = "Add Location – Error (Not Found)", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorNotFound() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.ADDITIONAL,
            inputMode = LocationInputMode.CITY,
            cityValue = "Xyzzyville, ZZ",
            errorMessage = "Location not found. Check spelling and try again.",
        )
    }
}

@Preview(name = "Add Location – Error Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun ErrorNotFoundDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        AddLocationScreen(
            context = AddLocationContext.ADDITIONAL,
            inputMode = LocationInputMode.CITY,
            cityValue = "Xyzzyville, ZZ",
            errorMessage = "Location not found. Check spelling and try again.",
        )
    }
}

// ── Loading state preview ─────────────────────────────────────────

@Preview(name = "Add Location – Loading", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun Loading() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.ADDITIONAL,
            inputMode = LocationInputMode.CITY,
            cityValue = "Portland, OR",
            nameValue = "Weekend Spot",
            isLoading = true,
        )
    }
}

// ── Large font preview ────────────────────────────────────────────

@Preview(name = "Add Location – Large Font", device = Devices.PIXEL_7, fontScale = 1.5f, showSystemUi = true)
@Composable
private fun LargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        AddLocationScreen(
            context = AddLocationContext.FIRST_TIME,
            inputMode = LocationInputMode.CITY,
            cityValue = "Seattle, WA",
            nameValue = "Home",
        )
    }
}
