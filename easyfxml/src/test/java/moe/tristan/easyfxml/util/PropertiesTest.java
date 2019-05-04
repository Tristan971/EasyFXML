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

import static moe.tristan.easyfxml.util.Properties.whenPropertyIsSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.Test;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class PropertiesTest {

    @Test
    public void shouldCallDirectlyIfSetWithValue() {
        final Object element = new Object();

        final Property<Object> called = new SimpleObjectProperty<>(null);

        Properties.newPropertyWithCallback(() -> new SimpleObjectProperty<>(element), called::setValue);

        assertThat(called.getValue()).isSameAs(element);
    }

    @Test
    public void shouldCallConsumerOnEverySetCall() {
        final Property<Integer> called = new SimpleObjectProperty<>();

        final Property<Integer> property = Properties.newPropertyWithCallback(SimpleObjectProperty::new, called::setValue);

        assertThat(called.getValue()).isNull();

        IntStream.range(0, 1000).forEach(value -> {
            property.setValue(value);
            assertThat(called.getValue()).isEqualTo(value);
        });
    }

    @Test
    public void awaitCallsDirectlyIfSet() {
        final Property<Object> valuedProp = new SimpleObjectProperty<>(new Object());

        final Property<Object> listener = new SimpleObjectProperty<>();

        whenPropertyIsSet(valuedProp, listener::setValue);

        assertThat(listener.getValue()).isEqualTo(valuedProp.getValue());
    }

    @Test
    public void awaitCallsAwaitsSetIfNullOriginally() {
        final Property<Object> valuedProp = new SimpleObjectProperty<>();

        final Property<Object> listener = new SimpleObjectProperty<>();

        whenPropertyIsSet(valuedProp, listener::setValue);

        assertThat(listener.getValue()).isNull();

        valuedProp.setValue(new Object());

        assertThat(listener.getValue()).isEqualTo(valuedProp.getValue());
    }

    @Test
    public void awaitShouldCallConsumerOnEverySetCall() {
        final Property<Integer> called = new SimpleObjectProperty<>();

        final Property<Integer> property = new SimpleObjectProperty<>();

        whenPropertyIsSet(property, called::setValue);

        assertThat(called.getValue()).isNull();

        IntStream.range(0, 1000).forEach(value -> {
            property.setValue(value);
            assertThat(called.getValue()).isEqualTo(value);
        });
    }

}
