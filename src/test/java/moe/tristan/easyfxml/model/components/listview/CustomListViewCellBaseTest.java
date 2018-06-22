package moe.tristan.easyfxml.model.components.listview;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.testfx.assertions.api.Assertions.assertThat;

public class CustomListViewCellBaseTest extends ApplicationTest {

    @Test
    public void updateItem() throws InterruptedException {
        final AtomicBoolean initialized = new AtomicBoolean(false);
        final BooleanProperty readProp = new SimpleBooleanProperty(false);
        final AtomicReference<String> value = new AtomicReference<>("");

        final Pane pane = new Pane();
        final CustomListViewCellController<String> clvcc = new CustomListViewCellController<String>() {
            @Override
            public void updateWithValue(String newValue) {
                value.set(newValue);
            }

            @Override
            public <U extends ObservableValue<Boolean>> void selectedProperty(U selected) {
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

        final TestListViewCell testListViewCell = new TestListViewCell(pane, clvcc);

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

    public class TestListViewCell extends CustomListViewCellBase<String> {

        public TestListViewCell(Pane cellNode, CustomListViewCellController<String> controller) {
            super(cellNode, controller);
        }

    }

}
