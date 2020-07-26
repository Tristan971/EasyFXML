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

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;

/**
 * This class is for classpath-based files simpler access (i.e. resources).
 */
public final class Resources {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resources.class);

    // Matches strings ending with / or \
    private static final Pattern DIRECTORY_SUFFIX_PATTERN = Pattern.compile(".*([/\\\\])$");

    private static final String CLASSPATH_FILES_PATTERN = "classpath*:%s*";
    public static final PathMatchingResourcePatternResolver PATH_MATCHING_RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

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
        return Try.of(() -> new ClassPathResource(resourceRelativePath))
                  .filter(ClassPathResource::exists)
                  .map(ClassPathResource::getPath)
                  .map(Paths::get);
    }

    private static URL getResourcesRootURL() {
        return Resources.class.getClassLoader().getResource(".");
    }

    /**
     * This method gets the {@link URL} associated to a classpath-located file.
     *
     * @param resourceRelativePath The path from the root of the classpath (target/classes/ in a maven project)
     *
     * @return The url associated with resource at said relative path to classpath.
     */
    public static Try<URL> getResourceURL(final String resourceRelativePath) {
        return Try.of(() -> new ClassPathResource(resourceRelativePath))
                  .mapTry(ClassPathResource::getURL);
    }

    private static URL getBaseURL() {
        return Resources.class.getClassLoader().getResource(".");
    }

    /**
     * Attempts to (non-recursively) list classpath resources at a given directory path.
     *
     * @param directory the {@link Path} of the directory within which to list resources. If it does not end with {@link File#separator}, it will automatically
     *                  be appended.
     *
     * @return an {@link Either} whose left projection contains failures (exceptions) encountered during resolution, and whose right contains the resolved
     * {@link Path}s of each individual resource found.
     *
     * @throws RuntimeException as per {@link PathMatchingResourcePatternResolver#getResources(String)} failure semantics.
     */
    public static Either<Seq<Throwable>, Seq<Path>> listFiles(Path directory) {
        String directoryPath = directory.toString();
        if (!DIRECTORY_SUFFIX_PATTERN.matcher(directoryPath).matches()) {
            directoryPath += File.separator;
        }

        LOGGER.info("Looking for files inside classpath at path: [{}]", directoryPath);

        String pathPattern = String.format(CLASSPATH_FILES_PATTERN, directoryPath);

        Resource[] resources = Try
            .of(() -> PATH_MATCHING_RESOURCE_RESOLVER.getResources(pathPattern))
            .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);

        return Either.sequence(
            Arrays
                .stream(resources)
                .map(resource -> Try
                    .of(() -> resource)
                    .mapTry(Resource::getFile)
                    .filter(File::isFile)
                    .map(File::toPath)
                    .toEither()
                ).collect(Collectors.toList())
        );
    }

}
