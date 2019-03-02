package moe.tristan.easyfxml.samples.helloworld.view;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javafx.stage.Stage;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;
import moe.tristan.easyfxml.FxUiManager;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.samples.helloworld.view.hello.HelloComponent;

@Component
public class HelloWorldUiManager extends FxUiManager {

    private final HelloComponent helloComponent;

    /**
     * Here we wire up the {@link EasyFxml} instance from your application's Spring Context.
     * The reason for this is (mostly) to enforce the sanity check that everything is wired up properly right at the beginning of the application's lifecycle.
     *
     * @param easyFxml       the instance of {@link EasyFxml} as exposed by {@link Import}ing {@link EasyFxmlAutoConfiguration} either in your main application
     *                       or one of your configuration classes.
     * @param helloComponent our root {@link FxmlNode} to load at startup
     */
    protected HelloWorldUiManager(EasyFxml easyFxml, HelloComponent helloComponent) {
        super(easyFxml);
        this.helloComponent = helloComponent;
    }

    /**
     * @return the main {@link Stage}'s title you want
     */
    @Override
    protected String title() {
        return "EasyFXML Sample application: HelloWorld";
    }

    /**
     * @return the component to load as the root view in your main {@link Stage}.
     */
    @Override
    protected FxmlNode mainComponent() {
        return helloComponent;
    }

}
