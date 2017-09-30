package moe.tristan.easyfxml.model.fxml;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.FxmlFile;
import moe.tristan.easyfxml.model.exception.ExceptionDialogDisplayRequest;
import moe.tristan.easyfxml.model.exception.ExceptionPaneBehavior;
import moe.tristan.easyfxml.model.exception.ExceptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * The {@link ViewsLoader} is a convenience {@link Service} acting as
 * a safe decorator around {@link EasyFxml} for broadly used tasks such as
 * directly opening a window which's base FXML file has a {@link FxmlFile}
 * instance counterpart.
 *
 * It provides :
 * - Error handling ({@link #loadingError(Throwable)}
 * - Pop-up exception handling
 *
 * It is recommended to use it but {@link EasyFxml} works fine without it.
 */
@Component
@Slf4j
public class ViewsLoader {

    private final ApplicationContext context;

    @Autowired
    public ViewsLoader(final ApplicationContext context) {
        this.context = context;
    }

    public Pane loadPaneForView(final FxmlFile fxmlFile, final ExceptionPaneBehavior onExceptionBehavior) {
        log.debug("Loading view : {} [{}]", fxmlFile, fxmlFile.getPath());
        final EasyFxml easyFxml = this.context.getBean(EasyFxml.class);
        return easyFxml.getPaneForView(fxmlFile).getOrElseGet(exception -> {
            switch (onExceptionBehavior) {
                case INLINE:
                    return this.loadingError(exception);
                case DIALOG:
                    this.loadingErrorDialog(exception);
                    return new Pane(new Label("An exception occured. See dialog and/or logs."));
                default:
                    throw new RuntimeException("Error behavior was not recognized. Was \""+ onExceptionBehavior +"\".");
            }
        });
    }

    private Pane loadingError(final Throwable exception) {
        log.error("Got exception loading view ! See stacktrace : ", exception);
        return new ExceptionPane(exception).asPane("Could not load component :( Details :");
    }

    private void loadingErrorDialog(final Throwable exception) {
        final Pane errPane = this.loadingError(exception);
        Platform.runLater(ExceptionDialogDisplayRequest.of("An error has occured.", errPane));
    }
}
