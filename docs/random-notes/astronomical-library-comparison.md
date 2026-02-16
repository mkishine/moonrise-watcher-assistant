# Astronomical Library Comparison

**Date:** February 16, 2026

Evaluation of JVM/Kotlin-compatible libraries for PRD Technical Requirements (astronomical
calculations: moonrise/sunset times, moon phase, azimuth). Decision recorded in PRD Decision Log.

## Evaluation Criteria (from PRD)

- Moonrise time (req 2.1, 2.2, 6.2.2, 6.4.2)
- Sunset time (req 2.2, 6.2.1, 6.4.1)
- Moon phase — full moon window calculation (req 1.1)
- Moonrise azimuth in degrees (req 7.3)
- Must run on Android (minSdk 26, native java.time)
- Available on Maven Central (reliable builds)
- Lightweight, zero or minimal dependencies

## Comparison Table

| Criteria                  | commons-suncalc              | Kastro                  | Astronomy Engine              | MoonLocatorLibrary   |
|---------------------------|------------------------------|-------------------------|-------------------------------|----------------------|
| **Moonrise time**         | Yes (`MoonTimes`)            | Yes                     | Yes (`searchRiseSet`)         | Yes                  |
| **Sunset time**           | Yes (`SunTimes`)             | Yes                     | Yes (`searchRiseSet`)         | No (moon-only)       |
| **Moon phase**            | Yes (`MoonIllumination`)     | Yes (named phases only) | Yes (phase angle + fraction)  | Yes                  |
| **Full moon date calc**   | Yes (`MoonPhase`)            | Unknown                 | Yes                           | Unknown              |
| **Moonrise azimuth**      | Yes (`MoonPosition`)         | No                      | Yes (horizon transforms)      | Yes                  |
| **Illumination fraction** | Yes                          | No                      | Yes                           | Yes                  |
| **All 4 requirements**    | **Yes**                      | No (missing azimuth)    | Yes                           | No (missing sunset)  |
| **Maven Central**         | **Yes**                      | Yes                     | No (JitPack only)             | Yes                  |
| **Dependencies**          | **Zero**                     | kotlinx-datetime        | Zero                          | Unknown              |
| **Android documented**    | **Yes (API 26+)**            | Likely (KMP)            | Not documented                | Likely (Java 17+)    |
| **License**               | Apache 2.0                   | Apache 2.0              | MIT                           | MIT                  |
| **Maturity**              | **High** (years of releases) | Low (pre-1.0, v0.5.0)   | High (multi-language project) | Very low (4 commits) |
| **Latest release**        | v3.11 (June 2024)            | v0.5.0                  | v2.1.19 (Dec 2023)            | v1.0.0 (Feb 2026)    |
| **API style**             | Builder pattern              | Sequence-based          | General-purpose, lower-level  | Unknown              |

## Candidate Details

### commons-suncalc

`org.shredzone.commons:commons-suncalc:3.11` — Apache 2.0

- Java library, works seamlessly from Kotlin. Zero runtime dependencies.
- Explicitly documents Android API 26+ compatibility.
- Actively maintained with consistent releases across v2.x and v3.x branches.
- Builder-style API maps cleanly to the app's use cases:
  ```kotlin
  val moonrise = MoonTimes.compute().on(date).at(lat, lng).execute().rise
  val sunset = SunTimes.compute().on(date).at(lat, lng).execute().set
  val azimuth = MoonPosition.compute().on(dateTime).at(lat, lng).execute().azimuth
  val phase = MoonIllumination.compute().on(date).execute().phase
  val nextFullMoon = MoonPhase.compute().phase(MoonPhase.Phase.FULL_MOON).execute().time
  ```
- Accuracy: ~1 minute for rise/set times.
- Kastro (see below) is actually built on top of this library internally.

### Kastro

`dev.jamesyox:kastro:0.5.0` — Apache 2.0

- Kotlin Multiplatform library built on commons-suncalc internally.
- Exposes a Sequence-based API, but only a subset of commons-suncalc features.
- Missing: moonrise azimuth, illumination fraction.
- Pre-1.0 version with small community.
- Depends on kotlinx-datetime.

### Astronomy Engine

`com.github.cosinekitty:astronomy:v2.1.19` — MIT

- Full-featured astronomical engine (planets, eclipses, transits, etc.).
- Covers all four requirements but the API is general-purpose and lower-level.
- **Only available via JitPack**, not Maven Central. JitPack has known reliability issues (builds
  from source on demand, occasional timeouts).
- Multi-language project (C, C#, Python, JS, Kotlin) with regular releases.

### MoonLocatorLibrary

`io.github.dgrims3:moon-library:1.0.0` — MIT

- Moon-only library — **does not provide sunset times**.
- Brand new: 4 total commits, single developer, first release February 2026.
- Too immature for a dependency.

## Decision

**commons-suncalc 3.11** selected. It is the only library that satisfies all four criteria:

1. Covers all required calculations (moonrise, sunset, phase, azimuth)
2. Available on Maven Central with zero dependencies
3. Explicitly supports Android API 26+
4. Mature and actively maintained

No trade-offs needed — it is clearly the best fit.

## Caveat

This analysis is based on AI training data (cutoff May 2025) plus web searches. Maven Central
availability and version numbers should be verified during dependency addition:

- https://github.com/shred/commons-suncalc
- https://shredzone.org/maven/commons-suncalc/
- https://central.sonatype.com/artifact/org.shredzone.commons/commons-suncalc
