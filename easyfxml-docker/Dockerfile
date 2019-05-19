FROM adoptopenjdk/maven-openjdk11:latest

MAINTAINER Tristan Deloche <tristandeloche@gmail.com>

RUN apt update -qq

RUN apt install -qq -y \
    apt-utils \
    apt-transport-https \
    xvfb \
    git \
    dbus-x11 \
    chromium-browser

ENV term xterm-256color

ADD ./maven_clean_install.sh /bin/maven_clean_install

ENTRYPOINT ["/bin/bash"]
