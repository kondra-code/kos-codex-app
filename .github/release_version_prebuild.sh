#! /bin/bash
THIS_SCRIPT=$(realpath "$0")
THIS_SCRIPT_DIR=$(dirname "$THIS_SCRIPT")
TOP_DIR="${THIS_SCRIPT_DIR}/.."

set -e -o pipefail

if [ -z "${KOSBUILD_VERSION}" ]; then
   echo "check release version is for release builds, KOSBUILD_VERSION must be defined"
   exit 1
fi

cd "${TOP_DIR}"
npx nx run kosdev-codex-ui:version --args=--ver="${KOSBUILD_VERSION}"

exit 0