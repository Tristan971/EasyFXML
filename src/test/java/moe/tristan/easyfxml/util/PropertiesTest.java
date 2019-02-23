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
