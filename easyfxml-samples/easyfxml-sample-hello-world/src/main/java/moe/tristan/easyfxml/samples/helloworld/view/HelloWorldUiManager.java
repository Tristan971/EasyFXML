package moe.tristan.easyfxml.samples.helloworld.view;

import org.springframework.stereotype.Component;

import javafx.stage.Stage;

import moe.tristan.easyfxml.FxUiManager;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.samples.helloworld.view.hello.HelloComponent;

@Component
public class HelloWorldUiManager extends FxUiManager {

    private final HelloComponent helloComponent;

    protected HelloWorldUiManager(HelloComponent helloComponent) {
        this.helloComponent = helloComponent;
    }

    /**
     * @return the main {@link Stage}'s title you want
     */
    @Override
    protected String title() {
        return "Hello, World!";
    }

    /**
     * @return the component to load as the root view in your main {@link Stage}.
     */
    @Override
    protected FxmlNode mainComponent() {
        return helloComponent;
    }

}
