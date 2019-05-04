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
