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

import static javafx.scene.input.MouseButton.PRIMARY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import moe.tristan.easyfxml.junit.SpringBootComponentTest;

public class ButtonsTest extends SpringBootComponentTest {

    @Test
    public void setOnClick() {
        final AtomicBoolean success = new AtomicBoolean(false);

        final Button testButton = new Button("TEST_BUTTON");
        Buttons.setOnClick(testButton, () -> success.set(true));

        withNodes(testButton)
            .startWhen(() -> point(testButton).query() != null)
            .willDo(() -> clickOn(testButton, PRIMARY))
            .andAwaitFor(success::get);

        assertThat(success).isTrue();
    }

    @Test
    public void setOnClickWithNode() {
        final Button testButton = new Button("Test button");
        final Label testLabel = new Label("Test label");

        Buttons.setOnClickWithNode(testButton, testLabel, label -> label.setVisible(false));

        withNodes(testButton, testLabel)
            .startWhen(() -> point(testButton).query() != null)
            .willDo(() -> clickOn(testButton, PRIMARY))
            .andAwaitFor(() -> !testLabel.isVisible());

        assertThat(testLabel.isVisible()).isFalse();
    }

}
