package moe.tristan.easyfxml.model.components.listview;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.testfx.assertions.api.Assertions.assertThat;

public class ComponentListCellTest extends ApplicationTest {

    @Test
    public void updateItem() throws InterruptedException {
        final AtomicBoolean initialized = new AtomicBoolean(false);
        final BooleanProperty readProp = new SimpleBooleanProperty(false);
        final AtomicReference<String> value = new AtomicReference<>("");

        final Pane pane = new Pane();
        final ComponentCellFxmlController<String> clvcc = new ComponentCellFxmlController<>() {
            @Override
            public void updateWithValue(String newValue) {
                value.set(newValue);
            }

            @Override
            public void selectedProperty(final BooleanExpression selected) {
                readProp.bind(selected);
            }

            @Override
            public void initialize() {
                if (initialized.get()) {
                    throw new IllegalStateException("Double init!");
                }
                initialized.set(true);
            }
        };

        final TestListCell testListViewCell = new TestListCell(pane, clvcc);

        testListViewCell.updateItem("TEST", false);
        Thread.sleep(200);
        assertThat(value.get()).isEqualTo("TEST");

        testListViewCell.updateItem("TEST2", false);
        Thread.sleep(200);
        assertThat(value.get()).isEqualTo("TEST2");

        testListViewCell.updateItem(null, true);
        Thread.sleep(200);
        assertThat(value.get()).isEqualTo(null);
    }

    public class TestListCell extends ComponentListCell<String> {

        public TestListCell(Pane cellNode, ComponentCellFxmlController<String> controller) {
            super(cellNode, controller);
        }

    }

}
