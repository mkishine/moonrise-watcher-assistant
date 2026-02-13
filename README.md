# Moonrise Watcher Assistant

An Android application designed to help users identify optimal nights for watching moonrises by
consolidating moon phase data, moonrise timing, and weather forecasts into a single, easy-to-use
interface.

## Project Status

**UI skeleton phase** — composable previews render in Android Studio, no business logic yet.

- ✅ Requirements gathering complete
- ✅ Product Requirements Document complete
- ✅ User stories defined
- ✅ User flows documented
- ✅ Main Screen wireframe complete
- ✅ Android project skeleton with Jetpack Compose previews
- 🔄 Next: Remaining wireframes (Detail View, Settings, Location Management, First-Time Setup)
- ⏳ Business logic, networking, and storage not yet started

## Quick Overview

### What Problem Does This Solve?

Watching moonrise requires three conditions to align:
1. Moon must be near full phase (visually impressive)
2. Moon must rise at a reasonable hour (before bedtime)
3. Sky must be clear, especially at the horizon

Currently, users must check multiple sources and manually correlate this information. This app
consolidates everything into one place.

### Key Features (Planned)

- **Moon phase forecast** with clear good/bad night indicators (default 3 months, configurable)
- **Weather integration** showing sky clarity, temperature, wind, and precipitation
- **Configurable bedtime constraint** (default: 11 PM)
- **Multiple saved locations** for travelers and multi-site observers
- **Azimuth display** showing compass direction of moonrise
- **At-a-glance interface** requiring minimal interaction

## Technology Stack

| Component        | Version    | Notes                              |
|------------------|------------|------------------------------------|
| AGP              | 9.0.0      | Built-in Kotlin support            |
| Kotlin (via AGP) | 2.2.10     | Bundled with AGP 9.0               |
| Compose BOM      | 2026.01.01 | Material 3 1.4.0, Compose UI 1.10 |
| Gradle           | 9.1        | Minimum for AGP 9.0               |
| compileSdk       | 36         | Android 16                         |
| minSdk           | 26         | Android 8.0 (native java.time)     |
| JDK              | 17         | Required by AGP 9.0               |

**Requires:** Android Studio Meerkat Feature Drop (2024.3.2) or newer.

## Project Structure

```
moonrise-watcher-assistant/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/values/strings.xml
│       └── kotlin/.../moonriseassistant/
│           ├── model/              # Data classes (ForecastDay, enums)
│           ├── ui/theme/           # Material 3 theme (Color, Type, Theme)
│           ├── components/         # TopBar, TodaySection, ForecastList*
│           ├── screens/            # MainScreen (adaptive layout)
│           ├── preview/            # Sample data + @Preview composables
│           └── MainActivity.kt     # Entry point
├── docs/
│   ├── requirements/               # Product Requirements Document
│   └── design/
│       ├── user-stories/           # User stories with acceptance criteria
│       ├── user-flows/             # Mermaid navigation diagrams
│       └── wireframes/             # ASCII wireframes for screen layouts
├── gradle/
│   ├── libs.versions.toml          # Version catalog
│   └── wrapper/                    # Gradle 9.1 wrapper
├── build.gradle.kts                # Root build file
└── settings.gradle.kts             # Project settings
```

## Getting Started

### Prerequisites

- Android Studio Meerkat Feature Drop (2024.3.2) or newer
- JDK 17

### Viewing Compose Previews

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Open `app/src/main/kotlin/.../preview/MainScreenPreviews.kt`
4. Previews render in the Preview pane (portrait light/dark, landscape, large font, components)

### Running the App

The app is runnable on an emulator or device — it displays the Main Screen with hardcoded sample
data (Seattle, Feb 2026).

## Documentation

| Document                                                   | Purpose                                        |
|------------------------------------------------------------|-------------------------------------------------|
| [PRD](docs/requirements/Moonrise_App_PRD.md)              | Full requirements and technical specs           |
| [User Stories](docs/design/user-stories/User_Stories.md)  | User-focused features with acceptance criteria  |
| [User Flows](docs/design/user-flows/User_Flows.md)       | Navigation flowcharts in Mermaid format         |
| [Main Screen Wireframe](docs/design/wireframes/Main_Screen_Wireframe.md) | ASCII wireframes for Main Screen |

## Development Phases

### Phase 1: MVP (Minimum Viable Product)
**Goal:** Core functionality for single-location moonrise forecasting

- Manual location entry (single location)
- Configurable forecast period (default 3 months) with moonrise/sunset times and weather
- Good/bad day indicators based on phase and time constraints
- List view with at-a-glance information
- Detail view with expanded weather information

### Phase 2: Enhancement
**Goal:** Multi-location support and refinements

- Multiple saved locations with management interface
- Configurable maximum moonrise time per location
- Improved weather visualization
- Performance optimizations and bug fixes

## Design Principles

1. **Today First** — Most important information (tonight's conditions) displayed prominently
2. **Minimal Interaction** — Common tasks require minimal taps
3. **Visual Clarity** — Good nights stand out immediately with clear visual indicators
4. **No Clutter** — At-a-glance view shows only essential information
5. **Progressive Disclosure** — Detailed information available on demand

## License

*License to be determined*
