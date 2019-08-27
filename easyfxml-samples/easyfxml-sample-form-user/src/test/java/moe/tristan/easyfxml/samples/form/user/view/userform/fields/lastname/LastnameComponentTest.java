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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.lastname;

import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.lastname.LastnameController.ERROR_EMPTY_PROVIDED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.FxmlComponentTest;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LastnameComponentTest extends FxmlComponentTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private LastnameComponent lastnameComponent;

    private Pane lastnamePane;
    private LastnameController lastnameController;

    @Before
    public void setUp() {
        final FxmlLoadResult<Pane, LastnameController> load = easyFxml.load(lastnameComponent, Pane.class, LastnameController.class);
        lastnamePane = load.getNodeOrExceptionPane();
        lastnameController = load.getController().get();
    }

    @Test
    public void checkValidLastname() {
        final String validLastname = "Deloche";

        withNodes(lastnamePane)
            .willDo(() -> clickOn("#lastNameField").write(validLastname))
            .run();

        assertThat(!lookup("#invalidLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Error label is expected to not be visible on valid name!")
            .isTrue();
    }

    @Test
    public void checkEmptyLastname() {
        withNodes(lastnamePane)
            .willDo(() -> clickOn("#lastNameField"))
            .run();

        assertThat(lastnameController.isValid())
            .withFailMessage("Expected empty name to be invalid.")
            .isFalse();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Expected invalid name to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).getText())
            .isEqualTo(ERROR_EMPTY_PROVIDED);
    }

}
