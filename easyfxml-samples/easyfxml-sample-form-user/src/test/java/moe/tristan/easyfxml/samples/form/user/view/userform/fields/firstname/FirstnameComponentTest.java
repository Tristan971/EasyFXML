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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname;

import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameController.ERROR_EMPTY_PROVIDED;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameController.ERROR_NAME_INVALID_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.SpringBootComponentTest;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
public class FirstnameComponentTest extends SpringBootComponentTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private FirstnameComponent firstnameComponent;

    private Pane firstnamePane;
    private FirstnameController firstnameController;

    @BeforeEach
    public void setUp() {
        final FxmlLoadResult<Pane, FirstnameController> load = easyFxml.load(firstnameComponent, Pane.class, FirstnameController.class);
        firstnamePane = load.getNodeOrExceptionPane();
        firstnameController = load.getController().get();
    }

    @Test
    public void checkValidFirstname() {
        final String validFirstname = "Tristan";

        withNodes(firstnamePane)
            .willDo(() -> clickOn("#firstNameField").write(validFirstname))
            .run();

        assertThat(!lookup("#invalidLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Error label is expected to not be visible on valid name!")
            .isTrue();
    }

    @Test
    public void checkEmptyFirstname() {
        withNodes(firstnamePane)
            .willDo(() -> clickOn("#firstNameField"))
            .run();

        assertThat(firstnameController.isValid())
            .withFailMessage("Expected empty name to be invalid.")
            .isFalse();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Expected invalid name to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).getText())
            .isEqualTo(ERROR_EMPTY_PROVIDED);
    }

    @Test
    public void checkInvalidFirstname() {
        final String invalidFirstname = "Tristan2";

        withNodes(firstnamePane)
            .willDo(() -> clickOn("#firstNameField").write(invalidFirstname))
            .run();

        assertThat(firstnameController.isValid())
            .withFailMessage("Expected name that does not follow pattern to be invalid.")
            .isFalse();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Expected name that does not follow pattern to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#invalidLabel").queryAs(Label.class).getText())
            .isEqualTo(ERROR_NAME_INVALID_PATTERN);
    }

}
