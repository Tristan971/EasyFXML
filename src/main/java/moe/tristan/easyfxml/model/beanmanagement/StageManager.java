package moe.tristan.easyfxml.model.beanmanagement;

import javafx.stage.Stage;
import moe.tristan.easyfxml.FxmlController;
import org.springframework.stereotype.Component;

/**
 * @see moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StageManager extends AbstractInstanceManager<FxmlController, Stage, Object> {}
