package moe.tristan.easyfxml.model.components.listview;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

public class ComponentListCellTest extends ApplicationTest {

    @Test
    public void updateItem() {
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
        await().until(() -> value.get().equals("TEST"));

        testListViewCell.updateItem("TEST2", false);
        await().until(() -> value.get().equals("TEST2"));

        testListViewCell.updateItem(null, true);
        await().until(() -> value.get() == null);
    }

    public class TestListCell extends ComponentListCell<String> {

        public TestListCell(Pane cellNode, ComponentCellFxmlController<String> controller) {
            super(cellNode, controller);
        }

    }

}
