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

package moe.tristan.easyfxml.samples.helloworld.view.hello;

import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.FxNodeTest;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HelloComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private HelloComponent helloComponent;

    private Pane helloPane;

    @Before
    public void setUp() {
        this.helloPane = easyFxml.loadNode(helloComponent).getNode().getOrElseGet(ExceptionHandler::fromThrowable);
    }

    @Test
    public void shouldGreetWithUserEnteredName() {
        final String expectedUserName = "Tristan Deloche";

        withNodes(helloPane).willDo(() -> {
            clickOn("#userNameTextField").write(expectedUserName);
            clickOn("#helloButton");
        }).andAwaitFor(() -> lookup("#greetingBox").queryAs(HBox.class).isVisible());

        assertThat(lookup("#greetingName").queryAs(Label.class)).hasText(expectedUserName);
    }

    @Test
    public void shouldGreetWithHelloWorldWhenDidNotEnterName() {
        final String defaultGreetingName = "World";

        withNodes(helloPane)
            .willDo(() -> clickOn("#helloButton"))
            .andAwaitFor(() -> lookup("#greetingBox").query().isVisible());

        assertThat(lookup("#greetingName").queryAs(Label.class)).hasText(defaultGreetingName);
    }

}
