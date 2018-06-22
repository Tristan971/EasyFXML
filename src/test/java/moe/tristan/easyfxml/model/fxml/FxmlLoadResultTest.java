package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlController;
import io.vavr.control.Try;
import org.junit.Before;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

import static org.testfx.assertions.api.Assertions.assertThat;

public class FxmlLoadResultTest {

    private static final Node TEST_NODE = new Pane();
    private static final FxmlController TEST_CONTROLLER = () -> Paths.get("fakepath");

    private FxmlLoadResult<Node, FxmlController> fxmlLoadResult;

    @Before
    public void setUp() {
        fxmlLoadResult = new FxmlLoadResult<>(
            Try.of(() -> TEST_NODE),
            Try.of(() -> TEST_CONTROLLER)
        );
    }

    @Test
    public void getNode() {
        assertThat(fxmlLoadResult.getNode().get()).isEqualTo(TEST_NODE);
    }

    @Test
    public void getController() {
        assertThat(fxmlLoadResult.getController().get()).isEqualTo(TEST_CONTROLLER);
    }

    @Test
    public void arity() {
        assertThat(fxmlLoadResult.arity()).isEqualTo(2);
    }

    @Test
    public void toSeq() {
        assertThat(
            fxmlLoadResult.toSeq()
                          .map(Try.class::cast)
                          .map(Try::get)
                          .toJavaList()
        ).containsExactlyInAnyOrder(TEST_NODE, TEST_CONTROLLER);
    }
}
