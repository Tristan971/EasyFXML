FROM fedora:latest

RUN dnf update -y

RUN dnf install -y \
    maven \
    bash \
    xorg-x11-server-Xvfb \
    dbus-x11 \
    gtk3 \
    gtk2 \
    firefox

# Set up OpenJDK 11
RUN dnf install -y java-11-openjdk java-11-openjdk-devel
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk

# Enable colored output
ENV term xterm

# Copy current version of git repo
ADD . /EasyFXML
RUN chmod +x ./EasyFXML/docker-util/*

# Copy local maven repo
# Move into repo
# Pre-download required maven dependencies to limit dependency resolution output in compile-test step
WORKDIR /EasyFXML

# Start tests
ENTRYPOINT ["./docker-util/run_tests.sh"]
