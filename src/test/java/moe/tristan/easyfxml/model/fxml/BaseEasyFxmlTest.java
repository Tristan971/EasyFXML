package moe.tristan.easyfxml.model.fxml;

import io.vavr.CheckedFunction1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import javafx.application.Platform;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.spring.SpringContext;
import moe.tristan.easyfxml.util.PathUtils;
import moe.tristan.easyfxml.util.StageUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static io.vavr.API.unchecked;
import static moe.tristan.easyfxml.TestUtils.isSpringSingleton;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = { SpringContext.class, SAMPLE_CONTROL_CLASS.class })
@RunWith(SpringRunner.class)
public class BaseEasyFxmlTest extends ApplicationTest {

    private static final Object SELECTOR = new Object();

    @Autowired
    private ApplicationContext context;
    @Autowired
    private BaseEasyFxml easyFxml;
    @Autowired
    private ControllerManager controllerManager;

    @Override
    public void start(final Stage stage) {
        // initializes JavaFX Platform
    }

    @Test
    public void load_as_pane_single() throws ExecutionException, InterruptedException {
        final Pane testPane = this.assertSuccessAndGet(this.easyFxml.loadNode(TEST_NODES.PANE));

        this.assertAppliedStyle(testPane, TEST_NODES.PANE);

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

        this.assertControllerBoundToTestPane(testPane, this.controllerManager.getSingle(TEST_NODES.PANE));
    }

