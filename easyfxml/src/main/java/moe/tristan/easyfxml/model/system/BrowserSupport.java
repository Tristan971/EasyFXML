/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
