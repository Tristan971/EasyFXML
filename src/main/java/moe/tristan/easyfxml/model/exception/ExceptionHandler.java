package moe.tristan.easyfxml.model.exception;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.util.DomUtils;
import moe.tristan.easyfxml.util.StageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public final class ExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    private static final double ERROR_FIELD_MARGIN_SIZE = 20.0;
    private final Throwable exception;

    public ExceptionHandler(final Throwable exception) {
        LOG.debug("Generating ExceptionPane for exception of type {}", exception.getClass());
        this.exception = exception;
    }

    public Pane asPane() {
        return this.asPane(this.exception.getMessage());
    }

    public Pane asPane(final String userReadableError) {
        LOG.debug("Generating node corresponding to ExceptionPane...");
        final Label messageLabel = new Label(userReadableError);
        final TextArea throwableDataLabel = new TextArea(formatErrorMessage(this.exception));

        AnchorPane.setLeftAnchor(messageLabel, ERROR_FIELD_MARGIN_SIZE);
        DomUtils.centerNode(throwableDataLabel, ERROR_FIELD_MARGIN_SIZE);
        return new AnchorPane(messageLabel, throwableDataLabel);
    }

    public static CompletionStage<Stage> displayExceptionPane(
        final String title,
        final String readable,
        final Throwable exception
    ) {
        final Pane exceptionPane = new ExceptionHandler(exception).asPane(readable);
        final CompletionStage<Stage> exceptionStage = StageUtils.stageOf(title, exceptionPane);
        return exceptionStage.thenCompose(StageUtils::scheduleDisplaying);
    }

    private static String formatErrorMessage(final Throwable throwable) {
        return "Message : \n" +
            throwable.getMessage() +
            "\nStackTrace:\n" +
            Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }
}
