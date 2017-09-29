package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StageUtils {

    private StageUtils() {}

    public static Stage stageOf(final String title, final Pane rootPane) {
        final Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(new Scene(rootPane));
        return stage;
    }

    public static void scheduleDisplaying(final Stage stage) {
        log.debug(
                "Requested displaying of stage {} with title : \"{}\"",
                stage,
                stage.getTitle()
        );
        Platform.runLater(stage::show);
    }
}
