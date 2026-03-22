# Moonrise Watcher Assistant

An Android application designed to help users identify optimal nights for watching moonrises by
consolidating moon phase data, moonrise timing, and weather forecasts into a single, easy-to-use
interface.

## Project Status

Fully functional. Live weather and astronomical data, multi-location support, Room storage,
and a comprehensive test suite (unit tests + compose instrumented tests).

## Quick Overview

### What Problem Does This Solve?

Watching moonrise requires three conditions to align:

1. Moon must be near full phase (visually impressive)
2. Moon must rise at a reasonable hour (before bedtime)
3. Sky must be clear, especially at the horizon

Currently, users must check multiple sources and manually correlate this information. This app
consolidates everything into one place.

### Key Features

- **Moon phase forecast** with good/bad night indicators (default 3 months, configurable)
- **Weather integration** showing sky clarity, temperature, wind, and precipitation
- **Configurable bedtime constraint** (default: 11 PM) per location
- **Multiple saved locations** with a location selector bottom sheet
- **Azimuth display** showing compass direction of moonrise
- **At-a-glance interface** requiring minimal interaction
- **First-time tutorial** guiding new users through setup
- **City geocoding** — enter a city name to resolve coordinates automatically

## Technology Stack

| Component             | Version    | Notes                                         |
|-----------------------|------------|-----------------------------------------------|
| AGP                   | 9.0.1      | Built-in Kotlin support                       |
| Kotlin (via AGP)      | 2.2.10     | Bundled with AGP 9.0                          |
| Compose BOM           | 2026.02.00 | Material 3, Compose UI                        |
| Gradle                | 9.1.0      | Minimum for AGP 9.0                           |
| compileSdk            | 36         | Android 16                                    |
| minSdk                | 26         | Android 8.0 (native java.time)                |
| JDK                   | 21         | JetBrains vendor, via Gradle daemon toolchain |
| Room                  | 2.8.4      | Local storage for locations, settings, cache  |
| Ktor                  | 3.3.3      | HTTP client (CIO engine)                      |
| kotlinx.serialization | 1.9.0      | JSON parsing for weather API responses        |
| Navigation Compose    | 2.9.7      | Screen-to-screen navigation                   |
| Lifecycle / ViewModel | 2.10.0     | ViewModels + Compose state integration        |
| Detekt                | 1.23.7     | Static analysis; build fails on any issue     |

- **Astronomical calculations:** commons-suncalc 3.11 (moonrise/sunset times, moon phase, azimuth)
- **Weather API:** Visual Crossing Timeline Weather API
- **Testing:** JUnit 5 (junit-jupiter) + AssertJ; Compose UI test framework

**Requires:** Android Studio Meerkat Feature Drop (2024.3.2) or newer.

## Project Structure

