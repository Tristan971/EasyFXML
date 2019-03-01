package moe.tristan.easyfxml.model.components.listview;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.components.listview.cell.ComponentCellFxmlSampleController;
import moe.tristan.easyfxml.model.components.listview.view.ComponentListViewSampleFxmlController;

public enum CustomListViewTestComponents implements FxmlNode {
    VIEW("view/ComponentListViewSample.fxml", ComponentListViewSampleFxmlController.class),
    CELL("cell/ComponentCell.fxml", ComponentCellFxmlSampleController.class);

    private final String relFileName;
    private final Class<? extends FxmlController> controllerClass;

    CustomListViewTestComponents(String relFileName, Class<? extends FxmlController> controllerClass) {
        this.relFileName = relFileName;
        this.controllerClass = controllerClass;
    }

    @Override
    public FxmlFile getFile() {
        return () -> "moe/tristan/easyfxml/model/components/listview/" + relFileName;
    }

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return controllerClass;
    }
}
