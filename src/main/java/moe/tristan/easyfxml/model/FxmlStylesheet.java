package moe.tristan.easyfxml.model;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.beanmanagement.StylesheetCache;

public interface FxmlStylesheet {
    /**
     * @return the CSS content that composes the stylesheet
     */
    String getFilePath();
    
    /**
     * Returns the content of the file represented by {@link #getFilePath()}
     */
    default Try<String> getContentOfSheet() {
        return StylesheetCache.getInstance().tryLoadContent(this);
    }
}
