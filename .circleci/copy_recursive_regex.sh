#!/usr/bin/env bash

REGEX="$1"
DEST_FOLDER=$2

rm -rf ${DEST_FOLDER}
mkdir -p ${DEST_FOLDER}

for testXml in $(find . -type f -regex ${REGEX}); do
    echo ${testXml}
    cp ${testXml} ${DEST_FOLDER}
done

echo
