# EasyFXML
A tiny framework to combine the convenience of _Spring Boot_ and _JavaFX_ together

[![Maven Central](https://img.shields.io/badge/maven--central-3.1.0-blue.svg)](https://search.maven.org/artifact/moe.tristan/easyfxml/3.1.0/jar)

[![Build Status](https://api.travis-ci.org/Tristan971/EasyFXML.svg?branch=master)](https://travis-ci.org/Tristan971/EasyFXML)
[![Maintainability](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/maintainability)](https://codeclimate.com/github/Tristan971/EasyFXML/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/test_coverage)](https://codeclimate.com/github/Tristan971/EasyFXML/test_coverage)
[![Known Vulnerabilities](https://snyk.io/test/github/tristan971/easyfxml/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)

## Features

- Declarative and type-safe definition and usage of visual components
- Fully compatible with Spring Boot 2, Java 11+ and the module path
- Easier asynchronous management of components' lifecycle
- Built with first-class support for FXML files
- No specific configuration needed

## Basics

The idea of EasyFXML is to leverage the current MVC model for front-end components and apply it to _JavaFX_ to avoid proper 
separation of concerns and lifecycle management.

There are two core parts defining a visual element (an `FxmlNode`, hereafter):
- Its view, an FXML file described by an `FxmlFile`, that is, a `String` supplier that is in charge of providing the path to the view file as a classpath resource.
  - The reason behind not simply using a `String`, `File` or `Path` is to allow for dynamic management of view files (OSGi, dynamic choice FXML file to load...)
- Its controller, a Spring Bean implementing `FxmlController`

## Getting started
###### This section is mostly a simplified version of the [Hello World](./easyfxml-samples/easyfxml-sample-hello-world) if you want to check it out for yourself

Let's see how building a very minimal greeter window, like follows, would work:

![Hello World Sample Screenshot](doc/images/sample-hello-world.png)

For this you will need:
- A component to load (the aforementionned Hello World one) along with its controller
- An entrypoint for the UI
- A main class

##### Component (i.e. the `FxmlNode`):
```java
@Component // Note: does not have to necessarily be a Spring Bean, an enum can be used if they do not have internal state (and they should almost never do)
public class HelloComponent implements FxmlNode {
    
    @Override 
    public FxmlFile getFile() {
        return () -> "my/package/view/hello/HelloView.fxml"; // The component lies in the `my.package.view.hello` package
    }                                                        // and its FXML view file is named `HelloView.fxml`

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return HelloController.class; // Its controller class is `HelloController`
    }

}
```

##### Controller (`FxmlController`):
```java
@Component // Note: if you can have multiple windows that are the same view (notifications, tooltips, etc), make sure that this bean is not a singleton
public class HelloController implements FxmlController {

    @FXML 
    private TextField userNameTextField;
    
    @FXML 
    private Button helloButton;
    
    @FXML 
    private HBox greetingBox;
    
    @FXML 
    private Label greetingName;

    @Override
    public void initialize() { // Called automatically by JavaFX once the nodes are loaded and bound to this controller's fields
        greetingBox.setVisible(false);
        setOnClick(helloButton, this::greet);
    }

    private void greet() {
        greetingName.setText(userNameTextField.getText());
        greetingBox.setVisible(true);
    }

}
```

##### Entrypoint of the UI side of the application (`FxUiManager`)
```java
@Component
public class HelloWorldUiManager extends FxUiManager {

    private final HelloComponent helloComponent;

    @Autowired
    public HelloWorldUiManager(HelloComponent helloComponent) {
        this.helloComponent = helloComponent;
    }

    @Override
    protected String title() {
        return "Hello, World!";
    }

    @Override
    protected FxmlNode mainComponent() { // defines what component must be loaded first into the main stage
        return helloComponent;
    }

}
```

##### Main class (`FxApplication` extends JavaFX's `Application` and wires up Spring properly)
```java
@SpringBootApplication // the EasyFXML Configuration class is automatically imported by Spring Boot if on the classpath
public class HelloWorld extends FxApplication {
    public static void main(String[] args) {
        launch(args);
    }
}
```

And that's about it. Feel free to look into [Hello World](./easyfxml-samples/easyfxml-sample-hello-world) if you want to know more!

## Use in your project
It is very easy to use EasyFXML via Maven/Gradle. The current version can be imported into your project with:

```xml
<dependencies>
    <dependency>
        <groupId>moe.tristan</groupId>
        <artifactId>easyfxml</artifactId>
        <version>3.1.0</version>
    </dependency>
</dependencies>
```

Testing in the asynchronous world of JavaFX can be especially complicated, especially when running in a CI environment.
Fortunately, libraries like [TestFX](https://github.com/TestFX/TestFX) will help a lot.
You also can use [EasyFXML-JUnit](./easyfxml-junit) (_experimental_), which is based on TestFX, 
for pre-made test infrastructure aimed at properly and predictably executing tests of EasyFXML-based JavaFX applications.

```xml
<dependencies>
    <dependency>
        <groupId>moe.tristan</groupId>
        <artifactId>easyfxml-junit</artifactId>
        <version>3.1.0</version>
    </dependency>
</dependencies>
```
