package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Option;
import io.vavr.control.Try;
import javafx.application.Platform;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.model.FxmlController;
import moe.tristan.easyfxml.model.FxmlFile;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.FxmlStylesheet;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.spring.SpringContext;
import moe.tristan.easyfxml.util.StageUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
    public void load_as_pane_single() {
        final Pane testPane = this.assertSuccessAndGet(this.easyFxml.loadNode(FXMLNODES.PANE));
        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

        this.assertControllerBoundToTestPane(testPane, this.controllerManager.getSingle(FXMLNODES.PANE));
    }

    @Test
    public void load_as_pane_multiple() {
        final Pane testPane = this.assertSuccessAndGet(
            this.easyFxml.loadNode(FXMLNODES.PANE, SELECTOR)
        );

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getMultiple(FXMLNODES.PANE, SELECTOR)
        );

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

    }

    @Test
    public void load_with_type_success() {
        final Pane testPane = this.assertSuccessAndGet(
            this.easyFxml.loadNode(FXMLNODES.PANE, Pane.class)
        );

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(FXMLNODES.PANE)
        );
    }

    @Test
    public void load_with_type_single_invalid_class_failure() {
        final Try<Pane> testPane = this.easyFxml.loadNode(FXMLNODES.BUTTON, Pane.class);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPane,
            this.controllerManager.getSingle(FXMLNODES.BUTTON),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_single_invalid_file_failure() {
        final Try<? extends Node> failingLoadResult = this.easyFxml.loadNode(FXMLNODES.INVALID);

        this.assertPaneFailedLoadingAndDidNotRegister(
            failingLoadResult,
            this.controllerManager.getSingle(FXMLNODES.INVALID),
            LoadException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_class_failure() {
        final Try<Pane> testPane = this.easyFxml.loadNode(FXMLNODES.BUTTON, Pane.class, SELECTOR);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPane,
            this.controllerManager.getMultiple(FXMLNODES.BUTTON, SELECTOR),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_file_failure() {
        final Try<Pane> testPaneLoadResult = this.easyFxml.loadNode(FXMLNODES.INVALID, SELECTOR);

        this.assertPaneFailedLoadingAndDidNotRegister(
            testPaneLoadResult,
            this.controllerManager.getMultiple(FXMLNODES.INVALID, SELECTOR),
            LoadException.class
        );
    }

    @Test
    public void can_instantiate_controller_as_prototype() {
        final SAMPLE_CONTROL_CLASS inst1 = this.context.getBean(SAMPLE_CONTROL_CLASS.class);
        final SAMPLE_CONTROL_CLASS inst2 = this.context.getBean(SAMPLE_CONTROL_CLASS.class);
        assertThat(Arrays.asList(inst1, inst2)).doesNotContainNull();
        assertThat(inst1).isNotEqualTo(inst2);
    }

    private <T extends Node> T assertSuccessAndGet(final Try<T> loadResult) {
        assertThat(loadResult.isSuccess()).isTrue();
        return loadResult.get();
    }

    private void assertControllerBoundToTestPane(
        final Pane testPane,
        final Option<FxmlController> controllerLookup
    ) {
        assertThat(controllerLookup.isDefined()).isTrue();
        assertThat(controllerLookup.get().getClass()).isEqualTo(SAMPLE_CONTROL_CLASS.class);

        StageUtils.stageOf("TEST_PANE", testPane)
            .thenCompose(StageUtils::scheduleDisplaying)
            .thenCompose(stage -> {
                final Button btn = (Button) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
                return this.clickOnNode(stage, btn);
            })
            .thenRun(() -> {
                final SAMPLE_CONTROL_CLASS testController = (SAMPLE_CONTROL_CLASS) controllerLookup.get();
                assertThat(testController.hasBeenClicked).isTrue();
            });
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
    private enum FXMLNODES implements FxmlNode {
        PANE(
            () -> "fxml/test_pane.fxml",
            Option.of(SAMPLE_CONTROL_CLASS.class),
            Option.none()
        ),

        BUTTON(
            () -> "fxml/button.fxml",
            Option.none(),
            Option.of(() -> "fxml/test_style.css")
        ),

        INVALID(
            () -> "fxml/invalid_file.fxml",
            Option.none(),
            Option.none()
        );

        private final FxmlFile fxmlFile;
        private final Option<Class<? extends FxmlController>> controllerClass;

        private final Option<FxmlStylesheet> stylesheet;

        FXMLNODES(final FxmlFile fxmlFile, final Option<Class<? extends FxmlController>> controllerClass, final Option<FxmlStylesheet> stylesheet) {
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
    }
}
