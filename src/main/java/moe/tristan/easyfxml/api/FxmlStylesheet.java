package moe.tristan.easyfxml.api;

import java.nio.file.Path;

import javafx.stage.Stage;
import moe.tristan.easyfxml.util.Stages;

/**
 * Represents a stylesheet a supplier of Path. What this means is that any protocol is acceptable.
 * Whether it is local file-based or remote URI-based.
 * <p>
 * See {@link Stages#setStylesheet(Stage, FxmlStylesheet)} for usage.
 * <p>
 * Previously strongly linked to {@link FxmlNode} but the html-like implementation of the JavaFX DOM makes it so
 * stylesheets are window-bound rather than component-bound.
 */
public interface FxmlStylesheet {

    /**
     * @return the CSS file that composes the stylesheet
     */
    Path getPath();
}
