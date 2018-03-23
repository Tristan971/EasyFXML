package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import io.vavr.control.Try;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * This class contains some utility methods to open URLs with the default web browser of the user.
 */
@Component
public class BrowserSupport {

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     *
     * @param url The URL as a String. Must conform to {@link URL#URL(String)}.
     *
     * @return a {@link Try} that can be either {@link Try.Success} (and empty) or {@link Try.Failure} and contain an
     * exception.
     * @see Try#onFailure(Consumer)
     */
    public Try<Void> openUrl(final String url) {
        return Try.of(() -> url)
                  .mapTry(URL::new)
                  .flatMap(this::openUrl)
                  .onFailure(cause -> this.onException(cause, url));
    }

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     *
     * @param url The URL as an {@link URL}.
     *
     * @return a {@link Try} that can be either {@link Try.Success} (and empty) or {@link Try.Failure} and contain an
     * exception.
     * @see Try#onFailure(Consumer)
     */
    public Try<Void> openUrl(final URL url) {
        return Try.of(() -> url)
                  .mapTry(URL::toURI)
                  .flatMap(this::browse)
                  .onFailure(cause -> this.onException(cause, Objects.toString(url)));
    }

    private Try<Void> browse(final URI uri) {
        return Try.run(() -> Desktop.getDesktop().browse(uri))
                  .onFailure(cause -> onException(cause, uri.toString()));
    }

    private void onException(final Throwable cause, final String url) {
        ExceptionHandler.displayExceptionPane(
            "Browser error",
            "We could not open the following url in the browser : " + url,
            cause
        );
    }
}
