package moe.tristan.easyfxml.model.awt.integrations;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import org.springframework.stereotype.Component;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import static io.vavr.API.unchecked;
import moe.tristan.easyfxml.model.awt.AwtUtils;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;

/**
 * This class contains some utility methods to open URLs with the default web browser of the user.
 */
@Component
public class BrowserSupport {

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     * @param url The URL as a String. Must conform to {@link URL#URL(String)}.
     */
    public void openUrl(final String url) {
        Try.of(() -> url)
           .map(unchecked((CheckedFunction1<String, URL>) URL::new))
           .onSuccess(this::openUrl)
           .onFailure(cause -> this.onException(cause, url));
    }

    /**
     * Opens a given URL or a pop-up with the error if it could not do so.
     * @param url The URL as an {@link URL}.
     */
    public void openUrl(final URL url) {
        Try.of(() -> url)
           .map(unchecked(URL::toURI))
           .onSuccess(this::browse)
           .onFailure(cause -> this.onException(cause, Objects.toString(url)));
    }

    private void onException(final Throwable cause, final String url) {
        ExceptionHandler.displayExceptionPane(
            "Browser error",
            "We could not open the following url in the browser : " + url,
            cause
        );
    }

    private void browse(final URI uri) {
        final CompletionStage<Try<Void>> browserOpeningResult = AwtUtils.asyncAwtCallbackWithRequirement(
            Desktop::getDesktop,
            desktop -> Try.run(() -> desktop.browse(uri))
        );

        browserOpeningResult.thenAccept(
            result -> result.onFailure(cause -> onException(cause, uri.toString()))
        );
    }
}
