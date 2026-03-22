# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project Overview

This is the **Moonrise Watcher Assistant** - an Android app to help users identify optimal nights
for moonrise viewing by combining moon phase data, timing constraints, and weather forecasts.

**Current Status:** MVP complete. Full app with domain logic, networking, Room storage, ViewModels,
navigation, and UI. 48 unit tests pass. End-to-end flow functional: first-time setup → live
forecast with today's conditions and upcoming phase-window nights.

## Repository Structure

```
app/src/main/kotlin/name/kishinevsky/michael/moonriseassistant/
├── model/                 # Data classes (ForecastDay, AppSettings, enums)
├── domain/                # AstroCalculator, VerdictEngine (pure Kotlin, no Android deps)
├── network/               # VisualCrossingApi + model/ (Ktor + kotlinx.serialization DTOs)
├── storage/               # Room database, DAOs, entities
├── repository/            # ForecastRepository, LocationRepository, SettingsRepository
├── viewmodel/             # MainViewModel, SettingsViewModel, AddLocationViewModel + UiState
├── navigation/            # MoonriseNavHost, Routes
├── di/                    # AppContainer (manual DI)
├── location/              # GeocodingService (city name → coordinates)
├── ui/theme/              # Material 3 theme (Color, Type, Theme)
├── components/            # Reusable composables (TopBar, TodaySection, ForecastList*, DetailSheet, etc.)
├── screens/               # Screen-level composables (MainScreen, SettingsScreen, AddLocationScreen)
├── preview/               # Sample data and @Preview composables
├── MoonriseApplication.kt # Application subclass; creates AppContainer
└── MainActivity.kt        # Entry point; hosts MoonriseNavHost
docs/
├── requirements/          # PRD with feature specifications
├── design/
│   ├── user-stories/      # User stories with acceptance criteria
│   ├── user-flows/        # Mermaid diagrams showing navigation
│   ├── wireframes/        # ASCII wireframes for screen layouts
│   └── architecture/      # Architecture decisions and data flow
```

## Key Documents

| Document                                                | Purpose                                                |
|---------------------------------------------------------|--------------------------------------------------------|
| `docs/requirements/Moonrise_App_PRD.md`                 | Full requirements, technical specs, development phases |
| `docs/design/user-stories/User_Stories.md`              | User-focused features with acceptance criteria         |
| `docs/design/user-flows/User_Flows.md`                  | Navigation flowcharts in Mermaid format                |
| `docs/design/wireframes/Main_Screen_Wireframe.md`       | ASCII wireframes for Main Screen layout                |
| `docs/design/wireframes/Detail_View_Wireframe.md`       | ASCII wireframes for Detail View bottom sheet          |
| `docs/design/wireframes/Settings_Wireframe.md`          | ASCII wireframes for Settings screen                   |
| `docs/design/wireframes/Add_Location_Wireframe.md`      | ASCII wireframes for Add Location screen               |
| `docs/design/wireframes/Location_Selector_Wireframe.md` | ASCII wireframes for Location Selector bottom sheet    |
| `docs/design/architecture/Architecture.md`              | Architecture decisions, data flow, module design       |
| `docs/design/architecture/Execution_Plan.md`            | Sequenced MVP build steps with test criteria           |

## Development Phases

- **Phase 1 (MVP):** Single location, 3-month forecast period (weather for ~14 days, astro-only
  beyond), good/bad indicators, list + detail views
- **Phase 2:** Multiple saved locations, per-location settings, enhanced weather visualization

## Technology Stack

| Component        | Version    | Notes                                         |
|------------------|------------|-----------------------------------------------|
| AGP              | 9.0.1      | Built-in Kotlin support                       |
| Kotlin (via AGP) | 2.2.10     | Bundled with AGP 9.0                          |
| Compose BOM      | 2026.02.00 | Material 3, Compose UI                        |
| Gradle           | 9.1.0      | Minimum for AGP 9.0                           |
| compileSdk       | 36         | Android 16                                    |
| minSdk           | 26         | Android 8.0 (native java.time)                |
| JDK              | 21         | JetBrains vendor, via Gradle daemon toolchain |

- **Astronomical calculations:** commons-suncalc 3.11 (moonrise/sunset times, moon phase, azimuth)
- **Weather API:** Visual Crossing Timeline Weather API
- **Storage:** Local device storage for locations and preferences
- **Testing:** JUnit 5 (junit-jupiter) + AssertJ

## Domain Concepts

- **Good night criteria:** Moon phase (2 days before to 5 days after full moon), moonrise after
  sunset (with configurable tolerance, default 30 min) and before user's bedtime (default 11 PM),
  clear weather
