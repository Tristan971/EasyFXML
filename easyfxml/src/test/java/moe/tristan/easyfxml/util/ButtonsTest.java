package moe.tristan.easyfxml.util;

import static javafx.scene.input.MouseButton.PRIMARY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import javafx.scene.control.Button;

import moe.tristan.easyfxml.test.FxNodeTest;

public class ButtonsTest extends FxNodeTest {

    @Test
    public void setOnClickWithNode() {
        final AtomicBoolean success = new AtomicBoolean(false);

        final Button testButton = new Button("TEST_BUTTON");
        Buttons.setOnClick(testButton, () -> success.set(true));

        withNodes(testButton)
            .startWhen(() -> point(testButton).query() != null)
            .willDo(() -> clickOn(testButton, PRIMARY))
            .andAwaitFor(success::get);

        assertThat(success).isTrue();
    }

}
