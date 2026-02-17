package name.kishinevsky.michael.moonriseassistant.preview

import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
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
        temperatureF = 45,
        windchillF = 38,
        windSpeedMph = 10,
        verdict = Verdict.GOOD,
        verdictChecks = VerdictChecks(
            phaseWindow = CheckResult.PASS,
            moonriseAfterSunset = CheckResult.PASS,
            moonriseBeforeBedtime = CheckResult.PASS,
            skyClear = CheckResult.PASS,
        ),
        azimuthCardinalExpanded = "East-Southeast",
        cloudCoverPercent = 10,
        windDirection = "NW",
        precipitationPercent = 5,
        precipitationType = "rain",
    )

    val upcomingDays = listOf(
        ForecastDay(
            date = LocalDate.of(2026, 2, 13),
            sunset = LocalTime.of(17, 35),
            moonrise = LocalTime.of(19, 8),
            azimuthDegrees = 103,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.PARTLY_CLOUDY,
            temperatureF = 42,
            windchillF = 35,
            windSpeedMph = 12,
            verdict = Verdict.GOOD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.PASS,
                skyClear = CheckResult.PASS,
            ),
            azimuthCardinalExpanded = "East-Southeast",
            cloudCoverPercent = 40,
            windDirection = "NW",
            precipitationPercent = 10,
            precipitationType = "rain",
        ),
        ForecastDay(
            date = LocalDate.of(2026, 2, 14),
            sunset = LocalTime.of(17, 37),
            moonrise = LocalTime.of(20, 15),
            azimuthDegrees = 107,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.CLOUDY,
            temperatureF = 52,
            windchillF = 52,
            windSpeedMph = 3,
            verdict = Verdict.BAD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.PASS,
                skyClear = CheckResult.FAIL,
            ),
            azimuthCardinalExpanded = "East-Southeast",
            cloudCoverPercent = 90,
            windDirection = "S",
            precipitationPercent = 60,
            precipitationType = "rain",
        ),
        ForecastDay(
            date = LocalDate.of(2026, 2, 16),
            sunset = LocalTime.of(17, 39),
            moonrise = LocalTime.of(23, 42),
            azimuthDegrees = 112,
            azimuthCardinal = "ESE",
            weather = WeatherCondition.CLEAR,
            temperatureF = 38,
            windchillF = 30,
            windSpeedMph = 8,
            verdict = Verdict.BAD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.FAIL,
                skyClear = CheckResult.PASS,
            ),
            azimuthCardinalExpanded = "East-Southeast",
            cloudCoverPercent = 5,
            windDirection = "N",
            precipitationPercent = 0,
        ),
        ForecastDay(
            date = LocalDate.of(2026, 3, 12),
            sunset = LocalTime.of(18, 12),
            moonrise = LocalTime.of(18, 45),
            azimuthDegrees = 96,
            azimuthCardinal = "E",
            weather = WeatherCondition.UNKNOWN,
            verdict = Verdict.GOOD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.PASS,
                skyClear = CheckResult.UNKNOWN,
            ),
            azimuthCardinalExpanded = "East",
        ),
    )

    // Detail view sample days (aliases for convenience)
    val detailGoodDay = upcomingDays[0]
    val detailBadDay = upcomingDays[2]
    val detailWeatherUnknown = upcomingDays[3]

    // Settings sample data
    val defaultSettings = AppSettings()
    val customSettings = AppSettings(
        daysBeforeFullMoon = 3,
        daysAfterFullMoon = 7,
        forecastPeriodMonths = 6,
        maxMoonriseTime = LocalTime.of(22, 30),
        beforeSunsetToleranceMin = 45,
        useMetric = true,
    )

    // Location sample data
    val savedLocations = listOf(
        SavedLocation(
            id = "1",
            name = "Home \u2014 Seattle, WA",
            cityState = "Seattle, WA",
            latitude = 47.6062,
            longitude = -122.3321,
        ),
        SavedLocation(
            id = "2",
            name = "Cabin \u2014 Leavenworth",
            cityState = "Leavenworth, WA",
            latitude = 47.5962,
            longitude = -120.6615,
        ),
        SavedLocation(
            id = "3",
            name = "Observatory \u2014 Goldendale",
            cityState = "Goldendale, WA",
            latitude = 45.8205,
            longitude = -120.8217,
        ),
    )

    val singleLocation = listOf(savedLocations.first())

    // State screen sample data
    const val NEXT_FULL_MOON_DATE = "Mar 12"
    const val ERROR_NETWORK = "Check your connection and try again."
    const val ERROR_LOCATION = "Unable to determine location. Check settings."
    const val ERROR_API = "Weather service unavailable. Try again later."
}
