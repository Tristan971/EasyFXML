package moe.tristan.easyfxml.model.views;

import io.vavr.control.Option;
import javafx.stage.Stage;
import moe.tristan.easyfxml.FxmlController;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @see moe.tristan.easyfxml.model.views.AbstractInstanceManager
 *
 * {@inheritDoc}
 */
@Component
public class StageManager extends AbstractInstanceManager<FxmlController, Stage, Object> {
    @Override
    public Stage registerSingle(Class<? extends FxmlController> clazz, Stage controller) {
        return super.registerSingle(clazz, controller);
    }

    @Override
    public Option<Stage> getSingle(Class<? extends FxmlController> clazz) {
        return super.getSingle(clazz);
    }

    @Override
    public Map.Entry<Object, Stage> registerMultiple(Class<? extends FxmlController> clazz, Object o, Stage instance) {
        return super.registerMultiple(clazz, o, instance);
    }

    @Override
    public Map.Entry<?, Stage> registerMultiple(Class<? extends FxmlController> clazz, Supplier<Object> selector, Stage instance) {
        return super.registerMultiple(clazz, selector, instance);
    }

    @Override
    public Option<Stage> getMultiple(Class<? extends FxmlController> clazz, Object o) {
        return super.getMultiple(clazz, o);
    }
}
