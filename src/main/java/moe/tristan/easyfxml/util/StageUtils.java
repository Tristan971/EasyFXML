package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public final class StageUtils {
    private static final Logger LOG = LoggerFactory.getLogger(StageUtils.class);

    public static CompletionStage<Stage> stageOf(final String title, final Pane rootPane) {
        final CompletableFuture<Stage> upcomingStage = new CompletableFuture<>();
        Platform.runLater(() -> {
            final Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(rootPane));
            upcomingStage.complete(stage);
        });
        return upcomingStage;
    }

    public static CompletionStage<Stage> scheduleDisplaying(final Stage stage) {
        LOG.debug(
            "Requested displaying of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return asyncStageOperation(stage, Stage::show);
    }

    public static CompletionStage<Stage> asyncStageOperation(final Stage stage, final Consumer<Stage> asyncOp) {
        return FxAsyncUtils.doOnFxThread(stage, asyncOp);
    }

    public static CompletionStage<Stage> scheduleHiding(final Stage stage) {
        LOG.debug(
            "Requested hiding of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return asyncStageOperation(stage, Stage::hide);
    }
}
