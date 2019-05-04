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

package moe.tristan.easyfxml.util;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;

public final class Buttons {

    private Buttons() {
    }

    public static void setOnClick(final Button button, final Runnable action) {
        button.setOnAction(e -> Platform.runLater(action));
    }

    public static void setOnClickWithNode(final Button button, final Node node, final Consumer<Node> action) {
        setOnClick(button, () -> action.accept(node));
    }

}
