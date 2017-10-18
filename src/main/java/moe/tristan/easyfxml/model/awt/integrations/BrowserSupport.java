package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtUtils;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.unchecked;

@Component
public class BrowserSupport {

    public void openUrl(final String url) {
        Try.of(() -> url)
            .map(unchecked((CheckedFunction1<String, URL>) URL::new))
            .onSuccess(this::openUrl)
            .onFailure(cause -> this.onException(cause, url));
    }

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

        browserOpeningResult.thenAccept(result ->
            result.onFailure(cause -> onException(cause, uri.toString()))
        );
    }
}
