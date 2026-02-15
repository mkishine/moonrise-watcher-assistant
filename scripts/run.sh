#!/usr/bin/env bash
# Usage: scripts/run.sh <description> <command...>
# Example: scripts/run.sh build ./gradlew assembleDebug
#
# Logs command output to logs/YYYYMMDD-HHMMSS-<description>.log
# Output appears in both terminal and log file via tee.

set -euo pipefail

if [[ $# -lt 2 ]]; then
    echo "Usage: scripts/run.sh <description> <command...>"
    echo "Example: scripts/run.sh build ./gradlew assembleDebug"
    exit 1
fi

description="$1"
shift

# Sanitize description for filename (replace spaces/special chars with hyphens)
safe_desc=$(echo "$description" | tr ' /' '-' | tr -cd '[:alnum:]-')

log_dir="$(cd "$(dirname "$0")/.." && pwd)/logs"
mkdir -p "$log_dir"

timestamp=$(date +"%Y%m%d-%H%M%S")
log_file="${log_dir}/${timestamp}-${safe_desc}.log"

echo "--- Command: $*" | tee "$log_file"
echo "--- Log: $log_file" | tee -a "$log_file"
echo "---" | tee -a "$log_file"

"$@" 2>&1 | tee -a "$log_file"
