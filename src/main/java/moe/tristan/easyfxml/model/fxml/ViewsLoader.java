package moe.tristan.easyfxml.model.fxml;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.exception.ExceptionPane;
import moe.tristan.easyfxml.model.exception.ExceptionPaneBehavior;
import moe.tristan.easyfxml.util.StageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * The {@link ViewsLoader} is a convenience {@link Service} acting as
 * a safe decorator around {@link EasyFxml} for error-handling.
 *
 * It provides :
 * - Error handling ({@link #loadingError(Throwable)}
 * - Error styles {@link ExceptionPaneBehavior}
 *
 * It is recommended to use it but {@link EasyFxml} works fine without it.
 */
@Component
public class ViewsLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ViewsLoader.class);

    private final ApplicationContext context;

    @Autowired
    public ViewsLoader(final ApplicationContext context) {
        this.context = context;
    }

    public Pane loadPaneForNode(final FxmlNode fxmlNode, final ExceptionPaneBehavior onExceptionBehavior) {
        LOG.debug("Loading view : {} [{}]", fxmlNode, fxmlNode.getFxmlFile().getPath());
        final EasyFxml easyFxml = this.context.getBean(EasyFxml.class);
        return easyFxml.loadNode(fxmlNode).getOrElseGet(exception -> {
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
        LOG.error("Got exception loading view ! See stacktrace : ", exception);
        return new ExceptionPane(exception).asPane("Could not load component :( Details :");
    }

    private void loadingErrorDialog(final Throwable exception) {
        final Pane errPane = this.loadingError(exception);
        final Stage errorStage = StageUtils.stageOf("An error has occured", errPane);
        StageUtils.scheduleDisplaying(errorStage);
    }
}
