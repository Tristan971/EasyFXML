#!/usr/bin/env bash

REGEX="$1"
DEST_FOLDER=$2

rm -rf ${DEST_FOLDER}
mkdir -p ${DEST_FOLDER}

for matching_file in $(find . -type f -regex ${REGEX}); do
    echo ${matching_file}
    cp ${matching_file} ${DEST_FOLDER}
done

echo
