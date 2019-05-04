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

import javafx.beans.binding.BooleanExpression;

import moe.tristan.easyfxml.api.FxmlController;

public interface ComponentCellFxmlController<T> extends FxmlController {

    void updateWithValue(final T newValue);

    /**
     * @param selected a property watching whether this list view cell is currently selected. You can listen on it and react accordingly.
     */
    default void selectedProperty(final BooleanExpression selected) {
    }

}
