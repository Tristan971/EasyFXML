FROM adoptopenjdk/maven-openjdk11:latest

RUN apt update

RUN apt install -y \
    apt-utils \
    apt-transport-https \
    xvfb \
    git \
    dbus-x11 \
    chromium-browser

# Enable colored output
ENV term xterm

# Copy current version of git repo
# Explicitly include .git folder as well for git info during build
COPY . /EasyFXML
COPY .git/ /EasyFXML/.git/

RUN chmod +x ./EasyFXML/docker-util/*

# Copy local maven repo
# Move into repo
# Pre-download required maven dependencies to limit dependency resolution output in compile-test step
WORKDIR /EasyFXML

# Start tests
ENTRYPOINT ["./docker-util/run_tests.sh"]
