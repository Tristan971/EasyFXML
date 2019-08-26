# EasyFXML Docker image

[![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/tristandeloche/easyfxml-docker.svg?style=for-the-badge)](https://hub.docker.com/r/tristandeloche/easyfxml-docker)

This is a pre-made [Docker image](Dockerfile) which includes all the needed configuration
needed for properly executing JavaFX integration tests in CI environments.

### Properties
- **Linux base**: Same as the [parent image](https://hub.docker.com/r/adoptopenjdk/maven-openjdk11), currently Ubuntu
- **Parent image:** [AdoptOpenJDK/maven-openjdk11:**latest**](https://hub.docker.com/r/adoptopenjdk/maven-openjdk11)
- **Java version:** 11
- **Provider:** AdoptOpenJDK

### Usage

The image executes maven with the given arguments on the folder `/root/build`, so an example would be:
`docker run -v /my/project:/root/build -it tristandeloche/easyfxml-docker:1.2.3 clean install`

### Specifics

Running _JavaFX_ mostly requires the X Virtual FrameBuffer (`xvfb`), but it is far from enough for most features and workflows.

This images sets up basically all of what you might want.

It is invoked by [maven_clean_install.sh](maven) (which is on the path inside the image).

That script wraps maven invocations with `xvfb-run` to ensure set-up and teardown of the
virtual desktop environment when building and most importantly testing.

Another few notable aspects are the installation of git (which your maven build might)
rely on for various aspects including resource filtering and releasing.

Finally, `dbus-x11` and `gtk+{2,3}` are some of the needed Linux runtime dependencies for
various non-headless _JavaFX_ features.

### Miscellaneous

It is strictly a non-goal to have small images, which is why things like alpine base and minimal
image size footprint have voluntarily not been considered. Indeed it is not expected to be stored
and shared images, but rather only use to have an easier time running integration tests.

You must also be careful about the fact that _JavaFX_ does **not** support cross-compilation, so releasing
full applications automatically is **not** enabled by this image in any way.
