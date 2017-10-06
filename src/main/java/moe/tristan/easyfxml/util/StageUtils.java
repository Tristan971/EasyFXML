package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public final class StageUtils {
    private static final Logger LOG = LoggerFactory.getLogger(StageUtils.class);

    private StageUtils() {
    }

    public static Stage stageOf(final String title, final Pane rootPane) {
        final Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(new Scene(rootPane));
        return stage;
    }

    public static Future<Stage> scheduleDisplaying(final Stage stage) {
        LOG.debug(
            "Requested displaying of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return asyncStageOperation(stage, Stage::show);
    }

    public static Future<Stage> asyncStageOperation(final Stage stage, final Consumer<Stage> asyncOp) {
        final CompletableFuture<Stage> onAsyncOpDone = new CompletableFuture<>();
        Platform.runLater(() -> {
            asyncOp.accept(stage);
            onAsyncOpDone.complete(stage);
        });
        return onAsyncOpDone;
    }

    public static Future<Stage> scheduleHiding(final Stage stage) {
        LOG.debug(
            "Requested hiding of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return asyncStageOperation(stage, Stage::hide);
    }
}
