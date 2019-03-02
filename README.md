# EasyFXML
A tiny framework to combine the convenience of _Spring Boot_ and _JavaFX_ together

[![Build Status](https://api.travis-ci.org/Tristan971/EasyFXML.svg?branch=master)](https://travis-ci.org/Tristan971/EasyFXML)
[![Maintainability](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/maintainability)](https://codeclimate.com/github/Tristan971/EasyFXML/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/89c1e95e4d5d41b35d9f/test_coverage)](https://codeclimate.com/github/Tristan971/EasyFXML/test_coverage)
[![Known Vulnerabilities](https://snyk.io/test/github/tristan971/easyfxml/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)

## Features

- Never have directly deal with `FxmlLoader` again. Yes.
- Declarative, MVC-like management of visual components
- Fully compatible with Spring Boot 2, Java 11+ and the module path
- Type-safe and asynchronous management of visual components from before displaying them to after disposal
- Built with first-class support for FXML-based components
- No specific configuration needed

## Getting started

The idea of EasyFXML was to leverage the current MVC model for front-end components and apply it to _JavaFX_ to avoid proper 
separation of concerns and lifecycle management.

Here is how you would declare a component, called `FxmlNode`:
```java
public class MyComponent implements FxmlNode {
    
}
```

## Usage
With Maven or Gradle, you can either use only EasyFXML itself or also the JUnit DSL (experimental at the moment) as well for easier testing:

[![Maven Central](https://img.shields.io/badge/maven--central-3.1.0-blue.svg)](https://search.maven.org/artifact/moe.tristan/easyfxml/3.1.0/jar)
```xml
<dependencies>
    <dependency>
        <groupId>moe.tristan</groupId>
        <artifactId>easyfxml</artifactId>
        <version>3.1.0</version>
    </dependency>
    <dependency>
        <groupId>moe.tristan</groupId>
        <artifactId>easyfxml-junit</artifactId>
        <version>3.1.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Why ?
If you ever programmed an application using _JavaFX_ you most likely realized 
that it felt, in a lot of ways,  unfinished.

- **FXMLLoader** is terribly unenjoyable to use
- It is hard to make sense of how to do the appropriate **context switching** 
required by a lot of GUIs
- Many system integration features like having a status bar icon, or even simply
opening the browser when your user clicks a link are buried into **deep
incompatibility with the model that AWT used to provide decades ago**.

None of this is due to _JavaFX_ being inherently incapable. 
It is just not as polished/mature as we are used to have things being in the 
java ecosystem.

This is where EasyFXML comes. It aims at taking the role of both a helping library
and a framework for how to do things right before regretting having gone another
way a few months back.

## Quick documentation

The aim is to mimick, when it makes sense, modern web development frameworks.
That is because they already worked out how to organise UI components well.

There is though a quick caveat to it as it is very impractical to style an
individual `Node` in JavaFX so we only support styling at the `Stage` (Window)
level for now.

#### Declaring a UI component (i.e. an `FxmlNode`) :

An `FxmlNode` is the declarative interface for:
- An `FxmlFile` - that is, a supplier for the access scheme of the `.fxml` file
- An `FxmlController` - that is, a Spring bean that
contains the controller logic/binding

Thus, here's what using an `FxmlNode`-implementing `enum` to declare all your
components looks like.

```java
public enum Components implements FxmlNode {
    CONTROL_BAR("controlbar/ControlBar.fxml", ControlBarController.class),
    CURRENT_ACCOUNT("currentaccount/CurrentAccount.fxml", CurrentAccountController.class)

    /*...*/

    USER_TIMELINE("usertimeline/UserTimeline.fxml", UserTimelineController.class),
    NOTIFICATIONS_PANE("notifications/NotificationPane.fxml", NotificationsController.class);

    private static final String COMPONENTS_BASE_PATH = "moe/lyrebird/view/components/";

    private final String filePath;
    private final Class<? extends FxmlController> controllerClass;

    Components(final String filePath, final Class<? extends FxmlController> controllerClass) {
        this.filePath = filePath;
        this.controllerClass = controllerClass;
    }

    @Override
    public FxmlFile getFile() {
        return () -> COMPONENTS_BASE_PATH + filePath;
    }

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return controllerClass;
    }
}

```

#### Loading a UI component with `EasyFxml`

What is meant by "loading" here is:
- Creating an instance of the controller class
- Loading the `.fxml` file's hierarchy into the matching `Node` subtype
- Binding both, saving the controller in the `ControllerManager` for
later easy retrieval
- Returning the element ready to be added in the _Scene_.

```java
public class SomeClass {
    
    private EasyFxml easyFxml;
    
    @Autowired // constructor injection YES
    public SomeClass(EasyFxml easyFxml) {
        this.easyFxml = easyFxml;
    }
    
    /* ... */
    
    private Try<Node> loadLoginNode() {
        return easyFxml.load(Views.LOGIN).getNode();
    }
}
```

Now, unless you used it before, you probably wonder what is `Try<Node>`. 
It is a monadic structure from the wonderful `vavr` library.

These are big words to say that it is actually super useful. You can easily
safely handle loading exceptions without writing 290000 try-catchs
yourselves, and take advantage of them shiny lambdas.

A way to take advantage of it is as follows : 

```java
public class SomeClass {
    
    /***/
    
    private Scene getRootScene() {
            final Try<Pane> rootPane = this.easyFxml
                    .loadNode(Views.ROOT)
                    .getNode()
                    .recover(ExceptionHandler::fromThrowable); //load an error pane instead of just crashing
                    //.onError(Consumer<Throwable>)
                    //.onSuccess(Consumer<Pane>)
                    //.isSuccess() etc etc
            return new Scene(rootPane.get()); // or use #get() if you're already sure it loads
    }
}
```

And finally here is how you would use some of the utility methods provided as well to display and hide the just-made window:
```java
// supposing we still are in that same class for simplicity
public class SomeClass {
    
    /***/
    
    public CompletableFuture<Stage> displayLoginWindow() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(this.getRootScene());
        return Stages.scheduleDisplaying(loginStage);
    }
    
    public CompletableFuture<Stage> hideLoginWindow() {
        Stage loginStage = StageManager.getSingle(Views.LOGIN);
        Stages.scheduleHiding(loginStage);
    }
    
    // Here the async is necessary to ensure we don't hide before shown
    // or simply wait too long
    public void showThenHideLoginWindow() {
        displayLoginWindow().thenRun(this::hideLoginWindow);
    }
}
```

Remember _JavaFX_ is asynchronous. You can block on waiting for the stage
to show up with `CompletableFuture#join()` if you want to avoid that.

#### Managing the controllers instances

There's no really satisfactory way to go about it and I don't like how
I did it here so far but it's the current version.
Should you have better ideas, they would be most welcome.

Anyway, you can look up `AbstractInstanceManager` and its two
implementations `ControllerManager` and `StageManager`.

It works. It is a bit clunky but it works decently well.

Basically if your components are unique during their lifetime
(i.e. only one "Login" window), then you can simply do as 
this documentation shows, and get the instance from anywhere
with `ControllerManager#Views.LOGIN` for the controller class
and `StageManager#Views.LOGIN` for the stage. All of it is
auto-registered during calls to `EasyFxml#load(...)`.

But then if you can have multiple login component alive at the
same time, the differentiation is done with athe override 
`EasyFxml#load(..., Object selector)`. That is, anything of your
choice that you will have to supply to 
`...Manager#getMultiple(Views.LOGIN, myObjectToDistinguish)` later.

Yes it's very clunky and the memory impact of such things is hard
to evaluate...


#### What if I don't use Spring ?

Starting with EasyFXML 2.0.0, you need to use Spring to rely on it.

You can still use the latest 1.X.X version and `NoSpringSupport` utility class for non-spring projects.

#### F.A.Q.

Well, none so far, but you can check out [Lyrebird, an open-source cross-platform JavaFX-based Twitter client for the desktop](https://github.com/Tristan971/Lyrebird)
to see what usage looks like.
It might be a bit convoluted at first look so start with the `Components` and `Screens` classes to get a general feel of how it works.

#### Misc

Licence : Apache

Credits : Tristan Deloche (I hope this list grows by a lot in some time)
