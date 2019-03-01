package moe.tristan.easyfxml.test;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
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

public abstract class FxNodeTest extends ApplicationTest {

    protected final FxNodeAsyncQuery withNodes(Node... nodes) {
        return FxNodeAsyncQuery.withNodes(nodes);
    }

    protected Supplier<Boolean> showing(Node node) {
        return () -> {
            final PointQuery pointQuery = point(node);
            return pointQuery.query() != null;
        };
    }

    @SuppressWarnings("unchecked")
    public static final class FxNodeAsyncQuery {

        private List<Node> nodes;

        private List<Supplier<Boolean>> nodesReady = emptyList();

        private List<Runnable> actions = emptyList();

        private List<Supplier<Boolean>> witnesses = emptyList();

        public FxNodeAsyncQuery(Node[] nodes) {
            this.nodes = List.of(nodes);
        }

        public static FxNodeAsyncQuery withNodes(Node... nodes) {
            return new FxNodeAsyncQuery(nodes);
        }

        public final FxNodeAsyncQuery startWhen(Supplier<Boolean>... readyCheck) {
            this.nodesReady = List.of(readyCheck);
            return this;
        }

        public final FxNodeAsyncQuery thenDo(Runnable... actionWhenReady) {
            this.actions = List.of(actionWhenReady);
            return this;
        }

        public final void andFinallyAwaitFor(Supplier<Boolean>... awaited) {
            this.witnesses = List.of(awaited);
            run();
        }

        public void run() {
            runTestQuery(nodes, nodesReady, actions, witnesses);
        }

        private void runTestQuery(List<Node> nodes, List<Supplier<Boolean>> nodesReady, List<Runnable> actions, List<Supplier<Boolean>> witnesses) {
            CompletableFuture
                .runAsync(() -> buildStageWithNodes(nodes), Platform::runLater)
                .thenRun(() -> await().atMost(5, SECONDS).until(() -> allEvaluateToTrue(nodesReady)))
                .thenRun(() -> actions.forEach(Runnable::run)).join();

            for (Supplier<Boolean> witness : witnesses) {
                await().atMost(5, SECONDS).until(witness::get);
            }
        }

        private static void buildStageWithNodes(List<Node> nodes) {
            Stage testStage = new Stage();
            testStage.setScene(new Scene(new VBox(nodes.toArray(new Node[0]))));
            testStage.show();
        }

        private static Boolean allEvaluateToTrue(List<Supplier<Boolean>> nodesReady) {
            return nodesReady.stream().map(Supplier::get).reduce((prev, cur) -> prev && cur).orElse(true);
        }

    }

}
