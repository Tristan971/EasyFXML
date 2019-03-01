/*
 *     Lyrebird, a free open-source cross-platform twitter client.
 *     Copyright (C) 2017-2018, Tristan Deloche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
