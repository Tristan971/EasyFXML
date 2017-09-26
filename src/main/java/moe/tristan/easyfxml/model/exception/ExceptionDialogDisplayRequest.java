package moe.tristan.easyfxml.model.exception;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class ExceptionDialogDisplayRequest implements Runnable {

    private final Stage stage;

    private ExceptionDialogDisplayRequest(final Stage stage) {
        this.stage = stage;
    }

    public static ExceptionDialogDisplayRequest of(final String title, final Pane pane) {
        final Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(new Scene(pane));
        return new ExceptionDialogDisplayRequest(stage);
    }

    @Override
    public void run() {
        this.stage.show();
    }
}
