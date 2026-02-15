# Settings Wireframe

**Related Requirements:** PRD 1.1, 2.1–2.2, 7.4 | User Stories: US-013, US-014

---

## Screen Elements

| Region         | Element                 | Source  | Notes                               |
|----------------|-------------------------|---------|-------------------------------------|
| Top bar        | Back arrow              | —       | Returns to Main Screen              |
| Top bar        | Title "Settings"        | —       | Standard Material 3 top bar         |
| Viewing Window | Days before full moon   | PRD 1.1 | Stepper, default 2                  |
| Viewing Window | Days after full moon    | PRD 1.1 | Stepper, default 5                  |
| Viewing Window | Forecast period         | PRD 1.1 | Stepper in months, default 3        |
| Time           | Max moonrise time       | PRD 2.1 | Time picker, default 11:00 PM       |
| Time           | Before-sunset tolerance | PRD 2.2 | Stepper in minutes, default 30      |
| Units          | Unit system toggle      | PRD 7.4 | Imperial / Metric, default Imperial |
| About          | About & Help link       | US-014  | Phase 2 — navigates to About screen |

---

## Portrait Layout

```
┌──────────────────────────────────┐
│ ←  Settings                      │  ← top bar
├──────────────────────────────────┤
│                                  │
│  VIEWING WINDOW                  │
│  ─────────────────────────────── │
│                                  │
│  Days before full moon           │
│                        [ − 2 + ] │
│                                  │
│  Days after full moon            │
│                        [ − 5 + ] │
│                                  │
│  Forecast period (months)        │
│                        [ − 3 + ] │
│                                  │
│  TIME CONSTRAINTS                │
│  ─────────────────────────────── │
│                                  │
│  Latest moonrise time            │
│                      [ 11:00 PM ]│
│                                  │
│  Before-sunset tolerance (min)   │
│                       [ − 30 + ] │
│                                  │
│  UNITS                           │
│  ─────────────────────────────── │
│                                  │
│  Unit system                     │
│              [ Imperial | Metric ]│
│                                  │
│  ─────────────────────────────── │
│                                  │
│  About & Help                  > │
│                                  │
└──────────────────────────────────┘
```

## Time Picker (Overlay)

Shown when the user taps the "Latest moonrise time" value.

```
┌──────────────────────────────────┐
│ ←  Settings                      │
├──────────────────────────────────┤
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
│┌────────────────────────────────┐│
││                                ││
││     Latest moonrise time       ││
││                                ││
││       ┌──────┐  ┌──────┐      ││
││       │  11  │  │  00  │      ││
││       └──────┘  └──────┘      ││
││                                ││
││          ○ AM   ● PM           ││
││                                ││
││        [ Cancel ]  [ OK ]      ││
││                                ││
│└────────────────────────────────┘│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
└──────────────────────────────────┘
```

---

## Annotations

### Top Bar

- Material 3 `TopAppBar` with back navigation arrow (`Icons.AutoMirrored.Filled.ArrowBack`)
- Title: "Settings"
- Back arrow returns to Main Screen; changes apply immediately (no save button needed)

### Section Headers

- Labels: "VIEWING WINDOW", "TIME CONSTRAINTS", "UNITS"
- Style: `labelLarge`, bold, `primary` color
- Separator line below each label: `HorizontalDivider`
- 16 dp top margin above each section header (except the first, which has 8 dp)

### Stepper Controls

Used for days before/after full moon, forecast period, and before-sunset tolerance.

- Layout: label on left, stepper on right (same row or label above, stepper right-aligned below)
- Stepper: `[ − value + ]` — minus button, current value, plus button
- Buttons: `IconButton` with `Icons.Default.Remove` and `Icons.Default.Add`
- Value: `bodyLarge`, medium weight, centered between buttons, minimum 40 dp touch width
- Constraints:
    - Days before full moon: 0–7 (integer)
    - Days after full moon: 0–10 (integer)
    - Forecast period: 1–12 months (integer)
    - Before-sunset tolerance: 0–120 minutes (step by 5)
- Labels: `bodyLarge`, `onSurface`
- Description text (if needed): `bodySmall`, `onSurfaceVariant`, below label

### Time Picker

- Tapping the time value opens a Material 3 `TimePickerDialog`
- Shows current value in the tappable field: `bodyLarge`, medium weight, in a bordered container
- Time picker uses 12-hour format with AM/PM toggle
- Minutes step by 15 (11:00 PM, 11:15 PM, 11:30 PM, 11:45 PM, etc.)
- Cancel dismisses without changing; OK applies the new time

### Unit System Toggle

- Material 3 `SegmentedButton` with two segments: "Imperial" and "Metric"
- Selected segment uses `primaryContainer` background
- Label: `bodyLarge`, `onSurface`
- Default: Imperial selected
- Changing the toggle immediately updates all temperature and wind displays app-wide

### About & Help Link

- Full-width tappable row with label on left, chevron (`>`) on right
- Style: `bodyLarge`, `onSurface`
- Navigates to About screen (Phase 2 — not yet designed)
- Separated from the units section by a `HorizontalDivider`

### Immediate Apply Behavior

- All settings changes take effect immediately — no explicit "Save" button
- When the user navigates back to the Main Screen, the forecast recalculates with the new settings
- Settings are persisted to local storage on each change
- If the user force-quits mid-change, the last persisted value is restored

### Padding and Spacing

- Screen horizontal padding: 16 dp
- Top bar to first section: 8 dp
- Section header to first control: 12 dp
- Between controls within a section: 16 dp vertical
- Between sections: 24 dp
- Control row height: minimum 56 dp (Material 3 list item guideline)
