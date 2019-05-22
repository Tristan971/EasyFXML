# EasyFXML
A tiny opinionated framework for integrating _JavaFX_ with _Spring Boot_ seamlessly

[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml)

## Required dependencies
EasyFXML is based on, and requires the following runtime setup:
- **[Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)** *with first-class support for the module-path if you use it*, 
- **[OpenJFX 11](https://openjfx.io/)** via Maven,
- **[Spring Boot 2](https://spring.io/projects/spring-boot)** via Maven

It **might** still work on different environment set-ups, but there is no official support for it.

Especially considering that JavaFX applications are expected to be bundled with their own JDK and dependencies, this
should not be an issue.

## Features

- Full support of both classpath and module path
- Declarative and type-safe definition and usage of visual components
- Easier asynchronous management of components' lifecycle
- Built with first-class support for FXML files
- No specific configuration needed

## Philosophy

The idea of EasyFXML is to adopt the industry-standard MVC model for UI components and apply it to _JavaFX_.
This allows easier  separation of concerns and lifecycle management of these components inside applications.

There are thus three core elements that go into a UI component (an **[`FxmlNode`](src/main/java/moe/tristan/easyfxml/api/FxmlNode.java)** hereafter):
- For the **M**odel, it is simple as your standard classes are just provided and usable via Java itself, and services and other more complex things 
can be injected via _Spring_'s autowiring system.
- The **V**iew, a standard `.fxml` file in the form of an **[`FxmlFile`](src/main/java/moe/tristan/easyfxml/api/FxmlFile.java)**
- And **C**ontroller, that is, **a Spring Bean** implementing **[`FxmlController`](src/main/java/moe/tristan/easyfxml/api/FxmlController.java)**

## Getting started
It is very easy to use EasyFXML via Maven/Gradle. The current version can be imported into your project with:

```xml
<dependency>
    <groupId>moe.tristan</groupId>
    <artifactId>easyfxml</artifactId>
    <version>3.2.0</version>
</dependency>
```

Then when it comes to documentation and using it, this section is mostly an (extremely) simple example, available in the
[samples module](../easyfxml-samples), under [Hello World](../easyfxml-samples/easyfxml-sample-hello-world) if you want 
to check it out for yourself. Other more complex examples are available there.

So, let's see how building a very minimal greeter window, like follows, would work:

![Hello World Sample Screenshot](../doc/images/sample-hello-world.png)

For this you will need:
- The component's `FxmlNode`
- An entrypoint for the UI
- A main class

##### Component ([`FxmlNode`](../easyfxml/src/main/java/moe/tristan/easyfxml/api/FxmlNode.java))
```java
@Component
public class HelloComponent implements FxmlNode {
    
    @Override 
    public FxmlFile getFile() {
        return () -> "my/package/view/hello/HelloView.fxml"; 
        // component lies in `my.package.view.hello` package
    }   // and its FXML view file is `HelloView.fxml`

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return HelloController.class; 
        // Its controller class is `HelloController`
    }

}
```

##### Controller ([`FxmlController`](../easyfxml/src/main/java/moe/tristan/easyfxml/api/FxmlController.java))
```java
@Component
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
    public void initialize() { // called once loading is fully done
        greetingBox.setVisible(false);
        greetingName.textProperty().bind(userNameTextField.textProperty());

        setOnClick(helloButton, () -> greetingBox.setVisible(true));
    }

}
```
Note that if you can have multiple instances of a given component (a notification panel, or a individual cell in a list/table for example), 
you need to make sure that the controller class is not a singleton with @Scope(scopeName = ConfigurableBeanFactory.PROTOTYPE)

##### Entrypoint of the UI ([`FxUiManager`](../easyfxml/src/main/java/moe/tristan/easyfxml/FxUiManager.java))
###### (called by EasyFXML once JavaFX and Spring are both ready to use)
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

##### Main class ([`FxApplication`](../easyfxml/src/main/java/moe/tristan/easyfxml/FxApplication.java))
```java
@SpringBootApplication // EasyFXML wires itself in the context via Spring Boot's autoconfiguration
public class HelloWorld extends FxApplication { // FxApplication is essential here to set-up JavaFX
    public static void main(String[] args) {    // and Spring cohabitation
        launch(args);
    }
}
```

And that's about all we need here. 

Feel free to look into [the samples](../easyfxml-samples) if you want to see more advanced examples!
