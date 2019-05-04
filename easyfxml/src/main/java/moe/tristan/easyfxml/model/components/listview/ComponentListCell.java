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

package moe.tristan.easyfxml.model.components.listview;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

public abstract class ComponentListCell<T> extends ListCell<T> {

    protected final Pane cellNode;
    protected final ComponentCellFxmlController<T> cellController;

    public ComponentListCell(final EasyFxml easyFxml, final FxmlNode cellNode) {
        this(easyFxml.loadNode(
            cellNode,
            Pane.class,
            ComponentCellFxmlController.class
        ));
    }

    @SuppressWarnings("unchecked")
    public ComponentListCell(final FxmlLoadResult<Pane, ComponentCellFxmlController> loadResult) {
        this(loadResult.getNode().get(), loadResult.getController().get());
    }

    public ComponentListCell(final Pane cellNode, final ComponentCellFxmlController<T> controller) {
        this.cellNode = cellNode;
        this.cellController = controller;
    }

    @Override
    protected void updateItem(final T item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
            setVisible(false);
            setManaged(false);
        } else {
            setVisible(true);
            setManaged(true);
        }
        Platform.runLater(() -> {
            cellController.updateWithValue(item);
            cellController.selectedProperty(selectedProperty());
            if (getGraphic() == null) {
                setGraphic(cellNode);
            }
        });
    }

}
