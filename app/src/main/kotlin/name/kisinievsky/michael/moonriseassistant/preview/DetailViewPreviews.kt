package name.kisinievsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kisinievsky.michael.moonriseassistant.components.DetailSheetContent
import name.kisinievsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Good day previews ─────────────────────────────────────────────

@Preview(name = "Detail – Good Day", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailGoodDay() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(day = SampleData.detailGoodDay)
    }
}

@Preview(name = "Detail – Good Day Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailGoodDayDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(day = SampleData.detailGoodDay)
    }
}

// ── Bad day previews ──────────────────────────────────────────────

@Preview(name = "Detail – Bad Day (Too Late)", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailBadDay() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(day = SampleData.detailBadDay)
    }
}

@Preview(name = "Detail – Bad Day Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailBadDayDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(day = SampleData.detailBadDay)
    }
}

// ── Weather unknown previews ──────────────────────────────────────

@Preview(name = "Detail – Weather Unknown", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailWeatherUnknown() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(day = SampleData.detailWeatherUnknown)
    }
}

@Preview(name = "Detail – Weather Unknown Dark", showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun DetailWeatherUnknownDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        DetailSheetContent(day = SampleData.detailWeatherUnknown)
    }
}

// ── Large font preview ────────────────────────────────────────────

@Preview(name = "Detail – Large Font", showBackground = true, device = Devices.PIXEL_7, fontScale = 1.5f)
@Composable
private fun DetailLargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        DetailSheetContent(day = SampleData.detailGoodDay)
    }
}
