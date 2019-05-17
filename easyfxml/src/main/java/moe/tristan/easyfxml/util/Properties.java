/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

public final class Properties {

    private Properties() {
    }

    public static <T, P extends ObservableValue<T>> P newPropertyWithCallback(Supplier<P> propertyFactory, Consumer<T> callback) {
        final P property = propertyFactory.get();
        whenPropertyIsSet(property, callback);
        return property;
    }

    public static <T, P extends ObservableValue<T>> void whenPropertyIsSet(P property, Consumer<T> doWhenSet) {
        whenPropertyIsSet(property, () -> doWhenSet.accept(property.getValue()));
    }

    public static <A, B, P extends ObservableValue<A>> void whenPropertyIsSet(P property, Function<A, B> adapter, Consumer<B> doWhenSet) {
        whenPropertyIsSet(property, () -> doWhenSet.accept(adapter.apply(property.getValue())));
    }

    public static <S, C> void bind(ObservableValue<S> from, Function<S, C> adapter, Property<C> to) {
        whenPropertyIsSet(from, adapter, to::setValue);
    }

    public static <T, P extends ObservableValue<T>> void whenPropertyIsSet(P property, Runnable doWhenSet) {
        property.addListener((o, prev, cur) -> doWhenSet.run());
        if (property.getValue() != null) {
            doWhenSet.run();
        }
    }

}
