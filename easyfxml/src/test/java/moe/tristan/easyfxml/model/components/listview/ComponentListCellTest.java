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

import static org.awaitility.Awaitility.await;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.junit.FxmlComponentTest;

public class ComponentListCellTest extends FxmlComponentTest {

    @Test
    public void updateItem() {
        final AtomicBoolean initialized = new AtomicBoolean(false);
        final BooleanProperty readProp = new SimpleBooleanProperty(false);
        final AtomicReference<String> value = new AtomicReference<>("");

        final Pane pane = new Pane();
        final ComponentCellFxmlController<String> clvcc = new ComponentCellFxmlController<>() {
            @Override
            public void updateWithValue(String newValue) {
                value.set(newValue);
            }

            @Override
            public void selectedProperty(final BooleanExpression selected) {
                readProp.bind(selected);
            }

            @Override
            public void initialize() {
                if (initialized.get()) {
                    throw new IllegalStateException("Double init!");
                }
                initialized.set(true);
            }
        };

        final TestListCell testListViewCell = new TestListCell(pane, clvcc);

        testListViewCell.updateItem("TEST", false);
        await().until(() -> value.get().equals("TEST"));

        testListViewCell.updateItem("TEST2", false);
        await().until(() -> value.get().equals("TEST2"));

        testListViewCell.updateItem(null, true);
        await().until(() -> value.get() == null);
    }

    public static class TestListCell extends ComponentListCell<String> {

        public TestListCell(Pane cellNode, ComponentCellFxmlController<String> controller) {
            super(cellNode, controller);
        }

    }

}
