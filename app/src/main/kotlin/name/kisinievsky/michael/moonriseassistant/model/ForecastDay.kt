package name.kisinievsky.michael.moonriseassistant.model

import java.time.LocalDate
import java.time.LocalTime

data class ForecastDay(
    val date: LocalDate,
    val sunset: LocalTime,
    val moonrise: LocalTime,
    val azimuthDegrees: Int,
    val azimuthCardinal: String,
    val weather: WeatherCondition,
    val verdict: Verdict,
    val verdictReason: String? = null,
)

enum class Verdict { GOOD, BAD }

enum class WeatherCondition { CLEAR, PARTLY_CLOUDY, CLOUDY, UNKNOWN }
