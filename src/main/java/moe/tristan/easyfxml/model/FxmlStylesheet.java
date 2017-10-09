package moe.tristan.easyfxml.model;

import io.vavr.control.Option;
import io.vavr.control.Try;
import moe.tristan.easyfxml.util.PathUtils;

import java.nio.file.Files;

public interface FxmlStylesheet {
    /**
     * @return the CSS content that composes the stylesheet
     */
    String getCssContent();

    /**
     * Returns a FxmlStylesheet that loads content of resource file given.
     * @param filePath just like with {@link PathUtils#getPathForResource(String)}
     * @return The needed stylesheet wraper for {@link FxmlNode}.
     */
    static Option<FxmlStylesheet> ofResourceFile(final String filePath) {
        final Try<FxmlStylesheet> loadResult = PathUtils.getPathForResource(filePath)
            .mapTry(Files::readAllBytes)
            .map(String::new)
            .map(content -> (FxmlStylesheet) () -> content);
        return loadResult.toOption();
    }
}
