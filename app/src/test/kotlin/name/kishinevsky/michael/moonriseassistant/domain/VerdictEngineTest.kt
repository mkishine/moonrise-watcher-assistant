package name.kishinevsky.michael.moonriseassistant.domain

import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class VerdictEngineTest {

    private val engine = VerdictEngine()
    private val defaultSettings = AppSettings()

    private fun makeDay(
        sunset: LocalTime = LocalTime.of(18, 0),
        moonrise: LocalTime = LocalTime.of(18, 30),
        weather: WeatherCondition = WeatherCondition.CLEAR,
    ): ForecastDay {
        return ForecastDay(
            date = LocalDate.of(2026, 3, 3),
            sunset = sunset,
            moonrise = moonrise,
            azimuthDegrees = 90,
            azimuthCardinal = "E",
            weather = weather,
            verdict = Verdict.GOOD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.PASS,
                skyClear = CheckResult.PASS,
            ),
        )
    }

    @Test
    fun `good night - moonrise after sunset, before bedtime, clear weather`() {
        // Given: ideal conditions
        val day = makeDay(
            sunset = LocalTime.of(18, 0),
            moonrise = LocalTime.of(19, 0),
            weather = WeatherCondition.CLEAR,
        )

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is GOOD with all checks passing
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
        assertThat(result.checks.moonriseAfterSunset).isEqualTo(CheckResult.PASS)
        assertThat(result.checks.moonriseBeforeBedtime).isEqualTo(CheckResult.PASS)
        assertThat(result.checks.skyClear).isEqualTo(CheckResult.PASS)
    }

    @Test
    fun `bad - moonrise before sunset beyond tolerance`() {
        // Given: moonrise at 17:00, sunset at 18:00, tolerance 30 min → earliest allowed 17:30
        val day = makeDay(
            sunset = LocalTime.of(18, 0),
            moonrise = LocalTime.of(17, 0),
        )

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is BAD, moonrise-after-sunset check fails
        assertThat(result.verdict).isEqualTo(Verdict.BAD)
        assertThat(result.checks.moonriseAfterSunset).isEqualTo(CheckResult.FAIL)
        assertThat(result.checks.badgeReason()).isEqualTo("before sunset")
    }

    @Test
    fun `bad - moonrise after bedtime`() {
        // Given: moonrise at 23:30, bedtime default 23:00
        val day = makeDay(moonrise = LocalTime.of(23, 30))

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is BAD, moonrise-before-bedtime check fails
        assertThat(result.verdict).isEqualTo(Verdict.BAD)
        assertThat(result.checks.moonriseBeforeBedtime).isEqualTo(CheckResult.FAIL)
        assertThat(result.checks.badgeReason()).isEqualTo("too late")
    }

    @Test
    fun `bad - cloudy weather`() {
        // Given: cloudy conditions
        val day = makeDay(weather = WeatherCondition.CLOUDY)

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is BAD, sky-clear check fails
        assertThat(result.verdict).isEqualTo(Verdict.BAD)
        assertThat(result.checks.skyClear).isEqualTo(CheckResult.FAIL)
        assertThat(result.checks.badgeReason()).isEqualTo("weather")
    }

    @Test
    fun `multiple failures - all reasons reported in checks`() {
        // Given: moonrise before sunset AND cloudy
        val day = makeDay(
            sunset = LocalTime.of(18, 0),
            moonrise = LocalTime.of(17, 0),
            weather = WeatherCondition.CLOUDY,
        )

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is BAD, multiple checks fail
        assertThat(result.verdict).isEqualTo(Verdict.BAD)
        assertThat(result.checks.moonriseAfterSunset).isEqualTo(CheckResult.FAIL)
        assertThat(result.checks.skyClear).isEqualTo(CheckResult.FAIL)
    }

    @Test
    fun `unknown weather with good timing is GOOD`() {
        // Given: unknown weather but good moonrise timing
        val day = makeDay(weather = WeatherCondition.UNKNOWN)

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: verdict is GOOD (unknown doesn't block)
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
        assertThat(result.checks.skyClear).isEqualTo(CheckResult.UNKNOWN)
    }

    @Test
    fun `partly cloudy weather is PASS`() {
        // Given: partly cloudy conditions
        val day = makeDay(weather = WeatherCondition.PARTLY_CLOUDY)

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: sky check passes
        assertThat(result.checks.skyClear).isEqualTo(CheckResult.PASS)
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
    }

    @Test
    fun `moonrise exactly at sunset minus tolerance is PASS`() {
        // Given: moonrise exactly at sunset - 30 min tolerance boundary
        val day = makeDay(
            sunset = LocalTime.of(18, 0),
            moonrise = LocalTime.of(17, 30),
        )

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: boundary is inclusive — PASS
        assertThat(result.checks.moonriseAfterSunset).isEqualTo(CheckResult.PASS)
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
    }

    @Test
    fun `moonrise exactly at bedtime is PASS`() {
        // Given: moonrise exactly at 23:00 (default bedtime)
        val day = makeDay(moonrise = LocalTime.of(23, 0))

        // When: we evaluate
        val result = engine.evaluate(day, defaultSettings)

        // Then: boundary is inclusive — PASS
        assertThat(result.checks.moonriseBeforeBedtime).isEqualTo(CheckResult.PASS)
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
    }

    @Test
    fun `custom settings - tolerance and bedtime are respected`() {
        // Given: custom settings with 60 min tolerance and 22:00 bedtime
        val settings = AppSettings(
            beforeSunsetToleranceMin = 60,
            maxMoonriseTime = LocalTime.of(22, 0),
        )

        // Moonrise at 17:15 — within 60 min tolerance of 18:00 sunset (earliest 17:00)
        // But after 22:00 bedtime? No, 17:15 is before 22:00. So this should be GOOD.
        val day = makeDay(
            sunset = LocalTime.of(18, 0),
            moonrise = LocalTime.of(17, 15),
        )

        // When: we evaluate with custom settings
        val result = engine.evaluate(day, settings)

        // Then: passes with 60 min tolerance (17:15 >= 17:00)
        assertThat(result.checks.moonriseAfterSunset).isEqualTo(CheckResult.PASS)
        assertThat(result.verdict).isEqualTo(Verdict.GOOD)
    }

    @Test
    fun `custom settings - tighter bedtime causes failure`() {
        // Given: custom bedtime of 21:00
        val settings = AppSettings(maxMoonriseTime = LocalTime.of(21, 0))
        val day = makeDay(moonrise = LocalTime.of(21, 30))

        // When: we evaluate
        val result = engine.evaluate(day, settings)

        // Then: moonrise after custom bedtime → BAD
        assertThat(result.verdict).isEqualTo(Verdict.BAD)
        assertThat(result.checks.moonriseBeforeBedtime).isEqualTo(CheckResult.FAIL)
    }
}
