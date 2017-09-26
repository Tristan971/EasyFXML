package moe.tristan.easyfxml.model.exception;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.util.DomUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public final class ExceptionPane {
    private static final double ERROR_FIELD_MARGIN_SIZE = 20.0;
    private final Throwable exception;

    public ExceptionPane(final Throwable exception) {
        log.debug("Generating ExceptionPane for exception of type {}", exception.getClass());
        this.exception = exception;
    }

    public Pane asPane() {
        return this.asPane(this.exception.getMessage());
    }

    public Pane asPane(final String userReadableError) {
        log.debug("Generating node corresponding to ExceptionPane...");
        final Label messageLabel = new Label(userReadableError);
        final TextArea throwableDataLabel = new TextArea(formatErrorMessage(this.exception));

        DomUtils.centerNode(throwableDataLabel, ERROR_FIELD_MARGIN_SIZE);
        return new AnchorPane(messageLabel, throwableDataLabel);
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