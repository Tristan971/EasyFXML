package moe.tristan.easyfxml.model.components.listview;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

public abstract class CustomListViewCellBase<T> extends ListCell<T> {

    private final Pane cellNode;
    private final CustomListViewCellController<T> cellController;

    @SuppressWarnings("unchecked")
    public CustomListViewCellBase(final EasyFxml easyFxml, final FxmlNode cellNode) {
        this(easyFxml.loadNode(
                cellNode,
                Pane.class,
                CustomListViewCellController.class
        ));
    }

    @SuppressWarnings("unchecked")
    public CustomListViewCellBase(final FxmlLoadResult<Pane, CustomListViewCellController> loadResult) {
        this(loadResult.getNode().get(), loadResult.getController().get());
    }

    public CustomListViewCellBase(final Pane cellNode, final CustomListViewCellController<T> controller) {
        this.cellNode = cellNode;
        this.cellController = controller;
    }

    @Override
    protected void updateItem(final T item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
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