    @Test
    public void load_as_pane_multiple() throws ExecutionException, InterruptedException {
        final Pane testPane = this.assertSuccessAndGet(
            this.easyFxml.loadNode(TEST_NODES.PANE, SELECTOR)
        );

        this.assertAppliedStyle(testPane, TEST_NODES.PANE);

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getMultiple(TEST_NODES.PANE, SELECTOR)
        );

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

    }

    @Test
    public void load_with_type_success() throws ExecutionException, InterruptedException {
        final Pane testPane = this.assertSuccessAndGet(
            this.easyFxml.loadNode(TEST_NODES.PANE, Pane.class)
        );

        this.assertAppliedStyle(testPane, TEST_NODES.PANE);

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(TEST_NODES.PANE)
        );
    }

    @Test
    public void load_with_stylesheet() {
        final Button testButton = this.assertSuccessAndGet(
            this.easyFxml.loadNode(TEST_NODES.BUTTON, Button.class)
        );

        this.assertAppliedStyle(testButton, TEST_NODES.BUTTON);
    }

    @Test
    public void load_with_type_single_invalid_class_failure() {
        final Try<Pane> testPane = this.easyFxml.loadNode(TEST_NODES.BUTTON, Pane.class);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPane,
            this.controllerManager.getSingle(TEST_NODES.BUTTON),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_single_invalid_file_failure() {
        final Try<? extends Node> failingLoadResult = this.easyFxml.loadNode(TEST_NODES.INVALID);

        this.assertPaneFailedLoadingAndDidNotRegister(
            failingLoadResult,
            this.controllerManager.getSingle(TEST_NODES.INVALID),
            LoadException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_class_failure() {
        final Try<Pane> testPane = this.easyFxml.loadNode(TEST_NODES.BUTTON, Pane.class, SELECTOR);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPane,
            this.controllerManager.getMultiple(TEST_NODES.BUTTON, SELECTOR),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_file_failure() {
        final Try<Pane> testPaneLoadResult = this.easyFxml.loadNode(TEST_NODES.INVALID, SELECTOR);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPaneLoadResult,
            this.controllerManager.getMultiple(TEST_NODES.INVALID, SELECTOR),
            LoadException.class
        );
    }

    @Test
    public void can_instantiate_controller_as_prototype() {
        assertThat(isSpringSingleton(this.context, SAMPLE_CONTROL_CLASS.class)).isFalse();
    }

    private <T extends Node> T assertSuccessAndGet(final Try<T> loadResult) {
        assertThat(loadResult.isSuccess()).isTrue();
        return loadResult.get();
    }

    private <T extends Node> void assertAppliedStyle(final T loadedElement, final FxmlNode expectedData) {
        final String expectedStyle = expectedData.getStylesheet().isDefined() ?
            expectedData.getStylesheet().get().getStyle() :
            "";

        assertThat(loadedElement.getStyle()).isEqualToIgnoringWhitespace(expectedStyle);
    }

    /**
     * We have to sleep here because the event firing in JavaFX can't be waited on all the way.
     * So if we don't wait, as soon as the click is actually sent, but not yet registered,
     * we are already asserting.
     * The wait is a horrific thing that the whole async life promised to save us from. But it did
     * not deliver (yet).
     * @param testPane The pane to test bounding on
     * @param controllerLookup The controller as an {@link Option} so we can know if the test
     *                         actually failed because of some outside reason.
     * @throws ExecutionException If thread dies, or something.
     * @throws InterruptedException same as the ExecutionException.
     */
    private void assertControllerBoundToTestPane(
        final Pane testPane,
        final Option<FxmlController> controllerLookup
    ) throws ExecutionException, InterruptedException {
        assertThat(controllerLookup.isDefined()).isTrue();
        assertThat(controllerLookup.get().getClass()).isEqualTo(SAMPLE_CONTROL_CLASS.class);

        StageUtils.stageOf("TEST_PANE", testPane)
            .whenCompleteAsync((stage, err) -> StageUtils.scheduleDisplaying(stage))
            .thenCompose(stage -> {
                final Button btn = (Button) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
                return this.clickOnNode(stage, btn);
            })
            .whenCompleteAsync((stage, err) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                final SAMPLE_CONTROL_CLASS testController = (SAMPLE_CONTROL_CLASS) controllerLookup.get();
                assertThat(testController.hasBeenClicked).isTrue();
            })
            .toCompletableFuture().get();
    }

    private void assertPaneFailedLoadingAndDidNotRegister(
        final Try<? extends Node> failingLoadResult,
        final Option<FxmlController> controllerLookup,
        final Class<? extends Throwable> expectedExceptionClass
    ) {
        assertThat(failingLoadResult.isFailure()).isTrue();
        assertThat(failingLoadResult.getCause()).isInstanceOf(expectedExceptionClass);
        assertThat(controllerLookup.isEmpty()).isTrue();
    }

    private CompletionStage<Stage> clickOnNode(final Stage stage, final Node node) {
        final CompletableFuture<Stage> clickRequest = new CompletableFuture<>();
        Platform.runLater(() -> {
            this.clickOn(node, MouseButton.PRIMARY);
            clickRequest.complete(stage);
        });
        return clickRequest;
    }

    @Ignore("This is not a test class")
    private enum TEST_NODES implements FxmlNode {
        PANE(
            () -> "fxml/test_pane.fxml",
            Option.of(SAMPLE_CONTROL_CLASS.class),
            Option.none()
        ),

        BUTTON(
            () -> "fxml/button.fxml",
            Option.none(),
            Option.of(() -> loadStyleFromFile("fxml/test_style.css"))
        ),

        INVALID(
            () -> "fxml/invalid_file.fxml",
            Option.none(),
            Option.none()
        );

        private final FxmlFile fxmlFile;
        private final Option<Class<? extends FxmlController>> controllerClass;

        private final Option<FxmlStylesheet> stylesheet;

        TEST_NODES(final FxmlFile fxmlFile, final Option<Class<? extends FxmlController>> controllerClass, final Option<FxmlStylesheet> stylesheet) {
            this.fxmlFile = fxmlFile;
            this.controllerClass = controllerClass;
            this.stylesheet = stylesheet;
        }

        @Override
        public FxmlFile getFxmlFile() {
            return this.fxmlFile;
        }

        @Override
        public Option<Class<? extends FxmlController>> getControllerClass() {
            return this.controllerClass;
        }

        @Override
        public Option<FxmlStylesheet> getStylesheet() {
            return this.stylesheet;
        }

        private static String loadStyleFromFile(final String path) {
            return PathUtils.getPathForResource(path)
                .map(unchecked((CheckedFunction1<Path, byte[]>) Files::readAllBytes))
                .map(String::new)
                .get();
        }
    }
}
