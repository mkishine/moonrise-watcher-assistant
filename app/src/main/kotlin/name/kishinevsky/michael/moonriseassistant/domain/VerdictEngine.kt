package name.kishinevsky.michael.moonriseassistant.domain

import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.model.AppSettings

data class VerdictResult(
    val verdict: Verdict,
    val checks: VerdictChecks,
)

class VerdictEngine {

    fun evaluate(day: ForecastDay, settings: AppSettings): VerdictResult {
        val moonriseAfterSunset = checkMoonriseAfterSunset(day, settings)
        val moonriseBeforeBedtime = checkMoonriseBeforeBedtime(day, settings)
        val skyClear = checkSkyClear(day)

        val verdict = if (moonriseAfterSunset == CheckResult.FAIL ||
            moonriseBeforeBedtime == CheckResult.FAIL ||
            skyClear == CheckResult.FAIL
        ) {
            Verdict.BAD
        } else {
            Verdict.GOOD
        }

        return VerdictResult(
            verdict = verdict,
            checks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = moonriseAfterSunset,
                moonriseBeforeBedtime = moonriseBeforeBedtime,
                skyClear = skyClear,
            ),
        )
    }

    private fun checkMoonriseAfterSunset(day: ForecastDay, settings: AppSettings): CheckResult {
        val earliestAllowed = day.sunset.minusMinutes(settings.beforeSunsetToleranceMin.toLong())
        return if (!day.moonrise.isBefore(earliestAllowed)) {
            CheckResult.PASS
        } else {
            CheckResult.FAIL
        }
    }

    private fun checkMoonriseBeforeBedtime(day: ForecastDay, settings: AppSettings): CheckResult {
        return if (!day.moonrise.isAfter(settings.maxMoonriseTime)) {
            CheckResult.PASS
        } else {
            CheckResult.FAIL
        }
    }

    private fun checkSkyClear(day: ForecastDay): CheckResult {
        return when (day.weather) {
            WeatherCondition.CLEAR, WeatherCondition.PARTLY_CLOUDY -> CheckResult.PASS
            WeatherCondition.CLOUDY -> CheckResult.FAIL
            WeatherCondition.UNKNOWN -> CheckResult.UNKNOWN
        }
    }
}
