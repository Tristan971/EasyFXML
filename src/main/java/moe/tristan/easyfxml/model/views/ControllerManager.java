package moe.tristan.easyfxml.model.views;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(scopeName = SCOPE_SINGLETON)
public class ControllerManager {

    private final Map<Class<? extends FxmlController>, Set<FxmlController>> controllers = new ConcurrentHashMap<>();

    /**
     * Registers the controller in the Set of available controllers of type T.
     * We offer the opportunity of storing multiple conrollers of the same type.
     * It is up to the implementor to know how to choose the right one they want
     * according to the situation and to null it out as soon as it is not useful
     * anymore to avoid GC pressure and eventually memory leaks.
     *
     * If no controller of the same type has been saved before, it creates a new
     * set using {@link Collections#singleton(Object)}. If we are saving a second
     * one it switches to a {@link ConcurrentSkipListSet} to save instance.
     *
     * If there is no currently non-null refs in it it considers to have never
     * stored any instance and goes to the {@link Collections#singleton(Object)}
     * method.
     *
     * See {@link ControllerManager} for more information.
     *
     * @param controllerClass The controller class for which we are storing an instance.
     * @param controllerInstance The registered instance.
     * @param <T> The type of the class to provide at least some amount of type checking.
     */
    public <T extends FxmlController> void registerController(final Class<T> controllerClass, final T controllerInstance) {
        this.controllers.compute(
                controllerClass,
                (aClass, fxmlControllers) -> {
                    if ((fxmlControllers == null) || fxmlControllers.stream().allMatch(Objects::isNull)) {
                        return Collections.singleton(controllerInstance);
                    } else if (fxmlControllers.size() == 1) {
                        return new ConcurrentSkipListSet<>(fxmlControllers);
                    }

                    fxmlControllers.add(controllerInstance);
                    return fxmlControllers;
                }
        );
    }
}
