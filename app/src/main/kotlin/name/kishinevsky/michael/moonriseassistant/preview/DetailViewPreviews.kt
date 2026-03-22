package name.kishinevsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kishinevsky.michael.moonriseassistant.components.DetailSheetContent
import name.kishinevsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Good day previews ─────────────────────────────────────────────

@Preview(name = "Detail – Good Day", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailGoodDay() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 0)
    }
}

@Preview(name = "Detail – Good Day Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailGoodDayDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 0)
    }
}

// ── Bad day previews ──────────────────────────────────────────────

@Preview(name = "Detail – Bad Day (Too Late)", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailBadDay() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 2)
    }
}

@Preview(name = "Detail – Bad Day Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailBadDayDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 2)
    }
}

// ── Weather unknown previews ──────────────────────────────────────

@Preview(name = "Detail – Weather Unknown", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailWeatherUnknown() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 3)
    }
}

@Preview(name = "Detail – Weather Unknown Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailWeatherUnknownDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 3)
    }
}

// ── Large font preview ────────────────────────────────────────────

@Preview(name = "Detail – Large Font", showBackground = true, device = Devices.PIXEL_7, fontScale = 1.5f)
@Composable
private fun DetailLargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(days = SampleData.upcomingDays, initialIndex = 0)
    }
}
