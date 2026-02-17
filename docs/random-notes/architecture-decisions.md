# Architecture & Module Design Decisions

**Date:** February 16, 2026
**Context:** UI skeleton complete, no business logic/networking/storage yet. Deciding on
architecture
before implementation begins.

## 1. Navigation

**Decision:** How to wire up screen transitions between 3 screens + 2 bottom sheets.

### Options

| Option                                                               | Pros                                                            | Cons                                                                                             |
|----------------------------------------------------------------------|-----------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| **Jetpack Navigation Compose**                                       | Standard, back stack managed, deep link support, type-safe args | Extra dependency, bottom sheet integration requires Material Navigation, heavier for a small app |
| **Manual state-based** (`when` on sealed class in a root composable) | Zero dependencies, simple for small apps, full control          | No built-in back stack, deep links need manual work, doesn't scale                               |

### Recommendation: **Navigation Compose**

Even though the app is small, Navigation Compose handles the back stack correctly, integrates with
ViewModel scoping, and is the path of least surprise for an Android app. Bottom sheets (Detail View,
Location Selector) should be composable overlays managed by ViewModel state rather than nav
destinations — they're contextual to the screen they appear on, not standalone destinations.

### Nav graph sketch

```
MainActivity
  └─ NavHost
       ├─ MainScreen (start)          — shows Detail Sheet (overlay) and Location Selector (overlay)
       ├─ SettingsScreen
       └─ AddLocationScreen            — parameterized: isFirstTime: Boolean
```

### First-launch flow

When no saved location exists, MainScreen renders the `FirstTimeSetup` state instead of the
forecast. The user taps "Add Location," which navigates to `AddLocationScreen(isFirstTime = true)`.
On success, the app fetches forecast data and navigates back to MainScreen, which now shows the
normal forecast view.

---

## 2. State Management / ViewModel Layer

**Decision:** ViewModel count, state shape, and lifecycle scoping.

### Options

| Aspect              | Option A                                                                     | Option B                                |
|---------------------|------------------------------------------------------------------------------|-----------------------------------------|
| **ViewModel count** | One per screen: `MainViewModel`, `SettingsViewModel`, `AddLocationViewModel` | Shared ViewModel for related flows      |
| **State shape**     | Single sealed `UiState` per screen (Loading / Content / Error)               | Multiple independent `StateFlow` fields |
| **Scoping**         | Nav-graph scoped (default)                                                   | Activity-scoped                         |

### Recommendation: **One ViewModel per screen, sealed UiState, nav-scoped**

- **One per screen** keeps responsibilities clear. The screens are independent enough that sharing
  state would add coupling without benefit.
- **Sealed UiState** makes it impossible to display data and a loading spinner simultaneously —
  the UI is always in exactly one state. Example:

  ```kotlin
  sealed interface MainUiState {
      data object Loading : MainUiState
      data class Content(val today: ForecastDay?, val forecast: List<ForecastDay>) : MainUiState
      data class Error(val message: String) : MainUiState
      data object FirstTime : MainUiState
  }
  ```

- **Nav-scoped** is the default and correct for this app — each screen's ViewModel lives as long
  as that screen is in the back stack.

---

## 3. Dependency Injection

**Decision:** How to provide dependencies (repositories, API clients) to ViewModels.

### Options

| Option                                            | Pros                                                        | Cons                                                                    |
|---------------------------------------------------|-------------------------------------------------------------|-------------------------------------------------------------------------|
| **Hilt**                                          | Google-recommended, ViewModel integration, scoping built-in | Annotation processing, Dagger learning curve, boilerplate for small app |
| **Manual DI** (AppContainer in Application class) | Zero dependencies, transparent, easy to understand          | Wiring by hand, no scope management, verbose as app grows               |
| **Koin**                                          | Lightweight, Kotlin DSL, no code gen                        | Less "official", runtime errors instead of compile-time                 |

### Recommendation: **Manual DI**

For Phase 1 with ~3 ViewModels and ~3 repositories, manual DI is straightforward and avoids adding
annotation processing. A simple `AppContainer` created in the `Application` class and accessed via
`ViewModelProvider.Factory` keeps things transparent. Can migrate to Hilt later if complexity grows
in Phase 2.

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

---

## 4. Data / Repository Layer

**Decision:** Repository granularity, use-case layer, and caching strategy.

### Repository granularity

| Option                                                                                      | Pros                                              | Cons                         |
|---------------------------------------------------------------------------------------------|---------------------------------------------------|------------------------------|
| **One repo per concern** (`ForecastRepository`, `LocationRepository`, `SettingsRepository`) | Clear boundaries, single responsibility, testable | More classes for a small app |
| **Single repo for MVP**                                                                     | Less code                                         | Becomes a god class quickly  |

### Use-case / interactor layer

| Option                              | Pros                                             | Cons                                                   |
|-------------------------------------|--------------------------------------------------|--------------------------------------------------------|
| **Dedicated `VerdictEngine` class** | Highly testable, reusable, clear domain boundary | One more layer                                         |
| **Logic in ViewModel**              | Fewer classes                                    | Hard to test, mixes UI orchestration with domain logic |
| **Logic in Repository**             | Data + logic together                            | Repository becomes too smart                           |

