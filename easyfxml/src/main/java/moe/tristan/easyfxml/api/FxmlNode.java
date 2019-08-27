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

package moe.tristan.easyfxml.api;

import javafx.stage.Stage;

import moe.tristan.easyfxml.util.Stages;

/**
 * A {@link FxmlNode} is any element that has a graphical representation and a controller.
 * <p>
 * It is described by 2 elements :
 * <p>
 * - {@link FxmlFile} : The file which describes its DOM
 * <p>
 * - {@link FxmlController} class : The bean class to use as its controller (its logic)
 * <p>
 * While you might expect a separate stylesheet element to be related, it actually is not bound to the component but to the window in JavaFX. See {@link
 * Stages#setStylesheet(Stage, FxmlStylesheet)} for styling a window.
 */
public interface FxmlNode {

    FxmlFile getFile();

    Class<? extends FxmlController> getControllerClass();

}
