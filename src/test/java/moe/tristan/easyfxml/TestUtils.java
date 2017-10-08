package moe.tristan.easyfxml;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestUtils {
    public static Button fillTestStage(final Stage stage) {
        final Button testButton = new Button("TEST");
        final Pane testPane = new Pane(testButton);
        final Scene testScene = new Scene(testPane);
        stage.setScene(testScene);
        return testButton;
    }
}
