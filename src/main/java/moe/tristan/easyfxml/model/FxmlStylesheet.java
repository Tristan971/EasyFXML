package moe.tristan.easyfxml.model;

import io.vavr.control.Try;
import moe.tristan.easyfxml.util.PathUtils;

import java.nio.file.Files;

public interface FxmlStylesheet {
    /**
     * @return the CSS content that composes the stylesheet
     */
    String getFilePath();

    /**
     * Returns the content of the file represented by {@link #getFilePath()}
     */
    default Try<String> getContentOfSheet() {
        return PathUtils.getPathForResource(this.getFilePath())
            .mapTry(Files::readAllBytes)
            .map(String::new);
    }
}
