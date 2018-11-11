FROM alpine:latest

RUN apk -q add \
    sudo \
    curl \
    maven \
    bash \
    xvfb \
    dbus-x11 \
    fontconfig \
    gcompat \
    gtk+3.0 \
    gtk+2.0 \
    firefox-esr

RUN apk --update add tar

RUN fc-cache -f

ADD . /EasyFXML
RUN chmod +x ./EasyFXML/docker-util/*

RUN ./EasyFXML/docker-util/latest_openjdk.sh 11 OpenJDK11

ENV JAVA_HOME "/OpenJDK11"
ENV PATH "$PATH:$JAVA_HOME/bin"
ENV DISPLAY :99
ENV term linux

WORKDIR /EasyFXML
RUN echo "Downloading maven dependencies" && mvn dependency:go-offline -T4C

ENTRYPOINT ["./docker-util/run_tests.sh"]
