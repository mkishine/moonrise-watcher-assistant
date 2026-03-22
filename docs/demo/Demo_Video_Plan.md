# Demo Video Plan

A guide for creating a short demo video of the Moonrise Watcher Assistant app to share with
friends.

---

## Phase 1: Plan Your Script

Before recording anything, write out exactly what you will show and say. Keep it tight.

### Draft Script Outline

| Timestamp   | Narration                                                                                                                                                                       | On-Screen Action             |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------|
| 0:00 – 0:15 | "Here's an app I built that tells you the best nights to watch the moonrise."                                                                                                   | Show a green GOOD night card |
| 0:15 – 0:35 | "To get a great moonrise view, three things have to line up — the moon has to be near full, it has to rise after sunset but before you go to bed, and the sky has to be clear." | Static or slow scroll        |
| 0:35 – 2:00 | Walkthrough narration (see Phase 3)                                                                                                                                             | Full app demo                |
| 2:00 – 2:15 | "I built this because I kept missing good nights. Now it tells me in advance."                                                                                                  | Upcoming nights list         |

---

## Phase 2: Prepare Your Device

### Clean Up the Screen

- Set display to stay awake: **Settings → Developer Options → Stay Awake**
- Enable Do Not Disturb so no notifications appear during recording
- Set brightness to ~75% (not too dim, not blown out)

### Set Up Good Demo Data

- Use a location where the forecast shows at least a few green GOOD nights
- The next full moon is approximately **April 13, 2026** — ideal recording window is
  roughly April 8–18
- If recording before the phase window, use the detail sheet to show what a good night
  looks like and explain the criteria

---

## Phase 3: Record the Screen

### Option A — Android Built-In (Easiest)

1. Swipe down twice to open Quick Settings
2. Tap the **Screen Record** tile
3. Choose microphone audio for live voiceover, or record silently and dub later
4. Recording is saved to Photos/Gallery

### Option B — scrcpy from PC (Better Quality)

```bash
# Install scrcpy on Windows
winget install Genymobile.scrcpy

# Connect phone via USB, then run:
scrcpy --record demo.mp4
```

Mirrors and records simultaneously. Produces a clean MP4 on your PC.

### Option C — Android Studio Emulator

- Run the app in an emulator
- Use the camera icon in the emulator toolbar to record
- Easier to control but looks slightly less authentic than a real device

### Recording Tips

- Do a full dry run before recording for real
- Slow down your taps — what feels natural in person looks rushed on video
- If you make a mistake, pause and redo that section; cut it in editing
- You can record voiceover live or dub it in post — both work fine

### Walkthrough Sequence (0:35 – 2:00)

1. Open the app — show today's forecast card
2. Scroll the upcoming nights list — point out green vs. hidden days
3. Tap a GOOD night to open the detail sheet — show moonrise time, azimuth, weather
4. Go back, tap the location selector — switch to a second location
5. Show that the forecast reloads for the new location

---

## Phase 4: Edit the Video

### Tools by Effort Level

| Tool            | Platform | Effort | Notes                                              |
|-----------------|----------|--------|----------------------------------------------------|
| CapCut          | Mobile   | Low    | Free, beginner-friendly, good for captions and TTS |
| DaVinci Resolve | PC       | Medium | Free, professional quality, steeper learning curve |

### Voiceover: Text-to-Speech Options

If you are not confident in your own voiceover, use a text-to-speech tool instead.

**Option 1 — CapCut built-in TTS (recommended, lowest friction):**
1. Add your screen recording to the timeline
2. Add a **Text** layer with your narration text
3. Tap the text layer → **Text-to-Speech**
4. Choose a voice (many natural-sounding options available)
5. CapCut generates audio and syncs it to the text segment automatically

**Option 2 — ElevenLabs (best voice quality):**
- Free tier provides ~10 minutes/month — plenty for a 2-minute demo
- Paste your script, pick a voice, download as MP3
- Import the MP3 into CapCut or DaVinci Resolve and align it to the video

**Recommendation:** Start with CapCut's built-in TTS. If the voices sound too robotic,
switch to ElevenLabs for the audio and bring the MP3 back into CapCut.

### Editing Checklist

- [ ] Trim the start and end (remove the tap-to-record moment)
- [ ] Cut any hesitations or mistakes
- [ ] Add text overlays for key moments (e.g., "Today's forecast", "Tap for details")
- [ ] Add voiceover — live recording, CapCut TTS, or ElevenLabs MP3
- [ ] Add subtle background music (YouTube Audio Library has free tracks)

---

## Phase 5: Export and Share

- Export at **1080p, 30 fps** — good balance of quality and file size
- **Sharing with friends:** WhatsApp, iMessage, or YouTube (unlisted link)
- **Wider audience:** YouTube or a short clip on Twitter/X
