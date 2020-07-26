/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.model.fxml;

import static moe.tristan.easyfxml.TestUtils.isSpringSingleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.testfx.framework.junit5.Start;

import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.junit.SpringBootComponentTest;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.Selector;
import moe.tristan.easyfxml.samples.button.ButtonComponent;
import moe.tristan.easyfxml.samples.invalid.InvalidComponent;
import moe.tristan.easyfxml.samples.panewithbutton.PaneWithButtonComponent;
import moe.tristan.easyfxml.samples.panewithbutton.PaneWithButtonController;
import moe.tristan.easyfxml.util.Stages;

import io.vavr.control.Option;
import io.vavr.control.Try;

@SpringBootTest
public class DefaultEasyFxmlTest extends SpringBootComponentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEasyFxmlTest.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DefaultEasyFxml easyFxml;

    @Autowired
    private ControllerManager controllerManager;

    @Autowired
    private InvalidComponent invalidComponent;

    @Autowired
    private PaneWithButtonComponent paneWithButtonComponent;

    @Autowired
    private ButtonComponent buttonComponent;

    @Start
    public void start(Stage stage) {
        LOGGER.info("Start app with stage: {}", stage);
    }

    @Test
    public void loadAsPaneSingle() throws InterruptedException, ExecutionException, TimeoutException {
        Pane testPane = this.assertSuccessAndGet(this.easyFxml.load(paneWithButtonComponent).getNode());

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(paneWithButtonComponent)
        );
    }

    @Test
    public void loadAsPaneMultiple() throws InterruptedException, ExecutionException, TimeoutException {
        Object selector = new Object();

        Pane testPane = this.assertSuccessAndGet(this.easyFxml.load(paneWithButtonComponent, new Selector(selector)).getNode());

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getMultiple(paneWithButtonComponent, new Selector(selector))
        );

        assertThat(testPane.getChildren()).hasSize(1);
        assertThat(testPane.getChildren().get(0).getClass()).isEqualTo(Button.class);

    }

    @Test
    public void loadWithTypeSuccess() throws InterruptedException, ExecutionException, TimeoutException {
        Pane testPane = this.assertSuccessAndGet(this.easyFxml.load(
            paneWithButtonComponent,
            Pane.class,
            FxmlController.class
        ).getNode());

        this.assertControllerBoundToTestPane(
            testPane,
            this.controllerManager.getSingle(paneWithButtonComponent)
        );
    }

    @Test
    public void loadWithTypeSingleInvalidClassFailure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.load(buttonComponent, Pane.class, FxmlController.class).getNode(),
            this.controllerManager.getSingle(buttonComponent),
            ClassCastException.class
        );
    }

    @Test
    public void loadWithTypeSingleInvalidFileFailure() {
        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.load(invalidComponent).getNode(),
            this.controllerManager.getSingle(invalidComponent),
            LoadException.class
        );
    }

    @Test
    public void loadWithTypeMultipleInvalidClassFailure() {
        Object selector = new Object();

        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.load(buttonComponent, Pane.class, NoControllerClass.class, new Selector(selector)).getNode(),
            this.controllerManager.getMultiple(buttonComponent, new Selector(selector)),
            ClassCastException.class
        );
    }

    @Test
    public void loadWithTypeMultipleInvalidFileFailure() {
        Object selector = new Object();

        this.assertPaneFailedLoadingAndDidNotRegister(
            () -> this.easyFxml.load(invalidComponent, new Selector(selector)).getNode(),
            this.controllerManager.getMultiple(invalidComponent, new Selector(selector)),
            LoadException.class
        );
    }

    @Test
    public void canInstantiateControllerAsPrototype() {
        assertThat(isSpringSingleton(this.context, PaneWithButtonController.class)).isFalse();
    }

    private <T extends Node> T assertSuccessAndGet(Try<T> loadResult) {
        assertThat(loadResult.isSuccess()).isTrue();
        return loadResult.get();
    }

    /**
     * We have to sleep here because the event firing in JavaFX can't be waited on all the way. So if we don't wait, as soon as the click is actually sent, but
     * not yet registered, we are already asserting. The wait is a horrific thing that the whole async life promised to save us from. But it did not deliver
     * (yet).
     *
     * @param testPane         The pane to test bounding on
     * @param controllerLookup The controller as an {@link Option} so we can know if the test actually failed because of some outside reason.
     */
    private void assertControllerBoundToTestPane(Pane testPane, Option<FxmlController> controllerLookup)
    throws InterruptedException, ExecutionException, TimeoutException {
        assertThat(controllerLookup.isDefined()).isTrue();
        assertThat(controllerLookup.get().getClass()).isEqualTo(PaneWithButtonController.class);

        Stages.stageOf("TEST_PANE", testPane)
              .whenCompleteAsync((stage, err) -> Stages.scheduleDisplaying(stage))
              .whenCompleteAsync((stage, err) -> {
                  Button btn = (Button) stage.getScene().getRoot().getChildrenUnmodifiable().get(0);
                  btn.fire();
                  Stages.scheduleHiding(stage);
              })
              .whenCompleteAsync((stage, err) -> {
                  PaneWithButtonController testController = (PaneWithButtonController) controllerLookup.get();
                  assertThat(testController.locatedInstance).isTrue();
              })
              .toCompletableFuture()
              .get(5, TimeUnit.SECONDS);
    }

    private void assertPaneFailedLoadingAndDidNotRegister(
        Supplier<Try<? extends Node>> failingLoadResultSupplier,
        Option<FxmlController> controllerLookup,
        Class<? extends Throwable> expectedFailureCauseClass
    ) {
        assertThatThrownBy(failingLoadResultSupplier::get)
            .isInstanceOf(FxmlComponentLoadException.class)
            .hasCauseInstanceOf(expectedFailureCauseClass);
        assertThat(controllerLookup.isEmpty()).isTrue();
    }

}
