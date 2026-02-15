# Location Selector Wireframe

**Related Requirements:** PRD 4.3 | User Stories: US-010, US-011, US-012

**Phase Note:** Location management is a Phase 2 feature. This wireframe is included now for
complete design coverage. The MVP (Phase 1) supports only a single location.

---

## Screen Elements

| Region         | Element                | Source  | Notes                                        |
|----------------|------------------------|---------|----------------------------------------------|
| Header         | Title "Locations"      | —       | Bottom sheet header                          |
| Header         | Close button           | —       | X icon to dismiss sheet                      |
| Location list  | Location name          | US-010  | e.g. "Home — Seattle, WA"                    |
| Location list  | Selection indicator    | US-010  | Checkmark on currently active location       |
| Location list  | Coordinates subtitle   | US-010  | e.g. "47.61°N, 122.33°W"                    |
| Footer         | Add Location button    | US-009  | Opens Add Location screen                    |
| Context menu   | Edit option            | US-011  | Opens edit flow for the long-pressed location |
| Context menu   | Delete option          | US-012  | Shows delete confirmation dialog             |
| Dialog         | Delete confirmation    | US-012  | Confirms before deleting a location          |

---

## Sample Data

| Location Name         | Coordinates            | Active |
|-----------------------|------------------------|--------|
| Home — Seattle, WA    | 47.61°N, 122.33°W     | Yes    |
| Cabin — Leavenworth   | 47.60°N, 120.66°W     | No     |
| Observatory — Goldendale | 45.82°N, 120.82°W  | No     |

---

## Portrait Layout — Normal

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │  ← main screen (dimmed)
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
├──────────────────────────────────┤
│          ── drag handle ──       │  ← bottom sheet
│                                  │
│  Locations                    ✕  │
│  ─────────────────────────────── │
│                                  │
│  ✓ Home — Seattle, WA           │
│    47.61°N, 122.33°W             │
│  ─────────────────────────────── │
│    Cabin — Leavenworth           │
│    47.60°N, 120.66°W             │
│  ─────────────────────────────── │
│    Observatory — Goldendale      │
│    45.82°N, 120.82°W             │
│  ─────────────────────────────── │
│                                  │
│  + Add Location                  │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Context Menu (Long-Press)

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
├──────────────────────────────────┤
│          ── drag handle ──       │
│                                  │
│  Locations                    ✕  │
│  ─────────────────────────────── │
│                                  │
│  ✓ Home — Seattle, WA           │
│    47.61°N, 122.33°W             │
│  ─────────────────────────────── │
│  ┌─────────────────┐             │
│  │  ✎ Edit         │◄── context  │
│  │  🗑 Delete       │    menu     │
│  └─────────────────┘             │
│    Cabin — Leavenworth           │  ← long-pressed item
│    47.60°N, 120.66°W             │
│  ─────────────────────────────── │
│    Observatory — Goldendale      │
│    45.82°N, 120.82°W             │
│  ─────────────────────────────── │
│                                  │
│  + Add Location                  │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Delete Confirmation

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░ ┌────────────────────┐ ░░░░░│
│░░░░░ │                    │ ░░░░░│
│░░░░░ │  Delete location?  │ ░░░░░│
│░░░░░ │                    │ ░░░░░│
│░░░░░ │  "Cabin —          │ ░░░░░│
│░░░░░ │   Leavenworth"     │ ░░░░░│
│░░░░░ │  will be           │ ░░░░░│
│░░░░░ │  permanently       │ ░░░░░│
│░░░░░ │  removed.          │ ░░░░░│
│░░░░░ │                    │ ░░░░░│
│░░░░░ │ [ Cancel ] [Delete]│ ░░░░░│
│░░░░░ │                    │ ░░░░░│
│░░░░░ └────────────────────┘ ░░░░░│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
└──────────────────────────────────┘
```

---

## Annotations

### Bottom Sheet Behavior

- Presented as a Material 3 `ModalBottomSheet`
- Drag handle at top, standard Material 3 styling
- Triggered by tapping the location name in the Main Screen top bar
- Sheet height adjusts to content (wraps to fit the location list)
- Maximum height: 60% of screen
- If the list exceeds maximum height, the list becomes scrollable
- Closing: swipe down, tap scrim, tap close (✕), or system back

### Header

- Title "Locations": `titleMedium`, bold, left-aligned
- Close button (✕): `IconButton` with `Icons.Default.Close`, right-aligned, same row
- `HorizontalDivider` below header row

### Location List Items

- Each item is a tappable row
- **Active location:** checkmark (`✓`) prefix, `bodyLarge`, medium weight
- **Inactive locations:** no prefix (indented to align with active item text)
- Subtitle: coordinates in compact format (`"47.61°N, 122.33°W"`), `bodySmall`, `onSurfaceVariant`
- `HorizontalDivider` between items
- Tap: switches to tapped location, closes sheet, Main Screen refreshes
- Long-press: shows context menu (see below)
- Item height: minimum 64 dp (two-line list item)

### Context Menu

- Material 3 `DropdownMenu` anchored to the long-pressed item
- Options:
  - **Edit** (`✎`): opens Add Location screen pre-filled with the location's data
  - **Delete** (`🗑`): shows delete confirmation dialog
- Menu appears above the long-pressed item
- Tapping outside the menu dismisses it

### Delete Confirmation Dialog

- Material 3 `AlertDialog`
- Title: "Delete location?"
- Body: `"{location name}" will be permanently removed.`
- Buttons:
  - "Cancel" — `TextButton`, dismisses dialog
  - "Delete" — `TextButton`, `error` color, performs deletion
- After deletion: if the deleted location was active, switch to the first remaining location
- **Single-location guard:** if only one location exists, the Delete option in the context menu is
  disabled (grayed out) with no tooltip — the user must always have at least one location (US-012)

### Add Location Button

- Full-width tappable row at the bottom of the list
- Icon: `+` (`Icons.Default.Add`) + "Add Location" label
- Style: `bodyLarge`, `primary` color
- Tapping opens the Add Location screen (additional location context)
- 16 dp vertical padding

### Edit Flow

- Selecting "Edit" from the context menu opens the Add Location screen
- Fields are pre-populated with the location's current data
- Top bar title changes to "Edit Location"
- Primary button text changes to "Save"
- Saving updates the existing location (does not create a duplicate)

### Padding and Spacing

- Sheet horizontal padding: 16 dp
- Header vertical padding: 16 dp top, 8 dp bottom
- List item padding: 16 dp horizontal, 12 dp vertical
- Between last item and Add Location button: 8 dp
- Add Location button padding: 16 dp horizontal, 16 dp vertical
- Bottom safe area inset respected
