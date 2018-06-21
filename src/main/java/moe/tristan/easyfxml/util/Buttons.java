package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public final class Buttons {

    private Buttons() {
    }

    public static void setOnClick(final Button button, final Runnable action) {
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> Platform.runLater(action));
    }

    public static void setOnClickWithNode(final Button button, final Node node, final Consumer<Node> action) {
        setOnClick(button, () -> action.accept(node));
    }

}
