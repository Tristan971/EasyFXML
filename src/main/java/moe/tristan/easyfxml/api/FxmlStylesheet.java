package moe.tristan.easyfxml.api;

public interface FxmlStylesheet {
    FxmlStylesheet INHERIT = () -> "INHERIT_PARENT";
    FxmlStylesheet DEFAULT = () -> "USE_DEFAULT_JAVAFX_STYLE";

    /**
     * @return the CSS content that composes the stylesheet
     */
    String getStyle();
}
