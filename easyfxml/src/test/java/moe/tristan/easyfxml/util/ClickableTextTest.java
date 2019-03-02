package moe.tristan.easyfxml.util;

import static javafx.scene.input.MouseButton.PRIMARY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import moe.tristan.easyfxml.test.FxNodeTest;

public class ClickableTextTest extends FxNodeTest {

    @Test
    public void shouldExecuteActionGiven() {
        final AtomicBoolean called = new AtomicBoolean();

        final ClickableText clickableText = new ClickableText("Sample text", () -> called.set(true));

        withNodes(clickableText)
            .startWhen(showing(clickableText))
            .willDo(() -> clickOn(clickableText, PRIMARY))
            .andAwaitFor(called::get);

        assertThat(called).isTrue();
    }

}
