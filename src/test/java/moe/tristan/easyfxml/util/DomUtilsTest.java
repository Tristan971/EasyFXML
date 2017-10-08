package moe.tristan.easyfxml.util;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class DomUtilsTest extends ApplicationTest {
    private static final double MARGIN = 5d;

    private AnchorPane testPane;
    private Button testButton;

    @Override
    public void start(Stage stage) {
        this.testButton = new Button();
        this.testPane = new AnchorPane(this.testButton);
        stage.setScene(new Scene(this.testPane, 200, 200));
        stage.show();
    }

    @Test
    public void centerNode() {
        DomUtils.centerNode(this.testButton, MARGIN);
    }
}
