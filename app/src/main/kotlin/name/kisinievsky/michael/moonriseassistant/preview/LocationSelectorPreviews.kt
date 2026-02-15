package name.kisinievsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kisinievsky.michael.moonriseassistant.components.LocationSelectorContent
import name.kisinievsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Normal list previews ──────────────────────────────────────────

@Preview(name = "Location Selector – Multiple", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun LocationSelectorMultiple() {
    MoonriseAssistantTheme(darkTheme = false) {
        LocationSelectorContent(
            locations = SampleData.savedLocations,
            activeLocationId = "1",
        )
    }
}

@Preview(name = "Location Selector – Multiple Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun LocationSelectorMultipleDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        LocationSelectorContent(
            locations = SampleData.savedLocations,
            activeLocationId = "1",
        )
    }
}

// ── Single location preview ───────────────────────────────────────

@Preview(name = "Location Selector – Single", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun LocationSelectorSingle() {
    MoonriseAssistantTheme(darkTheme = false) {
        LocationSelectorContent(
            locations = SampleData.singleLocation,
            activeLocationId = "1",
        )
    }
}

// ── Different active location ─────────────────────────────────────

@Preview(name = "Location Selector – Second Active", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun LocationSelectorSecondActive() {
    MoonriseAssistantTheme(darkTheme = false) {
        LocationSelectorContent(
            locations = SampleData.savedLocations,
            activeLocationId = "2",
        )
    }
}

// ── Large font preview ────────────────────────────────────────────

@Preview(name = "Location Selector – Large Font", showBackground = true, device = Devices.PIXEL_7, fontScale = 1.5f)
@Composable
private fun LocationSelectorLargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        LocationSelectorContent(
            locations = SampleData.savedLocations,
            activeLocationId = "1",
        )
    }
}
