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

import java.util.concurrent.CompletionStage;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Utility class that provides convenience methods over {@link Pane}-based components.
 */
public final class Panes {

    private Panes() {
    }

    /**
     * Sets as the sole content of a pane another Node. This is supposed to work as having a first Pane being the wanted
     * display zone and the second Node the displayed content.
     *
     * @param parent  The container defining the displayable zone, as a {@link Pane}.
     * @param content The content to display
     * @param <T>     The subtype if necessary of the container
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation
     */
    public static <T extends Pane> CompletionStage<T> setContent(final T parent, final Node content) {
        return FxAsync.doOnFxThread(parent, parentNode -> {
            parentNode.getChildren().clear();
            parentNode.getChildren().add(content);
        });
    }

}
