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

package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlController;

/**
 * Empty controller for logicless components.
 * <p>
 * You can use it if your component does nothing more than display its content.
 */
public class NoControllerClass implements FxmlController {

    /**
     * Empty voluntarily as no logic is to be included in this class.
     */
    @SuppressWarnings("EmptyMethod")
    @Override
    public void initialize() {
        //see doc
    }

}
