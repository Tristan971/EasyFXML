package moe.tristan.easyfxml.model.beanmanagement;

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
public class ControllerManager extends AbstractInstanceManager<FxmlController, FxmlController, Object> {
    @Override
    public FxmlController registerSingle(Class<? extends FxmlController> clazz, FxmlController controller) {
        return super.registerSingle(clazz, controller);
    }

    @Override
    public Option<FxmlController> getSingle(Class<? extends FxmlController> clazz) {
        return super.getSingle(clazz);
    }

    @Override
    public Map.Entry<Object, FxmlController> registerMultiple(Class<? extends FxmlController> clazz, Object o, FxmlController instance) {
        return super.registerMultiple(clazz, o, instance);
    }

    @Override
    public Map.Entry<?, FxmlController> registerMultiple(Class<? extends FxmlController> clazz, Supplier<Object> selector, FxmlController instance) {
        return super.registerMultiple(clazz, selector, instance);
    }

    @Override
    public Option<FxmlController> getMultiple(Class<? extends FxmlController> clazz, Object o) {
        return super.getMultiple(clazz, o);
    }
}
