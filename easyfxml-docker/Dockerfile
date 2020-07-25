FROM fedora:32

MAINTAINER Tristan Deloche <tristandeloche@gmail.com>

RUN dnf install -y \
    java-latest-openjdk-devel \
    dbus-x11 \
    git \
    maven \
    xorg-x11-server-Xvfb

ENV term xterm-256color

ENV JAVA_HOME /usr/lib/jvm/java-14-openjdk

# verify that maven is indeed using the expect JDK
RUN mvn -v | grep -B2 -A2 "$JAVA_HOME"

ADD ./easyfxml-maven /bin/easyfxml-maven

ENTRYPOINT ["/bin/easyfxml-maven"]
