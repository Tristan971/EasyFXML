package moe.tristan.easyfxml.model.beanmanagement;

import io.vavr.control.Option;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is aimed at helping you store references to various kinds of class instances.
 * <p>
 * We distinguish two types of instances :
 * - Single = only 1 instance of it is ever active at the same time)
 * - Multiple/Prototype = any number of instances active at the same time
 * <p>
 * In case all is well, you only use Single and getting the instance is as easy as providing the instance
 * that it is a children of.
 * <p>
 * In case you have stages multiple instances for the same parent instance, we cannot keep doing that.
 * This is why we need to make sure we can store them in an easy wa that can allow distinction
 * between those when needed afterwards.
 * See {@link #registerMultiple(TYPE_COMMON_INST, TYPE_SELECTOR, TYPE_ACTUAL_INST)} for this.
 */
public abstract class AbstractInstanceManager<TYPE_COMMON_INST, TYPE_ACTUAL_INST, TYPE_SELECTOR> {

    private final Map<TYPE_COMMON_INST, TYPE_ACTUAL_INST> singletons = new ConcurrentHashMap<>();
    private final Map<TYPE_COMMON_INST, Map<TYPE_SELECTOR, TYPE_ACTUAL_INST>> prototypes = new ConcurrentHashMap<>();

    public TYPE_ACTUAL_INST registerSingle(final TYPE_COMMON_INST parent, final TYPE_ACTUAL_INST instance) {
        return this.singletons.put(parent, instance);
    }

    /**
     * Should you have a premade method that supplies you with selectors so you can choose your own management system,
     * then you can give access to a lambda version of it in the form of a {@link Supplier} instead of giving the actual
     * value directly. This is marginal but if you wished for it, here it is.
     * <p>
     * Apart from this, the way this method works is strictly identical to
     * {@link #registerMultiple(TYPE_COMMON_INST, TYPE_SELECTOR, TYPE_ACTUAL_INST)}.
     */
    public Map.Entry<?, TYPE_ACTUAL_INST> registerMultiple(
        final TYPE_COMMON_INST parent,
        final Supplier<TYPE_SELECTOR> selector,
        final TYPE_ACTUAL_INST instance
    ) {
        return this.registerMultiple(parent, selector.get(), instance);
    }

    /**
     * This method stores your instances in a {@link ConcurrentHashMap} that looks like this :<br>
     * |-- CommonInst1 --<br>
     * |                |-- Selector1 -> Instance 1 of class {@link TYPE_ACTUAL_INST}<br>
     * |                |-- Selector2 -> Instance 2 of class {@link TYPE_ACTUAL_INST}<br>
     * |<br>
     * |-- CommonInst2 --<br>
     * |                |-- Selector# -> Instance # of class {@link TYPE_ACTUAL_INST}<br>
     * |                ...<br>
     * |                |-- SelectorN -> Instance N of class {@link TYPE_ACTUAL_INST}<br>
     * ...<br>
     * |<br>
     * <br>
     * <p>
     * The point is that "selectors" are anything you want them to be, whether it be the hashcode of the
     * instance (but you won't be able to access it easily later), or some kind of related Node.
     * Whatever fits your model in the best way.
     * <p>
     * Be wary of collisions (i.e. Instance 1 and 2 having the same selector) as it replaces instances in case of collision.
     * <p>
     * You are responsible for providing correct and non-colliding selectors.
     * We also offer a sloght variation in the method with
     * {@link #registerMultiple(TYPE_COMMON_INST, Supplier, TYPE_COMMON_INST)}. Look at it if you think about writing
     * something like a {@link Function} &lt; {@link TYPE_COMMON_INST} , {@link Object} &gt; to compute selectors.
     *
     * @param parent   An instance, typically an enum member
     * @param selector The selector that you have to provide to recover this particular instance later
     * @param instance The instance to save.
     * @return A map entry containing the selector and the controller registered in case you need it.
     * @throws RuntimeException in case there was an error in saving the instance.
     */
    public Map.Entry<TYPE_SELECTOR, TYPE_ACTUAL_INST> registerMultiple(
        final TYPE_COMMON_INST parent,
        final TYPE_SELECTOR selector,
        final TYPE_ACTUAL_INST instance
    ) {
        final Optional<Map.Entry<TYPE_SELECTOR, TYPE_ACTUAL_INST>> newEntry = this.prototypes.merge(
            parent,
            new HashMap<TYPE_SELECTOR, TYPE_ACTUAL_INST>() {{
                this.put(selector, instance);
            }},
            this::mergePrototypes
        ).entrySet().stream().filter(entry -> entry.getKey().equals(selector)).findAny();
        return newEntry.orElseThrow(RuntimeException::new);
    }

    /**
     * Tries to find the instance you look for.
     * Returns an {@link Option} (similar to {@link Optional}) that either
     * contains {@link Option.Some} (found) or {@link Option.None} (not found/exception was thrown).
     * <p>
     * Look at {@link Option} for information on how to use it.
     *
     * @param parent   The instance who's children you look for.
     * @param selector The selector previously used in {@link #registerMultiple(TYPE_COMMON_INST, TYPE_SELECTOR, TYPE_ACTUAL_INST)}
     * @return The {@link Option} that either contains it ({@link Option.Some}) or is empty ({@link Option.None, which means
     * it was not found or at some point in the hierarchy there has been an exception).
     */
    public Option<TYPE_ACTUAL_INST> getMultiple(final TYPE_COMMON_INST parent, final TYPE_SELECTOR selector) {
        return Option.of(this.prototypes.get(parent)).map(selectorMap -> selectorMap.get(selector));
    }

    public List<TYPE_ACTUAL_INST> getAll(final TYPE_COMMON_INST parent) {
        final List<TYPE_ACTUAL_INST> all = this.getMultiples(parent);
        this.getSingle(parent).peek(all::add);
        return all;
    }

    public List<TYPE_ACTUAL_INST> getMultiples(final TYPE_COMMON_INST parent) {
        return new ArrayList<>(Option.of(this.prototypes.get(parent))
            .map(Map::values)
            .getOrElse(Collections.emptyList()));
    }

    public Option<TYPE_ACTUAL_INST> getSingle(final TYPE_COMMON_INST parent) {
        return Option.of(this.singletons.get(parent));
    }

    /**
     * This is a very basic and simple merge for the {@link Map} &lt; {@link TYPE_SELECTOR} , {@link TYPE_COMMON_INST} &gt;
     * that contains the instances organized per parent instance and distinct by selector.
     *
     * @param oldValue The old map provided by Java
     * @param newValue The new map to be added
     * @return The merge result to be stored (a simple result of a null-check followed by a {@link Map#putAll(Map)}).
     */
    private Map<TYPE_SELECTOR, TYPE_ACTUAL_INST> mergePrototypes(
        final Map<TYPE_SELECTOR, TYPE_ACTUAL_INST> oldValue,
        final Map<TYPE_SELECTOR, TYPE_ACTUAL_INST> newValue
    ) {
        final Map<TYPE_SELECTOR, TYPE_ACTUAL_INST> finalValue =
            (oldValue == null) ?
                new ConcurrentHashMap<>() :
                new ConcurrentHashMap<>(oldValue);

        finalValue.putAll(newValue);
        return finalValue;
    }
}
