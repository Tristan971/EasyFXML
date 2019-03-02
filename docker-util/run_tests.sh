#!/usr/bin/env bash

echo "Info for runtime config of tests in $(pwd)"
echo "Bash at $(which bash)"
echo "Maven at $(which mvn) with config $(mvn -version)"

COV_MODULE="easyfxml"
echo "Preparing test coverage reporting in module $COV_MODULE:"
curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 > ./${COV_MODULE}/cc-test-reporter
chmod +x ./${COV_MODULE}/cc-test-reporter
echo "Will use CodeClimate's test reporter at $(pwd)/${COV_MODULE}/cc-test-reporter"
echo "Set before-build notice"
./${COV_MODULE}/cc-test-reporter before-build

CMD="mvn clean install -Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw -Dprism.text=t2k -Dtestfx.setup.timeout=10000"

echo "Test command = ${CMD}"

set -x
bash -c "./docker-util/xvfb-run.sh -a ${CMD}"
set +x

echo "Finished running tests!"

echo "Notifying CodeClimate of test build's end"

cd ${COV_MODULE}
JACOCO_SOURCE_PATH=src/main/java ./cc-test-reporter format-coverage target/site/jacoco/jacoco.xml --input-type jacoco
./cc-test-reporter upload-coverage -r 9791cde00c987e47a9082b96f73a2b4eb3590f308c501a3c61d34e0276c93ec1
