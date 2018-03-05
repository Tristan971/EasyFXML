package moe.tristan.easyfxml.model.fxml;

import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.api.FxmlController;

/**
 * Empty controller for logicless components.
 * <p>
 * You can use it if your component does nothing more than display its content.
 */
@Component
public class NoControllerClass implements FxmlController {

    /**
     * Empty voluntarily as no logic is to be included in this class.
     */
    @SuppressWarnings("EmptyMethod")
    @Override
    public void initialize() {
        //see doc
    }

}
