package moe.tristan.easyfxml.util;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;

public final class Buttons {

    private Buttons() {
    }

    public static void setOnClick(final Button button, final Runnable action) {
        button.setOnAction(e -> Platform.runLater(action));
    }

    public static void setOnClickWithNode(final Button button, final Node node, final Consumer<Node> action) {
        setOnClick(button, () -> action.accept(node));
    }

}
