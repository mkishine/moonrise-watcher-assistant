#!/usr/bin/env bash
# Creates an AVD matching the project's recommended configuration.
# See docs/random-notes/avd-configuration.md for rationale.
#
# Usage: scripts/run.sh create-avd scripts/create-avd.sh

set -xeuo pipefail

AVD_NAME="Pixel6_API33"
PACKAGE="system-images;android-33;google_apis;x86_64"
DEVICE="pixel_6"

# Ensure the system image is installed
if ! sdkmanager --list_installed 2>/dev/null | grep -q "$PACKAGE"; then
    echo "==> Installing system image: $PACKAGE"
    sdkmanager "$PACKAGE"
else
    echo "==> System image already installed: $PACKAGE"
fi

# Delete existing AVD with the same name (if any)
if avdmanager list avd -c 2>/dev/null | grep -q "^${AVD_NAME}$"; then
    echo "==> Deleting existing AVD: $AVD_NAME"
    avdmanager delete avd -n "$AVD_NAME"
fi

# Create the AVD (--force to skip interactive prompt)
echo "==> Creating AVD: $AVD_NAME"
echo "no" | avdmanager create avd \
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
