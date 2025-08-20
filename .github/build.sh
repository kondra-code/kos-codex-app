#! /bin/bash

set -e -o pipefail

THIS_SCRIPT=$(realpath "$0")
THIS_SCRIPT_DIR=$(dirname "$THIS_SCRIPT")
TOP_DIR="${THIS_SCRIPT_DIR}/.."

cd "${TOP_DIR}"

echo "************ NPM Install ***********"
npm ci
echo "************ NPM run kab ***********"
npm run kab

