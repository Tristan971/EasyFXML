# EasyFXML
##### A collection of tools and libraries for easier development on the _JavaFX_ platform


[![Build status](https://img.shields.io/circleci/build/github/Tristan971/EasyFXML.svg?style=for-the-badge)](https://circleci.com/gh/Tristan971/EasyFXML)
[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml-parent.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml-parent)
[![License](https://img.shields.io/github/license/Tristan971/EasyFXML.svg?style=for-the-badge)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_shield)

[![Maintainability](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/maintainability)](https://codeclimate.com/github/Tristan971/EasyFXML/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/test_coverage)](https://codeclimate.com/github/Tristan971/EasyFXML/test_coverage)
[![Known Vulnerabilities](https://snyk.io/test/github/tristan971/easyfxml/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)

## Motivation

Having a cool framework for building desktop applications is a great feat on itself, and the _JavaFX_ team and contributors
certainly came up with an amazing platform to build upon.

Unfortunately, due to shifting business priorities at Oracle, it was never pushed forward enough to realize its potential ;
indeed a lot of the tooling for making _JavaFX_ apps easy to develop and maintain across multiple platforms with consistency
is missing, and some parts of the standard library feel somewhat unpolished, most likely due to a lack of time to polish them.

This project's aim is to build and provide tools and extensions to the broader community so as to bridge these gaps left
in the standard distribution of _JavaFX_.

## Overview of the project's modules

- **[EasyFXML](easyfxml)**: Core MVC-style framework for reusable and decoupled UI components
  - Includes first-class support of Java 11 with module path (no more `--add-opens` magic)
  - Based on a seamless Spring Boot 2 integration with(in) the _JavaFX_ model

- **[EasyFXML - JUnit](easyfxml-junit)**: Testing infrastructure for fully asynchronous (yet simple) _JavaFX_ component testing
  - Uses pre-made [TestFX](https://github.com/TestFX/TestFX) configurations and patterns to achieve robust yet realistic UI testing
  - Relies on [Awaitility](https://github.com/awaitility/awaitility) to reconcile test synchronization and the naturally asynchronous nature of UI testing

- **[EasyFXML - Docker](easyfxml-docker)**: Provides proper support for continuous integration services
  - Public [docker image](https://hub.docker.com/r/tristandeloche/easyfxml-docker) preconfigured with all the runtime requirements for _JavaFX_
  - Includes large support for non-headless features that usually cannot be reliably tested without loading a full desktop environment in your test instance

- **[EasyFXML - FXKit](easyfxml-fxkit)**: Basic boilerplate and architecture for quiter prototyping of common kinds of UX components

- **[EasyFXML - Native](easyfxml-native)**: Helps when non-cross-platform features are needed or suit the application better when done by-underlying-OS
  - Using [Oshi](https://github.com/oshi/oshi) for platform detection (no more brittle `switch`ing on the platform name system property)
  - *(Planned)* Proper successor to [Zenjava's JavaFX plugin](https://github.com/javafx-maven-plugin/javafx-maven-plugin) to enable native bundling 
  and simpler distribution to end-users

- **[EasyFXML - Samples](easyfxml-samples)**: Self-contained project samples
  - Allows to showcase and learn about individual features in isolation and when coupled with others
  - Only a few samples for now but will grow overtime, maybe with your help even :-)

## Getting started
A first necessary step is to get familiar with both _JavaFX_ and _Spring Boot_.

As they are central building blocks of the project and not necessarily meant to work together, there is some usage of both
in the process of making them work together that might not be a great first experience with either.

Once that is done, a look at both the *[core EasyFXML module](easyfxml)*, and then at  *[some of the latest usage samples](easyfxml-samples)* 
should provide a good overview of the design philosophy and expected usage of the project.

For real usag in a fully-fledged application, here's a list of projects using EasyFXML at the moment:
- [Lyrebird](https://github.com/Tristan971/Lyrebird) a _JavaFX_ Twitter client
  - Most of the gaps in _JavaFX_ that I discovered developping it motivated this whole project
  - It is not quite a completed project but there is a substantial-enough amount of usage in it to get a good idea for yourself

## Licensing and contributions
All contributions are welcome, both as questions, constructive criticism, feature requests and of course pull requests.

There is not yet a clear organization for making first-contributions easy, but a good rule of thumb is to ensure opening an issue
before making a pull-request so that the technical implementation's requirements can be discussed before work is done. Then, crack on! :-)

[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/0)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/0)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/1)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/1)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/2)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/2)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/3)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/3)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/4)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/4)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/5)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/5)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/6)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/6)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/7)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/7)

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_large)
