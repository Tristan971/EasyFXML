package moe.tristan.easyfxml.util;

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

}
