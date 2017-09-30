package moe.tristan.easyfxml.model.beanmanagement;

import com.sun.javafx.css.Stylesheet;
import io.vavr.control.Option;
import moe.tristan.easyfxml.FxmlController;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @see moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StylesheetManager extends AbstractInstanceManager<FxmlController, Stylesheet, Object> {
    @Override
    protected Stylesheet registerSingle(Class<? extends FxmlController> clazz, Stylesheet controller) {
        return super.registerSingle(clazz, controller);
    }

    @Override
    protected Option<Stylesheet> getSingle(Class<? extends FxmlController> clazz) {
        return super.getSingle(clazz);
    }

    @Override
    protected Map.Entry<Object, Stylesheet> registerMultiple(Class<? extends FxmlController> clazz, Object o, Stylesheet instance) {
        return super.registerMultiple(clazz, o, instance);
    }

    @Override
    protected Map.Entry<?, Stylesheet> registerMultiple(Class<? extends FxmlController> clazz, Supplier<Object> selector, Stylesheet instance) {
        return super.registerMultiple(clazz, selector, instance);
    }

    @Override
    protected Option<Stylesheet> getMultiple(Class<? extends FxmlController> clazz, Object o) {
        return super.getMultiple(clazz, o);
    }
}
