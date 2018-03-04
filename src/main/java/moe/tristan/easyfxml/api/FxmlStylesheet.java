package moe.tristan.easyfxml.api;

import javafx.stage.Stage;
import moe.tristan.easyfxml.util.Resources;
import moe.tristan.easyfxml.util.Stages;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a stylesheet a supplier of Path. What this means is that any protocol is acceptable.
 * Whether it is local file-based or remote URI-based.
 * <p>
 * See {@link Stages#setStylesheet(Stage, FxmlStylesheet)} for usage.
 * <p>
 *
 * You can override one or each of these methods as they depend on each other and thus overriding at least one will make
 * the other (and thus both) valid.
 */
public interface FxmlStylesheet {

    /**
     * @return the CSS file that composes the stylesheet as a {@link Path}.
     * <p>
     * See {@link Resources#getResourcePath(String)}
     */
    default Path getPath() {
        return Paths.get(URI.create(getExternalForm()));
    }

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
