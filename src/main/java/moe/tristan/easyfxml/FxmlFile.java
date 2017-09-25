package moe.tristan.easyfxml;

/**
 * Base class to implement for your fxml file equivalents.
 * Using an enum is the recommended way here.
 */
public interface FxmlFile {
    /**
     * @return the path relative to the classpath root (/target/classes in Maven's default model) as a {@link String}.
     */
    String getPath();
}
