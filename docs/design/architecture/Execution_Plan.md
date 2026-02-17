# MVP Execution Plan

**Date:** February 16, 2026

This document sequences the MVP build into incremental, testable steps. Each step produces working,
tested code before the next step begins. The ordering is inside-out: pure domain logic first, then
infrastructure, then wiring.

**Phase 1 user stories covered:** US-001, US-003, US-004, US-005, US-006, US-007, US-013

---

## Step 1: Dependencies

**Goal:** Add all MVP libraries to the build so subsequent steps can import them immediately.

### What to build

Add Room, Ktor, kotlinx.serialization, Navigation Compose, ViewModel/Lifecycle, and Coroutines to
the Gradle version catalog and app build file.

### Files to modify

- `gradle/libs.versions.toml` — add version entries, library aliases, and plugin aliases for:
    - Room (runtime, compiler via KSP)
    - Ktor Client (core, CIO engine, content-negotiation, kotlinx-serialization)
    - kotlinx.serialization (json)
    - Navigation Compose
    - Lifecycle ViewModel Compose
    - kotlinx.coroutines (core, android, test)
- `build.gradle.kts` (project) — apply KSP and kotlinx.serialization plugins (with `apply false`)
- `app/build.gradle.kts` — apply KSP and kotlinx.serialization plugins, add all new
  `implementation` and `ksp` dependencies, add `testImplementation` for coroutines-test

### Tests to write

None (build-only step).

### Verification

- Project syncs without errors
- `scripts/run.sh gradle-sync ./gradlew assembleDebug` succeeds

---

## Step 2: Domain Logic — AstroCalculator

**Goal:** Wrap commons-suncalc behind a clean interface that hides API quirks (±180 phase
convention, builder patterns). Pure Kotlin, no Android dependencies.

**User stories:** Supports US-003, US-004, US-006 (moonrise time, sunset time, azimuth, phase)

### What to build

`AstroCalculator` class with methods:

- `moonrise(date: LocalDate, lat: Double, lng: Double): LocalTime?` — returns null if moon doesn't
  rise
