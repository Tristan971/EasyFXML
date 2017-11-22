# EasyFXML
All the _JavaFX_ base building blocks in one library

[![Build Status](https://travis-ci.org/Tristan971/EasyFXML.svg?branch=master)](https://travis-ci.org/Tristan971/EasyFXML) [![codecov](https://codecov.io/gh/Tristan971/EasyFXML/branch/master/graph/badge.svg)](https://codecov.io/gh/Tristan971/EasyFXML)

Maven dependency : (soon)
```xml
<dependency>
    <groupId>moe.tristan</groupId>
    <artifactId>easyfxml</artifactId>
    <version>1.0.1</version>
</dependency>
```

#### Why ?
If you ever programmed an application using _JavaFX_ you most likely realized 
that it felt, in a lot of ways,  unfinished.

- **FXMLLoader** is terribly unenjoyable to use
- It is hard to make sense of how to do the appropriate **context switching** 
requiredby a lot of GUIs
- Many system integration features like having a status bar icon, or even simply
opening the browser when your user clicks a link are buried into **deep
incompatibility with the model that AWT used to provide decades ago**.

None of this is due to _JavaFX_ being inherently incapable. 
It is just not as polished/mature as we are used to have things being in the 
java ecosystem.

This is where EasyFXML comes. It aims at taking the role of both a helping library
and a framework for how to do things right before regretting having gone another
way a few months back.

Hopefully this ends up becoming a community effort as having one single person
doing that can only lead to tunnel vision. I tried to avoid that, but feel 
free to point out anything that you dislike or would have done differently..

#### What does it do for me ?

The aim is to mimick, when it makes sense, modern web development frameworks.
That is because they already worked out how to organise UI components well.

Thus, here's what a unit of your UI's declaration might look like with EasyFXML:
(note that we use an enum here but you are free to make many classes if you prefer)
```java
public enum Views implements FxmlNode {
    ROOT("RootView.fxml", RootController.class, Stylesheets.BASE_STYLE),
    LOGIN("Login.fxml", LoginController.class),
    TIMELINE("Timeline.fxml", TimelineController.class),
    SETTINGS("Tweet.fxml", SettingsController.class);

    private static final String VIEWS_ROOT = "components/";
    
    /** constructor, fields... **/

    @Override
    public FxmlFile getFxmlFile() {
        return () -> VIEWS_ROOT + fxmlFile;
    }

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return controllerClass;
    }

    @Override
    public FxmlStylesheet getStylesheet() {
        return stylesheet;
    }
}
```

And then here's how you would load your login component, all
at once creating the controller class instance, storing it for
later retrieval, applying the stylesheet if needed and returning
you the ready-to-use Node :

```java
public class MyGuiOrchestratorClass {
    @Autowired
    private EasyFxml easyFxml;
    
    /* ... */
    
    private Try<Node> loadLoginNode() {
        easyFxml.load(Views.LOGIN);
    }
}
```

Now you probably wonder what is `Try<Node>`. It is a monadic
structure from the wonderful `vavr` library which allows us to
safely handle loading exceptions without writing 290000 try-catchs
ourselves and to take advantage of that Java 8 functional goodness.

A way to take advantage of it is as follows : 
```java
public class GuiManager {
    /***/
    
    private Scene getRootScene(EasyFxml easyFxml) {
            final Try<Pane> rootPane = easyFxml
                    .loadNode(Views.ROOT)
                    .recover(error -> new ErrorPane(error)); // equivalent to ErrorPane::new
            return new Scene(rootPane.get());
    }
}
```

And finally here is how you would completely load a windowed component and get
its owning stage back, showing an error window instead if there was an issue during
loading :
```java
public class SomeClass {
    @Autowired
    private EasyFxml easyFxml;
    
    public Stage openLoginWindow() {
        Try<Stage> loginWindow = easyFxml.loadNode(Views.LOGIN)
                .map(pane -> StageUtils.stageOf("Login", pane))
                .recover(ErrorPane::new)
                .andThen(StageUtils::scheduleDisplaying);
        
        return loginWindow.get();
    }
}
```

#### What if I don't use Spring ?
Spring isn't mandatory as a way to use the library although it makes it simpler.
You can use `NoSpringSupport#getInstance(Class)` with no loss of functionnality.

That said, the library uses Spring so it has to be available on the classpath if
you do tomcat-embedding, custom VM linking in Java 9 or other funky things.

