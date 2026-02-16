# Remaining Design Decisions

**Date:** February 15, 2026

## Completed So Far

- Requirements gathering
- Product Requirements Document
- User stories defined
- User flows documented
- All wireframes (Main, Detail View, Settings, Add Location, Location Selector)
- Android project skeleton with Jetpack Compose previews for all screens

## Storage Decision

**Not a blocker.** The PRD already specifies local device storage for locations, preferences, and
cached weather data. For Phase 1 (single location, preferences only), Preferences DataStore is the
standard choice. Phase 2 with multiple locations would benefit from Room, but that's a later
concern.
Can be decided during implementation.

## Weather API Selection

**Priority: High — blocker for implementation.**

PRD Open Question #1. Three candidates: OpenWeatherMap, WeatherAPI, Visual Crossing.

Matters because it determines:

- Available data fields (horizon-specific cloud coverage, hourly vs daily, etc.)
- Data models and caching strategy (PRD Open Question #2)
- Cost and rate-limit implications
- Repository interface shape

**Action:** API comparison spike — check each candidate's free tier, 14-day forecast support, cloud
coverage detail, and response format. Document decision in PRD Decision Log.

## Astronomical Library Selection

**Priority: High — blocker for implementation.**

PRD says "Library TBD (SunCalc candidate)." This is the core calculation engine. Need to verify a
JVM/Kotlin-compatible library exists that provides all needed values:

- Moonrise time
- Sunset time
- Moon phase
- Azimuth

**Action:** Quick evaluation of available libraries.

## Architecture / Module Design

**Priority: Medium.**

Currently no navigation, ViewModels, DI, or repository layer. Before coding, sketch the high-level
architecture:

- Screen connectivity and navigation graph
- Data flow (repository pattern, ViewModel per screen)
- DI approach (Hilt vs manual)

A brief `docs/design/architecture.md` or a section in the PRD would suffice.

## Data Model Finalization

**Priority: Medium — depends on API and library choices.**

The `model/` package has `ForecastDay` etc., but these are preview stubs. Once the weather API and
astro library are chosen, confirm the domain model covers all wireframe data points.

## First-Launch Empty State

**Priority: Low.**

US-001 says the app prompts for location on first launch, but there's no wireframe for the "no
location" empty state. The Add Location wireframe exists, but the flow of "empty state -> prompt ->
add location -> main screen" could use a quick note.

## Summary

| Decision                       | Priority | Effort               | Blocker? |
|--------------------------------|----------|----------------------|----------|
| Weather API selection          | High     | Spike (~1 session)   | Yes      |
| Astronomical library selection | High     | Quick evaluation     | Yes      |
| Architecture sketch            | Medium   | Brief document       | No       |
| Data model finalization        | Medium   | Quick pass           | No       |
| First-launch empty state       | Low      | Minor wireframe note | No       |