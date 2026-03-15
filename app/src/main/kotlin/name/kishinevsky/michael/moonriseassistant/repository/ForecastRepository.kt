package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.serialization.json.Json
import name.kishinevsky.michael.moonriseassistant.domain.AstroCalculator
import name.kishinevsky.michael.moonriseassistant.domain.VerdictEngine
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.network.VisualCrossingApi
import name.kishinevsky.michael.moonriseassistant.network.model.DayResponse
import name.kishinevsky.michael.moonriseassistant.storage.dao.WeatherCacheDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt

open class ForecastRepository(
    private val api: VisualCrossingApi,
    private val weatherCacheDao: WeatherCacheDao,
    private val astroCalculator: AstroCalculator,
    private val verdictEngine: VerdictEngine,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {

    open suspend fun getForecast(
        location: SavedLocation,
        settings: AppSettings,
        zone: ZoneId,
        today: LocalDate = LocalDate.now(),
    ): List<ForecastDay> {
        val weatherByDate = fetchWeatherByDate(location, settings)
        return buildForecast(weatherByDate, location, settings, zone, today)
    }

    /**
     * Pure function: builds the forecast list from pre-fetched weather data.
     * Exposed for testing without network/cache dependencies.
     */
    fun buildForecast(
        weatherByDate: Map<LocalDate, DayResponse>,
        location: SavedLocation,
        settings: AppSettings,
        zone: ZoneId,
        today: LocalDate,
    ): List<ForecastDay> {
        val endDate = today.plusMonths(settings.forecastPeriodMonths.toLong())
        val result = mutableListOf<ForecastDay>()

        var date = today
        while (!date.isAfter(endDate)) {
            val inPhaseWindow = astroCalculator.isInPhaseWindow(date, settings.daysBeforeFullMoon, settings.daysAfterFullMoon)
            // Always include today; only include future days that are in the phase window
            if (date == today || inPhaseWindow) {
                val moonrise = astroCalculator.moonrise(date, location.latitude, location.longitude, zone)
                if (moonrise != null) {
                    val sunset = astroCalculator.sunset(date, location.latitude, location.longitude, zone)
                    val azimuth = astroCalculator.moonAzimuth(date, location.latitude, location.longitude, zone)
                    val weatherDay = weatherByDate[date]

                    val day = ForecastDay(
                        date = date,
                        sunset = sunset,
                        moonrise = moonrise,
                        azimuthDegrees = azimuth.roundToInt(),
                        azimuthCardinal = azimuthToCardinal(azimuth),
                        weather = weatherDay?.toWeatherCondition() ?: WeatherCondition.UNKNOWN,
                        temperatureF = weatherDay?.temp?.roundToInt(),
                        windchillF = weatherDay?.feelslike?.roundToInt(),
                        windSpeedMph = weatherDay?.windspeed?.roundToInt(),
                        verdict = Verdict.GOOD, // placeholder, replaced below
                        verdictChecks = PLACEHOLDER_CHECKS, // placeholder, replaced below
                        azimuthCardinalExpanded = azimuthToCardinalExpanded(azimuth),
                        cloudCoverPercent = weatherDay?.cloudcover?.roundToInt(),
                        windDirection = weatherDay?.winddir?.let { azimuthToCardinal(it) },
                        precipitationPercent = weatherDay?.precipprob?.roundToInt(),
                        precipitationType = weatherDay?.preciptype?.joinToString(", "),
                    )

                    val verdictResult = verdictEngine.evaluate(day, settings, inPhaseWindow)
                    result.add(
                        day.copy(
                            verdict = verdictResult.verdict,
                            verdictChecks = verdictResult.checks,
                        )
                    )
                }
            }
            date = date.plusDays(1)
        }

        return result
    }

    private suspend fun fetchWeatherByDate(
        location: SavedLocation,
        settings: AppSettings,
    ): Map<LocalDate, DayResponse> {
        val locationId = location.id.toLongOrNull() ?: 0L
        val unitGroup = if (settings.useMetric) "metric" else "us"

        return try {
            val response = api.getTimeline(location.latitude, location.longitude, unitGroup)
            cacheWeatherDays(locationId, response.days)
            response.days.associateBy { LocalDate.parse(it.datetime) }
        } catch (_: Exception) {
            loadFromCache(locationId)
        }
    }

    private suspend fun cacheWeatherDays(locationId: Long, days: List<DayResponse>) {
        val now = System.currentTimeMillis()
        val entities = days.map { day ->
            WeatherCacheEntity(
                locationId = locationId,
                date = day.datetime,
                jsonBlob = json.encodeToString(DayResponse.serializer(), day),
                fetchedAt = now,
            )
        }
        weatherCacheDao.deleteForLocation(locationId)
        weatherCacheDao.insertAll(entities)
    }

    private suspend fun loadFromCache(locationId: Long): Map<LocalDate, DayResponse> {
        return weatherCacheDao.getForLocation(locationId).associate { entity ->
            LocalDate.parse(entity.date) to json.decodeFromString(DayResponse.serializer(), entity.jsonBlob)
        }
    }

    companion object {
        private val PLACEHOLDER_CHECKS = VerdictChecks(
            phaseWindow = CheckResult.PASS,
            moonriseAfterSunset = CheckResult.PASS,
            moonriseBeforeBedtime = CheckResult.PASS,
            skyClear = CheckResult.PASS,
        )

        private val CARDINAL_DIRECTIONS = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        private val CARDINAL_EXPANDED = listOf(
            "North", "Northeast", "East", "Southeast",
            "South", "Southwest", "West", "Northwest",
        )

        fun azimuthToCardinal(degrees: Double): String {
            val index = ((degrees + 22.5) / 45.0).toInt() % 8
            return CARDINAL_DIRECTIONS[index]
        }

        fun azimuthToCardinalExpanded(degrees: Double): String {
            val index = ((degrees + 22.5) / 45.0).toInt() % 8
            return CARDINAL_EXPANDED[index]
        }

        fun DayResponse.toWeatherCondition(): WeatherCondition {
            return when {
                cloudcover <= 25.0 -> WeatherCondition.CLEAR
                cloudcover <= 75.0 -> WeatherCondition.PARTLY_CLOUDY
                else -> WeatherCondition.CLOUDY
            }
        }
    }
}
