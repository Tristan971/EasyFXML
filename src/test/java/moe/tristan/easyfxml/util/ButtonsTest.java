package moe.tristan.easyfxml.util;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.PointQuery;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class ButtonsTest extends ApplicationTest {

    private Pane container;

    @Override
    public void start(Stage stage) {
        final Pane container = new Pane();
        this.container = container;
        final Scene scene = new Scene(container);
        stage.setScene(scene);
        stage.setX(200);
        stage.setY(200);
        stage.show();
    }

    @Test
    public void setOnClickWithNode() throws InterruptedException {
        final Button testButton = new Button("TEST_BUTTON");
        final AtomicBoolean success = new AtomicBoolean(false);

        Platform.runLater(() -> {
            container.getChildren().add(testButton);
            Buttons.setOnClickWithNode(testButton, null, node -> success.set(true));
            final PointQuery pos = point(testButton).atOffset(10, 10);
            clickOn(pos.query(), MouseButton.PRIMARY);
        });

        Thread.sleep(2000);
        assertThat(success).isTrue();
    }
}
