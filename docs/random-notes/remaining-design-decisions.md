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

## ~~Weather API Selection~~

**Resolved** — Visual Crossing selected. See `weather-api-comparison.md` and PRD Decision Log.

## ~~Astronomical Library Selection~~

**Resolved** — commons-suncalc 3.11 selected. See `astronomical-library-comparison.md` and PRD
Decision Log.

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

| Decision                           | Priority | Effort                 | Blocker? |
|------------------------------------|----------|------------------------|----------|
| ~~Weather API selection~~          | ~~High~~ | ~~Spike (~1 session)~~ | Resolved |
| ~~Astronomical library selection~~ | ~~High~~ | ~~Quick evaluation~~   | Resolved |
| Architecture sketch                | Medium   | Brief document         | No       |
| Data model finalization            | Medium   | Quick pass             | No       |
| First-launch empty state           | Low      | Minor wireframe note   | No       |