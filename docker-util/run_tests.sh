#!/usr/bin/env bash

echo "Info for runtime config of tests in $(pwd)"
echo "Bash at $(which bash)"
echo "Maven at $(which mvn) with config $(mvn -version)"

XVFB="./docker-util/xvfb-run.sh"
CMD="mvn clean install"

echo "- xvfb-run.sh ran at $(pwd)/${XVFB}"
echo "- test command = ${CMD}"

${XVFB} ${CMD}

echo "Finished running tests!"
