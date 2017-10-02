package moe.tristan.easyfxml.model.beanmanagement;

import javafx.stage.Stage;
import moe.tristan.easyfxml.FxmlNode;
import org.springframework.stereotype.Component;

/**
 * @see moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StageManager extends AbstractInstanceManager<FxmlNode, Stage, Object> {}
