#!/usr/bin/env bash

echo "Info for runtime config of tests in $(pwd)"
echo "Bash at $(which bash)"
echo "Maven at $(which mvn) with config $(mvn -version)"

PREFLIGHT="mvn -q dependency:go-offline"
CMD="mvn clean install"

echo "Test command = ${CMD}"

set -x
${PREFLIGHT}
bash -c "./docker-util/xvfb-run.sh -a ${CMD}"
set +x

echo "Finished running tests!"
