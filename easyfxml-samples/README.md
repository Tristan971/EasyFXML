# EasyFXML - Usage samples

## Why samples?
An easy answer would be to say that it is for end-user to learn about how to use this project.
And it would not be a wrong nor misguided one.

But there is more to it. Documentation unfortunately easily gets way behind code as it
is a largely tedious and complex endeavour to ensure that it is udpated, if not simply
not plainly wrong as the underlying code evolves.
There is no such thing as a documentation compiler.

In that sense, real concrete usage of a library often proves way more useful than large
tutorials, even more so than stern Javadoc.

Another huge advantage is that if downstream-using projects are compiled at the same
time as the library itself, there is a non-trivial amount of implied regression testing
gained in that. Especially if these projects are well-tested.

This is why this project, although it is not really part of the codebase, will overtime
probably end up being one of the most crucial ones.

## Current set of samples available

Not all samples are general-use ones, and more often than not they will aim to demonstrate
usage of some specific modules or feature-sets.

A last point to note is that you should feel free to experiment by slightly modifying these and
see for yourself how slight usage differences translate into actual concrete code.

- [EasyFXML (core)](../easyfxml)
  - [Simple Hello World greeter](easyfxml-sample-hello-world): Very basic usage of EasyFXML
  in the context of a single-view single-UI-component context. A good first look with as little
  overhead complexity than necessary to showcase what a minimal project would look like.
  
- [EasyFXML - FXKit](../easyfxml-fxkit)
  - [User identity form](easyfxml-sample-form-user): Basic form description, usage and testing for
  an imaginary user identity submission view

Hopefully this list grows a lot in the future. Feel free to ask for more use-case samples if you
feel it would help with understanding some of the more complex modules.
