package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtRequired;
import moe.tristan.easyfxml.model.exception.ExceptionPane;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import static io.vavr.API.unchecked;

@Component
public class BrowserSupport implements AwtRequired {

    private final Desktop desktop = Desktop.getDesktop();

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

    private void onException(final Throwable cause, final String url) {
        ExceptionPane.displayExceptionPane(
            "Browser error",
            "We could not open the following url in the browser : " + url,
            cause
        );
    }

    private void browse(final URI uri) {
        try {
            this.desktop.browse(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
