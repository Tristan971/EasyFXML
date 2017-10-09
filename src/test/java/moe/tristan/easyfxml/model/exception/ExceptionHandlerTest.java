package moe.tristan.easyfxml.model.exception;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionHandlerTest extends ApplicationTest {

    private String EXCEPTION_TEXT;
    private String EXCEPTION_TEXT_READABLE;
    private Exception EXCEPTION;
    private Pane ERR_PANE;
    private Pane ERR_PANE_READBLE;

    @Override
    public void start(final Stage stage) {
        this.EXCEPTION_TEXT = "SAMPLE EXCEPTION";
        this.EXCEPTION_TEXT_READABLE = "USER READABLE ERROR";
        this.EXCEPTION = new Exception(this.EXCEPTION_TEXT);
        this.ERR_PANE = new ExceptionHandler(this.EXCEPTION).asPane();
        this.ERR_PANE_READBLE = new ExceptionHandler(this.EXCEPTION).asPane(this.EXCEPTION_TEXT_READABLE);
    }


    @Test
    public void asPane() {
        final Label errLabel = (Label) this.ERR_PANE.getChildren().filtered(node -> node instanceof Label).get(0);
        assertThat(errLabel.getText()).isEqualTo(this.EXCEPTION.getMessage());
    }

    @Test
    public void asPane_with_user_readable() {
        final Label errLabel = (Label) this.ERR_PANE_READBLE.getChildren().filtered(node -> node instanceof Label).get(0);
        assertThat(errLabel.getText()).isEqualTo(this.EXCEPTION_TEXT_READABLE);
    }

    @Test
    public void displayExceptionPane() {
        final CompletionStage<Stage> displayedStage = ExceptionHandler.displayExceptionPane(
            this.EXCEPTION_TEXT,
            this.EXCEPTION_TEXT_READABLE,
            this.EXCEPTION
        );

        displayedStage.thenAccept(stage -> assertThat(stage.isShowing()));
    }
}
