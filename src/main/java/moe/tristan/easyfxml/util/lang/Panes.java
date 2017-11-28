package moe.tristan.easyfxml.util.lang;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import moe.tristan.easyfxml.util.FxAsyncUtils;

import java.util.concurrent.CompletionStage;

public final class Panes {

    private Panes() {}

    public static <T extends Pane> CompletionStage<T> setContent(final T container, final Node content) {
        return FxAsyncUtils.doOnFxThread(container, _container -> {
            _container.getChildren().clear();
            _container.getChildren().add(content);
        });
    }
}
