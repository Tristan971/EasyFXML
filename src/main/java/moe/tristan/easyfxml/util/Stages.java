package moe.tristan.easyfxml.util;

import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import moe.tristan.easyfxml.api.FxmlStylesheet;

import io.vavr.Tuple;

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

    public static CompletionStage<Stage> scheduleDisplaying(final CompletionStage<Stage> stageCreationResult) {
        return stageCreationResult.whenCompleteAsync((res, err) -> scheduleDisplaying(res));
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

    public static CompletionStage<Stage> scheduleHiding(final CompletionStage<Stage> stageCreationResult) {
        return stageCreationResult.whenCompleteAsync((res, err) -> scheduleHiding(res));
    }

    /**
     * Boilerplate for setting the stylesheet of a given stage via Java rather than FXML.
     *
     * @param stage      The stage whose stylesheet we are changing
     * @param stylesheet The new stylesheet in external form. That is, if it is a file, including the protocol info
     *                   "file:/" before the actual path. Use {@link Resources#getResourceURL(String)} and {@link
     *                   java.net.URL#toExternalForm()} if your css file is included in the classpath.
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
    public static CompletionStage<Stage> setStylesheet(final Stage stage, final String stylesheet) {
        LOG.info(
            "Setting stylesheet {} for stage {}({})",
            stylesheet,
            stage.toString(),
            stage.getTitle()
        );
        return FxAsync.doOnFxThread(
            stage,
            theStage -> {
                final Scene stageScene = theStage.getScene();
                stageScene.getStylesheets().clear();
                stageScene.getStylesheets().add(stylesheet);
            }
        );
    }

    public static CompletionStage<Stage> setStylesheet(final CompletionStage<Stage> stageCreationResult, final String stylesheet) {
        return stageCreationResult.whenCompleteAsync((res, err) -> setStylesheet(res, stylesheet));
    }

    /**
     * See {@link #setStylesheet(Stage, String)}
     *
     * @param stage      The stage to apply the style to
     * @param stylesheet The {@link FxmlStylesheet} pointing to the stylesheet to apply
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
    public static CompletionStage<Stage> setStylesheet(final Stage stage, final FxmlStylesheet stylesheet) {
        return setStylesheet(stage, stylesheet.getExternalForm());
    }

    public static CompletionStage<Stage> setStylesheet(final CompletionStage<Stage> stageCreationResult, final FxmlStylesheet stylesheet) {
        return setStylesheet(stageCreationResult, stylesheet.getExternalForm());
    }

}
