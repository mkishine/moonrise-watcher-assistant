package name.kishinevsky.michael.moonriseassistant.di

import android.content.Context
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import name.kishinevsky.michael.moonriseassistant.BuildConfig
import name.kishinevsky.michael.moonriseassistant.domain.AstroCalculator
import name.kishinevsky.michael.moonriseassistant.domain.VerdictEngine
import name.kishinevsky.michael.moonriseassistant.network.VisualCrossingApi
import name.kishinevsky.michael.moonriseassistant.repository.ForecastRepository
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository
import name.kishinevsky.michael.moonriseassistant.repository.SettingsRepository
import name.kishinevsky.michael.moonriseassistant.storage.MoonriseDatabase

class AppContainer(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val database = Room.databaseBuilder(
        context.applicationContext,
        MoonriseDatabase::class.java,
        "moonrise.db",
    ).build()

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val astroCalculator = AstroCalculator()
    private val verdictEngine = VerdictEngine()

    private val visualCrossingApi = VisualCrossingApi(
        client = httpClient,
        apiKey = BuildConfig.VISUAL_CROSSING_API_KEY,
    )

    val locationRepository = LocationRepository(database.locationDao())
    val settingsRepository = SettingsRepository(database.settingsDao())
    val forecastRepository = ForecastRepository(
        api = visualCrossingApi,
        weatherCacheDao = database.weatherCacheDao(),
        astroCalculator = astroCalculator,
        verdictEngine = verdictEngine,
        json = json,
    )
}
