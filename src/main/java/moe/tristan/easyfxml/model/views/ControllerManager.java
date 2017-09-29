package moe.tristan.easyfxml.model.views;

import io.vavr.control.Option;
import javafx.stage.Stage;
import javafx.stage.Window;
import moe.tristan.easyfxml.FxmlController;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is aimed at helping you store references to your controller class instances.
 * <p>
 * We distinguish two types of controllers :
 * - SingleStage (only 1 instance of it is ever active at the same time)
 * - MultiStage (any number of instances active at the same time)
 * <p>
 * In case all is well, you only use SingleStage and getting the instance is as easy as providing the class
 * that it is an instance of.
 * <p>
 * In case you have stages ({@link Stage} is basically a {@link Window} that you can manipulate) that can show up
 * a buch of times at once/in parallel you can have multiple instances of the same controller. This is why we need
 * to make sure we can store them in an easy wa that can allow distinction between those when needed afterwards.
 * See {@link #registerMultistageController(Class, Object, FxmlController)} for this.
 */
@Component
public class ControllerManager {
    private final Map<Class<? extends FxmlController>, FxmlController> singleStageControllers = new ConcurrentHashMap<>();

    private final Map<Class<? extends FxmlController>, Map<Object, FxmlController>> multiStageControllers = new ConcurrentHashMap<>();

    public FxmlController registerSingleStageController(final Class<? extends FxmlController> clazz, final FxmlController controller) {
        return this.singleStageControllers.put(clazz, controller);
    }

    public Option<FxmlController> getSingleStageController(final Class<? extends FxmlController> clazz) {
        return Option.of(this.singleStageControllers.get(clazz));
    }

    /**
     * This method stores your controller in a {@link ConcurrentHashMap} that looks like this :
     * |-- ClassA --
     * |           |-- Selector1 -> Instance 1 of class ClassA
     * |           |-- Selector2 -> Instance 2 of class ClassA
     * |
     * |-- CLassB --
     * |           |-- Selector# -> Instance # of class ClassB
     * |          ...
     * |           |-- SelectorN -> Instance N of class ClassB
     * ...
     * |
     *
     * <p>
     * The point is that "selectors" are anything you want them to be, whether it be the hashcode of the
     * instance (but you won't be able to access it easily later), or some kind of related Node.
     * Whatever fits your model in the best way.
     * <p>
     * Be wary of collisions (i.e. Instance 1 and 2 having the same selector) as it replaces instances in case of collision.
     * That is to say if ClassA's Instance 1's selector is "sel1" and you register(ClassA.class, "sel1", Instance2) then
     * you will lose access to Instance 1.
     * <p>
     * You are responsible for providing correct and non-colliding selectors.
     * We also offer a sloght variation in the method with
     * {@link #registerMultistageController(Class, Supplier, FxmlController)}. Look at it if you think about writing
     * something like a {@link Function} &lt; {@link FxmlController} , {@link Object} &gt; to compute selectors.
     *
     * @param clazz      The class of your controller
     * @param selector   The selector that you have to provide to recover this particular controller instance later
     * @param controller The controller to save.
     *
     * @return A map entry containing the selector and the controller registered in case you need it.
     *
     * @throws RuntimeException in case there was an error in adding the controller.
     */
    public Entry<?, FxmlController> registerMultistageController(final Class<? extends FxmlController> clazz, final Object selector, final FxmlController controller) {
        final Optional<Entry<Object, FxmlController>> newEntry = this.multiStageControllers.merge(
                clazz,
                new HashMap<Object, FxmlController>() {{
                    this.put(selector, controller);
                }},
                this::mergeMultistageController
        ).entrySet().stream().filter(entry -> entry.getKey().equals(selector)).findAny();
        return newEntry.orElseThrow(RuntimeException::new);
    }

    /**
     * Should you have a premade method that supplies you with selectors so you can choose your own management system,
     * then you can give access to a lambda version of it in the form of a {@link Supplier} instead of giving the actual
     * value directly. This is marginal but if you wished for it, here it is.
     *
     * Apart from this, the way this method works is strictly identical to
     * {@link #registerMultistageController(Class, Object, FxmlController)}.
     */
    public Entry<?, FxmlController> registerMultistageController(final Class<? extends FxmlController> clazz, final Supplier<?> selector, final FxmlController controller) {
        return this.registerMultistageController(clazz, selector.get(), controller);
    }

    /**
     * Tries to find the controller you look for. Returns an {@link Option} (similar to {@link Optional}) that either
     * contains {@link Option.Some} &lt; {@link FxmlController} &gt; or {@link Option.None}.
     *
     * Look at {@link Option} for information on how to use it.
     *
     * @param clazz The class of the instance of controller you look for.
     * @param selector The selector previously used in {@link #registerMultistageController(Class, Object, FxmlController)}
     * @return The {@link Option} &lt; {@link FxmlController} &gt; that either contains it (success) or is empty (null was found at some
     * point in the hierarchy meaning one of you or me broke something somewhere).
     */
    public Option<FxmlController> getMultistageController(final Class<? extends FxmlController> clazz, final Object selector) {
        return Option.of(this.multiStageControllers.get(clazz)).map(selectorMap -> selectorMap.get(selector));
    }

    /**
     * This is a very basic and simple merge for the {@link Map} &lt; {@link Object} , {@link FxmlController} &gt;
     * that contains the controller instances organized per class and distinct by selector.
     *
     * @param oldValue The old map provided by Java
     * @param newValue The new map to be added
     * @return The merge result to be stored (a simple result of a null-check followed by a {@link Map#putAll(Map)}).
     */
    private Map<Object, FxmlController> mergeMultistageController(final Map<Object, FxmlController> oldValue, final Map<Object, FxmlController> newValue) {
        final Map<Object, FxmlController> finalValue =
                (oldValue == null) ?
                        new ConcurrentHashMap<>() :
                        new ConcurrentHashMap<>(oldValue);

        finalValue.putAll(newValue);
        return finalValue;
    }
}
