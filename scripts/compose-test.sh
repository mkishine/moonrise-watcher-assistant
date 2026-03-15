#!/usr/bin/env bash
# Usage: scripts/compose-test.sh
#
# Ensures the Pixel6_API33 emulator is running, then runs instrumented Compose tests.
# If no emulator is connected, starts Pixel6_API33 and waits for it to fully boot.

set -euo pipefail

ADB="$HOME/AppData/Local/Android/Sdk/platform-tools/adb.exe"
EMULATOR="$HOME/AppData/Local/Android/Sdk/emulator/emulator.exe"
AVD="Pixel6_API33"

if "$ADB" devices | grep -q "^emulator.*device$"; then
    echo "Emulator already running."
else
    echo "Starting $AVD..."
    "$EMULATOR" -avd "$AVD" &

    echo "Waiting for emulator to boot (this may take a minute)..."
    "$ADB" wait-for-device
    until [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; do
        sleep 3
    done
    echo "Emulator ready."
fi

./gradlew connectedDebugAndroidTest
