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

import moe.tristan.easyfxml.junit.SpringBootComponentTest;

public class ClickableTextTest extends SpringBootComponentTest {

    @Test
    public void shouldExecuteActionGiven() {
        final AtomicBoolean called = new AtomicBoolean();

        final ClickableText clickableText = new ClickableText("Sample text", () -> called.set(true));

        withNodes(clickableText)
            .willDo(() -> clickOn(clickableText, PRIMARY))
            .andAwaitFor(called::get);

        assertThat(called).isTrue();
    }

}
