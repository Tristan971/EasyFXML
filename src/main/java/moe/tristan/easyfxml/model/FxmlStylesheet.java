package moe.tristan.easyfxml.model;

@FunctionalInterface
public interface FxmlStylesheet {
    FxmlStylesheet NONE = () -> "";

    /**
     * @return the CSS content that composes the stylesheet
     */
    String getCssContent();
}
