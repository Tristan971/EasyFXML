package moe.tristan.easyfxml.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.vavr.control.Try;

/**
 * This class is because Brian Goetz doesn't like you reading files from the classpath
 */
public final class Paths {

    private Paths() {
    }

    /**
     * This method gets the {@link Path} associated to a classpath-located file.
     *
     * @param resourceName The path from the root of the classpath (where resources are located, and used for them, thus
     *                     the name)
     *
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<Path> getPathForResource(final String resourceName) {
        return Try.of(Paths.class::getClassLoader)
                  .map(cl -> cl.getResource(resourceName))
                  .mapTry(URL::toURI)
                  .mapTry(java.nio.file.Paths::get);
    }

    /**
     * Returns a stream from the files in the given directory. Simple wrapper around {@link DirectoryStream}.
     *
     * @param directory The directory to iterate over
     *
     * @return A stream of the files under the given directory or an empty stream if the {@link Path} was not a
     * directory.
     * @throws IOException when accessing privileged paths with non-privileged user
     */
    public static List<Path> listFiles(final Path directory) throws IOException {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {
            return StreamSupport.stream(files.spliterator(), false)
                                .collect(Collectors.toList());
        }
    }
}
