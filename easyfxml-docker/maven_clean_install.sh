#!/usr/bin/env bash

MAVEN_PROJECT_FOLDER=$1;

echo "Executing in $(pwd)"
echo "Bash: $(which bash)"
echo "Maven: $(mvn -version)"
echo "Target: ${MAVEN_PROJECT_FOLDER}"

MAVEN_COMMAND="\
mvn clean install \
-Djava.awt.headless=true \
-Dtestfx.robot=glass \
-Dtestfx.headless=true \
-Dprism.order=sw \
-Dprism.text=t2k \
-Dtestfx.setup.timeout=10000 \
-f $MAVEN_PROJECT_FOLDER"

echo "Final maven command = ${MAVEN_COMMAND}"

EXEC="xvfb-run -a ${MAVEN_COMMAND}"

bash -c "${EXEC}"

echo "Finished running tests!"
