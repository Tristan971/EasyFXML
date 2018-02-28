package moe.tristan.easyfxml.util;

import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.Tuple;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import moe.tristan.easyfxml.api.FxmlStylesheet;

public final class Stages {

    private static final Logger LOG = LoggerFactory.getLogger(Stages.class);

    private Stages() {}

    public static CompletionStage<Stage> stageOf(final String title, final Pane rootPane) {
        return FxAsync.computeOnFxThread(
            Tuple.of(title, rootPane),
            titleAndPane -> {
                final Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle(title);
                stage.setScene(new Scene(rootPane));
                return stage;
            }
        );
    }

    public static CompletionStage<Stage> scheduleDisplaying(final Stage stage) {
        LOG.debug(
            "Requested displaying of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return FxAsync.doOnFxThread(
            stage,
            Stage::show
        );
    }

    public static CompletionStage<Stage> scheduleHiding(final Stage stage) {
        LOG.debug(
            "Requested hiding of stage {} with title : \"{}\"",
            stage,
            stage.getTitle()
        );
        return FxAsync.doOnFxThread(
            stage,
            Stage::hide
        );
    }

    public static CompletionStage<Stage> setStylesheet(final Stage stage, final FxmlStylesheet stylesheet) {
        LOG.info(
            "Setting stylesheet {} for stage {}({})",
            stylesheet.getPath().toAbsolutePath().toString(),
            stage.toString(),
            stage.getTitle()
        );
        return FxAsync.doOnFxThread(
            stage,
            theStage -> {
                final Scene stageScene = theStage.getScene();
                stageScene.getStylesheets().clear();
                stageScene.getStylesheets().add(stylesheet.getPath().toString());
            }
        );
    }
}
