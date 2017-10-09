package moe.tristan.easyfxml.model.fxml;

import javafx.fxml.FXMLLoader;

import java.util.function.Consumer;

public class FxmlLoader extends FXMLLoader {
    private Runnable onSuccess = () -> {};
    private Consumer<Throwable> onFailure = cause -> {};

    public void setOnSuccess(final Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void onSuccess(final Object loadResult) {
        this.onSuccess.run();
    }

    public void setOnFailure(final Consumer<Throwable> onFailure) {
        this.onFailure = onFailure;
    }

    public void onFailure(final Throwable cause) {
        this.onFailure.accept(cause);
    }
}
