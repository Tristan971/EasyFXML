package moe.tristan.easyfxml.api;

import moe.tristan.easyfxml.util.Resources;
import moe.tristan.easyfxml.util.Stages;
import io.vavr.control.Try;

import javafx.stage.Stage;

import java.net.URI;
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
     * This method is a sample implementation that should work in almost all general cases.
     * An {@link java.io.IOException} can be thrown in very rare cases.
     *
     * If you encounter them, post an issue with system details.
     *
     * @return the CSS file in external form (i.e. with the file:/, http:/...) protocol info before it.
     * <p>
     *
     * @see Resources#getResourceURL(String)
     * @see URL#toExternalForm()
     */
    default String getExternalForm() {
        return Try.of(this::getPath)
            .map(Path::toUri)
            .mapTry(URI::toURL)
            .map(URL::toExternalForm)
            .get();
    }

}
