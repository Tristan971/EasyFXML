package moe.tristan.easyfxml.model;

/**
 * Base class to implement for your fxml file equivalents.
 * Implementation in an enum is the recommended way to use it.
 */
public interface FxmlFile {
    /**
     * @return the path relative to the classpath root (/target/classes in Maven's default model) as a {@link String}.
     */
    String getFxmlFilePath();
}
