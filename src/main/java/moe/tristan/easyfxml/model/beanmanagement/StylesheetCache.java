package moe.tristan.easyfxml.model.beanmanagement;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.FxmlStylesheet;
import moe.tristan.easyfxml.util.PathUtils;
import org.assertj.core.util.VisibleForTesting;

import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StylesheetCache {
    private static final StylesheetCache INSTANCE = new StylesheetCache();

    @VisibleForTesting
    protected final ConcurrentHashMap<FxmlStylesheet, String> cacheMap = new ConcurrentHashMap<>();

    @VisibleForTesting
    protected StylesheetCache() {}

    public static StylesheetCache getInstance() {
        return INSTANCE;
    }

    public Try<String> tryLoadContent(final FxmlStylesheet stylesheet) {
        return Try.of(() -> stylesheet)
            .map(this.cacheMap::get)
            .filter(Objects::nonNull)
            .recoverWith(reason -> this.readAndCacheFileContent(stylesheet));
    }

    private Try<String> readAndCacheFileContent(final FxmlStylesheet stylesheet) {
        return PathUtils.getPathForResource(stylesheet.getFilePath())
            .mapTry(Files::readAllBytes)
            .map(String::new)
            .onSuccess(content -> this.cacheMap.put(stylesheet, content));
    }
}
