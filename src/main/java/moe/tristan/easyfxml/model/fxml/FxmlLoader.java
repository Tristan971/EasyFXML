package moe.tristan.easyfxml.model.fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.util.function.Consumer;

/**
 * Subclass of {@link FXMLLoader} to add post-load operations depending on success or failure.
 */
public class FxmlLoader extends FXMLLoader {

    private Consumer<Node> onSuccess = node -> {
    };

    private Consumer<Throwable> onFailure = cause -> {
    };

    /**
     * @param onSuccess the consumer for the node loaded on success
     */
    public void setOnSuccess(final Consumer<Node> onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * @param loadResult the node to feed {@link #onSuccess} with.
     */
    public void onSuccess(final Node loadResult) {
        this.onSuccess.accept(loadResult);
    }

    /**
     * @param onFailure the consumer for the loading error on failure
     */
    public void setOnFailure(final Consumer<Throwable> onFailure) {
        this.onFailure = onFailure;
    }

    /**
     * @param cause the reason to feed {@link #onFailure} with.
     */
    public void onFailure(final Throwable cause) {
        this.onFailure.accept(cause);
    }

}
