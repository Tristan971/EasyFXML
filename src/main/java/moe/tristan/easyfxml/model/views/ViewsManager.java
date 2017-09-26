package moe.tristan.easyfxml.model.views;

import javafx.scene.layout.Pane;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.FxmlFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * The {@link ViewsManager} is a convenience {@link Service} acting as
 * a safe decorator around {@link EasyFxml} for broadly used tasks such as
 * directly opening a window which's base FXML file has a {@link FxmlFile}
 * instance counterpart.
 *
 * It is recommended to use it but {@link EasyFxml} works fine without it.
 */
@Lazy
@Service
public class ViewsManager {

    private final ApplicationContext context;

    @Autowired
    public ViewsManager(final ApplicationContext context) {
        this.context = context;
    }

    public Pane loadPaneForView(final FxmlFile fxmlFile) {
        final EasyFxml easyFxml = this.context.getBean(EasyFxml.class);
        return easyFxml.getPaneForView(fxmlFile).getOrElseGet(this::loadingError);
    }

    private Pane loadingError(final Throwable exception) {
        return null;
    }
}
