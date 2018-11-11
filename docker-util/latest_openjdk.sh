#!/usr/bin/env bash

JDK_VER=$1
TARGET_FOLDER_NAME=$2

BASE_URL="https://github.com/AdoptOpenJDK/openjdk${JDK_VER}-binaries/releases/latest"
BIN_NAME="OpenJDK${JDK_VER}U-jdk_x64_linux_hotspot"

echo "Will Download latest JDK (release ${JDK_VER}) from AdoptOpenJDK"
echo "Will extract as folder (at relative path to current, $(pwd)) ${TARGET_FOLDER_NAME}"
echo "Base release URL is : ${BASE_URL}"
echo "Expecting binary name to begin with ${BIN_NAME}"

BIN_URL=$(curl -sL ${BASE_URL} | grep "${BIN_NAME}" | grep -v "sha256" | cut -d '=' -f2,3 | cut -d ' ' -f1 | tr -d \" | head -n1)
echo "Found binary download endpoint at ${BIN_URL}"

FULL_URL="https://github.com${BIN_URL}"
echo "Full binary download URL is : ${FULL_URL}"

TARGET_FILE="${TARGET_FOLDER_NAME}.tar.gz"
set -x
wget ${FULL_URL} -O ${TARGET_FILE}
tar xzf ${TARGET_FILE} --transform "s/jdk-[0-9.+]*/${TARGET_FOLDER_NAME}/"
rm ${TARGET_FILE}
set +x

echo "Done, set \$JAVA_HOME to $(pwd)/$TARGET_FOLDER_NAME"