### Caching strategy

The PRD specifies caching for offline viewing. Visual Crossing's free tier is 1,000 records/day, so
minimizing calls matters.

| Option                             | Pros                                         | Cons                  |
|------------------------------------|----------------------------------------------|-----------------------|
| **In-memory cache with TTL**       | Simple, fast                                 | Lost on process death |
| **Disk cache (JSON file or Room)** | Survives process death, true offline support | More complexity       |
| **Both (memory + disk)**           | Best UX — instant loads + offline            | Most complex          |

### Recommendation

- **One repo per concern** — the boundaries are natural (weather data, locations, settings).
- **Dedicated `VerdictEngine`** — the good-night algorithm is the core domain logic and deserves
  isolated unit testing. It takes a `ForecastDay` + `AppSettings` and returns a `Verdict` with
  reason.
- **Disk cache for weather data** — store the last API response per location with a timestamp.
  Re-fetch if stale (e.g., >6 hours) or on manual refresh. For Phase 1 with a single location,
  even a simple JSON file in internal storage would work, but Room sets us up for Phase 2.

### Data flow

```
Visual Crossing API ──→ ForecastRepository ──→ MainViewModel ──→ MainScreen
                              ↕                      ↑
                         Disk Cache            VerdictEngine
                                               (applies settings
commons-suncalc ─────→ AstroCalculator ──┘      + astro + weather)

Preferences DataStore ──→ SettingsRepository ──→ SettingsViewModel ──→ SettingsScreen

Local Storage ──→ LocationRepository ──→ MainViewModel / AddLocationViewModel
```

---

## 5. Networking

**Decision:** HTTP client and JSON parsing library.

### HTTP client

| Option                    | Pros                                                                   | Cons                                       |
|---------------------------|------------------------------------------------------------------------|--------------------------------------------|
| **Retrofit + OkHttp**     | Industry standard, suspend support, interceptors, great error handling | Two dependencies                           |
| **Ktor Client**           | Kotlin-first, multiplatform, lightweight                               | Less Android-specific tooling              |
| **Raw HttpURLConnection** | Zero dependencies, already used in tests                               | Verbose, manual threading, no interceptors |

### JSON parsing

| Option                    | Pros                                              | Cons                                             |
|---------------------------|---------------------------------------------------|--------------------------------------------------|
| **kotlinx.serialization** | Kotlin-native, multiplatform, compile-time safe   | Plugin setup, less forgiving with malformed JSON |
| **Moshi**                 | Kotlin-friendly, good null safety, codegen option | Another dependency                               |
| **Gson**                  | Ubiquitous, simple                                | Weak Kotlin support (nulls, defaults)            |

### Recommendation: **Ktor Client + kotlinx.serialization**

Ktor is Kotlin-native with built-in coroutine support and pairs naturally with
kotlinx.serialization. For a single-API app, it's lighter than Retrofit + OkHttp + a separate JSON
library. Both are JetBrains-maintained and work well together.

Alternative: Retrofit + Moshi is equally valid and more conventional in the Android ecosystem. This
is a low-stakes decision — either works well.

---

## 6. Business Logic Placement

**Decision:** Where does the "good night" verdict algorithm live?

The algorithm combines:

- Moon phase from commons-suncalc (is it within the configured window around full moon?)
- Timing: moonrise after sunset (with tolerance) and before bedtime
- Weather: cloud cover / conditions

### Recommendation: **Dedicated `VerdictEngine` class**

```kotlin
class VerdictEngine {
    fun evaluate(day: ForecastDay, settings: AppSettings): VerdictResult
}

data class VerdictResult(
    val verdict: Verdict,
    val reason: String?,        // Human-readable explanation for BAD verdicts
    val checks: VerdictChecks,  // Individual pass/fail for the detail view checklist
)
```

This is the most testable approach — pure function, no Android dependencies, easily covered by
unit tests with different combinations of phase/timing/weather. The ViewModel calls it after
assembling data from the repositories.

Similarly, commons-suncalc calls should be wrapped in an `AstroCalculator` class rather than called
directly from the repository, for testability and to encapsulate the library's API quirks (e.g.,
the ±180° phase convention).

---

## Summary of Recommendations

| # | Decision       | Recommendation                  | Rationale                                       |
|---|----------------|---------------------------------|-------------------------------------------------|
| 1 | Navigation     | Navigation Compose              | Standard, manages back stack, ViewModel scoping |
| 2 | ViewModel      | One per screen, sealed UiState  | Clear ownership, impossible invalid states      |
| 3 | DI             | Manual (AppContainer)           | Sufficient for Phase 1, zero dependencies       |
| 4 | Repositories   | One per concern + VerdictEngine | Clean boundaries, testable domain logic         |
| 5 | Networking     | Ktor + kotlinx.serialization    | Kotlin-native, lightweight, coroutine-friendly  |
| 6 | Business logic | Dedicated VerdictEngine class   | Pure function, highly testable, no Android deps |

## Resolved Questions

- **Storage:** Room for all phases. No DataStore — Room handles settings, locations, and cached
  weather data uniformly. Avoids a Phase 2 migration.

- **Online-first:** Fetch fresh data on launch, show loading state while fetching. Fall back to
  cached data only when the network is unavailable.
