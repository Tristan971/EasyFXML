package moe.tristan.easyfxml.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.concurrent.CompletionStage;

/**
 * Utility class that provides convenience methods over {@link Pane}-based components.
 */
public final class Panes {

    private Panes() {
    }

    /**
     * Sets as the sole content of a pane another Node. This is supposed to work as having a first Pane being the wanted
     * display zone and the second Node the displayed content.
     *
     * @param parent The container defining the displayable zone, as a {@link Pane}.
     * @param content   The content to display
     * @param <T>       The subtype if necessary of the container
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
    public static <T extends Pane> CompletionStage<T> setContent(final T parent, final Node content) {
        return FxAsync.doOnFxThread(parent, parentNode -> {
            parentNode.getChildren().clear();
            parentNode.getChildren().add(content);
        });
    }
}
