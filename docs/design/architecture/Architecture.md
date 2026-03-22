# Architecture

**Date:** February 16, 2026

## Navigation

Jetpack Navigation Compose with bottom sheets as composable overlays (not nav destinations).

```
MainActivity
  └─ NavHost
       ├─ MainScreen (start)          — shows Detail Sheet (overlay) and Location Selector (overlay)
       ├─ SettingsScreen
       └─ AddLocationScreen            — parameterized: isFirstTime: Boolean
```

### First-launch flow

When no saved location exists, MainScreen renders the `FirstTimeSetup` state. The user taps "Add
Location," which navigates to `AddLocationScreen(isFirstTime = true)`. On success, the app fetches
forecast data and navigates back to MainScreen, which now shows the normal forecast view.

## State Management

One ViewModel per screen (`MainViewModel`, `SettingsViewModel`, `AddLocationViewModel`), each with a
single sealed `UiState`. ViewModels are nav-graph scoped.

```kotlin
sealed interface MainUiState {
    data object Loading : MainUiState
    data class Content(
        val locationName: String,
        val today: ForecastDay?,           // today by date; null if moon doesn't rise today
        val forecast: List<ForecastDay>,   // upcoming phase-window days after today
        val maxMoonriseTime: LocalTime,    // passed through to detail view
    ) : MainUiState
    data class Error(val locationName: String, val message: String) : MainUiState
    data object FirstTime : MainUiState
}
```

## Dependency Injection

Manual DI via an `AppContainer` created in the `Application` class. Can migrate to Hilt if
complexity grows in Phase 2.

```kotlin
class MoonriseApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(context: Context) {
    val settingsRepository = SettingsRepository(/* Room */)
    val locationRepository = LocationRepository(/* Room */)
    val weatherApi = VisualCrossingApi(/* HttpClient */)
    val forecastRepository = ForecastRepository(weatherApi /* cache */)
    val verdictEngine = VerdictEngine()
}
```

## Data Layer

### Repositories

One repository per concern:

- **`ForecastRepository`** — fetches weather data from Visual Crossing, manages disk cache
- **`LocationRepository`** — CRUD for saved locations
- **`SettingsRepository`** — reads/writes user preferences

### Storage

Room for all phases. Handles settings, locations, and cached weather data uniformly.

### Networking

Ktor Client + kotlinx.serialization. Kotlin-native, coroutine-friendly, pairs naturally.

### Caching

Online-first: fetch fresh data on launch, show loading state while fetching. Fall back to cached
data only when the network is unavailable. Cache weather API responses per location with a timestamp;
re-fetch if stale (e.g., >6 hours) or on manual refresh.

## Data Flow

```
Visual Crossing API ──→ ForecastRepository ──→ MainViewModel ──→ MainScreen
                              ↕                      ↑
                         Room Cache            VerdictEngine
                                               (applies settings
commons-suncalc ─────→ AstroCalculator ──┘      + astro + weather)

Room ──→ SettingsRepository ──→ SettingsViewModel ──→ SettingsScreen

Room ──→ LocationRepository ──→ MainViewModel / AddLocationViewModel
```

## Business Logic

### VerdictEngine

Dedicated class that evaluates whether a night is good for moonrise viewing. Pure function, no
Android dependencies.

```kotlin
class VerdictEngine {
    fun evaluate(day: ForecastDay, settings: AppSettings, inPhaseWindow: Boolean = true): VerdictResult
}

data class VerdictResult(
    val verdict: Verdict,
    val checks: VerdictChecks,
)
```

The verdict combines four constraints:

- **Phase window:** moon is within the configured window around full moon. FAIL when evaluating
  today outside the window (today is always shown; days after today that are outside the window are
  not included in the forecast at all).
- **Timing — after sunset:** moonrise after sunset with configurable tolerance (default 30 min)
- **Timing — before bedtime:** moonrise before user's configured maximum time (default 11 PM)
- **Weather:** sky clarity (clear, partly cloudy = PASS; cloudy = FAIL; unknown = UNKNOWN)

Each constraint produces a `CheckResult` (PASS, FAIL, UNKNOWN) stored in `VerdictChecks` and
displayed in the detail view checklist.

### AstroCalculator

Wraps commons-suncalc to encapsulate API quirks (e.g., the ±180° phase convention where 0° = full
moon). Provides moonrise time, sunset time, moon phase, and azimuth.

## Summary

| Aspect         | Decision                        |
|----------------|---------------------------------|
| Navigation     | Navigation Compose              |
| ViewModel      | One per screen, sealed UiState  |
| DI             | Manual (AppContainer)           |
| Repositories   | One per concern + VerdictEngine |
| Storage        | Room (all phases)               |
| Networking     | Ktor + kotlinx.serialization    |
| Data strategy  | Online-first with disk cache    |
| Business logic | Dedicated VerdictEngine class   |
