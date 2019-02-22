package moe.tristan.easyfxml.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class PropertyAwaitingFxmlControllerTest {

    @Test
    public void shouldCallDirectlyIfSetOnInitializeAndAlsoSubsequently() {
        final Object INITIAL = new Object();

        final Property<Object> prop = new SimpleObjectProperty<>();

        final PropertyAwaitingFxmlController<Object> controller = new PropertyAwaitingFxmlController<>() {
            @Override
            public void controllerDidLoad() {
                prop.bind(valueProperty());
            }

            @Override
            public void valueChanged(Object value) {
            }
        };
        controller.setValue(INITIAL);
        controller.initialize();

        assertThat(prop.getValue()).isSameAs(INITIAL);
        assertThat(controller.getValue()).isSameAs(INITIAL);

        final Object NEXT = new Object();
        controller.setValue(NEXT);

        assertThat(prop.getValue()).isSameAs(NEXT);
        assertThat(controller.getValue()).isSameAs(NEXT);
    }


}
