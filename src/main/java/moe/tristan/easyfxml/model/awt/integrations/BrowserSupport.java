package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtRequired;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import static io.vavr.API.unchecked;
import static java.awt.Desktop.Action.*;

@Component
public class BrowserSupport implements AwtRequired {

    private final Desktop desktop;

    @Autowired
    public BrowserSupport(final Desktop desktop) {
        this.desktop = desktop;
    }

    public void openUrl(final String url) {
        Try.of(() -> url)
            .map(unchecked((CheckedFunction1<String, URL>) URL::new))
            .onSuccess(this::openUrl)
            .onFailure(cause -> this.onException(cause, url));
    }

    public void openUrl(final URL url) {
        Try.of(url::toURI)
            .onSuccess(this::browse)
            .onFailure(cause -> this.onException(cause, Objects.toString(url)));
    }

    @Override
    public boolean isSupported() {
        return this.desktop.isSupported(BROWSE);
    }

    private void onException(final Throwable cause, final String url) {
        ExceptionHandler.displayExceptionPane(
            "Browser error",
            "We could not open the following url in the browser : " + url,
            cause
        );
    }

    private void browse(final URI uri) {
        try {
            this.desktop.browse(uri);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
