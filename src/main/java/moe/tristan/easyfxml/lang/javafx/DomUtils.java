package moe.tristan.easyfxml.lang.javafx;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public final class DomUtils {

    private DomUtils() {}

    public static void centerNode(final Node node, final Double marginSize) {
        AnchorPane.setTopAnchor(node, marginSize);
        AnchorPane.setBottomAnchor(node, marginSize);
        AnchorPane.setLeftAnchor(node, marginSize);
        AnchorPane.setRightAnchor(node, marginSize);
    }
}
