# Remaining Design Decisions

**Date:** February 15, 2026

## Completed So Far

- Requirements gathering
- Product Requirements Document
- User stories defined
- User flows documented
- All wireframes (Main, Detail View, Settings, Add Location, Location Selector)
- Android project skeleton with Jetpack Compose previews for all screens

## ~~Storage Decision~~

**Resolved** — Room for all phases. See `architecture-decisions.md`.

## ~~Weather API Selection~~

**Resolved** — Visual Crossing selected. See `weather-api-comparison.md` and PRD Decision Log.

## ~~Astronomical Library Selection~~

**Resolved** — commons-suncalc 3.11 selected. See `astronomical-library-comparison.md` and PRD
Decision Log.

## ~~Architecture / Module Design~~

**Resolved** — Navigation Compose, one ViewModel per screen, manual DI, one repository per concern,
Ktor + kotlinx.serialization, dedicated VerdictEngine. See `architecture-decisions.md`.

## ~~Data Model Finalization~~

**Resolved** — Audited model against all wireframes. Added `VerdictChecks` data class with
per-constraint pass/fail/unknown results, replacing the flat `verdictReason` string. Next full moon
date will live on the UI state (ViewModel), not the domain model.

## ~~First-Launch Empty State~~

**Resolved** — UI already exists (`FirstTimeSetup` composable + `MainScreenFirstTime`). Flow
documented in `architecture-decisions.md` under "First-launch flow."

## Summary

| Decision                           | Priority   | Effort                   | Blocker? |
|------------------------------------|------------|--------------------------|----------|
| ~~Weather API selection~~          | ~~High~~   | ~~Spike (~1 session)~~   | Resolved |
| ~~Astronomical library selection~~ | ~~High~~   | ~~Quick evaluation~~     | Resolved |
| ~~Storage~~                        | ~~Medium~~ | ~~N/A~~                  | Resolved |
| ~~Architecture sketch~~            | ~~Medium~~ | ~~Brief document~~       | Resolved |
| ~~Data model finalization~~        | ~~Medium~~ | ~~Quick pass~~           | Resolved |
| ~~First-launch empty state~~       | ~~Low~~    | ~~Minor wireframe note~~ | Resolved |