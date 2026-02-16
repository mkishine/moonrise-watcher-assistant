package name.kishinevsky.michael.moonriseassistant.model

import java.time.LocalDate
import java.time.LocalTime

data class ForecastDay(
    val date: LocalDate,
    val sunset: LocalTime,
    val moonrise: LocalTime,
    val azimuthDegrees: Int,
    val azimuthCardinal: String,
    val weather: WeatherCondition,
    val temperatureF: Int? = null,
    val windchillF: Int? = null,
    val windSpeedMph: Int? = null,
    val verdict: Verdict,
    val verdictReason: String? = null,
    // Detail view fields (PRD 6.4.4–6.4.8)
    val azimuthCardinalExpanded: String? = null,
    val cloudCoverPercent: Int? = null,
    val windDirection: String? = null,
    val precipitationPercent: Int? = null,
    val precipitationType: String? = null,
)

enum class Verdict { GOOD, BAD }

enum class WeatherCondition { CLEAR, PARTLY_CLOUDY, CLOUDY, UNKNOWN }

data class SavedLocation(
    val id: String,
    val name: String,
    val cityState: String?,
    val latitude: Double,
    val longitude: Double,
)

data class AppSettings(
    val daysBeforeFullMoon: Int = 2,
    val daysAfterFullMoon: Int = 5,
    val forecastPeriodMonths: Int = 3,
    val maxMoonriseTime: LocalTime = LocalTime.of(23, 0),
    val beforeSunsetToleranceMin: Int = 30,
    val useMetric: Boolean = false,
)
