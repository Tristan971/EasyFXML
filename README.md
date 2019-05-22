# EasyFXML

[![Build status](https://img.shields.io/circleci/build/github/Tristan971/EasyFXML.svg?style=for-the-badge)](https://circleci.com/gh/Tristan971/EasyFXML)
[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml-parent.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml-parent)
[![Maintainability](https://img.shields.io/codeclimate/maintainability/Tristan971/EasyFXML.svg?style=for-the-badge)](https://codeclimate.com/github/Tristan971/EasyFXML/maintainability)
[![Test coverage](https://img.shields.io/codeclimate/coverage/Tristan971/EasyFXML.svg?style=for-the-badge)](https://codeclimate.com/github/Tristan971/EasyFXML/test_coverage)
[![Snyk Vulnerabilities](https://img.shields.io/snyk/vulnerabilities/github/Tristan971/EasyFXML.svg?style=for-the-badge)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)
[![License](https://img.shields.io/github/license/Tristan971/EasyFXML.svg?style=for-the-badge)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_shield)

## Motivation

Having a cool framework for building desktop applications is a great feat on itself, and the _JavaFX_ team and contributors
certainly came up with an amazing platform to build upon.

Unfortunately, due to shifting business priorities at Oracle, it was never pushed forward enough to realize its potential ;
indeed a lot of the tooling for making _JavaFX_ apps easy to develop and maintain across multiple platforms with consistency
is missing, and some parts of the standard library feel somewhat unpolished, most likely due to a lack of time to polish them.

This project's aim is to build and provide tools and extensions to the broader community so as to bridge these gaps left
in the standard distribution of _JavaFX_.

## Overview of modules and features

- **[EasyFXML](easyfxml)**: Core MVC framework for reusable and decoupled UI components
  - First-class support of Java 11 with module path (no more `--add-opens` magic)
  - Uses seamless Spring Boot 2 support with(in) _JavaFX_ components lifecycle

- **[EasyFXML - JUnit](easyfxml-junit)**: Infrastructure for fully asynchronous (yet simple) _JavaFX_ integration testing
  - Pre-made [TestFX](https://github.com/TestFX/TestFX) configuration and usage
  - Relies on [Awaitility](https://github.com/awaitility/awaitility) to schedule and synchronize at testing time

- **[EasyFXML - Docker](easyfxml-docker)**: Proper support for integration testing in continuous integration services
  - Public [docker image](https://hub.docker.com/r/tristandeloche/easyfxml-docker) preconfigured with all the runtime requirements for _JavaFX_
  - Strives to include actual support for most (if not all) non-headless features as well

- **[EasyFXML - FXKit](easyfxml-fxkit)**: Loosely constraining "design patterns" for common UX component types and behaviors

- **[EasyFXML - Native](easyfxml-native)**: Supporting native underlying operating system when the experience is better
  - Relies on [Oshi](https://github.com/oshi/oshi) for platform detection (no more brittle `switch`ing on the platform name)
  - *(Planned)* Proper successor to [Zenjava's JavaFX plugin](https://github.com/javafx-maven-plugin/javafx-maven-plugin) for native bundling

- **[EasyFXML - Samples](easyfxml-samples)**: Self-contained project samples to see how real usage looks like

## Getting started
A first step is to get familiar with _JavaFX_ and _Spring Boot_ as they are central building blocks of the project.

Once that is done, a look at both the *[core EasyFXML module](easyfxml)*, and then at 
*[some of the latest usage samples](easyfxml-samples)* should provide a good overview of
the design philosophy and expected usage at time of writing of the modules of the project.

For larger-scale usage than small contained samples, you can have a look at:
- [Lyrebird](https://github.com/Tristan971/Lyrebird) a _JavaFX_ Twitter client whose development difficulties motivated a lot of
features you will find here

## Licensing and contributions
All contributions are welcome, both as questions, constructive criticism, feature requests and of course pull requests.

There is not yet a clear organization for making first-contributions easy, but a good rule of thumb is to ensure opening an issue
before making a pull-request so that the technical implementation's requirements can be discussed before work is done. Then, crack on! :-)

[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/0)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/0)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/1)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/1)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/2)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/2)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/3)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/3)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/4)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/4)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/5)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/5)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/6)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/6)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/7)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/7)

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_large)
