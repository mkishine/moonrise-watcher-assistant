package name.kisinievsky.michael.moonriseassistant.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import name.kisinievsky.michael.moonriseassistant.screens.SettingsScreen
import name.kisinievsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

// ── Default settings previews ─────────────────────────────────────

@Preview(name = "Settings – Default Light", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun SettingsDefaultLight() {
    MoonriseAssistantTheme(darkTheme = false) {
        SettingsScreen(settings = SampleData.defaultSettings)
    }
}

@Preview(name = "Settings – Default Dark", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun SettingsDefaultDark() {
    MoonriseAssistantTheme(darkTheme = true) {
        SettingsScreen(settings = SampleData.defaultSettings)
    }
}

// ── Custom settings preview ───────────────────────────────────────

@Preview(name = "Settings – Custom Values", device = Devices.PIXEL_7, showSystemUi = true)
@Composable
private fun SettingsCustomValues() {
    MoonriseAssistantTheme(darkTheme = false) {
        SettingsScreen(settings = SampleData.customSettings)
    }
}

// ── Large font preview ────────────────────────────────────────────

@Preview(name = "Settings – Large Font", device = Devices.PIXEL_7, fontScale = 1.5f, showSystemUi = true)
@Composable
private fun SettingsLargeFont() {
    MoonriseAssistantTheme(darkTheme = false) {
        SettingsScreen(settings = SampleData.defaultSettings)
    }
}
