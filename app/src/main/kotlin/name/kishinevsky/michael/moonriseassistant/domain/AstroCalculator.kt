package name.kishinevsky.michael.moonriseassistant.domain

import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import org.shredzone.commons.suncalc.MoonPosition
import org.shredzone.commons.suncalc.MoonTimes
import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.abs

class AstroCalculator {

    fun moonrise(date: LocalDate, lat: Double, lng: Double, zone: ZoneId): LocalTime? {
        val moonTimes = MoonTimes.compute()
            .on(date)
            .at(lat, lng)
            .timezone(zone)
            .oneDay()
            .execute()
        return moonTimes.rise?.toLocalTime()
    }

    fun sunset(date: LocalDate, lat: Double, lng: Double, zone: ZoneId): LocalTime {
        val sunTimes = SunTimes.compute()
            .on(date)
            .at(lat, lng)
            .timezone(zone)
            .execute()
        return sunTimes.set?.toLocalTime()
            ?: error("No sunset for $date at ($lat, $lng)")
    }

    /**
     * Returns moon phase normalized to 0.0 = new moon, 0.5 = full moon.
     *
     * commons-suncalc convention: phase -180..+180 where 0 = full moon, ±180 = new moon.
     * Our convention: 0.0 = new moon, 0.5 = full moon, 1.0 = new moon (next cycle).
     */
    fun moonPhase(date: LocalDate): Double {
        val illumination = MoonIllumination.compute()
            .on(date)
            .execute()
        return (180.0 - abs(illumination.phase)) / 360.0
    }

    fun moonAzimuth(date: LocalDate, lat: Double, lng: Double, zone: ZoneId): Double {
        val moonTimes = MoonTimes.compute()
            .on(date)
            .at(lat, lng)
            .timezone(zone)
            .oneDay()
            .execute()
        val rise = moonTimes.rise
            ?: error("No moonrise for $date at ($lat, $lng)")
        val position = MoonPosition.compute()
            .on(rise)
            .at(lat, lng)
            .execute()
        return position.azimuth
    }

    fun isInPhaseWindow(date: LocalDate, daysBefore: Int, daysAfter: Int): Boolean {
        // Search from (date - daysAfter) so that dates already past the full moon
        // but still within the trailing window resolve to the correct full moon.
        val fullMoon = nextFullMoon(date.minusDays(daysAfter.toLong()))
        val windowStart = fullMoon.minusDays(daysBefore.toLong())
        val windowEnd = fullMoon.plusDays(daysAfter.toLong())
        return !date.isBefore(windowStart) && !date.isAfter(windowEnd)
    }

    fun nextFullMoon(after: LocalDate): LocalDate {
        val result = MoonPhase.compute()
            .phase(MoonPhase.Phase.FULL_MOON)
            .on(after)
            .execute()
        return result.time.toLocalDate()
    }
}
