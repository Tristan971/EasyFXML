FROM alpine:latest

# Use JDK12 as there are no musl-compatible JDK11 releases for now
RUN wget "https://download.java.net/java/early_access/alpine/18/binaries/openjdk-12-ea+18_linux-x64-musl_bin.tar.gz" -O jdk12.tar.gz
RUN tar xzf jdk12.tar.gz && rm jdk12.tar.gz
RUN mkdir /opt
RUN mv jdk-12 /opt/

# Set required packages
RUN apk -q add \
    maven \
    bash \
    xvfb \
    dbus-x11 \
    fontconfig \
    gcompat \
    gtk+3.0 \
    gtk+2.0 \
    firefox-esr
RUN fc-cache -f

# Set required environment variables
ENV JAVA_HOME="/opt/jdk-12"
ENV PATH="$PATH:$JAVA_HOME/bin"
ENV DISPLAY=:99
ENV term=linux

# Copy current version of git repo
ADD . /EasyFXML
RUN chmod +x ./EasyFXML/docker-util/*

# Move into repo and pre-download required maven dependencies to limit dependency resolution output
# in compile-test step
WORKDIR /EasyFXML
RUN mvn dependency:go-offline -T4C

# Start tests
ENTRYPOINT ["./docker-util/run_tests.sh"]
