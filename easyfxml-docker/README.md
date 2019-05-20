# EasyFXML Docker image

[![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/tristandeloche/easyfxml-docker.svg)](https://hub.docker.com/r/tristandeloche/easyfxml-docker)

- **Java version:** 11

- **Provider:** AdoptOpenJDK

- **Parent image:** [AdoptOpenJDK/maven-openjdk11:**latest**](https://hub.docker.com/r/adoptopenjdk/maven-openjdk11)

Pre-made [Docker image](Dockerfile) set-up for running JavaFX integration tests in CI
environments.

Leverages the X Virtual FrameBuffer, invoked by
[maven_clean_install.sh](maven_clean_install.sh) that wraps invocations
with `xvfb-run` to ensure set-up and teardown of the temporary headless
UI environment.
