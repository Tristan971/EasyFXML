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

package moe.tristan.easyfxml.junit;

import static java.util.Collections.emptyList;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.PointQuery;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class FxmlComponentTest extends ApplicationTest {

    protected final FxNodeAsyncQuery withNodes(Node... nodes) {
        return FxNodeAsyncQuery.withNodes(nodes);
    }

    protected Supplier<Boolean> showing(Node node) {
        return () -> {
            final PointQuery pointQuery = point(node);
            return pointQuery.query() != null;
        };
    }

    public static final class FxNodeAsyncQuery {

        private List<Node> nodes;

        private List<Supplier<Boolean>> nodesReady = emptyList();

        private List<Runnable> actions = emptyList();

        private List<Supplier<Boolean>> witnesses = emptyList();

        FxNodeAsyncQuery(Node[] nodes) {
            this.nodes = List.of(nodes);
        }

        public static FxNodeAsyncQuery withNodes(Node... nodes) {
            return new FxNodeAsyncQuery(nodes);
        }

        @SafeVarargs
        public final FxNodeAsyncQuery startWhen(Supplier<Boolean>... readyCheck) {
            this.nodesReady = List.of(readyCheck);
            return this;
        }

        public final FxNodeAsyncQuery willDo(Runnable... actionsWhenReady) {
            this.actions = List.of(actionsWhenReady);
            return this;
        }

        @SafeVarargs
        public final void andAwaitFor(Supplier<Boolean>... awaited) {
            this.witnesses = List.of(awaited);
            run();
        }

        public void run() {
            runTestQuery(nodes, nodesReady, actions, witnesses);
        }

        private void runTestQuery(List<Node> nodes, List<Supplier<Boolean>> nodesReady, List<Runnable> actions, List<Supplier<Boolean>> witnesses) {
            CompletableFuture
                .runAsync(() -> buildStageWithNodes(nodes), Platform::runLater)
                .thenRunAsync(() -> await().until(() -> allEvaluateToTrue(nodesReady)))
                .thenRunAsync(() -> actions.forEach(Runnable::run)).join();

            for (Supplier<Boolean> witness : witnesses) {
                await().until(witness::get);
            }
        }

        private static void buildStageWithNodes(List<Node> nodes) {
            Stage testStage = new Stage();
            testStage.setScene(new Scene(new VBox(nodes.toArray(new Node[0]))));
            testStage.show();
            testStage.toFront();
        }

        private static Boolean allEvaluateToTrue(List<Supplier<Boolean>> nodesReady) {
            return nodesReady.stream().map(Supplier::get).reduce((prev, cur) -> prev && cur).orElse(true);
        }

    }

}
