package moe.tristan.easyfxml.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.beans.property.Property;

public final class Properties {

    private Properties() {
    }

    public static <T, P extends Property<T>> P propertyWithCallbackOnSet(Supplier<P> propertyFactory, Consumer<T> callback) {
        final P property = propertyFactory.get();
        if (property.getValue() != null) {
            callback.accept(property.getValue());
        }

        property.addListener((o, prev, cur) -> callback.accept(cur));

        return property;
    }

}
