package name.kishinevsky.michael.moonriseassistant.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId

class AstroCalculatorTest {

    private val calc = AstroCalculator()

    // Seattle, WA
    private val lat = 47.6062
    private val lng = -122.3321
    private val zone = ZoneId.of("America/Los_Angeles")

    // 2026-03-03 is a full moon
    private val fullMoonDate = LocalDate.of(2026, 3, 3)

    @Test
    fun `moonrise returns time for a known date and location`() {
        // Given: a full moon date in Seattle
        // When: we compute moonrise
        val rise = calc.moonrise(fullMoonDate, lat, lng, zone)

        // Then: moonrise is present and in the evening (full moon rises near sunset)
        assertThat(rise).isNotNull()
        assertThat(rise!!.hour)
            .describedAs("Full moon moonrise should be in the evening")
            .isBetween(17, 20)
    }

    @Test
    fun `sunset returns time for a known date and location`() {
        // Given: a date in early March in Seattle
        // When: we compute sunset
        val sunset = calc.sunset(fullMoonDate, lat, lng, zone)

        // Then: sunset is in the late afternoon/early evening
        assertThat(sunset.hour)
            .describedAs("Sunset in Seattle in early March")
            .isBetween(17, 19)
    }

    @Test
    fun `moonPhase returns 0_5 for full moon date`() {
        // Given: a full moon date
        // When: we compute normalized phase
        val phase = calc.moonPhase(fullMoonDate)

        // Then: phase is near 0.5 (full moon)
        assertThat(phase)
            .describedAs("Full moon should normalize to ~0.5")
            .isBetween(0.45, 0.5)
    }

    @Test
    fun `moonPhase returns near 0 for new moon date`() {
        // Given: a new moon date (2026-03-19 is a new moon)
        val newMoonDate = LocalDate.of(2026, 3, 19)

        // When: we compute normalized phase
        val phase = calc.moonPhase(newMoonDate)

        // Then: phase is near 0.0 (new moon)
        assertThat(phase)
            .describedAs("New moon should normalize to ~0.0")
            .isBetween(0.0, 0.05)
    }

    @Test
    fun `moonAzimuth returns value in 0 to 360 range`() {
        // Given: a date with a known moonrise
        // When: we compute azimuth
        val azimuth = calc.moonAzimuth(fullMoonDate, lat, lng, zone)

        // Then: azimuth is a valid compass bearing
        assertThat(azimuth)
            .describedAs("Azimuth should be between 0 and 360 degrees")
            .isBetween(0.0, 360.0)
    }

    @Test
    fun `isInPhaseWindow returns true for date within window`() {
        // Given: default window (2 days before, 5 days after full moon on 2026-03-03)
        // When: we check the full moon date itself
        val result = calc.isInPhaseWindow(fullMoonDate, daysBefore = 2, daysAfter = 5)

        // Then: it's in the window
        assertThat(result).isTrue()
    }

    @Test
    fun `isInPhaseWindow returns true for date at window boundary`() {
        // Given: 2 days before full moon (2026-03-01)
        val boundaryDate = fullMoonDate.minusDays(2)

        // When: we check the boundary
        val result = calc.isInPhaseWindow(boundaryDate, daysBefore = 2, daysAfter = 5)

        // Then: boundary is included
        assertThat(result).isTrue()
    }

    @Test
    fun `isInPhaseWindow returns false for date outside window`() {
        // Given: 10 days before full moon (well outside the 2-day before window)
        val outsideDate = fullMoonDate.minusDays(10)

        // When: we check
        val result = calc.isInPhaseWindow(outsideDate, daysBefore = 2, daysAfter = 5)

        // Then: it's outside the window
        assertThat(result).isFalse()
    }

    @Test
    fun `nextFullMoon returns correct date`() {
        // Given: a date before the full moon
        val startDate = LocalDate.of(2026, 2, 15)

        // When: we find the next full moon
        val result = calc.nextFullMoon(startDate)

        // Then: it should be around 2026-03-03
        assertThat(result)
            .describedAs("Next full moon after Feb 15 should be around Mar 3")
            .isBetween(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 4))
    }

    @Test
    fun `moonrise returns null when moon does not rise`() {
        // Given: a location near the North Pole where the moon can stay below the horizon
        val polarLat = 85.0
        val polarLng = 0.0
        val polarZone = ZoneId.of("UTC")

        // When: we search across two full lunar months for a date with no moonrise
        var foundNoRise = false
        val searchStart = LocalDate.of(2026, 1, 1)
        for (dayOffset in 0L..60L) {
            val date = searchStart.plusDays(dayOffset)
            if (calc.moonrise(date, polarLat, polarLng, polarZone) == null) {
                foundNoRise = true
                break
            }
        }

        // Then: at least one date in the search range had no moonrise
        assertThat(foundNoRise)
            .describedAs("Should find at least one date with no moonrise near the North Pole")
            .isTrue()
    }
}