```
moonrise-watcher-assistant/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── res/values/strings.xml
│       │   └── kotlin/.../moonriseassistant/
│       │       ├── model/              # Data classes, enums (ForecastDay, AppSettings, enums)
│       │       ├── domain/             # AstroCalculator, VerdictEngine (pure Kotlin)
│       │       ├── network/            # VisualCrossingApi + model/ (Ktor + serialization DTOs)
│       │       ├── storage/            # Room database, DAOs, entities
│       │       ├── repository/         # ForecastRepository, LocationRepository, SettingsRepository
│       │       ├── viewmodel/          # MainViewModel, SettingsViewModel, AddLocationViewModel,
│       │       │                       #   LocationSelectorViewModel + UiState classes
│       │       ├── navigation/         # MoonriseNavHost, Routes
│       │       ├── di/                 # AppContainer (manual DI)
│       │       ├── location/           # GeocodingService (city name → coordinates)
│       │       ├── ui/theme/           # Material 3 theme (Color, Type, Theme)
│       │       ├── components/         # TopBar, TodaySection, ForecastList, DetailSheet,
│       │       │                       #   LocationSelector, StateViews
│       │       ├── screens/            # MainScreen, SettingsScreen, AddLocationScreen,
│       │       │                       #   TutorialScreen, AboutScreen
│       │       ├── preview/            # SampleData + @Preview composables
│       │       ├── MoonriseApplication.kt  # Application subclass; creates AppContainer
│       │       └── MainActivity.kt         # Entry point; hosts MoonriseNavHost
│       ├── test/                       # JVM unit tests (domain, network, repository, viewmodel)
│       └── androidTest/                # Compose instrumented tests (screens, components)
├── docs/
│   ├── requirements/                   # Product Requirements Document
│   └── design/
│       ├── user-stories/               # User stories with acceptance criteria
│       ├── user-flows/                 # Mermaid navigation diagrams
│       ├── wireframes/                 # ASCII wireframes for screen layouts
│       └── architecture/               # Architecture decisions and data flow
├── gradle/
│   ├── libs.versions.toml              # Version catalog
│   └── wrapper/                        # Gradle 9.1.0 wrapper
├── scripts/
│   ├── run.sh                          # Logs build/test output to logs/
│   └── compose-test.sh                 # Starts emulator if needed and runs instrumented tests
├── build.gradle.kts                    # Root build file
├── detekt.yml                          # Detekt static analysis configuration
└── settings.gradle.kts                 # Project settings
```

## Getting Started

### Prerequisites

- Android Studio Meerkat Feature Drop (2024.3.2) or newer
- JDK 21
- Visual Crossing API key in `secrets.properties` (for live weather data)

### Running the App

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Run on an emulator or device
4. On first launch, the Tutorial screen guides you through adding a location

### Running Tests

```bash
# Unit tests
scripts/run.sh unit-tests ./gradlew testDebugUnitTest

# Compose instrumented tests (starts emulator automatically if needed)
scripts/run.sh compose-tests scripts/compose-test.sh

# Static analysis
scripts/run.sh detekt ./gradlew detekt
```

### Viewing Compose Previews

Open any file in `app/src/main/kotlin/.../preview/` in Android Studio to see the Preview pane
with light/dark, landscape, large font, and component variant previews.

## Documentation

| Document                                                                             | Purpose                                        |
|--------------------------------------------------------------------------------------|------------------------------------------------|
| [PRD](docs/requirements/Moonrise_App_PRD.md)                                         | Full requirements and technical specs          |
| [User Stories](docs/design/user-stories/User_Stories.md)                             | User-focused features with acceptance criteria |
| [User Flows](docs/design/user-flows/User_Flows.md)                                   | Navigation flowcharts in Mermaid format        |
| [Architecture](docs/design/architecture/Architecture.md)                             | Architecture decisions and data flow           |
| [Main Screen Wireframe](docs/design/wireframes/Main_Screen_Wireframe.md)             | ASCII wireframes for Main Screen               |
| [Detail View Wireframe](docs/design/wireframes/Detail_View_Wireframe.md)             | ASCII wireframes for Detail View bottom sheet  |
| [Settings Wireframe](docs/design/wireframes/Settings_Wireframe.md)                   | ASCII wireframes for Settings screen           |
| [Add Location Wireframe](docs/design/wireframes/Add_Location_Wireframe.md)           | ASCII wireframes for Add Location screen       |
| [Location Selector Wireframe](docs/design/wireframes/Location_Selector_Wireframe.md) | ASCII wireframes for Location Selector         |
| [Tutorial Wireframe](docs/design/wireframes/Tutorial_Wireframe.md)                   | ASCII wireframes for Tutorial screen           |

## Design Principles

1. **Today First** — Most important information (tonight's conditions) displayed prominently
2. **Minimal Interaction** — Common tasks require minimal taps
3. **Visual Clarity** — Good nights stand out immediately with clear visual indicators
4. **No Clutter** — At-a-glance view shows only essential information
5. **Progressive Disclosure** — Detailed information available on demand

## License

Copyright 2026 Michael Kishinevsky. Licensed under the [Apache License, Version 2.0](LICENSE).
