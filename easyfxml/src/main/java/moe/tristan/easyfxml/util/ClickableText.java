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

import java.util.Objects;
import java.util.function.Consumer;

import javafx.scene.text.Text;

public final class ClickableText extends Text {

    public static final String CLICKABLE_HYPERLINK_STYLE_CLASS = "clickable-hyperlink";

    public <T> ClickableText(final T element, final Runnable onClicked) {
        super(String.valueOf(Objects.requireNonNull(element)));
        setOnMouseClicked(e -> onClicked.run());
        getStyleClass().setAll(CLICKABLE_HYPERLINK_STYLE_CLASS);
    }

    public <T> ClickableText(final T element, final Consumer<T> onClicked) {
        this(element, () -> onClicked.accept(element));
    }

}
