package name.kisinievsky.michael.moonriseassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import name.kisinievsky.michael.moonriseassistant.preview.SampleData
import name.kisinievsky.michael.moonriseassistant.screens.MainScreen
import name.kisinievsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoonriseAssistantTheme {
                MainScreen(
                    locationName = SampleData.LOCATION_NAME,
                    today = SampleData.today,
                    upcomingDays = SampleData.upcomingDays,
                )
            }
        }
    }
}
