package name.kishinevsky.michael.moonriseassistant.astro

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import org.shredzone.commons.suncalc.MoonPosition
import org.shredzone.commons.suncalc.MoonTimes
import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.abs

/**
 * Smoke test for commons-suncalc library.
 * Verifies the library provides all four calculations the app needs:
 * moonrise time, sunset time, moon phase, and moonrise azimuth.
 *
 * Uses a known date (2026-03-03, full moon) and location (Seattle) so
 * expected values can be roughly verified.
 */
class CommonsSuncalcSmokeTest {

    // Seattle, WA
    private val lat = 47.6062
    private val lng = -122.3321
    private val zone = ZoneId.of("America/Los_Angeles")
    private val fullMoonDate = LocalDate.of(2026, 3, 3)

    @Test
    fun `moonrise time is computed for a given date and location`() {
        // Given: a date near full moon and a location
        // When: we compute moon times
        val moonTimes = MoonTimes.compute()
            .on(fullMoonDate)
            .at(lat, lng)
            .timezone(zone)
            .execute()

        // Then: moonrise is present and falls on the expected date
        val rise = moonTimes.rise
        assertThat(rise).isNotNull()
        assertThat(rise!!.toLocalDate())
            .describedAs("Moonrise should be on or near the requested date")
            .isBetween(fullMoonDate.minusDays(1), fullMoonDate.plusDays(1))
    }

    @Test
    fun `sunset time is computed for a given date and location`() {
        // Given: a date and location
        // When: we compute sun times
        val sunTimes = SunTimes.compute()
            .on(fullMoonDate)
            .at(lat, lng)
            .timezone(zone)
            .execute()

        // Then: sunset is present and has a reasonable hour (afternoon/evening)
        val set = sunTimes.set
        assertThat(set).isNotNull()
        val hour = set!!.hour
        assertThat(hour)
            .describedAs("Sunset hour in Seattle in March should be in the afternoon/evening")
            .isBetween(17, 20)
    }

    @Test
    fun `moon illumination and phase are computed`() {
        // Given: a full moon date
        // When: we compute moon illumination
        val illumination = MoonIllumination.compute()
            .on(fullMoonDate)
            .execute()

        // Then: fraction is near 1.0 (full moon) and phase is near 0
        // Phase range is -180 to +180 where 0 = full moon, ±180 = new moon
        assertThat(illumination.fraction)
            .describedAs("Illumination fraction near full moon should be high")
            .isGreaterThan(0.9)
        assertThat(abs(illumination.phase))
            .describedAs("Phase angle near full moon should be close to 0")
            .isLessThan(30.0)
    }

    @Test
    fun `next full moon date can be calculated`() {
        // Given: a starting date before the full moon
        val startDate = LocalDate.of(2026, 2, 15)

        // When: we find the next full moon
        val nextFullMoon = MoonPhase.compute()
            .phase(MoonPhase.Phase.FULL_MOON)
            .on(startDate)
            .execute()

        // Then: the result is a date in late February or early March 2026
        val fullMoonTime = nextFullMoon.time
        assertThat(fullMoonTime).isNotNull()
        assertThat(fullMoonTime.toLocalDate())
            .describedAs("Next full moon after Feb 15 should be around Mar 3, 2026")
            .isBetween(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 5))
    }

    @Test
    fun `moonrise azimuth is computed in degrees`() {
        // Given: a date/time and location
        // When: we compute moon position at moonrise time
        val moonTimes = MoonTimes.compute()
            .on(fullMoonDate)
            .at(lat, lng)
            .timezone(zone)
            .execute()
        val rise = moonTimes.rise!!

        val position = MoonPosition.compute()
            .on(rise)
            .at(lat, lng)
            .execute()

        // Then: azimuth is a valid compass bearing (0-360 degrees)
        val azimuth = position.azimuth
        assertThat(azimuth)
            .describedAs("Azimuth should be a valid compass bearing")
            .isBetween(0.0, 360.0)
    }
}
