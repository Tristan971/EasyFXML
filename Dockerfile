FROM alpine:latest

RUN wget https://download.java.net/java/early_access/alpine/18/binaries/openjdk-12-ea+18_linux-x64-musl_bin.tar.gz -O jdk12.tar.gz
RUN mkdir /opt
RUN tar xzf jdk12.tar.gz && rm jdk12.tar.gz

RUN apk -q add \
    maven \
    bash \
    xvfb \
    dbus-x11 \
    fontconfig \
    gcompat \
    gtk+3.0 \
    gtk+2.0 \
    firefox-esr \
    msttcorefonts-installer

RUN update-ms-fonts && fc-cache -f

ENV JAVA_HOME="/jdk-12"
ENV PATH="$PATH:$JAVA_HOME/bin"
ENV DISPLAY=:99
ENV term=linux

ADD . /EasyFXML
WORKDIR /EasyFXML
RUN echo "Downloading maven dependencies" && mvn dependency:go-offline -T4C

RUN chmod +x docker-util/*

ENTRYPOINT ["./docker-util/run_tests.sh"]
