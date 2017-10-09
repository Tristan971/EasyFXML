package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Option;
import io.vavr.control.Try;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = { SpringContext.class, SAMPLE_CONTROL_CLASS.class })
@RunWith(SpringRunner.class)
public class BaseEasyFxmlTest extends ApplicationTest {
    private enum FXMLNODES implements FxmlNode {
        PANE(() -> "fxml/test_pane.fxml", Option.of(SAMPLE_CONTROL_CLASS.class), Option.none()),
        BUTTON(() -> "fxml/button.fxml", Option.none(), Option.none()),
        INVALID(() -> "fxml/invalid_file.fxml", Option.none(), Option.none());

        private final FxmlFile fxmlFile;
        private final Option<Class<? extends FxmlController>> controllerClass;

        private final Option<FxmlStylesheet> stylesheet;

        FXMLNODES(FxmlFile fxmlFile, Option<Class<? extends FxmlController>> controllerClass, Option<FxmlStylesheet> stylesheet) {
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

    private static final int WAITING_FOR_UI_TO_LOAD = 4000;
    private static final Object SELECTOR = new Object();

    @Autowired
    private ApplicationContext context;
    @Autowired
    private BaseEasyFxml easyFxml;
    @Autowired
    private ControllerManager controllerManager;

    @Override
    public void start(Stage stage) {

    }

    @Test
    public void loadNode_as_pane_single_success() throws InterruptedException {
        final Pane testPane = this.assertTestPaneLoadedCorrectly(
            () -> this.easyFxml.loadNode(FXMLNODES.PANE)
        );

        this.assertTestPaneHasCorrectControllerBinding(
            testPane,
            () -> this.controllerManager.getSingle(FXMLNODES.PANE)
        );
    }

    @Test
    public void loadNode_as_pane_single_failure() {
        final Try<Pane> testPaneLoadResult = this.easyFxml.loadNode(FXMLNODES.INVALID);
        assertThat(testPaneLoadResult.isFailure());
        assertThat(this.controllerManager.getSingle(FXMLNODES.INVALID).isEmpty());
    }

    @Test
    public void loadNode_as_pane_multiple_success() throws InterruptedException {
        final Pane testPane = this.assertTestPaneLoadedCorrectly(
            () -> this.easyFxml.loadNode(FXMLNODES.PANE, SELECTOR)
        );

        this.assertTestPaneHasCorrectControllerBinding(
            testPane,
            () -> this.controllerManager.getMultiple(FXMLNODES.PANE, SELECTOR)
        );
    }

    @Test
    public void loadNode_as_pane_multiple_failure() {
        final Try<Pane> testPaneLoadResult = this.easyFxml.loadNode(FXMLNODES.INVALID, SELECTOR);
        assertThat(testPaneLoadResult.isSuccess()).isFalse();
        assertThat(this.controllerManager.getMultiple(FXMLNODES.INVALID, SELECTOR).isEmpty());
    }

    @Test
    public void loadNode_with_type_single() {
    }

    @Test
    public void loadNode_with_type_multiple() {
    }

    @Test
    public void loadNode_by_manual_path() {
    }

    @Test
    public void can_instantiate_test_class() {
        final SAMPLE_CONTROL_CLASS bean = this.context.getBean(SAMPLE_CONTROL_CLASS.class);
        assertThat(bean).isNotNull();
    }

    private Pane assertTestPaneLoadedCorrectly(Supplier<Try<Pane>> paneLoadingSupplier) {
        final Try<Pane> testPaneLoadResult = paneLoadingSupplier.get();
        assertThat(testPaneLoadResult.isSuccess());

        final Pane testPane = testPaneLoadResult.get();
        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

        return testPane;
    }

    private void assertTestPaneHasCorrectControllerBinding(
        final Pane testPane,
        final Supplier<Option<FxmlController>> controllerLoadingSupplier
    ) throws InterruptedException {
        final Option<FxmlController> controller = controllerLoadingSupplier.get();
        assertThat(controller.isDefined());
        assertThat(controller.get().getClass()).isEqualTo(SAMPLE_CONTROL_CLASS.class);

        StageUtils.stageOf("TEST_PANE", testPane)
            .thenCompose(StageUtils::scheduleDisplaying)
            .thenAccept(stage -> {
                final Button btn = (Button) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
                this.clickOn(btn, MouseButton.PRIMARY);
            });
        Thread.sleep(WAITING_FOR_UI_TO_LOAD); // magic number, depends on graphical performance of your system
        final SAMPLE_CONTROL_CLASS testController = (SAMPLE_CONTROL_CLASS) controller.get();
        System.out.println("<CLICK EXPECTED on instance : "+testController.toString()+" >");
        assertThat(testController.hasBeenClicked);
        System.out.println("VALID");
    }

}
