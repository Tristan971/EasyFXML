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

    @Override
    public void initialize() {
    }

}
