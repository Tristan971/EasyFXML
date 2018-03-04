package moe.tristan.easyfxml.util;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This class is for classpath-based files simpler access (i.e. resources).
 */
public final class Resources {

    private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

    private Resources() {
    }

    /**
     * This method gets the {@link Path} associated to a classpath-located file.
     *
     * @param resourceRelativePath The path from the root of the classpath (target/classes/ in a maven project)
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<Path> getResourcePath(final String resourceRelativePath) {
        return Try.of(Resources::getResourcesRootURL)
            .mapTry(URL::toURI)
            .mapTry(Paths::get)
            .map(resPath -> {
                final String actualPath = resPath.toAbsolutePath().toString() + File.separator + resourceRelativePath;
                LOG.debug("Trying to load file path : {}", actualPath);
                try {
                    return Paths.get(actualPath).toRealPath();
                } catch (IOException e) {
                    throw new RuntimeException("Could not load file at " + actualPath, e);
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
     * @return The path associated with resource at said relative path to classpath.
     */
    public static Try<URL> getResourceURL(final String resourceRelativePath) {
        return Try.of(Resources.class::getClassLoader)
            .map(cl -> cl.getResource(resourceRelativePath));
    }

    /**
     * Returns a stream from the files in the given directory. Simple wrapper around {@link DirectoryStream}.
     *
     * @param directory The directory to iterate over
     * @return A stream of the files under the given directory or an empty stream if the {@link Path} was not a
     * directory.
     */
    public static Try<List<Path>> listFiles(final Path directory) {
        return Try.of(() -> Files.newDirectoryStream(directory))
            .map(ds -> StreamSupport.stream(ds.spliterator(), false))
            .map(ps -> ps.collect(Collectors.toList()));
    }
}