- **Azimuth:** Compass direction of moonrise in degrees (0°=N, 90°=E, 180°=S, 270°=W)
- **Moon phase (commons-suncalc):** `MoonIllumination.getPhase()` returns -180° to +180° where
  **0° = full moon** and ±180° = new moon. This is the opposite of some other libraries/APIs
  (e.g., Visual Crossing uses 0 = new moon, 0.5 = full moon).
- **Phase window:** Only the 7-day window around full moon is shown in the upcoming forecast list
  (days outside the window are hidden, not grayed out). **Today is always shown** in the Today
  section regardless of phase window — if today is outside the window, VerdictEngine marks the
  phase window check as FAIL and the verdict is BAD.

## Design Document Formats

- **Wireframes:** ASCII art in Markdown. Each wireframe follows a standard structure: Screen
  Elements table, Sample Data, ASCII layout(s), and Annotations with detailed specs.
- **User flows:** Mermaid diagrams in Markdown.

## Rendering Mermaid Diagrams

The user flow diagrams use Mermaid syntax. To render locally:

```bash
npm install -g @mermaid-js/mermaid-cli
mmdc -i docs/design/user-flows/User_Flows.md -o flows.png
```

## Build Logs

Use `scripts/run.sh <description> <command...>` to run shell commands. It automatically logs output
to `logs/YYYYMMDD-HHMMSS-<description>.log` (git-ignored) via `tee`, so output appears both in the
terminal and in the log file.

## Code Style

- **Always use braces** after `if`, `else`, `for`, `while`, etc. — even for single-line bodies

## Linting

Detekt is configured at `detekt.yml` and **must pass** before code is considered complete.
Build fails on any issue (`maxIssues: 0`).

Run: `scripts/run.sh detekt ./gradlew detekt`

Key rules and project-specific decisions:
- `UnsafeCallOnNullableType` — active; never use `!!` (fix the root cause instead)
- `GlobalCoroutineUsage` — active; never use `GlobalScope`
- `FunctionNaming` — `@Composable`/`@Preview` functions are exempt (PascalCase is correct for Compose)
- `LongMethod`/`LongParameterList`/`CyclomaticComplexMethod` — relaxed thresholds; `@Composable`/`@Preview` exempt
- `MagicNumber` — **disabled**; astro math (phase angles, azimuth degrees) and test data use numeric literals legitimately
- `MaxLineLength` — 120 characters

## Testing

### Unit Tests (JVM)
- **Framework:** JUnit 5 (junit-jupiter) + AssertJ assertions
- **Style:** Given/When/Then comments in each test, one behavior per test
- **Test data:** Record-and-replay pattern — capture live API responses as JSON fixtures in
  `app/src/test/resources/fixtures/`, then replay from fixtures in unit tests
- **API keys:** Stored in `secrets.properties` (gitignored), read at test runtime via
  `java.util.Properties`
- **When to write:** Write or update unit tests whenever business logic changes (domain, repository,
  viewmodel)
- **Run:** `scripts/run.sh unit-tests ./gradlew testDebugUnitTest`

### Compose Tests (Instrumented)
- **Location:** `app/src/androidTest/` — mirror the `screens/` and `components/` structure
- **Style:** Given/When/Then comments, one behaviour per test; use `SampleData` for test fixtures
- **When to write:** Write or update Compose tests whenever a composable's visible behaviour
  changes — new UI elements, new interactions, changed text/icons
- **Run:** `scripts/run.sh compose-tests scripts/compose-test.sh` — automatically starts
  Pixel6_API33 if no emulator is connected, waits for full boot, then runs
  `connectedDebugAndroidTest`

### End-of-Phase Checklist
Before declaring a development phase complete, all of the following must pass:
1. `scripts/run.sh detekt ./gradlew detekt`
2. `scripts/run.sh unit-tests ./gradlew testDebugUnitTest`
3. `scripts/run.sh compose-tests scripts/compose-test.sh`

## Document Maintenance

When making **substantive changes** to `docs/requirements/Moonrise_App_PRD.md` (features, scope,
criteria, phases), check these documents for consistency:

- `docs/design/user-stories/User_Stories.md`
- `docs/design/user-flows/User_Flows.md`
- `CLAUDE.md` (Project Overview, Development Phases, Domain Concepts sections)

Skip this for typo fixes or minor rewording.

# Android Kotlin Code Standards

## Code Inspector Compliance
- Always use `val` over `var` unless mutation is required
- Never use `!!` (non-null assertion) — use `?.let`, `?: return`, or `requireNotNull()`
- All `when` expressions must be exhaustive
- Coroutines: always use `viewModelScope` or `lifecycleScope`, never `GlobalScope`
- Resources: always close streams in `use {}` blocks
- Prefer `data class` for models; override `equals`/`hashCode` if using regular classes
- Use `@StringRes`, `@DrawableRes`, etc. for resource ID parameters
- Lambdas over anonymous classes for SAM interfaces
- No unused imports, no unused variables
- Follow Android Lint suppression policy: fix the issue, don't suppress it