package moe.tristan.easyfxml.model.beanmanagement;

import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.FxmlStylesheet;
import org.springframework.stereotype.Component;

/**
 * @see moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StylesheetManager extends AbstractInstanceManager<FxmlNode, FxmlStylesheet, Object> {}
