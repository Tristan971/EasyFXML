package moe.tristan.easyfxml.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.beans.property.Property;

public final class Properties {

    private Properties() {
    }

    public static <T, P extends Property<T>> P newPropertyWithCallback(Supplier<P> propertyFactory, Consumer<T> callback) {
        final P property = propertyFactory.get();
        whenPropertyIsSet(property, callback);
        return property;
    }

    public static <T, P extends Property<T>> void whenPropertyIsSet(P property, Consumer<T> doWhenSet) {
        property.addListener((o, prev, cur) -> doWhenSet.accept(cur));
        if (property.getValue() != null) {
            doWhenSet.accept(property.getValue());
        }
    }

}
