# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project Overview

This is the **Moonrise Watcher Assistant** - an Android app to help users identify optimal nights
for moonrise viewing by combining moon phase data, timing constraints, and weather forecasts.

**Current Status:** UI skeleton with Compose previews. No business logic, networking, or storage yet.

## Repository Structure

```
app/src/main/kotlin/name/kisinievsky/michael/moonriseassistant/
├── model/                 # Data classes (ForecastDay, enums)
├── ui/theme/              # Material 3 theme (Color, Type, Theme)
├── components/            # Reusable composables (TopBar, TodaySection, ForecastList*)
├── screens/               # Screen-level composables (MainScreen)
├── preview/               # Sample data and @Preview composables
└── MainActivity.kt        # Entry point (renders MainScreen with sample data)
docs/
├── requirements/          # PRD with feature specifications
├── design/
│   ├── user-stories/      # User stories with acceptance criteria
│   ├── user-flows/        # Mermaid diagrams showing navigation
│   └── wireframes/        # ASCII wireframes for screen layouts
```

## Key Documents

| Document                                   | Purpose                                                |
|--------------------------------------------|--------------------------------------------------------|
| `docs/requirements/Moonrise_App_PRD.md`    | Full requirements, technical specs, development phases |
| `docs/design/user-stories/User_Stories.md` | User-focused features with acceptance criteria         |
| `docs/design/user-flows/User_Flows.md`     | Navigation flowcharts in Mermaid format                |
| `docs/design/wireframes/Main_Screen_Wireframe.md` | ASCII wireframes for Main Screen layout          |

## Development Phases

- **Phase 1 (MVP):** Single location, 14-day forecast, good/bad indicators, list + detail views
- **Phase 2:** Multiple saved locations, per-location settings, enhanced weather visualization

## Technology Stack

| Component        | Version    | Notes                                           |
|------------------|------------|-------------------------------------------------|
| AGP              | 9.0.0      | Built-in Kotlin support                         |
| Kotlin (via AGP) | 2.2.10     | Bundled with AGP 9.0                            |
| Compose BOM      | 2026.01.01 | Material 3 1.4.0, Compose UI 1.10.2            |
| Gradle           | 9.1.0      | Minimum for AGP 9.0                             |
| compileSdk       | 36         | Android 16                                      |
| minSdk           | 26         | Android 8.0 (native java.time)                  |
| JDK              | 21         | JetBrains vendor, via Gradle daemon toolchain   |

- **Astronomical calculations:** Library TBD (SunCalc candidate)
- **Weather API:** TBD (OpenWeatherMap, WeatherAPI, Visual Crossing candidates)
- **Storage:** Local device storage for locations and preferences

## Domain Concepts

- **Good night criteria:** Moon phase (2 days before to 5 days after full moon), moonrise after
  sunset (with configurable tolerance, default 30 min) and before user's bedtime (default 11 PM),
  clear weather
- **Azimuth:** Compass direction of moonrise in degrees (0°=N, 90°=E, 180°=S, 270°=W)
- **Phase window:** Only the 7-day window around full moon is shown in the forecast (days outside
  the window are hidden, not grayed out)

## Rendering Mermaid Diagrams

The user flow diagrams use Mermaid syntax. To render locally:

```bash
npm install -g @mermaid-js/mermaid-cli
mmdc -i docs/design/user-flows/User_Flows.md -o flows.png
```

## Build Logs

Redirect all shell command output to timestamped log files in `logs/` (git-ignored) so the user can
monitor progress and review later. Use the format `logs/YYYYMMDD-HHMMSS-description.log` with `tee`
so output appears both in the terminal and in the file.

## Document Maintenance

When making **substantive changes** to `docs/requirements/Moonrise_App_PRD.md` (features, scope, criteria, phases), check these documents for consistency:

- `docs/design/user-stories/User_Stories.md`
- `docs/design/user-flows/User_Flows.md`
- `CLAUDE.md` (Project Overview, Development Phases, Domain Concepts sections)

Skip this for typo fixes or minor rewording.
