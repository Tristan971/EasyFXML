package moe.tristan.easyfxml.model.fxml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.Selector;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import moe.tristan.easyfxml.util.Stages;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static moe.tristan.easyfxml.TestUtils.isSpringSingleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

@ContextConfiguration(classes = {FxSpringContext.class, SAMPLE_CONTROL_CLASS.class})
@RunWith(SpringRunner.class)
public class BaseEasyFxmlTest extends ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEasyFxmlTest.class);

    private static final Object SELECTOR = new Object();

    @Autowired
    private ApplicationContext context;
    @Autowired
    private BaseEasyFxml easyFxml;
    @Autowired
    private ControllerManager controllerManager;

    @SuppressWarnings("EmptyMethod")
    @Override
    public void start(final Stage stage) {
        // initializes JavaFX Platform
    }

    @Test
    public void load_as_pane_single() {
        final Pane testPane = this.assertSuccessAndGet(this.easyFxml.loadNode(TEST_NODES.PANE).getNode());

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(TEST_NODES.PANE)
        );
    }

    @Test
    public void load_as_pane_multiple() {
        final Pane testPane = this.assertSuccessAndGet(this.easyFxml.loadNode(TEST_NODES.PANE, new Selector(SELECTOR))
                                                                    .getNode());

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getMultiple(TEST_NODES.PANE, new Selector(SELECTOR))
        );

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

    }

    @Test
    public void load_with_type_success() {
        final Pane testPane = this.assertSuccessAndGet(this.easyFxml.loadNode(
            TEST_NODES.PANE,
            Pane.class,
            FxmlController.class
        ).getNode());

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(TEST_NODES.PANE)
        );
    }

    @Test
    public void load_with_type_single_invalid_class_failure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.loadNode(TEST_NODES.BUTTON, Pane.class, FxmlController.class).getNode(),
            this.controllerManager.getSingle(TEST_NODES.BUTTON),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_single_invalid_file_failure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.loadNode(TEST_NODES.INVALID).getNode(),
            this.controllerManager.getSingle(TEST_NODES.INVALID),
            LoadException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_class_failure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.loadNode(TEST_NODES.BUTTON, Pane.class, NoControllerClass.class, new Selector(SELECTOR)).getNode(),
            this.controllerManager.getMultiple(TEST_NODES.BUTTON, new Selector(SELECTOR)),
            ClassCastException.class
        );
    }

    @Test
    public void load_with_type_multiple_invalid_file_failure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.loadNode(TEST_NODES.INVALID, new Selector(SELECTOR)).getNode(),
            this.controllerManager.getMultiple(TEST_NODES.INVALID, new Selector(SELECTOR)),
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

    /**
     * We have to sleep here because the event firing in JavaFX can't be waited on all the way. So if we don't wait, as
     * soon as the click is actually sent, but not yet registered, we are already asserting. The wait is a horrific
     * thing that the whole async life promised to save us from. But it did not deliver (yet).
     *
     * @param testPane         The pane to test bounding on
     * @param controllerLookup The controller as an {@link Option} so we can know if the test actually failed because of
     *                         some outside reason.
     */
    private void assertControllerBoundToTestPane(final Pane testPane, final Option<FxmlController> controllerLookup) {
        assertThat(controllerLookup.isDefined()).isTrue();
        assertThat(controllerLookup.get().getClass()).isEqualTo(SAMPLE_CONTROL_CLASS.class);

        Stages.stageOf("TEST_PANE", testPane)
              .whenCompleteAsync((stage, err) -> Stages.scheduleDisplaying(stage))
              .whenCompleteAsync((stage, err) -> {
                  final Button btn = (Button) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
                  btn.fire();
                  Stages.scheduleHiding(stage);
              })
              .whenCompleteAsync((stage, err) -> {
                  final SAMPLE_CONTROL_CLASS testController = (SAMPLE_CONTROL_CLASS) controllerLookup.get();
                  assertThat(testController.locatedInstance).isTrue();
              })
              .toCompletableFuture()
              .join();
    }

    private void assertPaneFailedLoadingAndDidNotRegister(
        final Supplier<Try<? extends Node>> failingLoadResultSupplier,
        final Option<FxmlController> controllerLookup,
        final Class<? extends Throwable> expectedExceptionClass
    ) {
        try {
            LOGGER.debug("Expecting load exception of type {}", expectedExceptionClass);
            final Try<? extends Node> failingLoadResult = failingLoadResultSupplier.get();
            assertThat(failingLoadResult.isFailure()).isTrue();
            assertThat(failingLoadResult.getCause()).isInstanceOf(expectedExceptionClass);
            assertThat(controllerLookup.isEmpty()).isTrue();
        } catch (Throwable err) {
            if (!err.getClass().equals(expectedExceptionClass)) {
                LOGGER.error("Expected exception of type {} but got {}", expectedExceptionClass, err.getClass());
                throw err;
            }
            LOGGER.debug("Caught exception of expected type. All good.");
        }
    }

    @Ignore("This is not a test class")
    public enum TEST_NODES implements FxmlNode {
        PANE(
            () -> "fxml/test_pane.fxml",
            SAMPLE_CONTROL_CLASS.class
        ),

        BUTTON(
            () -> "fxml/button.fxml",
            NoControllerClass.class
        ),

        INVALID(
            () -> "fxml/invalid_file.fxml",
            NoControllerClass.class
        );

        private final FxmlFile fxmlFile;
        private final Class<? extends FxmlController> controllerClass;

        TEST_NODES(final FxmlFile fxmlFile, final Class<? extends FxmlController> controllerClass) {
            this.fxmlFile = fxmlFile;
            this.controllerClass = controllerClass;
        }

        @Override
        public FxmlFile getFile() {
            return this.fxmlFile;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return this.controllerClass;
        }

    }
}
