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

package moe.tristan.easyfxml.util;

import static io.vavr.API.$;
import static io.vavr.API.Case;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.vavr.control.Try;

/**
 * This class is for classpath-based files simpler access (i.e. resources).
 */
public final class Resources {

    private Resources() {
    }

    /**
     * This method gets the {@link Path} associated to a classpath-located file.
     *
     * @param resourceRelativePath The path from the root of the classpath (target/classes/ in a maven project)
     *
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<Path> getResourcePath(final String resourceRelativePath) {
        return Try.of(Resources::getResourcesRootURL)
                  .mapTry(URL::toURI)
                  .mapTry(Paths::get)
                  .map(resPath -> {
                      try {
                          return Paths.get(getBaseURL().toURI()).resolve(resourceRelativePath).toRealPath();
                      } catch (IOException | URISyntaxException e) {
                          throw new IllegalArgumentException(
                              "Could not load file at " + getBaseURL() + resourceRelativePath,
                              e
                          );
                      }
                  });
    }

    private static URL getResourcesRootURL() {
        return Resources.class.getClassLoader().getResource(".");
    }

    /**
     * This method gets the {@link Path} associated to a classpath-located file.
     *
     * @param resourceRelativePath The path from the root of the classpath (target/classes/ in a maven project)
     *
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<URL> getResourceURL(final String resourceRelativePath) {
        final ClassLoader classLoader = Resources.class.getClassLoader();
        return Try.of(() -> classLoader)
                  .map(cl -> cl.getResource(resourceRelativePath))
                  .map(Objects::requireNonNull)
                  .mapFailure(
                      Case(
                          $(err -> err instanceof NullPointerException || err instanceof NoSuchFileException),
                          err -> new IllegalArgumentException(
                              "Error loading file at: " + getBaseURL().toExternalForm() + resourceRelativePath,
                              err
                          )
                      )
                  );
    }

    private static URL getBaseURL() {
        return Resources.class.getClassLoader().getResource(".");
    }

    /**
     * Returns a stream from the files in the given directory. Simple wrapper around {@link DirectoryStream}.
     *
     * @param directory The directory to iterate over
     *
     * @return A stream of the files under the given directory or an empty stream if the {@link Path} was not a
     * directory.
     */
    public static Try<List<Path>> listFiles(final Path directory) {
        return Try.of(() -> Files.newDirectoryStream(directory))
                  .map(ds -> StreamSupport.stream(ds.spliterator(), false))
                  .map(ps -> ps.collect(Collectors.toList()));
    }

}
