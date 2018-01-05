package moe.tristan.easyfxml.model.styles;

import io.vavr.control.Try;
import moe.tristan.easyfxml.util.Paths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class CachedFileBasedFxmlStylesheet extends CachedFxmlStylesheet {

    private final Path stylesheetFilePath;

    public CachedFileBasedFxmlStylesheet(Path stylesheetFilePath) {
        this.stylesheetFilePath = stylesheetFilePath;
    }

    public CachedFileBasedFxmlStylesheet(String resourcesRelativePath) {
        this(Paths.getPathForResource(resourcesRelativePath).get());
    }

    @Override
    protected String loadStylesheet() {
        return Try.of(() -> stylesheetFilePath)
            .mapTry(Files::readAllBytes)
            .map(String::new)
            .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
    }
}
