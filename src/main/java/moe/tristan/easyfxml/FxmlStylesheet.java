package moe.tristan.easyfxml;

public interface FxmlStylesheet {
    /**
     * @return Gives the path to the CSS file that is describing the stylesheet
     */
    String getPath();

    FxmlStylesheet NONE = () -> null;
}
