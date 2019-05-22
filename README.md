# EasyFXML
This repository contains all the modules that define the EasyFXML project.

This project's aim is to enable _JavaFX_ developers' around the world to be more productive by exposing a large amount
of tooling that is absent otherwise from the _JavaFX_ ecosystem.

Some of the current highlights include
- First-class support of the module path, with Java + OpenJFX 11
- Much simpler usage of FXML as markup for UI components, instead of composing them in straight Java
- Developing your _JavaFX_ application with the latest _Spring Boot_-based architecture
- Writing properly asynchronous integration tests without pain
- And even running them on any docker-enabled public CI environment
- Better integratinon with the underlying operating-system to allow better usage of non cross-platform features

[![Build status](https://circleci.com/gh/Tristan971/EasyFXML.svg?style=svg)](https://circleci.com/gh/Tristan971/EasyFXML)
[![Maintainability](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/maintainability)](https://codeclimate.com/github/Tristan971/EasyFXML/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/test_coverage)](https://codeclimate.com/github/Tristan971/EasyFXML/test_coverage)
[![Known Vulnerabilities](https://snyk.io/test/github/tristan971/easyfxml/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)
[![Licensing Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_shield)

## General design philosophy
_JavaFX_, like any technology, does not exist in a vacuum. And no matter how good (or bad) it is or could be, it will need an
ecosystem to thrive.

Inspired by pioneering works that set out to build the first blocks of such an ecosystem, such as
[TornadoFX](https://github.com/edvin/tornadofx), [ControlsFX](https://github.com/controlsfx/controlsfx),
[AfterburnerFX](https://github.com/AdamBien/afterburner.fx) and many others, this project aims at bridging some of the
gaps in booth supporting tooling and the standard library.

The focus has originally been on the integration of _Spring Boot_, but since then has *enjoyed* a sizable amount of scope
creep as more and more of the gaps at time of writing made themselves visible.

## Components of the EasyFXML project
Since all of the components are expected to live a life (partly) of their own, they each will enjoy their own README and
documentation that you can access in each others' directory.

### Core
- *[EasyFXML](easyfxml)*: Tiny opinionated framework for integrating _JavaFX_ with _Spring Boot_ seamlessly
- *[EasyFXML - JUnit](easyfxml-junit)*: Infrastructure for fully asynchronous (yet simple) _JavaFX_ integration testing

### Extras
- *[EasyFXML - FXKit](easyfxml-fxkit)*: Design patterns for common UX components types
- *[EasyFXML - Native](easyfxml-native)*: Support for non cross-platform features relying on underlying operating system

### Infrastructure
- *[EasyFXML - Docker](easyfxml-docker)*: Docker image and tools for out-of-the-box support of continuous integration systems
- *[EasyFXML - Module Base](easyfxml-module-base)*: Maven meta-module ensuring dependency and plugin consistency across the whole project

### Miscellaneous
- *[EasyFXML - Samples](easyfxml-samples)*: Fully-contained project samples to showcase real-usage and enhance testing span

## Getting started
A first step is to get familiar with _JavaFX_ and _Spring Boot_ as they are central building blocks of the project.

Once that is done, a look at both the *[core EasyFXML module](easyfxml)*, and then at 
*[some of the latest usage samples](easyfxml-samples)* should provide a good overview of
the design philosophy and expected usage at time of writing of the modules of the project.

## Contributing
All contributions are welcome, both as questions, constructive criticism, feature requests and of course pull requests.

There is not yet a clear organization for making first-contributions easy, but a good rule of thumb is to ensure opening an issue
before making a pull-request so that the technical implementation's requirements can be discussed before work is done. Then, crack on! :-)

[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/0)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/0)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/1)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/1)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/2)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/2)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/3)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/3)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/4)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/4)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/5)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/5)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/6)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/6)[![](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/images/7)](https://sourcerer.io/fame/Tristan971/Tristan971/EasyFXML/links/7)


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FTristan971%2FEasyFXML?ref=badge_large)
