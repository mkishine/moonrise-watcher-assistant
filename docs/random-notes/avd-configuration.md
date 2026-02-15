# AVD Configuration Notes

## Host PC Specs

| Component | Details                            |
|-----------|------------------------------------|
| CPU       | Intel i7-8700 (6 cores/12 threads) |
| RAM       | 32 GB                              |
| GPU       | Intel UHD Graphics 630 (1 GB)      |

## Recommended AVD Setup

| Setting           | Value                         | Why                                                    |
|-------------------|-------------------------------|--------------------------------------------------------|
| **Device**        | Pixel 6 (or Medium Phone)     | 1080x2400 — standard size, not oversized               |
| **System Image**  | API 33 (Android 13), x86_64   | Good balance of modern features vs. emulator weight    |
| **Image variant** | Google APIs (not Google Play) | Lighter than Play images, sufficient for development   |
| **RAM**           | 3072 MB                       | Comfortable for API 33; 32 GB host has plenty to spare |
| **VM Heap**       | 512 MB                        | Default is fine                                        |
| **Storage**       | 2048 MB                       | Enough for app installs                                |
| **GPU**           | Hardware - GLES 2.0 (Auto)    | Uses Intel UHD 630                                     |
| **Boot option**   | Quick Boot (default)          | Snapshot-based — fast restarts after first cold boot   |
| **Multi-Core**    | 4 cores                       | i7-8700 has 6c/12t, 4 for AVD is fine                  |

## Prerequisites

**Status (2026-02-15):** HypervisorPresent = False. No hypervisor active — emulator is using slow
software emulation.

Enable **Windows Hypervisor Platform** for hardware acceleration. Open PowerShell as Administrator
and run:

```powershell
Enable-WindowsOptionalFeature -Online -FeatureName HypervisorPlatform
Enable-WindowsOptionalFeature -Online -FeatureName VirtualMachinePlatform
```

Reboot required after enabling. Alternatively, use the GUI:

```
Settings > Apps > Optional features > More Windows features >
  ☑ Hyper-V
  ☑ Virtual Machine Platform
  ☑ Windows Hypervisor Platform
```

## Why API 33 Instead of 36?

The app targets `minSdk 26` and `compileSdk 36`. API 33 boots faster and runs smoother for
day-to-day development. Add a second API 36 AVD later for compatibility testing.

## What Affects AVD Performance

- **API version:** Higher = heavier (more background services). Image type matters more (x86_64 vs
  ARM).
- **Screen size:** Negligible impact (GPU-rendered on host).
- **Biggest wins:** x86_64 image + hardware acceleration + Quick Boot snapshots + adequate RAM.