- `sunset(date: LocalDate, lat: Double, lng: Double): LocalTime`
- `moonPhase(date: LocalDate): Double` — normalized to 0.0 = new moon, 0.5 = full moon (converting
  from commons-suncalc's 0 = full convention)
- `moonAzimuth(date: LocalDate, lat: Double, lng: Double): Double` — degrees 0-360
- `isInPhaseWindow(date: LocalDate, daysBefore: Int, daysAfter: Int): Boolean` — true if date falls
  within the configured window around full moon
- `nextFullMoon(after: LocalDate): LocalDate`

All methods are pure functions taking explicit parameters (no stored state).

### Files to create

- `app/src/main/kotlin/.../domain/AstroCalculator.kt`
- `app/src/test/kotlin/.../domain/AstroCalculatorTest.kt`

### Tests to write

- Moonrise time for a known date/location matches expected value (from existing smoke test data:
  Seattle, 2026-03-03)
- Sunset time for a known date/location matches expected value
- Phase normalization: commons-suncalc phase 0 (full moon) maps to 0.5; ±180 (new moon) maps to
  0.0
- Azimuth returns value in 0-360 range
- `isInPhaseWindow` returns true for dates within window, false outside
- `nextFullMoon` returns the correct date
- Edge case: moonrise returns null when moon doesn't rise at that location/date

### Verification

`scripts/run.sh astro-tests ./gradlew testDebugUnitTest --tests "*.domain.AstroCalculatorTest"`
passes.

---

## Step 3: Domain Logic — VerdictEngine

**Goal:** Implement the good/bad night evaluation as a pure function. No Android dependencies.

**User stories:** US-003, US-004, US-007 (good/bad verdict, reason for bad verdict)

### What to build

`VerdictEngine` class with a single method:

```kotlin
fun evaluate(day: ForecastDay, settings: AppSettings): VerdictResult
```

The method evaluates three constraints:

1. **Timing — moonrise after sunset:** PASS if moonrise >= sunset - tolerance; FAIL otherwise
2. **Timing — moonrise before bedtime:** PASS if moonrise <= maxMoonriseTime; FAIL otherwise
3. **Weather — sky clarity:** PASS if CLEAR or PARTLY_CLOUDY; FAIL if CLOUDY; UNKNOWN if weather
   data unavailable

Overall verdict: GOOD if all checks are PASS or UNKNOWN; BAD if any check is FAIL.

`VerdictChecks.badgeReason()` already exists in the model — VerdictEngine populates the checks.

### Files to create

- `app/src/main/kotlin/.../domain/VerdictEngine.kt`
- `app/src/test/kotlin/.../domain/VerdictEngineTest.kt`

### Tests to write

- Good night: moonrise after sunset, before bedtime, clear weather → GOOD
- Bad: moonrise before sunset (beyond tolerance) → BAD, reason "Moon rises before sunset"
- Bad: moonrise after bedtime → BAD, reason "Moon rises too late"
- Bad: cloudy weather → BAD, reason "Poor weather"
- Multiple failures: all reasons reported in checks
- Unknown weather: moonrise timing is good → GOOD (weather unknown doesn't block)
- Edge case: moonrise exactly at sunset-tolerance boundary → PASS
- Edge case: moonrise exactly at bedtime → PASS
- Settings variations: custom tolerance and bedtime values are respected

### Verification

`scripts/run.sh verdict-tests ./gradlew testDebugUnitTest --tests "*.domain.VerdictEngineTest"`
passes.

---

## Step 4: Networking — VisualCrossingApi Client

**Goal:** Type-safe API client using Ktor + kotlinx.serialization, tested against recorded fixtures.

**User stories:** US-003, US-004, US-005 (weather data for today and forecast)

### What to build

`VisualCrossingApi` class that:

- Takes an `HttpClient` (injected for testability)
- Fetches 15-day timeline forecast for a lat/lng
- Deserializes JSON response into Kotlin data classes via kotlinx.serialization
- Returns a domain-friendly result (list of daily weather data)

API response model classes (annotated with `@Serializable`):

- `TimelineResponse` — top-level response with `days: List<DayResponse>`
- `DayResponse` — daily weather data (datetime, tempmax, tempmin, temp, feelslike, windspeed,
  cloudcover, precip, precipprob, preciptype, conditions, moonphase, sunrise, sunset,
  hours: List<HourResponse>)
- `HourResponse` — hourly data (datetime, cloudcover, etc.)

### Files to create

- `app/src/main/kotlin/.../network/VisualCrossingApi.kt`
- `app/src/main/kotlin/.../network/model/TimelineResponse.kt` (response DTOs)
- `app/src/test/kotlin/.../network/VisualCrossingApiTest.kt` (replay tests)

### Files to modify

- Existing smoke test can remain as-is for live integration testing

### Tests to write (replay from fixtures)

- Deserialize recorded fixture `visual-crossing-timeline-response.json` into `TimelineResponse`
- Verify day count (15-16 days)
- Verify required fields are present and correctly typed (temp, cloudcover, windspeed, etc.)
- Verify hourly data is present for near-term days
- Verify precipitation fields parse correctly (including null/empty preciptype)

### Verification

`scripts/run.sh api-tests ./gradlew testDebugUnitTest --tests "*.network.VisualCrossingApiTest"`
passes.

---

## Step 5: Storage — Room Database

**Goal:** Local persistence for locations, settings, and cached weather data.

**User stories:** US-001 (save location), US-013 (persist settings)

### What to build

Room database with three tables:

**Entities:**

- `LocationEntity` — id (auto-generated primary key), name, cityState (nullable), latitude,
  longitude, isActive (boolean, only one active at a time)
- `SettingsEntity` — single-row table (id = 1), daysBeforeFullMoon, daysAfterFullMoon,
  forecastPeriodMonths, maxMoonriseHour, maxMoonriseMinute, beforeSunsetToleranceMin, useMetric
- `WeatherCacheEntity` — locationId, date, jsonBlob (serialized DayResponse), fetchedAt (timestamp)

**DAOs:**

- `LocationDao` — insert, getAll, getActive, setActive, delete, count
- `SettingsDao` — getSettings, insertOrUpdate (UPSERT)
- `WeatherCacheDao` — insertAll, getForLocation, deleteStaleEntries, deleteForLocation

**Database:**

- `MoonriseDatabase` (abstract RoomDatabase subclass)

### Files to create

- `app/src/main/kotlin/.../storage/entity/LocationEntity.kt`
- `app/src/main/kotlin/.../storage/entity/SettingsEntity.kt`
- `app/src/main/kotlin/.../storage/entity/WeatherCacheEntity.kt`
- `app/src/main/kotlin/.../storage/dao/LocationDao.kt`
- `app/src/main/kotlin/.../storage/dao/SettingsDao.kt`
- `app/src/main/kotlin/.../storage/dao/WeatherCacheDao.kt`
- `app/src/main/kotlin/.../storage/MoonriseDatabase.kt`

### Tests to write

Room DAO tests require Android instrumented tests or Robolectric. For MVP, defer DAO testing to
integration tests via the repository layer (Step 6). The Room schema itself is validated at compile
time by the annotation processor.

### Verification

- Project compiles: `scripts/run.sh room-compile ./gradlew assembleDebug`
- KSP generates Room implementation classes without errors

---

## Step 6: Repositories

**Goal:** Wire API + Room + domain logic into repository classes that ViewModels consume.

**User stories:** US-001, US-003, US-004, US-005, US-013

### What to build

**ForecastRepository:**

- `getForecast(location: SavedLocation, settings: AppSettings): Flow<List<ForecastDay>>` or
  suspend function
- Fetches weather from VisualCrossingApi (online-first)
- Falls back to WeatherCacheDao if network fails
- Computes astro data via AstroCalculator for the full forecast period (default 3 months)
- Filters to phase window dates via `AstroCalculator.isInPhaseWindow()`
- Merges weather data (available ~14 days) with astro-only data (remaining dates)
- Runs VerdictEngine on each day
- Caches weather responses to Room

**LocationRepository:**

- `getActiveLocation(): Flow<SavedLocation?>`
- `addLocation(name, cityState, lat, lng): SavedLocation`
- `setActive(locationId)`
- `getLocationCount(): Int`
- Maps between `LocationEntity` and `SavedLocation`

**SettingsRepository:**

- `getSettings(): Flow<AppSettings>`
- `updateSettings(settings: AppSettings)`
- Maps between `SettingsEntity` and `AppSettings`
- Returns `AppSettings()` defaults if no row exists yet

### Files to create

- `app/src/main/kotlin/.../repository/ForecastRepository.kt`
- `app/src/main/kotlin/.../repository/LocationRepository.kt`
- `app/src/main/kotlin/.../repository/SettingsRepository.kt`
- `app/src/test/kotlin/.../repository/ForecastRepositoryTest.kt`

### Tests to write (ForecastRepository)

- Given recorded weather fixture + known location + default settings → produces correct
  ForecastDay list
- Days outside phase window are excluded
- Days within weather range have weather data populated; days beyond have
  `WeatherCondition.UNKNOWN`
- VerdictEngine is applied correctly (good/bad matches expectations)
- Astro fields (moonrise, sunset, azimuth) are populated for all days

LocationRepository and SettingsRepository are thin DAO wrappers — tested indirectly through
ViewModel integration or manually.

### Verification

`scripts/run.sh repo-tests ./gradlew testDebugUnitTest --tests "*.repository.*Test"` passes.

---

## Step 7: Dependency Injection — AppContainer

**Goal:** Wire all dependencies together in a single place.

**User stories:** None directly; infrastructure for all features.

### What to build

`MoonriseApplication` (extends `Application`):

- Creates `AppContainer` in `onCreate()`

`AppContainer(context: Context)`:

- Instantiates `MoonriseDatabase` (Room)
- Instantiates `VisualCrossingApi` with configured `HttpClient`
- Instantiates `AstroCalculator`
- Instantiates `VerdictEngine`
- Instantiates `LocationRepository(locationDao)`
- Instantiates `SettingsRepository(settingsDao)`
- Instantiates `ForecastRepository(api, cache, astro, verdict)`

### Files to create

- `app/src/main/kotlin/.../MoonriseApplication.kt`
- `app/src/main/kotlin/.../di/AppContainer.kt`

### Files to modify

- `AndroidManifest.xml` — set `android:name=".MoonriseApplication"` on `<application>`

### Tests to write

None (wiring-only step; tested by the app launching successfully).

### Verification

- App compiles and launches on emulator without crashing
- `scripts/run.sh build-debug ./gradlew assembleDebug` succeeds

---

## Step 8: ViewModels

**Goal:** State management layer between repositories and UI.

**User stories:** US-001, US-003, US-004, US-005, US-006, US-007, US-013

### What to build

**MainViewModel:**

```kotlin
sealed interface MainUiState {
    data object Loading : MainUiState
    data class Content(val today: ForecastDay?, val forecast: List<ForecastDay>) : MainUiState
    data class Error(val message: String) : MainUiState
    data object FirstTime : MainUiState
}
```

- On init: check if any location exists → FirstTime or load forecast
- `loadForecast()` — fetches from ForecastRepository, emits Content or Error
- `refresh()` — re-fetches data
- Observes active location and settings changes

**SettingsViewModel:**

```kotlin
sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Content(val settings: AppSettings) : SettingsUiState
}
```

- Loads current settings on init
- `updateDaysBefore(value: Int)`, `updateDaysAfter(value: Int)`, etc. — individual update methods
- Persists each change immediately

**AddLocationViewModel:**

```kotlin
sealed interface AddLocationUiState {
    data object Idle : AddLocationUiState
    data object Saving : AddLocationUiState
    data class Error(val message: String) : AddLocationUiState
    data object Success : AddLocationUiState
}
```

- `saveLocation(name, cityState, lat, lng)` — validates, saves via LocationRepository, sets as
  active
- For MVP (single location), simply saves the one location

### Files to create

- `app/src/main/kotlin/.../viewmodel/MainViewModel.kt`
- `app/src/main/kotlin/.../viewmodel/SettingsViewModel.kt`
- `app/src/main/kotlin/.../viewmodel/AddLocationViewModel.kt`

### Tests to write

- MainViewModel: emits FirstTime when no location exists
- MainViewModel: emits Loading then Content on successful forecast load
- MainViewModel: emits Error when repository fails
- SettingsViewModel: emits current settings on load
- SettingsViewModel: persists changes
- AddLocationViewModel: emits Success after saving valid location

### Verification

`scripts/run.sh viewmodel-tests ./gradlew testDebugUnitTest --tests "*.viewmodel.*Test"` passes.

---

## Step 9: Navigation

**Goal:** Wire up screen transitions using Navigation Compose.

**User stories:** US-001 (first-launch flow), US-005 (detail view), US-013 (settings access)

### What to build

Navigation graph in a `MoonriseNavHost` composable:

```
Routes:
  "main"         → MainScreen (start destination)
  "settings"     → SettingsScreen
  "addLocation/{isFirstTime}" → AddLocationScreen
```

Bottom sheets are composable overlays managed by `MainScreen` state (not nav destinations):

- Detail Sheet — shown when user taps a forecast item
- Location Selector — shown when user taps location name in top bar (Phase 2, but wire the
  overlay mechanism now)

### Files to create

- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt`
- `app/src/main/kotlin/.../navigation/Routes.kt` (route constants)

### Files to modify

- `MainActivity.kt` — replace sample data rendering with `MoonriseNavHost`

### Tests to write

None (navigation is tested manually and via UI tests later).

### Verification

- App launches, shows FirstTime state (no location saved yet)
- Navigating to Settings and back works
- Navigating to AddLocation and back works

---

## Step 10: Wiring — Connect ViewModels to UI

**Goal:** Replace sample data in composables with live ViewModel state.

**User stories:** US-003, US-004, US-005, US-006, US-007, US-013

### What to build

Update screen composables to observe ViewModel state:

**MainScreen:**

- Obtain `MainViewModel` from navigation back stack entry
- Collect `uiState` as Compose state
- Render appropriate sub-composable based on sealed state (Loading, Content, Error, FirstTime)
- Pass `onDayClick` to show DetailSheet overlay
- Pass `onSettingsClick` to navigate to Settings
- Pass `onAddLocationClick` to navigate to AddLocation

**SettingsScreen:**

- Obtain `SettingsViewModel`
- Collect settings state
- Wire stepper/toggle callbacks to ViewModel update methods
- Navigate back on back press

**AddLocationScreen:**

- Obtain `AddLocationViewModel`
- Wire save button to ViewModel
- On Success state, navigate back (triggers MainScreen to reload)

### Files to modify

- `app/src/main/kotlin/.../screens/MainScreen.kt` — add ViewModel integration
- `app/src/main/kotlin/.../screens/SettingsScreen.kt` — add ViewModel integration
- `app/src/main/kotlin/.../screens/AddLocationScreen.kt` — add ViewModel integration
- `app/src/main/kotlin/.../components/TopBar.kt` — wire settings and location callbacks

### Tests to write

None for this step (UI wiring tested manually). Preview composables continue to work with sample
data.

### Verification

- App compiles and runs on emulator
- FirstTime screen appears on fresh install
- After adding location, MainScreen shows Loading then Content
- Settings changes are persisted and reflected in forecast
- Detail sheet opens when tapping a forecast item
- All existing preview composables still render correctly

---

## Step 11: First-Launch Flow

**Goal:** Complete the end-to-end first-time user experience.

**User stories:** US-001 (add first location)

### What to build

Wire the full flow:

1. App launches → MainViewModel checks location count → emits `FirstTime`
2. MainScreen renders `FirstTimeSetup` composable
3. User taps "Add Location" → navigates to `AddLocationScreen(isFirstTime = true)`
4. User enters city or coordinates, taps Save
5. AddLocationViewModel saves location, sets as active, emits Success
6. App navigates back to MainScreen
7. MainViewModel detects active location, fetches forecast, emits Content
8. User sees today's conditions and upcoming forecast

### Files to modify

Minor adjustments to files from Steps 9-10 as needed to complete the flow. No new files expected.

### Tests to write

None beyond what's already covered. This step is integration verification.

### Verification

- Fresh install (clear app data): full flow from FirstTime → AddLocation → MainScreen with live
  data
- Subsequent launches skip first-time flow and go straight to forecast
- API key is loaded correctly at runtime (from BuildConfig or local properties)

---

## Cross-Cutting Concerns

These items apply across multiple steps and should be addressed as they come up:

### API Key Management

The Visual Crossing API key is currently in `secrets.properties` for tests. For the running app,
inject it via `BuildConfig` field (read from `local.properties` or `secrets.properties` in the app
build file).

### Error Handling

- Network errors → show Error state with retry button (already in UI skeleton)
- Invalid location → show error in AddLocationScreen (already in UI skeleton)
- Empty forecast (no days in phase window) → show EmptyForecastMessage (already in UI skeleton)

### Unit System

The `AppSettings.useMetric` flag affects display formatting. The existing composables already handle
unit-aware formatting. The API should fetch data in both unit systems or convert as needed. Visual
Crossing supports a `unitGroup` parameter (`us` or `metric`).

---

## Summary

| Step | What                       | New Files | Key Tests                | Depends On |
|------|----------------------------|-----------|--------------------------|------------|
| 1    | Dependencies               | 0         | Build compiles           | —          |
| 2    | AstroCalculator            | 2         | Phase, times, azimuth    | 1          |
| 3    | VerdictEngine              | 2         | Good/bad verdicts        | 1          |
| 4    | VisualCrossingApi client   | 3         | Fixture deserialization  | 1          |
| 5    | Room database              | 7         | Compile-time validation  | 1          |
| 6    | Repositories               | 4         | Forecast assembly        | 2, 3, 4, 5 |
| 7    | AppContainer + Application | 2         | App launches             | 6          |
| 8    | ViewModels                 | 3         | State transitions        | 6          |
| 9    | Navigation                 | 2         | Manual screen navigation | 7, 8       |
| 10   | Wiring UI to ViewModels    | 0         | Manual end-to-end        | 9          |
| 11   | First-launch flow          | 0         | Manual end-to-end        | 10         |
