package moe.tristan.easyfxml.model.fxml;

import java.io.Serializable;

import moe.tristan.easyfxml.api.FxmlNode;

public class FxmlNodeLoadException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5375149649340364076L;

    public FxmlNodeLoadException(FxmlNode node, Throwable cause) {
        super("Could not load node " + node + " !", cause);
    }

}
