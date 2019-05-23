# EasyFXML - Sample - Hello World

This sample project demonstrates a minimal set-up for an [EasyFXML](../../easyfxml) application.

### Presentation

Let's see how building a very minimal greeter window, like follows, would work:

![Hello World Sample Screenshot](doc/sample-hello-world.png)

For this you will need:
- The component's `FxmlNode`
- An entrypoint for the UI
- A main class

##### Component ([`FxmlNode`](../../easyfxml/src/main/java/moe/tristan/easyfxml/api/FxmlNode.java))
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

##### Controller ([`FxmlController`](../../easyfxml/src/main/java/moe/tristan/easyfxml/api/FxmlController.java))
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

##### Entrypoint of the UI ([`FxUiManager`](../../easyfxml/src/main/java/moe/tristan/easyfxml/FxUiManager.java))
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

##### Main class ([`FxApplication`](../../easyfxml/src/main/java/moe/tristan/easyfxml/FxApplication.java))
```java
@SpringBootApplication // EasyFXML wires itself in the context via Spring Boot's autoconfiguration
public class HelloWorld extends FxApplication { // FxApplication is essential here to set-up JavaFX
    public static void main(String[] args) {    // and Spring cohabitation
        launch(args);
    }
}
```
