#!/bin/sh
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle is not installed. Please install Gradle 8.6 or newer." >&2
  exit 1
fi
