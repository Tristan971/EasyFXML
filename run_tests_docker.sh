#!/usr/bin/env bash

DISPLAY=:99
Xvfb ${DISPLAY}
sleep 3

mvn clean install -T2C
