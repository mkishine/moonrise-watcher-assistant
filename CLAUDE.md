# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project Overview

This is the **Moonrise Watching Application** - an Android app to help users identify optimal nights
for moonrise viewing by combining moon phase data, timing constraints, and weather forecasts.

**Current Status:** Design phase (no code yet). Documentation only.

## Repository Structure

```
docs/
├── requirements/          # PRD with feature specifications
├── design/
│   ├── user-stories/      # User stories with acceptance criteria
│   └── user-flows/        # Mermaid diagrams showing navigation
```

## Key Documents

| Document                                   | Purpose                                                |
|--------------------------------------------|--------------------------------------------------------|
| `docs/requirements/Moonrise_App_PRD.md`    | Full requirements, technical specs, development phases |
| `docs/design/user-stories/User_Stories.md` | User-focused features with acceptance criteria         |
| `docs/design/user-flows/User_Flows.md`     | Navigation flowcharts in Mermaid format                |

## Development Phases

- **Phase 1 (MVP):** Single location, 14-day forecast, good/bad indicators, list + detail views
- **Phase 2:** Multiple saved locations, per-location settings, enhanced weather visualization

## Planned Technology Stack

- **Platform:** Android native (Kotlin)
- **Astronomical calculations:** Library TBD (SunCalc candidate)
- **Weather API:** TBD (OpenWeatherMap, WeatherAPI, Visual Crossing candidates)
- **Storage:** Local device storage for locations and preferences

## Domain Concepts

- **Good night criteria:** Moon phase (2 days before to 5 days after full moon), moonrise before
  user's bedtime (default 11 PM), clear weather
- **Azimuth:** Compass direction of moonrise in degrees (0°=N, 90°=E, 180°=S, 270°=W)
- **Phase window:** Only the 7-day window around full moon is shown in the forecast

## Rendering Mermaid Diagrams

The user flow diagrams use Mermaid syntax. To render locally:

```bash
npm install -g @mermaid-js/mermaid-cli
mmdc -i docs/design/user-flows/User_Flows.md -o flows.png
```

## Document Maintenance

When making **substantive changes** to `docs/requirements/Moonrise_App_PRD.md` (features, scope, criteria, phases), check these documents for consistency:

- `docs/design/user-stories/User_Stories.md`
- `docs/design/user-flows/User_Flows.md`
- `CLAUDE.md` (Project Overview, Development Phases, Domain Concepts sections)

Skip this for typo fixes or minor rewording.
