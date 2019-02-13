package moe.tristan.easyfxml.model.system;

import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.HostServices;

import moe.tristan.easyfxml.model.exception.ExceptionHandler;

import io.vavr.control.Try;

/**
 * This class contains some utility methods to open URLs with the default web browser of the user.
 */
@Component
public class BrowserSupport {

    private final HostServices hostServices;

    @Autowired
    public BrowserSupport(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     *
     * @param url The URL as a String. Does not have to conform to {@link URL#URL(String)}.
     *
     * @return a {@link Try} that can be either {@link Try.Success} (and empty) or {@link Try.Failure} and contain an
     * exception.
     *
     * @see Try#onFailure(Consumer)
     */
    public Try<Void> openUrl(final String url) {
        return Try.run(() -> hostServices.showDocument(url))
                  .onFailure(cause -> onException(cause, url));
    }

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     *
     * @param url The URL as an {@link URL}.
     *
     * @return a {@link Try} that can be either {@link Try.Success} (and empty) or {@link Try.Failure} and contain an
     * exception.
     *
     * @see Try#onFailure(Consumer)
     */
    public Try<Void> openUrl(final URL url) {
        return Try.of(() -> url)
                  .mapTry(URL::toURI)
                  .map(URI::toString)
                  .flatMap(this::openUrl);
    }

    private void onException(final Throwable cause, final String url) {
        ExceptionHandler.displayExceptionPane(
            "Browser error",
            "We could not open the following url in the browser : " + url,
            cause
        );
    }

}
