package moe.tristan.easyfxml.model.fxml;

import javafx.fxml.FXMLLoader;

import java.util.function.Consumer;

public class FxmlLoader extends FXMLLoader {
    private Runnable onSuccess = () -> {};
    private Consumer<Throwable> onFailure = cause -> {};

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void onSuccess(Object loadResult) {
        this.onSuccess.run();
    }

    public void onFailure(final Throwable cause) {
        this.onFailure.accept(cause);
    }
}
