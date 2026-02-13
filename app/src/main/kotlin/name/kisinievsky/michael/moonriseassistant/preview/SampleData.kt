package name.kisinievsky.michael.moonriseassistant.preview

import name.kisinievsky.michael.moonriseassistant.model.ForecastDay
import name.kisinievsky.michael.moonriseassistant.model.Verdict
import name.kisinievsky.michael.moonriseassistant.model.WeatherCondition
import java.time.LocalDate
import java.time.LocalTime

object SampleData {

    const val LOCATION_NAME = "Home — Seattle, WA"

    val today = ForecastDay(
        date = LocalDate.of(2026, 2, 12),
        sunset = LocalTime.of(17, 34),
        moonrise = LocalTime.of(18, 12),
        azimuthDegrees = 98,
        azimuthCardinal = "ESE",
        weather = WeatherCondition.CLEAR,
        verdict = Verdict.GOOD,
    )

    val upcomingDays = listOf(
        ForecastDay(
            date = LocalDate.of(2026, 2, 13),
            sunset = LocalTime.of(17, 35),
            moonrise = LocalTime.of(19, 8),
            azimuthDegrees = 103,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.PARTLY_CLOUDY,
            verdict = Verdict.GOOD,
        ),
        ForecastDay(
            date = LocalDate.of(2026, 2, 14),
            sunset = LocalTime.of(17, 37),
            moonrise = LocalTime.of(20, 15),
            azimuthDegrees = 107,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.CLOUDY,
            verdict = Verdict.BAD,
            verdictReason = "weather",
        ),
        ForecastDay(
            date = LocalDate.of(2026, 2, 16),
            sunset = LocalTime.of(17, 39),
            moonrise = LocalTime.of(23, 42),
            azimuthDegrees = 112,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.CLEAR,
            verdict = Verdict.BAD,
            verdictReason = "too late",
        ),
        ForecastDay(
            date = LocalDate.of(2026, 3, 12),
            sunset = LocalTime.of(18, 12),
            moonrise = LocalTime.of(18, 45),
            azimuthDegrees = 96,
            azimuthCardinal = "E",
            weather = WeatherCondition.UNKNOWN,
            verdict = Verdict.GOOD,
        ),
    )
}
