package moe.tristan.easyfxml.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Small DOM-related utils for JavaFX nodes.
 */
public final class Nodes {

    private Nodes() {
    }

    /**
     * Centers a node that is inside an enclosing {@link AnchorPane}.
     *
     * @param node       The node to center
     * @param marginSize The margins to keep on the sides
     */
    public static void centerNode(final Node node, final Double marginSize) {
        AnchorPane.setTopAnchor(node, marginSize);
        AnchorPane.setBottomAnchor(node, marginSize);
        AnchorPane.setLeftAnchor(node, marginSize);
        AnchorPane.setRightAnchor(node, marginSize);
    }

    public static void hideAndResizeParentIf(
        final Node node,
        final ObservableValue<? extends Boolean> condition
    ) {
        autoresizeContainerOn(node, condition);
        bindContentBiasCalculationTo(node, condition);
    }

    public static void autoresizeContainerOn(
        final Node node,
        final ObservableValue<?> observableValue
    ) {
        observableValue.addListener((observable, oldValue, newValue) -> node.autosize());
    }

    public static void bindContentBiasCalculationTo(
        final Node node,
        final ObservableValue<? extends Boolean> observableValue
    ) {
        node.visibleProperty().bind(observableValue);
        node.managedProperty().bind(node.visibleProperty());
    }

}
