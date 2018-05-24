package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlStylesheet;

/**
 * This class contains a few constants for default {@link FxmlStylesheet} implementations.
 */
public final class FxmlStylesheets {

    private FxmlStylesheets() {
        throw new UnsupportedOperationException("Utility class, should not be instanciated.");
    }

    public static final FxmlStylesheet DEFAULT_JAVAFX_STYLE = () -> null;

}
