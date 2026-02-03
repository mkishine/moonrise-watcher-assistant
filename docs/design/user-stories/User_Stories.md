# User Stories
# Moonrise Watching Application

**Version:** 1.0  
**Date:** February 3, 2026  
**Related Document:** Moonrise_App_PRD.md

---

## Table of Contents

1. [Introduction](#introduction)
2. [User Story Format](#user-story-format)
3. [Epic: First-Time Setup](#epic-first-time-setup)
4. [Epic: Daily Usage](#epic-daily-usage)
5. [Epic: Location Management](#epic-location-management)
6. [Epic: Settings and Preferences](#epic-settings-and-preferences)
7. [User Story Mapping](#user-story-mapping)

---

## Introduction

This document contains user stories for the Moonrise Watching Application. User stories describe features from the end-user's perspective and help ensure we're building what users actually need.

Each story follows the format: **"As a [user type], I want to [action], so that [benefit]"**

---

## User Story Format

Each user story includes:
- **Story ID**: Unique identifier
- **Story**: The user story statement
- **Acceptance Criteria**: Specific conditions that must be met
- **Priority**: Must Have / Should Have / Nice to Have
- **Story Points**: Effort estimate (to be added during sprint planning)
- **Related Requirements**: Links to PRD requirements

---

## Epic: First-Time Setup

### US-001: Add First Location

**Story:**  
As a new user, I want to enter my viewing location when I first open the app, so that I can immediately see moonrise forecasts for where I'll be watching.

**Acceptance Criteria:**
- [ ] App prompts for location entry on first launch
- [ ] User can enter location by city name (e.g., "Seattle, WA")
- [ ] User can enter location by coordinates (e.g., "47.6062° N, 122.3321° W")
- [ ] App validates the location and displays confirmation
- [ ] If location is invalid, app shows clear error message and allows retry
- [ ] After successful entry, app immediately loads forecast data

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 4.2

---

### US-002: Understand App Purpose

**Story:**  
As a new user, I want to see a brief explanation of what the app does, so that I understand how to use it effectively.

**Acceptance Criteria:**
- [ ] First launch shows optional brief tutorial/welcome screen
- [ ] Tutorial explains the concept of "good moonrise nights"
- [ ] Tutorial shows example of the list view
- [ ] User can skip tutorial
- [ ] Tutorial is accessible later from settings

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** General UX

---

## Epic: Daily Usage

### US-003: Check Tonight's Conditions

**Story:**  
As a moonrise watcher, I want to see today's moonrise information prominently when I open the app, so that I can quickly decide if tonight is a good night to watch.

**Acceptance Criteria:**
- [ ] Today's information appears at the top of the screen
- [ ] Today's section is visually distinct from the forecast list
- [ ] Shows: date, sunset time, moonrise time, azimuth, weather icon/indicator
- [ ] Shows clear "good/bad" indicator for tonight
- [ ] Information is readable without scrolling

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 6.2

---

### US-004: See Upcoming Good Nights

**Story:**  
As a moonrise watcher, I want to quickly scan the next two weeks to find all the good moonrise nights, so that I can plan ahead.

**Acceptance Criteria:**
- [ ] List view shows next 14 days below today's section
- [ ] Good nights are visually highlighted (e.g., green indicator/badge)
- [ ] Bad nights are visually de-emphasized (e.g., gray or red indicator)
- [ ] Each day shows: date, sunset time, moonrise time, weather icon
- [ ] List is scrollable
- [ ] Good nights stand out immediately without reading details

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 6.3, 7.1

---

### US-005: View Detailed Weather

**Story:**  
As a moonrise watcher, I want to see detailed weather information for a specific night, so that I can prepare appropriately (dress warmly, bring rain gear, etc.).

**Acceptance Criteria:**
- [ ] Tapping any day in the list opens detail view
- [ ] Detail view shows: temperature, feels-like temperature (windchill), wind speed, precipitation forecast
- [ ] Detail view shows detailed cloud coverage information
- [ ] Detail view shows moonrise azimuth in degrees and cardinal direction (e.g., "95° ESE")
- [ ] User can close detail view to return to list
- [ ] Detail view loads quickly without noticeable delay

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 6.4

---

### US-006: Know Where to Look

**Story:**  
As a moonrise watcher, I want to know what direction the moon will rise, so that I can position myself correctly and check if my viewing spot has clear sightlines.

**Acceptance Criteria:**
- [ ] Azimuth displayed in both degrees and cardinal direction
- [ ] Format example: "95° (East-Southeast)" or "95° ESE"
- [ ] Visible in both list view (abbreviated) and detail view (full)
- [ ] Direction is accurate for the specified location

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 7.3

---

### US-007: Understand Why a Night is Bad

**Story:**  
As a moonrise watcher, I want to understand why a particular night is marked as unfavorable, so that I can make informed decisions.

**Acceptance Criteria:**
- [ ] Bad nights show indicator explaining reason (e.g., "Moon rises too late", "Poor weather", "Moon phase not ideal")
- [ ] Detail view shows all constraint evaluations
- [ ] User can see that requirements aren't met even if they want to try anyway

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** 1.1, 2.1, 3.1-3.4

---

### US-008: Refresh Forecast Data

**Story:**  
As a moonrise watcher, I want to refresh the weather forecast, so that I can get the most current information as conditions change.

**Acceptance Criteria:**
- [ ] Pull-to-refresh gesture updates forecast
- [ ] Manual refresh button available
- [ ] Loading indicator shown during refresh
- [ ] Timestamp shows when data was last updated
- [ ] Error handling if refresh fails (offline, API error)

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** 3.1

---

## Epic: Location Management

### US-009: Add Additional Location

**Story:**  
As a moonrise watcher who travels, I want to add multiple viewing locations, so that I can check forecasts for different places I visit.

**Acceptance Criteria:**
- [ ] "Add Location" button accessible from main screen or menu
- [ ] Same entry options as first location (city name or coordinates)
- [ ] Each location can be given a custom name (e.g., "Home", "Cabin", "Observatory")
- [ ] Locations are saved permanently until user deletes them
- [ ] No arbitrary limit on number of locations (reasonable limit like 20)

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 4.4

---

### US-010: Switch Between Locations

**Story:**  
As a moonrise watcher with multiple locations, I want to quickly switch between my saved locations, so that I can check forecasts for different places.

**Acceptance Criteria:**
- [ ] Location selector visible and easily accessible
- [ ] Shows current location name
- [ ] Tapping selector shows list of all saved locations
- [ ] Selecting a location immediately loads forecast for that location
- [ ] Currently selected location is visually indicated
- [ ] Location switch is fast (uses cached data when available)

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 4.4

---

### US-011: Edit Location Details

**Story:**  
As a moonrise watcher, I want to edit my saved locations' names or coordinates, so that I can fix mistakes or update information.

**Acceptance Criteria:**
- [ ] Long-press or swipe on location shows edit option
- [ ] Can edit location name
- [ ] Can edit coordinates/city
- [ ] Changes are saved immediately
- [ ] Forecast data refreshes if coordinates changed

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** 4.4

---

### US-012: Delete Location

**Story:**  
As a moonrise watcher, I want to delete locations I no longer use, so that my location list stays organized.

**Acceptance Criteria:**
- [ ] Long-press or swipe on location shows delete option
- [ ] Confirmation prompt before deletion
- [ ] Cannot delete if it's the only location (must have at least one)
- [ ] After deletion, app switches to another saved location

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** 4.4

---

## Epic: Settings and Preferences

### US-013: Set Maximum Moonrise Time

**Story:**  
As a moonrise watcher with a bedtime, I want to set the latest time I'm willing to stay up for moonrise, so that the app only highlights nights that work with my schedule.

**Acceptance Criteria:**
- [ ] Settings screen has "Maximum Moonrise Time" option
- [ ] User can set time using time picker
- [ ] Default is 11:00 PM
- [ ] Setting applies to all locations (or per-location in Phase 2)
- [ ] Changes immediately update good/bad indicators
- [ ] Time is saved persistently

**Priority:** Must Have  
**Story Points:** TBD  
**Related Requirements:** 2.1

---

### US-014: View About/Help Information

**Story:**  
As a moonrise watcher, I want to access help information and app details, so that I can understand how to use features and troubleshoot issues.

**Acceptance Criteria:**
- [ ] About screen accessible from menu
- [ ] Shows app version
- [ ] Shows brief explanation of how "good nights" are determined
- [ ] Links to glossary (azimuth explanation, etc.)
- [ ] Contact/feedback option (if applicable)

**Priority:** Should Have  
**Story Points:** TBD  
**Related Requirements:** General UX

---

## User Story Mapping

### MVP (Phase 1) - Prioritized Stories

**Must Have for MVP:**
1. US-001: Add First Location
2. US-003: Check Tonight's Conditions
3. US-004: See Upcoming Good Nights
4. US-005: View Detailed Weather
5. US-006: Know Where to Look
6. US-013: Set Maximum Moonrise Time

**Should Have for MVP:**
7. US-002: Understand App Purpose
8. US-008: Refresh Forecast Data
9. US-007: Understand Why a Night is Bad

**Phase 2:**
10. US-009: Add Additional Location
11. US-010: Switch Between Locations
12. US-011: Edit Location Details
13. US-012: Delete Location
14. US-014: View About/Help Information

---

## Story Dependencies

```
US-001 (Add First Location)
  └─> US-003 (Check Tonight's Conditions)
       └─> US-004 (See Upcoming Good Nights)
            └─> US-005 (View Detailed Weather)
                 └─> US-006 (Know Where to Look)

US-001 (Add First Location)
  └─> US-009 (Add Additional Location)
       └─> US-010 (Switch Between Locations)
            ├─> US-011 (Edit Location Details)
            └─> US-012 (Delete Location)

US-013 (Set Maximum Moonrise Time) - Independent, affects US-004 display
```

---

## Notes for Development

- Stories marked "Must Have" should be completed for MVP
- Story points will be estimated during sprint planning
- Acceptance criteria will be used to create test cases
- Stories may be split into smaller tasks during implementation
- This document should be updated as stories are completed or requirements change

