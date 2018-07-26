package moe.tristan.easyfxml.model.components.listview;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

public abstract class ComponentListCell<T> extends ListCell<T> {

    protected final Pane cellNode;
    protected final ComponentCellFxmlController<T> cellController;

    @SuppressWarnings("unchecked")
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
