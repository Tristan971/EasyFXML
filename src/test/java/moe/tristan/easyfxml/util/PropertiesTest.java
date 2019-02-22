package moe.tristan.easyfxml.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.Test;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class PropertiesTest {

    @Test
    public void shouldCallDirectlyIfSetWithValue() {
        Object element = new Object();

        Property<Object> called = new SimpleObjectProperty<>(null);

        Properties.propertyWithCallbackOnSet(() -> new SimpleObjectProperty<>(element), called::setValue);

        assertThat(called.getValue()).isSameAs(element);
    }

    @Test
    public void shouldCallConsumerOnEverySetCall() {
        Property<Integer> called = new SimpleObjectProperty<>();

        final Property<Integer> property = Properties.propertyWithCallbackOnSet(SimpleObjectProperty::new, called::setValue);

        assertThat(called.getValue()).isNull();

        IntStream.range(0, 1000).forEach(value -> {
            property.setValue(value);
            assertThat(called.getValue()).isEqualTo(value);
        });
    }

}
