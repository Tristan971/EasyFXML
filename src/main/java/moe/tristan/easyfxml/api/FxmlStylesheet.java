package moe.tristan.easyfxml.api;

import moe.tristan.easyfxml.util.Resources;
import moe.tristan.easyfxml.util.Stages;

import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Represents a stylesheet a supplier of Path. What this means is that any protocol is acceptable. Whether it is local
 * file-based or remote URI-based.
 * <p>
 *
 * @see Stages#setStylesheet(Stage, FxmlStylesheet)
 */
public interface FxmlStylesheet {

    /**
     * @return the CSS file that composes the stylesheet as a {@link Path}.
     * <p>
     * See {@link Resources#getResourcePath(String)}
     */
    Path getPath();

    /**
     * @return the CSS file in external form (i.e. with the file:/, http:/...) protocol info before it.
     * <p>
     * See {@link Resources#getResourceURL(String)} and {@link URL#toExternalForm()}
     */
    default String getExternalForm() {
        try {
            return getPath().toUri().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
