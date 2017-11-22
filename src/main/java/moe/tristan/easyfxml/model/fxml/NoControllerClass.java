package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.stereotype.Component;

/**
 * Empty controller for logicless components
 */
@Component
public class NoControllerClass implements FxmlController {
    @Override
    public void initialize() {}
}
