# EasyFXML
An opinionated framework for building modern _JavaFX_ apps.

Last build's statuses:

Build : 
[![Build Status](https://jenkins.tristan.moe/job/EasyFXML/job/master/badge/icon)](https://jenkins.tristan.moe/job/EasyFXML/job/master)

Quality : 
[![Coverage](https://sonar.tristan.moe/api/project_badges/measure?project=moe.tristan%3Aeasyfxml&metric=coverage)](https://sonar.tristan.moe/project/activity?graph=coverage&id=moe.tristan%3Aeasyfxml)
[![Sonar bugs](https://sonar.tristan.moe/api/project_badges/measure?project=moe.tristan%3Aeasyfxml&metric=bugs)](https://sonar.tristan.moe/project/issues?id=moe.tristan%3Aeasyfxml&resolved=false&types=BUG)
[![Known Vulnerabilities](https://snyk.io/test/github/tristan971/easyfxml/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/tristan971/easyfxml?targetFile=pom.xml)

Trivia : 
[![LoC](https://sonar.tristan.moe/api/project_badges/measure?project=moe.tristan%3Aeasyfxml&metric=ncloc)](https://sonar.tristan.moe/project/activity?graph=coverage&id=moe.tristan%3Aeasyfxml)
[![Technical debt](https://sonar.tristan.moe/api/project_badges/measure?project=moe.tristan%3Aeasyfxml&metric=sqale_index)](https://sonar.tristan.moe/project/issues?facetMode=effort&id=moe.tristan%3Aeasyfxml&resolved=false&types=CODE_SMELL)

[![Quality gate status](https://sonar.tristan.moe/api/project_badges/quality_gate?project=moe.tristan%3Aeasyfxml)](https://sonar.tristan.moe/dashboard?id=moe.tristan%3Aeasyfxml)

Maven dependency : [![Maven Central](https://maven-badges.herokuapp.com/maven-central/moe.tristan/easyfxml/badge.svg)](http://search.maven.org/#artifactdetails%7Cmoe.tristan%7Ceasyfxml%7C1.1.5%7Cjar)
```xml
<dependency>
    <groupId>moe.tristan</groupId>
    <artifactId>easyfxml</artifactId>
    <version>1.1.5</version>
</dependency>
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
- An `FxmlController` - that is, a `@Component` annotated (for Spring) class that
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
                    .recover(err -> new ExceptionHandler(err).asPane()); //load an error pane instead of just crashing
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

The good news is, it's a beatiful day to start doing so :)

But then again I can understand so Spring is absolutely not required from your
code's standpoint. That said, this library has a dependency to Spring Boot so
you should keep that in mind if you create custom JVM images or tomcat containers
so that it doesn't crash with these sweet sweet `ClassNotFoundException`s :)

Acting like a Spring Context is `NoSpringSupport` class's `#getInstance(Class)`
method. It should successfully supply any component of the library that is meant
to be exposed.

#### F.A.Q.

Well, none so far, but you can check out my [twitter client project (Lyrebird)](https://github.com/Tristan971/Lyrebird)
to see what usage looks like.
It is not nearly advanced nor up to date with this library for now though. It's
mostly for getting a general idea I'd say.

#### Misc

Licence : Apache (see LICENSE, tl;dr : don't be evil :)

Credits : Tristan Deloche (I hope this list grows by a lot in some time)
