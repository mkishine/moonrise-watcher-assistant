#!/usr/bin/env bash
# Creates an AVD matching the project's recommended configuration.
# See docs/random-notes/avd-configuration.md for rationale.
#
# Usage: scripts/run.sh create-avd scripts/create-avd.sh

set -xeuo pipefail

# Locate Android SDK — check ANDROID_HOME, then the default Windows location
if [[ -n "${ANDROID_HOME:-}" && -d "$ANDROID_HOME" ]]; then
    SDK_ROOT="$ANDROID_HOME"
elif [[ -n "${ANDROID_SDK_ROOT:-}" && -d "$ANDROID_SDK_ROOT" ]]; then
    SDK_ROOT="$ANDROID_SDK_ROOT"
elif [[ -d "$HOME/AppData/Local/Android/Sdk" ]]; then
    SDK_ROOT="$HOME/AppData/Local/Android/Sdk"
else
    echo "ERROR: Android SDK not found. Set ANDROID_HOME or install the SDK."
    exit 1
fi

# Convert Windows-style path (C:\...) to Unix-style (/c/...) for Git Bash PATH
SDK_ROOT="$(cd "$SDK_ROOT" && pwd)"

echo "==> Using Android SDK at: $SDK_ROOT"

# Git Bash may not resolve .bat extensions automatically — alias the SDK tools
SDKMANAGER="$SDK_ROOT/cmdline-tools/latest/bin/sdkmanager.bat"
AVDMANAGER="$SDK_ROOT/cmdline-tools/latest/bin/avdmanager.bat"

for tool in "$SDKMANAGER" "$AVDMANAGER"; do
    if [[ ! -f "$tool" ]]; then
        echo "ERROR: $(basename "$tool") not found at $tool"
        exit 1
    fi
done

AVD_NAME="Pixel6_API33"
PACKAGE="system-images;android-33;google_apis;x86_64"
DEVICE="pixel_6"

# Ensure the system image is installed
if ! "$SDKMANAGER" --list_installed 2>/dev/null | grep -q "$PACKAGE"; then
    echo "==> Installing system image: $PACKAGE"
    "$SDKMANAGER" "$PACKAGE"
else
    echo "==> System image already installed: $PACKAGE"
fi

# Delete existing AVD with the same name (if any)
if "$AVDMANAGER" list avd -c 2>/dev/null | grep -q "^${AVD_NAME}$"; then
    echo "==> Deleting existing AVD: $AVD_NAME"
    "$AVDMANAGER" delete avd -n "$AVD_NAME"
fi

# Create the AVD (--force to skip interactive prompt)
echo "==> Creating AVD: $AVD_NAME"
echo "no" | "$AVDMANAGER" create avd \
    -n "$AVD_NAME" \
    -k "$PACKAGE" \
    -d "$DEVICE" \
    --force

# Locate config.ini and apply hardware settings
AVD_DIR="$HOME/.android/avd/${AVD_NAME}.avd"
CONFIG="$AVD_DIR/config.ini"

if [[ ! -f "$CONFIG" ]]; then
    echo "ERROR: config.ini not found at $CONFIG"
    exit 1
fi

echo "==> Applying hardware settings to $CONFIG"

apply_setting() {
    local key="$1" value="$2"
    if grep -q "^${key} " "$CONFIG" 2>/dev/null; then
        sed -i "s|^${key} .*|${key} = ${value}|" "$CONFIG"
    else
        echo "${key} = ${value}" >> "$CONFIG"
    fi
}

# RAM: 3072 MB
apply_setting "hw.ramSize"       "3072"

# VM Heap: 512 MB
apply_setting "hw.mainKeys"      "no"
apply_setting "hw.keyboard"      "yes"

# CPU cores: 4
apply_setting "hw.cpu.ncore"     "4"

# Internal storage: 2048 MB
apply_setting "disk.dataPartition.size" "2048M"

# GPU: hardware acceleration (auto)
apply_setting "hw.gpu.enabled"   "yes"
apply_setting "hw.gpu.mode"      "auto"

# Screen: Pixel 6 defaults (1080x2400)
apply_setting "hw.lcd.width"     "1080"
apply_setting "hw.lcd.height"    "2400"
apply_setting "hw.lcd.density"   "411"

echo ""
echo "==> AVD '$AVD_NAME' created successfully."
echo "    Launch with:  emulator -avd $AVD_NAME"
