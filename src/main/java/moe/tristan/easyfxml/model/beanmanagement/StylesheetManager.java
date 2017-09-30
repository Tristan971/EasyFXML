package moe.tristan.easyfxml.model.beanmanagement;

import com.sun.javafx.css.Stylesheet;
import moe.tristan.easyfxml.FxmlController;
import org.springframework.stereotype.Component;

/**
 * @see moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StylesheetManager extends AbstractInstanceManager<FxmlController, Stylesheet, Object> {}
