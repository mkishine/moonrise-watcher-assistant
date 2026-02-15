# Add Location Wireframe

**Related Requirements:** PRD 4.1–4.2 | User Stories: US-001, US-009

---

## Screen Elements

| Region     | Element              | Source  | Notes                                                  |
|------------|----------------------|---------|--------------------------------------------------------|
| Top bar    | Back/Cancel          | —       | Cancel (additional location) or no back (first-time)   |
| Top bar    | Title                | —       | "Add Location" or "Set Up Location"                    |
| Input mode | Tab selector         | PRD 4.2 | City Name / Coordinates toggle                         |
| City tab   | City/State field     | PRD 4.2 | Text input, e.g. "Seattle, WA"                         |
| Coords tab | Latitude field       | PRD 4.2 | Numeric input, e.g. "47.6062"                          |
| Coords tab | Longitude field      | PRD 4.2 | Numeric input, e.g. "-122.3321"                        |
| Common     | Custom name field    | US-009  | Optional, e.g. "Home", "Cabin"                         |
| Common     | Add/Save button      | —       | Primary action button                                  |
| Feedback   | Error message        | US-001  | Shown below input on validation failure                |
| Feedback   | Loading indicator    | US-001  | Shown during geocoding/validation                      |

---

## Portrait Layout — First-Time Setup (City Tab)

```
┌──────────────────────────────────┐
│       Set Up Location            │  ← centered top bar, no back
├──────────────────────────────────┤
│                                  │
│              ☽                   │
│                                  │
│   Where will you be watching     │
│   the moonrise?                  │
│                                  │
│   [ City Name | Coordinates ]    │  ← tab selector
│                                  │
│   City, State                    │
│   ┌────────────────────────────┐ │
│   │ Seattle, WA                │ │
│   └────────────────────────────┘ │
│                                  │
│   Location name (optional)       │
│   ┌────────────────────────────┐ │
│   │ Home                       │ │
│   └────────────────────────────┘ │
│                                  │
│     [ Get Started ]              │  ← primary button
│                                  │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Additional Location (Coordinates Tab)

```
┌──────────────────────────────────┐
│ ←  Add Location                  │  ← top bar with back
├──────────────────────────────────┤
│                                  │
│   [ City Name | Coordinates ]    │  ← tab selector
│                                  │
│   Latitude                       │
│   ┌────────────────────────────┐ │
│   │ 47.6062                    │ │
│   └────────────────────────────┘ │
│                                  │
│   Longitude                      │
│   ┌────────────────────────────┐ │
│   │ -122.3321                  │ │
│   └────────────────────────────┘ │
│                                  │
│   Location name (optional)       │
│   ┌────────────────────────────┐ │
│   │ Observatory                │ │
│   └────────────────────────────┘ │
│                                  │
│           [ Save ]               │  ← primary button
│                                  │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Validation Error

```
┌──────────────────────────────────┐
│ ←  Add Location                  │
├──────────────────────────────────┤
│                                  │
│   [ City Name | Coordinates ]    │
│                                  │
│   City, State                    │
│   ┌────────────────────────────┐ │
│   │ Xyzzyville, ZZ             │ │
│   └────────────────────────────┘ │
│   ⚠ Location not found. Check   │
│     spelling and try again.      │
│                                  │
│   Location name (optional)       │
│   ┌────────────────────────────┐ │
│   │                            │ │
│   └────────────────────────────┘ │
│                                  │
│           [ Save ]               │  ← disabled while error
│                                  │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Loading

```
┌──────────────────────────────────┐
│ ←  Add Location                  │
├──────────────────────────────────┤
│                                  │
│   [ City Name | Coordinates ]    │
│                                  │
│   City, State                    │
│   ┌────────────────────────────┐ │
│   │ Portland, OR               │ │
│   └────────────────────────────┘ │
│                                  │
│   Location name (optional)       │
│   ┌────────────────────────────┐ │
│   │ Weekend Spot               │ │
│   └────────────────────────────┘ │
│                                  │
│       [ ◌ Verifying... ]         │  ← loading button
│                                  │
│                                  │
└──────────────────────────────────┘
```

---

## Annotations

### Context Variants

The Add Location screen has two contexts that affect its appearance:

| Aspect         | First-Time Setup                   | Additional Location              |
|----------------|------------------------------------|----------------------------------|
| Top bar        | `CenterAlignedTopAppBar`, no back  | `TopAppBar` with back arrow      |
| Title          | "Set Up Location"                  | "Add Location"                   |
| Welcome art    | Moon icon + subtitle shown         | No welcome art                   |
| Primary button | "Get Started"                      | "Save"                           |
| Cancel         | Not possible (must add a location) | Back arrow or system back        |

### Tab Selector

- Material 3 `SegmentedButton` with two segments: "City Name" and "Coordinates"
- Default: "City Name" selected
- Switching tabs clears the input fields of the other tab (but preserves the custom name)
- Selected segment uses `primaryContainer` background

### City Name Input

- Material 3 `OutlinedTextField`
- Label: "City, State"
- Placeholder: e.g. "Seattle, WA" (light gray)
- Keyboard type: default text
- Supports free-form text; geocoding resolves to coordinates
- Validation: triggered on button press, not on each keystroke

### Coordinate Inputs

- Two Material 3 `OutlinedTextField` fields
- Labels: "Latitude", "Longitude"
- Keyboard type: decimal number with sign
- Latitude range: -90 to 90
- Longitude range: -180 to 180
- Validation: format check on button press
- Error messages:
  - Invalid latitude: "Latitude must be between -90 and 90"
  - Invalid longitude: "Longitude must be between -180 and 180"
  - Non-numeric: "Enter a valid number"

### Custom Name Field

- Material 3 `OutlinedTextField`
- Label: "Location name (optional)"
- If left empty, the resolved city name is used as the display name
- Maximum length: 30 characters
- Appears in both City Name and Coordinates tabs

### Error States

- Error text appears below the relevant input field
- Style: `bodySmall`, `error` color
- Warning icon (`⚠`) prefix
- Input field border changes to `error` color
- Error messages:
  - City not found: "Location not found. Check spelling and try again."
  - Network error: "Unable to verify location. Check your connection."
  - Invalid coordinates: field-specific messages (see above)
- Save/Get Started button is disabled while an error is displayed
- Editing the input field clears the error

### Loading State

- Triggered when the user taps Save/Get Started
- Button text changes to "Verifying..." with a `CircularProgressIndicator`
- Input fields become read-only during loading
- Tab selector is disabled during loading
- On success: navigates to Main Screen (first-time) or back to previous screen (additional)
- On failure: shows error state (see above)

### First-Time Welcome Art

- Moon icon (`☽`): `headlineLarge`, centered
- Subtitle: "Where will you be watching the moonrise?"
- Style: `bodyLarge`, `onSurfaceVariant`, centered
- 16 dp between icon and subtitle, 24 dp between subtitle and tab selector

### Padding and Spacing

- Screen horizontal padding: 24 dp
- Top bar to content: 16 dp (additional location) or 24 dp (first-time, after top bar)
- Between tab selector and first input: 20 dp
- Between input fields: 16 dp
- Between last input and button: 24 dp
- Button: full width minus horizontal padding, `FilledButton` (first-time: `FilledTonalButton`)
- Button height: 48 dp
