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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.email;

import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.email.EmailController.ERROR_EMPTY_EMAIL;
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
import moe.tristan.easyfxml.junit.FxNodeTest;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private EmailComponent emailComponent;

    private Pane emailPane;
    private EmailController emailController;

    @Before
    public void setUp() {
        final FxmlLoadResult<Pane, EmailController> load = easyFxml.loadNode(emailComponent, Pane.class, EmailController.class);
        emailPane = load.getNodeOrExceptionPane();
        emailController = load.getController().get();
    }

    @Test
    public void checkValidEmail() {
        final String validEmail = "tristandeloche@nospamthx.com";

        withNodes(emailPane)
            .willDo(() -> clickOn("#emailField").write(validEmail))
            .run();

        assertThat(!lookup("#errorLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Error label is expected to not be visible on valid email!")
            .isTrue();
    }

    @Test
    public void checkEmptyEmail() {
        withNodes(emailPane)
            .willDo(() -> clickOn("#emailField"))
            .run();

        assertThat(emailController.isValid())
            .withFailMessage("Expected empty email to be invalid.")
            .isFalse();

        assertThat(lookup("#errorLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Expected invalid email to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#errorLabel").queryAs(Label.class).getText())
            .isEqualTo(ERROR_EMPTY_EMAIL);
    }

    @Test
    public void checkInvalidEmail() {
        final String noDomainEmail = "tristandeloche";

        withNodes(emailPane)
            .willDo(() -> clickOn("#emailField").write(noDomainEmail))
            .run();

        assertThat(emailController.isValid())
            .withFailMessage("Expected email that does not follow valid pattern to be invalid.")
            .isFalse();

        assertThat(lookup("#errorLabel").queryAs(Label.class).isVisible())
            .withFailMessage("Expected email that does not follow pattern to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#errorLabel").queryAs(Label.class).getText())
            .contains("domain");
    }

}
