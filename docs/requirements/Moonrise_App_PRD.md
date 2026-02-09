# Product Requirements Document

# Moonrise Watching Application

**Status:** Initial Draft
**Platform:** Android

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [Problem Statement](#problem-statement)
3. [Goals and Objectives](#goals-and-objectives)
4. [Target Users](#target-users)
5. [Feature Requirements](#feature-requirements)
6. [Technical Requirements](#technical-requirements)
7. [Known Limitations](#known-limitations)
8. [Success Metrics](#success-metrics)
9. [Development Phases](#development-phases)
10. [Open Questions](#open-questions)
11. [Feedback Loop Strategy](#feedback-loop-strategy)
12. [Appendices](#appendices)

---

## Executive Summary

This document outlines the requirements for an Android application designed to help users identify
optimal nights for watching moonrises. The app addresses the challenge of finding the perfect
conditions: moon phase, timing, and weather clarity. By providing an astronomical forecast (default 3
months) with weather data for the near term, users can plan their moonrise viewing sessions well in
advance.

---

## Problem Statement

Watching the moon rise is a rewarding experience, but finding the right conditions requires
considering multiple factors simultaneously:

- The moon must be in a favorable phase (near full moon) to be visually impressive
- The moon must rise at a reasonable hour (before the observer's bedtime)
- The sky must be clear, particularly at the horizon in the direction of moonrise
- These conditions must align on the same night

Currently, users must check multiple sources (moon phase calendars, moonrise times, weather
forecasts) and manually correlate this information. This app consolidates all relevant data into a
single, easy-to-use interface.

---

## Goals and Objectives

### Primary Goal

Enable users to quickly identify which upcoming nights offer favorable moonrise timing (default
forecast period: 3 months, configurable), with weather conditions shown when forecast data is
available.

### Secondary Goals

- Provide detailed weather information to help users prepare for outdoor viewing
- Support multiple saved locations for users who observe from different places
- Deliver information in an at-a-glance format that requires minimal interaction

---

## Target Users

### Primary User Persona

**Name:** The Amateur Astronomer

**Description:** An individual who enjoys observing celestial events as a hobby. They appreciate
natural phenomena but are not professional astronomers. They have specific preferences about viewing
conditions and want to maximize their chances of successful observations.

**Needs:** Quick identification of favorable viewing nights, weather-dependent planning, advance
notice to arrange schedules.

**Technical Comfort:** Comfortable with smartphones and basic apps, expects intuitive interfaces.

---

## Feature Requirements

### 1. Moon Phase Filtering

**Requirement ID:** 1.1
**Priority:** Must Have

**Description:**
The app shall display moonrise information only for dates when the moon is within a configurable
window around the full moon. The following parameters shall be user-configurable via Settings:

| Parameter             | Default | Description                                            |
|-----------------------|---------|--------------------------------------------------------|
| Days before full moon | 2       | How many days before full moon to include              |
| Days after full moon  | 5       | How many days after full moon to include               |
| Forecast period       | 3       | How many months ahead to display the forecast          |

With default settings, the app displays a 7-day window (2 days before through 5 days after) around
each full moon within a 3-month forecast period.

**Rationale:**
Full moon rises at sunset, providing optimal viewing timing. Days before full moon have earlier
rises; days after have progressively later rises. The defaults reflect typical viewing preferences,
but users may wish to adjust these based on their schedule, equipment, or interest level. For
example, a user with a telescope may enjoy phases further from full moon, while a casual observer may
prefer a narrower window. Similarly, some users may want to plan further ahead than 3 months.

**Acceptance Criteria:**

- App correctly identifies and displays the phase window around each full moon within the forecast
  period using the configured parameter values
- Default values are applied on first launch without requiring user configuration
- Users can modify all three parameters in Settings
- Changes to parameters take effect immediately upon returning to the forecast view
- Parameter values are persisted across app restarts

---

### 2. Moonrise Time Constraint

**Requirement ID:** 2.1  
**Priority:** Must Have

**Description:**  
The app shall allow users to set a maximum moonrise time (default: 11:00 PM). Days when moonrise
occurs after this time shall be marked as unfavorable or excluded from 'good viewing' status.

**Rationale:**  
Users have different schedules and bedtimes. A configurable cutoff time ensures the app accommodates
individual preferences rather than using a fixed window after sunset.

**Acceptance Criteria:**  
Users can set their preferred maximum moonrise time. The app correctly evaluates each day against
this constraint and updates the 'good/bad' status accordingly.

---

### 3. Weather Forecast Integration

**Requirement ID:** 3.1 - 3.4  
**Priority:** Must Have

**Description:**

- **3.1:** Provide weather forecast for dates within the reliable forecast window (approximately 14
  days)
- **3.1.1:** Dates beyond the weather forecast window shall display astronomical data (timing,
  phase) without weather indicators, clearly marked as "weather unknown"
- **3.2:** Display simple sky clarity categories (clear/partly cloudy/cloudy) for dates 3+ days in
  advance
- **3.3:** Display detailed cloud coverage percentage for current day and 1-2 days ahead
- **3.4:** When available, indicate horizon clarity in the direction of moonrise (azimuth-specific
  forecast)

**Rationale:**  
Sky clarity is essential for moonrise viewing. Long-range forecasts provide general planning;
detailed near-term forecasts enable day-of decisions. Horizon-specific clarity addresses the unique
challenge that overhead clouds may not obstruct a moon rising at the horizon.

**Technical Notes:**  
Horizon-specific clarity may not be available from all weather APIs. Implementation shall use the
best available data sources and clearly indicate limitations. Future versions may incorporate user
feedback to improve forecast accuracy.

**Acceptance Criteria:**  
Weather data displays correctly for 14 days. Simple categories appear for distant dates; detailed
data appears for imminent dates. Horizon clarity information displays when available from the
weather API.

---

### 4. Location Management

**Requirement ID:** 4.1 - 4.4  
**Priority:** Must Have

**Description:**

- **4.1:** No GPS auto-detection of location
- **4.2:** Manual location entry via city name or geographic coordinates
- **4.3:** GPS auto-detection not required
- **4.4:** Ability to save and manage multiple favorite locations

**Rationale:**  
Manual entry provides precise control. Users may want to plan for locations they will visit (
vacation spots, observatories) rather than only their current location. Multiple saved locations
support users who observe from various places regularly.

**Acceptance Criteria:**  
Users can add locations via city name or coordinates. Locations can be saved, edited, renamed, and
deleted. Users can switch between saved locations. Moonrise and weather data updates correctly when
location changes.

---

### 5. Notifications

**Requirement ID:** 5.1  
**Priority:** Must Have

**Description:**  
The app shall not implement notifications. Users will check the app manually when planning.

**Rationale:**  
User preference for manual checking reduces notification fatigue and battery usage.

**Acceptance Criteria:**  
No notification system implemented in v1.0.

---

### 6. User Interface

**Requirement ID:** 6.1 - 6.4  
**Priority:** Must Have

**Description:**

- **6.1:** No calendar view
- **6.2:** Today's moonrise details prominently displayed at top of screen
- **6.3:** List view of upcoming days (up to the configured forecast period) showing at-a-glance
  information
- **6.4:** Tapping a day in the list reveals detailed weather information

**At-a-Glance Information (List View):**

- 6.4.1: Sunset time
- 6.4.2: Moonrise time
- 6.4.3: Sky clearness indicator

**Detailed Information (Detail View):**

- 6.4.5: Temperature (actual)
- 6.4.6: Temperature with windchill factor
- 6.4.7: Wind speed
- 6.4.8: Precipitation forecast
- 6.4.9: More detailed cloud/weather information

**Rationale:**  
List view enables quick scanning of multiple days. Today's prominence helps users quickly answer '
Can I watch tonight?' Detail view provides planning information (what to wear, whether to bring rain
gear) without cluttering the main view.

**Acceptance Criteria:**  
Today's information displays prominently. List shows all at-a-glance items. Tapping a day reveals
all detailed items. Interface is clean and uncluttered.

---

### 7. Visual Indicators

**Requirement ID:** 7.1 - 7.3  
**Priority:** Must Have

**Description:**

- **7.1:** Good viewing days shall be clearly highlighted (e.g., green badge or background)
- **7.2:** Moon illumination percentage shall not be displayed
- **7.3:** Moonrise azimuth (compass direction) shall be displayed

**Rationale:**  
Clear visual distinction helps users identify good nights instantly without reading details. Azimuth
helps users know where to look and assess local horizon obstructions (buildings, trees). Moon
illumination is implicitly handled by the phase window filter.

**Acceptance Criteria:**  
Good days have distinct visual treatment. Azimuth displays correctly (in degrees and/or cardinal
direction). No illumination percentage visible anywhere in the UI.

---

### 8. Units and Preferences

**Requirement ID:** 7.4  
**Priority:** Must Have

**Description:**  
The app shall use Fahrenheit for temperature and miles per hour (mph) for wind speed.

**Rationale:**  
User preference for Imperial units.

**Acceptance Criteria:**  
All temperature values display in °F. All wind speed values display in mph. No unit conversion
options in v1.0.

---

## Technical Requirements

### Platform

- Android (minimum API level to be determined during design phase)
- Native Android development recommended for performance and integration

### Data Sources

- **Astronomical calculations:** Use reliable library (e.g., SunCalc, astronomical algorithms) for
  moonrise/sunset times, moon phase, and azimuth
- **Weather data:** Weather API supporting 14-day forecasts with cloud coverage data (candidates:
  OpenWeatherMap, WeatherAPI, Visual Crossing)

### Performance

- App shall load and display current day data within 2 seconds on average mobile connection
- Forecast data shall be cached to minimize API calls and enable offline viewing of previously
  loaded data

### Data Storage

- Saved locations stored locally on device
- User preferences (maximum moonrise time) stored locally
- Weather forecast data cached with appropriate TTL (time-to-live) based on forecast date

---

## Known Limitations

- Weather API may not provide azimuth-specific (directional) cloud coverage. App will use best
  available general cloud coverage data.
- Weather forecasts become less accurate for distant dates. This is a limitation of meteorological
  forecasting, not the app.
- Weather data is only available for approximately 14 days. Dates beyond this window show timing
  information but weather status displays as "unknown."
- Local horizon obstructions (buildings, mountains, trees) cannot be automatically detected. Users
  must manually assess whether their viewing location has clear sightlines at the indicated azimuth.

---

## Success Metrics

### MVP Success Criteria

- App correctly identifies favorable moonrise nights based on phase, time, and weather constraints
- User can add, save, and switch between multiple locations
- At-a-glance view loads quickly and displays essential information without scrolling
- Detailed view provides all specified weather information

### User Satisfaction Indicators

- User reports successful moonrise viewings on nights identified as 'good' by the app
- User finds the interface intuitive and does not require instructions to use core features

---

## Development Phases

### Phase 1: MVP (Minimum Viable Product)

**Goal:** Deliver core functionality for single-location moonrise forecasting

**Features:**

- Manual location entry (single location only for MVP)
- Configurable forecast period (default 3 months) with moonrise times, sunset times, and basic
  weather (weather shown only for dates within forecast window)
- Good/bad day indicators based on phase and time constraints
- List view with at-a-glance information
- Detail view with expanded weather information

---

### Phase 2: Enhancement

**Goal:** Add multi-location support and refinements based on Phase 1 learnings

**Features:**

- Multiple saved locations with location management interface
- Configurable maximum moonrise time per location
- Improved weather visualization
- Performance optimizations and bug fixes

---

### Future Considerations (Post-Phase 2)

- User feedback mechanism to improve forecast accuracy
- Historical viewing log (user records when they actually watched moonrise and conditions)
- Integration with additional weather data sources for more accurate horizon clarity
- Widget showing today's moonrise status on home screen
- Metric/Imperial unit toggle in settings

---

## Open Questions

1. Which specific weather API provides the best balance of accuracy, cost, and feature set for our
   needs?
2. What is the optimal caching strategy for weather data? (e.g., cache for 6 hours for today, 24
   hours for distant forecasts)
3. Should the app show days outside the favorable moon phase window in a grayed-out state, or
   completely hide them?
4. What should happen when moonrise occurs before sunset (during bright daylight)? Should these be
   marked as unfavorable?
5. Should the detail view remain open when user swipes to next/previous day, or should it close?

---

## Feedback Loop Strategy

This PRD is a living document. Requirements will evolve through design, development, testing, and
real-world usage. The following practices will guide our iteration process:

### Document Management

- This document shall be versioned (v1.0, v1.1, etc.) with a changelog tracking significant
  revisions
- Open questions and to-be-determined items shall be maintained in a dedicated section
- Original requirements shall remain intact; revisions shall be added as amendments with clear
  rationale

### Iteration Process

- **After design mockups:** Review against requirements, update PRD if design reveals gaps or
  conflicts
- **During development:** Track technical limitations and update 'Known Limitations' section
- **After testing:** Capture usability findings and update requirements or interface specifications
- **After real-world usage:** Document what worked, what didn't, and incorporate lessons into Phase
  2 planning

### Decision Log

Significant design and implementation decisions shall be documented with:

- Date of decision
- Options considered
- Decision made
- Rationale

**Example:**

```
Date: 2026-02-15
Decision: Selected OpenWeatherMap API over WeatherAPI
Options: 
  - OpenWeatherMap (free tier: 1000 calls/day)
  - WeatherAPI (free tier: 1M calls/month)
  - Visual Crossing (complex pricing)
Rationale: OpenWeatherMap provides sufficient free tier for MVP, 
well-documented, includes cloud layer data
```

---

## Appendices

### Appendix A: Glossary

**Azimuth:** The compass direction of a celestial object, measured in degrees clockwise from north (
0° = north, 90° = east, 180° = south, 270° = west).

**Full Moon:** The moon phase when the moon is fully illuminated as seen from Earth, occurring
approximately once per 29.5 days.

**Moonrise:** The time when the leading edge of the moon appears above the horizon.

**Sunset:** The time when the trailing edge of the sun disappears below the horizon.

**Windchill:** The perceived decrease in temperature felt by the body due to the flow of air.

**Cloud Coverage:** The fraction of the sky covered by clouds, typically expressed as a percentage
or in oktas (eighths of the sky).

---

### Appendix B: Requirement Traceability Matrix

This matrix will be populated during design and development phases to track how each requirement is
implemented and tested.

| Req ID  | Requirement                  | Implementation | Test Case |
|---------|------------------------------|----------------|-----------|
| 1.1     | Moon Phase Filtering         | TBD            | TBD       |
| 2.1     | Moonrise Time Constraint     | TBD            | TBD       |
| 3.1-3.4 | Weather Forecast Integration | TBD            | TBD       |
| 4.1-4.4 | Location Management          | TBD            | TBD       |
| 5.1     | Notifications                | TBD            | TBD       |
| 6.1-6.4 | User Interface               | TBD            | TBD       |
| 7.1-7.3 | Visual Indicators            | TBD            | TBD       |
| 7.4     | Units and Preferences        | TBD            | TBD       |

*(Additional rows to be added as development progresses)*

---

## Notes for Developers

This markdown file can be:

- Version controlled in Git alongside your code
- Easily referenced and updated during development
- Converted to other formats when needed (PDF, HTML, Word)
- Linked from your repository README or development documentation

To convert to other formats:

```bash
# To HTML
pandoc Moonrise_App_PRD_v1.0.md -o PRD.html

# To PDF (requires LaTeX)
pandoc Moonrise_App_PRD.md -o PRD.pdf

# To Word
pandoc Moonrise_App_PRD.md -o PRD.docx
```
