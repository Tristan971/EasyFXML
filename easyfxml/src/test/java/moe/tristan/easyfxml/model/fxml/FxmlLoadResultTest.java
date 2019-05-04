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

import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.api.FxmlController;

import io.vavr.control.Try;

public class FxmlLoadResultTest extends ApplicationTest {

    private static final Node TEST_NODE = new Pane();
    private static final FxmlController TEST_CONTROLLER = () -> {
        throw new RuntimeException("Force failure on initialize call.");
    };

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
    public void orExceptionPane() {
        final FxmlLoadResult<Node, FxmlController> loadResult = new FxmlLoadResult<>(
            Try.failure(new RuntimeException("TEST")),
            Try.failure(new RuntimeException("TEST"))
        );

        assertThat(loadResult.orExceptionPane().isSuccess()).isTrue();
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

    @Test
    public void testPostProcessWithSuccessfulLoad() {
        final String propToSet = "testPostProcessWithSuccessfulLoad";
        final Object expectedPropValue = new Object();

        final FxmlLoadResult<Node, FxmlController> stillSuccessful = fxmlLoadResult.afterNodeLoaded(
            node -> node.getProperties().put(propToSet, expectedPropValue)
        );
        assertThat(stillSuccessful.getNode().isSuccess()).isTrue();
        assertThat(TEST_NODE.getProperties().getOrDefault(propToSet, null)).isEqualTo(expectedPropValue);

        final FxmlLoadResult<Node, FxmlController> shouldBeFailure = stillSuccessful.afterControllerLoaded(
            FxmlController::initialize
        );
        assertThat(shouldBeFailure.getController().isFailure()).isTrue();

        final AtomicBoolean didCallPostProcess = new AtomicBoolean(false);
        Assertions.assertThatThrownBy(() -> shouldBeFailure.afterControllerLoaded(ctrl -> didCallPostProcess.set(true)))
                  .isInstanceOf(IllegalStateException.class)
                  .hasCauseInstanceOf(RuntimeException.class);
        assertThat(didCallPostProcess).isFalse();
    }

    @Test
    public void testPostProcessWithFailingLoad() {
        final FxmlLoadResult<Node, FxmlController> failedLoad = new FxmlLoadResult<>(
            Try.failure(new RuntimeException()),
            Try.failure(new RuntimeException())
        );

        Assertions.assertThatThrownBy(() -> failedLoad.afterControllerLoaded(FxmlController::initialize))
                  .isInstanceOf(IllegalStateException.class)
                  .hasCauseInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> failedLoad.afterNodeLoaded(Node::getProperties))
                  .isInstanceOf(IllegalStateException.class)
                  .hasCauseInstanceOf(RuntimeException.class);
    }

}
