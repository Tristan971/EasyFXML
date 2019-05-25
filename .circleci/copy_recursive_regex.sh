#!/usr/bin/env bash

REGEX="$1"
DEST_FOLDER=$2

rm -rf ${DEST_FOLDER}
mkdir -p ${DEST_FOLDER}

find . -type f -regex ${REGEX} \
-exec echo {} \; \
-exec cp {} ${DEST_FOLDER} \;
