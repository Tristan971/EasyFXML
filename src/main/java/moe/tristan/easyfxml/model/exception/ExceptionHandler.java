package moe.tristan.easyfxml.model.exception;

import moe.tristan.easyfxml.util.Nodes;
import moe.tristan.easyfxml.util.Stages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Utility class to quickly turn an exception into a readable error pop-up.
 */
public final class ExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final double ERROR_FIELD_MARGIN_SIZE = 20.0;

    private final Throwable exception;

    /**
     * Creates an instance with the given exception.
     *
     * @param exception The exception to base this instance on
     */
    public ExceptionHandler(final Throwable exception) {
        LOG.debug("Generating ExceptionPane for exception of type {}", exception.getClass());
        this.exception = exception;
    }

    /**
     * @return The exception in a pane with {@link Throwable#getMessage()} as a label over the stacktrace.
     */
    public Pane asPane() {
        return this.asPane(this.exception.getMessage());
    }

    /**
     * Same as {@link #asPane()} but with a custom error label on top of the stack trace.
     *
     * @param userReadableError The custom error message.
     *
     * @return The exception in a pane with the custom error message as a label over the stacktrace.
     */
    public Pane asPane(final String userReadableError) {
        LOG.debug("Generating node corresponding to ExceptionPane...");
        final Label messageLabel = new Label(userReadableError);
        final TextArea throwableDataLabel = new TextArea(formatErrorMessage(this.exception));

        AnchorPane.setLeftAnchor(messageLabel, ERROR_FIELD_MARGIN_SIZE);
        Nodes.centerNode(throwableDataLabel, ERROR_FIELD_MARGIN_SIZE);
        return new AnchorPane(messageLabel, throwableDataLabel);
    }

    public static Pane fromThrowable(final Throwable throwable) {
        return new ExceptionHandler(throwable).asPane();
    }

    /**
     * Creates a pop-up and displays it based on a given exception, pop-up title and custom error message.
     *
     * @param title     The title of the error pop-up
     * @param readable  The custom label to display on top of the stack trace
     * @param exception The exception to use
     *
     * @return a {@link CompletionStage} to know when the pop-up displayed and have a handle on it.
     */
    public static CompletionStage<Stage> displayExceptionPane(
        final String title,
        final String readable,
        final Throwable exception
    ) {
        final Pane exceptionPane = new ExceptionHandler(exception).asPane(readable);
        final CompletionStage<Stage> exceptionStage = Stages.stageOf(title, exceptionPane);
        return exceptionStage.thenCompose(Stages::scheduleDisplaying);
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
