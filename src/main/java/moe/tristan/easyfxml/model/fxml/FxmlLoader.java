package moe.tristan.easyfxml.model.fxml;

import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class FxmlLoader extends FXMLLoader {
    private Consumer<Node> onSuccess = node -> {};
    private Consumer<Throwable> onFailure = cause -> {};

    public void setOnSuccess(final Consumer<Node> onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void onSuccess(final Node loadResult) {
        this.onSuccess.accept(loadResult);
    }

    public void setOnFailure(final Consumer<Throwable> onFailure) {
        this.onFailure = onFailure;
    }

    public void onFailure(final Throwable cause) {
        this.onFailure.accept(cause);
    }
}
