package moe.tristan.easyfxml.util;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class ButtonsTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {}

    @Test
    public void setOnClickWithNode() throws InterruptedException {
        final Button testButton = new Button("TEST_BUTTON");
        final AtomicBoolean success = new AtomicBoolean(false);

        Platform.runLater(() -> {
            Buttons.setOnClickWithNode(testButton, null, node -> success.set(true));
            testButton.fire();
        });

        Thread.sleep(1000);
        assertThat(success).isTrue();
    }
}
