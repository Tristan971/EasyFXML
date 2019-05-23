# EasyFXML - FXKit
Pre-configured boilerplate for frictionless development of common UX components

[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml-fxkit.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml-fxkit)

## Overview

A lot of applications share some common UX behaviors and components that need not be totally different from one another. 
The goal of FXKit is to provide ready-made boilerplate and architectural guidelines to quickly and efficiently design such
commonly used components.

## Current component types and usage examples

### Forms
**Description:** A form is, in this context, a component that requires the following
- it encompasses a set of individual user inputs
- these are possibly validated, whether together or individually
- and then possibly submitted to another software component after aggregation (for further processing, saving etc.)

**Sample usage:** [The user identity form sample](../easyfxml-samples/easyfxml-sample-form-user) is the application
of this pattern to an imaginary user subscription flow.
