package name.kishinevsky.michael.moonriseassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.screens.MainScreen
import name.kishinevsky.michael.moonriseassistant.ui.theme.MoonriseAssistantTheme

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
