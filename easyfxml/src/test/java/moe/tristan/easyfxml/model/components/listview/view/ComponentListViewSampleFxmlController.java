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

package moe.tristan.easyfxml.model.components.listview.view;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import moe.tristan.easyfxml.model.components.listview.ComponentListViewFxmlController;
import moe.tristan.easyfxml.model.components.listview.cell.ComponentListCellSample;

@Component
public class ComponentListViewSampleFxmlController extends ComponentListViewFxmlController<String> {

    private final ObservableList<String> values = FXCollections.observableArrayList();
    public final BooleanProperty scrolledToEnd = new SimpleBooleanProperty(false);

    public ComponentListViewSampleFxmlController(ConfigurableApplicationContext applicationContext) {
        super(applicationContext, ComponentListCellSample.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        listView.itemsProperty().bind(new ReadOnlyListWrapper<>(values));
    }

    public void addValue(final String value) {
        values.add(value);
    }

    @Override
    protected void onScrolledToEndOfListView() {
        scrolledToEnd.setValue(true);
    }

}
