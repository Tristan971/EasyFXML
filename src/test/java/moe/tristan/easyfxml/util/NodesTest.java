package moe.tristan.easyfxml.util;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NodesTest extends ApplicationTest {

    private static final double MARGIN = 5d;
    private static final int WIDTH_PANE = 200;
    private static final int HEIGHT_PANE = 200;

    private Button testButton;

    @Override
    public void start(final Stage stage) {
        this.testButton = new Button();
        final AnchorPane testPane = new AnchorPane(this.testButton);
        stage.setScene(new Scene(testPane, WIDTH_PANE, HEIGHT_PANE));
        stage.show();
    }

    @Test
    public void centerNode() {
        Nodes.centerNode(this.testButton, MARGIN);
    }
}
