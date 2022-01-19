#!/bin/bash
set -e

# Build the services
pushd TokenService
./build.sh
popd