package moe.tristan.easyfxml.util;

import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NodesTest extends ApplicationTest {

    private static final double MARGIN = 5d;

    private Button testButton;

    @Override
    public void start(final Stage stage) {
        this.testButton = new Button();
        Pane container = new AnchorPane(this.testButton);
        stage.setScene(new Scene(container));
        stage.show();
    }

    @Test
    public void centerNode() {
        Nodes.centerNode(this.testButton, MARGIN);
    }

    @Test
    public void testAutosizeHelpers() {
        final BooleanProperty shouldDisplay = new SimpleBooleanProperty(true);
        final Button testButton = new Button();
        Nodes.hideAndResizeParentIf(testButton, shouldDisplay);

        assertThat(testButton.isManaged()).isTrue();
        assertThat(testButton.isVisible()).isTrue();

        shouldDisplay.setValue(false);
        assertThat(testButton.isManaged()).isFalse();
        assertThat(testButton.isVisible()).isFalse();
    }

}
