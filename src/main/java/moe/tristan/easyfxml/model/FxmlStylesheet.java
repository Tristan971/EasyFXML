package moe.tristan.easyfxml.model;

@FunctionalInterface
public interface FxmlStylesheet {
    /**
     * @return the CSS content that composes the stylesheet
     */
    String getCssContent();

    FxmlStylesheet NONE = () -> "";
}
