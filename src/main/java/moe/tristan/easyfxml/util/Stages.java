package moe.tristan.easyfxml.util;

import java.net.URL;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vavr.Tuple;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import moe.tristan.easyfxml.api.FxmlStylesheet;

/**
 * Utility class to perform asynchronous operations on {@link Stage} instance on the JavaFX thread.
 */
public final class Stages {

    private static final Logger LOG = LoggerFactory.getLogger(Stages.class);

    private Stages() {
    }

    /**
     * Creates a Stage.
     *
     * @param title    The title of the stage
     * @param rootPane The scene's base
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous creation. It will
     * eventually contain the newly created stage.
     */
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

    /**
     * Schedules a stage for displaying.
     *
     * @param stage The stage to schedule displaying of.
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
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

    /**
     * Schedules a stage for hiding
     *
     * @param stage The stage to shcedule hiding of.
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
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

    /**
     * Allows to set the stylesheet of a whole stage.
     *
     * @param stage      The stage whose stylesheet we are changing
     * @param stylesheet The new stylesheet (must conform to {@link URL#URL(String)} and its derivatives).
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
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
