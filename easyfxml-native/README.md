# EasyFXML - Native extensions

[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml-native.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml-native)

## Overview

While _JavaFX_ strives to be a fully cross-platform development framework, it's often
a good idea, and enjoyable for the user, to provide some features which are not cross-platform.

That includes, but not limited to, things like:
- Self-updates
- Native operating system notifications
- Native application bundling

This part of the project is still in its early development stages, and for now it
only offers reliable underlying platform detection via the excellent [Oshi](https://github.com/oshi/oshi)
library.

The aforementionned feature are critical priority when it comes to the future feature set
to be offered by this module.

Some various libraries exist to support some of these features but are either outdated
or somewhat difficult to implement reliably.

These include, but again not limited to:
- [UpdateFX](https://github.com/bitgamma/updatefx) for selfupdates of JavaFX applications
- [Zenjava's JavaFX maven plugin](https://github.com/javafx-maven-plugin/javafx-maven-plugin)

Unfortunately they will not be able to be simply integrated and the goal is mostly
to get inspiration from them and achieve feature parity in a well-integrated way.
