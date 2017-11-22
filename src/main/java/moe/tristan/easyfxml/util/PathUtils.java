package moe.tristan.easyfxml.util;

import io.vavr.control.Try;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class is because Brian Goetz doesn't like you reading files from the classpath
 */
public final class PathUtils {

    private PathUtils() {}

    /**
     * This method gets the {@link Path} associated to a classpath-located file.
     *
     * @param resourceName The path from the root of the classpath
     *                     (where resources are located, and used for them, thus the name)
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<Path> getPathForResource(final String resourceName) {
        return Try.of(PathUtils.class::getClassLoader)
            .map(cl -> cl.getResource(resourceName))
            .mapTry(URL::toURI)
            .mapTry(Paths::get);
    }

    /**
     * Returns a stream from the files in the given directory.
     * Simple wrapper around {@link DirectoryStream}.
     *
     * @param directory The directory to iterate over
     * @return A stream of the files under the given directory or an empty stream if
     * the {@link Path} was not a directory.
     */
    public static List<Path> listFiles(final Path directory) throws IOException {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {
            return StreamSupport.stream(files.spliterator(), false)
                .collect(Collectors.toList());
        }
    }
}
