package moe.tristan.easyfxml.model.views;

import io.vavr.control.Option;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;
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
 * See {@link #registerMultistageController(Class, Object, TYPE_COMMON_INST)} for this.
 */
public abstract class AbstractManager<TYPE_COMMON_INST, TYPE_ACTUAL_INST> {

    private final Map<Class<? extends TYPE_COMMON_INST>, TYPE_ACTUAL_INST> singleStageControllers = new ConcurrentHashMap<>();
    private final Map<Class<? extends TYPE_COMMON_INST>, Map<Object, TYPE_ACTUAL_INST>> multiStageControllers = new ConcurrentHashMap<>();

    public TYPE_ACTUAL_INST registerSingleStageController(final Class<? extends TYPE_COMMON_INST> clazz, final TYPE_ACTUAL_INST controller) {
        return this.singleStageControllers.put(clazz, controller);
    }

    public Option<TYPE_ACTUAL_INST> getSingleStageController(final Class<? extends TYPE_COMMON_INST> clazz) {
        return Option.of(this.singleStageControllers.get(clazz));
    }

    /**
     * This method stores your controller in a {@link ConcurrentHashMap} that looks like this :<br>
     * |-- ClassA --<br>
     * |           |-- Selector1 -> Instance 1 of class ClassA<br>
     * |           |-- Selector2 -> Instance 2 of class ClassA<br>
     * |<br>
     * |-- CLassB --<br>
     * |           |-- Selector# -> Instance # of class ClassB<br>
     * |          ...<br>
     * |           |-- SelectorN -> Instance N of class ClassB<br>
     * ...<br>
     * |<br>
     * <br>
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
     * {@link #registerMultistageController(Class, Supplier, TYPE_COMMON_INST)}. Look at it if you think about writing
     * something like a {@link Function} &lt; {@link TYPE_COMMON_INST} , {@link Object} &gt; to compute selectors.
     *
     * @param clazz      The class of your controller
     * @param selector   The selector that you have to provide to recover this particular controller instance later
     * @param controller The controller to save.
     *
     * @return A map entry containing the selector and the controller registered in case you need it.
     *
     * @throws RuntimeException in case there was an error in adding the controller.
     */
    public Map.Entry<?, TYPE_ACTUAL_INST> registerMultistageController(final Class<? extends TYPE_COMMON_INST> clazz, final Object selector, final TYPE_ACTUAL_INST controller) {
        final Optional<Map.Entry<Object, TYPE_ACTUAL_INST>> newEntry = this.multiStageControllers.merge(
                clazz,
                new HashMap<Object, TYPE_ACTUAL_INST>() {{
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
     * {@link #registerMultistageController(Class, Object, TYPE_COMMON_INST)}.
     */
    public Map.Entry<?, TYPE_ACTUAL_INST> registerMultistageController(final Class<? extends TYPE_COMMON_INST> clazz, final Supplier<?> selector, final TYPE_ACTUAL_INST controller) {
        return this.registerMultistageController(clazz, selector.get(), controller);
    }

    /**
     * Tries to find the controller you look for. Returns an {@link Option} (similar to {@link Optional}) that either
     * contains {@link Option.Some} &lt; {@link TYPE_COMMON_INST} &gt; or {@link Option.None}.
     *
     * Look at {@link Option} for information on how to use it.
     *
     * @param clazz The class of the instance of controller you look for.
     * @param selector The selector previously used in {@link #registerMultistageController(Class, Object, TYPE_COMMON_INST)}
     * @return The {@link Option} &lt; {@link TYPE_COMMON_INST} &gt; that either contains it (success) or is empty (null was found at some
     * point in the hierarchy meaning one of you or me broke something somewhere).
     */
    public Option<TYPE_ACTUAL_INST> getMultistageController(final Class<? extends TYPE_COMMON_INST> clazz, final Object selector) {
        return Option.of(this.multiStageControllers.get(clazz)).map(selectorMap -> selectorMap.get(selector));
    }

    /**
     * This is a very basic and simple merge for the {@link Map} &lt; {@link Object} , {@link TYPE_COMMON_INST} &gt;
     * that contains the controller instances organized per class and distinct by selector.
     *
     * @param oldValue The old map provided by Java
     * @param newValue The new map to be added
     * @return The merge result to be stored (a simple result of a null-check followed by a {@link Map#putAll(Map)}).
     */
    private Map<Object, TYPE_ACTUAL_INST> mergeMultistageController(final Map<Object, TYPE_ACTUAL_INST> oldValue, final Map<Object, TYPE_ACTUAL_INST> newValue) {
        final Map<Object, TYPE_ACTUAL_INST> finalValue =
                (oldValue == null) ?
                        new ConcurrentHashMap<>() :
                        new ConcurrentHashMap<>(oldValue);

        finalValue.putAll(newValue);
        return finalValue;
    }
}