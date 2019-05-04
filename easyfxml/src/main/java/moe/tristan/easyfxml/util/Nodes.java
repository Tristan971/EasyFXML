/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
